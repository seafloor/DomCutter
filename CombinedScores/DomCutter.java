import java.io.*;
import java.util.*;

public class DomCutter {

    public DomCutter(String targetmodels, String target, String QAfile)
    {
        try
        {
            //call class to read QA file and top 10 model names and store in Vector or linkedHashSet 
            System.out.println("Reading in top QA models");
            TopReader tr = new TopReader(QAfile);
            LinkedHashSet<String> topservers = new LinkedHashSet(tr.getServerNames());

            //call runPDP in a loop to divide up models
            System.out.println("Generating a consensus domain prediction");
            Iterator itr = topservers.iterator();
            String dir = "/home/matt/project/data/CASP10_server_models/" + target;
            StringBuffer buffer = new StringBuffer ();
            String dompred = "";
            int length_check = 0;
            String sequence = "";
            while(itr.hasNext()) {
                //String QAserver = itr.next()
                runPDP rpdp = new runPDP(dir + "/" + itr.next(), dir + ".fasta");
                dompred = rpdp.getDomainPrediction(); 
                sequence = rpdp.getSequence();
                dompred = dompred.replaceAll("\\n", "");
                if(length_check == 0) {
                    length_check = dompred.length();
                }
                buffer.append(dompred + "\n");
                System.out.print("pdp prediction:" + "\n" + dompred + "\n");
            }

            String all = buffer.toString();
            //System.out.print(all);
            //System.out.println("end of buffer");

            //call a class to perform consensus (majority vote) on domain prediction
            //call this on runpdp_array
            consensus con = new consensus(all, length_check);
            String consensus_pdp = con.getConsensus();
            System.out.println("consensus prediction");
            System.out.println(consensus_pdp);

            //find the number of domains in the file - used in different bits of code later
            NumOfDoms nod = new NumOfDoms(consensus_pdp);
            String strdomain_nums = nod.getNumberOfDomains();
            System.out.println("Initial domain numbers: " + strdomain_nums);

            //call a class to find domains smaller than 12 residues-long and assign them to neighbouring domains
            //the 12 residue threshold may need to be changed
            DomainResizer dr = new DomainResizer(consensus_pdp, strdomain_nums);
            String resized_doms = dr.getResizedDomains();
            System.out.println("The new consensus pred:" + "\n" + resized_doms);
            consensus_pdp = resized_doms;

            //find the new domain numbers after domain reassignment
            NumOfDoms don = new NumOfDoms(consensus_pdp);
            strdomain_nums = don.getNumberOfDomains();
            LinkedHashSet<String> domain_nums = new LinkedHashSet(don.getDomainsAsHash());
            System.out.println("New domain numbers: " + strdomain_nums);

            //split the native structure file into the PDP domains (used later in analysis)
            NativeCutter nc = new NativeCutter(consensus_pdp, target);

            //divide model file
            //for each file in the directory, iterate through the pdp file and put the PDB lines into the correct domain file
            //StringTokenizer dompredtoks = new StringTokenizer( dompred, "\n" );
            System.out.println("Splitting models into domains... ");
            File targetdir = new File(targetmodels);
            String files;
            File[] listOfFiles = targetdir.listFiles(); 
            Arrays.sort(listOfFiles);
            String residue_dom, atom_coordinate, struct_res, struct_res_clean, modelname;
            int struct_res_num = 0;
            //for(File modelpred: targetdir.listfiles()) {
            for (int iterate = 0; iterate < listOfFiles.length; iterate++) { 
                if (listOfFiles[iterate].isFile()) { 
                    modelname = listOfFiles[iterate].getName();
                    //print out which model is currently being split - make sure all dirs are done!
                    System.out.println("Splitting model " + modelname);
                    int pdp_line_num = 0;
                    for(int i = 0; i < consensus_pdp.length(); i++) {
                        char c = consensus_pdp.charAt(i);
                        residue_dom = Character.toString(c);
                        pdp_line_num++;
                        //read in an ATOM file from the target directory line-by-line
                        BufferedReader StructPredFile = new BufferedReader(new FileReader(targetmodels + modelname));
                        while ((atom_coordinate = StructPredFile.readLine()) != null) {
                            if (atom_coordinate.startsWith("ATOM")) {
                                //trim the substr so it just contains the residue number
                                struct_res = atom_coordinate.substring(21, 26);
                                struct_res_clean = struct_res.trim();
                                struct_res_num = Integer.parseInt(struct_res_clean);
                                //if the residue number of the ATOM file is the same as the residue position in the pdp file, write the ATOM coordinates to a new file for the domain number
                                if (struct_res_num == pdp_line_num) {
                                    //creates new directory for each target and then a different subdirectory for each domain, with each directory containing the models for that domain
                                    //new plan: move the mkdirs out of the loop - if the directory already exists then overwrite, otherwise create it. Use domain numbers from the linked hash set to create the dirs
                                    new File("/home/matt/project/data/split_doms/" + target + "/").mkdirs();
                                    new File("/home/matt/project/data/split_doms/" + target + "/" + "dom_" + residue_dom + "/").mkdirs();
                                    File output = new File("/home/matt/project/data/split_doms/" + target + "/" + "dom_" + residue_dom + "/" + modelname);
                                    BufferedWriter writedata = new BufferedWriter(new FileWriter(output, true));
                                    writedata.write(atom_coordinate + "\n");
                                    writedata.close();
                                }
                            }
                        }
                    }
                //new code
                } else {
                    System.out.println("Error: file " + listOfFiles[iterate] + " is not a file");
                }
            }
            System.out.println("done.");

            //call class to split the fasta file into different sections
            int targetSize = 0;
            ArrayList<Integer> domsizes = new ArrayList();
            System.out.println("Splitting the fasta file... ");
            File fastainput = new File(dir + ".fasta");
            String fastafile = "";
            try {
                Scanner wholescan = new Scanner(fastainput).useDelimiter("\\Z");
                fastafile = wholescan.next();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if(!fastafile.isEmpty()) {
                FastaCutter fc = new FastaCutter(target, fastafile, consensus_pdp, strdomain_nums);
                System.out.println("Success: split the fasta file");
                fastafile = fc.getFastaFile();
                domsizes = fc.getDomainSizes();
                targetSize = fc.getFastaLength();
                if(fastafile.equals(sequence)) {
                    //System.out.println("Fasta files are the same!");
                } else {
                    System.out.println("Error: Fasta files are not equal");
                }
            } else {
                System.out.println("Error: fasta file was not read in");
            }
            
            //check that all output files are the right length
            System.out.println("Cleaning up output...");
            Iterator<String> iterator = domain_nums.iterator();
            int linecheck = 0;
            String domcheck = "";
            String modelcheck = "";
            String resnum;
            int current = 0;
            int previous;
            int numint = 0;

            //csv file for output

            ArrayList<String> incomplete_doms = new ArrayList();
            //iterates through the domain numbers for this model, creating a list of all the files in the directory for that domain
            while(iterator.hasNext()) {
                //System.out.println("iterator loop number " + linecheck);
                domcheck = iterator.next();
                //System.out.println(domcheck);
                File dir_to_check = new File("/home/matt/project/data/split_doms/" + target + "/" + "dom_" + domcheck + "/");
                File[] filelist = dir_to_check.listFiles(); 
                Arrays.sort(filelist);
                //iterate through the files in fileslist
                for(int counter = 0; counter < filelist.length; counter++) {
                    if (filelist[counter].isFile()) { 
                        //read in each file, check the number of residues in it, then check if it's the right number by comparing to the split fasta files
                        modelcheck = filelist[counter].getName();
                        //System.out.println("Checking " + modelcheck);
                        String filepath = "/home/matt/project/data/split_doms/" + target + "/" + "dom_" + domcheck + "/" + modelcheck;
                        //System.out.println(filepath);
                        File checker = new File(filepath);
                        Scanner lines = new Scanner(checker);
                        //System.out.println(lines.next());
                        previous = 0; //will hold the previous residue number = to check if now on a new residue
                        int residue_count = 0; //counts the number of residues
                        while(lines.hasNextLine()) {
                            //System.out.println("Inside the while loop");
                            String number;
                            resnum = lines.nextLine();
                            //System.out.println(resnum);
                            if (resnum.startsWith("ATOM")) {
                                //trim the substr so it just contains the residue number
                                number = resnum.substring(21, 26);
                                number = number.trim();
                                numint = Integer.parseInt(number);
                                current = numint;
                            }
                            //increases the number of residues by one only if we're not on the first residue
                            if(current != previous) {
                                residue_count++;
                            }
                            previous = current;
                        }
                        int finalsize = domsizes.get(linecheck);
                        
                        //output target name, model name, domain number and domain size to a csv file
                        File csvout = new File("/home/matt/project/data/rawData/round1output/domainlengths.csv");
                        BufferedWriter writecsv = new BufferedWriter(new FileWriter(csvout, true));
                        writecsv.write(target + "," + modelcheck + "," + domcheck + "," + finalsize + "," + targetSize + "\n");
                        writecsv.close();
                        //System.out.println(target + "," + modelcheck + "," + domcheck + "," + finalsize + "\n");
                        System.out.println("Number of residues in the ATOM file: " + residue_count);
                        System.out.println("Number of residues in the fasta file (correct number): " + finalsize);

                        //checks if the number of residues in the ATOM file is equal to the number of residues in the fasta file
                        if(residue_count == finalsize) {
                            System.out.println(modelcheck + ": file length is okay");
                        } else {
                            //deletes the ATOM file if it isn't a full domain
                            //temporarily commented-out because domains still need a score, even if it is zero - now handled by updated ModFOLDclust2
                            //System.out.println(modelcheck + ": file length error. Incomplete domain will be discarded...");
                            incomplete_doms.add(modelcheck + "\n");
                            //if(checker.delete()) {
                            //    System.out.println("Success: file deleted");
                            //} else {
                            //    System.out.println("File permissions error - cannot delete file. May give an error with ModFOLDclust");
                            //}
                        }
                    }
                }
            linecheck++; //keeps track of what domain size we want to retrieve in the domsizes ArrayList
            }
            System.out.println("The following domains were incomplete");
            System.out.println(incomplete_doms);
            
        } catch (IOException e) {
            //System.err.println( e );
        }
    }

    public static void main(String[] args){
    
        DomCutter dc = new DomCutter( args[0], args[1], args[2] );
        
    }
}

