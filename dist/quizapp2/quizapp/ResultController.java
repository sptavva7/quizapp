/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quizapp.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author ArKadius
 */
public class ResultController implements Initializable {
    public static String username;

    @FXML
    private Label total;
    @FXML
    private Label correct;
    @FXML
    private Label accuracy;
    @FXML
    private JFXButton okbut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int tot=0;
        int cor=0;
        double accu;
        String sql="select username,answer,response from questions,answers where username='"+username+"'"+
                " and questions.qid=answers.qno";
        String sql2="update users set attempt='true' where username='"+username+"'";
        DatabaseHandler.executeUpdate(sql2);
        try{
            ResultSet rs=DatabaseHandler.executeQuery(sql);
            while(rs.next())
            {
                ++tot;
                if(rs.getString("answer").equalsIgnoreCase(rs.getString("response")))
                    ++cor;
            }
            accu=(double)cor/(double)tot*100;
            total.setText(String.valueOf(tot));
            correct.setText(String.valueOf(cor));
            accuracy.setText(String.valueOf(accu)+" %");
        }catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }    
void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((Stage) okbut.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void closeStage() {
        ((Stage) okbut.getScene().getWindow()).close();
    }
    @FXML
    private void gotologin(ActionEvent event) {
        closeStage();
        loadWindow("/quizapp/login/login.fxml","BlueBook");
    }
}    
