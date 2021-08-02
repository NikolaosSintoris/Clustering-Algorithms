package clustering;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ClusteringDataSet 
{
	private float[] pointsArray = new float[2];
	
	public void  createDataSet()
	{
		try
		{
			FileWriter writer = new FileWriter("ClusteringDataSet.txt");
			
			// Create first data set.
			boolean belongsToFirstSet;
			for(int i = 0; i < 100; i++)
			{
				pointsArray = createRandomNumbers((float)-0.3, (float)0.3);
				belongsToFirstSet = checkIfBelongsToFirstSet(pointsArray);
				while(belongsToFirstSet == false)
				{
					pointsArray = createRandomNumbers((float)-0.3, (float)0.3);
					belongsToFirstSet = checkIfBelongsToFirstSet(pointsArray);
				}
				
				String line = pointsArray[0] + "," + pointsArray[1] + "\n";
				writer.write(line);
			}
			
			// Create second data set.
			for(int i = 0; i < 100; i++)
			{
				pointsArray[0] = createRandomNumber((float)-1.1, (float)-0.5);
				pointsArray[1] = createRandomNumber((float)0.5, (float)1.1);

				String line = pointsArray[0] + "," + pointsArray[1] + "\n";
				writer.write(line);
			}
			
			// Create third data set.
			for(int i = 0; i < 100; i++)
			{
				pointsArray = createRandomNumbers((float)-1.1, (float)-0.5);

				String line = pointsArray[0] + "," + pointsArray[1] + "\n";
				writer.write(line);
			}
			
			// Create fourth data set.
			for(int i = 0; i < 100; i++)
			{
				pointsArray[0] = createRandomNumber((float)0.5, (float)1.1);
				pointsArray[1] = createRandomNumber((float)-1.1, (float)-0.5);

				String line = pointsArray[0] + "," + pointsArray[1] + "\n";
				writer.write(line);
			}
			
			// Create fifth data set.
			for(int i = 0; i < 100; i++)
			{
				pointsArray = createRandomNumbers((float)0.5, (float)1.1);

				String line = pointsArray[0] + "," + pointsArray[1] + "\n";
				writer.write(line);
			}
			
			// Create third data set.
			for(int i = 0; i < 100; i++)
			{
				pointsArray = createRandomNumbers(-1, 1);

				String line = pointsArray[0] + "," + pointsArray[1] + "\n";
				writer.write(line);
			}
			
			writer.close();	
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public float[]  createRandomNumbers(float minValue, float maxValue)
	{
		Random rand = new Random();
		float x1 = minValue + (maxValue - minValue) * rand.nextFloat();
		float x2 = minValue + (maxValue - minValue) * rand.nextFloat();

		float[] tempArray = new float[2];;
		tempArray[0] = x1;
		tempArray[1] = x2;
		
		return tempArray;
	}
	
	public boolean checkIfBelongsToFirstSet(float[] pointsArray)
	{
		if( (Math.pow((pointsArray[0] - 0), 2) + Math.pow((pointsArray[1] - 0), 2)) <= 0.09 )
		{
			return true;
		}
		return false;
	}
	
	public float  createRandomNumber(float minValue, float maxValue)
	{
		Random rand = new Random();
		float randomNumber = minValue + (maxValue - minValue) * rand.nextFloat();

		return randomNumber;
	}
}
