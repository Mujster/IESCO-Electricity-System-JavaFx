package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.sql.*;
public class deleteconsumer {
    private int value;
    @FXML
    private Label CNICLabel;

    @FXML
    private Button CancelButton;
    @FXML
    private TextField ConsumerIdField;
    @FXML
    private Button DeleteButton;

    @FXML
    private Label IdLabel;
    @FXML
    private Label ExistLabel;
    @FXML
    private Label NameLabel;

    @FXML
    private Label TypeLabel;

    @FXML
    void OnClickCancelButton(ActionEvent event) {

    }

    @FXML
    void OnClickDeleteButton(ActionEvent event) {
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
                try {
                    Db db = new Db();
                    Connection mycon = db.getConnection();
                    String query = "DELETE FROM consumer WHERE ConsumerId=?";
                    PreparedStatement statement = mycon.prepareStatement(query);
                    statement.setInt(1,value);
                    int rowsAffected = statement.executeUpdate();
                    if(rowsAffected>0) {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Success");
                    alert.setContentText("Consumer Successfully Deleted");
                    }
                    else{
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("Error In Deletion");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void initialize(){
        ConsumerIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean containsNumber = true;
            for (int i = 0; i < newValue.length(); i++) {
                char c = newValue.charAt(i);
                if (c < '0' || c > '9') {
                    containsNumber = false;
                    break;
                }
            }
            if(containsNumber) {
                try {
                    Db db = new Db();
                    Connection mycon = db.getConnection();
                    value = Integer.parseInt(ConsumerIdField.getText());
                    String query = "SELECT * FROM consumer WHERE ConsumerId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1, value);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        IdLabel.setText(rs.getString("ConsumerId"));
                        NameLabel.setText(rs.getString("ConsumerName"));
                        CNICLabel.setText(rs.getString("ConsumerCNIC"));
                        TypeLabel.setText(rs.getString("ConsumerType"));
                        ExistLabel.setText("Exists");
                        rs.close();
                        st.close();
                        mycon.close();
                    } else {
                        ExistLabel.setText("Not Exists");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(!containsNumber || newValue==null){
                ExistLabel.setText("Invalid Input");
            }
        });
    }
}
