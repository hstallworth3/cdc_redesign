options linesize=max;

data templatePA04_STD;
length descrip $50 value1 8 percent1 $8 ;
informat _CHAR_ value1 _NUMERIC_ percent1 _CHAR_ ;
format descrip $50. value1 best9. percent1 $8. ;
infile datalines truncover dsd dlm='!' ;
input descrip $ value1  percent1 $ ;
datalines ;
CASES CLOSED:						!0!    
CASES INTERVIEWED:					!0!	
CASES NCI:							!0!0.0%
CASES RE-INTERVIEWED:				!0!0.0%
CASES NCI WITH RE-INTERVIEW:		!0!0.0%
CASES HIV TESTED:					!0!0.0%
CASES HIV SEROPOSITIVE:				!0!0.0%
CASES POSTTEST COUNSELED:			!0!0.0%
TOTAL PERIOD PARTNERS:				!0!    
PERIOD PARTNER INDEX:				!0!	
run;

data templatePA04_STDD;
length descrip $50 value1 8 percent1 $8 ;
informat _CHAR_ value1 _NUMERIC_ percent1 _CHAR_ ;
format descrip $50. value1 best9. percent1 $8. ;
infile datalines truncover dsd dlm='!' ;
input descrip $ value1  percent1 $ ;
datalines ;
CASES CLOSED:						!0!    
CASES INTERVIEWED:					!0!	
CASES NCI:							!0!0.0%
CASES RE-INTERVIEWED:				!0!0.0%
CASES NCI WITH RE-INTERVIEW:		!0!0.0%
CASES HIV TESTED:					!0!0.0%
CASES HIV SEROPOSITIVE:				!0!0.0%
CASES POSTTEST COUNSELED:			!0!0.0%
TOTAL PERIOD PARTNERS:				!0!    
PERIOD PARTNER INDEX:				!0!	
run;


data templatePA04;
length descrip $35 col1 8 col2 $8 col3 8 col4 $8 col5 8 col6 $8.;
informat _CHAR_ col1 _CHAR_ col2 _CHAR_ col3 _CHAR_ col4 _NUMERIC_ col5 _NUMERIC_ col6;
format descrip $35. col1 best9. col2 $8. col3 best9. col4 $8. col5 best9. col6 $8.;
infile datalines truncover dsd dlm='!' ;
input descrip  col1  col2  col3 col4  col5 col6 ;
datalines ;
PARTNERS INITIATED:!0!!0!!0! 
PARTNERS EXAMINED:!0!0.0%!0!0!0! 
DISPO. A:!0!0.0%!0!0!0! 
DISPO. C:!0!0.0%!0!0!0! 
DISPO. E:!0!0!0!0!0! 
TREAT. INDEX.:!0! !0! !0! 
DI. INDEX.:!0! !0! !0! 
DISPO. B:!0!0.0%!0!0!0! 
DISPO. D:!0!0!0!0!0! 
DISPO. F:!0!0!0!0!0! 
PARTNERS NOT EXAMINED:!0!0!0!0!0! 
DISPO. G:!0!0!0!0!0! 
DISPO. H:!0!0!0!0!0! 
DISPO. J:!0!0!0!0!0! 
DISPO. K:!0!0!0!0!0! 
DISPO. L:!0!0!0!0!0! 
DISPO. V:!0!0!0!0!0! 
DISPO. X:!0!0!0!0!0! 
DISPO. Z:!0!0!0!0!0! 
CLUSTERS INITIATED:!0!!0!!0! 
CLUSTERS EXAMINED:!0!0!0!0!0! 
DISPO A:!0!0!0!0!0! 
DISPO C:!0!0!0!0!0! 
DISPO E:!0!0!0!0!0! 
TREAT. INDEX:!0! !0! !0! 
DI. INDEX:!0! !0! !0! 
DISPO B:!0!0!0!0!0! 
DISPO D:!0!0!0!0!0! 
DISPO F:!0!0!0!0!0! 
CLUSTERS NOT EXAMINED:!0!0!0!0!0! 
DISPO G:!0!0!0!0!0! 
DISPO H:!0!0!0!0!0! 
DISPO J:!0!0!0!0!0! 
DISPO K:!0!0!0!0!0! 
DISPO L:!0!0!0!0!0! 
DISPO V:!0!0!0!0!0! 
DISPO X:!0!0!0!0!0! 
DISPO Z:!0!0!0!0!0! 
run;


data templatePA04_HIV;
length descrip $35 col1 8 col2 $8 col3 8 col4 $8 col5 8 col6 $8.;
informat _CHAR_ col1 _CHAR_ col2 _CHAR_ col3 _CHAR_ col4 _NUMERIC_ col5 _NUMERIC_ col6;
format descrip $35. col1 best9. col2 $8. col3 best9. col4 $8. col5 best9. col6 $8.;
infile datalines truncover dsd dlm='!' ;
input descrip  col1  col2  col3 col4  col5 col6 ;
datalines ;
PARTNERS INITIATED:!0!!0!!0! 
PARTNERS EXAMINED:!0!0.0%!0!0!0! 
DISPO. 1:!0!0.0%!0!0!0! 
DISPO. 2:!0!0.0%!0!0!0! 
DISPO. 3:!0!0.0%!0!0!0! 
DISPO. 4:!0!0!0!0!0! 
DISPO. 5:!0!0!0!0!0! 
DISPO. 6:!0!0!0!0!0! 
DISPO. 7:!0!0!0!0!0! 
NOTIF. INDEX.:!0! !0! !0! 
TESTING. INDEX.:!0! !0! !0! 
PARTNERS NOT EXAMINED:!0!0!0!0!0! 
DISPO. G:!0!0!0!0!0! 
DISPO. H:!0!0!0!0!0! 
DISPO. J:!0!0!0!0!0! 
DISPO. K:!0!0!0!0!0! 
DISPO. L:!0!0!0!0!0! 
DISPO. V:!0!0!0!0!0! 
DISPO. X:!0!0!0!0!0! 
CLUSTERS INITIATED:!0!!0!!0! 
CLUSTERS EXAMINED:!0!0!0!0!0! 
DISPO 1:!0!0.0%!0!0!0! 
DISPO 2:!0!0.0%!0!0!0! 
DISPO 3:!0!0.0%!0!0!0! 
DISPO 4:!0!0!0!0!0! 
DISPO 5:!0!0!0!0!0! 
DISPO 6:!0!0!0!0!0! 
DISPO 7:!0!0!0!0!0! 
NOTIF. INDEX:!0! !0! !0! 
TESTING. INDEX:!0! !0! !0! 
CLUSTERS NOT EXAMINED:!0!0!0!0!0! 
DISPO G:!0!0!0!0!0! 
DISPO H:!0!0!0!0!0! 
DISPO J:!0!0!0!0!0! 
DISPO K:!0!0!0!0!0! 
DISPO L:!0!0!0!0!0! 
DISPO V:!0!0!0!0!0! 
DISPO X:!0!0!0!0!0! 
run;
