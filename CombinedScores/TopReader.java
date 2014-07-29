// will read the ModFOLDclust2 QA and take in the top 10 server names
 
//imports
import java.io.*;
import java.util.*;

//filename required when calling the class instance
public class TopReader {
	LinkedHashSet top_servers = new LinkedHashSet();
    public TopReader(String filename) throws IOException {
		//LinkedHashSet top_servers = new LinkedHashSet();
        String server;
		Scanner scanner = new Scanner(new File(filename));
        int i = 0;
		while(scanner.hasNext()) {
			server = scanner.next();
			//System.out.println(server);
			//Stores the top 10 server names in a Linked HashSet
            try {
				if(server.matches("(.*)TS(.*)") && i<10) {
					//System.out.println("match found");
					//System.out.println(server + " " + score);
                    top_servers.add(server);
                    i++;
                } else {
					//System.out.println("no matches found");
				}
			} catch(InputMismatchException e) {
				//e.printStackTrace();
			}
		}
	}
    public LinkedHashSet getServerNames() {
        return top_servers; //needs to be converted to a string!
    }
	public static void main(String[] args) throws IOException {
        TopReader tr = new TopReader(args[0]);
        System.out.println(tr.getServerNames());
    }
}
			
