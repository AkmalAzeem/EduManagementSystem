package com.developersstack.edumanagement.controller;

import com.developersstack.edumanagement.db.Database;
import com.developersstack.edumanagement.db.DbConnection;
import com.developersstack.edumanagement.model.Student;
import com.developersstack.edumanagement.view.tm.StudentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentFormController {
    public AnchorPane context;
    public TextField txtId;
    public TextField txtName;
    public DatePicker txtDOB;
    public TextField txtAddress;
    public TableView<StudentTm> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colDOB;
    public TableColumn colAddress;
    public TableColumn colOption;
    public Button btn;
    public TextField txtSearch;
    String searchText="";

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setStudentId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            setTableData(searchText);
        });

        tblStudent.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null!=newValue){
                        setData(newValue);
                    }
        });
    }

    private void setData(StudentTm tm) {  //Update
        txtId.setText(tm.getId());
        txtName.setText(tm.getFullName());
        txtDOB.setValue(LocalDate.parse(tm.getDob(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtAddress.setText(tm.getAddress());
        btn.setText("Update Student");
    }

    private void setTableData(String searchText) {
        ObservableList<StudentTm> obList = FXCollections.observableArrayList();
        try {
            for (Student st: searchStudents(searchText)
            ) {

                    Button btn = new Button("Delete");
                    StudentTm tm = new StudentTm(
                            st.getStudentId(),
                            st.getFullName(),
                            new SimpleDateFormat("yyyy-MM-dd").format(st.getDateOfBirth()),
                            st.getAddress(),
                            btn
                    );

                    btn.setOnAction(e->{
                        Alert alert = new Alert(
                                Alert.AlertType.CONFIRMATION, "Are you sure?",
                                ButtonType.YES,ButtonType.NO
                        );
                        Optional<ButtonType>buttonType = alert.showAndWait();
                        if (buttonType.get().equals(ButtonType.YES )){
                            try {
                                if (deleteStudentId(st.getStudentId())){
                                    new Alert(Alert.AlertType.INFORMATION,"Deleted!").show();
                                    setTableData(searchText);
                                    setStudentId();
                                }
                                else {
                                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                                }
                            } catch (ClassNotFoundException | SQLException ex) {
                                new Alert(Alert.AlertType.ERROR, ex.toString()).show();
                            }

                        }
                    });

                    obList.add(tm);
                }
            tblStudent.setItems(obList);
        }
        catch (SQLException | ClassNotFoundException e){

        }

    }

    private void setStudentId() {
        String lastIdIntegerNumberAsAString = null;
        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] = lastId.split("-");
                lastIdIntegerNumberAsAString = splitData[1];
                int lastIntegerIDAsInt = Integer.parseInt(lastIdIntegerNumberAsAString);
                lastIntegerIDAsInt++;
                String generatedStudentId = "S-" + lastIntegerIDAsInt;
                txtId.setText(generatedStudentId);
            }
            else {
                txtId.setText("S-1");
            }
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void saveOnAction(ActionEvent actionEvent) {
        Student student = new Student(
                txtId.getText(),
                txtName.getText(),
                Date.from(txtDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                txtAddress.getText()
        );
        if (btn.getText().equalsIgnoreCase("Save Student")){

            try {
               if (saveStudent(student)){
                   Database.studentTable.add(student);
                   setStudentId();
                   clear();
                   setTableData(searchText);
                   new Alert(Alert.AlertType.INFORMATION,"Student saved!").show();
               }
               else {
                   new Alert(Alert.AlertType.WARNING,"Try Again!").show();
               }
            }catch (SQLException | ClassNotFoundException e){
                new Alert(Alert.AlertType.ERROR, e.toString()).show();
            }

        }
        else {
            try {
                if (updateStudent(student)){
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Student Updated!").show();
                }
                else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }catch (SQLException | ClassNotFoundException e){
                new Alert(Alert.AlertType.ERROR, e.toString()).show();
            }
        }
    }
    private void clear(){
        txtDOB.setValue(null);
        txtName.clear();
        txtAddress.clear();
    }

    public void newStudentOnAction(ActionEvent actionEvent) {
        clear();
        setStudentId();
        btn.setText("Save Student");
        setTableData(searchText);
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }
    private boolean saveStudent(Student student) throws ClassNotFoundException, SQLException {
        // Create a Connection
        Connection connection = DbConnection.getInstance().getConnection();
        // write a SQL
        String sql = "INSERT INTO student VALUES (?,?,?,?)";
        // create statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getStudentId());
        statement.setString(2, student.getFullName());
        statement.setObject(3, student.getDateOfBirth());
        statement.setString(4, student.getAddress());

        // set sql into the statement and execute
        return statement.executeUpdate()>0; // INSERT, UPDATE, DELETE use executeUpdate
    }
    private String getLastId() throws ClassNotFoundException, SQLException {
        // Create a Connection
        Connection connection = DbConnection.getInstance().getConnection();
        // write a SQL
        String sql = "SELECT student_id FROM student ORDER BY CAST(SUBSTRING(student_id,3) AS UNSIGNED ) DESC LIMIT 1";
        // create statement
        PreparedStatement statement = connection.prepareStatement(sql);

        // set sql into the statement and execute
        ResultSet resultSet = statement.executeQuery();  // INSERT, UPDATE, DELETE use executeUpdate
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
    private List<Student> searchStudents(String text) throws ClassNotFoundException, SQLException {
        text="%" + text + "%"; // %text%
        Connection connection = DbConnection.getInstance().getConnection();
        // write a SQL
        String sql = "SELECT * FROM student WHERE full_name LIKE ? OR address LIKE ?";
        // create statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,text);
        statement.setString(2,text);
        // set sql into the statement and execute
        ResultSet resultSet = statement.executeQuery();  // INSERT, UPDATE, DELETE use executeUpdate
        List<Student> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDate(3),
                            resultSet.getString(4)
                    )
            );
        }
        return list;
    }
    private boolean deleteStudentId(String id) throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        // write a SQL
        String sql = "DELETE FROM student WHERE student_id=?";
        // create statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id);
        // set sql into the statement and execute
        return  statement.executeUpdate()>0;  // INSERT, UPDATE, DELETE use executeUpdate
        }
    private boolean updateStudent(Student student) throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        // write a SQL
        String sql = "UPDATE student SET full_name=?, dob=?, address=? WHERE student_id=?";
        // create statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getFullName());
        statement.setObject(2, student.getDateOfBirth());
        statement.setString(3, student.getAddress());
        statement.setString(4, student.getStudentId());

        // set sql into the statement and execute
        return statement.executeUpdate()>0; // INSERT, UPDATE, DELETE use executeUpdate
         }
    }

