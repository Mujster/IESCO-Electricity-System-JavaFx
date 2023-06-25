package com.example.test3;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

public class employee {
    private int userId;
    public void setUserId(int id){userId=id;}
    @FXML
    private Button AccountButton;

    @FXML
    private Button CalendarButton;

    @FXML
    private Button ComplaintButton;

    @FXML
    private Label DeptLabel;

    @FXML
    private Button EditConsumerButton;

    @FXML
    private Label NameLabel;

    @FXML
    private Label RoleLabel;

    @FXML
    private Button LogoutButton;
    @FXML
    private Button UpdateInfoButton;

    @FXML
    private Button UpdateStatusButton;

    @FXML
    private Button UpdateTariffButton;

    @FXML
    void OnClickAccountButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empaccount.fxml"));
            Parent newPageRoot = loader.load();
            EmpAccount controller = loader.getController();
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
    void OnClickCalendarButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendar.fxml"));
            Parent newPageRoot = loader.load();
            calendar controller = loader.getController();
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
    void OnClickComplaintButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("complaint.fxml"));
            Parent newPageRoot = loader.load();
            complaint controller = loader.getController();
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
    void OnClickEditConsumerButton(ActionEvent event) {
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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickLogoutButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent newPageRoot = loader.load();
            Login controller = loader.getController();
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newPageScene = new Scene(newPageRoot);
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickUpdateInfoButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updatebilling.fxml"));
            Parent newPageRoot = loader.load();
            updatebilling controller = loader.getController();
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
    void OnClickUpdateStatusButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updatestatus.fxml"));
            Parent newPageRoot = loader.load();
            updatestatus controller = loader.getController();
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
    void OnClickUpdateTariffButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updatetariff.fxml"));
            Parent newPageRoot = loader.load();
            updatetariff controller = loader.getController();
            controller.setUserId(userId);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newPageScene = new Scene(newPageRoot);
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT EmployeeName,EmployeeRole,EmployeeDept FROM employee WHERE EmployeeId=?";
            PreparedStatement statement = mycon.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                NameLabel.setText(resultSet.getString("EmployeeName"));
                RoleLabel.setText(resultSet.getString("EmployeeRole"));
                DeptLabel.setText(resultSet.getString("EmployeeDept"));
            }
            resultSet.close();
            statement.close();
            mycon.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
}
