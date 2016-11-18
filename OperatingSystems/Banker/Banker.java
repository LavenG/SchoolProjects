import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Banker {

    //These variables are used to read in and parse the input
    public static String fileName;
    public static File inputFile;
    public static Scanner inputScanner;

    //Used to keep track of the total number of tasks
    public static int totalNumberOfTasks;

    //Used to keep track of the current running tasks
    public static ArrayList<Task> tasks = new ArrayList<Task>();
    //Used to keep track of the tasks that are waiting to be executed
    public static ArrayList<Task> waitList = new ArrayList<Task>();
    //Used to keep track of the terminated tasks
    public static ArrayList<Task> terminatedTasks = new ArrayList<Task>();

    //Used to keep track of currently available resources
    public static ArrayList<Integer> resources = new ArrayList<Integer>();

    public static void main(String[] args) {

        //Checks that the number of arguments is valid
        if (args.length != 1) {
            System.out.println("Wrong usage of the command line, please provide the input file name as an argumnet");
            System.exit(1);
        } else {
            fileName = args[0];
        }

        //Initializes the scanner to read from the correct file
        initializeScanner();
        //Parses in the input and creates necessary objects from it
        parseInput();
        //Simulates the optimistic-FIFO resource manager
        optimistic();
        //clears all the global variables, re-initializes the scanners and reads in the input
        flush();

        System.out.println();
        //Simulates the Dijkstra's Banker's algorithm
        banker();
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

    //Simulates the optimistic-FIFO resource manager
    public static void optimistic() {

        //Used to keep track of cycles
        int cycle = 0;

        //used to iterate through the tasks
        int i = 0;

        //used to keep track of resources released in the current cycle so they can be added in the next cycle
        int[] resourcesReleased = new int[resources.size()];

        System.out.println("FIFO");

        //Keep simulating until the end
        while (terminatedTasks.size() != totalNumberOfTasks) {

            //iterate over the tasks
            while (i < tasks.size()) {

                //Handle tasks that are currently computing and the edge case when a task only computes for one cycle
                if (tasks.get(i).isComputing()) {

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
                    //If the task still has stuff to do
                    if (!tasks.get(i).getActivities().isEmpty()) {
                        switch (tasks.get(i).getActivities().get(0).getActivity()) {
                            case "initiate":
                                //we just add one to the time taken when initiating as FIFO doesn't care about claims
                                tasks.get(i).getActivities().remove(0);
                                break;
                            case "request":
                                //Keep track of the resource and amount requested
                                int resourceRequested = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountRequested = tasks.get(i).getActivities().get(0).getAmount();

                                //grant the resource if conditions are met
                                if (amountRequested <= resources.get(resourceRequested - 1)) {
                                    resources.set(resourceRequested - 1, (resources.get(resourceRequested - 1) - amountRequested));
                                    tasks.get(i).getResources()[resourceRequested - 1] += amountRequested;
                                    tasks.get(i).getActivities().remove(0);
                                }
                                //Don't grant the resource and make the task wait
                                else {
                                    waitList.add(tasks.get(i));
                                    tasks.remove(i);
                                    i--;
                                }
                                break;
                            case "release":
                                //Keep track of the resource and amount released
                                int resourceReleased = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountReleased = tasks.get(i).getActivities().get(0).getAmount();
                                //account for the released resource
                                resourcesReleased[resourceReleased - 1] += amountReleased;
                                //substract the released resource from the task's resources
                                tasks.get(i).getResources()[resourceReleased - 1] -= amountReleased;
                                tasks.get(i).getActivities().remove(0);
                                break;
                            case "compute":
                                //Make the task compute
                                tasks.get(i).setIsComputing(true);
                                tasks.get(i).addTimeComputing(1);
                                break;
                            case "terminate":
                                //release all of the tasks resources
                                addBackResources(tasks.get(i).getResources());
                                //Determine the time at which the task terminated
                                tasks.get(i).setTimeTaken(cycle);
                                //Move the task to terminated tasks
                                terminatedTasks.add(tasks.get(i));
                                tasks.get(i).getActivities().remove(0);
                                tasks.remove(i);
                                i--;
                                break;
                        }
                    }
                }
                i++;
            }
            //adds time to tasks that are computing
            addComputingTimes();
            //increment the cycle
            cycle += 1;
            //add back the resources that were released in the previous cycle
            addBackResources(resourcesReleased);
            //add time to tasks that are waiting
            addWaitingTimes();
            //check if anything is done waiting
            waitListCheck();

            //check if there is a deadlock until the simulation is not deadlocked anymore
            while (isDeadLocked()) {
                handleDeadLocked();
                waitListCheck();
            }
            //Reset the index to go through the tasks again
            i = 0;
        }
        //print the results
        resultPrinter();

    }


    //Simulates Dijkstra's Banker's Algorithm
    public static void banker() {

        //Keeps track of the cycle
        int cycle = 0;

        //Used to iterate through the tasks
        int i = 0;

        //used to keep track of resources released in the current cycle so they can be added in the next cycle
        int[] resourcesReleased = new int[resources.size()];

        //Keep track of initial resources to check if any claims exceeds them
        ArrayList<Integer> initialResources = new ArrayList<Integer>();
        initialResources = copyIntegerArrayList(resources);

        System.out.println("BANKER's");
        //Keep simulating until the end
        while (terminatedTasks.size() != totalNumberOfTasks) {
            //iterate over the tasks
            while (i < tasks.size()) {
                //Handle tasks that are currently computing and the edge case when a task only computes for one cycle
                if (tasks.get(i).isComputing()) {
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
                    //If the task still has stuff to do
                    if (!tasks.get(i).getActivities().isEmpty()) {
                        switch (tasks.get(i).getActivities().get(0).getActivity()) {
                            case "initiate":
                                //Keep track of the resource and amount claimed
                                int resourceType = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountClaimed = tasks.get(i).getActivities().get(0).getAmount();

                                //Make sure the claim doesn't exceed the initial amount of resource Available and if it does abort the task
                                if (amountClaimed > initialResources.get(resourceType - 1)) {
                                    tasks.get(i).abort();
                                    terminatedTasks.add(tasks.get(i));
                                    tasks.remove(i);
                                    i--;
                                } else {
                                    //keep track of the claim
                                    tasks.get(i).getClaims()[resourceType - 1] = amountClaimed;
                                    tasks.get(i).getActivities().remove(0);
                                }

                                break;
                            case "request":
                                //Keep track of the resource and amount requested
                                int resourceRequested = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountRequested = tasks.get(i).getActivities().get(0).getAmount();

                                //Used to make sure that if the request is filled the amount that would be held does not exceed the claim
                                int amountThatWouldBeHeld = amountRequested +tasks.get(i).getResources()[resourceRequested-1];
                                //If the amount does exceed the claim abort the task
                                if(amountThatWouldBeHeld > tasks.get(i).getClaims()[resourceRequested-1]){
                                    tasks.get(i).abort();
                                    resourcesReleased[resourceRequested-1] += tasks.get(i).getResources()[resourceRequested-1];
                                    terminatedTasks.add(tasks.get(i));
                                    tasks.remove(i);
                                    i--;
                                }else{
                                    //If the request is safe full fill it if not add the task to the wait list
                                    if (isSafe(tasks.get(i))) {
                                        resources.set(resourceRequested - 1, (resources.get(resourceRequested - 1) - amountRequested));
                                        tasks.get(i).getResources()[resourceRequested - 1] += amountRequested;
                                        tasks.get(i).getActivities().remove(0);
                                    } else {
                                        waitList.add(tasks.get(i));
                                        tasks.remove(i);
                                        i--;
                                    }
                                }
                                break;
                            case "release":
                                //Keep track of the amount and resource released
                                int resourceReleased = tasks.get(i).getActivities().get(0).getResourceType();
                                int amountReleased = tasks.get(i).getActivities().get(0).getAmount();
                                tasks.get(i).getActivities().remove(0);
                                //keep track of the released resources
                                resourcesReleased[resourceReleased - 1] += amountReleased;
                                tasks.get(i).getResources()[resourceReleased - 1] -= amountReleased;
                                break;
                            case "compute":
                                //Make the task compute
                                tasks.get(i).setIsComputing(true);
                                tasks.get(i).addTimeComputing(1);
                                break;
                            case "terminate":
                                //release all of the tasks resources
                                addBackResources(tasks.get(i).getResources());
                                //Determine the time at which the task terminated
                                tasks.get(i).setTimeTaken(cycle);
                                //Move the task to terminated tasks
                                terminatedTasks.add(tasks.get(i));
                                tasks.get(i).getActivities().remove(0);
                                tasks.remove(i);
                                i--;
                                break;
                        }
                        //We're done with the current activity
                    }
                }
                i++;
            }
            //adds time to tasks that are computing
            addComputingTimes();
            //increment the cycle
            cycle += 1;
            //add back the resources that were released
            addBackResources(resourcesReleased);
            //check if anything is done waiting
            addWaitingTimes();
            //remove the items from the wait list
            bankersWaiList();
            //Reset the index to go through the tasks again
            i = 0;
        }
        //print the results
        resultPrinter();

    }


    //Used to format and print the result of the simulation
    public static void resultPrinter() {
        //Make sure the tasks are printed in order by task number
        Collections.sort(terminatedTasks, new comparatorByTaskNumber());
        int totalTimeTaken = 0;
        int totalTimeWaited = 0;
        for (int k = 0; k < terminatedTasks.size(); k++) {
            int taskNumber = terminatedTasks.get(k).getTaskNumber();
            int timeTaken = terminatedTasks.get(k).getTimeTaken();
            int waitTime = terminatedTasks.get(k).getWaitingTime();
            float percentage = ((float)waitTime/(float)timeTaken)*100;
            if (!terminatedTasks.get(k).isAborted()) {


                totalTimeTaken += timeTaken;
                totalTimeWaited += waitTime;
                System.out.printf("%10s%2d%6d%6d%6.0f%s\n","TASK", taskNumber , timeTaken, waitTime , percentage, "%");

            } else {
                System.out.printf("%10s%2d%18s\n","TASK", terminatedTasks.get(k).getTaskNumber(), "aborted");
            }

        }

        float totalPercentage = (float)totalTimeWaited/(float)totalTimeTaken*100;
        System.out.printf("%12s%6d%6d%6.0f%s\n","total", totalTimeTaken, totalTimeWaited , totalPercentage, "%");

    }

    //Increments the wait time of all the tasks in the wait list
    public static void addWaitingTimes() {
        for (int i = 0; i < waitList.size(); i++) {
            waitList.get(i).addWaitingTime(1);
        }
    }

    //Increments the computing time of all the tasks that are computing
    public static void addComputingTimes() {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isComputing() && tasks.get(i).getActivities().get(0).getAmount() == 1) {
                tasks.get(i).getActivities().remove(0);
                tasks.get(i).setTimeComputing(0);
                tasks.get(i).setIsComputing(false);
            } else if (tasks.get(i).isComputing()) {
                tasks.get(i).addTimeComputing(1);
            }
        }
    }

    //Handles deadlocks by aborting the lowest numbered task
    public static void handleDeadLocked() {
        //If everything has not terminated and there is nothing running then there must be a deadlock
        if (terminatedTasks.size() != totalNumberOfTasks && tasks.isEmpty()) {
            //Find the lowest numbered Task to be aborted
            int lowestNumberedTask = findLowestNumberedTask(waitList);
            addBackResources(waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)).getResources());
            waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)).abort();
            terminatedTasks.add(waitList.get(findTaskIndexByNumber(waitList, lowestNumberedTask)));
            waitList.remove(findTaskIndexByNumber(waitList, lowestNumberedTask));

        }
    }
    //Checks if there is a deadlock
    public static boolean isDeadLocked() {
        if (terminatedTasks.size() != totalNumberOfTasks && tasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    //Finds the lowest number task
    public static int findLowestNumberedTask(ArrayList<Task> toSearch) {
        int lowestNumber = toSearch.get(0).getTaskNumber();
        for (int i = 0; i < toSearch.size(); i++) {
            if (toSearch.get(i).getTaskNumber() < lowestNumber) {
                lowestNumber = toSearch.get(i).getTaskNumber();
            }
        }
        return lowestNumber;
    }

    //Finds the index of a certain task in a list and returns it
    public static int findTaskIndexByNumber(ArrayList<Task> list, int taskNumber) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTaskNumber() == taskNumber) {
                return i;
            }
        }
        return -1;
    }
    //adds back the resources in the provided array to the current resources
    public static void addBackResources(int[] resourcesReleased) {
        for (int i = 0; i < resourcesReleased.length; i++) {
            resources.set(i, (resources.get(i) + resourcesReleased[i]));
            resourcesReleased[i] = 0;
        }
    }
