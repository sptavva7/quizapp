/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp.login.admin.questions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
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
 * @author bose4
 */
public class QuestionsController implements Initializable {

    @FXML
    private JFXTextField question;
    @FXML
    private JFXTextField op1;
    @FXML
    private JFXTextField op4;
    @FXML
    private JFXTextField op3;
    @FXML
    private JFXTextField op2;
    @FXML
    private JFXButton insertdata;
    @FXML
    private JFXTextField answer;
    @FXML
    private JFXButton back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DatabaseHandler.disconnect();
    }    
    public boolean check()
    {
        if(op1.getText().isEmpty()||op2.getText().isEmpty()||op3.getText().isEmpty()||op4.getText().isEmpty()||answer.getText().isEmpty()||question.getText().isEmpty())
            return false;
        if(!(answer.getText().equals(op1.getText())||answer.getText().equals(op2.getText())||answer.getText().equals(op3.getText())||answer.getText().equals(op4.getText())))
            return false;
        return true;
     }
    @FXML
    private void insertData(ActionEvent event) {
        String sql="insert into questions(question,option1,option2,option3,option4,answer) values('"+question.getText()+"','"
                +op1.getText()+"','"+op2.getText()+"','"
                +op3.getText()+"','"+op4.getText()+"','"
                +answer.getText()+"')";
        if(check()&&DatabaseHandler.executeUpdate(sql)!=0)
        {
            AlertMaker.showNotification("Awesome !", "You just added a new question to the quiz",AlertMaker.image_checked);
            question.setText("");
            op1.setText("");
            op2.setText("");
            op3.setText("");
            op4.setText("");
            answer.setText("");
        }
        else
        {
            AlertMaker.showNotification("Sorry !", "There is a problem in the input data",AlertMaker.image_movie_forbidden);
        }
    }
private void closeStage() {
        ((Stage) question.getScene().getWindow()).close();
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
    private void goBack(ActionEvent event) {
        closeStage();
    }
    
}
