package com.sanvalero.trailrun;

import com.sanvalero.trailrun.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Creado por @ author: Pedro Orós
 * el 20/11/2020
 */
public class App extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("trailrun.fxml"));
        loader.setController(new AppController());
        VBox vbox = loader.load();

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(R.getUI("registro.fxml"));
        loader2.setController(new InicioRegistroController());
        VBox vbox2 = loader2.load();

        Scene scene2 = new Scene(vbox2);
        Stage stage = new Stage();
        stage.setScene(scene2);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
