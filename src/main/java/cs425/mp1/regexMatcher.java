package cs425.mp1;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class for matching regex with lines of a log file.
 * It takes query pattern and log file as input. It
 * also takes an output stream as input in which it
 * puts the matching lines. This class supports all
 * grep matching control options.
 */
public class regexMatcher {
	private grepQuery pattern;
	private final PrintWriter out;
	private final String logFile;

    /** Constructor
     * @param pattern pattern string with options
     * @param fname log filename
     * @param output output stream in which matching lines are put
     * @throws FileNotFoundException
     */
	private regexMatcher(grepQuery pattern, String fname, PrintWriter output)
			throws FileNotFoundException {
		this.pattern = pattern;
		out = output;
		logFile = fname;
	}

    /** Get new instance
     * @param pattern pattern string with options
     * @param fname log filename
     * @param output output stream to put matching lines
     * @return object of this class
     * @throws FileNotFoundException
     */
	static regexMatcher getNewInstance(grepQuery pattern, String fname, PrintWriter output)
			throws FileNotFoundException {
		return new regexMatcher(pattern, fname, output);
	}

    /** Matches regex with lines and puts matching lines in outputstream
     * @throws IOException
     */
	public void printMatchingLines() throws IOException {
		String line;
		// System.out.println("[RegexMatcher] pattern : "+pattern);
		Pattern p;
		if (pattern.ignoreCase) {
			p = Pattern.compile(pattern.queryPattern, Pattern.CASE_INSENSITIVE);
		} else
			p = Pattern.compile(pattern.queryPattern);

		int counter = 0;
		Scanner scanner = new Scanner(new FileReader(logFile));
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
	}
}
