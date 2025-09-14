package JavaFx.view;

import JavaFx.controller.AnaEkranController;
import JavaFx.controller.NotEditorController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NotDefteri extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Not Defteri");

        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("ana-ekran");

        AnaEkranView anaEkranView = new AnaEkranView();
        NotEditorView notEditorView = new NotEditorView();
        NotEditorController notEditorController = new NotEditorController();

        new AnaEkranController(anaEkranView, notEditorView, notEditorController, borderPane, stage);

        borderPane.setLeft(anaEkranView);
        stackPane.getChildren().add(borderPane);


        Scene scene = new Scene(stackPane, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // css kodlarının tanımlanması için
        stage.setScene(scene);
        stage.show();
    }
}
