package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class signupnextform {
   private String consumertype_;
   private int userId;
   public void setUserId(int id){userId=id;}
   @FXML
   private MenuItem Commercial;

   @FXML
   private MenuItem Domestic;

   @FXML
   private MenuItem Industrial;

   @FXML
   private Button SaveButton;

   @FXML
   private TextField address;

   @FXML
   private TextField cnic;

   @FXML
   private MenuButton consumertype;

   @FXML
   private TextField contact;

   @FXML
   private TextField name;

   @FXML
   void OnClickCommercial(ActionEvent event) {
       consumertype_="Commercial";
      consumertype.setText(consumertype_);
   }

   @FXML
   void OnClickDomestic(ActionEvent event) {
      consumertype_="Domestic";
      consumertype.setText(consumertype_);
   }

   @FXML
   void OnClickIndustrial(ActionEvent event) {
      consumertype_="Industrial";
      consumertype.setText(consumertype_);
   }

   @FXML
   void OnClickSaveButton(ActionEvent event) {
      try{
         Db db=new Db();
         Connection mycon=db.getConnection();
         PreparedStatement st=mycon.prepareStatement("UPDATE consumer SET ConsumerName=?,ConsumerCNIC=?,ConsumerContact=?,ConsumerAddress=?,ConsumerType=? WHERE ConsumerId=?");
         st.setString(1,name.getText());
         st.setString(2, cnic.getText());
         st.setString(3,contact.getText());
         st.setString(4,address.getText());
         st.setString(5,consumertype_);
         st.setInt(6,userId);
         int r=st.executeUpdate();
         if(r>0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Information Saved Successfully");
            alert.showAndWait();
         }
         st= mycon.prepareStatement("SELECT RequestApproved FROM consumer_request WHERE ConsumerId=?");
         st.setInt(1,userId);
         ResultSet rs=st.executeQuery();
         if(rs.next()){
            String check=rs.getString("RequestApproved");
            if(check.equals("Approved")) {
               try {
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("Consumer.fxml"));
                  Parent newPageRoot = loader.load();
                  Consumer controller = loader.getController();
                  controller.setUserId(userId);
                  controller.initialize();
                  Scene newPageScene = new Scene(newPageRoot);
                  Stage primaryStage = (Stage) SaveButton.getScene().getWindow();
                  primaryStage.setScene(newPageScene);
                  primaryStage.show();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
            else{
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Information");
               alert.setContentText("Your Request Will Be Verified Soon");
               alert.showAndWait();
               try {
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
                  Parent newPageRoot = loader.load();
                  signup controller = loader.getController();
                  Scene newPageScene = new Scene(newPageRoot);
                  Stage primaryStage = (Stage) SaveButton.getScene().getWindow();
                  primaryStage.setScene(newPageScene);
                  primaryStage.show();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
            rs.close();
            st.close();
            mycon.close();
         }
      }
      catch(SQLException e){
         e.printStackTrace();
      }
   }

}

