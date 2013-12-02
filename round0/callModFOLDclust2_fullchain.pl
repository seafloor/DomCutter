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
my $delFiles;
my @delArgs;
my @delSearch;
my $delSearchFile;

say("the directory is $ARGV[0]");
opendir (DIR, $ARGV[0]) or die "couldn't open the directory";
while (my $file = readdir(DIR)) {
    #say("$file");
    push(@files, $file);
}
@files=sort(@files);
#say("@files");

#array of multi-domai targets according to the official domain definition
my @multiDomains = qw! T0651 T0652 T0658 T0663 T0671 T0674 T0675 T0677 T0684 T0685 T0686 T0690 T0693 T0705 T0713 T0717 T0724 T0726 T0732 T0735 T0739 T0756 !;

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

        #if target isn't multidomain then skip loop
        if(grep { $_ eq $name} @multiDomains) {
            say("target $name is multidomain");
        } else {
            say("$name only contains one domain. Skipping...");
            next;
        }

        #skip this loop if the ModFOLDclust2 file already exists
        $skipFile = "$folder"."$name"."_ModFOLDclust2.sort";
        #or delete this feles if it has only been partially run, then run it again
        $delFiles = "$folder"."$name"."_ModFOLDclustQ.unsort";
        @delArgs = ("$folder"."$name"."_pairwise.out", "$folder"."$name"."_ModFOLDclustQ.unsort", "$folder"."$name"."_ModFOLDclust.unsort");
        if(-e $skipFile) {        
            say("$name has already been run. Skipping...");
        } elsif(-e $delFiles) {
            system("rm", "-rf", "$folder"."tmp.out");
            system("rm", @delArgs);
            opendir(SEARCH, $folder) or die "couldn't open dir for partially done files";
            while($delSearchFile = readdir(SEARCH)) {
                push(@delSearch, $delSearchFile);
            }
            foreach(@delSearch) {
                if($_ =~ /.*(bfact|gnuplot)/) {
                    system("rm", "$folder"."$_");
                    say("$_ was deleted");
                }
            }
            my @args = ("-jar", "ModFOLDclust2.jar", "$name", "$fasta", "$folder");
            system("java", @args) == 0
                or die "failed: $?"
        } else {
            #otherwise, run ModFOLDclust2
            say("$name has not been run yet. Running now...");
            my @args = ("-jar", "ModFOLDclust2.jar", "$name", "$fasta", "$folder");
            say("@args");
            system("java", @args) == 0
                or die "failed: $?"
        }
    }
}
