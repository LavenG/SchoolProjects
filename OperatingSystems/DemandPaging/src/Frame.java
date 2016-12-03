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
        if(f.getProcessNumber() == f.getProcessNumber() && f.getPage() == ((Frame) o).getPage()){
            return true;
        }
        return false;
    }
}
