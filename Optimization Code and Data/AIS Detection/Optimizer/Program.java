import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Program {

	public static void main(String[] args) {
		
		//get the rankings of denoising parameter sets and clustering and no clustering
		
		String directory = args[0];
		
		ArrayList<String> lengths = new ArrayList<String>();
		lengths.add("300");
		lengths.add("325");
		lengths.add("350");
		lengths.add("375");
		lengths.add("400");
		
		ArrayList<String> meeFilters = new ArrayList<String>();
		meeFilters.add("1.0");
		meeFilters.add("1.5");
		meeFilters.add("2.0");
		meeFilters.add("2.5");
		meeFilters.add("3.0");
		
		ArrayList<String> minnums = new ArrayList<String>();
		minnums.add("1");
		minnums.add("2");
		
		ArrayList<String> qFilters = new ArrayList<String>();
		qFilters.add("10");
		qFilters.add("20");
		qFilters.add("30");
		
		ArrayList<String> minAbundances = new ArrayList<String>();
		minAbundances.add("2");
		minAbundances.add("4");
		minAbundances.add("8");
		
		ArrayList<String> clusteringRadii = new ArrayList<String>();
		clusteringRadii.add("97");
		clusteringRadii.add("98");
		clusteringRadii.add("99");

		ArrayList<Result> clusteringResults = new ArrayList<Result>();
		ArrayList<Result> denoisingResults = new ArrayList<Result>();
		ArrayList<Result> noClusteringResults = new ArrayList<Result>();
		
		try{
			for(String length: lengths){
				for(String meeFilter: meeFilters){
					for(String minnum: minnums){
						for (String qFilter: qFilters){
							for (String minAbundance: minAbundances){
	
								float correctSequencesAverage = 0;
								float ambiguousSequencesAverage = 0;
								float incorrectSequencesAverage = 0;
								float redundantSequencesAverage = 0;
								
								String fileName  = directory + "OTUCounts_" + length + "_" + qFilter + "_" + meeFilter + "_" + minAbundance + "_" + minnum + "_denoising.csv";

								try {
									BufferedReader br = new BufferedReader(new FileReader(fileName));
									String line;
								
									while ((line = br.readLine()) != null) {
										StringTokenizer stk = new StringTokenizer(line);
										int correct = Integer.parseInt(stk.nextToken());
										int ambiguous = Integer.parseInt(stk.nextToken());
										int incorrect = Integer.parseInt(stk.nextToken());
										int redundant = Integer.parseInt(stk.nextToken());
										
										correctSequencesAverage += correct;
										ambiguousSequencesAverage += ambiguous;
										incorrectSequencesAverage += incorrect;
										redundantSequencesAverage += redundant;
									}
									br.close();
								} catch (IOException e){
									System.out.println("Something broke with denoising");
								}
								
								correctSequencesAverage /= 100;
								ambiguousSequencesAverage /= 100;
								incorrectSequencesAverage /= 100;
								redundantSequencesAverage /= 100;
								
								insertionSort(denoisingResults, new Result(length + "," + qFilter + "," + meeFilter + "," + minAbundance + "," + minnum, correctSequencesAverage, ambiguousSequencesAverage, incorrectSequencesAverage, redundantSequencesAverage));
							}
							
							for (String clusteringRadius: clusteringRadii){
	
								float correctSequencesAverage = 0;
								float ambiguousSequencesAverage = 0;
								float incorrectSequencesAverage = 0;
								float redundantSequencesAverage = 0;
								
								String fileName  = directory + "OTUCounts_" + length + "_" + qFilter + "_" + meeFilter + "_" + clusteringRadius + "_" + minnum + "_clustering.csv";

								try {
									BufferedReader br;
									br = new BufferedReader(new FileReader(fileName));
									String line;
								
									while ((line = br.readLine()) != null) {
										StringTokenizer stk = new StringTokenizer(line);
										int correct = Integer.parseInt(stk.nextToken());
										int ambiguous = Integer.parseInt(stk.nextToken());
										int incorrect = Integer.parseInt(stk.nextToken());
										int redundant = Integer.parseInt(stk.nextToken());
										
										correctSequencesAverage += correct;
										ambiguousSequencesAverage += ambiguous;
										incorrectSequencesAverage += incorrect;
										redundantSequencesAverage += redundant;
									}
									br.close();
								} catch (IOException e){
									System.out.println("Something broke with clustering");
								}
								correctSequencesAverage /= 100;
								ambiguousSequencesAverage /= 100;
								incorrectSequencesAverage /= 100;
								redundantSequencesAverage /= 100;
								
								insertionSort(clusteringResults, new Result(length + "," + qFilter + "," + meeFilter + "," + clusteringRadius + "," + minnum, correctSequencesAverage, ambiguousSequencesAverage, incorrectSequencesAverage, redundantSequencesAverage));
							}
							
							String id = "100";
							float correctSequencesAverage = 0;
							float ambiguousSequencesAverage = 0;
							float incorrectSequencesAverage = 0;
							float redundantSequencesAverage = 0;
							
							String fileName  = directory + "OTUCounts_" + length + "_" + qFilter + "_" + meeFilter + "_" + id + "_" + minnum + "_clustering.csv";

							try {
								BufferedReader br;
								br = new BufferedReader(new FileReader(fileName));
								String line;
							
								while ((line = br.readLine()) != null) {
									StringTokenizer stk = new StringTokenizer(line);
									int correct = Integer.parseInt(stk.nextToken());
									int ambiguous = Integer.parseInt(stk.nextToken());
									int incorrect = Integer.parseInt(stk.nextToken());
									int redundant = Integer.parseInt(stk.nextToken());
									
									correctSequencesAverage += correct;
									ambiguousSequencesAverage += ambiguous;
									incorrectSequencesAverage += incorrect;
									redundantSequencesAverage += redundant;
								}
								br.close();
							} catch (IOException e){
								System.out.println("Something broke with clustering with ID 100");
							}
							correctSequencesAverage /= 100;
							ambiguousSequencesAverage /= 100;
							incorrectSequencesAverage /= 100;
							redundantSequencesAverage /= 100;
							
							insertionSort(noClusteringResults, new Result(length + "," + qFilter + "," + meeFilter + "," + id + "," + minnum, correctSequencesAverage, ambiguousSequencesAverage, incorrectSequencesAverage, redundantSequencesAverage));
						}
					}
				}
			}
			try {
				BufferedWriter outClustering = new BufferedWriter(new FileWriter(directory + "OptimizationResultsClustering.csv"));
				BufferedWriter outDenoising = new BufferedWriter(new FileWriter(directory + "OptimizationResultsDenoising.csv"));
				BufferedWriter outNoClustering = new BufferedWriter(new FileWriter(directory + "OptimizationResultsNoClustering.csv"));
				
				for (Result r: clusteringResults){
					outClustering.write(r + "\n");
				}
				outClustering.close();
				
				for (Result r: denoisingResults){
					outDenoising.write(r + "\n");
				}
				outDenoising.close();
				
				for (Result r: noClusteringResults){
					outNoClustering.write(r + "\n");
				}
				outNoClustering.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} finally{
		}
		
	}
	
	public static void insertionSort(ArrayList<Result> currentList, Result result){
		int i = 0;
		while(i < currentList.size()){
			if (result.lessThan(currentList.get(i))) i++;
			else break;
		}
		if (i < currentList.size())currentList.add(i, result);
		else currentList.add(result);
	}
}
