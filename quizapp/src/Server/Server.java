/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Class.Times;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Class.*;

/**
 *
 * @author TAB
 */
public class Server {
        public static int getStudent(String user, String pwd) {
        Student st = new Student();
        try {
            Connection connect = (Connection) MyConnection.getJDBCConnection();
            String sql = "SELECT * FROM SINHVIEN WHERE USERNAME ='" + user + "' AND PWD ='" + pwd + "' ";
            Statement statement = null;
            ResultSet rs = null;
            statement = connect.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) 
            {
                st.setId((String) rs.getString("USERNAME"));
                st.setName((String) rs.getNString("HOTEN"));
                st.setClas((String) rs.getString("LOP"));
            }
            rs.close();
            statement.close();
            connect.close();
            if (st.getId() == null) {
                return 1;
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static ArrayList<Question> getListQuestion() {
        try 
        {
            ArrayList<Question> list = new ArrayList<Question>();
            Connection connect = (Connection) MyConnection.getJDBCConnection();
            String sql = "SELECT TOP (20) * FROM dbo.CAUHOI INNER JOIN dbo.DAPAN ON DAPAN.ID_CAUHOI = CAUHOI.ID_CAUHOI ORDER BY NEWID()";
            Statement statement = null;
            ResultSet rs = null;
            statement = connect.createStatement();
            
            rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Question question = new Question();
                question.setQuestion(rs.getNString("CAUHOI"));
                question.setCorrectAnswer(rs.getNString("DAPAN"));
                
                ArrayList<String> answer = new ArrayList<>();
                answer.add(rs.getNString("A"));
                answer.add(rs.getNString("B"));
                answer.add(rs.getNString("C"));
                answer.add(rs.getNString("D"));
                question.setListAnswer(answer);
                list.add(question);
            }
            int sizeRs = list.size();
            return sizeRs == 0 ? null : list;
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1111);
        Times Time = null;
        ArrayList<Question> listQuestion = null;
        while (true)
        {
            Socket client = server.accept();
            DataInputStream din = new DataInputStream(client.getInputStream());
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
            String n = din.readUTF();
            switch (n) 
            {
                case "LOGIN": 
                {
                    String userName = din.readUTF();
                    String pwd = din.readUTF();
                    int result = getStudent(userName, pwd);
                    dout.writeInt(result);
                    break;
                }
                case "START": 
                {
                    Time = new Times();
                    Time.start();
                    listQuestion = getListQuestion();
                    if(listQuestion == null){
                        dout.writeInt(0);
                    }
                    else{
                        dout.writeInt(listQuestion.size());
                    }
                    for(Question question: listQuestion)
                    {
                        dout.writeUTF(question.getQuestion());
                        for(String answer: question.getListAnswer()){
                            dout.writeUTF(answer);
                        }
                    }
                    break;
                }
                case "FINISH": {
                    if (Time != null) {
                        Time.close();
                    }
                    int diem = 0;
                    for(int i = 0; i < listQuestion.size(); i++){
                        String answer = din.readUTF(); 
                        String correct = listQuestion.get(i).getCorrectAnswer();
                        if(answer.equalsIgnoreCase(correct)){
                            diem += 1;
                        }
                    }
                    dout.writeInt(diem);
                    break;
                }
            }
        }
    }
}
