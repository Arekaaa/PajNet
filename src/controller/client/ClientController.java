package controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController  {
    private static final int PORT = 4500;

    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String nickname;
    private String newLine ="\n";
    private String wiadomosc;
    private String IP;
    private boolean validateIp;

    @FXML
    private Button sendButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private TextField ipField;

    @FXML
    private TextField messageField;

    @FXML
    private Label nickLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private TextArea messagesArea;

    void loadNick(String nick) {
        nickLabel.setText(nick);
        nickname = nickLabel.getText();
    }

    private void validateIp(){ //Validate nick method
        if (ipField.getText().isEmpty()){
            ipField.getStyleClass().add("error");
            errorLabel.setText("Podaj adres IP serwera!");
            validateIp = false;
        }
        else{
            ipField.getStyleClass().remove("error");
            errorLabel.setText("");
            validateIp = true;
        }
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
    void onConnectClick(ActionEvent event) {
        validateIp();
        if(validateIp) {
            IP = ipField.getText();
            try {
                socket = new Socket(IP, PORT);
                System.out.println("Podłączono do " + socket);
                messagesArea.appendText("Połączono z " + socket + "." + newLine);
                printWriter = new PrintWriter(socket.getOutputStream());
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                connectButton.setDisable(true);
                disconnectButton.setDisable(false);
                sendButton.setDisable(false);
                ipField.setDisable(true);
                messageField.setDisable(false);

            } catch (IOException e) {
                connectButton.setDisable(false);
                disconnectButton.setDisable(true);
                sendButton.setDisable(true);
                ipField.setDisable(false);
                messageField.setDisable(true);
                messagesArea.appendText("Błąd połączenia! Sprawdź adres IP." + newLine);
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String tekst;

                    try {
                        while ((tekst = bufferedReader.readLine()) != null) {
                            System.out.println(tekst);
                            messagesArea.appendText(tekst + newLine);
                        }
                    } catch (Exception e) {
                        System.out.println("Utracono połączenie z serwerem");
                    }
                }
            });
            t.start();
        }
    }

    @FXML
    void onDisconnectClick(ActionEvent event){
        try{
            messagesArea.appendText("Rozłączono z serwerem." +newLine);
            printWriter.println(nickname + " rozłączył się");
            printWriter.flush();
            printWriter.close();
            bufferedReader.close();
            socket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        sendButton.setDisable(true);
        ipField.setDisable(false);
        messageField.setDisable(true);
    }

    @FXML
    public void messageFieldAction(ActionEvent event) {
        wiadomosc = messageField.getText();
        messageField.clear();

        try {
                printWriter.println(nickname + ": " + wiadomosc);
                printWriter.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onSendClick(ActionEvent event) {
        wiadomosc = messageField.getText();
        messageField.clear();

        try {
            printWriter.println(nickname + ": " + wiadomosc);
            printWriter.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    }





















