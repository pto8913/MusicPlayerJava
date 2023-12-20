package pto.Events;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class MulticastDelegate <T extends PtoEvent> implements EventListener
{
    private Map<String, Delegate<T>> events = new HashMap<>();

    public void Bind(String tag, Delegate<T> inEvent)
    {
        events.put(tag, inEvent);
    }
    public void Unbind(String tag)
    {
        events.remove(tag);
    }

    public void Broadcast(T inEvent)
    {
        for (Delegate<T> event : events.values())
        {
            if (event.isValid())
            {
                event.Broadcast(inEvent);
            }
        }
    }
}
