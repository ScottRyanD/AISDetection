#!/bin/bash

for taxon in Artemia Balanus Brachionus Cancer Carcinus Cercopagis Corbicula Daphnia Diacyclops Echinogammarus Epischura Leptodora Mesocyclops Microstella Oikopleura Paleomonetes Pleuroxus Quagga Senecella Themisto; do
	java -Xmx1024m -Xms256m Program /work/nap_9022/Ryan/CAISN/2017Optimization100/${taxon}.fq 100 /work/nap_9022/Ryan/CAISN/2017Optimization100/ ${taxon}
done
