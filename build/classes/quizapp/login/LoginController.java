/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quizapp.ResultController;
import quizapp.alert.AlertMaker;
import quizapp.database.DatabaseHandler;
import quizapp.mainpageController;

/**
 * FXML Controller class
 *
 * @author UEM
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField username1;
    @FXML
    private JFXTextField username2;
    @FXML
    private JFXPasswordField password1;
    @FXML
    private JFXPasswordField password2;
    @FXML
    private JFXButton log;
    @FXML
    private JFXButton reg;
    @FXML
    private JFXButton adm;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((Stage) username1.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
           System.out.println("Something is wrong ... double check loadWindow");
        }
    }
    private void closeStage() {
        ((Stage) username1.getScene().getWindow()).close();
    }
    @FXML
    private void loginuser(ActionEvent event) {
        String sql="select * from users";
        int r=0;
        boolean flag=false,attempt=false;
        try{
        ResultSet rs=DatabaseHandler.executeQuery(sql);
        while(rs.next())
        {
            if(rs.getString("username").equals(username1.getText())&&rs.getString("password").equals(password1.getText()))
            {    flag=true;
                  attempt=(rs.getString("attempt")).equals("false")?false:true;
                 break;
            }
        }
        rs.close();
        }catch(SQLException ex)
        {
            AlertMaker.showNotification("Error !","User not Available.Create user",AlertMaker.image_warning2);
        }
        if(flag==true)
        {
            if(attempt)
            {
             ResultController.username=username1.getText();
             closeStage();
            loadWindow("/quizapp/result.fxml","RESULT");
            }
            else
            {
            mainpageController.username=username1.getText();
            ResultSet mr=DatabaseHandler.executeQuery("select count(*)as c from questions");
                try {
                    mr.next();
                    r=mr.getInt("c");
                } catch (SQLException ex) {
                }
            if(r==0)
                AlertMaker.showNotification("Sorry !","No questions in quiz",AlertMaker.image_warning);
            else{
                mainpageController.total=r;
                closeStage();
                loadWindow("/quizapp/mainpage.fxml","Quiz questions");
            }
            }
        }
        else
        {
            AlertMaker.showNotification("Oops !","Looks like your username/password is Invalid.",AlertMaker.image_cross);
        }
    }
    
    @FXML
    private void registeruser(ActionEvent event) {
        String sql="insert into users values('"+username2.getText()+"','"+password2.getText()+"','false')";
        if(username2.getText().isEmpty()||password2.getText().isEmpty())
            AlertMaker.showNotification("Oops !","You have not entered valid details",AlertMaker.image_warning);
        else if(DatabaseHandler.executeUpdate(sql)!=0)
        {
            AlertMaker.showNotification("Successful !","New user Successfully registered.",AlertMaker.image_checked);
        }
        else
        {
            AlertMaker.showNotification("Sorry !","This username is already registered..",AlertMaker.image_cross);
        }
        username2.setText("");
        password2.setText("");
    }

    @FXML
    private void switchuser(ActionEvent event) {
        closeStage();
        loadWindow("/quizapp/login/admin/adminlogin.fxml","Quiz App");
    }
    
}
