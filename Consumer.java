package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;



public class Consumer {
    public int userId;
    public void setUserId(int id){
        userId=id;
    };
    private ActionEvent event;
    @FXML
    private Label AddressLabel;
    @FXML
    private Button AddComplaintButton;
    @FXML
    private Button BillingButton;


    @FXML
    void OnClickAddComplaintButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LodgeComplaint.fxml"));
            Parent newPageRoot = loader.load();
            LodgeComplaint controller = loader.getController();
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
    private NumberAxis Units;
    @FXML
    private CategoryAxis Months;
    @FXML
    private Label BillingMonthLabel;

    @FXML
    private Label CNICLabel;

    @FXML
    private Button CalculateBillButton;

    @FXML
    private Button ConnectionButton;

    @FXML
    private Label ContactLabel;

    @FXML
    private Button LogoutButton;

    @FXML
    private Label NameLabel;

    @FXML
    private LineChart<String,Number> UsageChart;

    @FXML
    private Button accountButton;

    @FXML
    void OnClickAccountButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Parent newPageRoot = loader.load();
            account controller = loader.getController();
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
    void OnClickBillingButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("billinghistory.fxml"));
            Parent newPageRoot = loader.load();
            billinghistory controller = loader.getController();
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
    void OnClickCalculateBillButton(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("generatebill.fxml"));
            Parent newPageRoot = loader.load();
            generatebill controller = loader.getController();
            LocalDate currentdate= LocalDate.now();
            int currentmonth=currentdate.getMonthValue();
            try {
                Db db = new Db();
                Connection mycon = db.getConnection();
                String query = "SELECT MonthNumber,BillId, BillStatus FROM bill WHERE MonthNumber=MONTH(CURRENT_DATE())";
                PreparedStatement statement = mycon.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                String status = rs.getString("BillStatus");
                int bill=rs.getInt("BillId");
                if (status.equals("Paid")) {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Billing Month Status");
                    alert.setContentText("Bill Already Paid For "+Month.of(currentmonth).name());
                    alert.showAndWait();
                } else {
                    controller.setUserId(userId);
                    controller.setBillId(bill);
                    controller.initialize();
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene newPageScene = new Scene(newPageRoot);
                    primaryStage.setScene(newPageScene);
                    primaryStage.show();
                }
              }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
       }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickConnectionButton(ActionEvent event) {
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
    private void initializeLabel(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT ConsumerName,ConsumerCNIC,ConsumerContact,ConsumerAddress FROM consumer WHERE ConsumerId=?";
            PreparedStatement statement = mycon.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                NameLabel.setText(resultSet.getString("ConsumerName"));
                CNICLabel.setText(resultSet.getString("ConsumerCNIC"));
                ContactLabel.setText(resultSet.getString("ConsumerContact"));
                AddressLabel.setText(resultSet.getString("ConsumerAddress"));
            }
            resultSet.close();
            statement.close();
            mycon.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void initializechart() {
        UsageChart = new LineChart<>(Months,Units);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            Db db = new Db();
            Connection con = db.getConnection();
            PreparedStatement st = con.prepareStatement("SELECT ConsumedUnits,Month_Name FROM consumerusage WHERE ConsumerId=?");
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String monthName = rs.getString("Month_Name");
                int consumedUnits = rs.getInt("ConsumedUnits");
                series.getData().add(new XYChart.Data<>(monthName, consumedUnits));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        UsageChart.getData().add(series);
    }
    @FXML
    public void initialize(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            PreparedStatement st=mycon.prepareStatement("SELECT RequestApproved FROM consumer_request WHERE ConsumerId=?");
            st.setInt(1,userId);
            ResultSet rs= st.executeQuery();
            if(rs.next()){
                String request=rs.getString("RequestApproved");
                if(request.equals("Approved")){
                    LogoutButton.setOnAction(this::OnClickLogoutButton);
                    ConnectionButton.setOnAction(this::OnClickConnectionButton);
                    CalculateBillButton.setOnAction(this::OnClickCalculateBillButton);
                    BillingButton.setOnAction(this::OnClickBillingButton);
                    accountButton.setOnAction(this::OnClickAccountButton);
                    initializeLabel();
                    UsageChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
                    initializechart();
                }
                else{
                    Alert success=new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Access Denied");
                    success.setHeaderText(null);
                    success.setContentText("Your Request Is Not Approved");
                    success.showAndWait();
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
                rs.close();
                st.close();
                mycon.close();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
