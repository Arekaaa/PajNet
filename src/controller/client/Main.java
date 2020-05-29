package controller.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcomeScreen.fxml"));
        primaryStage.setTitle("PajNet");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 653, 400));
        primaryStage.show();
}
    public static void main(String[] args) {
        launch(args);
    }

}