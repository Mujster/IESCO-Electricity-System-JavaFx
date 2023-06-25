package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class account {

    private int userId;
    @FXML
    private Button BackButton;

    public void setUserId(int id){
        userId=id;
    }
    @FXML
    private Label NameLabel;

    @FXML
    private Button PasswordButton;

    @FXML
    private Button ProfileButton;

    @FXML
    private StackPane contentArea;

    @FXML
    void OnClickPasswordButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("passwordchange.fxml"));
            Parent fxml=loader.load();
            passwordchange controller=loader.getController();
            controller.setUserId(userId);
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    @FXML
    void OnClickProfileButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("profile.fxml"));
            Parent fxml1=loader.load();
            profile controller=loader.getController();
            controller.setUserId(userId);
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    @FXML
    void OnClickBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("consumer.fxml"));
            Parent newPageRoot = loader.load();
            Consumer controller = loader.getController();
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

    void initialize(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT ConsumerName FROM consumer WHERE ConsumerId=?";
            PreparedStatement statement = mycon.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                NameLabel.setText(resultSet.getString("ConsumerName"));
            }
            resultSet.close();
            statement.close();
            mycon.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("profile.fxml"));
            Parent fxml1=loader.load();
            profile controller=loader.getController();
            controller.setUserId(userId);
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}
