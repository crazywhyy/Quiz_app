/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TAB
 */
public class MyConnection {
    public static Connection getJDBCConnection() {
    String username = "sa";
    String password = "123456";
    String url = "jdbc:sqlserver://localhost:1433;Database=GIUAKY;user=sa;password=123456";
    try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");   
        return DriverManager.getConnection(url,username,password);           
    } catch (ClassNotFoundException | SQLException ex) {
        Logger.getLogger(MyConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    return null;
    }
}
