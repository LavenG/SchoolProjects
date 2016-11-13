import java.util.*;

public class Task {
	
	private int taskNumber;
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	
	public Task(int tasknumber){
		this.taskNumber = tasknumber;
	}
	
	public int getTaskNumber(){
		return this.taskNumber;
	}
	
	public ArrayList<Activity> getActivities(){
		return this.activities;
	}
}
