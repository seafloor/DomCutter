import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.Iterator;

public class TopReader {
    private LinkedHashSet<String> topServers; // will hold the list of the top 10 servers
    private ArrayList<String> topBfactors; // will hold the bfactor scores from the top 10 servers
    private ArrayList<String> fullOutput; // will hold the full ModFOLDclust2 output
    private String filePath; // hold the path for the ModFOLDclust2.sort file
    private String topServersString; // to hold the top 10 server list as a string
    private int listSize; // will hold the value for the number of top models to consider

    // call with the location of the ModFOLDclust2.sort file and the number of files to be considered
    // e.g. java TopReader /path/to/ModFOLDclust2.sort 10
    public TopReader (String filePath, int listSize) {
        this.filePath = filePath;
        this.listSize = listSize;
        this.topServers = new LinkedHashSet<String>(listSize);
        this.topBfactors = new ArrayList<String>(listSize);
        this.fullOutput = new ArrayList<String>(listSize);
    }

    // not called directly
    private void readSortFile() {
        String outputLine;
        int i = 0;
        try {
            Scanner scanner = new Scanner(new File(this.filePath));
            while (scanner.hasNextLine() && i < this.listSize) {
                outputLine = scanner.nextLine();
                this.fullOutput.add(outputLine);
                i++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // call to get the full output from .sort for the number of models specified
    public ArrayList<String> getSortFile() {
        readSortFile();
        return this.fullOutput;
    }

    // not called directly
    private void readTopServers() {
        for (String serverOutput : this.fullOutput) {
            Scanner scanner = new Scanner(serverOutput);
            String word;
            while (scanner.hasNext()) {
                word = scanner.next();
                String server;

                try {
                    if (word.matches("(.*)TS(.*)")) {
                        server = word;
                        this.topServers.add(server);
                    }
                } catch (InputMismatchException e) {
                    e.printStackTrace();
                }
            }
        }
	}

    // call to get server names as a linkedhashset
    public LinkedHashSet<String> getTopServers() {
        readTopServers();
        return this.topServers; //needs to be converted to a string!
    }

    // call to get server names as a String
    public String getServersAsString() {
        readTopServers();
        Iterator itr = this.topServers.iterator();
        StringBuffer buffer = new StringBuffer();
        while (itr.hasNext()) {
            buffer.append(itr.next() + " ");
        }

        this.topServersString = buffer.toString();
        return this.topServersString;
    }

    // not called directly
    private void readBfactors() {
        int counter;
        for (String serverOutput : this.fullOutput) {
            Scanner scanner = new Scanner(serverOutput);
            String bfactor;
            StringBuffer buffer = new StringBuffer();
            counter = 0;
            while (scanner.hasNext()) {
                if (counter > 1) {
                    bfactor = scanner.next();
                    buffer.append(bfactor + " ");
                } else {
                    scanner.next();
                }
                counter++;
            }

            String scores = buffer.toString();
            this.topBfactors.add(scores);
        }
    }

    // call to get local scores for each model as an arraylist
    public ArrayList<String> getBfactors() {
        readBfactors();
        return this.topBfactors;
    }
    
	public static void main(String[] args) {
        // TopReader tr = new TopReader (args[0]);
        // ArrayList<String> sf = new ArrayList<String>(tr.getSortFile());

        // LinkedHashSet<String> gts = new LinkedHashSet<String>(tr.getTopServers());
        // Iterator iter = gts.iterator();
        // while (iter.hasNext()) {
        //     System.out.println(iter.next());
        // }

        // String str = tr.getServersAsString();
        // System.out.println(str);

        // ArrayList<String> gbf = new ArrayList<String>(tr.getBfactors());
        // for (int i = 0; i < sf.size(); i++) {
        //     System.out.println(sf.get(i));
        //     System.out.println(gbf.get(i));
        // }
    }
}
			
