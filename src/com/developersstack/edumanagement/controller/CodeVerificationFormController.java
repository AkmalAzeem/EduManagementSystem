package com.developersstack.edumanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeVerificationFormController {
    public AnchorPane Context;
    public Button btn;
    public TextField txtCode;
    public Label txtSelectedEmail;


    int code=0;
    String selectedEmail="";
    public void  setUserData(int verificationCode, String email){
        System.out.println(verificationCode);
        code = verificationCode;
        selectedEmail = email;
        txtSelectedEmail.setText(email);
    }

    public void backToLoginPAge(ActionEvent actionEvent) {
    }

    public void verifyCodeOnAction(ActionEvent actionEvent) throws IOException {
        if (
                String.valueOf(code).equals(txtCode.getText())
        ){
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("../view/ResetPasswordForm.fxml"));
            Parent parent = fxmlLoader.load();
            ResetPasswordFormController controller = fxmlLoader.getController();
            controller.setUserData(selectedEmail);
            Stage stage = (Stage) Context.getScene().getWindow();
            stage.setScene(new Scene(parent));
            //navigate reset pwd
        }else {
            new Alert(Alert.AlertType.ERROR, "Wrong verification Code").show();
        }
    }

    public void changeEmailOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ForgotPasswordForm");
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage)  Context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
}
