package pto.Events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class PtoEvent extends Event
{
    private static final long serialVersionUID = 382974217L;
    public static final EventType<PtoEvent> PTO = new EventType<>(Event.ANY, "PTO");

    public static final EventType<PtoEvent> ANY = PTO;

    /**
     * Creates a new {@code PtoEvent} with an event type of {@code PTO}.
     * The source and target of the event is set to {@code NULL_SOURCE_TARGET}.
     */
    public PtoEvent() {
        super(PTO);
    }

    /**
     * Construct a new {@code PtoEvent} with the specified event source and target.
     * If the source or target is set to {@code null}, it is replaced by the
     * {@code NULL_SOURCE_TARGET} value. All ActionEvents have their type set to
     * {@code PTO}.
     *
     * @param source    the event source which sent the event
     * @param target    the event target to associate with the event
     */
    public PtoEvent(Object source, EventTarget target) {
        super(source, target, PTO);
    }

    @Override
    public PtoEvent copyFor(Object newSource, EventTarget newTarget) {
        return (PtoEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends PtoEvent> getEventType() {
        return (EventType<? extends PtoEvent>) super.getEventType();
    }
}
