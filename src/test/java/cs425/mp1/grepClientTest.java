package cs425.mp1;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import cs425.mp1.clientMain.FormatCommandLineInputs;

public class grepClientTest {
	public class LineCompare implements Comparator<String>{
		@Override
		public int compare(String s1, String s2){
			String subs1 = s1.substring(0,s1.indexOf(':'));
			String subs2 = s2.substring(0,s1.indexOf(':'));
			return subs1.compareTo(subs2);
		}
		
	}
	@Test
	public void test() throws IOException {
		String [] args = {"-configFile", "/home/agupta80/mp1-distributed-logging/src/test/resources/config.properties", "-regex", "this"};
		FormatCommandLineInputs format = new FormatCommandLineInputs(args);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(out);
		grepClient.getNewInstance(format.configFileName,p).executeGrep(format.query);
		p.flush();
		String[] olines = out.toString().split("\n");
		Arrays.sort(olines, new LineCompare());
		Scanner sc = new Scanner(new File("/home/agupta80/mp1-distributed-logging/src/test/resources/sample.log"));
		List<String> lines = new ArrayList<String>();
		while (sc.hasNextLine()) {
		  lines.add(sc.nextLine());
		}
		String[] elines = lines.toArray(new String[0]);
		Arrays.sort(elines, new LineCompare());
		assertEquals(elines,olines);
	}
}
