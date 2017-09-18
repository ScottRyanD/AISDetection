#!/bin/bash


echo "running with denoising"

for minnum in "1" "2"; do
	for qfilter in "10" "20" "30"; do
		for minabundance in "2" "4" "8"; do
			for i in `seq 0 99`; do
				./denoise_runner.sh OptimizationSample $1 $qfilter $2 $minabundance $minnum "denoising" $i
			done
			#all 100 are complete, this is where I need to run the program to check OTU classes
			java -Xmx1024m -Xms256m Program $1 $qfilter $2 $minabundance $minnum "denoising"
			rm OptimizationSample_${1}_${qfilter}_${2}_${minabundance}_${minnum}-denoising*_otus.fa
		done
	done
done

echo "Done!"

