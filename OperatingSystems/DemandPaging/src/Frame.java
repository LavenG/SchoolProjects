import java.util.Objects;

/**
 * Created by alperenkaraoglu on 12/2/16.
 */
public class Frame {
    private int processNumber;
    private int page;
    private int timeLoaded;

    public Frame(int processNumber, int page, int cycle){
        this.processNumber = processNumber;
        this.page = page;
        timeLoaded = cycle;
    }

    public int getProcessNumber(){return this.processNumber;}
    public int getPage(){return this.page;}
    public int getLoadTime(){return this.timeLoaded;}

    @Override
    public boolean equals(Object o){
        Frame f = (Frame)o;
//        System.out.println("In .equals COMPARING: ");
//        System.out.println(f.getProcessNumber() + " " + f.getPage());
//        System.out.println(this.getProcessNumber() + " " + this.getPage());
        if(f.getProcessNumber() == this.getProcessNumber() && f.getPage() == this.getPage()){
            return true;
        }
        return false;
    }
}
