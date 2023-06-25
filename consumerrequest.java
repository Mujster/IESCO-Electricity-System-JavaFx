package com.example.test3;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class consumerrequest {
    private int userId,consumerId;
    public void setUserId(int id){userId=id;}
    @FXML
    private TableColumn<Object[], Integer> ConsumerIdColumn;
    @FXML
    private TableColumn<Object[], String> ConsumerNameColumn;
    @FXML
    private TableColumn<Object[], LocalDate> RequestDateColumn;
    @FXML
    private TableView<Object[]> RequestTable;

    public void initializepage() {
        try {
            Db db = new Db();
            Connection mycon = db.getConnection();
            String query = "SELECT c.ConsumerId, cr.RequestDate, c.ConsumerName,cr.RequestApproved FROM consumer_request cr JOIN consumer c ON cr.ConsumerId = c.ConsumerId WHERE cr.ConsumerRequest = 'Yes'";
            PreparedStatement statement = mycon.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                if(resultSet.getString("RequestApproved").equals("UnApproved")) {
                consumerId = resultSet.getInt("ConsumerId");
                Date requestDate = resultSet.getDate("RequestDate");
                String consumerName = resultSet.getString("ConsumerName");

                Object[] requestData = {consumerId, requestDate, consumerName};
                RequestTable.getItems().add(requestData);
               }
            }
            resultSet.close();
            statement.close();
            mycon.close();
            ConsumerIdColumn.setCellValueFactory(cellData -> cellData.getValue()[0] instanceof Integer ? new SimpleIntegerProperty((Integer) cellData.getValue()[0]).asObject() : null);
            ConsumerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2].toString()));
            RequestDateColumn.setCellValueFactory(cellData -> {
                Object value = cellData.getValue()[1];
                if (value instanceof LocalDate) {
                    return new SimpleObjectProperty<>((LocalDate) value);
                } else if (value instanceof Date) {
                    return new SimpleObjectProperty<>(((Date) value).toLocalDate());
                } else {
                    return null;
                }
            });
            ConsumerNameColumn.setCellFactory(col -> {
                TableCell<Object[], String> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item); // Use the item directly instead of getString()
                    }
                };

                cell.setOnMouseClicked(event -> {
                    if (!cell.isEmpty()) {
                        String consumerName = cell.getItem(); // Retrieve the consumer name from the cell

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("addconsumer.fxml"));
                            Parent newPageRoot = loader.load();
                            addconsumer controller = loader.getController();
                            controller.setUserId(userId);
                            controller.initialize(consumerId);
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
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}

