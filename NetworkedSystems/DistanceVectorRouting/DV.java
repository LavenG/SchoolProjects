import java.lang.Math;
import java.util.*;

public class DV implements RoutingAlgorithm {

    //when the outgoing interface for an address is local address
    static int LOCAL = -1;

    //when the outgoing inteface for an address is unkown
    static int UNKNOWN = -2;

    //Metric for when a link is down
    static int INFINITY = 60;

    //stores the current router object to be passed to the routing algorthm
    private Router router;

    //stores the current routing update interval
    private int u;

    //The following value and explanation are referenced from https://tools.ietf.org/html/rfc2453#section-3.8
    //As per the RFC2453 specifications there are two timers associated with the
    //entry expiration process, the timeout and garbage collection timers.
    //For the purpose of this coursework we only implement the garbage collection timer,
    //meaning that once we hit the timeout we do not set the metrics to INFINITY
    //before we eventually garbage collect.

    //Stores the gabage collection time, it's value is 120/30 = 4
    static int GARBAGE_COLLECTION = 4;

    //stores wether split horizon with poison reverse is allowed
    private boolean sh_pr;

    //stored wether routing table entry expiration is allowed
    private boolean expiration;

    //This array list represents the routing table of the current router and is used to
    //store DVRoutingTableEntry objects
    private ArrayList<DVRoutingTableEntry> routing_table;

    /**
    * Constructor for this object
    * Initalises the routing_table array list
    */
    public DV()
    {
      routing_table = new ArrayList<DVRoutingTableEntry>();
    }

    /**
     * Passes the router object to the Routing algorithm, so that the algorithm can retreive values from the router.
     * @param obj
     */
    public void setRouterObject(Router obj)
    {
      router = obj;
    }

    /**
     * Sets the routing update interval in time slots.
     * @param interval the update interval
     */
    public void setUpdateInterval(int u)
    {
      this.u = u;
    }

    /**
     *Enables or disables split horizon with poison reverse.
     *@param flag a boolean indicating whether split horizon
     *with poison reverse is on or off.
     */
    public void setAllowPReverse(boolean flag)
    {
      sh_pr = flag;
    }

    /**
     *Enables or disables routing table entry expiration.
     *@param flag a boolean indicating whether entry expiration
     *is on or off.
     */
    public void setAllowExpire(boolean flag)
    {
      expiration = flag;
    }

    /**
     * Initalise the routing algorthm. This must be called once the
     * <code>setRouterObject</code> has been called.
     */
    public void initalise()
    {
      //Creates a new routing table entry with d = id of current router, i =
      //local, m = 0 and t = 0 and map it to the router id in the routing table
      routing_table.add(new DVRoutingTableEntry(router.getId(), LOCAL, 0, 0));
    }

    /**
    * Given a destination finds an entry in the routing table
    * that has the same destination. If no mathing entries are
    * found we return null
    * @param d the destination address
    * @return DVRoutingTableEntry object that matches d or null
    */
    public DVRoutingTableEntry find_matching_entry(int d){
      for(DVRoutingTableEntry current_entry : routing_table){
        if(current_entry.getDestination() == d){
          return current_entry;
        }
      }
      return null;
    }

    /**
     * Given a destination address, returns the out going interface for that address, -1 is returned for a local address, -2 is an unknown address.
     * @param destination the destination address
     * @return the local interface
     */
    public int getNextHop(int destination)
    {
      //retrieve the routing table entry that stores the destination
      DVRoutingTableEntry current_entry = find_matching_entry(destination);
      //If such an entry exists and the link is not down return the local interface
      if(current_entry != null && current_entry.getMetric() != INFINITY){
        return current_entry.getInterface();
      //otherwise return unkown
      }else{
        return UNKNOWN;
      }
    }

    //Implements garbage collection according to RFC2453 specifications
    public void expire_stale_entries(){
      int i = 0;
      //Go through the routing table
      while(i < routing_table.size()){
        //Don't remove entries if the interface is local
        //If the metric is infinity and we hit the garbage collection time we remove the entry from the table
        if(routing_table.get(i).getInterface() != LOCAL && routing_table.get(i).getMetric() == INFINITY && router.getCurrentTime() >= routing_table.get(i).getTime() +GARBAGE_COLLECTION*u ){
          routing_table.remove(i);
          i--;
        }
        i++;
      }
    }

    /**
     * A periodic task to tidy up the routing table. This method is called before
     * processing any new packets each round.
     */
    public void tidyTable()
    {
      //for all the interfaces on the current router
      for(int i = 0; i < router.getNumInterfaces(); i++){
        //if the interface is down
        if(router.getInterfaceState(i) == false){
          //go through all the entries in the routing table and set the metric
          //of the entries with that interface to infinity
          for(DVRoutingTableEntry current_entry : routing_table){
            if(current_entry.getInterface() == i){
              current_entry.setMetric(INFINITY);
            }
          }
        }
      }
      //If we need to implement expiration of stale entries do garbage collection
      if(expiration){
        expire_stale_entries();
      }
    }

