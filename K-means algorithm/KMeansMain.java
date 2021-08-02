package clustering;

import java.util.ArrayList;
import java.util.Scanner;

import java.awt.Color;  
import javax.swing.JFrame;  
import javax.swing.SwingUtilities;  
import javax.swing.WindowConstants;  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.plot.XYPlot;  
import org.jfree.data.xy.XYDataset;  
import org.jfree.data.xy.XYSeries;  
import org.jfree.data.xy.XYSeriesCollection;

public class KMeansMain extends JFrame
{
	private static final long serialVersionUID = 6294689542092367723L; 
	private static float minClusteringError;
	private static ArrayList<float[]> bestClusteringDataSetArrayList;
	private static ArrayList<float[]> bestCentroidsArrayList;

	
	public KMeansMain(String title, int numberOfCentroids)
	{
		super(title);
		
		minClusteringError = 100000000000000f;
		
		bestClusteringDataSetArrayList = new ArrayList<float[]>();
		bestCentroidsArrayList = new ArrayList<float[]>();
		
	    XYDataset dataset = createDataset(numberOfCentroids);  

	    // Create chart. 
	    JFreeChart chart = ChartFactory.createScatterPlot("K-Means Scatter Plot", "X-Axis", "Y-Axis", dataset); 
	    
	    //Changes background color. 
	    //XYPlot plot = (XYPlot)chart.getPlot();  
	    //plot.setBackgroundPaint(new Color(255,228,196)); 
	    
	    // Create Panel.
	    ChartPanel panel = new ChartPanel(chart);  
	    setContentPane(panel);  
	}
	
	private XYDataset createDataset(int numberOfCentroids)
	{
		XYSeriesCollection dataset = new XYSeriesCollection(); 
		
		
		//------- Find the centroids ----------------------------------------- 
		kMeansAlgorithm kMeansObject = new kMeansAlgorithm(numberOfCentroids);
		kMeansObject.loadClusteringDataSet();
		
		for(int iteration = 1; iteration <= 5; iteration++)
		{
			//System.out.println("ITERATION: " + iteration);

			float tempClusteringError = kMeansObject.kMeans();
			
			if(tempClusteringError <= minClusteringError)
			{
				minClusteringError = tempClusteringError;
				bestClusteringDataSetArrayList = kMeansObject.getBestClusteringSet();
				bestCentroidsArrayList = kMeansObject.getBestCentroids();
			}
		}
		System.out.println("Min clustering error: " + minClusteringError);
		//----------------------------------------------------------------- 
		
		
		XYSeries series1 = new XYSeries("Centroids"); 
		for(int i = 0; i < bestCentroidsArrayList.size(); i++)
		{
			float[] centroid = bestCentroidsArrayList.get(i);
			
			series1.add(centroid[0], centroid[1]);  
		}
	    dataset.addSeries(series1);
		
		XYSeries series2 = new XYSeries("Examples");
		for(int i = 0; i < bestClusteringDataSetArrayList.size(); i++)
		{
			float[] example = bestClusteringDataSetArrayList.get(i);
			if(bestCentroidsArrayList.contains(example) == false)
			{
				series2.add(example[0], example[1]);
			}
		}
		dataset.addSeries(series2); 
		
	    return dataset;  
	}
	
	public static void main(String[] args) 
	{
		Scanner userInput = new Scanner(System.in);
		
		System.out.println("K-Means!!!!!!!");
		System.out.println();
		
		System.out.println("Creation of clustering data set.");
		ClusteringDataSet data = new ClusteringDataSet();
		data.createDataSet();
		System.out.println("End of creation.");
		System.out.println();
		
		int counter = 1;
		while(counter <= 6)
		{
			counter++;
			
			System.out.println("Give number of clusters: ");
			int numberOfCentroids = userInput.nextInt();
			
		    SwingUtilities.invokeLater(() -> {  
		    	KMeansMain example = new KMeansMain("K-means Scatter Plot with " + numberOfCentroids + " centroids", numberOfCentroids);  
		        example.setSize(800, 400);  
		        example.setLocationRelativeTo(null);  
		        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
		        example.setVisible(true);  
		      }); 
		}
	    userInput.close();
	    System.out.println("End of k-means");
	}  
}
