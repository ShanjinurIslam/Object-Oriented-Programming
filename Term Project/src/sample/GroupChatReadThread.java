package sample;

import javafx.scene.control.TextArea;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Spondon on 29/11/2016.
 */
public class GroupChatReadThread implements Runnable{
    private Socket socket ;
    private ObjectInputStream in  ;
    private TextArea textArea ;
    private Thread thread ;
    GroupChatReadThread(Socket socket, ObjectInputStream in , TextArea textArea){
        this.socket = socket ;
        this.in = in ;
        this.textArea = textArea ;
        this.thread = new Thread(this) ;
        thread.start();
    }
    @Override
    public void run() {
        try {
            while (true) {
                String[] s = (String[]) in.readObject();
                int i = s[0].indexOf("Group") ;
                s[0] = s[0].substring(0,i) ;
                textArea.appendText(s[0] + " :: " + s[1]+"\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            in.close();
            thread.stop();
            socket.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
