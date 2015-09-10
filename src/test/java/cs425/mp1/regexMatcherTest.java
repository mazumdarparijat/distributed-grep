package cs425.mp1;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class regexMatcherTest {
	
	@Test
	public void testPrintMatchingLines() {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter printer= new PrintWriter(out);
		regexMatcher matcher = new regexMatcher("this.*", printer);
		matcher.printMatchingLines();
		printer.flush();
		System.out.println("Printing out "+out.toString());
		assertEquals("this is a test file\nthis was created by Parijat\n",out.toString());
	}

}
