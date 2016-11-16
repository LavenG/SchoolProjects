import java.io.*;
import java.util.*;

public class Banker {

    public static String fileName;
    public static File inputFile;
    public static Scanner inputScanner;

    public static int totalNumberOfTasks;

    public static ArrayList<Task> tasks = new ArrayList<Task>();
    //Used to keep track of the tasks that are waiting to be executed
    public static ArrayList<Task> waitList = new ArrayList<Task>();
    //Used to keep track of the terminated tasks
    public static ArrayList<Task> terminatedTasks = new ArrayList<Task>();

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
        flush();
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
        totalNumberOfTasks = numberOfTasks;

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

    //clears all the global variables and re-initializes the scanners and the input
    public static void flush() {
        inputFile = null;
        inputScanner = null;
        totalNumberOfTasks = 0;
        tasks.clear();
        waitList.clear();
        terminatedTasks.clear();
        resources.clear();

        initializeScanner();
        parseInput();
    }

    public static void optimistic() {

        int cycle = 0;

        int i = 0;


        int numberOfTasksTerminated = 0;

        //used to keep track of resources released in the current cycle so they can be added in the next cycle
        int[] resourcesReleased = new int[resources.size()];

        boolean incrementCycle = true;

        System.out.println("OPTIMISTIC");

        while (terminatedTasks.size() != totalNumberOfTasks) {
//            System.out.println("Terminated tasks "+ terminatedTasks.size());
//            System.out.println("Current tasks " + tasks.size());
//            System.out.println("Waiting tasks " + waitList.size());
            //waitListCheck(true);
            System.out.println("In Cycle : " + cycle);
            System.out.println("Items available " + resources.get(0));

            while (i < tasks.size()) {
                System.out.println("CURR Task " + tasks.get(i).getTaskNumber() + " activity " + tasks.get(i).getActivities().get(0));
                if (tasks.get(i).isComputing()) {
                    //tasks.get(i).addTimeComputing(1);
                    System.out.println("Task :" + tasks.get(i).getTaskNumber() + "Has been computing for " + tasks.get(i).getTimeComputing() + "/" + tasks.get(i).getActivities().get(0).getAmount());
                    if (tasks.get(i).getActivities().get(0).getAmount() == 1) {
                        tasks.get(i).getActivities().remove(0);
                        tasks.get(i).setTimeComputing(0);
                        tasks.get(i).setIsComputing(false);

                    } else if (tasks.get(i).getTimeComputing() >= tasks.get(i).getActivities().get(0).getAmount()) {

                        tasks.get(i).getActivities().remove(0);
                        tasks.get(i).setTimeComputing(0);
                        tasks.get(i).setIsComputing(false);

                    }

                } else {
                    if (!tasks.get(i).getActivities().isEmpty()) {
                        //System.out.println(tasks.get(i).getActivities().get(0).toString());
                        switch (tasks.get(i).getActivities().get(0).getActivity()) {
                            case "initiate":

                                //we just add one to the time taken when initiating
                                System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                tasks.get(i).getActivities().remove(0);
                                break;
                            case "request":

                                int resourceRequested = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountRequested = tasks.get(i).getActivities().get(0).getAmount();

                                //grant the resource
                                if (amountRequested <= resources.get(resourceRequested - 1)) {
                                    System.out.println("IN REQUEST, RESOURCE AVAILABLE " + resources.get(resourceRequested - 1) + " AMOUNT REQUESTED " + amountRequested);

                                    resources.set(resourceRequested - 1, (resources.get(resourceRequested - 1) - amountRequested));
                                    tasks.get(i).getResources()[resourceRequested - 1] += amountRequested;
                                    System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                    System.out.println("Resourecs now available at this point " + resources.get(0));
                                    tasks.get(i).getActivities().remove(0);
                                }
                                //Don't grant the resource and make the task wait
                                else {
                                    System.out.println("Task :" + tasks.get(i).getTaskNumber() + " is being added to the waitlist");
                                    waitList.add(tasks.get(i));
                                    tasks.remove(i);
                                    i--;
                                }
                                //check request
                                break;
                            case "release":
                                int resourceReleased = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountReleased = tasks.get(i).getActivities().get(0).getAmount();
                                System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                tasks.get(i).getActivities().remove(0);
                                //keep track of the released resources
                                resourcesReleased[resourceReleased - 1] += amountReleased;

                                // System.out.println("SOMETHING NEGATIVE HERE " + (tasks.get(i).getResources()[resourceReleased-1] - amountReleased));
                                //System.out.println(tasks.get(i).getResources()[resourceReleased-1]);
                                tasks.get(i).getResources()[resourceReleased - 1] -= amountReleased;
                                break;
                            case "compute":
                                tasks.get(i).setIsComputing(true);
                                tasks.get(i).addTimeComputing(1);
                                break;
                            case "terminate":
                                //System.out.println("ENTER TERMINATE");
                                incrementCycle = false;
                                System.out.println("BEFORE RESOURCES ADDED BACK " + resources.get(0));
                                addBackResources(tasks.get(i).getResources());
                                tasks.get(i).setTimeTaken(cycle);
                                terminatedTasks.add(tasks.get(i));

                                System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                tasks.get(i).getActivities().remove(0);
                                tasks.remove(i);
                                i--;
                                break;
                        }
                        //We're done with the current acitvity
                    }
                }
                i++;
            }
            addComputingTimes();
            //check if the current cycle should be incremented
            cycle += 1;
            //add back the resources that were realeased
            addBackResources(resourcesReleased);
            //System.out.println("BEFORE CHECKS RESOURCE AVAILBLE" + resources.get(0));
            //check if anything is done waiting
            addWaitingTimes();
            waitListCheck();
            //check if there is a deadlock
            while (isDeadLocked()) {
                handleDeadLocked();
                waitListCheck();
            }

            //Reset the index to go through the tasks again
            i = 0;
        }


        resultPrinter();

    }

