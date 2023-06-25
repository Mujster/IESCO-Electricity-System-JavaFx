package com.example.test3;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class complaint {
    private int userId,complaintID,consumerID;
    public void setUserId(int id){userId=id;}
    @FXML
    private Button BackButton;

    @FXML
    private TableView<Object[]> ComplaintChart;
    @FXML
    private TableColumn<Object[],String> ComplaintIdColumn;

    @FXML
    private TableColumn<Object[],LocalDate> DateColumn;

    @FXML
    private TableColumn<Object[],String> NameColumn;

    @FXML
    private TableColumn<Object[],String> NatureColumn;

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
    void initialize() {
        try {
            Db db = new Db();
            Connection connection = db.getConnection();

            String sqlQuery = "SELECT c.ComplaintId,con.ConsumerId, con.ConsumerName, c.ComplaintNature, c.ComplaintDate FROM complaint c JOIN consumer con ON c.ConsumerId = con.ConsumerId WHERE c.ComplaintStatus=?";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1,"UnResolved");
            ResultSet resultSet = statement.executeQuery();

            ComplaintIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0].toString()));
            NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1].toString()));
            NatureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2].toString()));
            DateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(LocalDate.parse(cellData.getValue()[3].toString())));

            while(resultSet.next()){
                String complaintId = resultSet.getString("ComplaintId");
                complaintID=Integer.parseInt(complaintId);
                consumerID=Integer.parseInt(resultSet.getString("ConsumerId"));
                String name = resultSet.getString("ConsumerName");
                LocalDate complaintDate = resultSet.getDate("ComplaintDate").toLocalDate();
                String nature = resultSet.getString("ComplaintNature");
                Object[] rowData = {complaintId, name, nature, complaintDate};
                //ComplaintChart.getItems().clear();
                ComplaintChart.getItems().add(rowData);
            }
            resultSet.close();
            statement.close();
            connection.close();
            NatureColumn.setCellFactory(col -> {
                TableCell<Object[], String> cell = new TableCell<Object[], String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : getString());
                    }
                    private String getString() {
                        return getItem() == null ? "" : getItem().toString();
                    }
                };

                cell.setOnMouseClicked(event -> {
                    if (!cell.isEmpty()) {
                        String nature = cell.getItem();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("complaintview.fxml"));
                            Parent newPageRoot = loader.load();
                            complaintview controller = loader.getController();
                            controller.setId(userId,consumerID,complaintID);
                            controller.initialize();
                            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Scene newPageScene = new Scene(newPageRoot);
                            primaryStage.setScene(newPageScene);
                            primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return cell;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
   }
}
