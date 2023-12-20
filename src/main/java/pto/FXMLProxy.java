package pto;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import pto.Controller.ControllerFactory;

public class FXMLProxy {
    public static class LoadData {
        public FXMLLoader fxmlLoader;
        public Parent parent;     
    }

    public static LoadData loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(new ControllerFactory());
        LoadData out = new LoadData();
        out.fxmlLoader = fxmlLoader;
        try
        {
            out.parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        return out;
    }

    public static LoadData loadFXML(String fxml, Object Controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(Controller);
        LoadData out = new LoadData();
        out.fxmlLoader = fxmlLoader;
        try
        {
            out.parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        return out;
    }
}
