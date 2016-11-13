
public class Activity {
	
	private final String INITIATE = "initiate";
	private final String REQUEST = "request";
	private final String RELEASE = "release";
	private final String COMPUTE = "compute";
	private final String TERMINATE = "terminate";
	
	private String activityType;
	
	private int taskNumber;
	
	//Represents the type of resource concerned, also used for number of cycles for the compute activity
	private int resourceType;
	
	private int initialClaim;
	private int numberRequested;
	private int numberReleased;
	
	private int numberOfCycles;
	
	public Activity(String activity, int taskNumber, int resourceType, int number){
		switch (activity){
			case INITIATE: 
				this.activityType = INITIATE;
				this.taskNumber = taskNumber;
				this.resourceType = resourceType;
				this.initialClaim = number;
				break;
			case REQUEST:
				this.activityType = REQUEST;
				this.taskNumber = taskNumber;
				this.resourceType = resourceType;
				this.numberRequested = number;
				break;
			case RELEASE:
				this.activityType = RELEASE;
				this.taskNumber = taskNumber;
				this.resourceType = resourceType;
				this.numberReleased = number;
				break;
			case COMPUTE:
				this.activityType = COMPUTE;
				this.taskNumber = taskNumber;
				this.numberOfCycles = resourceType;
				break;
			case TERMINATE:
				this.activityType = TERMINATE;
				this.taskNumber = taskNumber;
				break;
			default:
				break;
		}
	}
	
	public String getActivity(){
		return this.activityType;
	}
	
	public int getTaskNumber(){
		return this.taskNumber;
	}
	
	public int getResourceType(){
		return this.resourceType;
	}
	
	public int getInitialClaim(){
		return this.initialClaim;
	}
	
	public int getNumberRequested(){
		return this.numberRequested;
	}
	
	public int getNumberReleased(){
		return this.numberReleased;
	}
	
	public int getNumberOfCycles(){
		return this.numberOfCycles;
	}
	
	@Override
	public String toString(){
		switch (this.activityType){
		case INITIATE: 
			return this.activityType +" "+ this.taskNumber +" "+ this.resourceType +" "+ this.initialClaim;
		case REQUEST:
			return this.activityType +" "+ this.taskNumber +" "+ this.resourceType +" "+ this.numberRequested;
		case RELEASE:
			return this.activityType +" "+ this.taskNumber +" "+ this.resourceType +" "+ this.numberReleased;
		case COMPUTE:
			return this.activityType +" "+ this.taskNumber +" "+ this.numberOfCycles;
		case TERMINATE:
			return this.activityType +" "+ this.taskNumber ;
		default:
			return "";
	}
	}
}
