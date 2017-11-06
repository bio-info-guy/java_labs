package lab8;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MultiThreadFastaStreamer
{
	private BufferedReader reader;
	private String header;
	private String nextheader = "Arandomstringthatcannotbetheheaderever";
	public MultiThreadFastaStreamer(String filepath) throws Exception
	{
		this.reader = new BufferedReader(new FileReader(new File(filepath)));
	}
	

	public FastaSequence read() throws Exception
	{
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
			if(this.header == null)
			{
				sequence = null;
				FastaSequence fa = new FastaSequence(sequence, this.header);
				return fa;
			}
		}// start from second line
		while( nextLine != null & !nextLine.startsWith(">"))
		{
			seq.append(nextLine);
			nextLine = this.reader.readLine();// start from second line
			if (nextLine == null)
			{
				sequence = null;
				FastaSequence fa = new FastaSequence(sequence, this.header);
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
	    MultiThreadFastaStreamer se = new MultiThreadFastaStreamer("/Users/suyangqi/git/java_labs/ERCC92.fa");
	    FastaSequence fa = se.read();
	    System.out.println(fa.getHeader());
	    System.out.println(fa.getSequence());
	    System.out.println(fa.getGCRatio());
	    fa=se.read();
	    System.out.println(fa.getHeader());
	    System.out.println(fa.getSequence());
	    System.out.println(fa.getGCRatio());

	}
}
