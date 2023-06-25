package com.example.test3;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class updatestatus{
    private String status_;
    @FXML
    private Button BackButton;
    @FXML
    private TextField BillId;

    @FXML
    private Button CancelButton;

    @FXML
    private TextField ConsumerId;

    @FXML
    private MenuItem Paid;

    @FXML
    private MenuButton Status;

    @FXML
    private MenuItem UnPaid;

    @FXML
    private Button UpdateButton;

    @FXML
    void OnClickBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("employee.fxml"));
            Parent newPageRoot = loader.load();
            employee controller = loader.getController();
            controller.setUserId(userId);
            controller.initialize();
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newPageScene = new Scene(newPageRoot);
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickCancelButton(ActionEvent event) {
       ConsumerId.clear();
       BillId.clear();
       Status.setText("Billing Status");
    }

    @FXML
    void OnClickUpdateButton(ActionEvent event) {
        try{
            Db db = new Db();
            Connection mycon = db.getConnection();
            String query = "UPDATE bill SET BillStatus=? WHERE BillId=? AND ConsumerId=?";
            PreparedStatement st = mycon.prepareStatement(query);
            st.setString(1,status_);
            st.setInt(2,billid);
            st.setInt(3,consumerid);
            int r=st.executeUpdate();
            if (r > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Status Updated Successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failure");
                alert.setContentText("Status Update Failed");
                alert.showAndWait();
            }
            st.close();
            mycon.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    void OnClickPaid(ActionEvent event) {
       status_="Paid";
    }

    @FXML
    void OnClickUnPaid(ActionEvent event) {
        status_="UnPaid";
    }

    @FXML
    private Label ConsumerExists;
    @FXML
    private Label BillExists;
    private int userId,consumerid,billid;
    public void setUserId(int id){userId=id;}
    public void initialize(){
        ConsumerId.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    String query = "SELECT ConsumerId FROM consumer WHERE ConsumerId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1,Integer.parseInt(ConsumerId.getText()));
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        consumerid=rs.getInt("ConsumerId");
                        ConsumerExists.setText("Exists");
                    } else {
                        ConsumerExists.setText("Not Exists");
                    }
                    rs.close();
                    st.close();
                    mycon.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        BillId.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    String query = "SELECT BillId FROM bill WHERE BillId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1,Integer.parseInt(BillId.getText()));
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        billid=rs.getInt("BillId");
                        BillExists.setText("Exists");
                    } else {
                        BillExists.setText("Not Exists");
                    }
                    rs.close();
                    st.close();
                    mycon.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
