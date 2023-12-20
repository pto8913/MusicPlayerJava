module pto {
    requires javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.media;

    // requires json.simple;
    requires transitive com.google.gson;
    requires java.sql;
    
    opens pto to javafx.fxml;
    exports pto;
    
    opens pto.Controller to javafx.fxml;
    exports pto.Controller;
    
    opens pto.Controller.ListView to javafx.fxml, com.google.gson, gson;
    exports pto.Controller.ListView;
}
