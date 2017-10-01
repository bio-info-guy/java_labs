package lab6;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashMap;

import lab4.FastaSequence;
import lab4.FastaStreamer;

public class FastaFileHandler {
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception
	{
		List<FastaSequence> seqlist = new ArrayList<FastaSequence>();
		FastaStreamer faReader = new FastaStreamer(filepath);
		FastaSequence fa0 = faReader.read();
		seqlist.add(fa0);
		
		while(fa0.getSequence() != null)
		{	
			fa0 = faReader.read();
			if(fa0.getSequence() == null)
			{
				return seqlist;
			}
			FastaSequence fa = new FastaSequence(fa0);
			seqlist.add(fa);
		}
		return seqlist;
	}
	//This is the method for lab5
	public static void uniqueFasta2File(String infile, String outfile) throws Exception//lab5 static method
	{

		FastaStreamer faReader = new FastaStreamer(infile);
		Map<String, Integer> map= new TreeMap<String, Integer>();
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
		FastaSequence fa = faReader.read();
		while(fa.getSequence() != null)
		{
			Integer count = map.get(fa.getSequence());
			if (count==null)
			{
				count=0;
			}
			count++;
			map.put(fa.getSequence(), count);
		fa = faReader.read();
		}
		for (String key: map.keySet())
		{
			bw.write(">"+map.get(key)+"\n");
			bw.write(key+"\n");
		}
		bw.close();
	}
	//This is the method for lab6
	public static void fastaSampleMap(String infile, String outfile) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();
		FastaStreamer faReader = new FastaStreamer(infile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
		FastaSequence fa = faReader.read();
		LinkedHashSet<String> sequences = new LinkedHashSet<String>();
		TreeSet<String> samples = new TreeSet<String>();
		while(fa.getSequence() != null)
		{
			
			StringTokenizer sToken = new StringTokenizer(fa.getHeader());
			sToken.nextToken();
			String s = sToken.nextToken();
			sequences.add(fa.getSequence());
			samples.add(s);
			HashMap<String, Integer> innermap = map.get(fa.getSequence());
			if (innermap == null)
			{
				innermap = new HashMap<String,Integer>();
			}
			Integer count = innermap.get(s);
			if (count==null)
			{
				count=0;
			}
			count++;
			innermap.put(s, count);
			map.put(fa.getSequence(),  innermap);
			fa = faReader.read();
		}
		bw.write("\t");
		for (String seq:sequences)
		{
			bw.write(seq+"\t");
		}
		bw.write("\n");
		for (String key: samples)
		{
			bw.write(key+"\t");
			for (String seq:sequences)
			{
				if (map.get(seq).get(key) == null)
				{
					bw.write(0+"\t");
				}
				else
					{
					bw.write(map.get(seq).get(key)+"\t");
					}
			}
			bw.write("\n");
		}
		bw.flush();
		bw.close();
	}
	}
