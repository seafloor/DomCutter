#!/usr/bin/perl
#used to call callTM.sh on all of the full-length CASP files
#call with the CASP folder, Official Structures folder and output folder
#path for official structures: ~/Dropbox/part_3_project/project/data/CASP10_full_structures_official/
#path for CASP folder: ~/Dropbox/part_3_project/project/data/CASP10_server_models/
#output is set to ~/Dropbox/part_3_project/project/data/fullTMscores/

use strict;
use 5.010;

my @files;
#my @QAfiles;
my $name;
my $folder;
#my $fasta;
#my $QAfolder;
my $outputdir = $ARGV[2];
my $officialstruct;

say("the directory is $ARGV[0]");
#say("the QA folder is $ARGV[1]");
opendir (DIR, $ARGV[0]) or die "couldn't open the directory";
#opendir (QADIR, $ARGV[1]) or die "coudn't open the top QA files directory";
while (my $file = readdir(DIR)) {
#say("$file");
    push(@files, $file);
}
@files=sort(@files);
say("@files");

#do the same for the QA folder
#while(my $QAfile = readdir(QADIR)) {
#    push(@QAfiles, $QAfile);
#}
#@QAfiles = sort(@QAfiles);
#say "@QAfiles";

foreach(@files) {
    if($_ =~ /(T\d+)\..*/) {
        #say("$_");
    } elsif($_ =~ /\./) {
        #say("$_");
    } else {
        #say("$_");
        $name = $_;
        $folder = "$ARGV[0]$_/";
        $officialstruct = "$ARGV[1]$_.pdb";
        #say "$QAfolder";
        #say "matched to file $_";
        #$QAfolder = "$ARGV[1]$_";
        #say "$QAfolder";
        my @args = ("callTM.sh", "$folder", "$officialstruct", "$outputdir", "$name");
        say "@args";
        system("sh", @args) == 0
            or die "failed: $?"
    }
}
