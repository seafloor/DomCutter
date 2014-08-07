

public class ReadFolder {
    private String[] // an array (or arraylist/linkedlist?) for holding each file location
    private String[] // an array to hold each PDB file as a string
    private String[] // an array to hold each fasta file as a string
    private String[] // an array to hold each B-factor list as a String? Might put this in a parent class?

    public ReadFolder () {
        // constructor called with directory
        // outputs file list to array
    }

    public ReadFolder () {
        // constructor called with a String of file names (and a directory?)
        // outputs file list to array again
    }

    private getMultiplePDB() {
        // private method to call getPDBString() for each array and put that in an array
    }

    public getPDBStrings() {
        // public method to return the array of PDB file strings
    }

    private getMultipleFasta() {
        // private method to call multiple getFasta() and output each to the array
    }

    public getFastaStrings() {
        //public method to return the fasta strings
    }

    public static main void () {
        // call at terminal
    }
}
