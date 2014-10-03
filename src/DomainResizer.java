import java.io.*;
import java.util.*;

//takes domain sizes from DomSizes and allocates those smaller than 12 residues to the neighbouring domains
public class DomainResizer {
    String finalconsensuspred = "";
    public DomainResizer(String consensuspred_original, String domnumbers) {
        //needs to be changed to string with newline chars (use scanner) - no need to do this - there a no newline chars in the pdp consensus file
        //add an if statement to prevent it running on only 1 domain-long sequences - probably not necessary actually, 
        //add checks in the code to make sure that the char at index won't be reassigned to a "-"
        //Call DomSizes to get the sizes of the domains
        DomSizes ds = new DomSizes(consensuspred_original, domnumbers);
        String domainsizes = ds.getDomainSizes();
        System.out.println("The domain sizes are: " + domainsizes);
        StringBuffer consensuspred = new StringBuffer(consensuspred_original);
        int domnumindex = 0;
        int checkbefore = 0;
        int checkafter = 0;
        int upperdiff = 0;
        int lowerdiff = 0;
        int afternum = 0;
        int beforenum = 0;
        char domainnumber = '\0';
        char dombefore = '\u0000';
        char domafter = '\u0000';
        char temp = '\u0000';
        char checkr = '-';
        Boolean is_firstdom = new Boolean(false);
        //for(int i = 0; i < domainsizes.length(); i++) {
        Scanner sizescan = new Scanner(domainsizes);
        int i = 0;
        while(sizescan.hasNext()) {
            String consensuspredstring = consensuspred.toString();
            String charsize = sizescan.next();
            //int size = Character.getNumericValue(charsize);
            int size = Integer.parseInt(charsize);
            System.out.println("The sizes of the domain being checked is: " + size);
            //the value 12  below should be changed to alter the threshold domain size
            //should use the p-dist of CASP10 domains to get a value
            if(size < 12) {
                System.out.println("Size of domain " + domnumbers.charAt(i) + " is less than 12. Reassigning to neighbouring domains...");
                //assign to neighbouring domains
                //makes sure the interator for domain numbers and for domains sizes are at the same index in the string, so that the values match up
                domnumindex = i;
                for(int count = 0; count < domnumbers.length(); count++) {
                    if(count == domnumindex) {
                        domainnumber = domnumbers.charAt(count);
                    }
                }
                //get to the position in the domain prediction where the incorrect domain starts
                //start counters from this position
                for(int index = 0; index < consensuspredstring.length(); index++) {
                    if(consensuspredstring.charAt(index) == domainnumber) {
                        //System.out.println("domain " + domainnumber + " at position " + index);
                        checkbefore = index;
                        checkafter = index;
                        
                        //keep moving 1 before the index value, check if the domain number is different, then store the index of the closest different domain
                        //if the index was actually the first domain to appear, then skip this step and assign index as the domain found below
                        if(domnumbers.charAt(0) != consensuspredstring.charAt(index)) {
                            while(dombefore == '\u0000') {
                                checkbefore--;
                                //if the new found residue is different, assign the wrong one to this new one. Don't do it if the new residue is "-"
                                if(consensuspredstring.charAt(checkbefore) != consensuspredstring.charAt(index) && consensuspredstring.charAt(checkbefore) != checkr) {
                                    //System.out.println("found a diff domain");
                                    dombefore = consensuspredstring.charAt(checkbefore);
                                } else {
                                    //System.out.println("dom still the same");
                                }
                            }
                        } else {
                            System.out.println("Domain " + domnumbers.charAt(i) + " is the first domain in the sequence. Skipping checking before it..");
                            is_firstdom = true;
                        }

                        //move 1 after the index value to find the next residue thats different, then store its index
                        //if the index was actually the last domain to appear, skip this step and assign the index as the domain from the loop above
                        if(domnumbers.charAt(domnumbers.length()-1) != consensuspredstring.charAt(index)) {
                            while(domafter == '\u0000') {
                                checkafter++;
                                //if the new found residue is different, assign the wrong one to this new one. Don't do it if the new residue is "-"
                                if(consensuspredstring.charAt(checkafter) != consensuspredstring.charAt(index) && consensuspredstring.charAt(checkafter) != checkr) {
                                    //System.out.println("found a diff domain");
                                    domafter = consensuspredstring.charAt(checkafter);
                                } else {
                                    //System.out.println("dom still the same");
                                }
                            }
                            if(is_firstdom) {
                                temp = consensuspredstring.charAt(checkafter);
                                consensuspred.setCharAt(index, temp);
                                is_firstdom = false;
                                dombefore = '\u0000';
                                domafter = '\u0000';
                                continue;
                            }
                        } else {
                            System.out.println("Domain " + domnumbers.charAt(i) + " is the last domain in the sequence. Skipping checking after it..");
                            temp = consensuspredstring.charAt(checkbefore);
                            consensuspred.setCharAt(index, temp);
                            dombefore = '\u0000';
                            domafter = '\u0000';
                            continue;
                        }
                        
                        //check which of the checkbefore and checkafter are closer the the original index value
                        upperdiff = checkafter - index;
                        lowerdiff = index - checkbefore;
                        //reassign the domain at index to the closest different domain
                        if(upperdiff < lowerdiff) {
                            System.out.println("closest domain is after");
                            temp = consensuspredstring.charAt(checkafter);
                            consensuspred.setCharAt(index, temp);
                        } else if (upperdiff > lowerdiff) {
                            System.out.println("closest domain is before");
                            temp = consensuspredstring.charAt(checkbefore);
                            consensuspred.setCharAt(index, temp);
                        } else if (upperdiff == lowerdiff) {
                            //if they're the same distance, assign the lowest numbered domain
                            System.out.println("domains are equally close");
                            temp = consensuspredstring.charAt(checkafter);
                            afternum = Character.getNumericValue(temp);
                            temp = consensuspredstring.charAt(checkbefore);
                            beforenum = Character.getNumericValue(temp);
                            if(beforenum < afternum) {
                                consensuspred.setCharAt(index, temp);
                            } else if(beforenum > afternum) {
                                temp = consensuspredstring.charAt(checkafter);
                                consensuspred.setCharAt(index, temp);
                            } else if(beforenum == afternum) {
                                consensuspred.setCharAt(index, temp);
                            }
                        }

                        //blank the char holders - done so the while loops will work
                        dombefore = '\u0000';
                        domafter = '\u0000';
                    }
                }
            } else {
                System.out.println("The size of domain" + domnumbers.charAt(i) + " is greater than 12");
            }
            i++;
        }
        finalconsensuspred = consensuspred.toString();
        //System.out.println(finalconsensuspred);
    }

    //method allowing object to be called
    public String getResizedDomains() {
        return finalconsensuspred;
    }

    public static void main(String[] args) {
        //String consensus = "123111133222233444445555";
        //the two strings below should probably be stored as a hash, "numbers' being the key of domain numbers, and "nums" being the value of domain size for each domain
        //String nums = "55554";
        //String numbers = "12345";
        DomainResizer dr = new DomainResizer(args[0], args[1]);
        System.out.println( dr.getResizedDomains() );
    }
}
