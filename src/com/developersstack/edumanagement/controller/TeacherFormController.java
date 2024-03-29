package com.developersstack.edumanagement.controller;

import com.developersstack.edumanagement.db.Database;
import com.developersstack.edumanagement.model.Student;
import com.developersstack.edumanagement.model.Teacher;
import com.developersstack.edumanagement.view.tm.StudentTm;
import com.developersstack.edumanagement.view.tm.TeacherTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

public class TeacherFormController {
    public AnchorPane teacherContext;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSearch;
    public Button btn;
    public TableView<TeacherTm> tblTeacher;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colOption;
    public TextField txtContact;
    String searchText="";

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setTeacherId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            setTableData(searchText);
        });

        tblTeacher.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null!=newValue){
                        setData(newValue);
                    }
                });
    }

    private void setData(TeacherTm tm) {
        txtId.setText(tm.getCode());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtContact.setText(tm.getContact());
        btn.setText("Update  Teacher");
    }

    private void setTableData(String searchText) {
        ObservableList<TeacherTm> obList = FXCollections.observableArrayList();
        for (Teacher t:Database.teacherTable
        ) {
            if (t.getName().contains(searchText)){
                Button btn = new Button("Delete");
                TeacherTm tm = new TeacherTm(
                        t.getCode(),
                        t.getName(),
                        t.getAddress(),
                        t.getContact(),
                        btn
                );

                btn.setOnAction(e->{
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION, "Are you sure?",
                            ButtonType.YES,ButtonType.NO
                    );
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES )){
                        Database.teacherTable.remove(t);
                        new Alert(Alert.AlertType.INFORMATION,"Deleted!").show();
                        setTableData(searchText);
                        setTeacherId();
                    }
                });

                obList.add(tm);
            }

        }
        tblTeacher.setItems(obList);
    }


    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newTeacherOnAction(ActionEvent actionEvent) {
        btn.setText("Save Teacher");
        setTeacherId();
        clear();
        setTableData(searchText);

    }

    public void saveOnAction(ActionEvent actionEvent) {
        if (btn.getText().equalsIgnoreCase("Save Teacher")){
            Teacher teacher = new Teacher(
                    txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContact.getText()
                    );
            Database.teacherTable.add(teacher);
            setTeacherId();
            clear();
            setTableData(searchText);
            new Alert(Alert.AlertType.INFORMATION,"Teacher saved!").show();
        }
        else {
            for (Teacher t:Database.teacherTable
            ) {
                if (t.getCode().equals(txtId.getText())){
                    //update
                    t.setName(txtName.getText());
                    t.setAddress(txtAddress.getText());
                    t.setContact(txtContact.getText());
                    setTableData(searchText);
                    clear();
                    setTeacherId();
                    btn.setText("Save Teacher");
                    return;
                }

            }
            new Alert(Alert.AlertType.WARNING, "Not Found").show();
        }

    }
    private void clear(){
        txtContact.clear();
        txtName.clear();
        txtAddress.clear();
    }

    private void setTeacherId() {
        if (!Database.teacherTable.isEmpty()){
            Teacher lastTeacher = Database.teacherTable.get(
                    Database.teacherTable.size()-1
            );
            String lastId = lastTeacher.getCode() ;
            String splitData[] = lastId.split("-");
            String lastIdIntegerNumberAsAString = splitData[1];
            int lastIntegerIDAsInt=Integer.parseInt(lastIdIntegerNumberAsAString);
            lastIntegerIDAsInt++;
            String generatedTeacherId="T-"+lastIntegerIDAsInt;
            txtId.setText(generatedTeacherId);
        }else{
            txtId.setText("T-1");
        }
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage)  teacherContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
}
