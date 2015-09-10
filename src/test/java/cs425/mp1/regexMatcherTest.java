package cs425.mp1;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class regexMatcherTest {
	
	@Test
	public void testPrintMatchingLines() {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter printer= new PrintWriter(out);
		regexMatcher matcher = null;
		try {
			grepQuery gr = new grepQuery("this",false,false,false);
			matcher = regexMatcher.getNewInstance(gr,"./",printer,"test");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			matcher.printMatchingLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
		printer.flush();
		System.out.println("Printing out "+out.toString());
		assertEquals("this is a test file\nthis was created by Parijat\n",out.toString());
	}

}
