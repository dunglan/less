package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class Less {
	
	private int bufferSize = 1024;
	/**
	 * 
	 * @param bufferSize size of read/write buffer
	 */
	public Less(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	

	/**
	 * Retrieve last N lines of file.
	 * Use tiny amount of memory, runs fast, and can process huge files.
	 * @param n - last n lines to retrieve
	 * @param file - file to process
	 * @param printWriter - output last n lines into here
	 * @throws IOException
	 */
	public void getLastLines(int n, File file, PrintWriter printWriter) throws IOException {
		
		
		try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			if(n <= 0) {
				throw new IllegalArgumentException("n must be greater than zero");
			}
			byte[] buffer = new byte[bufferSize];
			
			if(raf.length() ==0) {
				return;
			}
			
			//End resultant text block is the byte before the last byte for end of file.
			long end = raf.length()-1;
			
			//Start of text block is found by reading backwards from the end until we count N+1 lines
			long start = end;
			long lineCount = 1;
			boolean foundStart=false;
		
			while(!foundStart) {
				
				//Hit the the beginning of file.
				if(start == 0) {
					foundStart = true;
				}
				else {
			
					//Move start backwards
		 			int len = bufferSize;
					start -=bufferSize;
					
					//If start is negative, we've hit the beginning of the file.
					if(start < 0) {
						
						//Buffer size to read is negative
						len =  bufferSize + Math.toIntExact(start) +-1; 
						start=0;	
					}
						
					raf.seek(start);
					raf.readFully(buffer, 0, len);
					
					for(int i=len-1;i>=0;i--) {
						//Found a new line, count it, and check if N lines counted
						if(buffer[i]=='\n') {
							if(lineCount++ >= n ) {
								start+=i+1;
								foundStart=true;
								break;
							}
						}
					}
				}
			}
			
			//Start reading the block from start to end, in buffer size chunks forward
			raf.seek(start);
			boolean doneWriting = false;
			
			while(!doneWriting) {
				int len = bufferSize;
				if(start+bufferSize >= end) {
					doneWriting=true;
					len = Math.toIntExact(end)-Math.toIntExact(start)+1;
				}
				raf.readFully(buffer, 0, len);
				for(int i=0;i<len;i++) {
					printWriter.write(buffer[i]);
				}
				printWriter.flush();
				start+=bufferSize;
			}		
		}
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.out.println("Get last n lines of a file. Usage <filename> <n>");
		}
		File file = new File(args[0]);
		int n = Integer.parseInt(args[1]);
		Less less = new Less(1024);
		
		less.getLastLines(n, file, new PrintWriter(System.out));
		
	}
	
	

}
