package cs425.mp1;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.cli.ParseException;

public class regexMatcher {
	private grepQuery pattern;
	private final PrintWriter out;
	private final File logFile;
	private final String requestServerAddress;

	private regexMatcher(grepQuery pattern, String fname, PrintWriter output, String serverAddress)
			throws FileNotFoundException {
		this.pattern = pattern;
		out = output;
		logFile = new File(fname);
		this.requestServerAddress = serverAddress;
	}

	static regexMatcher getNewInstance(grepQuery pattern, String fname, PrintWriter output, String sourceID)
			throws FileNotFoundException {
		return new regexMatcher(pattern, fname, output, sourceID);
	}

	public void printMatchingLines() {
		String line;
		// System.out.println("[RegexMatcher] pattern : "+pattern);
		Pattern p;
		if (pattern.ignoreCase) {
			p = Pattern.compile(pattern.queryPattern, Pattern.CASE_INSENSITIVE);
		} else
			p = Pattern.compile(pattern.queryPattern);

		int counter = 0;
		try{
			Scanner scanner = new Scanner(logFile);
			scanner.useDelimiter("\n");
			while (scanner.hasNext()) {
			    line = scanner.next();
			    counter++;
				// System.out.print("[RegexMatcher] line : "+line+".... ");
				String printLine = (pattern.printLineNumbers) ? String.valueOf(counter) + ":" : "";
				if (p.matcher(line).find()) {
					// System.out.println("Match!!");
					if (!pattern.invertMatch)
						out.println(printLine + line);
				} else {
					if (pattern.invertMatch)
						out.println(printLine + line);
					// System.out.println("No Match!!");
				}
			}
		}catch (IOException e) {
            e.printStackTrace();
        }
	}
}
