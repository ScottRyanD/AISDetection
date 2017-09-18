import java.util.*;
import java.io.*;
public class Program {
	
	public static void main(String[] args) throws Exception {
		for (int y = 0; y < 100; y++){
			//first pass it the name of the input file, secondly the number of sequences we need, third the file name
			List<Sample> sampleList = new ArrayList<Sample>();	//make an array of samples to output
			
			String input = args[0]; //get the input file, as an FQ
			int numberOfSequences = Integer.parseInt(args[1]); //how many sequences are we going to randomly select
			String outputFileDirectory = args[2];	//where are we going to put these sequences
			String taxon = args[3];	//name of taxon
	
			System.out.println("Input file: " + input);
			System.out.println("Output file: " + outputFileDirectory);
			System.out.println("Taxon and replicate: " + taxon);
			
			BufferedReader br = new BufferedReader(new FileReader(input));
			while (br.ready()){	//read in the input file (an FQ file)
				String header = new String(br.readLine());
				String sequence = new String(br.readLine());
				String plusSign = new String(br.readLine());
				String quality = new String(br.readLine());
				Sample newSample = new Sample(header, sequence, plusSign, quality);
	
				sampleList.add(newSample); //adding all samples from the original sample to this list
			}
			br.close();
	
			String filenameFQ = outputFileDirectory + "OptimizationSample" + y + ".fq";
		    FileWriter fstreamFQ = new FileWriter(filenameFQ, true);
		    BufferedWriter outFQ = new BufferedWriter(fstreamFQ); //setting up the FQ output
		    
			String filenameFA = outputFileDirectory + "OptimizationSample" + y + ".fa";
		    FileWriter fstreamFA = new FileWriter(filenameFA, true);
		    BufferedWriter outFA = new BufferedWriter(fstreamFA); //setting up the FA output
		    	
			for (int z = 0; z < numberOfSequences; z ++){
				try {
					Random rand = new Random();
					int random = rand.nextInt(sampleList.size());
					outFQ.write(sampleList.get(random).Header + "\n");
					outFQ.write(sampleList.get(random).Sequence + "\n");
					outFQ.write(sampleList.get(random).PlusSign + "\n");
					outFQ.write(sampleList.get(random).Quality + "\n");
					
					String moddedHeader = ">" + sampleList.get(random).Header.substring(1);
					outFA.write(moddedHeader + "\n");
					outFA.write(sampleList.get(random).Sequence + "\n");
					System.out.println("printed out sample " + z);
					sampleList.remove(random);
				}
				catch (IOException e){
				    System.err.println("Error: " + e.getMessage());
				}
			}
			outFQ.close();
			outFA.close();
		}
	}
}
