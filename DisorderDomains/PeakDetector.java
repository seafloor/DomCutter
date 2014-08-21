import java.util.LinkedHashSet;
import java.util.Scanner;
import java.io.File;

public class PeakDetector {
    private LinkedHashSet<String> topServers = new LinkedHashSet<String>(10); // names of the top 10 models
    private String bfactors; // holds the bfactors for a given model as a String 
    private String residueQuality; // each residue is denoted "o" or "d" for ordered or disordered
    private String domainNumber; // holds the final domain boundaries as a String
    private String sortFile; // path for the ModFOLDclust2.sort file
    private int upperLimit; // the cutoff value for a region to be considered disordered

    // call with the path of the ModFOLDclust2.sort file
    public PeakDetector (String sortFile) {
        this.sortFile = sortfile;

        // read in the names of the top 10 servers
        TopReader tr = new TopReader (sortFile);
        this.topServers = tr.getServerNames();
    }

    // might remove this section
    // use a class that implements TopReader instead, but this time it reads names and score
    private getBfactors() {
        File localScores = new File(this.sortFile);
        Scanner scanner = new Scanner(localScores);
        String section;
    }
}
