package utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class LessTest {

	//Use small buffer size to test buffering
	int bufferSize=5;
	private File getFile(String file) {
		return new File(getClass().getClassLoader().getResource(file).getFile());
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void testNegativeLines() throws IOException{
		Less less = new Less(bufferSize);
		less.getLastLines(-1, getFile("3.lines.txt"), new PrintWriter(System.out));
	}
	
	@Test
	public void testEmptyFile() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Less less = new Less(bufferSize);
		less.getLastLines(1, getFile("empty.txt"), new PrintWriter(byteArrayOutputStream));
		assertEquals("", byteArrayOutputStream.toString("ascii"));
	}
	
	@Test
	public void testReadLines() throws IOException {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Less less = new Less(bufferSize);
		less.getLastLines(1, getFile("3.lines.txt"), new PrintWriter(byteArrayOutputStream));
		assertEquals("3 three thres", byteArrayOutputStream.toString("ascii"));
		
		byteArrayOutputStream = new ByteArrayOutputStream();
		less.getLastLines(2, getFile("3.lines.txt"), new PrintWriter(byteArrayOutputStream));
		assertEquals("2 two dos\r\n3 three thres", byteArrayOutputStream.toString("ascii"));
		
		byteArrayOutputStream = new ByteArrayOutputStream();
		less.getLastLines(3, getFile("3.lines.txt"), new PrintWriter(byteArrayOutputStream));
		assertEquals("1 one uno\r\n2 two dos\r\n3 three thres", byteArrayOutputStream.toString("ascii"));
		
		byteArrayOutputStream = new ByteArrayOutputStream();
		less.getLastLines(100, getFile("3.lines.txt"), new PrintWriter(byteArrayOutputStream));
		assertEquals("1 one uno\r\n2 two dos\r\n3 three thres", byteArrayOutputStream.toString("ascii"));
		
		
	}
	
	
	

}
