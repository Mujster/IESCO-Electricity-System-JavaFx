package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;
import java.sql.SQLException;


public class generatebill {
    private int billId,userId;
    private String consumertype;
    void setUserId(int id){
        userId=id;
    }
    void setBillId(int id){
        billId=id;
    }
    @FXML
    private Label AddressLabel;

    @FXML
    private Label AmountLabel;

    @FXML
    private Button BackButton;

    @FXML
    private Label BillNumberLabel;

    @FXML
    private Label ConsumerLabel;

    @FXML
    private Label DueDateLabel;

    @FXML
    private Label MonthLabel;

    @FXML
    private Button PrintButton;

    @FXML
    private Label ReadingDateLabel;

    @FXML
    private Label TariffLabel;

    @FXML
    private Label TaxLabel;

    @FXML
    private Label TotalAmountAfterLabel;

    @FXML
    private Label TotalAmountDueLabel;

    @FXML
    private Label UnitLabel;

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
    void OnClickPrintButton(ActionEvent event) {

    }
    void initialize(){
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            String query="SELECT c.ConsumerType,c.ConsumerName, c.ConsumerAddress,b.UnitsConsumed, b.ReadingDate, b.DueDate, b.Month_Name FROM consumer c JOIN bill b ON c.ConsumerId = b.ConsumerId WHERE c.ConsumerId = ? AND b.BillId = ?";
            PreparedStatement stmt = mycon.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, billId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                consumertype=rs.getString("ConsumerType");
                AddressLabel.setText(rs.getString("ConsumerAddress"));
                BillNumberLabel.setText(Integer.toString(billId));
                ConsumerLabel.setText(rs.getString("ConsumerName"));
                DueDateLabel.setText(rs.getString("DueDate"));
                MonthLabel.setText(rs.getString("Month_Name"));
                ReadingDateLabel.setText(rs.getString("ReadingDate"));
                UnitLabel.setText(rs.getString("UnitsConsumed"));
            }
            rs.close();
            stmt.close();
            String sqlquery="SELECT UnitPrice,TariffAmount FROM Tariffs WHERE TariffType=?";
            PreparedStatement sts=mycon.prepareStatement(sqlquery);
            sts.setString(1,consumertype);
            ResultSet srs=sts.executeQuery();
            if(srs.next()){
                TaxLabel.setText(srs.getString("TariffAmount"));
                double UnitPrice=srs.getDouble("UnitPrice");
                float tax = Float.parseFloat(TaxLabel.getText());
                int unit = Integer.parseInt(UnitLabel.getText());
                double amount=UnitPrice*unit;
                double temp=tax*amount;
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                AmountLabel.setText(Double.toString(amount));
                amount+=temp;
                String formattedNumber=decimalFormat.format(temp);
                TariffLabel.setText(formattedNumber);
                formattedNumber = decimalFormat.format(amount);
                TotalAmountDueLabel.setText(formattedNumber);
                amount+=230;
                formattedNumber = decimalFormat.format(amount);
                TotalAmountAfterLabel.setText(formattedNumber);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
