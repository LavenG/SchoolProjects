import java.util.*;
public class Process {
	
	private int arrivalTime;
	private int CPUBurstTime;
	private int totalCPUTime;
	private int IOBurstTime;
	
	private int unmodifiedTotalCPUTime;
	
	private int processNumber;
	
	private String processState;
	
	//This is used to compute I/O time
	private int timeBlocked;
	//This is used to compute Waiting time
	private int timeReady;
	
	private int totalTime;
	
	private int remainingBlockedTime;
	private int remainingRunningTime;
	
	
	
	public Process(int processNumber, int arrivalTime, int CPUBurstTime, int totalCPUTime, int IOBurstTime){
		this.processNumber = processNumber;
		this.arrivalTime = arrivalTime;
		this.CPUBurstTime = CPUBurstTime;
		this.totalCPUTime = totalCPUTime;
		this.IOBurstTime = IOBurstTime;
		this.unmodifiedTotalCPUTime = totalCPUTime;
		
		processState = "INITIALIZED";
		
		timeBlocked = 0;
		timeReady = 0;
		totalTime = 0;
		
		remainingBlockedTime = -1;
		remainingRunningTime = -1;
		
	}
	
	public int getProcessNumber(){
		return this.processNumber;
	}
	
	public int getArrivalTime(){
		return this.arrivalTime;
	}
	
	public int getCPUBurstTime(){
		return this.CPUBurstTime;
	}
	
	public int getTotalCPUTime(){
		return this.totalCPUTime;
	}
	
	public int getIOBurstTime(){
		return this.IOBurstTime;
	}
	
	public String getProcessState(){
	return this.processState;
	}
	
	public int getTimeBlocked(){
		return this.timeBlocked;
	}
	
	public int getTimeReady(){
		return this.timeReady;
	}
	
	public int getTotalTime(){
		return this.totalTime;
	}
	
	public int getRemainingBlockedTime(){
		return this.remainingBlockedTime;
	}
	
	public int getRemainigRunningTime(){
		return this.remainingRunningTime;
	}
	
	public int getUnmodifiedTotalCPUTime(){
		return this.unmodifiedTotalCPUTime;
	}
	
	public void setRemainingRunningTime(int i){
		this.remainingRunningTime = i;
	}
	
	public void setProcessState(String state){
		this.processState = state;
	}
	
	public void setRemainingBlockedTime(int i){
		this.remainingBlockedTime = i;
	}
	
	public void passTime(){
		if(this.processState.equals("READY")){
			this.timeReady++;
		}
		else if (this.processState.equals("BLOCKED")){
			this.timeBlocked++;
		}
		this.totalTime++;
	}
	
	
	public boolean shouldRun(){
		if(remainingRunningTime!= -1 && remainingRunningTime<= 0){
			System.exit(1);
		}
		
		remainingRunningTime--;
		totalCPUTime--;
		
		if(totalCPUTime == 0 || remainingRunningTime == 0 ){
			remainingRunningTime = -1;
			
			return true;
		}else{
			
			return false;
		}
		
	}
	
	
	public boolean doneBlocked(){
		//System.out.println("Inside the doneBlocked of process" + this.toString());
		
		if(remainingBlockedTime != -1 && remainingBlockedTime <= 0){
			System.out.println("This process should not be blocked");
			System.exit(1);
		}
		
		remainingBlockedTime--;
		
		if(remainingBlockedTime == 0){
			remainingBlockedTime = -1;
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		return "(" + this.getArrivalTime() +" " + this.getCPUBurstTime() +" "+ this.getTotalCPUTime() +" " + this.getIOBurstTime() + ")";
	}
	
	public String alternateToString(){
		return "(" + this.getArrivalTime() +"," + this.getCPUBurstTime() +","+ this.getUnmodifiedTotalCPUTime() +"," + this.getIOBurstTime() + ")";
	}
	
	public void printFull(){
		System.out.println("\t(A,B,C,M) = " + alternateToString()) ;
		System.out.println("\tFinishing time: " + totalTime);
		System.out.println("\tTurnaround time: " + (totalTime-arrivalTime));
		System.out.println("\tI/O time: " + timeBlocked);
		System.out.println("\tWaiting time:" + timeReady);
	}
	
}
