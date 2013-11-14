#!/usr/bin/perl
#call this with the CASP directory containing all the directories and fasta files
#it will call ModFOLDclust2 on each of the subdirectories with the associated fasta file
use strict;
use 5.010;

my @files;
my $name;
my $folder;
my $fasta;
my $skipFile;

say("the directory is $ARGV[0]");
opendir (DIR, $ARGV[0]) or die "couldn't open the directory";
while (my $file = readdir(DIR)) {
    #say("$file");
    push(@files, $file);
}
@files=sort(@files);
#say("@files");

foreach(@files) {
    if($_ =~ /(T\d+)\..*/) {
        #say("$_");
    } elsif($_ =~ /\./) {
        #say("$_");
    } else {
        $name = $_;
        $folder = "$ARGV[0]$_/";
        $fasta = "$ARGV[0]$_.fasta";
        say("Checking $name");

        #skip this loop if the ModFOLDclust2 file already exists
        $skipFile = "$folder"."$name"."_ModFOLDclust2.sort";
        if(-e $skipFile) {        
            say("$name has already been run. Skipping...");
        } else {
            #otherwise, run ModFOLDclust2
            say("$name has not been run yet. Running now...");
            my @args = ("-jar", "ModFOLDclust2.jar", "$name", "$fasta", "$folder");
            #say("@args");
            system("java", @args) == 0
                or die "failed: $?"
        }
    }
}
#my @args = ("-jar", "ModFOLDclust2.jar", "$name", "$fasta", "$folder");
#say("@args");
#system("java", @args) == 0
#    or die "failed: $?"

#    } elsif() {
#        say("$path is a directory");
#    } else {
#        say("$file is a tar file");
#    }
#}
