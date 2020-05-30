package controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;

public class ClientController {

    @FXML
    private TextField messageField;

    @FXML
    private Label nickLabel;

    @FXML
    private ListView<?> messageList;

    void loadNick(String nick){
    nickLabel.setText(nick);

    }


    void time(){
        Calendar now = Calendar.getInstance();
        String minuta;
        String godzina;
        if (now.get(Calendar.MINUTE) <= 9) {
            minuta = "0" + now.get(Calendar.MINUTE);
        } else {
            minuta = Integer.toString(now.get(Calendar.MINUTE));
        }
        if (now.get(Calendar.HOUR_OF_DAY) <= 9) {
            godzina = "0" + now.get(Calendar.HOUR_OF_DAY);
        } else {
            godzina = Integer.toString(now.get(Calendar.HOUR_OF_DAY));
        }
        String czas = godzina + ":" + minuta;
    }

    @FXML
    void onLogOutClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcomeScreen.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("PajNet");
            stage.show();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSendClick(ActionEvent event) {

    }

}
