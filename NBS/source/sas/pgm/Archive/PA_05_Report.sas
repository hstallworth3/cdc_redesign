
dm log 'clear' continue ;
dm list 'clear' continue ;

libname nbs_ods ODBC DSN=nedss2 UID=nbs_ods PASSWORD=ods ACCESS=READONLY;
libname nbs_rdb ODBC DSN=nbs_rdb1 UID=nbs_rdb PASSWORD=rdb ACCESS=READONLY;




ods listing;
proc sql;
create table pa5 as 
select distinct *, datepart(CA_INIT_INTVWR_ASSGN_DT) as interview_dt format =  mmddyy10.
from  nbs_rdb.STD_HIV_DATAMART 
where INVESTIGATOR_name in ('Keable, Kristi','Nightingale, Florence') 
order by INVESTIGATOR_name;
quit;

proc sql;
create table dte as 
select * ,(today()-interview_dt) as Days
from pa5
order by INVESTIGATOR_name;
quit; 



title;
proc freq data = dte ;
by INVESTIGATOR_NAME;
table CURR_PROCESS_STATE / nocum missing out = assign ;
run;

proc freq data = dte ;
table CURR_PROCESS_STATE / nocum missing out = first ;
run;


 proc freq data = dte ;
table Days / nocum missing out = dte_freq_all ;
where Days le 14;
run;

 proc freq data = dte ;
by INVESTIGATOR_NAME;
table Days / nocum missing out = dte_freq_ind ;
where Days le 14;
run;

options missing=0;

proc sql noprint;
select count(*) into :Val_A 
from dte 
where CA_PATIENT_INTV_STATUS in ('I - Interviewed');

select sum(count) into :Val_B
from assign where CURR_PROCESS_STATE = 'Open Case';
select sum(count) into :Val_C
from assign where CURR_PROCESS_STATE = 'Closed Case';
select sum(count) into :Val_D
from assign where CURR_PROCESS_STATE = 'Field Follow-up';

select count(CA_PATIENT_INTV_STATUS) into :Val_E 
from dte ;
select count(CA_PATIENT_INTV_STATUS) into :Val_F 
from dte 
where FL_FUP_DISPOSITION_CD = 'C';
select count(CA_PATIENT_INTV_STATUS) into :Val_G 
from dte
where FL_FUP_DISPOSITION_CD not in ('C');

select PERCENT/100 into :per_B
from first where CURR_PROCESS_STATE = 'Open Case';
select PERCENT/100 into :per_C
from first where CURR_PROCESS_STATE = 'Closed Case';
select PERCENT/100 into :per_D
from first where CURR_PROCESS_STATE = 'Field Follow-up';
select count(*) into :dte_14
from dte_freq_all where Days le 14;
select count(*) into :dte_07
from dte_freq_all where Days le 7 ; 
select count(*) into :dte_05
from dte_freq_all where Days le  5; 
select count(*) into :dte_03
from dte_freq_all where Days le 3; 
select sum(percent)/100 into :per_14
from dte_freq_all where Days le 14;
select sum(percent)/100 into :per_7
from dte_freq_all where Days le 7;
select sum(percent)/100 into :per_5
from dte_freq_all where Days le 5;
select sum(percent)/100 into :per_3
from dte_freq_all where Days le 3;

quit;





%let PER_E = %sysevalf(%eval(&Val_E) / %eval(&Val_A)) ;
%let PER_F = %sysevalf(%eval(&Val_F)/%eval(&Val_E)) ;
%let PER_G = %sysevalf(%eval(&Val_G) / %eval(&Val_E)) ;



data _null_;
put "Val_A =  &Val_A";
put "Val_B =  &Val_B";
put "Val_C =  &Val_C";

put "Val_D =  &Val_D";

put "Val_E =  &Val_E";
put "Val_F =  &Val_F";
put "Val_G =  &Val_G";
put "per_b =  &per_b";
put "per_c =  &per_c";
put "per_d =  &per_d";
put "PER_E =  &PER_E";
put "PER_F =  &PER_F";
put "PER_G =  &PER_G";
put "PER_14 =  &PER_14";
put "PER_7 =  &PER_7";
put "PER_5 =  &PER_5";
put "PER_3 =  &PER_3";
run;



/*select CA_PATIENT_INTV_STATUS ,CA_INIT_INTVWR_ASSGN_DT, * */
/*from  rdb..STD_HIV_DATAMART */
/*where INVESTIGATOR_NAME ='Keable, Kristi'---Worker*/
/*select distinct INVESTIGATOR_NAME  from rdb..STD_HIV_DATAMART*/


