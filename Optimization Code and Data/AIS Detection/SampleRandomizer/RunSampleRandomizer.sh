#!/bin/bash

for taxon in Artemia Balanus Brachionus Cancer Carcinus Cercopagis Corbicula CR17 Daphnia Diacyclops Echinogammarus Epischura Leptodora Mesocyclops Microstella Oikopleura Paleomonetes Pleuroxus Quagga Senecella Themisto; do
	java -Xmx1024m -Xms256m Program /work/nap_9022/Ryan/CAISN/2017Optimization/${taxon}.fq 50 /work/nap_9022/Ryan/CAISN/2017Optimization/ ${taxon}
done