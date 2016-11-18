import java.util.*;

public class Task implements Cloneable{

    //Keeps track of the task number
    private int taskNumber;

    private int timeTaken;
    private int waitingTime;

    private int timeComputing;

    private boolean isComputing;
    //Used to store all the activities related to this task
    private boolean isAborted;

    private ArrayList<Activity> activities = new ArrayList<Activity>();

    private int [] resources;
    private int [] claims;

    //initializes the task with the provided number
    public Task(int tasknumber,int size) {
        this.taskNumber = tasknumber;
        this.timeTaken = 0;
        this.waitingTime = 0;
        this.resources = new int[size];
        this.claims = new int[size];
        this.isComputing = false;
        this.isAborted = false;
        this.timeComputing = 0;
    }

    public Task(Task t){
        this.taskNumber = t.getTaskNumber();
        this.timeTaken = t.getTimeTaken();
        this.waitingTime = t.getWaitingTime();
        this.resources = arrayClone(t.getResources());
        this.claims = arrayClone(t.getClaims());
        this.isComputing = t.isComputing();
        this.isAborted = t.isAborted();
        this.timeComputing = t.getTimeComputing();
        this.activities = t.activities;
    }

    public int [] getResources() {return this.resources;}

    public int [] getClaims() {return  this.claims;}
    //returns the task number
    public int getTaskNumber() {
        return this.taskNumber;
    }

    //returns the arrayList of activities
    public ArrayList<Activity> getActivities() {
        return this.activities;
    }

    public int getTimeTaken() {return this.timeTaken; }
    public int getWaitingTime() {return this.waitingTime;}

    public void setTimeTaken(int time) {timeTaken = time;}
    public void setWaitingTime(int time) {waitingTime = time; }

    public void addTimeTaken(int time) {timeTaken += time;}
    public void addWaitingTime(int time) {waitingTime += time;}

    public void setIsComputing(boolean computing){
        this.isComputing = computing;
    }

    public boolean isComputing(){
        return this.isComputing;
    }

    public void setTimeComputing(int time){
        this.timeComputing = time;
    }

    public void addTimeComputing(int i){
        this.timeComputing += i;
    }

    public int getTimeComputing(){
        return this.timeComputing;
    }

    public boolean isAborted(){
        return this.isAborted;
    }

    public void abort(){
        this.isAborted = true;
    }

    public static int[] arrayClone(int [] toClone){
        int []cloned = new int[toClone.length];
        for(int i = 0; i < toClone.length; i++){
            cloned[i] = toClone[i];
        }
        return cloned;
    }
}
