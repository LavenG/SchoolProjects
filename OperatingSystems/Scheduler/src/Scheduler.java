import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Scheduler {

	private static boolean verbose = false;

	private static String fileName;
	private static File inputFile;
	private static Scanner inputScanner;
	private static Scanner randoms;

	private static ArrayList<Process> processes = new ArrayList<Process>();
	private static ArrayList<Process> initialProcesses = new ArrayList<Process>();

	private static ArrayList<Process> readyProcesses = new ArrayList<Process>();
	private static ArrayList<Process> blockedProcesses = new ArrayList<Process>();
	private static ArrayList<Process> terminatedProcesses = new ArrayList<Process>();

	private static int CPUCycle = 0;
	private static int blocked = 0;
	private static Process currentRunningProcess = null;
	
	private static PrintWriter output = null;
	private static File outputFile = null;
	
	public static void main(String[] args) {

		// no verbose tag
		if (args.length == 1) {
			fileName = args[0];
		}
		// verbose tag correctly used
		else if (args.length == 2 && args[0].equals("--verbose")) {
			fileName = args[1];
			verbose = true;

		}else if(args.length == 3){
			verbose = true;
			fileName = args[1];
			outputFile = new File(args[2]);
			try {
				output = new PrintWriter(outputFile);
			}
			catch (FileNotFoundException e) {
				System.err.printf("Error: cannot open file %s for writing.\n.",
						outputFile.getAbsolutePath());
			}
					
		}
		// Incorrect usage of arguments
		else {
			System.out.println("Incorrect usage of arguments, please use one of the following formats: \n");
			System.out.println("Scheduler <input-filename>");
			System.out.println("Scheduler --verbose <input-filename>");
		}

		initializeScanners();
		initializeProcesses();

		if(verbose){
			verbosePrint("FCFS");
		}
		while (currentRunningProcess != null || readyProcesses.size() > 0 || processes.size() > 0
				|| blockedProcesses.size() > 0) {
			FCFS();
		}
		output.println(normalPrint());

		flush();

		initializeScanners();
		initializeProcesses();
		System.out.println("uniprogrammed \n");
		if(verbose){
			verbosePrint("uniprogrammed");
		}
		while (currentRunningProcess != null || readyProcesses.size() > 0 || processes.size() > 0
				|| blockedProcesses.size() > 0) {
			uniprogrammed();
		}
		normalPrint();
		
//		flush();
//		initializeScanners();
//		initializeProcesses();
//		System.out.println("\nShortestJobFirst");
//		while (currentRunningProcess != null || readyProcesses.size() > 0 || processes.size() > 0
//				|| blockedProcesses.size() > 0) {
//			shortestJobFirst();
//		}
//		normalPrint();
//		
//		
//		if(verbose){
//		verbosePrint("FCFS");
//	}
//
//		flush();
//		initializeScanners();
//		initializeProcesses();
//		System.out.println("\nRound Robin\n");
//		while (currentRunningProcess != null || readyProcesses.size() > 0 || processes.size() > 0
//				|| blockedProcesses.size() > 0) {
//			roundRobin();
//		}
//		normalPrint();
//		
	}

	public static void initializeScanners() {

		inputFile = new File(fileName);

		if (!inputFile.canRead()) {
			System.err.printf("Cannot read from file %s\n.", inputFile.getAbsolutePath());
			System.exit(0);
		}

		try {
			inputScanner = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.err.printf("File %s not found\n", inputFile.getAbsolutePath());
			System.exit(0);
		}

		File randomsFile = new File("random-numbers.txt");
		try {
			randoms = new Scanner(randomsFile);
		} catch (FileNotFoundException e) {
			System.err.printf("File %s not found\n", randomsFile.getAbsolutePath());
			System.exit(0);
		}

	}

	// used to initialize all the processes from the input file into object form
	// and store them into an arraylist of all processes
	public static void initializeProcesses() {
		int numberOfProcesses = inputScanner.nextInt();

		for (int i = 0; i < numberOfProcesses; i++) {

			processes.add(new Process(i, inputScanner.nextInt(), inputScanner.nextInt(), inputScanner.nextInt(),
					inputScanner.nextInt()));
		}

		System.out.print("The original input was: " + numberOfProcesses + " ");
		for (int i = 0; i < processes.size(); i++) {
			System.out.print(processes.get(i).toString() + " ");
		}

		// sort processes
		sortProcesses(processes, "arrival");
		// print sorted processes
		for (int i = 0; i < processes.size(); i++) {
			initialProcesses.add(processes.get(i));
		}

		System.out.print("\nThe (sorted) input is:  " + numberOfProcesses + " ");
		for (int i = 0; i < initialProcesses.size(); i++) {
			System.out.print(initialProcesses.get(i).toString() + " ");
		}
		System.out.println();
	}

	public static int randomOs(int val) {
		return 1 + (randoms.nextInt() % val);
	}

	public static void FCFS() {

		// Keep track of the processes in each state

		if (readyProcesses.size() > 0 && currentRunningProcess == null) {
			// System.out.println("check0");
			currentRunningProcess = readyProcesses.get(0);
			// if this process hasn't ran before we initialize its running time
			if (currentRunningProcess.getRemainigRunningTime() == -1) {
				// System.out.println("\n"+
				// randomOs(currentRunningProcess.getCPUBurstTime()));
				currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
			}
			currentRunningProcess.setProcessState("RUNNING");
			readyProcesses.remove(0);
		}

		// go over the processes and check if any are ready
		for (int i = 0; i < processes.size(); i++) {
			// this process is ready
			if (processes.get(i).getArrivalTime() <= CPUCycle) {
				// if there are no running processes, we run the one that is
				// ready
				// System.out.println("\ncheck1----");
				if (currentRunningProcess == null) {
					// System.out.println("\ncheck2 should be here ----");
					currentRunningProcess = processes.get(i);
					currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
					// System.out.println("\n random os yields"+
					// randomOs(currentRunningProcess.getCPUBurstTime()));
					processes.get(i).setProcessState("RUNNING");
				}
				// there is already a running process, this one waits as ready
				else {
					readyProcesses.add(processes.get(i));
					processes.get(i).setProcessState("READY");
				}

				processes.remove(i);
				i--;

			}
		}

		// We increment the time passed after going over the ready processes
		passTime("FCFS");
		// System.out.println("we passed time correctly");

		if(verbose){
			verbosePrint("FCFS");
		}
		ArrayList<Process> doneBlockedProcesses = new ArrayList<Process>();

		// We then switch the blocked processes that are ready to ready
		// On the first pass there will be no blocked processes yet

		// Determine which processes should be unblocked
		for (int i = 0; i < blockedProcesses.size(); i++) {
			// System.out.println("entered blocked for the " + i);
			if (blockedProcesses.get(i).doneBlocked()) {
				// System.out.println("entered the if of blocked for the " + i);
				doneBlockedProcesses.add(blockedProcesses.get(i));
				blockedProcesses.get(i).setProcessState("READY");
				blockedProcesses.remove(i);
				i--;
			}
		}

		// Sort the unblocked processes and add them to the ready list
		sortProcesses(doneBlockedProcesses, "arrival-number");

		for (int i = 0; i < doneBlockedProcesses.size(); i++) {
			readyProcesses.add(doneBlockedProcesses.get(i));
			doneBlockedProcesses.remove(i);
			i--;
		}

		// Finally we go over the running process
		if (currentRunningProcess != null) {
			if (currentRunningProcess.shouldRun()) {
				if (currentRunningProcess.getTotalCPUTime() == 0) {
					terminatedProcesses.add(currentRunningProcess);
					currentRunningProcess.setProcessState("TERMINATED");
					currentRunningProcess = null;
				} else {
					blockedProcesses.add(currentRunningProcess);
					if (currentRunningProcess.getRemainingBlockedTime() == -1) {
						// System.out.println("about to set the remaining
						// blocked time of this process to " +
						// randomOs(currentRunningProcess.getIOBurstTime()));
						currentRunningProcess.setRemainingBlockedTime(randomOs(currentRunningProcess.getIOBurstTime()));

					}
					currentRunningProcess.setProcessState("BLOCKED");
					currentRunningProcess = null;
				}
			}
		}

	}
	

	public static void uniprogrammed() {

		boolean run = true;
		boolean wait = false;

		if (readyProcesses.size() > 0 && currentRunningProcess == null) {
			// System.out.println("check0");
			currentRunningProcess = readyProcesses.get(0);
			// if this process hasn't ran before we initialize its running time
			if (currentRunningProcess.getRemainigRunningTime() == -1) {
				// System.out.println("\n"+
				// randomOs(currentRunningProcess.getCPUBurstTime()));
				int runTimeSet = randomOs(currentRunningProcess.getCPUBurstTime());
				System.out.println("SETTING RUN TIME TO" + runTimeSet);
				currentRunningProcess.setRemainingRunningTime(runTimeSet);
			}
			currentRunningProcess.setProcessState("RUNNING");
			readyProcesses.remove(0);
		}

		// go over the processes and check if any are ready
		for (int i = 0; i < processes.size(); i++) {
			// this process is ready
			if (processes.get(i).getArrivalTime() <= CPUCycle) {
				// if there are no running processes, we run the one that is
				// ready
				// System.out.println("\ncheck1----");
				if (currentRunningProcess == null) {
					// System.out.println("\ncheck2 should be here ----");
					currentRunningProcess = processes.get(i);
					int runTimeSet = randomOs(currentRunningProcess.getCPUBurstTime());
					System.out.println("SETTING RUN TIME TO" + runTimeSet);
					currentRunningProcess.setRemainingRunningTime(runTimeSet);
					// System.out.println("\n random os yields"+
					// randomOs(currentRunningProcess.getCPUBurstTime()));
					processes.get(i).setProcessState("RUNNING");
				}
				// there is already a running process, this one waits as ready
				else {
					readyProcesses.add(processes.get(i));
					processes.get(i).setProcessState("READY");
				}

				processes.remove(i);
				i--;

			}
		}

		passTime("uniprogrammed");

		if(verbose){
			verbosePrint("uniprogrammed");
		}
		// keep track of the blocked processes

		if (blockedProcesses.size() > 0) {
			if (blockedProcesses.get(0).doneBlocked()) {
				blockedProcesses.get(0).setProcessState("RUNNING");
				if (blockedProcesses.get(0).getRemainigRunningTime() == -1) {
					blockedProcesses.get(0).setRemainingRunningTime(blockedProcesses.get(0).getCPUBurstTime());
				}
				blockedProcesses.remove(0);
				run = true;
				wait = true;
			}
		}

		// keep track of waiting processes
		if (currentRunningProcess != null) {
			if (run && !wait) {
				if (currentRunningProcess.shouldRun()) {
					if (currentRunningProcess.getTotalCPUTime() <= 0) {
						terminatedProcesses.add(currentRunningProcess);
						currentRunningProcess.setProcessState("TERMINATED");
						currentRunningProcess = null;
						run = true;
					} else {
						if (currentRunningProcess.getRemainingBlockedTime() == -1) {
							int blockedSet = randomOs(currentRunningProcess.getIOBurstTime());
							System.out.println("setting blocked to " + blockedSet);
							currentRunningProcess
									.setRemainingBlockedTime(blockedSet);
						}
						currentRunningProcess.setProcessState("BLOCKED");
						blockedProcesses.add(currentRunningProcess);
						run = false;
					}
				}
			}
		}
		wait=false;
	}
	
	public static void shortestJobFirst(){
		for(int i = 0; i< processes.size(); i++){
			if(processes.get(i).getArrivalTime() == CPUCycle){
				readyProcesses.add(processes.get(i));
				processes.get(i).setProcessState("READY");
				processes.remove(i);
				i--;
			}
		}
		sortProcesses(readyProcesses, "cpuTime");
		
		if(currentRunningProcess == null && readyProcesses.size()>0){
			currentRunningProcess = readyProcesses.get(0);
			if(currentRunningProcess.getRemainigRunningTime() == -1){
				currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
			}
			currentRunningProcess.setProcessState("RUNNING");
			readyProcesses.remove(0);
		}
		
		passTime("shortestJobFirst");
		
		if(verbose){
			verbosePrint("FCFS");
		}
		//Do Blocked Processes
		ArrayList<Process> doneBlockedProcesses = new ArrayList<Process>();
		for(int i=0; i<blockedProcesses.size(); i++){
			if(blockedProcesses.get(i).doneBlocked()){
				doneBlockedProcesses.add(blockedProcesses.get(i));
				blockedProcesses.get(i).setProcessState("READY");
				blockedProcesses.remove(i);
				i--;
			}
		}
		
		sortProcesses(doneBlockedProcesses, "arrival-number");
		
		for(int i =0; i<doneBlockedProcesses.size();i++){
			readyProcesses.add(doneBlockedProcesses.get(i));
			doneBlockedProcesses.remove(i);
			i--;
		}
		
		//Running Processes
		if(currentRunningProcess != null){
			if(currentRunningProcess.shouldRun()){
				if(currentRunningProcess.getTotalCPUTime() == 0){
					terminatedProcesses.add(currentRunningProcess);
					currentRunningProcess.setProcessState("TERMINATED");
					currentRunningProcess = null;
				}else{
					blockedProcesses.add(currentRunningProcess);
					if(currentRunningProcess.getRemainingBlockedTime() == -1){
						currentRunningProcess.setRemainingBlockedTime(randomOs(currentRunningProcess.getIOBurstTime()));
					}
					currentRunningProcess.setProcessState("BLOCKED");
					currentRunningProcess = null;
					
				}
			}
		}
		
	}
	
	public static void roundRobin(){
		
		final int quantum = 2;
		int quantumTracker = 2;
		if (readyProcesses.size() > 0 && currentRunningProcess == null) {
			// System.out.println("check0");
			currentRunningProcess = readyProcesses.get(0);
			// if this process hasn't ran before we initialize its running time
			if (currentRunningProcess.getRemainigRunningTime() == -1) {
				// System.out.println("\n"+
				// randomOs(currentRunningProcess.getCPUBurstTime()));
				currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
			}
			currentRunningProcess.setProcessState("RUNNING");
			readyProcesses.remove(0);
		}

		// go over the processes and check if any are ready
		for (int i = 0; i < processes.size(); i++) {
			// this process is ready
			if (processes.get(i).getArrivalTime() <= CPUCycle) {
				// if there are no running processes, we run the one that is
				// ready
				// System.out.println("\ncheck1----");
				if (currentRunningProcess == null) {
					// System.out.println("\ncheck2 should be here ----");
					currentRunningProcess = processes.get(i);
					currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
					// System.out.println("\n random os yields"+
					// randomOs(currentRunningProcess.getCPUBurstTime()));
					processes.get(i).setProcessState("RUNNING");
				}
				// there is already a running process, this one waits as ready
				else {
					readyProcesses.add(processes.get(i));
					processes.get(i).setProcessState("READY");
				}

				processes.remove(i);
				i--;

			}
		}
		
		passTime("roundRobin");
		
		if(verbose){
			verbosePrint("FCFS");
		}
		//blocked Processes
		ArrayList<Process> doneBlockedProcesses = new ArrayList<Process>();
		for(int i =0; i<blockedProcesses.size(); i++){
			if(blockedProcesses.get(i).doneBlocked()){
				doneBlockedProcesses.add(blockedProcesses.get(i));
				blockedProcesses.get(i).setProcessState("READY");
				blockedProcesses.remove(i);
				i--;
			}
		}
		
		//running Processes
		if(currentRunningProcess != null){
			if(currentRunningProcess.shouldRun()){
				if(currentRunningProcess.getTotalCPUTime() == 0){
					terminatedProcesses.add(currentRunningProcess);
					currentRunningProcess.setProcessState("TERMINATED");
					currentRunningProcess = null;
				}else{
					blockedProcesses.add(currentRunningProcess);
					if(currentRunningProcess.getRemainingBlockedTime() == -1){
						currentRunningProcess.setRemainingBlockedTime(randomOs(currentRunningProcess.getIOBurstTime()));
					}
					currentRunningProcess.setProcessState("BLOCKED");
					currentRunningProcess = null;
				}
				quantumTracker =2;
			}else{
				quantumTracker--;
				if(quantumTracker ==0){
					if(readyProcesses.size()>0){
						currentRunningProcess.setProcessState("READY");
						doneBlockedProcesses.add(currentRunningProcess);
						currentRunningProcess = readyProcesses.get(0);
						currentRunningProcess.setProcessState("RUNNING");
						if(currentRunningProcess.getRemainigRunningTime() == -1){
							currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
						}
						readyProcesses.remove(0);
						sortProcesses(doneBlockedProcesses, "arrival-number");
						for(int i = 0; i<doneBlockedProcesses.size(); i++){
							readyProcesses.add(doneBlockedProcesses.get(i));
							doneBlockedProcesses.remove(i);
							i--;
						}
						quantumTracker = 2;
					}
				}else if(doneBlockedProcesses.size() > 0){
					sortProcesses(doneBlockedProcesses, "arrival-number");
					currentRunningProcess.setProcessState("READY");
					doneBlockedProcesses.add(currentRunningProcess);
					currentRunningProcess = doneBlockedProcesses.get(0);
					currentRunningProcess.setProcessState("RUNNING");
					if(currentRunningProcess.getRemainigRunningTime() == -1){
						currentRunningProcess.setRemainingRunningTime(randomOs(currentRunningProcess.getCPUBurstTime()));
					}
					doneBlockedProcesses.remove(0);
					sortProcesses(doneBlockedProcesses, "arrival-number");
					for(int i=0; i< doneBlockedProcesses.size();i++){
						readyProcesses.add(doneBlockedProcesses.get(i));
						doneBlockedProcesses.remove(i);
						i--;
					}
					quantumTracker = 2;
				}else{
					quantumTracker = 2;
				}
			}
		}
		if(doneBlockedProcesses.size()>0){
			sortProcesses(doneBlockedProcesses, "arrival-number");
		}
		for(int i=0; i< doneBlockedProcesses.size();i++){
			readyProcesses.add(doneBlockedProcesses.get(i));
			doneBlockedProcesses.remove(i);
			i--;
		}

	}

	public static void passTime(String key) {

		boolean blockedProcess = false;

		for (int i = 0; i < processes.size(); i++) {
			processes.get(i).passTime();
		}

		for (int i = 0; i < blockedProcesses.size(); i++) {
			blockedProcesses.get(i).passTime();
			blockedProcess = true;
		}

		for (int i = 0; i < readyProcesses.size(); i++) {
			readyProcesses.get(i).passTime();
		}

		if (blockedProcess) {
			blocked++;
		}
		blockedProcess = false;
		if (currentRunningProcess != null) {
			if (key.equals("uniprogrammed")) {
				if (currentRunningProcess.getProcessState().equals("RUNNING")) {
					currentRunningProcess.passTime();
				}
			} else {
				currentRunningProcess.passTime();
			}

		}
		CPUCycle++;

	}

	// used to set everything to zero before running different scheduling
	// algorithms
	public static void flush() {
		processes.clear();
		initialProcesses.clear();
		readyProcesses.clear();
		blockedProcesses.clear();
		terminatedProcesses.clear();
		CPUCycle = 0;
		blocked = 0;
		currentRunningProcess = null;

	}

	// sorts the processes by their arrival time
	public static void sortProcesses(ArrayList<Process> toSort, String key) {
		if (key.equals("arrival")) {
			Collections.sort(toSort, new ProcessComparatorByArrivalTime());
		} else if (key.equals("arrival-number")) {
			Collections.sort(toSort, new ProcessComparatorByArrivalTimeAndNumber());
		} else if (key.equals("cpuTime")){
			Collections.sort(toSort, new ProcessComparatorByCPUTime());
		}

	}

	public static void normalPrint() {
		int finishingTime = 0;
		int waitTime = 0;
		int CPUTime = 0;
		int turnaroundTime = 0;

		for (int i = 0; i < initialProcesses.size(); i++) {
			System.out.println("\nProcess " + i + ":");
			initialProcesses.get(i).printFull();

			if (initialProcesses.get(i).getTotalTime() > finishingTime) {
				finishingTime = initialProcesses.get(i).getTotalTime();
			}

			CPUTime += initialProcesses.get(i).getTotalTime() - initialProcesses.get(i).getArrivalTime()
					- initialProcesses.get(i).getTimeBlocked() - initialProcesses.get(i).getTimeReady();

			waitTime += initialProcesses.get(i).getTimeReady();
			turnaroundTime += initialProcesses.get(i).getTotalTime() - initialProcesses.get(i).getArrivalTime();
		}

		System.out.println("\nSummary Data:");
		System.out.println("\tFinishing time: " + finishingTime);
		System.out.println("\tCPU Utilization: " + (float) CPUTime / finishingTime);
		System.out.println("\tI/O Utilization: " + (float) blocked / finishingTime);
		System.out.println("\tThroughput: " + (float) initialProcesses.size() / finishingTime * 100
				+ " processes per hundred cycles");
		System.out.println("\tAverage turnaround time : " + (float) turnaroundTime / initialProcesses.size());
		System.out.println("\tAverage waiting time: " + (float) waitTime / initialProcesses.size());
	}
	
	public static void verbosePrint(String key){
		System.out.print("\tBefore cycle " + CPUCycle + ":");
		for(int i= 0; i < initialProcesses.size();i++){
			if(initialProcesses.get(i).getProcessState().equals("INITIALIZED")){
				System.out.printf("%12s%3d",  "unstarted ", 0);
			}else if(initialProcesses.get(i).getProcessState().equals("READY")){
				System.out.printf("%12s%3d",  "ready",  0);
			}else if(initialProcesses.get(i).getProcessState().equals("RUNNING")){
				if(key.equals("RR")){
					System.out.printf("%12s",  "running");
				}else{
					System.out.printf("%12s%3d", "running ", initialProcesses.get(i).getRemainigRunningTime());
				}
			}else if(initialProcesses.get(i).getProcessState().equals("BLOCKED")){
				System.out.printf("%12s%3d", "blocked ", initialProcesses.get(i).getRemainingBlockedTime());
			}else if(initialProcesses.get(i).getProcessState().equals("TERMINATED")){
				System.out.printf("%12s%3d", "terminated " , 0);
			}
		}
		
		System.out.println();
	}

}

// Creates a comparator to sort the processes by their arrival time
class ProcessComparatorByArrivalTime implements Comparator<Process> {

	public int compare(Process process1, Process process2) {
		return ((Integer) process1.getArrivalTime()).compareTo((Integer) process2.getArrivalTime());

	}

}

class ProcessComparatorByCPUTime implements Comparator<Process>{
	
	public int compare(Process process1, Process process2){
		return ((Integer)process1.getTotalCPUTime()).compareTo((Integer)process2.getTotalCPUTime());
	}
}

class ProcessComparatorByArrivalTimeAndNumber implements Comparator<Process> {

	public int compare(Process process1, Process process2) {
		// this uses the tie breaker
		if (process1.getArrivalTime() == process2.getArrivalTime()) {
			return ((Integer) process1.getProcessNumber()).compareTo((Integer) process2.getProcessNumber());
		} else {
			return ((Integer) process1.getArrivalTime()).compareTo((Integer) process2.getArrivalTime());
		}
	}

}
