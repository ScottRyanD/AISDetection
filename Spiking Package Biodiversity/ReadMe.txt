This is all of the code used for performance testing for parameter sets optimized for estimation of biodiversity.

SpikeItRunner.sh is the entrypoint to the program.
SpikeIt.sh is essentially the main loop. It spikes 1 sequence of a taxon into a community, and then another, and so on.
FTISS is a program that "Finds the Target in the Spiked Sample". It determines if the target could, indeed, be recovered.
cluster_runner.sh and denoise_runner.sh run the actual UPARSE pipelines.
Spiker.sh generates a sample consisting of the community sample and the spiked, randomly selected sequences.

Directory names were mostly hard-coded so they will need to be modified if you intend to run this code.