import java.io.*;
import java.util.*;

//checks the number of residues in each domain
//it might be worth altering to check the number of continuous residues for a single domain to deal with over-allocation of domains as discontinuous by PDP
//will ignore "-" as they aren't domains
public class DomSizes {
    String domainsize = "";
    ArrayList<Integer> domainsizes = new ArrayList();
    public DomSizes (String consensuspred, String domainnums) {
        int domcounter = 0;
        for(int i = 0; i < domainnums.length(); i++) {
            char domnum = domainnums.charAt(i);
            if(Character.toString(domnum).equals("-")) {
                continue;
            }
            for(int index = 0; index < consensuspred.length(); index++) {
                char dom_to_check = consensuspred.charAt(index);
                if(domnum == dom_to_check) {
                    domcounter++;
                }
            }
            domainsizes.add(domcounter);
            domcounter = 0;
        }
        //change so it is a new line after each, which can be read with scanner!
        for(int i : domainsizes) {
            String s = Integer.toString(i);
            domainsize += s;
            domainsize += "\n";
        }
        //System.out.println(domainsize);
    }

    //method allowing object to be called
    public String getDomainSizes() {
        return domainsize;
    }

    public static void main(String[] args) {
        //String thing = "11--112212223-333442445--";
        //String other = "12345-";
        DomSizes ds = new DomSizes(args[0], args[1]);
        //DomSizes ds = new DomSizes(thing, other);
        System.out.println( ds.getDomainSizes() );
    }
}
