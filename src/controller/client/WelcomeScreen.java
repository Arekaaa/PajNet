package controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreen {
boolean validateNickBool;
boolean validateReqBool;
    @FXML
    private Button exitButton;

    @FXML
    private Button connectButton;

    @FXML
    private TextField nickField;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox reqBox;

    private void validateNick(){ //Validate nick method
        int minimalLength = 4;
        if (nickField.getText().length() < minimalLength){
            nickField.getStyleClass().add("error");
            errorLabel.setText("Twój nick ma mniej niż cztery znaki!");
            validateNickBool = false;
        }
        else{
            nickField.getStyleClass().remove("error");
            validateNickBool = true;
        }
    }
    private void validateReg(){
        if (!reqBox.isSelected()){
            reqBox.getStyleClass().add("error");
            errorLabel.setText("Musisz zaakceptować regulamin!");
            validateReqBool = false;
        }
        else{
            reqBox.getStylesheets().remove("error");
            validateReqBool = true;
        }
    }

    @FXML
    void onHyperLinkClick(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RegController.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("PajNet");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onConnectClick(ActionEvent event) {
        validateNick();
        validateReg();
        if (validateNickBool && validateReqBool) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ClientController.fxml"));
                Parent root = fxmlLoader.load();
                ClientController client = fxmlLoader.getController();
                client.loadNick(nickField.getText());
                Stage stage = new Stage();
                /*stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        //System.exit(0);
                    }
                });*/
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("PajNet");
                stage.show();
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }

    @FXML
    void onExitClick(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
}