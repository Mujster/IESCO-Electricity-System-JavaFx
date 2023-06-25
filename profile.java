package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.sql.*;
public class profile {
    private int userId;

    public void setUserId(int id) {
        userId = id;
    }

    @FXML
    private TextField Address;

    @FXML
    private Button CancelButton;

    @FXML
    private TextField Contact;

    @FXML
    private TextField FirstName;

    @FXML
    private TextField LastName;

    @FXML
    private Button UpdateButton;

    @FXML
    void OnClickCancelButton(ActionEvent event) {
        FirstName.clear();
        LastName.clear();
        Contact.clear();
        Address.clear();
    }

    @FXML
    void OnClickUpdateButton(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Are you sure?");
        confirmation.setContentText("Do you want to proceed?");
        confirmation.initModality(Modality.APPLICATION_MODAL);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmation.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                try {
                    Db db = new Db();
                    Connection mycon = db.getConnection();
                    String fullname = FirstName.getText() + " " + LastName.getText();
                    String contactno = Contact.getText();
                    String address_ = Address.getText();
                    String query = "UPDATE consumer SET ConsumerName=?,ConsumerContact=?, ConsumerAddress=? WHERE ConsumerId=?";
                    PreparedStatement statement = mycon.prepareStatement(query);
                    statement.setString(1, fullname);
                    statement.setString(2, contactno);
                    statement.setString(3, address_);
                    statement.setInt(4, userId);
                    int r = statement.executeUpdate();
                    if (r > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setContentText("Information Changed Successfully");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Failure");
                        alert.setContentText("Information Change Failed");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                confirmation.close();
            }
        });
    }
}