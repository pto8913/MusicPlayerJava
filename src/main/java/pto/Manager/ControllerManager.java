package pto.Manager;

import java.util.HashMap;

import pto.Controller.IFloatingController;

public class ControllerManager
{
    private static HashMap<String, Object> controllers = new HashMap<String, Object>();

    public static void addController(Object controller)
    {
        controllers.put(controller.getClass().getName(), controller);
    }
    public static <T> T getController(Class type)
    {
        return (T)controllers.get(type.getName());
    }
    public static void removeController(Class type)
    {
        controllers.remove(type.getName());
    }
    public static void removeController(Object controller)
    {
        controllers.remove(controller.getClass().getName());
    }

    public static void addController(String inTag, Object controller)
    {
        controllers.put(controller.getClass().getName(), controller);
    }
    public static <T> T getController(String inTag)
    {
        return (T)controllers.get(inTag);
    }
    public static void removeController(String inTag)
    {
        controllers.remove(inTag);
    }

    public static void closeAllFloatingController()
    {
        for (Object obj : controllers.values())
        {
            if (IFloatingController.class.isInstance(obj))
            {
                IFloatingController floatingController = (IFloatingController)obj;
                floatingController.playCloseAnimation();
            }
        }
    }
}