data _null_;
call symputx ('TDAY' , put(today(),mmddyy10.));
call symputx ('DTE1' , cats(put(intnx('month',today(),-1),mmddyy10.)));
call symputx ('DTE2' , cats(put(intnx('month',today(),-1,"E"),mmddyy10.)));
run;

data _null_;
put " Tday = &tday";
put " DTE1 = &DTE1";
put " DTE2 = &DTE2";
run;

options linesize=max;
data template (drop = blank blank2);
length descrip $50 blank $15 value1 $8 percent1 $15 blank $15 descrip2 $50 blank2 $15 value2 $15 percent2 $15;
infile datalines truncover pad dsd dlm='!' ;
input descrip $ blank $ value1 $ percent1 $ blank $ descrip2 $ blank2 $ value2 $ percent2 $;
datalines ;
NUM. CASES ASSIGNED:	! 	!73	!		!	!NUM OF OI'S:					!	!57	!	
	NUM. CASES OPEN:	!   !73	!100.0%	!	!	OI'S THAT WERE NCI:			!	!11	!19.3%	
	NUM. CASES CLOSED:	!   !0	!0.0%	!	!	PERIOD PARTNERS:			!	!53	!0.9	
	NUM. CASES PENDING:	!   !0	!0.0%	!	!	PARTNERS INITIATED:			!	!65	!1.1	
						!   !	!	!	!									!	!	!	
NUM. CASES IX'D:		!   !57	!78.1%	!	!NUM OF RI'S:					!	!16	!	
	 CLINIC IX'S:		!   !51	!89.5%	!	!	RI'S THAT WERE NCI:			!	!3	!18.8%	
	 FIELD IX'S:		!   !5	!8.8%	!	!	PARTNERS INITIATED:			!	!22	!1.4	
						!   !	!	!	!							  		!	!	!	
	IX'D W/IN 3 DAYS:	!   !57	!0.0%	!	!	CLUSTERS INIT.	(OI & RI):	!	!49	!0.7	
	IX'D W/IN 5 DAYS:	!   !57	!0.0%	!	!								!	!	!	
	IX'D W/IN 7 DAYS:	!   !57	!100.0%	!	!								!	!	!	
	IX'D W/IN 14 DAYS:	!   !57	!100.0%	!	!								!	!	!	
						!   !	!	!	!									!	!	!	
NUM. CASES NOT IX'D:	!   !3	!4.1%	!	!								!	!	!	
   	 REFUSED:			!   !1	!33.3%	!	!								!	!	!	
	 NO LOCATE:			!   !2	!66.7%	!	!								!	!	!	
	 OTHER:				!   !0	!0.0%	!	!								!	!	!	
;
RUN;




data xy;
set template;
if descrip = "NUM. CASES ASSIGNED:" then value1 = "&Val_A" ;
if descrip = "NUM. CASES OPEN:" then value1 = "&Val_B" ;
if descrip = "NUM. CASES CLOSED:" then value1 = "&Val_C" ;
if descrip = "NUM. CASES PENDING:" then value1 = "&Val_D" ;
if descrip = "NUM. CASES IX'D:" then value1 = "&Val_E" ;
if descrip = "CLINIC IX'S:" then value1 = "&Val_F" ;
if descrip = "FIELD IX'S:" then value1 = "&Val_G" ;
if descrip = "NUM. CASES OPEN:" then percent1 = put(round("&per_B",0.001),percent8.1) ;
if descrip = "NUM. CASES CLOSED:" then percent1 = put(round("&per_C",0.001),percent8.1) ;
if descrip = "NUM. CASES PENDING:" then percent1 = put(round("&per_D",0.001),percent8.1) ;
if descrip = "NUM. CASES IX'D:" then percent1 = put(round("&per_e",0.001),percent8.1) ;
if descrip = "CLINIC IX'S:" then percent1 = put(round("&per_f",0.001),percent8.1) ;
if descrip = "FIELD IX'S:" then percent1 = put(round("&per_g",0.001),percent8.1) ;
if descrip = "IX'D W/IN 3 DAYS:" then value1 = "&dte_03" ;
if descrip = "IX'D W/IN 5 DAYS:" then value1 = "&dte_05" ;
if descrip = "IX'D W/IN 7 DAYS:" then value1 = "&dte_07" ;
if descrip = "IX'D W/IN 14 DAYS:" then value1 = "&dte_14" ;
if descrip = "IX'D W/IN 3 DAYS:" then percent1 =put(round("&per_3",0.001),percent8.1)  ;
if descrip = "IX'D W/IN 5 DAYS:" then percent1 = put(round("&per_5",0.001),percent8.1)  ;
if descrip = "IX'D W/IN 7 DAYS:" then percent1 = put(round("&per_7",0.001),percent8.1)  ;
if descrip = "IX'D W/IN 14 DAYS:" then percent1 = put(round("&per_14",0.001),percent8.1)  ;
run;

