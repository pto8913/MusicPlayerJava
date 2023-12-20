package pto.Utils;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ButtonUtils {
    public static void onMouseHovered(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(255,255,255,0.25)");
        }
    }
    public static void onMouseLeave(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(255,255,255,0)");
        }
    }
    public static void onMousePressed(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(50,50,50,0.25)");
        }
    }
    public static void onMouseReleased(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(255,255,255,0.25)");
        }
    }
}
