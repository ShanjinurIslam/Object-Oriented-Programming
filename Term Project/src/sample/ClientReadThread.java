package sample;

import FileWriterReader.CustomFileWriter;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Spondon on 29/11/2016.
 */
public class ClientReadThread implements Runnable{
    private Socket socket ;
    private ObjectInputStream in  ;
    private TextArea textArea ;
    private Thread thread ;
    private String SenderName ;
    ClientReadThread(Socket socket, ObjectInputStream in , TextArea textArea, String SenderName){
        this.socket = socket ;
        this.in = in ;
        this.textArea = textArea ;
        this.SenderName = SenderName ;
        this.thread = new Thread(this) ;
        thread.start();
    }
    @Override
    public void run() {
        try {
            while (true) {
                String[] s = (String[]) in.readObject();
                String words[] = s[0].split(" ",2) ;
                System.out.println(words[0]);
                if(words[0].equals(SenderName) && !s[1].equals("inchatBox") && !s[1].equals("In chat Box") && !s[1].equals("Knocked") && !s[1].equals("%#unreceived")){
                    textArea.appendText(s[0] + " :: " + s[1]+"\n");
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media("file:///Users/spondon/IdeaProjects/Final/src/MessageReceive.mp3")) ;
                    mediaPlayer.play();
                    CustomFileWriter customFileWriter = new CustomFileWriter(new File("src/ChatHistory/"+ Controller.name + "-"+ SenderName + ".txt")) ;
                    customFileWriter.write(s[0] + " :: " + s[1]+"\n" );
                    customFileWriter.close() ;
                }
                if(words[0].equals(Controller.name) && s[1].endsWith("Unreceived")){
                    int index = s[1].indexOf("Unreceived") ;
                    textArea.appendText(s[1].substring(0,index));
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media("file:///Users/spondon/IdeaProjects/Final/src/MessageReceive.mp3")) ;
                    mediaPlayer.play();
                    CustomFileWriter customFileWriter = new CustomFileWriter(new File("src/ChatHistory/"+ Controller.name + "-"+ SenderName + ".txt")) ;
                    customFileWriter.write(s[1] + "\n");
                    customFileWriter.close() ;
                }
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
