package pto;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import pto.FXMLProxy.LoadData;
import pto.Manager.AppInstance;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException
    {
        Splash splash = new Splash();
        splash.show();
        stage.setScene(splash.getSplashScene());
        splash.getSequentialTransition().setOnFinished(
            event -> {
                Timeline timeline = new Timeline();
                KeyFrame key = new KeyFrame(Duration.millis(250), new KeyValue(splash.getSplashScene().getRoot().opacityProperty(), 0));
                timeline.getKeyFrames().add(key);
                timeline.setOnFinished(
                    (eventOnFinish) -> {
                        Stage mainWindow = AppInstance.get().getMainWindow();
                        LoadData loadData = FXMLProxy.loadFXML("Main");
                        scene = new Scene(loadData.parent);
                        mainWindow.setScene(scene);
                        AppInstance.get().getControllerManager().addController(loadData.fxmlLoader.getController());
                    }
                );
                timeline.play();
            }
        );
//
//        LoadData loadData = FXMLProxy.loadFXML("Main");
//        scene = new Scene(loadData.parent);
        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(this::onClose);
        AppInstance.get().setMainWindow(stage);
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