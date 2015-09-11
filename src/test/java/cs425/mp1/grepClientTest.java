package cs425.mp1;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import cs425.mp1.clientMain.FormatCommandLineInputs;

public class grepClientTest {

	@Test
	public void test() throws IOException {
		String [] args = {"-configFile", "/Users/agupta/Documents/cs425/mp1-distributed-logging/src/main/resources/config.properties", "-regex", "this"};
		FormatCommandLineInputs format = new FormatCommandLineInputs(args);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(out);
		grepClient.getNewInstance(format.configFileName,p).executeGrep(format.query);
		p.flush();
		String[] olines = out.toString().split("\n");
		Arrays.sort(olines);
		Scanner sc = new Scanner(new File("/Users/agupta/Documents/cs425/mp1-distributed-logging/src/main/resources/sample.log"));
		List<String> lines = new ArrayList<String>();
		while (sc.hasNextLine()) {
		  lines.add(sc.nextLine());
		}
		String[] elines = lines.toArray(new String[0]);
		Arrays.sort(elines);
		assertEquals(elines,olines);
	}
}
