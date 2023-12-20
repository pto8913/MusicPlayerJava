package pto.Controller;

import java.lang.reflect.Constructor;

import javafx.util.Callback;
import pto.AppContext;

public class ControllerFactory implements Callback<Class<?>, Object>
{
    private final AppContext context = new AppContext();

    public ControllerFactory()
    {
        // this.context;
        /* initialize from json or anything */
    }

    @Override
    public Object call(Class<?> type) 
    {
        try
        {
            for (Constructor<?> c : type.getConstructors())
            {
                if (c.getParameterCount() == 1 && c.getParameterTypes()[0].equals(AppContext.class))
                {
                    return c.newInstance(context);
                }
            }
            return type.getConstructor().newInstance();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Unexpected controller type: " + type);
        }
    } 
}
