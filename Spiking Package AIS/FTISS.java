import java.util.*;
import java.io.*;

public class FTISS {

	public static void main(String[] args) {

		int iterationCompleted = Integer.parseInt(args[0]);	//iteration completed
		String rootDirectory = args[1];						//directory ended with /
		String taxon = args[2];								//taxon we're working with

		List<String> params = new ArrayList<String>();
		params.add(new String("OTUs_trunc_400_quality_10_mee_2.5_radOrMinNum_99_minAbundance_1_clustering"));
		params.add(new String("OTUs_trunc_375_quality_10_mee_3.0_radOrMinNum_99_minAbundance_1_clustering"));
		params.add(new String("OTUs_trunc_400_quality_10_mee_3.0_radOrMinNum_99_minAbundance_2_clustering"));
		params.add(new String("OTUs_trunc_375_quality_10_mee_2.5_radOrMinNum_99_minAbundance_2_clustering"));
		params.add(new String(""));
		params.add(new String("OTUs_trunc_400_quality_10_mee_2.5_radOrMinNum_100_minAbundance_2_clustering"));
		params.add(new String("OTUs_trunc_375_quality_10_mee_2.0_radOrMinNum_100_minAbundance_2_clustering"));
		params.add(new String("OTUs_trunc_400_quality_10_mee_1.5_radOrMinNum_100_minAbundance_1_clustering"));
		params.add(new String("OTUs_trunc_375_quality_10_mee_2.5_radOrMinNum_100_minAbundance_1_clustering"));
		params.add(new String(""));
		params.add(new String("OTUs_trunc_400_quality_10_mee_3.0_radOrMinNum_2_minAbundance_2_denoising"));
		params.add(new String("OTUs_trunc_400_quality_10_mee_2.5_radOrMinNum_2_minAbundance_1_denoising"));
		params.add(new String("OTUs_trunc_375_quality_10_mee_3.0_radOrMinNum_2_minAbundance_1_denoising"));
		params.add(new String("OTUs_trunc_375_quality_10_mee_2.5_radOrMinNum_2_minAbundance_2_denoising"));
		params.add(new String(""));
		
		List<String> lengths = new ArrayList<String>();
		lengths.add("400");
		lengths.add("375");
		lengths.add("400");
		lengths.add("375");
		lengths.add("");
		lengths.add("400");
		lengths.add("375");
		lengths.add("400");
		lengths.add("375");
		lengths.add("");
		lengths.add("400");
		lengths.add("400");
		lengths.add("375");
		lengths.add("");
		lengths.add("");
		
		String outputFileName = rootDirectory + "SpikingResult"+ iterationCompleted + "-" + taxon +".csv";
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName, false));

			for (String param: params){	//for each parameter set
				bw.write(param);		//write the name of the parameter set we're looking at here
				for (int i = 0; i < 25; i++){
					try{
						String inputFilename = rootDirectory + param + "/CompleteSample" + i + "-" + iterationCompleted +"-" + taxon + "_otus.fa";	//open a file				
						
						BufferedReader br = new BufferedReader(new FileReader(inputFilename));	//read in the OTU file
						
						boolean foundIt = false;	//have I found the expected taxon in here yet
						
						while (br.ready()){ //check the OTU file
							
							String line = br.readLine();	//get the line
							
							//need to do BLAST cache checking here
							if (line.contains(taxon))
							{
					        	String length = lengths.get(params.indexOf(param));
					        	String fileNameCorrect = "/global/b/work/rrg-rgras-ad/Ryan/CAISN/BLASTNew/BLASTCache" + taxon + length + "CorrectSequences.txt";	//blast cache correct sequences
								String fileNameAmbiguous = "/global/b/work/rrg-rgras-ad/Ryan/CAISN/BLASTNew/BLASTCache" + taxon + length + "AmbiguousSequences.txt";	//blast cache ambiguous sequences
								String fileNameIncorrect = "/global/b/work/rrg-rgras-ad/Ryan/CAISN/BLASTNew/BLASTCache" + taxon + length + "IncorrectSequences.txt";	//blast cache incorrect sequences

								File correct = new File(fileNameCorrect);
								File ambiguous = new File(fileNameAmbiguous);
								File incorrect = new File(fileNameIncorrect);
								
								long correctLength = correct.length();
								long ambiguousLength = ambiguous.length();
								long incorrectLength = incorrect.length();
								
								String fileNameShortest = "";
								String fileNameSecondShortest = "";
								String fileNameLongest = "";
								
								//Get the smallest two caches, don't need to check the third
									//If we have a sequence, it must be in a cache file
									//Therefore it's either in the smallest two or we can assume it's in the third
								if (correctLength <= ambiguousLength && correctLength <= incorrectLength){
									fileNameShortest = fileNameCorrect;
									if (ambiguousLength <= incorrectLength){
										fileNameSecondShortest = fileNameAmbiguous; 
										fileNameLongest = fileNameIncorrect;
									} else{
										fileNameSecondShortest = fileNameIncorrect;
										fileNameLongest = fileNameAmbiguous;
									}
								}
								else if (ambiguousLength <= correctLength && ambiguousLength <= incorrectLength){
									fileNameShortest = fileNameAmbiguous;
									if (correctLength <= incorrectLength){
										fileNameSecondShortest = fileNameCorrect;
										fileNameLongest = fileNameIncorrect; 
									} else{
										fileNameSecondShortest = fileNameIncorrect;
										fileNameLongest = fileNameCorrect;
									}
								}
								else if (incorrectLength <= correctLength && incorrectLength <= ambiguousLength){
									fileNameShortest = fileNameIncorrect;
									if (correctLength <= ambiguousLength){
										fileNameSecondShortest = fileNameCorrect;
										fileNameLongest = fileNameAmbiguous; 
									} else{
										fileNameSecondShortest = fileNameAmbiguous;
										fileNameLongest = fileNameCorrect;
									}
								}
					        	
					        	
					        	//compare to the shortest cache
					        		//find it? done
					        		//don't find it?
					        			//compare to the second shortest cache
					        				//find it? done
					        					//don't find it? it's in the third cache
								BufferedReader brShortest = new BufferedReader(new FileReader(fileNameShortest));
								String cacheLine;		//tentative... this all needs to be tested
								boolean foundOTUIdentity = false;
								//Search the shortest cache first
						        while ((cacheLine = brShortest.readLine()) != null) {
						        	if (line.contains(cacheLine)){
						        		//it belongs to the shortest cache
						        		foundOTUIdentity = true;
						        		break;
						        	}
						        }
						        
						        brShortest.close();
						        
						        if (foundOTUIdentity){
						        	//found it in the first cache
						        	//process it properly according to which cache it is
						        	if (fileNameShortest.equals(fileNameCorrect)){
							        	foundIt = true;
						        	} else if (fileNameShortest.equals(fileNameAmbiguous)){
						        		foundIt = true;
						        	} else if (fileNameShortest.equals(fileNameIncorrect)){
						        		foundIt = false;
						        	} else {
						        		System.out.println("ERROR: Shortest BLAST Cache was not correctly set!");
						        		System.exit(0);
						        	}
						        	if (foundIt) break;
						        	else continue;
						        } else{
						        	//haven't yet found it
									BufferedReader brSecondShortest = new BufferedReader(new FileReader(fileNameSecondShortest));
									
							        while ((cacheLine = brSecondShortest.readLine()) != null) {
							        	if (line.contains(cacheLine)){
							        		//it belongs to the shortest cache
							        		foundOTUIdentity = true;
							        		break;
							        	}
							        }
							        brSecondShortest.close();
						        
							        if (foundOTUIdentity){
							        	//found it in the second cache, not the first
							        	//process it properly
							        	//found it in the first cache
							        	//process it properly according to which cache it is
							        	
							        	if (fileNameSecondShortest.equals(fileNameCorrect)){
								        	foundIt = true;
							        	} else if (fileNameSecondShortest.equals(fileNameAmbiguous)){
							        		foundIt = true;
							        	} else if (fileNameSecondShortest.equals(fileNameIncorrect)){
							        		foundIt = false;
							        	} else {
							        		System.out.println("ERROR: Second shortest BLAST Cache was not correctly set!");
							        		System.exit(0);
							        	}
							        	//we found this OTU, we're done this loop iteration now
							        	if (foundIt) break;
							        	else continue;
							        } else{
							        	//didn't find it in either cache... it belongs in the largest cache
							        	//process it properly
							        	//all done with this iteration
							        	if (fileNameLongest.equals(fileNameCorrect)){
							        		foundIt = true;
							        	} else if (fileNameLongest.equals(fileNameAmbiguous)){
							        		foundIt = true;
							        	} else if (fileNameLongest.equals(fileNameIncorrect)){
							        		foundIt = false;
							        	} else {
							        		System.out.println("ERROR: Second shortest BLAST Cache was not correctly set!");
							        		System.exit(0);
							        	}
							        	if (foundIt) break;
							        	else continue;
							        }
						        }
							}
						}
						if (foundIt) bw.write("," + 1);
						else bw.write("," + 0);
					
						br.close();
					}catch (FileNotFoundException e)
					{
						//Couldn't read the file (it doesn't exist)
						bw.write("," + 0);
					}
					
				}
				bw.write("\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
