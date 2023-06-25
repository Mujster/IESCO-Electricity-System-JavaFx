package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.AbstractList;

public class addconnection {
    private int value;
    private String consumertype,metertype;
    @FXML
    private Button AddButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Label ExistsLabel;

    @FXML
    private MenuItem CommericalMenu;

    @FXML
    private MenuButton ConnectionType;

    @FXML
    private MenuItem DomesticMenu;
    @FXML
    private TextField MeterNumber;
    @FXML
    private MenuItem IndustrialMenu;

    @FXML
    private MenuButton MeterType;

    @FXML
    private MenuItem OnePhaseMenu;
    @FXML
    private TextField ConsumerIdField;

    @FXML
    private MenuItem ThreePhaseMenu;

    @FXML
    void OnClickAddButton(ActionEvent event) {
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT COUNT(MeterId) FROM meter";
            PreparedStatement st=mycon.prepareStatement(query);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                value= rs.getInt(1);
            }
            rs.close();
            value+=1;
            LocalDate currentDate = LocalDate.now();
            String dateString = currentDate.toString();
            query="INSERT INTO meter(MeterId,MeterNumber,MeterType,DateInstalled,ConsumerId) VALUES(?,?,?,?,?)";
            st=mycon.prepareStatement(query);
            st.setInt(1,value);
            st.setString(2,MeterNumber.getText());
            st.setString(3,metertype);
            st.setString(4,dateString);
            st.setInt(5, Integer.parseInt(ConsumerIdField.getText()));
            int row= st.executeUpdate();
            if(row>0){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Connection Added Successfully");
                alert.showAndWait();
            }
            else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failure");
                alert.setContentText("Connection Addition Failed");
                alert.showAndWait();
            }
            st=mycon.prepareStatement("UPDATE connection_request SET ConnectionRequest=? WHERE ConsumerId=?");
            st.setString(1,"Approved");
            st.setInt(2,Integer.parseInt(ConsumerIdField.getText()));
            int r=st.executeUpdate();
            mycon.close();
            st.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickCancelButton(ActionEvent event) {
        ConsumerIdField.clear();
        MeterNumber.clear();
        ConnectionType.setText("ConnectionType");
        MeterType.setText("MeterType");
    }

    @FXML
    void OnClickCommericalMenu(ActionEvent event) {
        consumertype="Commercial";
        ConnectionType.setText("Commercial");
    }

    @FXML
    void OnClickConnectionType(ActionEvent event) {

    }

    @FXML
    void OnClickDomesticMenu(ActionEvent event) {
        consumertype="Domestic";
        ConnectionType.setText("Domestic");
    }

    @FXML
    void OnClickIndustrialMenu(ActionEvent event) {
        consumertype="Industrial";
        ConnectionType.setText("Industrial");
    }

    @FXML
    void OnClickMeterType(ActionEvent event) {

    }

    @FXML
    void OnClickOnePhaseMenu(ActionEvent event) {
        metertype="1-Phase";
        MeterType.setText("1-Phase");
    }

    @FXML
    void OnClickThreePhaseMenu(ActionEvent event) {
        metertype="3-Phase";
        MeterType.setText("3-Phase");
    }
    public void initialize(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            PreparedStatement st=mycon.prepareStatement("SELECT ConsumerId,ConsumerName,ConsumerCNIC FROM connection_request WHERE ConnectionRequest=?");
            st.setString(1,"UnApproved");
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                String name=rs.getString("ConsumerName");
                String cnic=rs.getString("ConsumerCNIC");
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New Request For Connection");
                alert.setHeaderText("Request For New Connection");
                alert.setContentText(name+" requested for new connection\n Consumer Cnic "+cnic);
                alert.showAndWait();
            }
            mycon.close();
            st.close();
            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        ConsumerIdField.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    value = Integer.parseInt(ConsumerIdField.getText());
                    String query = "SELECT * FROM consumer WHERE ConsumerId=?";
                    PreparedStatement st = mycon.prepareStatement(query);
                    st.setInt(1, value);
                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        ExistsLabel.setText("Exists");
                        mycon.close();
                    } else {
                        ExistsLabel.setText("Not Exists");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(!containsNumber || newValue==null){
                ExistsLabel.setText("Invalid Input");
            }
        });
    }
}
