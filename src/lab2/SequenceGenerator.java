package lab2;
import java.util.Random;
import java.lang.String;
import java.lang.reflect.Array;;
public class SequenceGenerator 
{
	
	public static final String nt = "ATCG" ;
	public static int length = 1000;
	public static String[] mersArray = new String[length];
	public static double countAAAs (String[] strArray, String mer)	
	{
		
		double count = 0;
		double size = Array.getLength(strArray);
		
		for ( int x = 0 ; x < size; x++)
		{
			//System.out.println(strArray[x]);
			if (strArray[x].equals(mer))
			{				
				count++;
			}
		}
		System.out.println(count);
		System.out.println(size);
		return count/size;
	}
	public static void main(String[] args) 
	{
		Random random = new Random();
		
		for (int x =0; x < length; x++)
		{
			String mers = "";
			for (int y =0; y <3; y++)
				{
				mers = mers + nt.charAt(random.nextInt(4));
				}
			System.out.printf(mers);
			mersArray[x] = mers;
		}
		
		String tmer="AAA";
		double uniformPercent = countAAAs(mersArray, tmer);
		System.out.println("Fraction of AAAs from a uniform generator is "+ uniformPercent);
		
		//if not uniformly generated
		for (int x =0; x < length; x++)
		{
			String mers = "";
			for (int y =0; y <3; y++)
				{
				if (random.nextFloat() < 0.12)
					{mers = mers+"A";}
				else if (random.nextFloat() < 0.50)
					{mers = mers+"C";}
				else if (random.nextFloat() < 0.89)
					{mers = mers+"G";}
				else
					{mers = mers+"T";}
				}
			System.out.printf(mers);
			mersArray[x] = mers;
		}
		double nonuniformPercent = countAAAs(mersArray, tmer);
		System.out.println("Fraction of AAAs from a uniform generator is "+ nonuniformPercent);
	}
}