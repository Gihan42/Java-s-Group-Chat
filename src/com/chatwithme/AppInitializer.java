package com.chatwithme;

import com.chatwithme.Controllers.serverController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/chatwithme/FXML/server.fxml"));
        Scene server = new Scene(loader.load());
        primaryStage.setScene(server);
        primaryStage.setTitle("Server App");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        // passing data via the controller
        try {
            serverController controller = loader.getController();
            controller.initData(primaryStage);
        }catch (NullPointerException e){
            System.out.println(e);
        }
        primaryStage.show();

    }
}
