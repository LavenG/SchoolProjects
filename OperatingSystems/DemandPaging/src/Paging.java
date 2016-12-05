import java.util.*;
import java.io.*;

/**
 * Created by alperenkaraoglu on 12/1/16.
 */

public class Paging {

    //Scanner to read in the random numbers
    public static Scanner inputScanner;

    //Keeps track of the machine size
    public static int M;
    //Keeps track of the page size in words
    public static  int P;
    //Keeps track of the size of a process
    public static int S;
    //Keeps track of the "job mix"
    public static int J;
    //Keeps track of the number of references for each process
    public static int N;
    //Keeps track of the replacement Algorithm;
    public static String R = new String();
    //Keeps track of the processes
    public static ArrayList<Process> processes = new ArrayList<Process>();
    //Used to implement the frame table
    public static Frame[] frameTable;
    //Used to keep track of the references per process
    public static int [] references;

    public static LinkedList<Frame> lruTracker = new LinkedList<Frame>();

    public static Stack<Frame> lifoTracker = new Stack<Frame>();

    public static final int QUANTUM = 3;


    public static void main(String[] args){

        try{
            inputScanner = new Scanner(new File("random-numbers"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //Checks that the number of arguments is valid
        if (args.length != 6) {
            System.out.println("Wrong usage of the command line, please provide 6 arguments");
            System.exit(1);
        } else {
            try{
                M = Integer.parseInt(args[0]);
                P = Integer.parseInt(args[1]);
                S = Integer.parseInt(args[2]);
                J = Integer.parseInt(args[3]);
                N = Integer.parseInt(args[4]);
                R = args[5];
                System.out.println(M/P);
                frameTable = new Frame[M/P];
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        infoPrinter();

        processInitializer();
        Pager();
        resultPrinter();


    }

    public static void infoPrinter(){
        System.out.println("The machine size is " + M + ".");
        System.out.println("The page size is " + P + ".");
        System.out.println("The process size is " + S + ".");
        System.out.println("The job mix number is " + J +  ".");
        System.out.println("The number of references per process is " + 10 +".");
        System.out.println("The replacement algorithm is " + R +".");
    }

    public static void processInitializer(){
        switch (J){
            case 1:
                processes.add(new Process(1, 1, 0, 0));
                references = new int[1];
                references[0] = N;

                break;
            case 2:
                references = new int[4];
                for(int i =1; i <= 4 ;i++){
                    processes.add(new Process(i, 1, 0, 0));
                }
                for(int i = 0; i< references.length;i++){
                    references[i] = N;
                }
                break;

            case 3:
                references = new int[4];
                for(int i =1; i <= 4 ;i++){
                    processes.add(new Process(i, 0, 0, 0));
                }
                for(int i = 0; i< references.length;i++){
                    references[i] = N;
                }
                break;
            case 4:
                references = new int[4];
                for(int i = 0; i< references.length;i++){
                    references[i] = N;
                }
                processes.add(new Process(1, .75f, .25f, 0));
                processes.add(new Process(2, .75f, 0, .25f));
                processes.add(new Process(3, .75f, .125f, .125f));
                processes.add(new Process(4, .5f, .125f, .125f));
                break;
            default:
                System.err.println("Error please pick J between 1 and 4");
                break;
        }
    }

    public static void Pager(){
        //Keep track to see if everything has finished and the time
        int counter = 1;
        while(counter <= N * processes.size()){
            for(int i = 0; i < processes.size(); i++){
                for(int j = 0; j < QUANTUM; j++){
                    System.out.println("\nTIME " + (counter));
                    references[i]--;

                    //First time, must fault
                    if(processes.get(i).getInitial()){
                        System.out.println("CURRENT WORD " + (int)Math.floor((double)(111*(i+1))%S) + " CURRENT PAGE " + (int)Math.floor((double)(111*(i+1))%S/(double)P));
                        processes.get(i).incFault();
                        int index = findHighestFreeFrameIndex();
                        if(index!= -1){
                            fault(index, new Frame(processes.get(i).getProcessNumber(), (int)Math.floor((double)(111*(i+1))%S/(double)P), counter));
                        }else{
                            evict(i, counter);
                        }
                        processes.get(i).setInitial(false);
                        processes.get(i).setPreRef((int)Math.floor((double)(111*(i+1))%S));
                    }else{
                        System.out.println("Current Word " + processes.get(i).getCurrentRef() + " Current page " +(int)Math.floor((double)(processes.get(i).getCurrentRef())/(double)P));
                        Frame currentFrame = new Frame(processes.get(i).getProcessNumber(), (int)Math.floor((double)processes.get(i).getCurrentRef()/(double)P), counter);


                        if(checkHit(currentFrame)){
                            hit(currentFrame);
                        }else{
                            int index = findHighestFreeFrameIndex();
                            processes.get(i).incFault();
                            if(index != -1){
                                fault(index, new Frame(processes.get(i).getProcessNumber(), (int)Math.floor((double)processes.get(i).getCurrentRef()/(double)P), counter));
                            }else{
                                evict(i, counter);
                            }
                        }
                        processes.get(i).setPreRef(processes.get(i).getCurrentRef());
                    }

                    int randomNum = inputScanner.nextInt();
                    System.out.println(processes.get(i).getProcessNumber() + "Using random num " + randomNum);
                    double y = randomNum/(Integer.MAX_VALUE + 1d);


                    if(y < processes.get(i).getA()){

                        processes.get(i).setCurrentRef(((processes.get(i).getPreRef()+1)%S));

                    }else if(y <processes.get(i).getA() + processes.get(i).getB()){
                        processes.get(i).setCurrentRef((processes.get(i).getPreRef()-5+S)%S);
                    }else if(y <processes.get(i).getA()+ processes.get(i).getB() + processes.get(i).getC()){
                        processes.get(i).setCurrentRef((processes.get(i).getPreRef()+4)%S);
                    }else{
                        int secondRand = inputScanner.nextInt();
                        processes.get(i).setCurrentRef(secondRand%S);
                        System.out.println("Using second random num " + secondRand);
                    }
                    System.out.println("AFTER ALL SET CURRENT REF TO " + processes.get(i).getCurrentRef());
                    counter++;
                    if(references[i]==0){
                        j = 5;
                    }
                }

            }

        }

    }



    public static int findHighestFreeFrameIndex(){

        for(int i = frameTable.length-1; i > -1; i--){
            if(frameTable[i] == null){
                return i;
            }
        }
        return -1;
    }

    public static boolean checkHit(Frame f){
        //find the process referenced
        for(int i =0; i< frameTable.length ;i++){
//            if(frameTable[i] != null){
//                System.out.println("at frame " + i + " init " + frameTable[i].getProcessNumber() + " " + frameTable[i].getPage());
//            }else{
//                System.out.println("at frame " + i + " NULL");
//            }

            if(frameTable[i] != null && frameTable[i].equals(f)){
//                System.out.println("COMPARING: ");
//                System.out.println(frameTable[i].getProcessNumber() + " " + frameTable[i].getPage());
//                System.out.println(f.getProcessNumber() + " " + f.getPage());
                return true;
            }
        }
        System.out.println("RETURNED FALSE");
        return false;
    }

    public static int findIndexOfFrame(Frame f){
        for(int i=0;i<frameTable.length;i++){
            if(frameTable[i] != null && frameTable[i].equals(f)){
                return i;
            }
        }
        System.out.println("ERROR");
        System.exit(0);
        return -1;
    }

    public static int findIndexOfFrameInLinkedList(Frame f){
        for(int i=0;i<lruTracker.size();i++){
            if(lruTracker.get(i)!= null && f.equals(lruTracker.get(i))){
                return i;
            }
        }
        System.out.println("ERROR");
        System.exit(0);
        return -1;
    }

    public static void hit(Frame f){
        System.out.println("HIT");
        switch (R){
            case "lru":
                int indexOfMRU = findIndexOfFrameInLinkedList(f);
                Frame mru = lruTracker.remove(indexOfMRU);
                lruTracker.addFirst(mru);

                break;
        }
    }

    public static void evict(int i,int time){
        switch (R){
            case "lru":
                //Evict the least recently used
                Frame toEvict = lruTracker.removeLast();
                //Increment residency time and number of evictions
                processes.get(toEvict.getProcessNumber()-1).incResidencyTime(time-toEvict.getLoadTime());
                processes.get(toEvict.getProcessNumber()-1).incEvict();
                //find the index at which the frame to evict is in the frame table
                int insertionIndex = findIndexOfFrame(toEvict);

                System.out.println("Evicting page " + toEvict.getPage() + " of "+ toEvict.getProcessNumber() + " from frame " + insertionIndex);
                frameTable[insertionIndex] = new Frame(processes.get(i).getProcessNumber(), (int)Math.floor((double)(processes.get(i).getCurrentRef())%S/(double)P), time);
                System.out.println("After evction we have " + frameTable[insertionIndex].getProcessNumber() + " with reference " + frameTable[insertionIndex].getPage());
                lruTracker.addFirst(frameTable[insertionIndex]);
                break;
            case "lifo":
                Frame lastIn = lifoTracker.pop();
                //Increment residency time and number of evictions
                processes.get(lastIn.getProcessNumber()-1).incResidencyTime(time-lastIn.getLoadTime());
                processes.get(lastIn.getProcessNumber()-1).incEvict();
                //find the index at which the frame to evict is in the frame table
                int lastInIndex = findIndexOfFrame(lastIn);
                frameTable[lastInIndex] = new Frame(processes.get(i).getProcessNumber(), (int)Math.floor((double)(processes.get(i).getCurrentRef())%S/(double)P), time);
                lifoTracker.push(frameTable[lastInIndex]);
                break;
            case "random":
                int rand = inputScanner.nextInt();
                System.out.println("In evict Using random num " + rand);
                int randIndex = rand%frameTable.length;
                System.out.println("Evicting page " + frameTable[randIndex].getPage() + " of "+ frameTable[randIndex].getProcessNumber() + " from frame " + randIndex);
                processes.get(frameTable[randIndex].getProcessNumber()-1).incResidencyTime(time-frameTable[randIndex].getLoadTime());
                processes.get(frameTable[randIndex].getProcessNumber()-1).incEvict();
                frameTable[randIndex] = new Frame(processes.get(i).getProcessNumber(), (int)Math.floor((double)(processes.get(i).getCurrentRef())%S/(double)P),time);

                break;
        }

    }

    public static void fault(int index, Frame f){
        switch(R){
            case "lru":
                frameTable[index] = f;
                lruTracker.addFirst(frameTable[index]);
                break;
            case "lifo":
                frameTable[index]= f;
                lifoTracker.push(f);
                break;
            case "random":
                frameTable[index] = f;
                break;
            default:
                System.err.println("Error wrong usage of paging algorithm");
                System.exit(1);
                break;
        }

        System.out.println("FAULT " + 1 + " References word " + " page " + f.getPage() +" using free frame " + index);
    }

    public static void resultPrinter(){
        int totalFaults = 0;
        int totalResidency = 0;
        int totalEvictions =0;
        for(int i=0; i<processes.size();i++){
            System.out.print("\nProcess " + processes.get(i).getProcessNumber() + " had " + processes.get(i).getFaults() + " fault ");
            if(processes.get(i).getEvictionCount() != 0){
                System.out.print(" and " + ((float)processes.get(i).getResidencyTime()/processes.get(i).getEvictionCount()) + " average residency.\n");
                totalEvictions += processes.get(i).getEvictionCount();
            }else{
                System.out.println("\n\tWith no evictions, the average residence is undefined.");
            }

            totalFaults += processes.get(i).getFaults();
            totalResidency +=processes.get(i).getResidencyTime();
        }
        if(totalEvictions != 0){
            System.out.println("\nThe total number of faults is " + totalFaults + " and the overall average residency is " + ((float)totalResidency/totalEvictions) + ".");
        }else{
            System.out.println("\nThe total number of faults is " + totalFaults);
            System.out.println("\tWith no evictions, the overall average residence is undefined.");
        }

    }

}



