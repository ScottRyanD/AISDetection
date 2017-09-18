#!/bin/bash
# first argument: the directory we're working with (make sure to move the spiking java program, blank, usearch, all that stuff)
#/work/nrap1125/Ryan/CAISN/AibinSpiking/ArtAmpSpikingClean/
# second argument: "Reads"
# third argument: taxon
# fourth argument: which number of sequences we're starting at
for (( j=$4; j < 102; j++ )) #sequences spiked in
do
	if [ ! -f SpikingResultFull${j}-${3}.csv ]; then	#file doesn't exist
		for (( i=0; i < 25; i++ ))	#replicates
		do
			#spike the sequences into the sample using the spiker program
			java -Xmx1024m -Xms256m Spiker ${1}${3}.fq ${j} $1 ${3}
			cat SampleToSpike${3}.fq >> CompleteSample${i}-${j}-${3}.fq
			cat ${1}${2}.fq >> CompleteSample${i}-${j}-${3}.fq
			cat SampleToSpike${3}.fa >> CompleteSample${i}-${j}-${3}.fa
			cat ${1}${2}.fa >> CompleteSample${i}-${j}-${3}.fa
			
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.5" "99" "2" "clustering" $i
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "2.0" "99" "2" "clustering" $i
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.0" "99" "1" "clustering" $i
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "1.5" "99" "1" "clustering" $i
			
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.5" "100" "2" "clustering" $i
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "2.0" "100" "2" "clustering" $i
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "350" "10" "2.5" "100" "2" "clustering" $i
			./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.0" "100" "1" "clustering" $i
			
			./denoise_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "1.5" "8" "2" "denoising" $i
			./denoise_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "3.0" "8" "1" "denoising" $i
			./denoise_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "2.0" "8" "2" "denoising" $i
			./denoise_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "2.5" "8" "1" "denoising" $i
			
			rm CompleteSample${i}-${j}-${3}.fq; rm SampleToSpike${3}.fq; rm CompleteSample${i}-${j}-${3}.fa; rm SampleToSpike${3}.fa
			echo "$i $j COMPLETED!"
		done
		java -Xmx1024m -Xms256m FTISS $j $1 $3
		rm OTUs*/CompleteSample*-${j}-${3}_otus.fa
	else												#file does exist
		cmp SpikingResultFull${j}-${3}.csv blank

		if [ "$?" -eq 0 ]; then							#the file is  empty	
			for (( i=0; i < 25; i++ ))	#replicates
			do
				#spike the sequences into the sample using the spiker program
				java -Xmx1024m -Xms256m Spiker ${1}${3}.fq ${j} $1 ${3}
				cat SampleToSpike${3}.fq >> CompleteSample${i}-${j}-${3}.fq
				cat ${1}${2}.fq >> CompleteSample${i}-${j}-${3}.fq
				cat SampleToSpike${3}.fa >> CompleteSample${i}-${j}-${3}.fa
				cat ${1}${2}.fa >> CompleteSample${i}-${j}-${3}.fa
				
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.5" "99" "2" "clustering" $i
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "2.0" "99" "2" "clustering" $i
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.0" "99" "1" "clustering" $i
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "1.5" "99" "1" "clustering" $i
				
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.5" "100" "2" "clustering" $i
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "2.0" "100" "2" "clustering" $i
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "350" "10" "2.5" "100" "2" "clustering" $i
				./cluster_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "1.0" "100" "1" "clustering" $i
				
				./denoise_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "1.5" "8" "2" "denoising" $i
				./denoise_runner.sh CompleteSample${i}-${j}-${3} "375" "10" "3.0" "8" "1" "denoising" $i
				./denoise_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "2.0" "8" "2" "denoising" $i
				./denoise_runner.sh CompleteSample${i}-${j}-${3} "400" "10" "2.5" "8" "1" "denoising" $i
				
				rm CompleteSample${i}-${j}-${3}.fq; rm SampleToSpike${3}.fq; rm CompleteSample${i}-${j}-${3}.fa; rm SampleToSpike${3}.fa
				echo "$i $j COMPLETED!"
			done
			java -Xmx1024m -Xms256m FTISS $j $1 $3
			rm OTUs*/CompleteSample*-${j}-${3}_otus.fa
		fi
	fi
done
