#!/bin/bash

echo "starting them up"
for length in "300" "325" "350" "375" "400"; do
	for meefilter in "1.0" "1.5" "2.0" "2.5" "3.0"; do
		sqsub -q serial -r 2d -o EROut1-${length}${meefilter}.txt --mpp 4g ./EverythingRunner1.sh ${length} ${meefilter}
		sqsub -q serial -r 2d -o EROut2-${length}${meefilter}.txt --mpp 4g ./EverythingRunner2.sh ${length} ${meefilter}
	done
done
