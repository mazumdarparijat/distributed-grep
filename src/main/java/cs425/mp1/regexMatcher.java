package cs425.mp1;

import java.io.*;
public class regexMatcher {
    private String pattern;
    private PrintWriter out;
    private BufferedReader logReader;
    private static final String fname="/Users/parijatmazumdar/Desktop/CS425Project/DistributedGrep/src/test/test.log";

    public regexMatcher(String pattern, PrintWriter output) {
        this.pattern=pattern;
        out=output;
        try {
            logReader=new BufferedReader(new FileReader(fname));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printMatchingLines() {
        String line;
        System.out.println("[RegexMatcher] pattern : "+pattern);
        try {
            while ((line=logReader.readLine())!=null) {
                System.out.print("[RegexMatcher] line : "+line+".... ");
                if (line.matches(pattern)) {
                    System.out.println("Match!!");
                    out.println(line);
                }
                else {
                    System.out.println("No Match!!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
