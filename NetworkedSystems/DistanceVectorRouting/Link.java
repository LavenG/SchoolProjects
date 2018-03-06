import java.util.Vector;
/**
 * A <code>Link</code> class that represents a link between two routers.
 * It contains four packet queues, and in bound and out bound queue for
 * each end. It also simulates the moving of packets from one end to
 * the other end.
 */
public class Link
{
    private int[] router = new int[2];
    private int[] iface = new int[2];
    private int[] weight = new int[2];
    private int[][] counter = new int[2][2];
    @SuppressWarnings("unchecked") private Vector<Packet>[] in = new Vector[2];
    @SuppressWarnings("unchecked") private Vector<Packet>[] out = new Vector[2];
    private boolean up;

    private int SENT = 0;
    private int RECV = 1;
    private int DROP = 2;

    /* Constructor that takes router id, interface id, and interface weight.
     * for both ends of the link.
     * @param r0 router 0's id
     * @param i0 router 0's interface
     * @param w0 weight associated with iterface 0.
     * @param r1 router 1's id
     * @param i1 router 1's interface
     * @param w1 weight associated with iterface 1.
     */
    public Link(int r0, int i0, int w0, int r1, int i1, int w1)
    {
        router[0] = r0;
        iface[0] = i0;
        weight[0] = w0;
        router[1] = r1;
        iface[1] = i1;
        weight[1] = w1;
        in[0] = new Vector<Packet>();
        out[0] = new Vector<Packet>();
        in[1] = new Vector<Packet>();
