package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class LVQAlgorithm 
{
	private ArrayList<float[]> clusteringDataSetArrayList;
	private ArrayList<float[]> centroidsArrayList;
	private ArrayList<float[]> tempCentroidsArrayList;
	private int numberOfCentroids;
	private float learningRate;
	private float clusteringError;
	
	public LVQAlgorithm(int numberOfCentroids, float learningRate)
	{
		this.clusteringDataSetArrayList = new ArrayList<float[]>();
		
		this.numberOfCentroids = numberOfCentroids;
		
		this.learningRate = learningRate;
		
		this.clusteringError = 0;
	}
	
	public void loadClusteringDataSet()
	{
        try 
        {
            FileReader fileReader = new FileReader("ClusteringDataSet.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
            	String[] tempStringArray= line.split(",");
            	float x1 = Float.parseFloat(tempStringArray[0]);
            	float x2 = Float.parseFloat(tempStringArray[1]);
            	float[] example = {x1, x2, 0};
            	this.clusteringDataSetArrayList.add(example);
            }
            fileReader.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
	}

	public float LVQ()
	{
		initializeRandomCentroids();
		initializeTempCentroidsArrayList();
		
		int epoch = 0;
		boolean flag = true;
		while(flag == true)
		{
			epoch++;
			//System.out.println("\tEpoch: " + epoch);
			
			this.clusteringError = 0;
			
			for(int i = 0; i < this.clusteringDataSetArrayList.size(); i++)
			{
				// compute the euclidian distances of an example from all centroids.
				float[] currentExample = this.clusteringDataSetArrayList.get(i);
				float[] allEuclidianDistances = new float[this.numberOfCentroids];
				for(int j = 0; j < allEuclidianDistances.length; j++)
				{
					float[] centroid = this.centroidsArrayList.get(j);
					float euclidianDistance = 0f;
					for(int k = 0; k < 2; k++)
					{
						euclidianDistance = (float) (euclidianDistance + Math.pow((currentExample[k] - centroid[k]), 2));
					}
					allEuclidianDistances[j] = euclidianDistance;
				}

				// Find the min distance.
				int minIndex = 0;
				float minValue = allEuclidianDistances[0];
				for(int index = 1; index < this.numberOfCentroids; index++)
				{
					if(allEuclidianDistances[index] <= minValue)
					{
						minValue = allEuclidianDistances[index];
						minIndex = index;
					}
				}
				currentExample[2] = minIndex + 1;
				this.clusteringDataSetArrayList.set(i, currentExample);
				
				// Update only the winner's centroid weight.
				float[] newWinnerWeight = {0, 0, 0}; 
				float[] oldWinnerWeight = this.centroidsArrayList.get(minIndex);
				for(int w = 0; w < 2; w++)
				{
					newWinnerWeight[w] = oldWinnerWeight[w] + this.learningRate * (currentExample[w] - oldWinnerWeight[w]);
				}
				newWinnerWeight[2] = oldWinnerWeight[2];
				this.tempCentroidsArrayList.set(minIndex, newWinnerWeight);
			}
			
			flag = checkIfCentroidsChanged();
			
			updateCentroidsArrayList();
			
			computeClusteringError();
			
			reducingLearningRate();
		}	

		return this.clusteringError;
	}
	
	public void initializeRandomCentroids()
	{
		this.centroidsArrayList = new ArrayList<float[]>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int randomIndex;
		for(int i = 0; i < this.numberOfCentroids; i++)
		{
			randomIndex = ThreadLocalRandom.current().nextInt(0, this.clusteringDataSetArrayList.size());

			if(temp.contains(randomIndex) == false)
			{
				float[] randomCentroid = this.clusteringDataSetArrayList.get(randomIndex);
				randomCentroid[2] = i + 1;
				this.centroidsArrayList.add(randomCentroid);
				temp.add(randomIndex);
			}
			else
			{
				randomIndex = ThreadLocalRandom.current().nextInt(0, this.clusteringDataSetArrayList.size());
				while(temp.contains(randomIndex) == true)
				{
					randomIndex = ThreadLocalRandom.current().nextInt(0, this.clusteringDataSetArrayList.size());
				}
				float[] randomCentroid = this.clusteringDataSetArrayList.get(randomIndex);
				randomCentroid[2] = i + 1;
				this.centroidsArrayList.add(randomCentroid);
				temp.add(randomIndex);
				/*
				randomIndex = ThreadLocalRandom.current().nextInt(0, this.clusteringDataSetArrayList.size());
				boolean flag = true;
				while(flag == true)
				{
					if(temp.contains(randomIndex) == true)
					{
						randomIndex = ThreadLocalRandom.current().nextInt(0, this.clusteringDataSetArrayList.size());
					}
					else
					{
						flag = false;
					}
				}
				float[] randomCentroid = this.clusteringDataSetArrayList.get(randomIndex);
				randomCentroid[2] = i + 1;
				this.centroidsArrayList.add(randomCentroid);
				temp.add(randomIndex);
				*/
			}
		}
	}
		
	public void initializeTempCentroidsArrayList()
	{
		this.tempCentroidsArrayList = new ArrayList<float[]>();
		float[] array = {0, 0, 0};
		for(int i = 0; i < this.numberOfCentroids; i++)
		{
			this.tempCentroidsArrayList.add(array);
		}
	}
	
	public boolean checkIfCentroidsChanged()
	{
		int counter = 0;
		for(int i = 0; i < this.numberOfCentroids; i++)
		{
			float[] previousCentroid = this.centroidsArrayList.get(i);
			float[] newCentroid = this.tempCentroidsArrayList.get(i);
			
			for(int j = 0; j <= 2; j++)
			{
				if(previousCentroid[j] == newCentroid[j])
				{
					counter++;
				}
			}
		}
		if(counter == (3 * this.numberOfCentroids))
		{
			return false;
		}
		return true;
	}
	
	public void updateCentroidsArrayList()
	{
		for(int i = 0; i < this.numberOfCentroids; i++)
		{
			float[] newCentroid = this.tempCentroidsArrayList.get(i);
			this.centroidsArrayList.set(i, newCentroid);
		}
	}
	
	public void computeClusteringError()
	{
		for(int i = 0; i < this.clusteringDataSetArrayList.size(); i++)
		{
			float[] currentExample = this.clusteringDataSetArrayList.get(i);
			
			for(int cluster = 0; cluster < this.numberOfCentroids; cluster++)
			{
				if((cluster+1) == currentExample[2])
				{
					float[] centroid = this.centroidsArrayList.get(cluster);
					float sumOfExampleFromHisCentroid = 0;
					for(int j = 0; j < 2; j++)
					{
						sumOfExampleFromHisCentroid = (float) (sumOfExampleFromHisCentroid + Math.pow((currentExample[j]- centroid[j]), 2));
					}
					this.clusteringError = this.clusteringError + sumOfExampleFromHisCentroid;
					break;
				}
			}
		}
		//System.out.println("\t\tClusteringError: " + this.clusteringError);
	}
	
	public void reducingLearningRate()
	{
		this.learningRate = (95 * this.learningRate)/100;
	}

	public ArrayList<float[]> getBestClusteringSet()
	{
		return this.clusteringDataSetArrayList;
	}
	
	public ArrayList<float[]> getBestCentroids()
	{
		return this.centroidsArrayList;
	}
	
}
