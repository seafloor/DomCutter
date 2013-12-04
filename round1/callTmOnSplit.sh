#!/bin/bash -f
#Call script with the dir containing the subdirs for all the split CASP files and the dir with fasta files
#Will call ModFOLDclust2 on each of these
#call as: bash callTmOnSplit.sh /home/matt/project/data/split_doms/ /home/matt/project/data/CASP10_split_structures_official/ /home/matt/project/data/rawData/

echo "called with the directory $1"
echo "the native structure dir is $2"
echo "the output dir is $3"

FILES=$1*
REGEX="dom_([0-9]?-?)"
REGEX2="GDT-TS-score=\s(0.\d{4})\s*"
i=0
multiDomains="T0651 T0658 T0663 T0671 T0674 T0677 T0684 T0685 T0686 T0690 T0693 T0705 T0717 T0726 T0732 T0739 T0756" 
for folder in $FILES; do
    filename=$(basename "$folder")
    for splitdoms in $folder/*; do
        #echo $splitdoms
        if [[ $splitdoms =~ dom_([0-9]?-?) ]]; then
            #echo ${BASH_REMATCH[1]}
            domain=${BASH_REMATCH[1]}
            native=$2${filename}-D${domain}.pdb
            if [[ $multiDomains =~ $filename ]]; then 
                echo $splitdoms
                echo $native
                for path in $splitdoms/*; do
                    modelname=$(basename $path)
                    echo "checking $modelname"
                    java -jar TMscore.jar $path $native >"$3${filename}_${domain}_${modelname}.GDT"
                    GDTline=$(grep GDT-TS "$3${filename}_${domain}_$modelname.GDT")
                    #echo $GDTline
                    if [[ $GDTline =~ GDT-TS-score=[[:space:]]+([0-9].[0-9]{4})[[:space:]] ]]; then
                        score=${BASH_REMATCH[1]}
                        #echo $score
                    else
                        score=""
                    fi
                    if [[ i==0 ]]; then
                        echo "Target,Domain,Model,GDTscore" >>"$3GDTscoresForSplit.csv"
                    fi
                    let i++
                    line="$filename,$domain,$modelname,$score"
                    echo "$line" >>"$3GDTscoresForSplit.csv"
                done
            else
                echo "didn't run TMscore ---target only contains one domain!"
            fi
        fi
    done
done

exit 0
