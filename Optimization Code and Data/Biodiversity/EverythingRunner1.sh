#!/bin/bash


echo "running with clustering"

for minnum in "1" "2"; do
	for qfilter in "10" "20" "30"; do
		for clusteringradius in "97" "98" "99" "100"; do
			for i in `seq 0 0`; do
				./cluster_runner.sh OptimizationSample $1 $qfilter $2 $clusteringradius $minnum "clustering" $i
			done
			#all 100 are complete, this is where I need to run the program to check OTU classes
			java -Xmx1024m -Xms256m Program $1 $qfilter $2 $clusteringradius $minnum "clustering"
			rm OptimizationSample_${1}_${qfilter}_${2}_${clusteringradius}_${minnum}-clustering*_otus.fa
		done
	done
done

echo "Done!"

