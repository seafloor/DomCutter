import java.util.ArrayList;
import java.util.LinkedHashSet;

public class DomainPredictor () {
    private LinkedHashSet<String> topServers; // names of the top 10 models
    private ArrayList<String> topBfactors; // arraylist of the bfactors for each server
    private String sortFile; // path for the ModFOLDclust2.sort file

    public DomainPredictor (String sortFile, int listSize) {
        this.sortFile = sortfile;
        this.topServers = new LinkedHashSet<String>(listSize);
        this.topBfactors = new ArrayList<String>(listSize);

        // read in the names of the top 10 servers
        TopReader tr = new TopReader (sortFile, listSize);
        this.topServers = tr.getServerNames();
        this.topBfactors = tr.getBfactors();
    }
    
    private void creadteDomainPredictions() {
        // calls PeakDetector.java iteratively on each of the top 10 
        // created as a separate method so that any other prediction methods can be called here too
    }

    private void createConsensusPrediction() {
        // calls consensus algorithm to get a consensus prediction
        // will initially just create a consensus of the PeakDetector prediction for the top 10 models
        // will eventually also create consensus from the different methods of domain prediction
    }

    public ArrayList<String> getConsensusPrediction
        // call consensus.java to get consensus from the top 10 domain predictions
    }
 
    public static main void (String args[]) {
        //
    }
}
