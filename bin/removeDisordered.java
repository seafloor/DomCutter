import java.io.*;
import java.util.*;

public class removeDisordered {
    String wholeFile;
    File[] listOfFiles;
    public removeDisordered (String target, String targetFolder, String boundaryFile) {
        //Scanner wholescan = new Scanner(boundaryFile).useDelimiter("\\Z");
        //wholeFile = wholescan.next();
        File targetDir = new File(targetFolder);
        listOfFiles = targetDir.listFiles();
        Arrays.sort(listOfFiles);
    }

    //change this to a method
    public void moveDomain (int lowerBound, int upperBound, int domainNo, String targetFolder, String target) {
        int structResNum = 0;
        String structRes, structResClean, atom, test, domain;
        File tmp = new File(targetFolder + "toRemove.tmp");
        for (int counter = 0; counter < listOfFiles.length; counter++) {
            if (listOfFiles[counter].isFile()) {
                String fileName = listOfFiles[counter].getName();
                System.out.println(fileName);
                try {
                    Scanner scanner = new Scanner (new File(targetFolder + fileName));
                    while (scanner.hasNext()) {
                        atom = scanner.nextLine();
                        if (atom.startsWith("ATOM")) {
                            //System.out.println(atom);
                            structRes = atom.substring(21, 26);
                            structResClean = structRes.trim();
                            structResNum = Integer.parseInt(structResClean);
                            // could write to a string buffer instead of a txt file to save time?
                            if (structResNum >= lowerBound && structResNum <= upperBound) { //call with the 242 value
                                try {
                                    BufferedWriter writetmp = new BufferedWriter (new FileWriter (tmp, true));
                                    writetmp.write(atom + "\n");
                                    writetmp.close();
                                } catch (IOException writeE) {
                                    writeE.printStackTrace();
                                }
                            }
                        }
                    }
                    scanner.close();
                } catch (IOException openE) {
                    openE.printStackTrace();
                }
                // open a writing bufffer
                // write each scanStruct line to the buffer only if it is not equal to any of the lines in the disordered file
                // write the string buffer over the original file
                StringBuffer buffer = new StringBuffer();
                try {
                    Scanner scanStruct = new Scanner (new File (targetFolder + fileName));
                    int i = 1;
                    while (scanStruct.hasNext()) {
                        test = scanStruct.nextLine();
                        Scanner scanTmp = new Scanner (new File (targetFolder + "toRemove.tmp"));
                        // could remove the while loop and make it a if x.contains(y)
                        while (scanTmp.hasNext()) {
                            domain = scanTmp.nextLine();
                            if (test.equals(domain)) {
                                if (i==1) {
                                    buffer.append("PARENT N/A" + "\n");
                                } else {
                                    buffer.append(test + "\n");
                                }
                                i++;
                            }
                        }
                        scanTmp.close();
                    }
                    scanStruct.close();
                    buffer.append("TER");
                    String domainNum = Integer.toString(domainNo);
                    File output = new File(targetFolder + target + "_" + domainNum);
                    if(!output.exists()) {
                        output.mkdir();
                    }
                    BufferedWriter writeNew = new BufferedWriter (new FileWriter (new File (output + "/" + fileName)));
                    writeNew.write(buffer.toString());
                    writeNew.flush();
                    writeNew.close();
                } catch (IOException scanE) {
                    scanE.printStackTrace();
                }
                if (tmp.delete()) {
                    System.out.println("success: tmp file deleted");
                } else {
                    System.out.println("error: tmp file not deleted - the model may not contain the domain in question");
                }
            }
        }
    }

    public static void main (String[] args) {
        int lowerBound = 0;
        int upperBound = 0;
        removeDisordered rd = new removeDisordered (args[0], args[1], args[2]);
        int lineNo = 1; // is the domain number - assumes domain sizes were given in order of domains
        String line;
        try {
            Scanner lineScan = new Scanner(new File(args[2]));
            while(lineScan.hasNextLine()) {
                line = lineScan.nextLine(); // takes separate lines
                //read the range from this line
                Scanner boundScan = new Scanner(line).useDelimiter("-"); //take the values separated by a "-"
                if (boundScan.hasNext()) {
                    lowerBound = boundScan.nextInt(); // takes the first value - the lower domain boundary
                    upperBound = boundScan.nextInt(); // takes the second value - the upper domain boundary
                    System.out.println("lower bound = " + lowerBound);
                    System.out.println("upper bound = " + upperBound);
                }

                //call the method for this line
                rd.moveDomain(lowerBound, upperBound, lineNo, args[1], args[0]);
                lineNo++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error in reading comman-separated boundary domains");
        }
    }
}
