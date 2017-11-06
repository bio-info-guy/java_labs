package lab8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class FastaFileWorker implements Runnable {
	private final String path;
	private final Semaphore s;
	public static final Integer NUM_WORKERS = 5;
	public static final File DIRPATH = new File("/Users/suyangqi/git/java_labs/fasta");
	private static Integer A = 0;
	private static Integer C = 0;
	private static Integer T = 0;
	private static Integer G = 0;
	private static Integer N = 0;
	private static final Map<Character, Integer> counter = Collections.synchronizedMap(new HashMap<Character, Integer>());
	static
	{
		counter.put('a', 0);
		counter.put('t', 0);
		counter.put('c', 0);
		counter.put('g', 0);
		counter.put('n', 0);	
	}
	
	public FastaFileWorker(String path, Semaphore semaphore)
	{
		this.s = semaphore;
		this.path = path;
	}
	
	public void run() 
	{
		try
		{
			Map<Character, Integer> map = FastaFileHandler.fileAlphaCounter(path);
			synchronized (A)
			{
				A = A+map.get('a');
			}
			synchronized (C)
			{
				C = C+map.get('c');
			}
			synchronized (T)
			{
				T = T+map.get('t');
			}
			synchronized (G)
			{
				G = G+map.get('g');
			}
			synchronized (N)
			{
				N = N+map.get('n');
			}
			this.s.release();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}
	}
	public static void main(String[] args) throws Exception, FileNotFoundException, IOException
	{
		long startTime = System.currentTimeMillis();
		String[] fileNames = DIRPATH.list();
		Semaphore semaphore = new Semaphore(NUM_WORKERS);
		for ( String fileName :fileNames)
		{
			semaphore.acquire();
			String filepath = DIRPATH.getAbsolutePath()+"/"+fileName;
			FastaFileWorker w = new FastaFileWorker(filepath, semaphore);
			new Thread(w).start();
		}
		int numAcquired= 0;
		while (numAcquired < NUM_WORKERS)
		{
			semaphore.acquire();
			numAcquired++;
		}
		System.out.println(A);
		System.out.println("Time "+((System.currentTimeMillis() - startTime)/1000f));
		
		
	}
	
}
