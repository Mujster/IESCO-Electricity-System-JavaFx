package com.example.test3;

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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class billinghistory {
    private int userId;

    public void setUserId(int id) {
        userId = id;
    }

    @FXML
    private Button BackButton;

    @FXML
    private TableColumn<Object[], Double> BillAmountColumn;

    @FXML
    private TableColumn<Object[], String> CNICColumn;

    @FXML
    private TableColumn<Object[], LocalDate> DateColumn;
    @FXML
    private TableColumn<Object[], String> billstatus;
    @FXML
    private Label MonthLabel;

    @FXML
    private TableColumn<Object[], Integer> UnitColumn;

    @FXML
    private TableView<Object[]> UsageTable;

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

    void initialize() {
        LocalDate currentdate= LocalDate.now();
        String month=currentdate.toString();
        MonthLabel.setText(month);
        try {
            Db db=new Db();
            Connection connection = db.getConnection();
            // Create and execute the SQL query
            String sqlQuery = "SELECT b.ReadingDate, c.ConsumerCNIC, b.UnitsConsumed, b.BillAmount,b.BillStatus FROM bill b JOIN consumer c ON b.ConsumerId = c.ConsumerId WHERE c.ConsumerId =?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            // Set up column cell value factories
            BillAmountColumn.setCellValueFactory(cellData -> cellData.getValue()[3] instanceof Double ? new SimpleDoubleProperty((Double) cellData.getValue()[3]).asObject() : null);
            CNICColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[1]));
            DateColumn.setCellValueFactory(cellData -> cellData.getValue()[0] instanceof LocalDate ? new SimpleObjectProperty<>((LocalDate) cellData.getValue()[0]) : null);
            UnitColumn.setCellValueFactory(cellData -> cellData.getValue()[2] instanceof Integer ? new SimpleIntegerProperty((Integer) cellData.getValue()[2]).asObject() : null);
            billstatus.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[4]));

            // Iterate over the result and add data to the table
            while (resultSet.next()) {
                LocalDate readingDate = resultSet.getDate("ReadingDate").toLocalDate();
                String consumerCNIC = resultSet.getString("ConsumerCNIC");
                Integer unitsConsumed = resultSet.getInt("UnitsConsumed");
                Double billAmount = resultSet.getDouble("BillAmount");
                String bill_status=resultSet.getString("BillStatus");
                Object[] rowData = {readingDate, consumerCNIC, unitsConsumed, billAmount,bill_status};
                UsageTable.getItems().add(rowData);
            }

            // Close the database connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
