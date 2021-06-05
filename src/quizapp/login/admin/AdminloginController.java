/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp.login.admin;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quizapp.alert.AlertMaker;
import quizapp.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class AdminloginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton signin;
    @FXML
    private JFXButton user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    public boolean ValidateUsernamePassword() throws SQLException
    {
        boolean valid=false;
        ResultSet Myres=DatabaseHandler.executeQuery("select * from admin");
        while(Myres.next())
        {
            if(Myres.getString("username").equals(username.getText()))
                if(Myres.getString("password").equals(password.getText()))
                    valid=true;
        }
        Myres.close();
        DatabaseHandler.disconnect();
        return valid;
    }
    @FXML
    private void SignInAdmin(ActionEvent event) {
        try {
            if(ValidateUsernamePassword())
            {
                closeStage();
                loadWindow("/quizapp/login/admin/AdminUI.fxml","Question Setup");
            }
            else
            {
                AlertMaker.showNotification("Failed!","Your Admin ID or Password is invalid", AlertMaker.image_cross);
            }
        } catch (SQLException ex) {
            AlertMaker.showWarning(ex);
        }
    }
    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
    @FXML
    private void SwitchtoUser(ActionEvent event) {
        closeStage();
        loadWindow("/quizapp/login/login.fxml","Bluebook");
    }

    
}
