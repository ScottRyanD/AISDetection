#!/bin/bash
# produce a stats log for quality and length filtering
# usearch -fastq_stats $1.fq -log stats.log

# quality and length filtering
echo "Quality and length filtering..."
echo "MEE and Q filter selected!"
usearch -fastq_filter ${1}.fq -fastq_trunclen $2 -fastq_truncqual $3  -fastq_maxee $4  -fastaout filtered_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa

# usearch -fastq_filter input.fq -fastq_trunclen 250 -fastq_maxee 0.5 -fastq_truncqual 15 -fastaout filtered_reads.fa
#if [ "$3" = "n" ]; then
#	echo "No Q filter selected"
#	if [ "$4" = "n" ]; then
#		echo "No MEE filter selected!"
#		usearch -fastq_filter $1.fq -fastq_trunclen $2 -fastaout filtered_${1}-${7}.fa
#	else
#		usearch -fastq_filter $1.fq -fastq_trunclen $2 -fastq_maxee $4 -fastaout filtered_${1}-${7}.fa
#	fi
#else
#	if [ "$4" = "n" ]; then
#		echo "No MEE filter selected!"
#		usearch -fastq_filter $1.fq -fastq_trunclen $2 -fastq_truncqual $3 -fastaout filtered_${1}-${7}.fa
#	else
#		echo "MEE and Q filter selected!"
#		usearch -fastq_filter $1.fq -fastq_trunclen $2 -fastq_truncqual $3  -fastq_maxee $4  -fastaout filtered_${1}-${7}.fa
#	fi
#fi

# dereplication
echo "Dereplicating..."
usearch -fastx_uniques filtered_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa -minuniquesize ${6} -fastaout derep_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa -sizeout

# sort abundance without unique samples
echo "Sorting and removing unique samples..."
usearch -sortbysize derep_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa -fastaout sorted_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa -minsize ${6}

# denoise these samples
echo "denoising with minimum abundance $5"
usearch -unoise3 sorted_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa -minsize $5 -ampout ${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}_otus.fa

echo "Denoising complete!"

cmp ${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}_otus.fa blank

if [ "$?" -ne 0 ]; then
	echo "We have OTUs to work with!"

	# looking for chimeras
	# echo "Looking for chimeras..."
	#usearch -uchime_ref ${1}-${7}_otus.fa -db $1.fa -strand plus -nonchimeras ${1}-${7}_nonchimeric_otus.fa

	# labelling OTUs
	# echo "Labelling the OTUs..."
	#python /work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/fasta_number.py ${1}_nonchimeric_otus.fa OTU_ > ${1}_OTUs_labeled.fa

	# mapping reads
	# echo "Mapping reads back to OTUs..."
	#usearch -usearch_global $1.fa -db ${1}_OTUs_labeled.fa -strand plus -id 0.$5 -uc mapped_$1.uc

	# creating OTU table
	# echo "Creating OTU table..."
	#python /work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/uc2otutab.py mapped_$1.uc > otu_table_$1.txt
else
	#we need a file, even an empty one
	#we have no sequences getting through the pipeline to generate OTUs here
	#this will allow further processing to occur successfully
	echo "No OTUs to work with! :("
	touch ${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}_otus.fa
fi

if [ ! -f ${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}_otus.fa ]; then
	#we need a file, even an empty one
	#we have no sequences getting through the pipeline to generate OTUs here
	#this will allow further processing to occur successfully
    touch ${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}_otus.fa
fi

# echo "Complete."

# organize everything
# echo "Moving files to their appropriate places..."
mkdir OTUs_trunc_${2}_quality_${3}_mee_${4}_radOrMinNum_${5}_minAbundance_${6}_${7}

#mv mapped_$1.uc /work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/Tables_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles
#mv otu_table_$1.txt /work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/Tables_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles

#mv $1_OTUs_labeled.fa /work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/OTUs_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles
#rm ${1}-${7}_otus.fa #/work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/OTUs_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles
mv ${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}_otus.fa OTUs_trunc_${2}_quality_${3}_mee_${4}_radOrMinNum_${5}_minAbundance_${6}_${7}/${1}_otus.fa
#rm ${1}-${7}_otus_init.fa #/work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/OTUs_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles

rm filtered_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa #/work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/Data_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles
rm derep_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa #/work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/Data_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles
rm sorted_${1}_${2}_${3}_${4}_${5}_${6}-${7}${8}.fa #/work/nrap1125/Ryan/CAISN/Rarefaction/Rarefaction$6/Data_trunc_${2}_quality_${3}_mee_${4}_rad_${5}_no_singles
#echo "Complete."
