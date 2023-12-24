package pto;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import pto.FXMLProxy.LoadData;
import pto.Manager.AppInstance;

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
        

        stage.setOnCloseRequest(event -> {onClose(event);});
        AppInstance.get().setMainWindow(stage);
        AppInstance.get().getControllerManager().addController(loadData.fxmlLoader.getController());
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(FXMLProxy.loadFXML(fxml).parent);
    }

    public static void main(String[] args) {
        launch();
    }

    private void onClose(WindowEvent event)
    {
        AppInstance.get().finalize();
    }
}