import java.lang.Integer;

/**
 * The <code>Router</code> class represents a router. Each Router
 * references its own routing algorithm class, whose name was provided
 * in the constructor. A Router looks up the
 * class from its name and instantiates it using java reflection.
 */
public class Router
{
    private int id;
    private Integer current_time;
    private int num_interfaces;
    private String classname;
    private Link[] link;
    private RoutingAlgorithm ralg;
    private int update_interval = 1;
    private int[] counter = new int[4];

    private int SENT = 0;
    private int RECV = 1;
    private int DROP = 2;
    private int FORW = 3;

    private boolean preverse = false;
    private boolean expire = false;

    /**
     * <code>Router</code> constructor takes the router id, the number of
     * interfaces, the class name of the routing algorithm to load and
     * the update interval, in addition to two flags indicating the level
     * of optimisation.
     * If the routing algorithm class is not found, prints an exception.
     * @param i router id
     * @param n number of interfaces
     * @param c class name of the routing algortihm to load.
     * @param u the update interval in seconds
     * @param pr if split horizon with poison reverse is enabled or not.
     * @param e if entry expiry is enabled or not.
     */
    public Router(int i, int n, String c, int u, boolean pr, boolean e) {
        id = i;
        num_interfaces = n;
        current_time = new Integer(0);
        classname = c;
    update_interval = u;
    preverse = pr;
    expire = e;

        link = new Link[n];

        try {
            ralg = (RoutingAlgorithm)(Class.forName(classname)).newInstance();
            ralg.setRouterObject(this);
        ralg.setUpdateInterval(update_interval);
        ralg.setAllowPReverse(preverse);
        ralg.setAllowExpire(expire);
            ralg.initalise();
