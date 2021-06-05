/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp.login.admin;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quizapp.alert.AlertMaker;
import quizapp.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author bose4
 */
public class AdminUIController implements Initializable {
    ObservableList<Question> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Question> table;
    @FXML
    private TableColumn<Question, Integer> idCol;
    @FXML
    private TableColumn<Question, String> questionCol;
    @FXML
    private TableColumn<Question, String> op1Col;
    @FXML
    private TableColumn<Question, String> op2Col;
    @FXML
    private TableColumn<Question, String> op3Col;
    @FXML
    private TableColumn<Question, String> op4Col;
    @FXML
    private TableColumn<Question, String> ansCol;
    @FXML
    private JFXButton refresh;
    @FXML
    private JFXButton but_movie;
    @FXML
    private JFXButton but_remove;
    @FXML
    private JFXButton cl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCols();
        loadtabledata();
    }    
    private void initCols() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        op1Col.setCellValueFactory(new PropertyValueFactory<>("op1"));
        op2Col.setCellValueFactory(new PropertyValueFactory<>("op2"));
        op3Col.setCellValueFactory(new PropertyValueFactory<>("op3"));
        op4Col.setCellValueFactory(new PropertyValueFactory<>("op4"));
        ansCol.setCellValueFactory(new PropertyValueFactory<>("answer"));
    }
    public void loadtabledata()
    {
        String sql = "SELECT * FROM questions";
        ResultSet rs = DatabaseHandler.executeQuery(sql);
        try {
            while (rs.next()) {
                int id =rs.getInt("qid");
                String question = rs.getString("question");
                String op1 = rs.getString("option1");
                String op2 = rs.getString("option2");
                String op3 = rs.getString("option3");
                String op4= rs.getString("option4");
                String answer=rs.getString("answer");
                list.add(new Question(id,question,op1,op2,op3,op4,answer));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showWarning(ex);
        }
        table.getItems().setAll(list);
    }
    private void refresh()
    {
        table.getItems().clear();
        list.removeAll(list);
        loadtabledata();
    }
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.initOwner(((Stage) refresh.getScene().getWindow()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
    private void closeStage() {
        ((Stage) refresh.getScene().getWindow()).close();
    }
    @FXML
    private void refreshTable(ActionEvent event) {
        refresh();
    }

    @FXML
    private void AddQ(ActionEvent event) {
        loadWindow("/quizapp/login/admin/questions/questions.fxml","Add new Question");
    }

    @FXML
    private void removeQ(ActionEvent event) {
        try{
            Question question= table.getSelectionModel().getSelectedItem();
            String sql="delete from questions where qid="+question.getId();
            if(DatabaseHandler.executeUpdate(sql)>0)
            {
                AlertMaker.showNotification("Successful", "Question Removed",AlertMaker.image_checked);
                refresh();
            }
            else
                AlertMaker.showNotification("Error","Selected Question cannot be deleted.", AlertMaker.image_cross);
        }
        catch(Exception ex)
        {
            AlertMaker.showNotification("Error","Not Selected question", AlertMaker.image_warning2);
        }
    }

    @FXML
    private void close(ActionEvent event) {
        closeStage();
        loadWindow("/quizapp/login/admin/adminlogin.fxml","Quiz App");
    }
    public static class Question {

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty question;
        private final SimpleStringProperty op1;
        private final SimpleStringProperty op2;
        private final SimpleStringProperty op3;
        private final SimpleStringProperty op4;
        private final SimpleStringProperty answer;

        public Question(int id, String question, String op1, String op2, String op3, String op4,String answer) {
            this.id =new SimpleIntegerProperty(id);
            this.question =new SimpleStringProperty(question);
            this.op1 =new SimpleStringProperty(op1);
            this.op2 = new SimpleStringProperty(op2);
            this.op3 = new SimpleStringProperty(op3);
            this.op4 = new SimpleStringProperty(op4);
            this.answer = new SimpleStringProperty(answer);
        }

    public int getId() {
        return id.get();
    }

    public String getQuestion() {
        return question.get();
    }

    public String getOp1() {
        return op1.get();
    }

    public String getOp2() {
        return op2.get();
    }

    public String getOp3() {
        return op3.get();
    }

    public String getOp4() {
        return op4.get();
    }
    public String getAnswer() {
        return answer.get();
    }
    }
}
