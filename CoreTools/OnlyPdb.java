import java.io.FilenameFilter;
import java.io.File;
import java.util.ArrayList;

public class OnlyPdb implements FilenameFilter { 
    ArrayList<String> ext = new ArrayList<String>(7);

    public OnlyPdb() { 
        this.ext.add(".bfact");
        this.ext.add(".out");
        this.ext.add(".gnuplot");
        this.ext.add(".sort");
        this.ext.add(".filt");
        this.ext.add(".unsort");
        this.ext.add(".DS_Store");
    } 

    public boolean accept(File dir, String name) { 
        int counter = 0;
        for (String end : ext) {
            if (name.endsWith(end)) {
                counter++;
            }
        }
        return counter==0;
    } 
}
