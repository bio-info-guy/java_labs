package lab8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class FasterFastaFileWorker implements Runnable
{

		private final String path;
		private final Semaphore s;
		public static final Integer NUM_WORKERS = 5;
		public static final File DIRPATH = new File("/Users/suyangqi/git/java_labs/fasta");
		private static final Map<String, int[]> counter = new ConcurrentHashMap<String, int[]>();
		public FasterFastaFileWorker(String path, Semaphore semaphore)
		{
			this.s = semaphore;
			this.path = path;
		}
		
		public void run() 
		{
			try
			{
				int[] map = FastaFileHandler.fileAlphaCounter(path);
				counter.put(map.toString(), map);
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
				FasterFastaFileWorker w = new FasterFastaFileWorker(filepath, semaphore);				
				new Thread(w).start();
				}
				
			}			
			int numAcquired= 0;
			while (numAcquired < NUM_WORKERS)
			{
				semaphore.acquire();
				numAcquired++;
			}
			Integer A = 0; Integer C =0; Integer G = 0; Integer T = 0; Integer N = 0;
			for(String x : counter.keySet())
			{
				A = A+counter.get(x)[0];
				C = C+counter.get(x)[1];
				T = T+counter.get(x)[2];
				G = G+counter.get(x)[3];
				N = N+counter.get(x)[4];
			}
			System.out.println("Time "+((System.currentTimeMillis() - startTime)/1000f));
			System.out.println("A: "+A);
			System.out.println("C: "+C);
			System.out.println("T: "+T);
			System.out.println("G: "+G);
			System.out.println("N: "+N);

		}
}