//Checks if the current state is safe
public static boolean isSafe(Task task){
    //Copy everything to avoid changing the original values
    ArrayList<Integer> resourcesCopy = copyIntegerArrayList(resources);
    ArrayList<Task> tasksCopy = copyTaskArrayList(tasks);
    ArrayList<Task> waitListCopy = copyTaskArrayList(waitList);
    ArrayList<Task> terminatedCopy = copyTaskArrayList(terminatedTasks);

    //substract the amount that would be granted from the resources
    int taskIndex = findTaskIndexByNumber(tasksCopy, task.getTaskNumber());
    int amountRequested = task.getActivities().get(0).getAmount();
    int resourceRequested = task.getActivities().get(0).getResourceType();


    tasksCopy.get(taskIndex).getResources()[resourceRequested-1] += amountRequested;
    resourcesCopy.set(resourceRequested-1, resourcesCopy.get(resourceRequested-1)-amountRequested);

    int terminated = -1;
    //Checks if the resource is allocated whether the remaining tasks can terminate or not
    while(terminated != 0) {
        terminated = 0;
        for (int i = 0; i < tasksCopy.size(); i++) {

            int resourcesSatisfied = 0;

            for (int j = 0; j < tasksCopy.get(i).getResources().length; j++) {
                if (tasksCopy.get(i).getClaims()[j] - tasksCopy.get(i).getResources()[j] <= resourcesCopy.get(j)) {
                    resourcesSatisfied++;
                }
            }
            if (resourcesSatisfied == resourcesCopy.size()) {
                terminated++;
                terminatedCopy.add(tasksCopy.get(i));
                for (int k = 0; k < tasksCopy.get(i).getResources().length; k++) {
                    resourcesCopy.set(k, resourcesCopy.get(k) + tasksCopy.get(i).getResources()[k]);
                }
                tasksCopy.remove(i);
            }
        }
    }
    if(tasksCopy.size() != 0){
        return false;
    }
    return true;


}
//Checks if anything in the wait list satisfies the conditions to be removed
public static void waitListCheck(){
    Stack<Task> stack = new Stack<>();
    for (int i = 0; i < waitList.size(); i++) {
        int resourceRequested = waitList.get(i).getActivities().get(0).getResourceType();
        int amountRequested = waitList.get(i).getActivities().get(0).getAmount();
        if (amountRequested <= resources.get(resourceRequested - 1)) {
            stack.push(waitList.remove(i));
        }
    }
    while(!stack.isEmpty()){
        tasks.add(0, stack.pop());
    }
}
//Removes everything from the waitlist and places it in front of the current running list in a FIFO manner
public static void bankersWaiList(){
    Stack<Task> stack = new Stack<Task>();

    int size = waitList.size();
    while(!waitList.isEmpty()){
        stack.push(waitList.remove(0));
    }
    while(!stack.isEmpty()){
        tasks.add(0, stack.pop());
    }
}
//Copies an integer array list such that the original is not modified
public static ArrayList<Integer> copyIntegerArrayList(ArrayList<Integer> toCopy){
    ArrayList<Integer> copy = new ArrayList<Integer>();
    for(int i =0; i < toCopy.size() ; i++){
        copy.add(toCopy.get(i));
    }
    return copy;
}
//Copies a task array list such that the original is not modified
public static ArrayList<Task> copyTaskArrayList(ArrayList<Task> toCopy){
    ArrayList<Task> copy = new ArrayList<Task>();
    for(Task task : toCopy){
        copy.add(new Task(task));
    }
    return copy;
}
//Creates a comparator to sort tasks by task number
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