    public static void resultPrinter() {
        Collections.sort(terminatedTasks, new comparatorByTaskNumber());
        System.out.println("RESULT: ");
        int totalTimeTaken = 0;
        for (int k = 0; k < terminatedTasks.size(); k++) {
            if (!terminatedTasks.get(k).isAborted()) {
                System.out.println("task number: " + terminatedTasks.get(k).getTaskNumber() + " time taken: " + terminatedTasks.get(k).getTimeTaken()
                        + " waiting time: " + terminatedTasks.get(k).getWaitingTime());
                totalTimeTaken += terminatedTasks.get(k).getTimeTaken();
            } else {
                System.out.println("Task number: " + terminatedTasks.get(k).getTaskNumber() + " aborted");
            }

        }
        System.out.println("Total time taken: " + totalTimeTaken);
    }

    public static void waitListCheck() {
        for (int i = 0; i < waitList.size(); i++) {
            int resourceRequested = waitList.get(i).getActivities().get(0).getResourceType();
            int amountRequested = waitList.get(i).getActivities().get(0).getAmount();
            //If the request can now be potentially satisfied, remove the task from the waitlist
            if (amountRequested <= resources.get(resourceRequested - 1)) {
                System.out.println("Items available before moving lists " + resources.get(resourceRequested - 1));
                System.out.println("Task :" + waitList.get(i).getTaskNumber() + " is being moved from the wait list to the ready list");
                tasks.add(waitList.get(i));
                waitList.remove(i);
            }
        }
    }

    public static void addWaitingTimes() {
        for (int i = 0; i < waitList.size(); i++) {
            waitList.get(i).addWaitingTime(1);
        }
    }

