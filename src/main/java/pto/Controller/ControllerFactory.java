package pto.Controller;

import java.lang.reflect.Constructor;

import javafx.util.Callback;
import pto.AppContext;
import pto.Controller.ListView.MusicListTypes;

public class ControllerFactory implements Callback<Class<?>, Object>
{
    private final AppContext context = new AppContext();
    private MusicListTypes cellTypes;

    public ControllerFactory()
    {
        // this.context;
        /* initialize from json or anything */
    }
    public ControllerFactory(MusicListTypes cellTypes)
    {
        this.cellTypes = cellTypes;
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
                else if (c.getParameterCount() == 1 && c.getParameterTypes()[0].equals(MusicListTypes.class))
                {
                    return c.newInstance(cellTypes);
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
