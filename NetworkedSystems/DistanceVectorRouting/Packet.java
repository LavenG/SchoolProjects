import java.util.Vector;
/**
 * The <code>Packet</code> class models a network packet.
 */
public class Packet
{
    /**
     * The unknown packet type, used before a packet is classified.
     */
    public static int UNKNOWN = 0;
    /**
     * The data packet type.
     */
    public static int DATA = 1;
    /**
     * A Routing Packet
     */
    public static int ROUTING = 2;

    /**
     * The Broadcast addess
     */
    public static int BROADCAST = 255;
    /**
     * The unknown address.
     */
    public static int UNKNOWNADDR = -1;

    protected int src;
    protected int dst;
    protected Payload data;
    protected int ttl = 255;
    protected int type;
    protected int seq;

    /**
     * <code>Packet</code> construtor for the super class. This defaults to
     * setting the packet type to be the UNKNOWN type.
     * @param s source address
     * @param d destination address
     */
    public Packet(int s, int d)
    {
        src = s;
        dst = d;
        type = UNKNOWN;
        data = new Payload();
        seq = 0;
    }

    /**
     * Gets the source address
     * @return int source address
     */
    public int getSource()
    {
        return src;
    }
