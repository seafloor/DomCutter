import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

public class ReadPDB {
    private String PDBString; // hold the PDB file to be returned as a string
    private String PDBFasta;  // will hold the Fasta file to be returned
    private String PDBfile;   // PDB file location
    private String target;    // target name

    public ReadPDB (String PDBfile, String target) {
        this.PDBfile = PDBfile;
        this.target = target;
    }

    // constructor overload - used if calling class just to get Fasta sequence from a string
    public ReadPDB () {
        System.out.println("Call object.getFasta(String) to get the fasta sequence for a PDB Atom file in String format");
    }

    // used to read in the PDB file - not called directly
    private void getPDBFile() {
        // main method
        try {
            Scanner wholeScan = new Scanner(new File(PDBfile)).useDelimiter("\\Z");
            PDBString = wholeScan.next();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // getter to return the PDB file as a String
    public String getPDBString() {
        getPDBFile();
        return PDBString;
    }

    // the 3 methods below are needed to return a Fasta File of the String
    // method below is called publicly to retrieve fasta file
    public String getFasta() {
        PDBFasta = getFastaFromPDB(PDBString);
        return PDBFasta;
    }

    // overload for getting fasta sequence if you already have the PDB file as a String
    public String getFasta(String PDBString) {
        PDBFasta = getFastaFromPDB(PDBString);
        return PDBFasta;
    }

    // hashmap to convert the triplet AA code to single letter AA code for the Fasta file
    // called by the private method below
    private String getAminoAcid(String threeLetter) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ALA", "A");
        map.put("ARG", "R");
        map.put("ASN", "N");
        map.put("ASP", "D");
        map.put("CYS", "C");
        map.put("GLN", "Q");
        map.put("GLU", "E");
        map.put("GLY", "G");
        map.put("HIS", "H");
        map.put("ILE", "I");
        map.put("LEU", "L");
        map.put("LYS", "K");
        map.put("MET", "M");
        map.put("PHE", "F");
        map.put("PRO", "P");
        map.put("SER", "S");
        map.put("THR", "T");
        map.put("TRP", "W");
        map.put("TYR", "Y");
        map.put("VAL", "V");
        
        return map.get(threeLetter);
    }

    // main method to convert PDB file to Fasta sequence
    // not called directly
    private String getFastaFromPDB(String PDBString) {
        StringBuffer buffer = new StringBuffer();
        String resnum, threeLetter, oneLetter, currLine;
        int current;
        int previous = 0;
        int residue_count = 0;
        Scanner lines = new Scanner(PDBString);
        while (lines.hasNextLine()) {
            currLine = lines.nextLine();
            // trim the substring so it just contains the residue number and convert to integer
            if (currLine.startsWith("ATOM")) {
                resnum = currLine.substring(21, 26);
                resnum = resnum.trim();
                current = Integer.parseInt(resnum);
            } else {
                continue;
            }

            if (current != previous) {
                residue_count++;                            // increases the number of residues by one if the residue number has changed
                threeLetter = currLine.substring(17, 20);
                oneLetter = getAminoAcid(threeLetter);      // retrieve the three letter code from currLine and look up the single letter code from the hashmap
                buffer.append(oneLetter);                   // add the single letter code to the string buffer
            }
            previous = current;
        }

        String output = buffer.toString();
        if (output.length() == residue_count) {
        } else {
            System.out.println("Error: sequence count and sequence length are not equal");
            System.out.println("Sequence:" + " \n" + output);
            System.out.println("Length: " + residue_count);
        }

        // create Fasta as a String
        output = ">" + target + ", , " + residue_count + " residues" + "\n" + output; 
        return output;
    }

    // write the String containing the Fasta file to a file
    public void writeFastaFile(String outputDir) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputDir + target + ".fasta")));
            writer.write(PDBFasta);
            writer.close();
        } catch (IOException writeE) {
            System.out.println("Error: couldn't write Fasta file");
            writeE.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ReadPDB rp = new ReadPDB(args[0], args[1]);
        String pdb = rp.getPDBString();
        System.out.println(pdb);
        String pdbfasta = rp.getFasta();
        System.out.println(pdbfasta);
    }
}
