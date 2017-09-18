import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Program {

	public static void main(String[] args) {

		ArrayList<String> taxa = new ArrayList<String>();
		taxa.add("Artemia");
		taxa.add("Balanus");
		taxa.add("Brachionus");
		taxa.add("Cancer");
		taxa.add("Cercopagis");
		taxa.add("Corbicula");
		taxa.add("Daphnia");
		taxa.add("Diacyclops");
		taxa.add("Echinogammarus");
		taxa.add("Epischura");
		taxa.add("Leptodora");
		taxa.add("Mesocyclops");
		taxa.add("Microstella");
		taxa.add("Oikopleura");
		taxa.add("Paleomonetes");
		taxa.add("Pleuroxus");
		taxa.add("Senecella");
		taxa.add("Themisto");
		taxa.add("Quagga");
		taxa.add("Carcinus");
		
		
		String length = args[0];
		String qFilter = args[1];
		String mee = args[2];
		String radiusOrNMA = args[3];
		String minAbundance = args[4];
		String clusteringOrNR = args[5];
		
		BufferedWriter out = null;
		String filenameout = null;
		
		FileWriter fstream;
		try {
			filenameout = "/global/b/work/rrg-rgras-ad/Ryan/CAISN/2017OptimizationFull/OTUCounts_" + length + "_" + qFilter + "_" + mee + "_" + radiusOrNMA + "_" + minAbundance + "_" + clusteringOrNR +  ".csv";
			System.out.println("\tLooking at parameter set " + length + " " + qFilter + " " + mee + " " + radiusOrNMA + " " + minAbundance + " " + clusteringOrNR + "...\n");

			BufferedWriter bw = new BufferedWriter(new FileWriter(filenameout));
			for (int i = 0; i < 1; i++){
				
				int [] numberOfCorrect = new int[taxa.size()];
				int [] numberOfAmbiguous = new int[taxa.size()];
				int [] numberOfIncorrect = new int[taxa.size()];
				
				System.out.println("Processing run " + i + "...\n");
				fstream = new FileWriter(filenameout, true);
			    out = new BufferedWriter(fstream);
			    //going to write correct, ambiguous correct, incorrect, redundant		
			    String filenameOTUs = "/global/b/work/rrg-rgras-ad/Ryan/CAISN/2017OptimizationFull/OptimizationSample" + "_" + length + "_" + qFilter + "_" + mee + "_" + radiusOrNMA + "_" + minAbundance + "-" + clusteringOrNR + i + "_otus.fa"; //gotta get the OTUs
				
				
				
				//for each OTU sequence, search the smallest file first
					//find it? Increase the respective count
					//don't find it.. search the second one
						//find it? Increase the respective count
						//don't find it? it's in the third one
				
			    //get all of the OTUs
				BufferedReader br = new BufferedReader(new FileReader(filenameOTUs));
				ArrayList<String> OTUs = new ArrayList<String>();
				String line;
		        while ((line = br.readLine()) != null) {
		        	if (line.contains("barcodelabel")){ //make sure in the denoise files it's not a chimera
		        		if (clusteringOrNR.equals("denoising")){ //it's denoising, need to make sure that we are adding a good OTU
		        			if (line.contains("amptype=otu")){ //not a chimera, at it
		        				OTUs.add(line);
		        			}
		        		} else{	//it's clustering, just add it
		        			OTUs.add(line);
		        		}
		        	}
		        	//assuming every line in the OTU file is just the sequence identifier
		        }
		        br.close();
		        

		        //So now we have all of the OTUs... need to check the caches now
		        //The caches have strings we can compare to the OTUs lines
		        
		        for(String OTU: OTUs){ //iterate through each OTU
		        	String taxon = "";
		        	int taxonIndex = -1;
		        	//find out which taxon's BLAST Cache we're using
		        	for (int v = 0; v < taxa.size(); v++){
		        		if (OTU.contains(taxa.get(v))){
		        			taxon = taxa.get(v);
		        			taxonIndex = v;
		        			break;
		        		}
		        	}
		        	if (taxon == "" || taxonIndex == -1) System.out.println("ERROR!");
		        	
		        	
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
			        	if (OTU.contains(cacheLine)){
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
				        	numberOfCorrect[taxonIndex]++;
			        	} else if (fileNameShortest.equals(fileNameAmbiguous)){
			        		numberOfAmbiguous[taxonIndex]++;
			        	} else if (fileNameShortest.equals(fileNameIncorrect)){
			        		numberOfIncorrect[taxonIndex]++;
			        	} else {
			        		System.out.println("ERROR: Shortest BLAST Cache was not correctly set!");
			        		System.exit(0);
			        	}
			        	//we found this OTU, we're done this loop iteration now
			        	continue;
			        } else{
			        	//haven't yet found it
						BufferedReader brSecondShortest = new BufferedReader(new FileReader(fileNameSecondShortest));
						
				        while ((cacheLine = brSecondShortest.readLine()) != null) {
				        	if (OTU.contains(cacheLine)){
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
					        	numberOfCorrect[taxonIndex]++;
				        	} else if (fileNameSecondShortest.equals(fileNameAmbiguous)){
				        		numberOfAmbiguous[taxonIndex]++;
				        	} else if (fileNameSecondShortest.equals(fileNameIncorrect)){
				        		numberOfIncorrect[taxonIndex]++;
				        	} else {
				        		System.out.println("ERROR: Second shortest BLAST Cache was not correctly set!");
				        		System.exit(0);
				        	}
				        	//we found this OTU, we're done this loop iteration now
				        	continue;
				        } else{
				        	//didn't find it in either cache... it belongs in the largest cache
				        	//process it properly
				        	//all done with this iteration
				        	if (fileNameLongest.equals(fileNameCorrect)){
				        		numberOfCorrect[taxonIndex]++;
				        	} else if (fileNameLongest.equals(fileNameAmbiguous)){
				        		numberOfAmbiguous[taxonIndex]++;
				        	} else if (fileNameLongest.equals(fileNameIncorrect)){
				        		numberOfIncorrect[taxonIndex]++;
				        	} else {
				        		System.out.println("ERROR: Second shortest BLAST Cache was not correctly set!");
				        		System.exit(0);
				        	}
				        }
			        }
		        }
		        int totalCorrect = 0;
		        int totalAmbiguous = 0;
		        int totalIncorrect = 0;
		        int totalRedundant = 0;
		        //Here we should actually process what we've found
		        //For each taxon, if it was found as correct, add (numberCorrect - 1) to redundant and (numberAmbiguous) to redundant
		        	//If it was not found as correct but was found as ambiguous, add (numberAmbiguous - 1) to redundant
			        	//If it was not found as correct or ambiguous, we have no redundant
		        
		        for (String t: taxa){
		        	int index = taxa.indexOf(t);
		        	if (numberOfCorrect[index] >= 1){
		        		int numberRedundantCorrect = numberOfCorrect[index] - 1;
		        		numberOfCorrect[index] = 1;
		        		totalRedundant += numberRedundantCorrect;
		        		int numberRedundantAmbiguous = numberOfAmbiguous[index];
		        		numberOfAmbiguous[index] = 0;
		        		totalRedundant += numberRedundantAmbiguous;
		        	}
		        	else if (numberOfAmbiguous[index] >= 1){
		        		int numberRedundantAmbiguous = numberOfAmbiguous[index] - 1;
		        		numberOfAmbiguous[index] = 1;
		        		totalRedundant += numberRedundantAmbiguous;
		        	}
		        	
		        	totalCorrect += numberOfCorrect[index];
		        	totalAmbiguous += numberOfAmbiguous[index];
		        	totalIncorrect += numberOfIncorrect[index];
		        	//redundant is taken care of, see above
		        }
			    
		        if (totalCorrect + totalAmbiguous > taxa.size()) System.out.println("ERROR! TOO MANY CORRECT + AMBIGUOUS!");
		        
		        bw.write("" + totalCorrect + " " + totalAmbiguous + " " + totalIncorrect + " " + totalRedundant + "\n");				
			}
			
			bw.close();
		    //write in out correct, ambiguous correct, incorrect, redundant
			out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
