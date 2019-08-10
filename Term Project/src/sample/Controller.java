//log in page
package sample;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Controller implements Initializable{
    public static String name ;
    @FXML
    private ImageView logo = new ImageView() ;
    Node node = (Node) logo ;
    @FXML
    TextField username = new TextField() ;
    @FXML
    PasswordField password = new PasswordField() ;
    @FXML
    Label Error = new Label() ;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    @FXML
    public void LogIn(ActionEvent event) throws Exception{
        File users = new File("src/users.txt") ;
        BufferedReader in = new BufferedReader(new FileReader(users)) ;
        name = username.getText().toString() ;
        String pass = password.getText().toString() ;
        String id = name+pass ;
        String src ;
        while ((src=in.readLine())!=null)
        {
            if(id.equals(src)){
                Parent root = FXMLLoader.load(getClass().getResource("sample4.fxml"));
                Scene scene = new Scene(root) ;
                Main.primaryStage.setScene(scene);
                FileWriter writer = new FileWriter(new File("src/OngoingChat/" + Controller.name + "OngoingChat.txt")) ;
                writer.write("");
                writer.flush();
                writer.close();
                break ;
            }
            else{
                Error.setText("Invalid Username or Password");
            }
        }
    }

    @FXML
    public void CreateNew(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        Scene scene = new Scene(root) ;
        Main.primaryStage.setScene(scene);
    }
}
