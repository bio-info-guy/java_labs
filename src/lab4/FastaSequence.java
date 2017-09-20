package lab4;

import java.io.*; 
import java.util.ArrayList;
import java.util.List;
public class FastaSequence
{
	private BufferedReader reader = null;
	private String sequence;
	private String header;
	private String nextheader;
	private boolean initiate = Boolean.TRUE;
	public FastaSequence(String filepath)
	{
		ReadOneSequence(filepath);
		this.initiate = Boolean.FALSE;		
	}
        public FastaSequence(FastaSequence fa)//allows for assigning one fastasequence with the values of another
	{
		this.sequence = fa.sequence;
		this.header = fa.header;
		this.reader = fa.reader;
		this.nextheader = fa.nextheader;
		this.initiate = fa.initiate;
	}
	public String getHeader()
	{
		return this.header;
	}
	public String getSequence()
	{
		return this.sequence;
	}
	public float getGCRatio()
	{
		int ct = 0;
		for (int i = 0; i < this.sequence.length(); i++)
		{
			char nt = this.sequence.charAt(i);
		    if(String.valueOf(nt).equals("G")|String.valueOf(nt).equals("C"))
		    {
		    		ct++;
		    }
		}
		System.out.println("The GC content of the current sequence is: ");
		return (float)ct/this.sequence.length();
	}
	public void ReadOneSequence(String filepath)
	{
		try {
			this.reader = new BufferedReader(new FileReader(new File(filepath)));
		}
		catch (FileNotFoundException e){
		    System.out.println(e);
		}
		StringBuffer seq = new StringBuffer();
		String nextLine = new String();
		try
		{
			nextLine = this.reader.readLine(); // first line is header
		}catch (IOException e) {System.out.println(e);}
		this.header = nextLine.substring(1,nextLine.length());
		try
		{
			nextLine = this.reader.readLine();// start from second line
		}catch(IOException e) {System.out.println(e);}
		while( nextLine != null & !nextLine.startsWith(">"))
		{
			seq.append(nextLine);
			try
			{
				nextLine = this.reader.readLine();// start from second line
			}catch(IOException e) {System.out.println(e);}
			if (nextLine.startsWith(">"))
			{
				this.nextheader = nextLine.substring(1,nextLine.length());//this is the new header for the next sequence
			}
		}
		this.sequence = seq.toString();
	}
	
	
	public void ReadNextSeq() throws Exception
	{
		if(initiate)
		{
			throw new Exception("this sequence hasn't been initiated");
		}
		else
		{
			this.header = this.nextheader;
			if(this.header == null)
			{
				this.sequence = null;
				return;
			}
			StringBuffer seq = new StringBuffer();
			String nextLine = this.reader.readLine();// start from second line
			while( !nextLine.startsWith(">"))
			{
				 seq.append(nextLine);
				 nextLine = this.reader.readLine();
				 if (nextLine == null)
				 {
					 this.nextheader = null;
					 return;
				 }
				 if (nextLine.startsWith(">"))
				 {
					 this.nextheader = nextLine.substring(1,nextLine.length());//this is the new header for the next sequence
				 }
			}
			this.sequence = seq.toString();
		}
		
	}
	
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception
	{
		List<FastaSequence> seqlist = new ArrayList<FastaSequence>();
		FastaSequence seq = new FastaSequence(filepath);
		FastaSequence fa0 = new FastaSequence(seq);
		seqlist.add(fa0);
		
		while(seq.sequence != null)
		{	
			seq.ReadNextSeq();
			if(seq.sequence == null)
			{
				return seqlist;
			}
			FastaSequence faN = new FastaSequence(seq);
			seqlist.add(faN);
		}
		return seqlist;
	}

	public static void main(String[] args) throws Exception, FileNotFoundException, IOException 
	{
	    FastaSequence se = new FastaSequence("/Users/suyangqi/git/java_labs/ERCC92.fa");
	    System.out.println(se.getHeader());
	    System.out.println(se.getSequence());
	    System.out.println(se.getGCRatio());
	    se.ReadNextSeq();
	    System.out.println(se.getHeader());
	    System.out.println(se.getSequence());
	    System.out.println(se.getGCRatio());
	    se.ReadNextSeq();
	    System.out.println(se.getHeader());
	    System.out.println(se.getSequence());
	    System.out.println(se.getGCRatio());
	

	    List<FastaSequence> fastaList = FastaSequence.readFastaFile("/Users/suyangqi/git/java_labs/ERCC92.fa");

	    for( FastaSequence fs : fastaList)
		{
		    System.out.println(fs.getHeader());
		    System.out.println(fs.getSequence());
		    System.out.println(fs.getGCRatio());
		}
	}
}
