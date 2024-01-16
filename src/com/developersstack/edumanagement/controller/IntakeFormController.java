package com.developersstack.edumanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class IntakeFormController {
    public AnchorPane Context;
    public TextField txtId;
    public TextField txtName;
    public Button btn;
    public TableView tblIntake;
    public TableColumn colId;
    public TableColumn colIntake;
    public TableColumn colStartDate;
    public TableColumn colProgram;
    public TableColumn colCompleteState;
    public TableColumn colOption;
    public TextField txtSearch;
    public DatePicker txtDate;
    public ComboBox cmbProgram;

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newIntakeOnAction(ActionEvent actionEvent) {
    }

    public void saveIntakeOnAction(ActionEvent actionEvent) {
    } 
    private void setUi(String location) throws IOException {
        Stage stage = (Stage)  Context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
}
