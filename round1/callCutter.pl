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
my @multiDomains = qw! T0651 T0652 T0658 T0663 T0671 T0674 T0675 T0677 T0684 T0685 T0686 T0690 T0693 T0705 T0713 T0717 T0724 T0726 T0732 T0735 T0739 T0756 !;

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
        if(grep { $_ eq $name} @multiDomains) {
            say "$name contains more than one domain. Splitting...";
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
        } else {
            say "$name only contains one domain. Skipping...";
        }
    }
}
