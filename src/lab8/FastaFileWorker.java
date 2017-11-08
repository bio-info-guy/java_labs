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
	
	public FastaFileWorker(String path, Semaphore semaphore)
	{
		this.s = semaphore;
		this.path = path;
	}
	
	public void run() 
	{
		try
		{
			int[] map = FastaFileHandler.fileAlphaCounter(path);
			synchronized (A)
			{
				A = A+map[0];
			}
			synchronized (C)
			{
				C = C+map[1];
			}
			synchronized (T)
			{
				T = T+map[2];
			}
			synchronized (G)
			{
				G = G+map[3];
			}
			synchronized (N)
			{
				N = N+map[4];
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
			if (fileName.startsWith("D5"))
			{
			semaphore.acquire();
			String filepath = DIRPATH.getAbsolutePath()+"/"+fileName;
			FastaFileWorker w = new FastaFileWorker(filepath, semaphore);
			new Thread(w).start();
			}
		}
		
		int numAcquired= 0;
		while (numAcquired < NUM_WORKERS)
		{
			semaphore.acquire();
			numAcquired++;
		}
		System.out.println("Time "+((System.currentTimeMillis() - startTime)/1000f));
		System.out.println("A: "+A);
		System.out.println("C: "+C);
		System.out.println("T: "+T);
		System.out.println("G: "+G);
		System.out.println("N: "+N);
	}
}
