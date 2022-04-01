/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

import Client.Notif;
import java.util.logging.Level;
import java.util.logging.Logger;
import Client.Time;

public class Times extends Thread {

    Time time = null;

    public String checkTime(int a) {
        String time;
        if (a < 10) {
            time = "0" + a;
        } else {
            time = "" + a;
        }
        return time;
    }
    
    public void close() {
        if (time != null) {
            time.setVisible(false);
        }
        Thread.interrupted();

    }

    @Override
    public void run() {
        time = new Time();
        time.setVisible(true);
        String times;
        for (int i = 100; i >= 0; i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Times.class.getName()).log(Level.SEVERE, null, ex);
            }
            times = checkTime(i / 60) + ":" + checkTime(i % 60);
            time.setTextTime(times);
            if (i == 0) {
                Notif notification = new Notif();
                notification.setVisible(true);
            }
        }
        close();

    }
}

