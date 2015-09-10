package cs425.mp1;

import java.io.*;
public class regexMatcher {
    private String pattern;
    private PrintWriter out;
    private BufferedReader logReader;

    public regexMatcher(String pattern, String fname, PrintWriter output) throws FileNotFoundException {
        this.pattern=pattern;
        out=output;
        logReader=new BufferedReader(new FileReader(fname));

    }

    public void printMatchingLines() throws IOException {
        String line;
        System.out.println("[RegexMatcher] pattern : "+pattern);
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
    }
}
