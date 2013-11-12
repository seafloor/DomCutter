#!/bin/bash -f
#Call script with the dir containing the subdirs for all the split CASP files and the dir with fasta files
#Will call ModFOLDclust2 on each of these
#call as: bash call_modFOLDclust2_split.sh /home/matt/project/data/split_doms/ /home/matt/project/data/fasta_doms/

echo "called with the directory $1"
echo "the fasta file dir is $2"
FILES=$1*
REGEX="dom_([0-9]?-?)"
for folder in $FILES; do
    filename=$(basename "$folder")
    for splitdoms in $folder/*; do
        #echo $splitdoms
        if [[ $splitdoms =~ $REGEX ]]; then
            #echo ${#BASH_REMATCH[*]}
            for (( i=0; i<${#BASH_REMATCH[@]}; i++)); do
                #echo "${BASH_REMATCH[i]}"
                for fasta in $2*; do
                    if [[ $fasta == *$filename* && $fasta == *${BASH_REMATCH[i]}.fasta ]]; then 
                        echo $filename
                        #echo $fasta
                        #echo "$splitdoms/"
                        if [ $filename != "T0644" -a $filename != "T0645" ]; then
                            #echo "run ModFOLDclust"
                            java -jar ModFOLDclust2.jar $filename $fasta $splitdoms/
                        else
                            echo "didn't run ModFOLDclust ---model is T0644 or T0645!"
                            echo "$filename"
                            echo "$splitdoms/"
                        fi
                    fi
                done
            done
        fi
    done
done

exit 0
