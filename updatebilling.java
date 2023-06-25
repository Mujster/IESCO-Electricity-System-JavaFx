package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class updatebilling {
    private int userId,value,count,value2,monthnumber;
    private double amount;
    private float unitprice,tariffamount;
    private String temp,temp2,monthname;
    @FXML
    private Button AddButton;

    private Date currentTime = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String readingdate = dateFormat.format(currentTime);
    @FXML
    private Button BackButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Label ConsumerExists;

    @FXML
    private TextField ConsumerId;

    @FXML
    private Label MeterExists;

    @FXML
    private TextField MeterId;

    @FXML
    private TextField Units;

    @FXML
    private TextField duedate;

    @FXML
    private TextField reading;

    @FXML
    private Label typelabel;

    @FXML
    void OnClickAddButton(ActionEvent event) {
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT UnitPrice,TariffAmount FROM Tariffs WHERE TariffType=?";
            PreparedStatement st=mycon.prepareStatement(query);
            st.setString(1,temp);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                unitprice=rs.getFloat("UnitPrice");
                tariffamount=rs.getFloat("TariffAmount");
            }
            query="SELECT COUNT(BillId) FROM bill";
            st=mycon.prepareStatement(query);
            rs=st.executeQuery();
            if(rs.next()){
                count= rs.getInt(1);
            }
            count+=1;
            query="INSERT INTO bill(BillId, MeterId, BillStatus, UnitsConsumed, ReadingDate, DueDate, ConsumerId, Month_Name,MonthNumber,BillAmount,BillType) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            st=mycon.prepareStatement(query);
            st.setInt(1,count);
            st.setInt(2,value2);
            st.setString(3,"UnPaid");
            st.setInt(4, Integer.parseInt(Units.getText()));
            st.setString(5,readingdate);
            st.setString(6, duedate.getText());
            st.setString(7, ConsumerId.getText());
            st.setString(8,monthname);
            st.setInt(9,monthnumber);
            amount=unitprice* Integer.parseInt(Units.getText());
            st.setDouble(10,amount);
            st.setString(11, typelabel.getText());
            int r=st.executeUpdate();
            if(r>0){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Billing Updated Successfully");
                alert.showAndWait();
            }
            else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failure");
                alert.setContentText("Billing Consumer Failed");
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

    @FXML
    void OnClickCancelButton(ActionEvent event) {
       ConsumerId.clear();
       MeterId.clear();
       Units.clear();
       reading.setText(readingdate);
       duedate.clear();
       typelabel.setText("e.g Domestic");
    }
    public void setUserId(int id){userId=id;}
    public void initialize(){
        reading.setText(readingdate);
        ConsumerId.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean containsNumber = true;
            for (int i = 0; i < newValue.length(); i++) {
                char c = newValue.charAt(i);
                if (c < '0' || c > '9') {
                    containsNumber = false;
                    break;
                }
            }
            if(containsNumber) {
                try {
                    Db db = new Db();
                    Connection mycon = db.getConnection();
                    value = Integer.parseInt(ConsumerId.getText());
                    String query = "SELECT ConsumerId,ConsumerType FROM consumer WHERE ConsumerId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1, value);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        ConsumerExists.setText("Exists");
                        temp2=rs.getString(1);
                        typelabel.setText(rs.getString("ConsumerType"));
                    } else {
                        ConsumerExists.setText("Not Exists");
                        typelabel.setText(rs.getString("e.g Domestic"));
                    }
                    rs.close();
                    st.close();
                    mycon.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        MeterId.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean containsNumber = true;
            for (int i = 0; i < newValue.length(); i++) {
                char c = newValue.charAt(i);
                if (c < '0' || c > '9') {
                    containsNumber = false;
                    break;
                }
            }
            if(containsNumber) {
                try {
                    Db db = new Db();
                    Connection mycon = db.getConnection();
                    value2 = Integer.parseInt(MeterId.getText());
                    String query = "SELECT MeterId FROM meter WHERE MeterId=? AND ConsumerId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1, value2);
                    st.setInt(2, Integer.parseInt(temp2));
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        MeterExists.setText("Exists");
                    } else {
                        MeterExists.setText("Not Exists");
                    }
                    rs.close();
                    st.close();
                    mycon.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
