#!/bin/bash -f
#Call script with the dir containing the subdirs for all the split CASP files and the dir with fasta files
#Will call ModFOLDclust2 on each of these
#call as: bash callTmOnFull.sh /home/matt/project/data/CASP10_server_models/ /home/matt/project/data/CASP10_full_structures_official/ /home/matt/project/data/rawData/round0output/fullChainGDTscores/ 

echo "called with the directory $1"
echo "the native structure dir is $2"
echo "the output dir is $3"

FILES=$1*
REGEX="dom_([0-9]?-?)"
REGEX2="GDT-TS-score=\s(0.\d{4})\s*"
i=0
multiDomains="T0651 T0658 T0671 T0677 T0686 T0690 T0705 T0717 T0726" 
for folder in $FILES; do
    filename=$(basename "$folder")
    native=$2${filename}.pdb
    if [[ $multiDomains =~ $filename ]]; then 
        echo $native
        for path in $folder/*; do
            modelname=$(basename $path)
            echo "checking $modelname"
            #echo $path
            java -jar TMscore.jar $path $native >"$3${filename}_${modelname}.GDT"
            GDTline=$(grep GDT-TS "$3${filename}_$modelname.GDT")
            #echo $GDTline
            if [[ $GDTline =~ GDT-TS-score=[[:space:]]+([0-9].[0-9]{4})[[:space:]] ]]; then
                score=${BASH_REMATCH[1]}
                #echo $score
                if [[ $i -eq 0 ]]; then
                    echo "Target,Model,GDTscore" >>"$3GDTscoresFullChain.csv"
                fi
                let i++
                line="$filename,$modelname,$score"
                echo "$line" >>"$3GDTscoresFullChain.csv"
            fi
            score=""
            GDTline=""
        done
    else
        echo "didn't run TMscore ---target only contains one domain!"
    fi
done

exit 0
