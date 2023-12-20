package pto;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import pto.FXMLProxy.LoadData;
import pto.Manager.ControllerManager;
import pto.Manager.StageManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        LoadData loadData = FXMLProxy.loadFXML("Main");
        scene = new Scene(loadData.parent);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(onClosed());
        StageManager.addStage(StageManager.Main, stage);
        ControllerManager.addController(loadData.fxmlLoader.getController());
    }
    private EventHandler<WindowEvent> onClosed()
    {
        return event -> {
            StageManager.removeAll();
        };
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(FXMLProxy.loadFXML(fxml).parent);
    }

    public static void main(String[] args) {
        launch();
    }
}