ODS _all_ CLOSE;
ods results;
OPTIONS orientation=portrait   NONUMBER NODATE LS=248 PS =200 COMPRESS=NO MISSING=''
topmargin=1.00 in
bottommargin=1.00in
leftmargin=1.00in
rightmargin=1.00in  nobyline  ;
ods noproctitle;
ods escapechar = "^";
title;
footnote;
*LIBNAME LIB "C:\Users\SameerA\Output";
 %let outpath = C:\Users\SameerA\Output ;  
 ODS pdf FILE =   "&outpath.\ PA5_Report.pdf" style= styles.listing  ;
 title;
ods layout start width=8.49in
height=10.99in;
ods region x = 1.00 in y = 0.25 in ;
ods text = "^{style[font_face='calibri' fontsize=10pt just=center fontweight=bold] 
				WORKER INTERVIEW ACTIVITY REPORT  (CDCWKST1)}";
ods text = "^{style[font_face='calibri' fontsize=10pt just=center fontweight=bold] 
					REPORT DATE:  &TDAY}";
ods region x = 1.00 in
y = 0.50 in;
ods text="		";
ods text = "^{style[font_face='calibri' fontsize=10pt just=center fontweight=bold] 
				SELECTION CRITERIA}";

ods region x = 0.25 in
y = 0.75 in ;
ods text="		";
ods text = "^{style[font_face='calibri' fontsize=9pt just=left] 
				ASSIGN DATE : &DTE1 - &DTE2}";
ods text="		";
ods text = "^{style[font_face='calibri' fontsize=9pt just=left ] 
				Diagnoses: No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Workers:   No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Detection: No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Source:	No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Risk: No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Area: No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Gender: No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Race: No specific selection: all included}";
				ods text="		";
				ods text = "^{style[font_face='calibri'  fontsize=9pt just=left] 
				Age: No specific selection: all included}";

ods region x = 1.00 in y = 4.00 in width = 7.25 in;  
ods text = "^{style[font_face='calibri' fontsize=10pt just=center fontweight=bold] 
				WORKER INTERVIEW ACTIVITY REPORT  (CDCWKST1)}";
ods text = "^{style[font_face='calibri' fontsize=10pt just=center fontweight=bold] 
					REPORT DATE: &TDAY}";
ods text = "^{style[font_face='calibri' fontsize=10pt just=center fontweight=bold] 
				Worker:  Summary}";

ods region x = 0.25 in y = 4.50 in width = 3.00 in;
proc report data = xy nowd spanrows ls =200  split='~' headline missing 
style(header)={just=left font_weight=bold font_face="Arial"
font_size = 8pt }
style(report)={font_size=9pt  rules=none frame=void}
style(column)={font_size=8pt};

columns  descrip    value1  percent1   
/*descrip2  blank2  value2  percent2*/
;

/*define break /group noprint;*/
define descrip /display order=internal ' ' left style = [cellwidth=40mm ];
/*define  blank    / display ' ' width=3   center   style = [cellwidth=20mm ];*/
define   value1    / display order=internal ''    style = [cellwidth=10mm ];
define   percent1 / display ''  center  style = [cellwidth=15mm ];

*break after case_number/skip; 
 run;
ods region x = 3.50 in y = 4.50 in width = 3.00 in;
proc report data = xy nowd spanrows ls =200  split='~' headline missing style(header)={just=left font_weight=bold font_face="Arial"
font_size = 8pt }
style(report)={font_size=9pt  rules=none frame=void}
style(column)={font_size=8pt};

columns  descrip2    value2  percent2   
/*descrip2  blank2  value2  percent2*/
;

/*define break /group noprint;*/
define descrip2 /display order=internal ' ' left style = [cellwidth=40mm ];
/*define  blank    / display ' ' width=3   center   style = [cellwidth=20mm ];*/
define   value2    / display order=internal ''    style = [cellwidth=10mm ];
define   percent2/ display ''  center  style = [cellwidth=15mm ];

*break after case_number/skip; 
 run;



 ods pdf close;




