package lab8;
import java.util.HashMap;
import java.util.Map;

import lab8.FastaSequence;
import lab8.MultiThreadFastaStreamer;

public class FastaFileHandler
{
	//for lab 8
	public static Map<Character, Integer> fileAlphaCounter(String filepath) throws Exception
	{
		Map<Character, Integer> ct = new HashMap<Character, Integer>();
		ct.putIfAbsent('a', 0);ct.putIfAbsent('g', 0);ct.putIfAbsent('c', 0);ct.putIfAbsent('t', 0);ct.putIfAbsent('n', 0);
		MultiThreadFastaStreamer faReader = new MultiThreadFastaStreamer(filepath);
		FastaSequence fa0 = faReader.read();
		
		while(fa0.getSequence() != null)
		{	
			fa0.countAlphabets(ct);
			fa0 = faReader.read();
		}
		return ct;

	}

	
}
