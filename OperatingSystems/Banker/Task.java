import java.util.*;

public class Task {

    //Keeps track of the task number
    private int taskNumber;

    private int timeTaken;
    private int waitingTime;

    //Used to store all the activities related to this task
    private ArrayList<Activity> activities = new ArrayList<Activity>();

    private int [] resources;

    //initializes the task with the provided number
    public Task(int tasknumber,int size) {
        this.taskNumber = tasknumber;
        this.timeTaken = 0;
        this.waitingTime = 0;
        this.resources = new int[size];

    }

    public int [] getResources() {return this.resources;}

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
}
