package com.example.test3;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class calendar implements Initializable{
    private int userId;

    public void initialize(){}
    public void setUserId(int id){
        userId=id;
    }
    @FXML
    private GridPane Grid;
    @FXML
    private Button BackButton;
    @FXML
    private Button NextMonth;

    @FXML
    private Button PreviousMonth;

    @FXML
    private Label monthYearLabel;

    private YearMonth currentYearMonth;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentYearMonth = YearMonth.now();
        updateCalendar();
    }

    @FXML
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
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
    void OnClickNextMonth(ActionEvent event) {
        nextMonth();
    }

    @FXML
    void OnClickPreviousMonth(ActionEvent event) {
        previousMonth();
    }
    private void updateCalendar() {
        Grid.getChildren().clear();

        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        monthYearLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        int rowIndex = 1;
        int columnIndex = (startDayOfWeek+6) % 7;
        String days[]={"Mo","Tu","We","Th","Fr","Sa","Su"};
        for(int i=0;i<7;i++) {
            Label day=new Label(days[i]);
            Grid.add(day, i, 0);
        }
        for (LocalDate date = firstDayOfMonth; date.getMonth() == currentYearMonth.getMonth(); date = date.plusDays(1)) {
            Label label = new Label(String.valueOf(date.getDayOfMonth()));
            label.getStyleClass().add("calendar-label");

            Grid.add(label, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 7) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }
}
