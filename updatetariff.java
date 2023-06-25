package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class updatetariff {
    private int userId,count;
    private String tarifftype;
    public void setUserId(int id){userId=id;}
    @FXML
    private Button BackButton;

    @FXML
    private Button CancelButton;

    @FXML
    private MenuItem Commercial;

    @FXML
    private MenuItem Domestic;

    @FXML
    private MenuItem Industrial;

    @FXML
    private TextField Unit;
    @FXML
    private MenuButton typebutton;
    @FXML
    private Button UpdateButton;

    @FXML
    private TextField tariff;

    @FXML
    void OnClickBackButtonima(ActionEvent event) {
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
    void OnClickCancelButton(ActionEvent event) {
       Unit.clear();
       tariff.clear();
       typebutton.setText("Tariff Type");
    }

    @FXML
    void OnClickCommercial(ActionEvent event) {
        tarifftype="Commercial";
        typebutton.setText(tarifftype);
    }

    @FXML
    void OnClickDomestic(ActionEvent event) {
        tarifftype="Domestic";
        typebutton.setText(tarifftype);
    }

    @FXML
    void OnClickIndustrial(ActionEvent event) {
        tarifftype="Industrial";
        typebutton.setText(tarifftype);
    }

    @FXML
    void OnClickUpdateButton(ActionEvent event) {
        try {
            Db db = new Db();
            Connection mycon = db.getConnection();
            PreparedStatement sts = mycon.prepareStatement("UPDATE tariffs SET UnitPrice = ?, TariffAmount = ? WHERE TariffType = ?");
            sts.setFloat(1, Float.parseFloat(Unit.getText()));
            sts.setFloat(2, Float.parseFloat(tariff.getText()));
            sts.setString(3, tarifftype);
            int r = sts.executeUpdate();
            if (r > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Tariffs Updated Successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failure");
                alert.setContentText("Tariffs Update Failed");
                alert.showAndWait();
            }
            sts.close();
            mycon.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
