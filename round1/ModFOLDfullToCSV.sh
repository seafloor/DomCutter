#!/bin/bash -f
# call with the CASP10 dir, dir for ModFOLDclust2 output files and the output dir
#e.g. bash ModFOLDfullToCSV.sh /home/matt/project/data/CASP10_server_models/ /home/matt/project/data/rawData/round0output/fullChainModFoldscores/modFoldClustScores/ /home/matt/project/data/rawData/round0output/fullChainModFoldscores/asCSV/

echo "$1 is the CASP10 dir"
echo "$2 is the ModFOLDclust files dir"
echo "$3 is the output dir"

multiDomains="T0651 T0658 T0671 T0677 T0686 T0690 T0705 T0717 T0726" 
i=0

for folder in $1*; do
    target=$(basename "$folder")
    if [[ $multiDomains =~ $target ]]; then
        for path in $folder/*; do
            model=$(basename "$path")
            echo "moving $model"
            ModFOLDline=$(grep $model "$2${target}_ModFOLDclust2.sort")
            if [[ $ModFOLDline =~ ${model}[[:space:]]+([0-9].[0-9]+)[[:space:]] ]]; then
                score=${BASH_REMATCH[1]}
                if [[ $i -eq 0 ]]; then
                    echo "Target,Model,ModFOLDscore" >>"$3ModFOLDscoresFullChain"
                    i=1
                fi
                line="$target,$model,$score"
                echo "$line" >>"$3ModFOLDscoresFullChain"
                score=""
                ModFOLDline=""
            fi
        done
    else
        echo "didn't move the score - $target only has one domain"
    fi
done
