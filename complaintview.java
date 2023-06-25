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

public class complaintview {
    private int userId,complaintid,consumerid;
    public void setId(int id,int id2,int id3){
        userId=id;
        consumerid=id;
        complaintid=id3;
    }
    @FXML
    private Button BackButton;

    @FXML
    private MenuItem ResolvedMenu;

    @FXML
    private MenuItem UnResolvedMenu;

    @FXML
    private Button UpdateButton;

    @FXML
    private Label complaintlabel;

    @FXML
    private Label datelabel;

    @FXML
    private Label namelabel;

    @FXML
    private Label naturelabel;

    @FXML
    private MenuButton statusbar;
    private String status;
    @FXML
    void OnClickBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("complaint.fxml"));
            Parent newPageRoot = loader.load();
            complaint controller = loader.getController();
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
    void OnClickResolved(ActionEvent event) {
        status="Resolved";
    }

    @FXML
    void OnClickUnResolved(ActionEvent event) {
        status="UnResolved";
    }

    @FXML
    void OnClickUpdateButton(ActionEvent event) {
        try{
            Db db=new Db();
            Connection mycon=db.getConnection();
            PreparedStatement st=mycon.prepareStatement("UPDATE complaint SET ComplaintStatus=? WHERE ComplaintId=?");
            st.setString(1,status);
            st.setInt(2,complaintid);
            int r=st.executeUpdate();
            if(r>0){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Complaint Status Updated Successfully");
                alert.showAndWait();
            }
            else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Complaint Status Update Failed");
                alert.showAndWait();
            }
            mycon.close();
            st.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void initialize(){
       try{
           Db db=new Db();
           Connection mycon=db.getConnection();
           PreparedStatement st=mycon.prepareStatement("SELECT c.ConsumerName, com.ComplaintNature, com.ComplaintDescription, com.ComplaintDate FROM complaint com JOIN consumer c ON com.ConsumerId = c.ConsumerId WHERE ComplaintId=?");
           st.setInt(1,complaintid);
           ResultSet rs=st.executeQuery();
           if(rs.next()){
               namelabel.setText(rs.getString("ConsumerName"));
               naturelabel.setText(rs.getString("ComplaintNature"));
               complaintlabel.setText(rs.getString("ComplaintDescription"));
               datelabel.setText(rs.getString("ComplaintDate"));
           }
           mycon.close();
           rs.close();
           st.close();
       }
       catch(SQLException e){
           e.printStackTrace();
       }
    }
}
