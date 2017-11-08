package lab8;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class FastaSequence implements Comparable<FastaSequence>
{
	private static final char[] residues = {'A','a','C','c','G','g','T','t'};
	private String sequence;
	private String header;
	private static final Set<Character> set;
	
	static
	{
		Set<Character> mySet = new LinkedHashSet<Character>();
		for(char c : residues)
			mySet.add(c);
		set = Collections.unmodifiableSet(mySet);
	}
	
	public void countAlphabets(int[] ct)
	{
		for (char s : this.sequence.toLowerCase().toCharArray())
		{
			if(s != 'a' & s != 'g' & s != 't' & s != 'c' )
			{
				ct[4]++;
			}
			else
			{
				switch (s)
				{
				case 'a': ct[0]++;
				break;
				case 'c': ct[1]++;
				break;
				case 't': ct[2]++;
				break;
				case 'g': ct[3]++;
				break;
				}
			}
			
		}
	}
	
	public Set<Character> getAlphabet()
	{
		return set;
	}
	
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
	
	public double getRationValid()
	{
		Set<Character> set = this.getAlphabet();
		double numValid = 0;
		for(char c : this.sequence.toCharArray())
		{
			if(set.contains(c))
				numValid++;
		}
		return numValid/this.sequence.length();
	}
	
	public int compareTo(FastaSequence anotherseq)
	{
		if (this.sequence.equals(anotherseq.sequence))
		{
			if (this.header.equals(anotherseq.header))
			{
				if (this.getGCRatio() == anotherseq.getGCRatio())
				{
					return Double.compare(this.getRationValid(), anotherseq.getRationValid());
				}
				return Float.compare(this.getGCRatio(), anotherseq.getGCRatio());
			}
			return this.header.compareTo(anotherseq.header);
		}
		return this.sequence.compareToIgnoreCase(anotherseq.sequence);
	}
	
	public static void main(String[] args)
	{
		
	}

}
