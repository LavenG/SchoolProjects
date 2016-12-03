import java.util.*;
import java.io.*;

/**
 * Created by alperenkaraoglu on 12/1/16.
 */

public class Paging {

    //public static String;
    public static File inputFile;
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

    public static LinkedList<Frame> lruTracker = new LinkedList<Frame>();

    public final int QUANTUM = 3;


    public static void main(String[] args){

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
        LRU();
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
        int frameTableSize = M/P;
        switch (J){
            case 1:
                processes.add(new Process(1, 1, 0, 0));
                break;
            case 2:
                for(int i =1; i <= 4 ;i++){
                    processes.add(new Process(i, 1, 0, 0));
                }
                break;
            case 3:
                for(int i =1; i <= 4 ;i++){
                    processes.add(new Process(i, 0, 0, 0));
                }
                break;
            case 4:
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

    public static void LRU(){

        for(int i = 1; i <= N; i++){
            System.out.println("TIME " + (i));
            //First time, must fault
            int wordReferenced = (int)Math.floor((double)(111+(i-1))%S);
            int pageReferenced = (int)Math.floor((double)(111+(i-1))%S/(double)P);

            if(processes.get(0).getInitial()){
                processes.get(0).incFault();
                int index = findHighestFreeFrameIndex(frameTable);
                if(index!= -1){
                    frameTable[index] = new Frame(processes.get(0).getProcessNumber(), pageReferenced, i);
                    lruTracker.addFirst(frameTable[index]);
                    System.out.println("FAULT " +1 + " References word " + wordReferenced + " page " + pageReferenced );
                }else{
                    evict(i);
                }
                processes.get(0).setInitial(false);
            }else{
                Frame currentFrame = new Frame(processes.get(0).getProcessNumber(), (int)Math.floor((double)(111+(i-1))%S/(double)P), i);
                int currentword = (int)Math.floor((double)(111+(i-1))%S);
                int currentReference = (int)Math.floor((double)(111+(i-1))%S/(double)P);
                System.out.println("CURRENT WORD " + currentword + " CURRENT PAGE " + currentReference);
                if(checkHit(currentFrame)){
                    int indexOfMRU = findIndexOfFrameInLinkedList(currentFrame);
                    Frame mru = lruTracker.remove(indexOfMRU);
                    lruTracker.addFirst(mru);
                    System.out.println("HIT");
                }else{
                    int index = findHighestFreeFrameIndex(frameTable);
                    processes.get(0).incFault();
                    if(index != -1){
                        frameTable[index] = new Frame(processes.get(0).getProcessNumber(), pageReferenced, i);
                        lruTracker.addFirst(frameTable[index]);
                        System.out.println("FAULT " + 1 + " References word " + wordReferenced + " page " + pageReferenced +" using free frame " + index);
                    }else{
                        evict(i);
                    }
                }
            }
        }
        }



    public static int findHighestFreeFrameIndex(Frame[] frameTable){

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
            if(frameTable[i] != null && frameTable[i].equals(f)){
                return true;
            }
        }
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

    public static void evict(int i){
        Frame toEvict = lruTracker.removeLast();
        processes.get(0).incResidencyTime(i-toEvict.getLoadTime());
        processes.get(0).incEvict();
        int insertionIndex = findIndexOfFrame(toEvict);
        System.out.print("Evicting page " + toEvict.getPage() + "of "+ toEvict.getProcessNumber() + "from fram " + insertionIndex);
        frameTable[insertionIndex] = new Frame(processes.get(0).getProcessNumber(), (int)Math.floor((double)(111+(i-1))%S/(double)P), i);
        lruTracker.addFirst(frameTable[insertionIndex]);
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



