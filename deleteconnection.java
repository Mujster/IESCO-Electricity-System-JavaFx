package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;

public class deleteconnection {
    private int value,meterid;
    private String type_;
    @FXML
    private Button CancelButton;

    @FXML
    private Label ConnectionLabel;

    @FXML
    private TextField ConsumerID;

    @FXML
    private Label DateLabel;

    @FXML
    private Button DeleteButton;

    @FXML
    private TextField MeterID;

    @FXML
    private Label ExistsLabel;
    @FXML
    private Label MeterExists;
    @FXML
    private Label TypeLabel;

    @FXML
    void OnClickCancelButton(ActionEvent event) {
         ConsumerID.clear();
         MeterID.clear();
         ConnectionLabel.setText("E.g 1");
         TypeLabel.setText("E.g Domestic");
         DateLabel.setText("E.g 2023-01-01");
    }

    @FXML
    void OnClickDeleteButton(ActionEvent event) {
         try{
             Db db = new Db();
             Connection mycon = db.getConnection();
             value = Integer.parseInt(ConsumerID.getText());
             String query = "DELETE FROM meter WHERE ConsumerId=? AND MeterId=?";
             PreparedStatement st = mycon.prepareStatement(query);
             st.setInt(1, value);
             st.setInt(2,meterid);
             int r=st.executeUpdate();
             if(r>0){
                 Alert alert=new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Success");
                 alert.setContentText("Connection Deleted Successfully");
                 alert.showAndWait();
             } else {
                 Alert alert=new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Error");
                 alert.setContentText("Connection Deletion Failed");
                 alert.showAndWait();
            }
             mycon.close();
             st.close();
         }
         catch(SQLException e){
             e.printStackTrace();
         }
    }
    public void initialize() {
        ConsumerID.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    value = Integer.parseInt(ConsumerID.getText());
                    String query = "SELECT * FROM consumer WHERE ConsumerId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1, value);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        ExistsLabel.setText("Exists");
                        type_=rs.getString("ConsumerType");
                        rs.close();
                        st.close();
                        mycon.close();
                    } else {
                        ExistsLabel.setText("Not Exists");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(!containsNumber || newValue==null){
                ExistsLabel.setText("Invalid Input");
            }
        });
        MeterID.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    meterid = Integer.parseInt(MeterID.getText());
                    String query = "SELECT * FROM meter WHERE MeterId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1, meterid);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        ConnectionLabel.setText(rs.getString("MeterId"));
                        DateLabel.setText(rs.getString("DateInstalled"));
                        TypeLabel.setText(type_);
                        MeterExists.setText("Exists");
                        rs.close();
                        st.close();
                        mycon.close();
                    } else {
                        MeterExists.setText("Not Exists");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(!containsNumber || newValue==null){
                MeterExists.setText("Invalid Input");
            }
        });
    }
}