    public static void addComputingTimes() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(tasks.get(i).getTaskNumber() + "FUCKING COMPUTING TIME IS " + tasks.get(i).getActivities().get(0).getAmount());
            if (tasks.get(i).isComputing() && tasks.get(i).getActivities().get(0).getAmount() == 1) {
                System.out.println("THUS WE MUST BE HERE FUCKER");
                tasks.get(i).getActivities().remove(0);
                tasks.get(i).setTimeComputing(0);
                tasks.get(i).setIsComputing(false);
            } else if (tasks.get(i).isComputing()) {
                tasks.get(i).addTimeComputing(1);
            }
        }
    }

    //Check for deadlocks
    public static void handleDeadLocked() {
        //If everything has not terminated and there is nothing running then there must be a deadlock
        if (terminatedTasks.size() != totalNumberOfTasks && tasks.isEmpty()) {
            //Find the lowest numbered Task to be aborted
            int lowestNumberedTask = findLowestNumberedTask(waitList);
            addBackResources(waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)).getResources());
            waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)).abort();
            System.out.println("Task :" + waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)).getTaskNumber() + " is being aborted");
            terminatedTasks.add(waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)));
            waitList.remove(findTaskIndexByNumber(waitList, lowestNumberedTask));

        }
    }

    public static boolean isDeadLocked() {
        if (terminatedTasks.size() != totalNumberOfTasks && tasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static int findLowestNumberedTask(ArrayList<Task> toSearch) {
        int lowestNumber = toSearch.get(0).getTaskNumber();
        for (int i = 0; i < toSearch.size(); i++) {
            if (toSearch.get(i).getTaskNumber() < lowestNumber) {
                lowestNumber = toSearch.get(i).getTaskNumber();
            }
        }
        System.out.println("The lowest numbered task's number is " + lowestNumber);
        return lowestNumber;
    }

    public static int findTaskIndexByNumber(ArrayList<Task> list, int taskNumber) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTaskNumber() == taskNumber) {
                return i;
            }
        }
        return -1;
    }

    public static void addBackResources(int[] resourcesReleased) {
        for (int i = 0; i < resourcesReleased.length; i++) {
            resources.set(i, (resources.get(i) + resourcesReleased[i]));
            resourcesReleased[i] = 0;
        }
    }

    public static void banker() {

        int cycle = 0;

        int i = 0;


        int numberOfTasksTerminated = 0;

        //used to keep track of resources released in the current cycle so they can be added in the next cycle
        int[] resourcesReleased = new int[resources.size()];

        boolean incrementCycle = true;

        System.out.println("OPTIMISTIC");

        while (terminatedTasks.size() != totalNumberOfTasks) {
//            System.out.println("Terminated tasks "+ terminatedTasks.size());
//            System.out.println("Current tasks " + tasks.size());
//            System.out.println("Waiting tasks " + waitList.size());
            //waitListCheck(true);
            System.out.println("In Cycle : " + cycle);
            System.out.println("Items available " + resources.get(0));

            while (i < tasks.size()) {
                System.out.println("CURR Task " + tasks.get(i).getTaskNumber() + " activity " + tasks.get(i).getActivities().get(0));
                if (tasks.get(i).isComputing()) {
                    //tasks.get(i).addTimeComputing(1);
                    System.out.println("Task :" + tasks.get(i).getTaskNumber() + "Has been computing for " + tasks.get(i).getTimeComputing() + "/" + tasks.get(i).getActivities().get(0).getAmount());
                    if (tasks.get(i).getActivities().get(0).getAmount() == 1) {
                        tasks.get(i).getActivities().remove(0);
                        tasks.get(i).setTimeComputing(0);
                        tasks.get(i).setIsComputing(false);

                    } else if (tasks.get(i).getTimeComputing() >= tasks.get(i).getActivities().get(0).getAmount()) {

                        tasks.get(i).getActivities().remove(0);
                        tasks.get(i).setTimeComputing(0);
                        tasks.get(i).setIsComputing(false);

                    }

                } else {
                    if (!tasks.get(i).getActivities().isEmpty()) {
                        //System.out.println(tasks.get(i).getActivities().get(0).toString());
                        switch (tasks.get(i).getActivities().get(0).getActivity()) {
                            case "initiate":

                                //we just add one to the time taken when initiating
                                System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                tasks.get(i).getActivities().remove(0);
                                break;
                            case "request":

                                int resourceRequested = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountRequested = tasks.get(i).getActivities().get(0).getAmount();

                                //grant the resource
                                if (amountRequested <= resources.get(resourceRequested - 1)) {
                                    System.out.println("IN REQUEST, RESOURCE AVAILABLE " + resources.get(resourceRequested - 1) + " AMOUNT REQUESTED " + amountRequested);

                                    resources.set(resourceRequested - 1, (resources.get(resourceRequested - 1) - amountRequested));
                                    tasks.get(i).getResources()[resourceRequested - 1] += amountRequested;
                                    System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                    System.out.println("Resourecs now available at this point " + resources.get(0));
                                    tasks.get(i).getActivities().remove(0);
                                }
                                //Don't grant the resource and make the task wait
                                else {
                                    System.out.println("Task :" + tasks.get(i).getTaskNumber() + " is being added to the waitlist");
                                    waitList.add(tasks.get(i));
                                    tasks.remove(i);
                                    i--;
                                }
                                //check request
                                break;
                            case "release":
                                int resourceReleased = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountReleased = tasks.get(i).getActivities().get(0).getAmount();
                                System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                tasks.get(i).getActivities().remove(0);
                                //keep track of the released resources
                                resourcesReleased[resourceReleased - 1] += amountReleased;

                                // System.out.println("SOMETHING NEGATIVE HERE " + (tasks.get(i).getResources()[resourceReleased-1] - amountReleased));
                                //System.out.println(tasks.get(i).getResources()[resourceReleased-1]);
                                tasks.get(i).getResources()[resourceReleased - 1] -= amountReleased;
                                break;
                            case "compute":
                                tasks.get(i).setIsComputing(true);
                                tasks.get(i).addTimeComputing(1);
                                break;
                            case "terminate":
                                //System.out.println("ENTER TERMINATE");
                                incrementCycle = false;
                                System.out.println("BEFORE RESOURCES ADDED BACK " + resources.get(0));
                                addBackResources(tasks.get(i).getResources());
                                tasks.get(i).setTimeTaken(cycle);
                                terminatedTasks.add(tasks.get(i));

                                System.out.println("Task :" + tasks.get(i).getTaskNumber() + " completed " + tasks.get(i).getActivities().get(0).toString());
                                tasks.get(i).getActivities().remove(0);
                                tasks.remove(i);
                                i--;
                                break;
                        }
                        //We're done with the current acitvity
                    }
                }
                i++;
            }
            addComputingTimes();
            //check if the current cycle should be incremented
            cycle += 1;
            //add back the resources that were realeased
            addBackResources(resourcesReleased);
            //System.out.println("BEFORE CHECKS RESOURCE AVAILBLE" + resources.get(0));
            //check if anything is done waiting
            addWaitingTimes();
            waitListCheck();
            //check if there is a deadlock
            while (isDeadLocked()) {
                handleDeadLocked();
                waitListCheck();
            }

            //Reset the index to go through the tasks again
            i = 0;
        }


        resultPrinter();

    }


public static void isSafe(){

}


static class comparatorByTaskNumber implements Comparator<Task> {
    public int compare(Task task1, Task task2) {
        // this uses the tie breaker
        if (task1.getTaskNumber() == task2.getTaskNumber()) {
            return ((Integer) task1.getTaskNumber()).compareTo((Integer) task2.getTaskNumber());
        } else {
            return ((Integer) task1.getTaskNumber()).compareTo((Integer) task2.getTaskNumber());
        }
    }
}



}