    /**
     * Generates a routing packet from the routing table.
     * @param iface interface id
     * @return Packet
     */
    public Packet generateRoutingPacket(int iface)
    {
        //First we check wether the interface is up
        if(router.getInterfaceState(iface)){
          //We construct a new payload for the packet
          Payload new_payload = new Payload();
          //We iterate through the routing table of the current router and add all the
          //entries the the payload's data
          for(DVRoutingTableEntry current_entry: routing_table){
            //We announce different routing tables to different routers as described in the course slides for SH/PR
            //If for a router X a router Y is the next hop to reach router Z we don't announce to Y a shorter
            //distance for Z. Instead we set the metric to infinity.
            if(sh_pr && getNextHop(current_entry.getDestination()) == iface){
              new_payload.addEntry(new DVRoutingTableEntry(current_entry.getDestination(), current_entry.getInterface(), INFINITY, router.getCurrentTime()));
            }else{
              new_payload.addEntry(new DVRoutingTableEntry(current_entry.getDestination(), current_entry.getInterface(), current_entry.getMetric(), router.getCurrentTime()));
            }
          }
          //We construct a packet to be returned
          Packet new_packet = new RoutingPacket(router.getId(), Packet.BROADCAST);
          //We add the payload to the packet and return it
          new_packet.setPayload(new_payload);
          return new_packet;
        }
        //If the interface is down we default to returning null
        return null;
    }

    /**
     * Given a routing packet from another host process it and add it to the routing table.
     * @param p the packet to process
     * @param iface the interface it came in on
     */
    public void processRoutingPacket(Packet p, int iface)
    {
      //We get the data from the packet's payload which is basically another router's routing table
      Vector<Object> data = p.getPayload().getData();
      //For each entry in the data
      for(Object o: data){
        //Convert it to a Routing table entry so it's easier to compare
        DVRoutingTableEntry entry_to_compare = (DVRoutingTableEntry)o;

        //The following code is an implementation of the DV algorithm pseudocode provided in lecture
        //equivalent to m += metric for interface i, we also ensure that the metric does not exceed INFINITY which causes bugs in the program
        int m = (entry_to_compare.getMetric() + router.getInterfaceWeight(iface) < INFINITY) ? entry_to_compare.getMetric() + router.getInterfaceWeight(iface) : INFINITY;

        entry_to_compare.setInterface(iface);
        entry_to_compare.setMetric(m);

        //rt = lookup(D) in routing table
        DVRoutingTableEntry rt = find_matching_entry(entry_to_compare.getDestination());
        //if(rt="not found") then
        if (rt == null){
          if(expiration && entry_to_compare.getMetric() == INFINITY) return;
          //r_new = new routing table entry
          entry_to_compare.setTime(router.getCurrentTime());
          routing_table.add(entry_to_compare);
        //else if (i==rt.iface) then
        }else if(iface == rt.getInterface()){
          if(expiration && rt.getMetric() == INFINITY && entry_to_compare.getMetric() == INFINITY) return;
          //rt.metric = m
          rt.setMetric(entry_to_compare.getMetric());
          rt.setTime(router.getCurrentTime());
        //else if (m < rt.metric) then
        }else if(m < rt.getMetric()){
          //rt.metric =m
          rt.setMetric(m);
          //r.iface = i
          rt.setInterface(iface);
          //used to implement expiration of stale entries
          rt.setTime(router.getCurrentTime());
        }
      }
    }


    /**
     * Prints the routing table to the screen.
     * The format is :
     * Router <id>
     * d <destination> i <interface> m <metric>
     * d <destination> i <interface> m <metric>
     * d <destination> i <interface> m <metric>
     */
    public void showRoutes()
    {
      //Before printing out the routing table we need to sort the tabe by destination
      //in case entries have been modyified and could possibly be out of order
      Collections.sort(routing_table, new Comparator<DVRoutingTableEntry>(){
        public int compare(DVRoutingTableEntry e1, DVRoutingTableEntry e2){
          return e1.getDestination() - e2.getDestination();
        }
      });

      //Go through all the entries in the routing table and print them out according
      //to the specified format
      System.out.println("Router " + router.getId());
      for(DVRoutingTableEntry current_entry : routing_table){
        System.out.println(current_entry.toString());
      }
    }
}

class DVRoutingTableEntry implements RoutingTableEntry
{
  //represents the destination field of the routing table entry
  private int d;
  //represents interface field of the routing table entry
  private int i;
  //represents the metric field of the routing table entry
  private int m;
  //represents the time field of the routing table entry
  private int t;

  //Constructor used to construct a single routing table entry object
  public DVRoutingTableEntry(int d, int i, int m, int t)
	{
    this.d = d;
    this.i = i;
    this.m = m;
    this.t = t;
	}

  //The following are simple getter and setter methods that either return
  //a specific field of the routing table entry or set it to a provided argument

  //returns destination field of table entry
  public int getDestination()
  {
    return this.d;
  }

  //sets destination field of table entry
  public void setDestination(int d)
  {
    this.d = d;
  }

  //returns interface field of table entry
  public int getInterface()
  {
     return this.i;
  }

  //sets interface field of table entry
  public void setInterface(int i)
  {
    this.i = i;
  }

  //gets metric field of table entry
  public int getMetric()
  {
    return this.m;
  }

  //sets metric field of table entry
  public void setMetric(int m)
  {
    this.m = m;
  }

  //gets time field (time added to table) of table entry
  public int getTime()
  {
    return this.t;
  }

  //sets time field (time added to table) of table entry
  public void setTime(int t)
  {
    this.t = t;
  }

  //returns printable version of entire table entry as String
  public String toString()
	{
    return "d " + this.d + " i " + this.i + " m " + this.m;
	}
}
