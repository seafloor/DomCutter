import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class PeakDetector {
    private ArrayList<Double> bfactors = new ArrayList<Double>(); // holds the bfactors for a given model as a String 
    private String residueQuality; // each residue is denoted "o" or "d" for ordered or disordered
    private String domainPred; // holds the final domain boundaries as a String
    private int upperLimit = 13; // the cutoff value for a region to be considered disordered
    private int window; // the size of the sliding window

    // call with the path of the ModFOLDclust2.sort file and the number of servers to consider
    public PeakDetector (String bfactors, int window) {
        this.window = window;

        // convert the bfactor string to an ArrayList<Double>
        Scanner scan = new Scanner(bfactors);
        String stringscore;
        Double score;
        while (scan.hasNext()) {
            stringscore = scan.next();
            score = Double.parseDouble(stringscore);
            this.bfactors.add(score);
        }
    }

    // private int findUpperLimit() {
        // will eventually be used to find to best upperlimit value for a file
        // this.upperLimit = new upper limit
    // }

    private void findDisorderedRegions() {
        Double average;
        int middle;
        StringBuffer buffer = new StringBuffer(bfactors.size());

        for (middle = 0; middle < this.bfactors.size(); middle++) {
            // set to deal with number out of bounds exceptions
            int boundary = (window -1) / 2;

            // average the values
            // if the "middle" value is to close to the edge of the array, and would cause an outofbounds exception
            // then just take the value as it is; no average is taken
            if (middle < boundary) {
                average = bfactors.get(middle);
            } else if ((this.bfactors.size() - middle) <= boundary) {
                average = bfactors.get(middle);
            } else {
                average = bfactors.get(middle);
                int count = 1; // starts at 1 because the middle has already been set
                int gap = 1; // moves away from the middle value by 1 each time
                try{
                    while (count < window) {
                        average += bfactors.get(middle - gap);
                        average += bfactors.get(middle + gap);
                        count += 2;
                        gap ++;
                    }
                } catch (ArrayIndexOutOfBoundsException aob) {
                    aob.printStackTrace();
                    System.out.println("middle = " + middle);
                    System.out.println("gap = " + gap);
                }
                average = average / window;
            }

            // see if the average value meets the threshold for being disordered
            if (average >= this.upperLimit) {
                buffer.append("d");
            } else {
                buffer.append("o");
            }
        }
        this.residueQuality = buffer.toString();
    }

    private void disorderedToDomains() {
        int currentDomain = 1;
        int currentSize = 0;
        char currentType = '\0';
        char previousType = '\0';
        StringBuffer buffer = new StringBuffer(this.residueQuality.length());

        for (int i = 0; i < this.residueQuality.length(); i++) {
            if (i == 0) {
                currentSize++;
                buffer.append(Integer.toString(currentDomain));
                previousType = currentType;
                continue;
            }
            currentType = this.residueQuality.charAt(i);

            if (currentType == previousType) {
                currentSize++;
                buffer.append(Integer.toString(currentDomain));
            } else if (currentType != previousType) {
                if (currentSize >= 30) { // change value to change domain size
                    currentSize = 0;
                    currentDomain++;
                    buffer.append(Integer.toString(currentDomain));
                } else if (currentSize < 30) { // change value to change domain size
                    currentSize++;
                    buffer.append(Integer.toString(currentDomain));
                }
            }
        }
        this.domainPred = buffer.toString();
    }

    // used to retrieve a single domain prediction for one file
    // call the above 2 methods
    public String getSinglePrediction() {
        findDisorderedRegions();
        System.out.println("disorder pred: " + this.residueQuality + "\n");
        disorderedToDomains();
        System.out.println("domain pred: " + this.domainPred + "\n");
        return this.domainPred;
    } 

    public static void main (String args[]) {
        String bfact = args[0];
        int size = Integer.parseInt(args[1]);
        PeakDetector pd = new PeakDetector(bfact, size);
        pd.getSinglePrediction();
    }
}
