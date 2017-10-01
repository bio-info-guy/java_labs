package lab4;

public class FastaSequence {
	private String sequence;
	private String header;
	public FastaSequence(String sequence, String header)
	{
		this.sequence = sequence;
		this.header = header;		
	}
    public FastaSequence(FastaSequence fa)//allows for assigning one FastaSequence with the values of another
	{
		this.sequence = fa.sequence;
		this.header = fa.header;
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
	public static void main(String[] args)
	{
		
	}

}
