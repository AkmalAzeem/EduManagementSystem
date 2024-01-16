package com.developersstack.edumanagement.controller;

import com.developersstack.edumanagement.db.Database;
import com.developersstack.edumanagement.model.Program;
import com.developersstack.edumanagement.model.Teacher;
import com.developersstack.edumanagement.view.tm.ProgramTm;
import com.developersstack.edumanagement.view.tm.TechAddTm;
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
import java.util.ArrayList;

public class ProgramsFormController {

    public AnchorPane context;


    public TextField txtId;


    public TextField txtName;


    public TextField txtCost;


    public TextField txtSearch;


    public Button btn;


    public TableView<ProgramTm> tblPrograms;


    public TableColumn<?, ?> colId;


    public TableColumn<?, ?> colName;


    public TableColumn<?, ?> colTeacher;


    public TableColumn<?, ?> colTech;


    public TableColumn<?, ?> colCost;


    public TableColumn<?, ?> colOption;


    public ComboBox<String> cmbTeacher;


    public TableView<TechAddTm> tblTechnology;


    public TableColumn<?, ?> colTCode;


    public TableColumn<?, ?> colTName;


    public TableColumn<?, ?> colTRemove;


    public TextField txtTechnology;

    public void initialize(){
        setProgramCode();
        setTeachers();
        loadPrograms();

        colId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colTName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTeacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        colTech.setCellValueFactory(new PropertyValueFactory<>("btnTech"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));


        colTCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colTName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTRemove.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }
    ObservableList<TechAddTm> tmList = FXCollections.observableArrayList();
    ArrayList<String> teacherArray = new ArrayList<>();


    private void setTeachers() {
        for (Teacher t: Database.teacherTable
             ) {
            teacherArray.add(t.getCode()+". "+t.getName());
        }
        ObservableList<String> obList = FXCollections.observableArrayList(teacherArray);
        cmbTeacher.setItems(obList);
    }

    private void setProgramCode() {
        if (!Database.programTable.isEmpty()){
            Program lastProgram = Database.programTable.get(
                    Database.programTable.size()-1
            );
            String lastId = lastProgram.getCode() ;
            String splitData[] = lastId.split("-");
            String lastIdIntegerNumberAsAString = splitData[1];
            int lastIntegerIDAsInt=Integer.parseInt(lastIdIntegerNumberAsAString);
            lastIntegerIDAsInt++;
            String generatedProgramId="P-"+lastIntegerIDAsInt;
            txtId.setText(generatedProgramId);
        }else{
            txtId.setText("P-1");
        }
    }

    public void backToHomeOnAction(ActionEvent event) throws IOException {
        setUi("DashboardForm");
    }


    public void newProgramOnAction(ActionEvent event) {

    }


    public void saveOnAction(ActionEvent event) {
        String[] selectedTechs = new String[tmList.size()];
        int pointer = 0;
        for (TechAddTm t: tmList
             ) {
            selectedTechs[pointer]= t.getName();
            pointer++;
        }

        if (btn.getText().equals("Save Program")){
            Program program = new Program(
                    txtId.getText(),
                    txtName.getText(),
                    selectedTechs,
                    cmbTeacher.getValue().split("\\.")[0],
                    Double.parseDouble(txtCost.getText())
            );
            Database.programTable.add(program);
            new Alert(Alert.AlertType.INFORMATION, "Saved").show();
            loadPrograms();
        }
    }

    private void loadPrograms(){
        ObservableList<ProgramTm> programTmList = FXCollections.observableArrayList();
        for (Program p: Database.programTable
             ) {
            Button techButton = new Button("show Tech");
            Button removeButton = new Button("Delete");
            ProgramTm tm = new ProgramTm(
                    p.getCode(),
                    p.getName(),
                    p.getTeacher(),
                    techButton,
                    p.getCost(),
                    removeButton
            );
            programTmList.add(tm);
        }
        tblPrograms.setItems(programTmList);
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage)  context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
    ObservableList<String> obList = FXCollections.observableArrayList(teacherArray);
    public void addTechOnAction(ActionEvent actionEvent) {
        if (!isExist(txtTechnology.getText().trim())){
            Button btn = new Button("Remove");

            TechAddTm tm = new TechAddTm(
                    tmList.size()+1,txtTechnology.getText().trim(),btn
            );
            tmList.add(tm);
            tblTechnology.setItems(tmList);
            txtTechnology.clear();
        }else {
            txtTechnology.selectAll();
            new Alert(Alert.AlertType.WARNING, "Already Exists").show();
        }
    }


    private boolean isExist(String tech){
        for (TechAddTm tm : tmList
             ) {
            if (tm.getName().equals(tech)){
                return true;
            }
        }
        return false;
    }
}
