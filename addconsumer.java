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
import java.sql.*;

public class addconsumer {
    private int userId;
    public void setUserId(int id){userId=id;}
    private int ConsumerId;
    private String consumertype;
    @FXML
    private Label Address;

    @FXML
    private Label CNIC;

    @FXML
    private Button CancelButton;

    @FXML
    private Button BackButton;
    @FXML
    private Label ConsumerType;

    @FXML
    private Label Contact;

    @FXML
    private Label FName;

    @FXML
    private Label LName;
    @FXML
    private Button RegisterButton;
    private String fullname;
    private String CNIC_;
    private String Contact_;
    private String Address_;
    @FXML
    void OnClickCancelButton(ActionEvent event) {
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="UPDATE consumer_request SET RequestApproved ='Denied' WHERE ConsumerId=?";
            PreparedStatement st= mycon.prepareStatement(query);
            st.setInt(1, ConsumerId);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Consumer Denied Successfully");
                alert.showAndWait();
            } else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Consumer Denied Failed");
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
    void OnClickRegisterButton(ActionEvent event) {
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="UPDATE consumer_request SET RequestApproved ='Approved' WHERE ConsumerId=?";
            PreparedStatement st= mycon.prepareStatement(query);
            st.setInt(1, ConsumerId);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Consumer Registered Successfully");
                alert.showAndWait();
            } else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Consumer Registration Failed");
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
    void OnClickBackButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editconsumer.fxml"));
            Parent newPageRoot = loader.load();
            editconsumer controller = loader.getController();
            controller.setUserId(userId);
            controller.initialize();
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newPageScene = new Scene(newPageRoot);
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize(int id){
        ConsumerId=id;
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT ConsumerName,ConsumerCNIC,ConsumerContact,ConsumerAddress,ConsumerType FROM consumer WHERE ConsumerId=?";
            PreparedStatement st=mycon.prepareStatement(query);
            st.setInt(1,ConsumerId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                fullname=rs.getString("ConsumerName");
                CNIC_=rs.getString("ConsumerCNIC");
                Contact_=rs.getString("ConsumerContact");
                Address_=rs.getString("ConsumerAddress");
                consumertype=rs.getString("ConsumerType");
                String[] names = fullname.split(" ");
                // Extract the first and last name
                String firstName = names[0];
                String lastName = names[names.length - 1];

                FName.setText(firstName);
                LName.setText(lastName);
                CNIC.setText(CNIC_);
                Contact.setText(Contact_);
                Address.setText(Address_);
                ConsumerType.setText(consumertype);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
