//import java.util.regex.Matcher;
import java.lang.Object;
//import java.util.regex.Pattern;
import java.util.regex.*;
import java.util.*;
import java.io.*;

public class FastaCutter {
    //takes in full path of the fasta file and the consensus pdp file
    //iterates through the lines of the fasta file, skipping the header (starts with ">")
    String trimmed;
    String header = "";
    String newheader = "";
    ArrayList<Integer> domainsizes = new ArrayList();
    public FastaCutter(String model, String fastafile, String domfile, String doms) {
        String numcheck = "";
        Scanner scanner = new Scanner(fastafile);
        StringBuffer buffer = new StringBuffer();
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            if(!line.startsWith(">")) {
                buffer.append(line);
                //System.out.println("Line in fasta file :" + line);
            } else {
                header = line;
            }
        }


        //System.out.println("The original header was: "+ header);
        //output is the trimmed fasta file without the header
        trimmed = buffer.toString();
        //System.out.print(trimmed);

        //check the pdp file and the fasta file are the same length
        int trimlen = trimmed.length();
        int domlen = domfile.length();
        StringBuffer fastabuffer = new StringBuffer();
        StringBuffer discon_buff = new StringBuffer();
        String updatedbuff = "";
        String outputbuf;
        int currentnum = 0;
        if(trimlen == domlen) {
            System.out.println("Fasta file and pdp file are the same length");
            //System.out.println("The pdp file read in for fasta splitting is: " + domfile);
            //System.out.println("The fasta file read in for fasta splitting is: " + trimmed);
            //iterates through the trimmed fasta file, checking each residue against the equivalent domain number in the pdp file
            char prev = domfile.charAt(0);
            //System.out.println("prev: " + prev);
            int sizecount = 0;
            for(int i = 0; i <= trimlen; i++) {
                //System.out.println("loop count " + i);
                if(i == trimlen) {
                    outputbuf = fastabuffer.toString();
                    //System.out.println("We made it inside the output section!");
                    File output = new File("/home/matt/project/data/fasta_doms/" + model + "_" + prev + ".fasta"); 
                    try {
                        Pattern p = Pattern.compile("\\s+(\\d+)\\s+residues");
                        if(output.isFile()) {
                            Scanner wholein = new Scanner(output);
                            while(wholein.hasNextLine()) {
                                discon_buff.append(wholein.nextLine());
                            }
                            updatedbuff = discon_buff.toString();
                            //System.out.println(updatedbuff);
                            Scanner numscan = new Scanner(updatedbuff);
                            while(numscan.hasNextLine()) {
                                numcheck = numscan.nextLine();
                                Matcher m = p.matcher(numcheck);
                                while(m.find()) {
                                    currentnum = Integer.parseInt(m.group(1));
                                    //System.out.println("There were " + currentnum + " residues in the original");
                                }
                            }
                            //System.out.println(sizecount + " residues are added");
                            sizecount += currentnum;
                            //System.out.println("Total number of residues = " + sizecount);
                            updatedbuff = updatedbuff.replaceAll("\\s+\\d+\\s+residues", " " + sizecount + " residues" + "\n");
                            //newheader = header.replaceAll("\\s+[0-9]+\\s+residues", " " + sizecount + " residues");
                            updatedbuff = updatedbuff + outputbuf;
                            //System.out.println("The updated fasta file: " + updatedbuff);
                            BufferedWriter writedata = new BufferedWriter(new FileWriter(output));
                            writedata.write(updatedbuff);
                            writedata.close();
                            discon_buff.delete(0, discon_buff.length());
                        } else { 
                            BufferedWriter writedata = new BufferedWriter(new FileWriter(output));
                            newheader = header.replaceAll("\\s+[0-9]+\\s+residues", " " + sizecount + " residues");
                            //domainsizes.add(sizecount);
                            //newheader = header.replace("residues", "blah");
                            //System.out.println(newheader);
                            writedata.write(newheader + "\n" + outputbuf);
                            writedata.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error in fastacutter when checking file lengths");
                    }
                    break;
                }
                char res = trimmed.charAt(i);
                //System.out.print(res);
                char dom = domfile.charAt(i);
                //output to a buffer, which we will print to file when full
                if(dom == prev) {
                    fastabuffer.append(res);
                    sizecount++;
                } else {
                    //System.out.println("There are " + sizecount + " residues in domain " + prev);
                    outputbuf = fastabuffer.toString();
                    //System.out.println("We made it inside the output section!");
                    fastabuffer.delete(0, fastabuffer.length());
                    fastabuffer.append(res);
                    File output = new File("/home/matt/project/data/fasta_doms/" + model + "_" + prev + ".fasta"); 
                    try {
                        Pattern p = Pattern.compile("\\s+(\\d+)\\s+residues");
                        if(output.isFile()) {
                            Scanner wholein = new Scanner(output);
                            while(wholein.hasNextLine()) {
                                discon_buff.append(wholein.nextLine());
                            }
                            updatedbuff = discon_buff.toString();
                            //System.out.println(updatedbuff);
                            Scanner numscan = new Scanner(updatedbuff);
                            while(numscan.hasNextLine()) {
                                numcheck = numscan.nextLine();
                                Matcher m = p.matcher(numcheck);
                                while(m.find()) {
                                    currentnum = Integer.parseInt(m.group(1));
                                    //System.out.println("There were " + currentnum + " residues in the original");
                                }
                            }
                            //System.out.println(sizecount + " residues are added");
                            sizecount += currentnum;
                            //System.out.println("Total number of residues = " + sizecount);
                            updatedbuff = updatedbuff.replaceAll("\\s+\\d+\\s+residues", " " + sizecount + " residues" + "\n");
                            //newheader = header.replaceAll("\\s+[0-9]+\\s+residues", " " + sizecount + " residues");
                            updatedbuff = updatedbuff + outputbuf;
                            //System.out.println("The updated fasta file: " + updatedbuff);
                            BufferedWriter writedata = new BufferedWriter(new FileWriter(output));
                            writedata.write(updatedbuff);
                            writedata.close();
                            discon_buff.delete(0, discon_buff.length());
                        } else {
                            //System.out.println("Inside the try statement!");
                            BufferedWriter writedata = new BufferedWriter(new FileWriter(output));
                            newheader = header.replaceAll("\\s+[0-9]+\\s+residues", " " + sizecount + " residues");
                            //domainsizes.add(sizecount);
                            //System.out.println(newheader);
                            writedata.write(newheader + "\n" + outputbuf);
                            writedata.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sizecount = 1;
                }
                prev = dom;
            }
        } else {
            System.out.println("Error: fasta file and pdp file are not the same length");
            System.out.println("Fasta file length: " + trimlen);
            //NEW edited line below
            System.out.println("pdp file length: " + domlen);
        }
       
        System.out.println("The domains in the string in fastacutter: " + doms);
        for(int index = 0; index < doms.length(); index ++) {
            try {
                Scanner scansplit = new Scanner (new File("/home/matt/project/data/fasta_doms/" + model + "_" + doms.charAt(index) + ".fasta"));
                StringBuffer strbfr = new StringBuffer();
                while(scansplit.hasNext()) {
                    String lin = scansplit.nextLine();
                    if(!lin.startsWith(">")) {
                        strbfr.append(lin);
                        //System.out.println("Line in fasta file :" + line);
                    }
                }
                String trim = strbfr.toString();
                System.out.println(trim);
                System.out.println("length is " + trim.length());
                domainsizes.add(trim.length());
            } catch (IOException ioer) {
                ioer.printStackTrace();
            }
        }


        //System.out.println("The original header was: "+ header);
        //output is the trimmed fasta file without the header
        
    }

    //return the domain sizes in an ArrayList (in order of the domains) - used by DomCutter to check the ATOM files contain the right number of residues
    public ArrayList getDomainSizes() {
        return domainsizes;
    }

    //returns the fasta file with the header removed
    public String getFastaFile() {
        return trimmed;
    }

    public static void main(String[] args) {
        //String fastafile = ">T0690 EL13177C, Faecalibacterium prausnitzii A2-165, 393 residues\nASDSPMAYTDGSYQFILNADNTATITKYTGNEHRITIPAQVTHGAYIYPVSKIGDRVFCN\nYKYVLTSVQIPDTVTEIGSNAFYNCTSLKRVTIQDNKPSCVKKIGRQAFMFCSELTDIPI\nLDSVTEIDSEAFHHCEELDTVTIPEGVTSVADGMFSYCYSLHTVTLPDSVTAIEERAFTG\nTALTQIHIPAKVTRIGTNAFSECFALSTITSDSESYPAIDNVLYEKSANGDYALIRYPSQ\nREDPAFKIPNGVARIETHAFDSCAYLASVKMPDSVVSIGTGAFMNCPALQDIEFSSRITE\nLPESVFAGCISLKSIDIPEGITQILDDAFAGCEQLERIAIPSSVTKIPESAFSNCTALNN\nIEYSGSRSQWNAISTDSGLQNLPVAPGSIDVTV";
        //String domfile = "111112222233333";
        //String model = "T0651";
        FastaCutter fc = new FastaCutter(args[0], args[1], args[2], args[3]);
        System.out.println( fc.getFastaFile() );
        System.out.println( fc.getDomainSizes() );
    }
}

