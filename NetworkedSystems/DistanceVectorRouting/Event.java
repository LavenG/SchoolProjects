/**
 * The <code>Event</code> class which represents an event to occur in the
 * simulator.
 */
public class Event
{
    private int time;
    private String operation;
    private String[] args;
    private boolean done;
    /**
     * <code>Event</code> constructor, which takes the name of the event,
     * the time the event is to occur, and a list of arguments to the event.
     * @param o the name of the event
     * @param t the time the event is to occur
     * @param a the arguements to the event.
     */
    public Event(String o, int t, String[] a)
    {
        operation = o;
        time = t;
        args = a;
        done = false;
    }

    /**
     * Return the time this event is schduled to occur at
     * @return current time
     */
    public int getTime() {
        return time;
    }

    /**
     * Get the operation set by this event.
     * @return name of the operation
     */
    public String getOperation() {
        return operation;
    }
