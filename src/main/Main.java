package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.InitFxml;


public class Main extends Application {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    @Override
    public void start(Stage primaryStage){

        try {
            InitFxml initFxml = new InitFxml();
            initFxml.initializeLoadersAndControllers(primaryStage);

        }catch (Exception e) {
            System.err.print(e);
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
