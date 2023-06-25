package com.example.test3;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.sql.*;

public class LodgeComplaint {
        private int userId;
        public void setUserId(int id){
            userId=id;
        }
        @FXML
        private Button BackButton;

        @FXML
        private Button CancelButton;

        @FXML
        private TableView<Object[]> ComplaintTable;

        @FXML
        private TableColumn<Object[],String> ContentColumn;

        @FXML
        private TableColumn<Object[], LocalDate> DateColumn;

        @FXML
        private Button LodgeButton;
        @FXML
        private TextArea contentfield;
        @FXML
        private TextField naturefield;
        @FXML
        private TableColumn<Object[],String> NatureColumn;

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
        void OnClickCancelButton(ActionEvent event) {
            naturefield.clear();
            contentfield.clear();
        }

        @FXML
        void OnClickLodgeButton(ActionEvent event) {
            try{
                Db db=new Db();
                Connection mycon=db.getConnection();
                String query="SELECT COUNT(ComplaintId) FROM complaint";
                PreparedStatement st=mycon.prepareStatement(query);
                ResultSet rs= st.executeQuery();
                if(rs.next()){
                    LocalDate currentDate = LocalDate.now();
                    String dateString = currentDate.toString();
                    int count=rs.getInt(1);
                    st= mycon.prepareStatement("INSERT INTO complaint(ComplaintId,ConsumerId,ComplaintNature,ComplaintDescription,ComplaintStatus,ComplaintDate)VALUES(?,?,?,?,?,?)");
                    st.setInt(1,count+1);
                    st.setInt(2,userId);
                    st.setString(3,naturefield.getText());
                    st.setString(4,contentfield.getText());
                    st.setString(5,"UnResolved");
                    st.setString(6,dateString);
                    int r=st.executeUpdate();
                    if(r>0){
                        initialize();
                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setContentText("Complaint Lodged Successfully");
                        alert.showAndWait();
                    } else {
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Complaint Failed");
                        alert.showAndWait();
                    }
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        public void initialize(){
            try{
                Db db=new Db();
                Connection mycon=db.getConnection();
                PreparedStatement st=mycon.prepareStatement("SELECT ComplaintNature,ComplaintDescription,ComplaintDate FROM complaint WHERE ConsumerId=?");
                st.setInt(1,userId);
                ResultSet rs= st.executeQuery();

                NatureColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[0]));
                ContentColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[1]));
                DateColumn.setCellValueFactory(cellData -> cellData.getValue()[2] instanceof LocalDate ? new SimpleObjectProperty<>((LocalDate) cellData.getValue()[2]) : null);

                ComplaintTable.getItems().clear();

                while (rs.next()) {
                    String comnature = rs.getString("ComplaintNature");
                    String content = rs.getString("ComplaintDescription");
                    LocalDate complaintDate = rs.getDate("ComplaintDate").toLocalDate();

                    Object[] rowData = {comnature, content, complaintDate};
                    ComplaintTable.getItems().add(rowData);
                }
                rs.close();
                st.close();
                mycon.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
}
