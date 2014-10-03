import java.io.*;
import java.util.*;

/*
 *
 *  Cuts the full native structure into the domains specified by PDP
 *
 */

public class NativeCutter {

    public NativeCutter (String pdpSequence, String target) {
        try {
            int pdpResNum = 0;
            for(int i = 0; i < pdpSequence.length(); i++) {
                char c = pdpSequence.charAt(i);
                String residueDom = Character.toString(c);
                pdpResNum++;
                Scanner input = new Scanner(new File("/home/matt/project/data/CASP10_full_structures_official/" + target + ".pdb"));
                while(input.hasNext()) {
                    String resLine = input.nextLine();
                    String residue;
                    int residueInt;
                    if(resLine.startsWith("ATOM")) {
                        residue = resLine.substring(21, 26);
                        residue = residue.trim();
                        residueInt = Integer.parseInt(residue);
                        if(residueInt == pdpResNum) {
                            File output = new File("/home/matt/project/data/CASP10_unofficial_split_structures/" + target + "-D" + residueDom + ".pdb");
                            BufferedWriter writeSplitChain = new BufferedWriter(new FileWriter(output, true));
                            writeSplitChain.write(resLine + "\n");
                            writeSplitChain.close();
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
       NativeCutter nc = new NativeCutter(args[0], args[1]);
    }
}
