#!/bin/bash -f
#Call script with the dir containing the subdirs for all the split CASP files and the dir with fasta files
#Will call ModFOLDclust2 on each of these
#call as: bash ModFOLDtoCSV.sh /home/matt/project/data/split_doms/ /home/matt/project/data/rawData/round1output/splitModFoldscores/

echo "called with the directory $1"
echo "the output dir is $2"

FILES=$1*
REGEX="dom_([0-9]?-?)"
REGEX2="GDT-TS-score=\s(0.\d{4})\s*"
i=0
multiDomains="T0651 T0658 T0671 T0677 T0686 T0690 T0705 T0717 T0726" 
for folder in $FILES; do
    filename=$(basename "$folder")
    for splitdoms in $folder/*; do
        #echo $splitdoms
        if [[ $splitdoms =~ dom_([0-9]?-?) ]]; then
            #echo ${BASH_REMATCH[1]}
            domain=${BASH_REMATCH[1]}
            if [[ $multiDomains =~ $filename ]]; then 
                echo $splitdoms
                for path in $splitdoms/*; do
                    modelname=$(basename $path)
                    echo "checking $modelname"
                    #java -jar TMscore.jar $path $native >"$3${filename}_${domain}_${modelname}.GDT"
                    echo "searching $1${filename}/dom_${domain}/${filename}_ModFOLDclust2.sort"
                    ModFOLDline=$(grep $modelname "$1${filename}/dom_${domain}/${filename}_ModFOLDclust2.sort")
                    #echo $ModFOLDline
                    if [[ $ModFOLDline =~ ${modelname}[[:space:]]+([0-9].[0-9]+)[[:space:]] ]]; then
                        score=${BASH_REMATCH[1]}
                        #echo "score is $score"
                        if [[ $i -eq 0 ]]; then
                            echo "Target,Domain,Model,ModFOLDscore" >>"$2ModFOLDscoresForSplit.csv"
                            i=1
                            echo $i
                        fi
                        line="$filename,$domain,$modelname,$score"
                        echo "$line" >>"$2ModFOLDscoresForSplit.csv"
                    else
                        score=""
                    fi
                done
                score=""
            else
                echo "didn't run TMscore ---target only contains one domain!"
            fi
        fi
    done
done

exit 0
