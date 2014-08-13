import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.io.File;
import java.io.FilenameFilter;

public class ReadFolder {
    private ArrayList<String> fileLocations; // an ArrayList (or arraylist/linkedlist?) for holding each file location
    private ArrayList<String> pdbStrings; // an ArrayList to hold each PDB file as a string
    private ArrayList<String> fastaStrings; // an ArrayList to hold each fasta file as a string
    private String target; // string to hold the target name
    private String fastaAsStrings; // will hold each fasta String on a new line of a String. Alternative to fastaStrings
    private String header; // the header of the fasta file. Only used if fastas returned as String, not ArrayList

    public ReadFolder (String path, String target) {
        // constructor called with directory
        // outputs file list to ArrayList
        // assumes all files in the directory are pdb files!
        File directory = new File(path);
        this.target = target;
        if (directory.isDirectory()) {
            FilenameFilter pdb = new OnlyPdb();
            ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(directory.list(pdb)));
            int numFiles = fileNames.size();
            pdbStrings = new ArrayList<String>(numFiles);
            fastaStrings = new ArrayList<String>(numFiles);
            fileLocations = new ArrayList<String>(numFiles);
            for (String name : fileNames) {
                File notDir = new File (path+name);
                if (!notDir.isDirectory()) {
                    fileLocations.add(path + name);
                }
            }
        } else {
            System.out.println("Error: String is not a directory");
        }
    }

    public ReadFolder (LinkedHashSet fileList, String path, String target) {
        // constructor called with a String of file names and a directory
        // outputs file list to ArrayList again
        this.target = target;
        int numFiles = fileList.size();
        fileLocations = new ArrayList<String>(numFiles);
        pdbStrings = new ArrayList<String>(numFiles);
        fastaStrings = new ArrayList<String>(numFiles);
        File directory = new File(path);

        if (directory.isDirectory()) {
            Iterator itr = fileList.iterator();
            String stringPath;
            while (itr.hasNext()) {
                stringPath = path + itr.next();
                File filePath = new File(stringPath);
                if (filePath.isFile()) {
                    fileLocations.add(stringPath);
                }
            }
        }
    }

    // not called directly
    // private method to call getPDBString() for each array and put that in an array
    private void getMultiplePDB() {
        String pdbString;
        for (String pdbLocation : this.fileLocations) {
            System.out.println(pdbLocation);
            ReadPDB rp = new ReadPDB (pdbLocation, this.target);
            pdbString = rp.getPDBString();
            this.pdbStrings.add(pdbString);
        }
    }

    // to be called directly
    // public method to return the array of PDB file strings
    public ArrayList getPDBStrings() {
        getMultiplePDB();
        return pdbStrings;
    }

    // not called directly
    // private method to call multiple getFasta() and output each to the array
    private void getMultipleFasta() {
        String fastaString;
        for (String pdbLocation : this.fileLocations) {
            ReadPDB rp = new ReadPDB (pdbLocation, this.target);
            rp.getPDBString();
            fastaString = rp.getFasta();
            this.fastaStrings.add(fastaString);
        }
    }

    // called directly
    // public method to return the fasta strings
    public ArrayList getFastaStringsAsArray() {
        getMultipleFasta();
        return this.fastaStrings;
    }

    // puts output in format needed for consensus.java
    private void getMultipleFastaAsString() {
        String fastaString;
        String header = "";
        int counter = 0;
        StringBuffer buffer = new StringBuffer();
        for (String pdbLocation : this.fileLocations) {
            ReadPDB rp = new ReadPDB (pdbLocation, this.target);
            rp.getPDBString();
            fastaString = rp.getFasta();

            Scanner scanner = new Scanner(fastaString);
            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                if(!line.startsWith(">")) {
                    buffer.append(line);
                } else {
                    if (counter == 0) {
                        header = line;
                    }
                }
            }
            buffer.append("\n");
            counter++;
        }
        this.fastaAsStrings = buffer.toString();
        this.header = header;
    }

    public String getFastaStrings() {
        getMultipleFastaAsString();
        return this.fastaAsStrings;
        // need to write private method to return as a string
    }
    
    // called directly
    // only call if you have already called getFastaAsStrings()
    public String getFastaHeader() {
        return this.header;
    }

    public static void main (String[] args) {
        ReadFolder rf = new ReadFolder (args[0], args[1]);
        ArrayList<String> ap = new ArrayList<String>(rf.getPDBStrings());
        ArrayList<String> al = new ArrayList<String>(rf.getFastaStringsAsArray());
        // for (int i = 0; i < ap.size(); i++) {
            // System.out.println(ap.get(i));
            // System.out.println(al.get(i));
        // }
    }
}
