import java.io.*;
import java.util.*;

public class Banker {

	public static String fileName;
	public static File inputFile;
	public static Scanner inputScanner;

	public static ArrayList<Task> tasks = new ArrayList<Task>();

	public static ArrayList<Integer> resources = new ArrayList<Integer>();

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Wrong usage of the command line, please provide the input file name as an argumnet");
			System.exit(1);
		} else {
			fileName = args[0];
		}

		initializeScanner();
		parseInput();
	}

	public static void initializeScanner() {
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
	}

	public static void parseInput() {

		int numberOfTasks = -1;
		int numberOfResourceTypes = -1;

		try {

			numberOfTasks = Integer.parseInt(inputScanner.next());
			numberOfResourceTypes = Integer.parseInt(inputScanner.next());

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < numberOfTasks; i++) {
			tasks.add(new Task(i + 1));
		}

		for (int i = 0; i < numberOfResourceTypes; i++) {
			int amountAvailable = -1;
			try {
				amountAvailable = Integer.parseInt(inputScanner.next());
			} catch (Exception e) {
				e.printStackTrace();
			}
			resources.add(amountAvailable);
		}
		
		while (inputScanner.hasNext() != false) {
			String activityType = inputScanner.next();
			int taskNumber = -1;
			int resourceType = -1;
			int number = -1;

			try {
				taskNumber = Integer.parseInt(inputScanner.next());
				resourceType = Integer.parseInt(inputScanner.next());
				number = Integer.parseInt(inputScanner.next());

			} catch (Exception e) {
				e.printStackTrace();
			}

			tasks.get(taskNumber - 1).getActivities().add(new Activity(activityType, taskNumber, resourceType, number));
		}

		for (int i = 0; i < tasks.size(); i++) {
			for (int j = 0; j < tasks.get(i).getActivities().size(); j++)
				System.out.println(
						"Task Number :" + (i+1) + " Activity: " + tasks.get(i).getActivities().get(j).toString());
		}

	}
	
	public static void optimistic(){
		int cycle = 0;
		
		int i = 0;
		int j = 0;
		
		while(i<tasks.get(j).getActivities().size()){
			while(j<tasks.size()){
				switch (tasks.get(j).getActivities().get(i).getActivity()){
					case "initiate":
						
				}
					
						
			}
		}
		
	}

}
