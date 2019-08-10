//Create Account Page
package sample;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller2 implements Initializable{
    @FXML
    TextField username = new TextField() ;
    @FXML
    PasswordField password = new PasswordField() ;
    @FXML
    PasswordField confirmpassword = new PasswordField() ;
    @FXML
    Label Error = new Label() ;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    public void AccountCreate(ActionEvent event) throws Exception{
        Scanner x = new Scanner(new File("src/Online.txt")) ;
        String a = username.getText().toString() ;
        String b = password.getText().toString() ;
        String id = a+b ;
        if((!a.equals(x.nextLine()))){
            if(b.equals(confirmpassword.getText().toString())){
                FileWriter fileWriter = new FileWriter("src/users.txt",true) ;
                BufferedWriter out = new BufferedWriter(fileWriter) ;
                out.write(id+"\n");
                out.close();
                BufferedWriter out2 = new BufferedWriter(new FileWriter(new File("src/Online.txt"),true));
                Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                out2.write(a + "\n");
                out2.close();
                Scene scene = new Scene(root) ;
                Main.primaryStage.setScene(scene);
            }
            else{
                Error.setText("Passwords do not match");
            }
        }
        else {
            Error.setText("User name exists");
        }
    }

    @FXML
    public void Back(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root) ;
        Main.primaryStage.setScene(scene);
    }
}