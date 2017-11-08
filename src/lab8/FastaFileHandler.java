package lab8;
import java.util.HashMap;
import java.util.Map;

import lab8.FastaSequence;
import lab8.MultiThreadFastaStreamer;

public class FastaFileHandler
{
	//for lab 8
	public static int[] fileAlphaCounter(String filepath) throws Exception
	{
		int[] ct = new int[5];
		ct[0] = 0; ct[1] = 0; ct[2] = 0; ct[3] = 0; ct[4] = 0;
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
