DomCutter
=========
*domain-based clustering for protein structure MQA*

### Outline
-----------

This project attempts to refine the quality assessment of protein models. When trying to predict the structure of a protein target, often several models can be obtained from different servers. These models then need to be assessed, but how do you know which is the best when you don't know what the actual structure looks like?

Currently all methods do this by producing a global quality score for each model. Some are single-model methods that consider each model individually, others consider all models together and take a clustering-based approach. Here we apply hierarchical clustering to individual domains, rather than entire structures. This is because when global scores are used, the highest-ranking model may have a very poorly modelled domain yet still have the best global score. It's also true that most accurate model is often missed and another model is selected as the best. Our domain-focussed approach attempts to produce new global scores that are more accurate, and create hybrid structures that could be better in quality than any of the original models.

### Sections
------------
- round0: scripts to generate data from the previous method
- round1: generates scores for separate domains
- round2: generates hybrid models

### Notes
---------
To run the code you will need [PDP](http://bioinformatics.oxfordjournals.org/content/19/3/429.long) and [ModFOLDclust2](http://www.reading.ac.uk/bioinf/ModFOLD/index.html). Analysis will need the [TMscore](http://zhanglab.ccmb.med.umich.edu/TM-score/) algorithm.
