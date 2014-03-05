#!/bin/bash -f
#Call script with the dir containing the subdirs for all the split CASP files and the dir with fasta files
#Will call ModFOLDclust2 on each of these
#call as: bash callModfoldOnSplit.sh /home/matt/project/data/split_doms/ /home/matt/project/data/fasta_doms/

echo "called with the directory $1"
echo "the fasta file dir is $2"

FILES=$1*
REGEX="dom_([0-9]?-?)"
multiDomains="T0726 T0739 T0756" 

for folder in $FILES; do
    filename=$(basename "$folder")
    for splitdoms in $folder/*; do
        #echo $splitdoms
        if [[ $splitdoms =~ $REGEX ]]; then
            #echo ${BASH_REMATCH[1]}
            domain=${BASH_REMATCH[1]}
            fasta="$2${filename}_${domain}.fasta"
            if [[ $multiDomains =~ $filename ]]; then 
                echo "run ModFOLDclust2"
                #echo $fasta
                #echo $splitdoms/
                echo $filename
                java -jar ModFOLDclust2.jar $filename $fasta $splitdoms/
            else
                echo "didn't run ModFOLDclust2 ---target only contains one domain!"
            fi
        fi
    done
done

exit 0
