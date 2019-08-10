package sample;

import FileWriterReader.CustomFileReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javax.management.Notification;
import java.io.* ;
import java.net.* ;

/**
 * Created by Spondon on 12/12/2016.
 */
public class popUpThread implements Runnable{
    private Socket socket ;
    private ObjectInputStream in ;
    private Thread t ;
    private String username ;
    private TextArea chatRequest ;
    private TextArea OnlineList ;

    public popUpThread(Socket socket, ObjectInputStream in, String username, TextArea chatRequest,TextArea OnlineList){
        this.socket = socket ;
        this.in = in ;
        this.username = username ;
        this.chatRequest = chatRequest ;
        this.OnlineList = OnlineList ;
        t = new Thread(this) ;
        t.start();
    }
    @Override
    public void run() {
        try{
            String tem = new String() ;
            while (true){
                String[] words = (String[]) in.readObject() ;
                System.out.println(words[1]);
                System.out.println(Controller.name+"-");
                int x = words[0].indexOf(" ") ;
                words[0]= words[0].substring(0,x) ;
                String target = username + "-" + words[0] ;
                File file = new File("src/OngoingChat/" + Controller.name + "OngoingChat.txt") ;
                CustomFileReader customFileReader = new CustomFileReader(file) ;
                if(words[1].equals("knocked")) {
                    if (customFileReader.isPresent(target) == true) {
                        System.out.println("present");
                    } else {
                        chatRequest.appendText(words[0] + " is wanting to have a chat with you\n");
                        Media media = new Media("file:///Users/spondon/IdeaProjects/Final/src/NotificationSound.mp3");
                        MediaPlayer mp = new MediaPlayer(media);
                        mp.play();
                        if(!tem.equals(words[0])){
                            OnlineList.appendText(words[0] + "\n");
                            tem = words[0] ;
                        }
                        System.out.println(tem);
                    }
                }

                if(words[1].equals("In chat Box")){
                    chatRequest.appendText(words[0] + " is in chat box, open chat box to start chat\n");
                    Media media = new Media("file:///Users/spondon/IdeaProjects/Final/src/NotificationSound.mp3");
                    MediaPlayer mp = new MediaPlayer(media);
                    mp.play();
                    if(!tem.equals(words[0])){
                        OnlineList.appendText(words[0] + "\n");
                        tem = words[0] ;
                    }
                    System.out.println(tem);
                }
            }
        }catch (Exception e){

        }
    }
}
