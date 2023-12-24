package pto.Events;

import java.util.EventListener;

import javafx.event.EventHandler;

public class Delegate <T extends PtoEvent> implements EventListener
{
    private EventHandler<T> event;

    public Delegate()
    {
    }
    public Delegate(EventHandler<T> inEvent)
    {
        event = inEvent;
    }

    public void Bind(EventHandler<T> inEvent)
    {
        event = inEvent;
    }

    public void Broadcast(T inEvent)
    {
        event.handle(inEvent);
    }

    public boolean isValid()
    {
        return event != null;
    }
}
