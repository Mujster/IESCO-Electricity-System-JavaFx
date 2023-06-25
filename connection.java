package com.example.test3;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class connection {
    private int userId;

    public void setUserId(int id) {
        userId = id;
    }

    @FXML
    private Button BackButton;

    @FXML
    private Label ConnectionLabel;

    @FXML
    private TableColumn<Object[], String> MeterIDColumn;

    @FXML
    private TableColumn<Object[], String> MeterTypeColumn;

    @FXML
    private TableColumn<Object[], LocalDate> DateColumn;
    @FXML
    private Button RequestButton;
    @FXML
    private TableColumn<Object[], Integer> UnitColumn;

    @FXML
    private TableView<Object[]> MeterChart;

    @FXML
    void OnClickBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Consumer.fxml"));
            Parent newPageRoot = loader.load();
            Consumer controller = loader.getController();
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
    void OnClickRequestButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reqconnection.fxml"));
            Parent newPageRoot = loader.load();
            reqconnection controller = loader.getController();
            controller.setUserId(userId);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newPageScene = new Scene(newPageRoot);
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void initialize() {
        try {
            Db db = new Db();
            Connection connection = db.getConnection();
            PreparedStatement sts=connection.prepareStatement("SELECT ConsumerName FROM consumer WHERE ConsumerId=?");
            sts.setInt(1,userId);
            ResultSet rs=sts.executeQuery();
            if(rs.next()) {
                ConnectionLabel.setText(rs.getString("ConsumerName"));
            }
            rs.close();
            sts.close();
            String sqlQuery = "SELECT MeterNumber, MeterType, DateInstalled, UnitsConsumed FROM meter WHERE ConsumerId=?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            MeterIDColumn.setCellValueFactory(cellData -> cellData.getValue()[0] instanceof String ? new SimpleStringProperty((String) cellData.getValue()[0]) : null);
            MeterTypeColumn.setCellValueFactory(cellData -> cellData.getValue()[1] instanceof String ? new SimpleStringProperty((String) cellData.getValue()[1]) : null);
            DateColumn.setCellValueFactory(cellData -> cellData.getValue()[2] instanceof LocalDate ? new SimpleObjectProperty<>((LocalDate) cellData.getValue()[2]) : null);
            UnitColumn.setCellValueFactory(cellData -> cellData.getValue()[3] instanceof Integer ? new SimpleIntegerProperty((Integer) cellData.getValue()[3]).asObject() : null);

            while (resultSet.next()) {
                String meterNumber = resultSet.getString("MeterNumber");
                String meterType = resultSet.getString("MeterType");
                LocalDate dateInstalled = resultSet.getDate("DateInstalled").toLocalDate();
                Integer unitsConsumed = resultSet.getInt("UnitsConsumed");

                Object[] rowData = {meterNumber, meterType, dateInstalled, unitsConsumed};
                MeterChart.getItems().add(rowData);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
