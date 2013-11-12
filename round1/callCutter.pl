#!/usr/bin/perl
#call with the CASP folder and the Top models folder
#used to call DomCutter.java on all of the full-length CASP files
#call as: perl callCutter.pl /home/matt/project/data/CASP10_server_models/ /home/matt/project/data/QAfiles/
use strict;
use 5.010;

my @files;
my @QAfiles;
my $name;
my $folder;
my $fasta;
my $QAfolder;

say("the directory is $ARGV[0]");
say("the QA folder is $ARGV[1]");
opendir (DIR, $ARGV[0]) or die "couldn't open the directory";
opendir (QADIR, $ARGV[1]) or die "coudn't open the top QA files directory";
while (my $file = readdir(DIR)) {
#say("$file");
    push(@files, $file);
}
@files=sort(@files);
#say("@files");

#do the same for the QA folder
while(my $QAfile = readdir(QADIR)) {
    push(@QAfiles, $QAfile);
}
@QAfiles = sort(@QAfiles);
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
        $fasta = "$ARGV[0]$_.fasta";
        foreach(@QAfiles) {
            #say "$QAfolder";
            if($_ =~ /$name.*/) {
                #say "matched to file $_";
                $QAfolder = "$ARGV[1]$_";
                #say "$QAfolder";
                my @args = ("DomCutter", "$folder", "$name", "$QAfolder");
                #my @TMscore = ("callTM.sh", "
                say "@args";
                #system("sh", @TMscore) == 0
                #    or die "failed: $?"
                system("java", @args) == 0
                    or die "failed: $?"
            }
        
        }
    }
}
