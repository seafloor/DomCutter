import java.util.ArrayList;
import java.util.LinkedHashSet;

public class DomainPredictor {
    private LinkedHashSet<String> topServers; // names of the top 10 models
    private ArrayList<String> topBfactors; // arraylist of the bfactors for each server
    private String sortFile; // path for the ModFOLDclust2.sort file
    private String domainPredictions; // holds the output of all domain predictions
    private String consensusPrediction; // the final consensus prediction to be returned

    public DomainPredictor (String sortFile, int listSize) {
        this.sortFile = sortFile;
        this.topServers = new LinkedHashSet<String>(listSize);
        this.topBfactors = new ArrayList<String>(listSize);

        // read in the names of the top 10 servers
        TopReader tr = new TopReader (sortFile, listSize);
        this.topServers = tr.getTopServers();
        this.topBfactors = tr.getBfactors();
    }
    
    private int createDomainPredictions() {
        // calls PeakDetector.java iteratively on each of the top 10 
        // created as a separate method so that any other prediction methods can be called here too
        int lengthCheck = 0;
        String singlePrediction;
        StringBuffer buffer = new StringBuffer();

        for (String bfacts : this.topBfactors) {
            PeakDetector pd = new PeakDetector (bfacts, 3);
            singlePrediction = pd.getSinglePrediction();
            buffer.append(singlePrediction + "\n");

            if(lengthCheck == 0) {
                lengthCheck = singlePrediction.length();
            }
        }
        this.domainPredictions = buffer.toString();
        return lengthCheck; // creates problem that the length given is just for one file
    }

    private void createConsensusPrediction(int lengthCheck) {
        // calls consensus algorithm to get a consensus prediction
        // will initially just create a consensus of the PeakDetector prediction for the top 10 models
        // will eventually also create consensus from the different methods of domain prediction
        consensus cs = new consensus(this.domainPredictions, lengthCheck);
        this.consensusPrediction = cs.getConsensus();
    }

    public String getConsensusPrediction() {
        // calls the above method
        // returns a string of the final domain prediction
        int lengthCheck = createDomainPredictions();
        createConsensusPrediction(lengthCheck);
        return this.consensusPrediction;
    }
 
    public static void main (String args[]) {
        //
    }
}
