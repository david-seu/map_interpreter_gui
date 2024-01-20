package com.example.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader listLoader = new FXMLLoader();
        listLoader.setLocation(getClass().getResource("list-view.fxml"));
        Parent root = listLoader.load();
        ListController listController = listLoader.getController();
        stage.setTitle("Select");
        stage.setScene(new Scene(root, 500, 550));
        stage.show();

        FXMLLoader programLoader = new FXMLLoader();
        programLoader.setLocation(getClass().getResource("main-view.fxml"));
        Parent programRoot = programLoader.load();
        MainController mainController = programLoader.getController();
        listController.setProgramController(mainController);
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Interpreter");
        secondaryStage.setScene(new Scene(programRoot, 640, 787));
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}