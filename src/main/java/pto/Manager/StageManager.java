package pto.Manager;

import java.util.HashMap;

import javafx.stage.Stage;

public class StageManager
{
    public static final String Main = "Main";

    private static HashMap<String, Stage> stages = new HashMap<String, Stage>();

    public static void addStage(String tag, Stage stage)
    {
        stages.put(tag, stage);
    }
    public static Stage getStage(String tag)
    {
        return stages.get(tag);
    }
    public static void removeStage(String tag)
    {
        stages.remove(tag);
    }
    public static void removeAll()
    {
        for (Stage stage : stages.values())
        {
            stage.close();
        }
    }
}
