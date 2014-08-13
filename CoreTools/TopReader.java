import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.LinkedHashSet;

public class TopReader {
	private LinkedHashSet topServers = new LinkedHashSet(10); // will hold the list of the top 10 servers
    private String filePath;

    public TopReader (String filePath) {
        this.filePath = filePath;
    }

    private void readSortFile() {
        String server;
        int i = 0;
        try {
            Scanner scanner = new Scanner(new File(this.filePath));
            while(scanner.hasNext()) {
                server = scanner.next();

                //Stores the top 10 server names in a Linked HashSet
                try {
                    if(server.matches("(.*)TS(.*)") && i<10) {
                        this.topServers.add(server);
                        i++;
                    } else {
                        //System.out.println("no matches found");
                    }
                } catch(InputMismatchException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}

    public LinkedHashSet getServerNames() {
        readSortFile();
        return this.topServers; //needs to be converted to a string!
    }

    // public String getServersAsString() {
        //
    // }

	public static void main(String[] args) {
        TopReader tr = new TopReader (args[0]);
        System.out.println(tr.getServerNames());
    }
}
			
