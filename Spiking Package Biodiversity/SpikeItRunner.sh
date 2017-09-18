#!/bin/bash
#pass it the directory we're working on /work/nrap1125/Ryan/CAISN/AibinSpiking/ArtAmpSpikingClean/
declare -a array1=("Artemia" "Balanus" "Brachionus" "Cancer" "Cercopagis" "Corbicula" "Daphnia" "Diacyclops" "Echinogammarus" "Epischura" "Leptodora" "Mesocyclops" "Microstella" "Oikopleura" "Paleomonetes" "Pleuroxus" "Senecella" "Themisto" "Carcinus" "Quagga")

arraylength1=${#array1[@]}
		
for (( i=0; i<${arraylength1}; i++ )); do
	sqsub -q serial -r 7d -o SpikeItOut1${array1[$i]}.txt --mpp 4g ./SpikeIt.sh $1 Reads ${array1[$i]} 1
done

