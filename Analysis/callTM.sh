#!/bin/sh -f
# Call this as "scriptname model_directory native_structure"
# It will return TMscore output as .txt files for each model
# path for official structures: ~/Dropbox/part_3_project/project/data/CASP10_full_structures_official/
# path for CASP folder: ~/Dropbox/part_3_project/project/data/CASP10_server_models/
# output is set to ~/Dropbox/part_3_project/project/data/fullTMscores/

echo "the directory for models is $1"
echo "the native structure path is $2" 
echo "the output dir is $3"
echo "the model name is $4"

#array of files which won't be used for now, because they throw problems with handling "-"
#arr=(T0blah T0blahblah etc.)

#dir=$1
#if [ -e "$2" ]; then
#    echo $2 native structure does exist
#else
#    echo $2 native structure does not exist
#fi

if [ -e "$2" ]; then
    for path in $1*; do
        echo $path
        filename=$(basename $path)
        #echo checking $filename
        java -jar TMscore.jar $path $2 >"$path.GDT"
        echo -n "$filename " >>"$3$4GDTscores.txt"
        grep GDT-TS "$path.GDT" >>"$3$4GDTscores.txt"
        #echo done
        #echo native structure exists
    done
else
    echo native structure for $4 does not exist
fi

exit 0
