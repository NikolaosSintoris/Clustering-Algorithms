package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class kMeansAlgorithm 
{
	private ArrayList<float[]> clusteringDataSetArrayList;
	private int numberOfCentroids;
	private ArrayList<float[]> centroidsArrayList;
	private ArrayList<float[]> tempCentroidsArrayList;
	
	private ArrayList<Float> sumXAxis;
	private ArrayList<Float> sumYAxis;
	private ArrayList<Integer> numberOfExamplesAtclusters;
	
	private float clusteringError;
	
	public kMeansAlgorithm(int numberOfCentroids)
	{
		this.clusteringDataSetArrayList = new ArrayList<float[]>();
		
		this.numberOfCentroids = numberOfCentroids;
		
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
	
	public float kMeans()
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
			
			initializeMeanArrayLists();
			
			changeClusterOfExamples();

			// Compute mean values.
			for(int i = 0; i < this.clusteringDataSetArrayList.size(); i++)
			{
				float[] currentExample = this.clusteringDataSetArrayList.get(i);
				
				for(int cluster = 1; cluster <= this.numberOfCentroids; cluster++)
				{
					if(cluster == currentExample[2])
					{
						float sum1 = this.sumXAxis.get(cluster-1) + currentExample[0];
						this.sumXAxis.set(cluster-1, sum1);
						
						float sum2 = this.sumYAxis.get(cluster-1) + currentExample[1];
						this.sumYAxis.set(cluster-1, sum2);
						
						int count = this.numberOfExamplesAtclusters.get(cluster-1) + 1;
						this.numberOfExamplesAtclusters.set(cluster-1, count);
					}
				}
			}
			computeNewCentroids();
			
			flag = checkIfCentroidsChanged();

			updateCentroidsArrayList();

			computeClusteringError();
		}	
		//System.out.println("End of kMeans!!!");
		//System.out.println();
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
	
	public void initializeMeanArrayLists()
	{
		this.sumXAxis = new ArrayList<Float>();
		this.sumYAxis = new ArrayList<Float>();
		this.numberOfExamplesAtclusters = new ArrayList<Integer>();
		
		for(int i = 0; i < this.numberOfCentroids; i++)
		{
			this.sumXAxis.add((float) 0);
			this.sumYAxis.add((float) 0.0);
			this.numberOfExamplesAtclusters.add(0);
		}
	}
	
	public void changeClusterOfExamples()
	{
		for(int i = 0; i < this.clusteringDataSetArrayList.size(); i++)
		{
			// Compute the euclidian distances of an example.
			float[] currentExample = this.clusteringDataSetArrayList.get(i);
			float[] allEuclidianDistances = new float[this.numberOfCentroids];
			for(int j = 0; j < this.centroidsArrayList.size(); j++)
			{
				float[] centroid = this.centroidsArrayList.get(j);
				float euclidianDistance = 0f;
				for(int k = 0; k < 2; k++)
				{
					euclidianDistance = (float) (euclidianDistance + Math.pow((currentExample[k] - centroid[k]), 2));
				}
				allEuclidianDistances[j] = euclidianDistance;
			}

			// Find the min distance and change the value.
			int minIndex = 0;
			float minValue = allEuclidianDistances[0];
			for(int index = 1; index < allEuclidianDistances.length; index++)
			{
				if(allEuclidianDistances[index] <= minValue)
				{
					minValue = allEuclidianDistances[index];
					minIndex = index;
				}
			}
			currentExample[2] = minIndex + 1;
			this.clusteringDataSetArrayList.set(i, currentExample);
		}
	}
	
	public void computeNewCentroids()
	{
		for(int i =0; i < this.numberOfCentroids; i++)
		{
			// Not examples at a cluster. To avoid devide with zero.
			if(this.numberOfExamplesAtclusters.get(i) == 0)
			{
				float[] newCentroid = {this.centroidsArrayList.get(i)[0], this.centroidsArrayList.get(i)[1], i+1};
				tempCentroidsArrayList.set(i, newCentroid);
			}
			else
			{
				float meanXValue = this.sumXAxis.get(i) / this.numberOfExamplesAtclusters.get(i);
				float meanYValue = this.sumYAxis.get(i) / this.numberOfExamplesAtclusters.get(i);
				float[] newCentroid = {meanXValue, meanYValue, i+1};
				tempCentroidsArrayList.set(i, newCentroid);
			}
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

	public ArrayList<float[]> getBestClusteringSet()
	{
		return this.clusteringDataSetArrayList;
	}
	
	public ArrayList<float[]> getBestCentroids()
	{
		return this.centroidsArrayList;
	}
	
}
