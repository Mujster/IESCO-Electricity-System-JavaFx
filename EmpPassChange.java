package com.example.test3;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.sql.*;
public class EmpPassChange {
    private int userId;
    @FXML
    private Button SaveButton;
    @FXML
    private TextField ConfirmPassword;

    @FXML
    private TextField NewPassword;

    @FXML
    void OnClickSaveButton(ActionEvent event) {
        Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Are you sure?");
        confirmation.setContentText("Do you want to proceed?");
        confirmation.initModality(Modality.APPLICATION_MODAL);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmation.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                if (NewPassword.getText().equals(ConfirmPassword.getText())) {
                    try {
                        Db db = new Db();
                        Connection mycon = db.getConnection();
                        String query = "UPDATE employee SET EmployeePassword=? WHERE EmployeeId=?";
                        PreparedStatement statement = mycon.prepareStatement(query);
                        statement.setString(1, ConfirmPassword.getText());
                        statement.setInt(2, userId);
                        int r = statement.executeUpdate();
                        if (r > 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setContentText("Password Changed Successfully");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Failure");
                            alert.setContentText("Password Change Failed");
                            alert.showAndWait();
                        }
                        statement.close();
                        mycon.close();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Passwords");
                    alert.setContentText("Passwords Do Not Match");
                    alert.showAndWait();
                }
            }
            else{
                confirmation.close();
            }
        });
    }
    public void setUserId(int id){userId=id;}
}
