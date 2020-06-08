package controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class ClientController  {
    private static final int PORT = 4541;

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
    private Button logOutButton;

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
                printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8),true);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));

                try {
                    printWriter.print(data() + "~" + nickname + "- " + "przychodzi. Przywitaj się :) " + newLine);
                    printWriter.flush();
                    String welcome = bufferedReader.readLine();
                    String[] subWelcomeDate = welcome.split("~");
                    String[] subWelcomeNickname = subWelcomeDate[1].split("-");
                    if (!subWelcomeNickname[0].equals(nickname)) {
                        messagesArea.appendText(welcome + newLine);
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                connectButton.setDisable(true);
                disconnectButton.setDisable(false);
                logOutButton.setDisable(true);
                sendButton.setDisable(false);
                ipField.setDisable(true);
                messageField.setDisable(false);

            } catch (IOException e) {
                connectButton.setDisable(false);
                disconnectButton.setDisable(true);
                logOutButton.setDisable(false);
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
                                String[] subStringDate = tekst.split("~");
                                String[] subString = subStringDate[1].split("-");  // Nie chcemy pobierać swojej wiadomości z serwera aby nie widzieć ich podwójnie
                                if (subString[0].equals(nickname)) { // Jeżeli to co przyjdzie nie jest naszym imieniem
                                    messagesArea.appendText(subStringDate[0]+"~ Ty- " + subString[1] + newLine);
                                } else {
                                    messagesArea.appendText(tekst + newLine);
                                }
                            }
                    } catch (Exception e) {
                        System.out.println("Utracono połączenie z serwerem");
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }

    @FXML
    void onDisconnectClick(ActionEvent event){
        try{
            messagesArea.appendText("Rozłączono z serwerem." +newLine);
            printWriter.println(data() + "~" + nickname +" rozłączył się.");
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
        logOutButton.setDisable(false);
        sendButton.setDisable(true);
        ipField.setDisable(false);
        messageField.setDisable(true);
    }

    @FXML
    public void messageFieldAction(ActionEvent event) {
        wiadomosc = messageField.getText();
        messageField.clear();

        if(!wiadomosc.isEmpty()) {
            try {
                printWriter.println(data() + "~" + nickname + "- " + wiadomosc);
                printWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onSendClick(ActionEvent event) {
        wiadomosc = messageField.getText();
        messageField.clear();

        if(!wiadomosc.isEmpty()) {
            try {
                printWriter.println(data() + "~" + nickname + "- " + wiadomosc);
                printWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String data() {
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
        return czas;
    }

}





















