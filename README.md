DomCutter
=========
*domain-based clustering for protein structure model quality assessment*

### Outline
-----------

This project attempts to refine the quality assessment of protein models by focussing on domains rather than whole structures. The term domain is used loosely: it may refer to a traditional domain prediction or a region predicted to be of different quality compared to the rest of the structure based on local quality scores or disorder predictions. The "domains" identified by these methods are scored separately via hierarchichal clustering. The resulting domain scores may be combined to provide a new global score, or they may be used to splice domains together into new structures.

### Sections
------------
- CombinedScores: generates scores for separate domains, as defined by PDP, and recombines them into new global scores
- Hybrids: generates hybrid models based on the best domains from CombinedScores (coming soon)
- DisorderDomains: uses local residue scores to define "domains" and create hybrid structures (coming soon)
- CoreTools: classes used by all sections to perform common tasks
- Analysis: scripts to reproduce analysis of all methods

### Notes
---------
You will need [PDP](http://bioinformatics.oxfordjournals.org/content/19/3/429.long) for CombinedScores and Hybrids, and [ModFOLDclust2](http://www.reading.ac.uk/bioinf/ModFOLD/index.html) for all 3 methods. Analysis will need [TMscore](http://zhanglab.ccmb.med.umich.edu/TM-score/).
