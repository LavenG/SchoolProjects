
public class Activity {

	//Used to define the type of activity
	private String activityType;
	//Used to store the task number that the activity is defined for
	private int taskNumber;
	//Represents the type of resource concerned, also used for number-of-cycles since it's passed as the third arg
	private int resourceType;
	//Used to store the initial-claim, number-requested, number-released
	private int amount;

	//Constructs an activity object from the input provided
	public Activity(String activity, int taskNumber, int resourceType, int number){

		//the task number is assigned the same way for each
		this.taskNumber = taskNumber;

		//Determine the type of activity and initialize the object accordingly
		switch (activity){
			case "initiate":
				this.activityType = "initiate";
				this.resourceType = resourceType;
				this.amount = number;
				break;
			case "request":
				this.activityType = "request";
				this.resourceType = resourceType;
				this.amount = number;
				break;
			case "release":
				this.activityType = "release";
				this.resourceType = resourceType;
				this.amount = number;
				break;
			case "compute":
				this.activityType = "compute";
				this.amount = resourceType;
				break;
			case "terminate":
				this.activityType = "terminate";
				break;
			default:
				break;
		}
	}
	//getter and setter methods
	public String getActivity(){
		return this.activityType;
	}
	public int getTaskNumber(){
		return this.taskNumber;
	}
	public int getResourceType(){
		return this.resourceType;
	}
	public int getAmount(){
		return this.amount;
	}

	//toString method used for debugging
	@Override
	public String toString(){
		switch (this.activityType){
		case "initiate":
			return this.activityType +" "+ this.taskNumber +" "+ this.resourceType +" "+ this.amount;
		case "request":
			return this.activityType +" "+ this.taskNumber +" "+ this.resourceType +" "+ this.amount;
		case "release":
			return this.activityType +" "+ this.taskNumber +" "+ this.resourceType +" "+ this.amount;
		case "compute":
			return this.activityType +" "+ this.taskNumber +" "+ this.amount;
		case "terminate":
			return this.activityType +" "+ this.taskNumber ;
		default:
			return "";
	}
	}
}
