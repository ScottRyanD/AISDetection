import java.util.*;
import java.io.*;
public class Spiker {
	
	public static void main(String[] args) throws Exception {
		//first pass it the name of the input file, secondly the number of sequences we need
		//third the output file directory
		//fourth the taxon name
		List<Sample> innoculationList = new ArrayList<Sample>();
		
		String input = args[0];
		int numberOfSequences = Integer.parseInt(args[1]);
		String outputFileDirectory = args[2];
		String appendToResult = args[3];
		
		BufferedReader br = new BufferedReader(new FileReader(input));
		while (br.ready()){
			String header = new String(br.readLine());
			String sequence = new String(br.readLine());
			String plusSign = new String(br.readLine());
			String quality = new String(br.readLine());
			Sample newSample = new Sample(header, sequence, plusSign, quality);

			innoculationList.add(newSample);
		}
		br.close();

		String filenameFQ = outputFileDirectory + "SampleToSpike" + appendToResult + ".fq";
	    FileWriter fstreamFQ = new FileWriter(filenameFQ, true);
	    BufferedWriter outFQ = new BufferedWriter(fstreamFQ);
	    
		String filenameFA = outputFileDirectory + "SampleToSpike" + appendToResult + ".fa";
	    FileWriter fstreamFA = new FileWriter(filenameFA, true);
	    BufferedWriter outFA = new BufferedWriter(fstreamFA);
	    	
		for (int z = 0; z < numberOfSequences; z ++){
			try {
				Random rand = new Random();
				int random = rand.nextInt(innoculationList.size());
				outFQ.write(innoculationList.get(random).Header + "\n");
				outFQ.write(innoculationList.get(random).Sequence + "\n");
				outFQ.write(innoculationList.get(random).PlusSign + "\n");
				outFQ.write(innoculationList.get(random).Quality + "\n");
				
				String moddedHeader = ">" + innoculationList.get(random).Header.substring(1);
				outFA.write(moddedHeader + "\n");
				outFA.write(innoculationList.get(random).Sequence + "\n");
				System.out.println("printed out sample " + z);
				innoculationList.remove(random);
			}
			catch (IOException e){
			    System.err.println("Error: " + e.getMessage());
			}
		}
		outFQ.close();
		outFA.close();
	}
}
