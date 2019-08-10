//Window Chat
package sample;

import FileWriterReader.CustomFileReader;
import FileWriterReader.CustomFileWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Spondon on 05/12/2016.
 */
public class Controller4 implements Initializable {
    Socket temSocket ;
    ObjectInputStream in ;
    ObjectOutputStream out ;
    public static String friendName = "" ;
    @FXML
    private TextArea OnlineList = new TextArea() ;
    @FXML
    TextArea chatRequests = new TextArea() ;
    @FXML
    private ChoiceBox<String> friendNames = new ChoiceBox<>() ;
    @FXML
    public void setStartChat(ActionEvent e) throws Exception{
        Stage stage = new Stage() ;
        friendName = friendNames.getValue() ;
        File file = new File("src/OngoingChat/" + Controller.name + "OngoingChat.txt") ;
        String targetString = Controller.name + "-" + friendName ;
        CustomFileWriter c =new CustomFileWriter(file) ;
        c.write(targetString);
        String words[] = new String[2] ;
        words[0] = Controller.name  ;
        words[1] = "@"+friendNames.getValue() +"-"+ "inchatBox" ;
        Object o = words ;
        out.writeObject(o);
        Parent root = FXMLLoader.load(getClass().getResource("sample3.fxml"));
        Scene scene = new Scene(root) ;
        stage.setScene(scene);
        stage.setTitle(Controller.name + "'s " + "Chat with " + friendName);
        stage.show();
    }

    @FXML
    public void refreshOnlineList(ActionEvent e) throws Exception{
        String words[] = new String[2] ;
        words[0] = Controller.name  ;
        words[1] = "@"+friendNames.getValue() +"-"+ "knocked" ;
        Object o = words ;
        out.writeObject(o);
        chatRequests.appendText("You knocked "+ friendNames.getValue()+ "\n");
    }

    @FXML
    public void lookForUnread(ActionEvent e) throws Exception{
        CustomFileReader customFileReader = new CustomFileReader(new File("src/Networking/Unreceived/" + Controller.name + "Notification" + ".txt")) ;
        String x = customFileReader.copy();
        chatRequests.appendText(x + "\n") ;
        FileWriter writer = new FileWriter(new File("src/Networking/Unreceived/" + Controller.name + "Notification" + ".txt")) ;
        writer.write("");
        writer.flush();
        writer.close();
    }
    @FXML
    public void setGroupChat(ActionEvent e) throws Exception{
        Stage stage = new Stage() ;
        Parent root = FXMLLoader.load(getClass().getResource("groupChat.fxml"));
        Scene scene = new Scene(root) ;
        stage.setScene(scene);
        stage.setTitle(Controller.name + "'s " +"GroupChat");
        stage.show();
    }
    @FXML
    public void logOut(ActionEvent e) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root) ;
        Main.primaryStage.setScene(scene);
        FileWriter writer = new FileWriter(new File("src/OngoingChat/" + Controller.name + "OngoingChat.txt")) ;
        writer.write("");
        writer.flush();
        writer.close();
        in.close();
        out.close();
        temSocket.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        try{
            temSocket = new Socket("localhost",15555) ;
            temSocket.setTcpNoDelay(true);
            out = new ObjectOutputStream(temSocket.getOutputStream()) ;
            in = new ObjectInputStream(temSocket.getInputStream()) ;
            new popUpThread(temSocket,in,Controller.name,chatRequests,OnlineList) ;
            String words[] = new String[2] ;
            words[0] = Controller.name   ;
            words[1] = "////" ;
            Object o = words ;
            out.writeObject(o);
            Main.primaryStage.setTitle(Controller.name + "'s SnapChat");
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/Online.txt"))) ;
            String tem ;
            String names[] = new String[100] ;
            int i = 0 ;
            while ((tem=reader.readLine())!=null){
                if(!tem.equals(Controller.name)){
                    names[i++] = tem ;
                }
            }
            i = 0 ;
            while (!names[i].equals("null")){
                friendNames.getItems().add(names[i]) ;
                i++ ;
            }
        }catch (Exception e){

        }
    }
}
