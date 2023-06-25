package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class editconsumer {
    private int userId;
    public void setUserId(int id){userId=id;}
    @FXML
    private Button AddConsumerButton;
    @FXML
    private Button AddConnectionButton;
    @FXML
    private Button DeleteConnectionButton;
    @FXML
    private Button BackButton;

    @FXML
    private Button DeleteConsumerButton;
    @FXML
    private StackPane ContentArea;
    @FXML
    void OnClickAddConsumerButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("consumerrequest.fxml"));
            Parent fxml1=loader.load();
            consumerrequest controller=loader.getController();
            controller.setUserId(userId);
            controller.initializepage();
            ContentArea.getChildren().removeAll();
            ContentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OnClickAddConnectionButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("addconnection.fxml"));
            Parent fxml1=loader.load();
            addconnection controller=loader.getController();
            ContentArea.getChildren().removeAll();
            ContentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    @FXML
    void OnClickDeleteConnectionButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("deleteconnection.fxml"));
            Parent fxml1=loader.load();
            deleteconnection controller=loader.getController();
            ContentArea.getChildren().removeAll();
            ContentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    @FXML
    void OnClickDeleteConsumerButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("deleteconsumer.fxml"));
            Parent fxml1=loader.load();
            deleteconsumer controller=loader.getController();
            ContentArea.getChildren().removeAll();
            ContentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    void initialize(){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("consumerrequest.fxml"));
            Parent fxml1=loader.load();
            consumerrequest controller=loader.getController();
            controller.initializepage();
            controller.setUserId(userId);
            ContentArea.getChildren().removeAll();
            ContentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}
