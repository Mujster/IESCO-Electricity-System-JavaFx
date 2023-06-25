package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpAccount{
    private int userId;
    public void setUserId(int id){userId=id;}
    @FXML
    private Button BackButton;

    @FXML
    private Label NameLabel;

    @FXML
    private Button PasswordButton;

    @FXML
    private Button ProfileButton;

    @FXML
    private StackPane contentArea;

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
    void OnClickPasswordButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("EmpPassChange.fxml"));
            Parent fxml=loader.load();
            EmpPassChange controller=loader.getController();
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
            FXMLLoader loader=new FXMLLoader(getClass().getResource("EmpProfile.fxml"));
            Parent fxml1=loader.load();
            EmpProfile controller=loader.getController();
            controller.setUserId(userId);
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    void initialize(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT EmployeeName FROM employee WHERE EmployeeId=?";
            PreparedStatement statement = mycon.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                NameLabel.setText(resultSet.getString("EmployeeName"));
            }
            resultSet.close();
            statement.close();
            mycon.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("EmpProfile.fxml"));
            Parent fxml1=loader.load();
            EmpProfile controller=loader.getController();
            controller.setUserId(userId);
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml1);
        }
        catch(IOException ex){
            Logger.getLogger(account.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}
