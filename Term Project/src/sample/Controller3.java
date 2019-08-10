//user UI page
package sample;

import java.awt.*;
import java.net.*;
import java.util.ResourceBundle;

import FileWriterReader.CustomFileReader;
import FileWriterReader.CustomFileWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.* ;

public class Controller3 implements Initializable{
    public static String para ;
    @FXML
    TextArea ChatHistory = new TextArea();
    @FXML
    TextField messages = new TextField();
    private Socket clientSocket = null;
    private ObjectOutputStream out = null ;
    private ObjectInputStream in = null ;

    @FXML
    public void sendMessage(ActionEvent e) throws Exception{
        String words[] = new String[2] ;
        words[0] = Controller.name  ;
        words[1] = "@"+Controller4.friendName +"-"+ messages.getText() ;
        Object o = words ;
        ChatHistory.appendText(words[0] + " :: " + messages.getText()+"\n");
        CustomFileWriter customFileWriter = new CustomFileWriter(new File("src/ChatHistory/"+ Controller.name + "-"+ para + ".txt")) ;
        customFileWriter.write(words[0] + " :: " + messages.getText() + "\n");
        customFileWriter.close();
        messages.clear();
        out.writeObject(o);
    }

    @FXML
    public void previousChats(ActionEvent e) throws Exception{
        String target ;
        CustomFileWriter customFileWriter = new CustomFileWriter(new File("src/ChatHistory/"+ Controller.name + "-"+ para + ".txt")) ;
        customFileWriter.write(" ");
        customFileWriter.close();
        CustomFileReader customFileReader = new CustomFileReader(new File("src/ChatHistory/"+ Controller.name + "-"+ para + ".txt")) ;
        target = customFileReader.copy() ;
        System.out.println(target);
        ChatHistory.setText(target);
        customFileReader.close();
    }

    @FXML
    public void unreceivedChats(ActionEvent e) throws Exception{
        String words[] = new String[2] ;
        words[0] = Controller.name  ;
        words[1] = "@"+Controller4.friendName +"-"+ "%#unreceived" ;
        Object o = words ;
        out.writeObject(o);
    }
    @FXML
    public void closeChat(ActionEvent e) throws Exception {
        ChatHistory.appendText("You left the chat");
        String words[] = new String[2];
        words[0]= Controller.name;
        words[1] = "Terminate" ;
        out.writeObject(words);
        out.close();
        clientSocket.close();
    }


    public void initialize(URL url, ResourceBundle rb) {
        try {
            para = Controller4.friendName ;
            clientSocket = new Socket("localhost", 15555);
            clientSocket.setTcpNoDelay(true);
            out = new ObjectOutputStream(clientSocket.getOutputStream()) ;
            in = new ObjectInputStream(clientSocket.getInputStream()) ;
            new ClientReadThread(clientSocket,in,ChatHistory,para) ;
            String words[] = new String[2] ;
            words[0] = Controller.name  ;
            words[1] = "////" ;
            Object o = words ;
            out.writeObject(o);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}

