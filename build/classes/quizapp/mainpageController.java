/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quizapp.database.DatabaseHandler;

/**
 *
 * @author UEM
 */
public class mainpageController implements Initializable {
    public static String username;
    public static int total;
    public String response="NA";
    public int i=0,qcount=0;
    public List<question> allquestions = new ArrayList<question>();
    @FXML
    private Label label;
    @FXML
    private Label question;
    @FXML
    private JFXRadioButton op1;
    @FXML
    private ToggleGroup options;
    @FXML
    private JFXRadioButton op2;
    @FXML
    private JFXRadioButton op4;
    @FXML
    private JFXRadioButton op3;
    @FXML
    private JFXButton next;
    @FXML
    private Label qno;
    @FXML
    private Label count;
    public void load_question()
    {
        question a=allquestions.get(i);
        question.setText(a.question);
        op1.setText(a.option1);
        op1.setSelected(false);
        op2.setText(a.option2);
        op2.setSelected(false);
        op3.setText(a.option3);
        op3.setSelected(false);
        op4.setText(a.option4);
        op4.setSelected(false);
        response="NA";
        qno.setText(String.valueOf(i+1));
        count.setText(String.valueOf(total-i-1));
        qcount=a.qid;
        i++;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHandler.disconnect();
        String sql="select * from questions";
        ResultSet  rs=DatabaseHandler.executeQuery(sql);
        try {
            while(rs.next())
            {
                question a=new question();
                a.question=rs.getString("question");
                a.option1=rs.getString("option1");
                a.option2=rs.getString("option2");
                a.option3=rs.getString("option3");
                a.answer=rs.getString("answer");
                a.option4=rs.getString("option4");
                a.qid=rs.getInt("qid");
                allquestions.add(a);
            }
            rs.close();
            DatabaseHandler.disconnect();
            load_question();
                } catch (SQLException ex) {
           ex.getMessage();
        }
            
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((Stage) question.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void closeStage() {
        ((Stage) question.getScene().getWindow()).close();
    }
    public void proceed()
    {
        String sql="insert into answers values('"+username+"',"+qcount+",'"+response+"')";
        if(DatabaseHandler.executeUpdate(sql)!=0&&i<total)
        {
            load_question();
        }
        else
        {
            ResultController.username=username;
            closeStage();
            loadWindow("/quizapp/result.fxml","Result");
        }
    }
    @FXML
    private void gotonextquestion(ActionEvent event) {
        proceed();
    }
    @FXML
    private void option1(ActionEvent event) {
        response=op1.getText();
    }

    @FXML
    private void option2(ActionEvent event) {
        response=op2.getText();
    }

    @FXML
    private void option4(ActionEvent event) {
        response=op4.getText();
    }

    @FXML
    private void option3(ActionEvent event) {
        response=op3.getText();
    }
 public class  question{
     public int qid;
     public String question;
     public String option1;
     public String option2;
     public String option3;
     public String option4;
     public String answer;
 } 
}
