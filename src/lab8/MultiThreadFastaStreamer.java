package lab8;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MultiThreadFastaStreamer
{
	private int finished = 0;
	private BufferedReader reader;
	private String header;
	private String nextheader = "Arandomstringthatcannotbetheheaderever";
	public MultiThreadFastaStreamer(String filepath) throws Exception
	{
		this.reader = new BufferedReader(new FileReader(new File(filepath)));
	}
	

	public FastaSequence read() throws Exception
	{
		if (finished == 1)
		{
			String sequence = null;
			FastaSequence fa = new FastaSequence(sequence, this.header);
			return fa;
		}
		String sequence = new String();
		StringBuffer seq = new StringBuffer();
		String nextLine = new String();
		nextLine = this.reader.readLine(); // first line is header
		if (nextLine.startsWith(">"))
		{
			this.header = nextLine.substring(1,nextLine.length());
			nextLine = this.reader.readLine();
		}
		else
		{
			this.header = this.nextheader;
		}// start from second line
		while( nextLine != null & !nextLine.startsWith(">"))
		{
			seq.append(nextLine);
			nextLine = this.reader.readLine();// start from second line
			if (nextLine == null)
			{
				sequence = seq.toString();;
				FastaSequence fa = new FastaSequence(sequence, this.header);
				finished=1;
				return fa;
			}
			if (nextLine.startsWith(">"))
			{
				this.nextheader = nextLine.substring(1,nextLine.length());//this is the new header for the next sequence
			}
		}
		sequence = seq.toString();
		FastaSequence fa = new FastaSequence(sequence, this.header);
		return fa;
	}
	

	//Main method
	public static void main(String[] args) throws Exception, FileNotFoundException, IOException 
	{
	    MultiThreadFastaStreamer faReader = new MultiThreadFastaStreamer("/Users/suyangqi/git/java_labs/ERCC92.fa");
	    Integer i = 1;
		FastaSequence fa0 = faReader.read();
		int[] ct = new int[5];
		ct[0] = 0; ct[1] = 0; ct[2] = 0; ct[3] = 0; ct[4] = 0;
		while(fa0.getSequence() != null)
		{	
			System.out.println(fa0.getHeader());
			System.out.println(fa0.getSequence());
			fa0.countAlphabets(ct);
			fa0 = faReader.read();
			i++;
		}
		System.out.println(ct[0]);
	}
}
