package com.example.test3;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;
import java.io.*;

public class Login {
    private int userId;
    @FXML
    private Button SignupButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button Loginbutton;
    @FXML
    void OnClickSignupButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent newPageRoot = loader.load();
            signup controller = loader.getController();
            Scene newPageScene = new Scene(newPageRoot);
            Stage primaryStage = (Stage) Loginbutton.getScene().getWindow();
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void LoginClick(ActionEvent actionEvent) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Invalid Credentials");
        alert.setHeaderText(null);
        alert.setContentText("Invalid username or password!");
        Alert success=new Alert(AlertType.INFORMATION);
        success.setTitle("Login Successful");
        success.setHeaderText(null);
        success.setContentText("Login Successful");
        if(!ConsumerLogin()){
            if(!EmployeeLogin()){
                alert.showAndWait();
            }
            else{
                success.showAndWait();
                try{
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("employee.fxml"));
                    Parent newPageRoot = loader.load();
                    employee controller = loader.getController();
                    controller.setUserId(userId);
                    controller.initialize();
                    Scene newPageScene = new Scene(newPageRoot);
                    Stage primaryStage = (Stage) Loginbutton.getScene().getWindow();
                    primaryStage.setScene(newPageScene);
                    primaryStage.show();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            success.showAndWait();
            try{
                FXMLLoader loader=new FXMLLoader(getClass().getResource("Consumer.fxml"));
                Parent newPageRoot = loader.load();
                Consumer controller = loader.getController();
                controller.setUserId(userId);
                controller.initialize();
                Scene newPageScene = new Scene(newPageRoot);
                Stage primaryStage = (Stage) Loginbutton.getScene().getWindow();
                primaryStage.setScene(newPageScene);
                primaryStage.show();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void initialize(){
        Loginbutton.setOnAction(this::LoginClick);
    }
    Boolean ConsumerLogin(){
        String userName=username.getText();
        String passWord=password.getText();
        try{
            Connection mycon=Db.getConnection();
            String query="SELECT *FROM consumer WHERE ConsumerUsername=? AND ConsumerPassword=?";
            PreparedStatement statement = mycon.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, passWord);
            ResultSet resultSet = statement.executeQuery();
            boolean loginSuccess = resultSet.next();
            if(loginSuccess){
                userId=resultSet.getInt("ConsumerId");
                resultSet.close();
                statement.close();
                mycon.close();
            }
            else {
                resultSet.close();
                statement.close();
                mycon.close();
            }
            return loginSuccess;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    Boolean EmployeeLogin(){
        String userName=username.getText();
        String passWord=password.getText();
        try{
            Connection mycon=Db.getConnection();
            String query="SELECT * FROM employee WHERE EmployeeUsername = ? AND EmployeePassword = ?";
            PreparedStatement statement = mycon.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, passWord);
            ResultSet resultSet = statement.executeQuery();
            boolean loginSuccess = resultSet.next();
            if(loginSuccess){
                userId=resultSet.getInt("EmployeeId");
                resultSet.close();
                statement.close();
                mycon.close();
            }
            else {
                resultSet.close();
                statement.close();
                mycon.close();
            }
            return loginSuccess;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}