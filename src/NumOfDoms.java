import java.io.*;
import java.util.*;

public class NumOfDoms {
    String strdomain_nums;
    LinkedHashSet<String> domain_nums = new LinkedHashSet();
    public NumOfDoms(String consensus) {
        //find the number of domains in the file - used in different bits of code later
        for(int index = 1; index < consensus.length(); index++) {
            String dom = Integer.toString(index);
            if(consensus.contains(dom)) {
                domain_nums.add(dom);
            }
            else {
                break;
            }
        }
        if(consensus.contains("-")) {
            domain_nums.add("-");
        }
        System.out.println("The number of domains in this target is " + domain_nums.size() + ". Domain numbers: " + domain_nums);
        
        //convert to string
        Iterator iter = domain_nums.iterator();
        StringBuffer strbffr = new StringBuffer();
        while(iter.hasNext()) {
            strbffr.append(iter.next());
        }
        strdomain_nums = strbffr.toString();
    }

    public String getNumberOfDomains() {
        return strdomain_nums;
    }

    public LinkedHashSet<String> getDomainsAsHash() {
        return domain_nums;
    }

    public static void main(String[] args) {
        NumOfDoms nod = new NumOfDoms(args[0]);
        System.out.println( nod.getNumberOfDomains() );
        System.out.println( nod.getDomainsAsHash() );
    }

}
