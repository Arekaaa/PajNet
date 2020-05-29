package controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WelcomeScreen {

    @FXML
    private Button exitButton;

    @FXML
    private Button connectButton;

    @FXML
    private TextField nickField;

    @FXML
    private Label errorLabel;

    private void validateNick(){ //Validate nick method
        int minimalLength = 4;
        if (nickField.getText().length() < minimalLength){
            nickField.getStyleClass().add("error");
            errorLabel.setText("Twój nick musi posiadać minimum 4 znaki!");
        }
        else{
            errorLabel.setText("");
            nickField.getStyleClass().remove("error");
        }
    }

    @FXML
    void onConnectClick(ActionEvent event) {
        validateNick();



    }

    @FXML
    void onExitClick(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

}