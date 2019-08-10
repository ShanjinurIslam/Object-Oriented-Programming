//user UI page
package sample;

import java.awt.*;
import java.net.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.* ;

public class GroupChatController implements Initializable{
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
        words[1] = messages.getText() ;
        ChatHistory.appendText(words[0] + " :: " + messages.getText()+"\n");
        words[0] += "Group" ;
        Object o = words ;
        out.writeObject(o);
    }
    @FXML
    public void closeConnection(ActionEvent e) throws Exception{
        String words[] = new String[2] ;
        words[0] = Controller.name  ;
        words[1] = "Terminate" ;
        out.writeObject(words);
        clientSocket.close();
        Parent root = FXMLLoader.load(getClass().getResource("sample4.fxml"));
        Scene scene = new Scene(root) ;
        ChatHistory.appendText("Connection Closed. Please close the window");
        Main.primaryStage.setScene(scene);
    }

    public void initialize(URL url, ResourceBundle rb) {
        try {
            String para = Controller4.friendName ;
            System.out.println(para);
            clientSocket = new Socket("localhost", 15555);
            clientSocket.setTcpNoDelay(true);
            out = new ObjectOutputStream(clientSocket.getOutputStream()) ;
            in = new ObjectInputStream(clientSocket.getInputStream()) ;
            new GroupChatReadThread(clientSocket,in,ChatHistory) ;
            String words[] = new String[2] ;
            words[0] = Controller.name + "Group" ;
            words[1] = "////" ;
            Object o = words ;
            out.writeObject(o);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}

