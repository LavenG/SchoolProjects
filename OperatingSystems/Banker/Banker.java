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
        optimistic();
    }

    //Initializes the scanner to read the input
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

    //Parses the input and creates objects from it
    public static void parseInput() {

        //read and parse the number of tasks and the number of resources types
        int numberOfTasks = -1;
        int numberOfResourceTypes = -1;

        try {
            numberOfTasks = Integer.parseInt(inputScanner.next());
            numberOfResourceTypes = Integer.parseInt(inputScanner.next());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //for each task in the input create a task and initialize its task number
        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(new Task(i + 1, numberOfResourceTypes));
        }

        //For each resource type, add the number of resource we hae
        for (int i = 0; i < numberOfResourceTypes; i++) {
            int amountAvailable = -1;
            try {
                amountAvailable = Integer.parseInt(inputScanner.next());
            } catch (Exception e) {
                e.printStackTrace();
            }
            resources.add(amountAvailable);
        }

        //For each activity, assign it to the corresponding task by adding it to its activities arrayList
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
                        "Task Number :" + (i + 1) + " Activity: " + tasks.get(i).getActivities().get(j).toString());
        }

    }

    //finds a task by task number in an arrayList
    public Task findTask(int tasknumber, ArrayList<Task> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTaskNumber() == tasknumber) {
                return list.get(i);
            }
        }
        return null;
    }

    public static void optimistic() {

        int cycle = 0;

        int i = 0;

        int numberOfTasksTerminated = 0;
        int totalNumberOfTasks = tasks.size();

        //used to keep track of resources released in the current cycle so they can be added in the next cycle
        int [] resourcesReleased = new int [resources.size()];

        boolean incrementCycle = true;

        ArrayList<Task> waitList = new ArrayList<Task>();

        System.out.println("OPTIMISTIC");

        while (numberOfTasksTerminated != totalNumberOfTasks) {

            while (i < tasks.size()) {

                if (!tasks.get(i).getActivities().isEmpty()) {
                   //System.out.println(tasks.get(i).getActivities().get(0).toString());
                    switch (tasks.get(i).getActivities().get(0).getActivity()) {
                        case "initiate":
                            //we just add one to the time taken when initiating
                            tasks.get(i).addTimeTaken(1);
                            break;
                        case "request":
                            int resourceRequested = tasks.get(i).getActivities().get(0).getResourceType();
                            int amountRequested = tasks.get(i).getActivities().get(0).getAmount();

                            //grant the resource
                            if (amountRequested < resources.get(resourceRequested - 1)) {
                                resources.set(resourceRequested - 1, (resources.get(resourceRequested - 1) - amountRequested));
                                tasks.get(i).getResources()[resourceRequested-1] = amountRequested;
                                tasks.get(i).addTimeTaken(1);
                            }
                            //Don't grant the resource and make the task wait
                            else {
                                waitList.add(tasks.get(i));
                                tasks.remove(i);
                            }
                            //check request
                            break;
                        case "release":
                            int resourceReleased = tasks.get(i).getActivities().get(0).getResourceType();
                            int amountReleased = tasks.get(i).getActivities().get(0).getAmount();
                            tasks.get(i).addTimeTaken(1);
                            //keep track of the released resources
                            resourcesReleased[resourceReleased-1] += amountReleased;
                            break;
                        case "compute":
                            break;
                        case "terminate":
                            incrementCycle = false;
                            numberOfTasksTerminated ++;
                            for(int k = 0; k<tasks.get(i).getResources().length;k++){
                                resourcesReleased[i] += tasks.get(i).getResources()[i];
                            }
                            break;
                    }
                    tasks.get(i).getActivities().remove(0);
                }
                i++;
            }
            cycle += 1;

            //add back the resources that were realeased
            for(int n = 0; n<resources.size(); n++){
                resources.set(n, (resources.get(n)+resourcesReleased[n]));
            }

            incrementCycle = true;
            i = 0;
        }

        System.out.println("RESULT: ");
        int totalTimeTaken  = 0;
        for (int k = 0; k < tasks.size(); k++) {
            System.out.println("task number: " + tasks.get(k).getTaskNumber() + "time taken: " + tasks.get(k).getTimeTaken()
                    + "waiting time: " + tasks.get(k).getWaitingTime());
            totalTimeTaken += tasks.get(k).getTimeTaken();
        }
        System.out.println("Total time taken: " + totalTimeTaken);

    }

}
