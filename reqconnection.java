package com.example.test3;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class reqconnection {
    private int userId;
    private String type,ctype,name,address,cnic,contact;
    public void setUserId(int id){
        userId=id;
    }
    @FXML
    private Button ApplyButton;

    @FXML
    private Button BackButton;

    @FXML
    private MenuItem CommercialMenu;

    @FXML
    private MenuButton connectiontype;
    @FXML
    private MenuItem DomesticMenu;

    @FXML
    private MenuItem IndustrialMenu;

    @FXML
    private Button CancelButton;

    @FXML
    private MenuItem OnePhaseMenu;

    @FXML
    private MenuItem ThreePhaseMenu;

    @FXML
    private MenuButton TypeMenu;

    @FXML
    void OnClickApplyButton(ActionEvent event) {
         try{
             Db db=new Db();
             Connection  mycon=db.getConnection();
             String query="SELECT * FROM consumer WHERE ConsumerId=?";
             PreparedStatement st= mycon.prepareStatement(query);
             st.setInt(1,userId);
             ResultSet rs=st.executeQuery();
             if(rs.next()){
                 cnic=rs.getString("ConsumerCNIC");
                 name=rs.getString("ConsumerName");
                 contact=rs.getString("ConsumerContact");
             }
             st.close();
             rs.close();
             query="INSERT INTO connection_request(ConsumerId,ConsumerName,ConsumerCNIC,ConsumerContact,MeterType,ConnectionType,ConnectionRequest) VALUES(?,?,?,?,?,?,?)";
             st= mycon.prepareStatement(query);
             st.setInt(1,userId);
             st.setString(2,name);
             st.setString(3,cnic);
             st.setString(4,contact);
             st.setString(5,type);
             st.setString(6,ctype);
             st.setString(7,"UnApproved");
             int r=st.executeUpdate();
             if(r>0){
                 Alert alert=new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Success");
                 alert.setContentText("Connection Application Made Successfully");
                 alert.showAndWait();
             }
             else{
                 Alert alert=new Alert(Alert.AlertType.WARNING);
                 alert.setTitle("Failure");
                 alert.setContentText("Connection Request Failed");
                 alert.showAndWait();
             }
         }
         catch(SQLException e){
             e.printStackTrace();
         }
    }
    @FXML
    void OnClickBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("connection.fxml"));
            Parent newPageRoot = loader.load();
            connection controller = loader.getController();
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
    void OnClickCancelButton(ActionEvent event) {
         TypeMenu.setText("Meter Type");
         connectiontype.setText("Connection Type");
    }

    @FXML
    void OnClickOnePhaseMenu(ActionEvent event) {
         type="1-Phase";
        TypeMenu.setText(type);
    }
    @FXML
    void OnClickCommercialMmenuenu(ActionEvent event) {
        ctype="Commercial";
        connectiontype.setText(ctype);
    }

    @FXML
    void OnClickDomesticMenu(ActionEvent event) {
        ctype="Domestic";
        connectiontype.setText(ctype);
    }

    @FXML
    void OnClickIndustrialMenu(ActionEvent event) {
        ctype="Industrial";
        connectiontype.setText(ctype);
    }
    @FXML
    void OnClickThreePhaseMenu(ActionEvent event) {
         type="3-Phase";
        TypeMenu.setText(type);
    }
}
