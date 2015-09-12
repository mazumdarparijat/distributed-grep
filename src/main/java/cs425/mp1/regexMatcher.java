package cs425.mp1;

import java.io.*;
import java.util.regex.Pattern;

public class regexMatcher {
    private grepQuery pattern;
    private final PrintWriter out;
    private final BufferedReader logReader;
    private final String requestServerAddress;

    private regexMatcher(grepQuery pattern, String fname, PrintWriter output,String serverAddress) throws FileNotFoundException {
        this.pattern=pattern;
        out=output;
        logReader=new BufferedReader(new FileReader(fname));
        this.requestServerAddress=serverAddress;
    }

    static regexMatcher getNewInstance(grepQuery pattern, String fname, PrintWriter output, String sourceID) throws FileNotFoundException {
        return new regexMatcher(pattern,fname,output,sourceID);
    }

    public void printMatchingLines() throws IOException {
        String line;
//        System.out.println("[RegexMatcher] pattern : "+pattern);
        Pattern p;
        if (pattern.ignoreCase) {
            p = Pattern.compile(pattern.queryPattern, Pattern.CASE_INSENSITIVE);
        }
        else
            p=Pattern.compile(pattern.queryPattern);

        int counter=0;
        while ((line=logReader.readLine())!=null) {
            counter++;
//            System.out.print("[RegexMatcher] line : "+line+".... ");
            String printLine=(pattern.printLineNumbers) ? String.valueOf(counter)+":" : "";
            if (p.matcher(line).find()) {
//                System.out.println("Match!!");
                if (!pattern.invertMatch)
                    out.println(printLine+line);
            }
            else {
                if (pattern.invertMatch)
                    out.println(printLine+line);
//                System.out.println("No Match!!");
            }
        }
    }
}
