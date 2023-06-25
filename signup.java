package com.example.test3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class signup {
    private int count;
    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private PasswordField conpass;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField username;

    @FXML
    void OnClickSignInButton(ActionEvent event) {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent newPageRoot = loader.load();
            Login controller = loader.getController();
            Scene newPageScene = new Scene(newPageRoot);
            Stage primaryStage = (Stage) SignInButton.getScene().getWindow();
            primaryStage.setScene(newPageScene);
            primaryStage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickSignUpButton(ActionEvent event) {
        if(pass.getText().equals(conpass.getText())){
            Date currentTime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String curr = dateFormat.format(currentTime);
          try {
              Db db = new Db();
              Connection mycon = db.getConnection();
              PreparedStatement st = mycon.prepareStatement("SELECT COUNT(ConsumerId) FROM consumer");
              ResultSet rs = st.executeQuery();
              if (rs.next()) {
                  count = rs.getInt(1);
              }
              st = mycon.prepareStatement("SELECT ConsumerUsername FROM Consumer WHERE ConsumerUsername=?");
              st.setString(1, username.getText());
              rs = st.executeQuery();
              if (!rs.next()) {
                  st = mycon.prepareStatement("INSERT INTO consumer(ConsumerId, ConsumerUsername, ConsumerPassword) VALUES(?,?,?)");
                  st.setInt(1, count + 1);
                  st.setString(2, username.getText());
                  st.setString(3, conpass.getText());
                  int r = st.executeUpdate();

                  st = mycon.prepareStatement("INSERT INTO consumer_request(ConsumerId,ConsumerRequest,RequestApproved,RequestDate)VALUES(?,?,?,?)");
                  st.setInt(1, count + 1);
                  st.setString(2, "Yes");
                  st.setString(3, "UnApproved");
                  st.setString(4, curr);
                  int row = st.executeUpdate();
                  if (r > 0 && row > 0) {
                      Alert alert = new Alert(Alert.AlertType.INFORMATION);
                      alert.setTitle("Success");
                      alert.setHeaderText("Signed Up Successfully");
                      alert.setContentText("Your Request Will Be Verified Soon");
                      alert.showAndWait();
                      try {
                          FXMLLoader loader = new FXMLLoader(getClass().getResource("signupnextform.fxml"));
                          Parent newPageRoot = loader.load();
                          signupnextform controller = loader.getController();
                          controller.setUserId(count+1);
                          Scene newPageScene = new Scene(newPageRoot);
                          Stage primaryStage = (Stage) SignUpButton.getScene().getWindow();
                          primaryStage.setScene(newPageScene);
                          primaryStage.show();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }
              }
              else{
                  Alert alert=new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error Signing Up");
                  alert.setContentText("User Already Exists");
                  alert.showAndWait();
              }
          }
          catch(SQLException e){
              e.printStackTrace();
          }
        }
        else {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Signing Up");
            alert.setContentText("Passwords Do Not Match");
            alert.showAndWait();
        }
    }

}
