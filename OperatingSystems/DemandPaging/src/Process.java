/**
 * Created by alperenkaraoglu on 12/2/16.
 */
public class Process {

    private int processNumber;

    private int currentRef;
    private int prevRef;

    private int faults = 0;
    private int residencyTime = 0;
    private int evictionCount = 0;
    private float A;
    private float B;
    private float C;

    private boolean initial;

    public Process(int processNumber, float A, float B, float C) {
        this.processNumber = processNumber;
        this.faults = 0;
        this.initial = true;
        this.A = A;
        this.B = B;
        this.C = C;

    }

    public int getProcessNumber(){return this.processNumber;}
    public int getFaults(){return this.faults;}
    public boolean getInitial(){return this.initial;}

    public void setInitial(boolean b){this.initial = b;}

    public void incFault(){this.faults++;}
    public void incResidencyTime(int i){this.residencyTime+=i;}
    public int getResidencyTime(){return this.residencyTime;}
    public void incEvict(){this.evictionCount++;}
    public int getEvictionCount(){return this.evictionCount;}

    public float getA(){return this.A;}
    public float getB(){return this.B;}
    public float getC(){return this.C;}

    public int getPrevRef(){return this.prevRef;}
    public void setPrevRef(int ref){this.prevRef =ref;}

    public int getCurrentRef(){return this.currentRef;}
    public void setCurrentRef(int ref){this.currentRef = ref;}

}
