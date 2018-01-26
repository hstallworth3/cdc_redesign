PROC SQL;
CREATE TABLE 
	PHC_UIDS AS  
SELECT 	
	PUBLIC_HEALTH_CASE_UID LENGTH =8 AS PAGE_CASE_UID 'PAGE_CASE_UID', 
	INVESTIGATION_FORM_CD, CD, LAST_CHG_TIME 
FROM 
	NBS_ODS.PUBLIC_HEALTH_CASE, NBS_SRT.CONDITION_CODE
WHERE 
	CONDITION_CODE.CONDITION_CD= PUBLIC_HEALTH_CASE.CD
AND 
	INVESTIGATION_FORM_CD 
NOT IN 
	('INV_FORM_BMDGAS','INV_FORM_BMDGBS','INV_FORM_BMDGEN','INV_FORM_BMDNM','INV_FORM_BMDSP','INV_FORM_GEN','INV_FORM_HEPA','INV_FORM_HEPBV','INV_FORM_HEPCV','INV_FORM_HEPGEN','INV_FORM_MEA','INV_FORM_PER','INV_FORM_RUB','INV_FORM_RVCT','INV_FORM_VAR');
QUIT;
PROC SQL;
CREATE TABLE 
	TEXT_METADATA AS
SELECT DISTINCT NUIM.QUESTION_GROUP_SEQ_NBR, NUIM.NBS_QUESTION_UID, NUIM.CODE_SET_GROUP_ID,  
	UPCASE(NRDBM.RDB_COLUMN_NM) AS RDB_COLUMN_NM,
	NUIM.QUESTION_GROUP_SEQ_NBR
FROM  
	NBS_ODS.NBS_RDB_METADATA NRDBM 
 INNER JOIN 
	NBS_ODS.NBS_UI_METADATA NUIM
ON 
	NUIM.NBS_UI_METADATA_UID=NRDBM.NBS_UI_METADATA_UID
	INNER JOIN NBS_SRT.CODE_VALUE_GENERAL CVG 
ON 
	CVG.CODE=NUIM.DATA_TYPE 
WHERE 		CVG.CODE_SET_NM = 'NBS_DATA_TYPE' AND CODE = 'TEXT' AND QUESTION_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	NBS_QUESTION_UID;
PROC SQL;
create index NBS_QUESTION_UID on TEXT_METADATA(NBS_QUESTION_UID);
quit;
PROC SQL;
CREATE TABLE 
	TEXT_DATA AS
SELECT DISTINCT  NBS_CASE_ANSWER_UID, PA.ANSWER_GROUP_SEQ_NBR,TRANSLATE(ANSWER_TXT,' ' ,'0D0A'x)	'ANSWER_TXT' as ANSWER_TXT,
ACT_UID LENGTH =8 AS PAGE_CASE_UID 'PAGE_CASE_UID',
	PA.RECORD_STATUS_CD, A.*	
FROM  
	TEXT_METADATA A LEFT OUTER JOIN 
	NBS_ODS.NBS_CASE_ANSWER PA 
ON 
	A.NBS_QUESTION_UID =PA.NBS_QUESTION_UID 
	WHERE ANSWER_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	ACT_UID;
QUIT;
PROC SORT DATA=TEXT_DATA NODUPKEY OUT=TEXT_DATA; BY NBS_CASE_ANSWER_UID; RUN;
PROC SORT  DATA= TEXT_DATA; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
PROC TRANSPOSE 
	DATA=TEXT_DATA OUT=TEXT_DATA_PIVOT;
	BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR;
	ID 	RDB_COLUMN_NM; 	VAR	ANSWER_TXT;
	COPY NBS_CASE_ANSWER_UID;
RUN;
PROC SQL;
CREATE TABLE TEXT_DATA_OUT 
	AS SELECT * FROM TEXT_DATA_PIVOT 
	WHERE LENGTHN(_NAME_)>0 AND PAGE_CASE_UID>0;
QUIT;
PROC SQL;
CREATE TABLE 
	CODED_METADATA AS
SELECT DISTINCT NUIM.QUESTION_GROUP_SEQ_NBR, NUIM.NBS_QUESTION_UID, NUIM.CODE_SET_GROUP_ID,  
	UPCASE(NRDBM.RDB_COLUMN_NM) AS RDB_COLUMN_NM,
	NUIM.QUESTION_GROUP_SEQ_NBR, NUIM.investigation_form_cd
FROM  
	NBS_ODS.NBS_RDB_METADATA NRDBM 
 INNER JOIN 
	NBS_ODS.NBS_UI_METADATA NUIM
ON 
	NUIM.NBS_UI_METADATA_UID=NRDBM.NBS_UI_METADATA_UID
	INNER JOIN NBS_SRT.CODE_VALUE_GENERAL CVG 
ON 
	CVG.CODE=NUIM.DATA_TYPE 
WHERE 		CVG.CODE_SET_NM = 'NBS_DATA_TYPE' AND CODE = 'CODED' AND QUESTION_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	NUIM.CODE_SET_GROUP_ID;

CREATE TABLE 
	CODED_TABLE AS
SELECT   NBS_CASE_ANSWER_UID, PA.ANSWER_GROUP_SEQ_NBR,ANSWER_TXT, ACT_UID LENGTH =8 AS PAGE_CASE_UID 'PAGE_CASE_UID',
	PA.RECORD_STATUS_CD, A.*	
FROM  
	CODED_METADATA A 
inner JOIN 
	NBS_ODS.NBS_CASE_ANSWER PA 
ON 
	A.NBS_QUESTION_UID =PA.NBS_QUESTION_UID 
inner join 
phc_uids 
on
	phc_uids.page_case_uid =PA.act_uid
WHERE ANSWER_GROUP_SEQ_NBR IS NOT NULL
AND phc_uids.investigation_form_cd=a.investigation_form_cd
ORDER BY 	ACT_UID;

QUIT;
PROC SORT DATA=CODED_TABLE NODUPKEY OUT=CODED_DATA; BY NBS_CASE_ANSWER_UID; RUN;
DATA CODED_TABLE;
SET CODED_TABLE;
	X = INDEX(ANSWER_TXT, '^');
	LENGTH=LENGTHN(ANSWER_TXT);
	IF X> 0 THEN ANSWER_OTH=SUBSTR(ANSWER_TXT, (5), LENGTH);
	IF X> 0 THEN ANSWER_TXT=SUBSTR(ANSWER_TXT, 1, (X-1));
	Y=LENGTHN(ANSWER_OTH);
	IF(Y>0) THEN RDB_COLUMN_NM2= TRIM(RDB_COLUMN_NM) || '_OTH';
	IF UPCASE (ANSWER_TXT)='OTH' THEN ANSWER_TXT='OTH';
RUN;
PROC SQL;
CREATE TABLE 
	CODED_TABLE AS 
SELECT 
	ANSWER_GROUP_SEQ_NBR, CODED.CODE_SET_GROUP_ID, PAGE_CASE_UID, NBS_QUESTION_UID, 
	NBS_CASE_ANSWER_UID, ANSWER_TXT, CVG.CODE_SET_NM, RDB_COLUMN_NM, ANSWER_OTH, RDB_COLUMN_NM2,
	CODE,CODE_SHORT_DESC_TXT AS ANSWER_TXT1 'ANSWER_TXT1'
FROM 
	CODED_TABLE CODED
	LEFT JOIN NBS_SRT.CODESET_GROUP_METADATA METADATA
ON  
	METADATA.CODE_SET_GROUP_ID=CODED.CODE_SET_GROUP_ID
	LEFT JOIN NBS_SRT.CODE_VALUE_GENERAL CVG
ON 	
	CVG.CODE_SET_NM=METADATA.CODE_SET_NM
AND	
	CVG.CODE=CODED.ANSWER_TXT;
QUIT;
PROC SORT DATA=CODED_TABLE; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR NBS_QUESTION_UID; RUN;

DATA CODED_TABLE_DESC;
SET CODED_TABLE;
	BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR NBS_QUESTION_UID;
	FORMAT ANSWER_DESC1-ANSWER_DESC10 $40. ANSWER_DESC11 $4000.;
	ARRAY ANSWER_DESC(10) ANSWER_DESC1-ANSWER_DESC10;
	RETAIN ANSWER_DESC1-ANSWER_DESC11 ' ' I 0;
	IF  FIRST.NBS_QUESTION_UID THEN DO;
		DO J=1 TO 10; ANSWER_DESC(J) = ' ';	END;
		I = 0; ANSWER_DESC11 = ''; 
		END;
	I+1;
	IF I <= 10 THEN DO;
		ANSWER_DESC(I) = ANSWER_TXT1;
		ANSWER_DESC11 =LEFT(TRIM(ANSWER_TXT1))||' | '|| LEFT(TRIM(ANSWER_DESC11)) ;
	END;
	IF LAST.NBS_QUESTION_UID  THEN OUTPUT;
RUN;

DATA CODED_TABLE_DESC;
SET CODED_TABLE_DESC;
 	A=LENGTHN(ANSWER_TXT);
 	IF TRIM(ANSWER_DESC11)=' |' THEN ANSWER_DESC11='';
 	LENGTH=LENGTHN(ANSWER_DESC11);
 	IF LENGTH> 0 THEN ANSWER_DESC11=TRIM(SUBSTR(ANSWER_DESC11, 1, (LENGTH-1)));
RUN;
PROC SQL;
CREATE TABLE 
	CODED_COUNTY_TABLE AS 
SELECT 
	ANSWER_GROUP_SEQ_NBR, CODED.CODE_SET_GROUP_ID, PAGE_CASE_UID, NBS_QUESTION_UID, 
	NBS_CASE_ANSWER_UID, ANSWER_TXT, CVG.CODE_SET_NM, RDB_COLUMN_NM, ANSWER_OTH, RDB_COLUMN_NM2,
	CVG.CODE,CODE_SHORT_DESC_TXT AS ANSWER_TXT1 'ANSWER_TXT1'
FROM 
	CODED_TABLE CODED
	LEFT JOIN NBS_SRT.CODESET_GROUP_METADATA METADATA
ON  
	METADATA.CODE_SET_GROUP_ID=CODED.CODE_SET_GROUP_ID
	LEFT JOIN NBS_SRT.V_STATE_COUNTY_CODE_VALUE CVG
ON 	
	CVG.CODE_SET_NM=METADATA.CODE_SET_NM
AND	
	CVG.CODE=CODED.ANSWER_TXT
WHERE METADATA.CODE_SET_NM= 'COUNTY_CCD';
QUIT;
PROC SORT DATA=CODED_COUNTY_TABLE; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR NBS_QUESTION_UID; RUN;
DATA CODED_COUNTY_TABLE_DESC;
SET CODED_COUNTY_TABLE;
	BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR NBS_QUESTION_UID;
	FORMAT ANSWER_DESC1-ANSWER_DESC10 $40. ANSWER_DESC11 $4000.;
	ARRAY ANSWER_DESC(10) ANSWER_DESC1-ANSWER_DESC10;
	RETAIN ANSWER_DESC1-ANSWER_DESC11 ' ' I 0;
	IF  FIRST.NBS_QUESTION_UID THEN DO;
		DO J=1 TO 10; ANSWER_DESC(J) = ' ';	END;
		I = 0; ANSWER_DESC11 = ''; 
		END;
	I+1;
	IF I <= 10 THEN DO;
		ANSWER_DESC(I) = ANSWER_TXT1;
		ANSWER_DESC11 =LEFT(TRIM(ANSWER_TXT1))||' | '|| LEFT(TRIM(ANSWER_DESC11)) ;
	END;
	IF LAST.NBS_QUESTION_UID  THEN OUTPUT;
RUN;

DATA CODED_COUNTY_TABLE_DESC;
SET CODED_COUNTY_TABLE_DESC;
 	A=LENGTHN(ANSWER_TXT);
 	IF TRIM(ANSWER_DESC11)=' |' THEN ANSWER_DESC11='';
 	LENGTH=LENGTHN(ANSWER_DESC11);
 	IF LENGTH> 0 THEN ANSWER_DESC11=TRIM(SUBSTR(ANSWER_DESC11, 1, (LENGTH-1)));
RUN;
DATA CODED_TABLE_OTH;
SET CODED_TABLE;
	IF LENGTHN(TRIM(RDB_COLUMN_NM2))>0;
 	IF(LENGTHN(RDB_COLUMN_NM2)>0) 
   		THEN RDB_COLUMN_NM=RDB_COLUMN_NM2;
	IF(LENGTHN(TRIM(RDB_COLUMN_NM2))>0) 
   		THEN ANSWER_DESC11=ANSWER_OTH;
RUN;
PROC SORT DATA=CODED_COUNTY_TABLE_DESC; BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; RUN;
PROC SORT DATA=CODED_TABLE_DESC; BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; RUN;
PROC SORT DATA=CODED_TABLE_OTH NODUPKEY; BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; RUN;

DATA CODED_TABLE_MERGED ; 
  MERGE CODED_TABLE_DESC CODED_COUNTY_TABLE_DESC CODED_TABLE_OTH; 
  BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; 
RUN;
PROC SORT  DATA= CODED_TABLE_MERGED; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR NBS_QUESTION_UID RDB_COLUMN_NM; RUN;
PROC TRANSPOSE 
	DATA=CODED_TABLE_MERGED OUT=CODED_DATA_PIVOT;	BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR;
	ID 	RDB_COLUMN_NM; VAR ANSWER_DESC11;
	COPY NBS_CASE_ANSWER_UID;
RUN;
PROC SQL;
CREATE TABLE CODED_DATA_OUT 
	AS SELECT * FROM CODED_DATA_PIVOT 
	WHERE LENGTHN(_NAME_)>0 AND PAGE_CASE_UID>0;
QUIT;
PROC DATASETS LIBRARY = WORK NOLIST;
DELETE CODED_TABLE CODED_TABLE1 CODED_TABLE2 CODED_TABLE_DESC CODED_TABLE_OTH CODED_TABLE_MERGED TEXT_DATA_PIVOT
RUN;
QUIT;
PROC SQL;
CREATE TABLE 
	DATE_METADATA AS
SELECT DISTINCT NUIM.QUESTION_GROUP_SEQ_NBR, NUIM.NBS_QUESTION_UID, NUIM.CODE_SET_GROUP_ID,  
	UPCASE(NRDBM.RDB_COLUMN_NM) AS RDB_COLUMN_NM,
	NUIM.QUESTION_GROUP_SEQ_NBR
FROM  
	NBS_ODS.NBS_RDB_METADATA NRDBM 
 INNER JOIN 
	NBS_ODS.NBS_UI_METADATA NUIM
ON 
	NUIM.NBS_UI_METADATA_UID=NRDBM.NBS_UI_METADATA_UID
	INNER JOIN NBS_SRT.CODE_VALUE_GENERAL CVG 
ON 
	CVG.CODE=NUIM.DATA_TYPE 
WHERE 		CVG.CODE_SET_NM = 'NBS_DATA_TYPE' AND CODE IN( 'DATETIME','DATE') AND QUESTION_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	NBS_QUESTION_UID;
QUIT;
PROC SQL;
create index NBS_QUESTION_UID on DATE_METADATA(NBS_QUESTION_UID);
quit;
PROC SQL;
CREATE TABLE 
	DATE_DATA AS
SELECT DISTINCT  NBS_CASE_ANSWER_UID, PA.ANSWER_GROUP_SEQ_NBR,ANSWER_TXT, ACT_UID LENGTH =8 AS PAGE_CASE_UID 'PAGE_CASE_UID',
	PA.RECORD_STATUS_CD, A.*	
FROM  
	DATE_METADATA A LEFT OUTER JOIN 
	NBS_ODS.NBS_CASE_ANSWER PA 
ON 
	A.NBS_QUESTION_UID =PA.NBS_QUESTION_UID 
	WHERE ANSWER_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	ACT_UID;
QUIT;
PROC SORT DATA=DATE_DATA NODUPKEY OUT=DATE_DATA; BY NBS_CASE_ANSWER_UID; RUN;
DATA DATE_DATA;
SET DATE_DATA;
	ANSWER_TXT1= INPUT(ANSWER_TXT, ANYDTDTM32.);   
	DROP ANSWER_TXT;
	RENAME ANSWER_TXT=ANSWER_TXT1;   
RUN;
PROC SQL; 
CREATE TABLE NBS_RDB.PAGE_DATE_TABLE( BUFSIZE=8192 ) 
  ( 
   NBS_CASE_ANSWER_UID NUM, 
	CODE_SET_GROUP_ID NUM, 
	RDB_COLUMN_NM CHAR(40), 
	ANSWER_TXT1 DATE,  
	PAGE_CASE_UID NUM, 
	LAST_CHG_TIME DATE, 
	RECORD_STATUS_CD CHAR(40),
    ANSWER_GROUP_SEQ_NBR NUM); 
QUIT; 
DATA DATE_DATA; 
SET DATE_DATA;  
DROP _LABEL_; 
DROP _NAME_; 
RUN; 
%DBLOAD (PAGE_DATE_TABLE, DATE_DATA); 
DATA PAGE_DATE_TABLE; SET NBS_RDB.PAGE_DATE_TABLE; RUN; 
PROC SORT  DATA= PAGE_DATE_TABLE; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
PROC TRANSPOSE DATA=PAGE_DATE_TABLE OUT=DATE_DATA_PIVOT; 
	BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; 
	ID 	RDB_COLUMN_NM; 
	VAR	ANSWER_TXT1; 
	COPY NBS_CASE_ANSWER_UID;
RUN; 
PROC SQL;
CREATE TABLE DATE_DATA_OUT 
	AS SELECT * FROM DATE_DATA_PIVOT 
	WHERE LENGTHN(_NAME_)>0 AND PAGE_CASE_UID>0;
QUIT;
PROC SQL;
DROP TABLE NBS_RDB.PAGE_DATE_TABLE;
QUIT;
PROC DATASETS LIBRARY = WORK NOLIST;
DELETE 
PAGE_DATE_TABLE
DATE_DATA
RUN;
QUIT;
PROC SQL;
CREATE TABLE 
	NUMERIC_METADATA AS
SELECT DISTINCT NUIM.QUESTION_GROUP_SEQ_NBR, NUIM.NBS_QUESTION_UID, NUIM.UNIT_VALUE as CODE_SET_GROUP_ID 'CODE_SET_GROUP_ID',  
	UPCASE(NRDBM.RDB_COLUMN_NM) AS RDB_COLUMN_NM,
	NUIM.QUESTION_GROUP_SEQ_NBR
FROM  
	NBS_ODS.NBS_RDB_METADATA NRDBM 
 INNER JOIN 
	NBS_ODS.NBS_UI_METADATA NUIM
ON 
	NUIM.NBS_UI_METADATA_UID=NRDBM.NBS_UI_METADATA_UID
	INNER JOIN NBS_SRT.CODE_VALUE_GENERAL CVG 
ON 
	CVG.CODE=NUIM.DATA_TYPE 
WHERE 		CVG.CODE_SET_NM = 'NBS_DATA_TYPE' AND CODE = 'NUMERIC' AND QUESTION_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	NUIM.CODE_SET_GROUP_ID;

QUIT;
PROC SQL;
create index NBS_QUESTION_UID on NUMERIC_METADATA(NBS_QUESTION_UID);
QUIT;
PROC SQL;
CREATE TABLE 
	NUMERIC_BASE_DATA AS
SELECT DISTINCT  NBS_CASE_ANSWER_UID, PA.ANSWER_GROUP_SEQ_NBR,ANSWER_TXT, ACT_UID LENGTH =8 AS PAGE_CASE_UID 'PAGE_CASE_UID',
	PA.RECORD_STATUS_CD, A.*	
FROM  
	NUMERIC_METADATA A LEFT OUTER JOIN 
	NBS_ODS.NBS_CASE_ANSWER PA 
ON 
	A.NBS_QUESTION_UID =PA.NBS_QUESTION_UID 
	WHERE ANSWER_GROUP_SEQ_NBR IS NOT NULL
ORDER BY 	ACT_UID;
QUIT;
PROC SORT DATA=NUMERIC_BASE_DATA NODUPKEY OUT=NUMERIC_BASE_DATA; BY NBS_CASE_ANSWER_UID; RUN;
DATA NUMERIC_DATA1;
SET NUMERIC_BASE_DATA;
	X = INDEX(ANSWER_TXT, '^');
	ANSWER_TXT1=ANSWER_TXT;
	LENGTH=LENGTHN(ANSWER_TXT1);
	IF X> 0 THEN ANSWER_UNIT=SUBSTR(ANSWER_TXT, 1, (X-1));
	IF X= 0 THEN ANSWER_UNIT=ANSWER_TXT;
	IF X> 0 THEN LENCODED=LENGTHN(ANSWER_UNIT);
	IF X> 0 THEN ANSWER_CODED=SUBSTR(ANSWER_TXT, (LENCODED+2), LENGTH);
	IF X> 0 THEN UNIT_VALUE1 = INPUT(CODE_SET_GROUP_ID, COMMA20.);
	Y=LENGTHN(ANSWER_CODED);
	IF(Y>0) THEN RDB_COLUMN_NM2= TRIM(RDB_COLUMN_NM) || ' UNIT';
RUN;
DATA NUMERIC_DATA2;
SET NUMERIC_DATA1;
	IF(LENGTHN(RDB_COLUMN_NM2)>0) THEN RDB_COLUMN_NM=RDB_COLUMN_NM2;
RUN;
PROC SORT DATA=NUMERIC_DATA2; BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; RUN;
 PROC SORT DATA=NUMERIC_DATA1; BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; RUN;
DATA NUMERIC_DATA_MERGED ; 
  MERGE NUMERIC_DATA1 NUMERIC_DATA2; 
  BY NBS_CASE_ANSWER_UID RDB_COLUMN_NM; 
RUN;
PROC SQL;
CREATE TABLE 
	NUMERIC_DATA_TRANS  AS 
SELECT 
	PAGE_CASE_UID, NBS_QUESTION_UID,  ANSWER_GROUP_SEQ_NBR,
	 NBS_CASE_ANSWER_UID, ANSWER_UNIT,ANSWER_CODED, CVG.CODE_SET_NM,RDB_COLUMN_NM,
	ANSWER_TXT,	CODE,CODE_SHORT_DESC_TXT AS UNIT 'UNIT', ANSWER_UNIT 
FROM 
	NUMERIC_DATA_MERGED CODED
	LEFT JOIN NBS_SRT.CODESET_GROUP_METADATA METADATA
ON 
	METADATA.CODE_SET_GROUP_ID=CODED.UNIT_VALUE1
	LEFT JOIN NBS_SRT.CODE_VALUE_GENERAL CVG
ON 
	CVG.CODE_SET_NM=METADATA.CODE_SET_NM
WHERE 
	CVG.CODE=CODED.ANSWER_CODED
	ORDER BY PAGE_CASE_UID;
QUIT;
DATA NUMERIC_DATA_TRANS;
SET NUMERIC_DATA_TRANS;
	X=INDEX(RDB_COLUMN_NM,' UNIT');
	IF TRIM(UNIT)=''  THEN ANSWER_TXT=ANSWER_TXT;
	ELSE IF X>0 THEN ANSWER_TXT=UNIT;
	ELSE ANSWER_TXT=ANSWER_UNIT;
RUN;
PROC SQL;
CREATE TABLE NUMERIC_DATA_TRANS1 AS 
SELECT DISTINCT 
	PAGE_CASE_UID,
	RDB_COLUMN_NM,
	ANSWER_UNIT,
	ANSWER_TXT,
	ANSWER_GROUP_SEQ_NBR,
	NBS_CASE_ANSWER_UID
FROM NUMERIC_DATA_TRANS;
QUIT;
PROC SORT  DATA= NUMERIC_DATA_TRANS1; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
PROC TRANSPOSE 
	DATA=NUMERIC_DATA_TRANS1 OUT=NUMERIC_DATA_PIVOT;
	BY PAGE_CASE_UID  ANSWER_GROUP_SEQ_NBR;
	ID 	RDB_COLUMN_NM;
	VAR	ANSWER_TXT;
	COPY NBS_CASE_ANSWER_UID;
RUN;
PROC SQL;
CREATE TABLE NUMERIC_DATA_OUT 
	AS SELECT * FROM NUMERIC_DATA_PIVOT 
WHERE LENGTHN(_LABEL_)>0 AND PAGE_CASE_UID>0;
QUIT;
DATA NUMERIC_DATA_OUT;
SET NUMERIC_DATA_OUT;
DROP _LABEL_;
DROP _NAME_;
RUN;
proc sql;
create table Stageing_key_metadata as 
select distinct NRDBM.RDB_COLUMN_NM,NUIM.NBS_QUESTION_UID, NUIM.CODE_SET_GROUP_ID,  
NUIM.INVESTIGATION_FORM_CD,data_type,
CODE_SET_GROUP_ID,QUESTION_GROUP_SEQ_NBR,DATA_TYPE
from
NBS_ODS.NBS_RDB_METADATA NRDBM,
NBS_ODS.NBS_UI_METADATA NUIM
where NRDBM.NBS_UI_METADATA_UID=NUIM.NBS_UI_METADATA_UID
AND QUESTION_GROUP_SEQ_NBR IS not NULL;

create table STAGING_KEY AS select
	ACT_UID LENGTH =8 AS PAGE_CASE_UID 'PAGE_CASE_UID',NBS_CASE_ANSWER_UID, PHC_UIDS.LAST_CHG_TIME
FROM  Stageing_key_metadata NUIM
	INNER JOIN
	NBS_ODS.NBS_CASE_ANSWER PA 
ON	NUIM.NBS_QUESTION_UID =PA.NBS_QUESTION_UID
	INNER JOIN 
	PHC_UIDS 
ON	PHC_UIDS.PAGE_CASE_UID=PA.ACT_UID
WHERE 
ANSWER_GROUP_SEQ_NBR IS not NULL
ORDER BY 
	ACT_UID,NBS_CASE_ANSWER_UID, NUIM.CODE_SET_GROUP_ID;
quit;
PROC SORT DATA=STAGING_KEY NODUPKEY; BY PAGE_CASE_UID; RUN;
PROC SQL;
DROP TABLE NBS_RDB.S_INVESTIGATION_REPEAT;
QUIT;
PROC SORT DATA=NUMERIC_DATA_OUT; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
PROC SORT DATA=DATE_DATA_OUT; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
PROC SORT DATA=CODED_DATA_OUT; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
PROC SORT DATA=TEXT_DATA_OUT; BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR; RUN;
DATA S_INVESTIGATION_REPEAT; 
MERGE 
	NUMERIC_DATA_OUT
	DATE_DATA_OUT
	CODED_DATA_OUT
	TEXT_DATA_OUT;
BY PAGE_CASE_UID ANSWER_GROUP_SEQ_NBR;
RUN;
PROC SQL;
DROP TABLE NBS_RDB.S_INVESTIGATION_REPEAT;
QUIT;
DATA S_INVESTIGATION_REPEAT;
	SET S_INVESTIGATION_REPEAT(DROP=_NAME_ _LABEL_ );
RUN;
PROC SQL;
CREATE TABLE 
	NBS_RDB.S_INVESTIGATION_REPEAT AS 
SELECT * FROM S_INVESTIGATION_REPEAT;
QUIT;
PROC SQL;
DROP TABLE NBS_RDB.L_INVESTIGATION_REPEAT;
CREATE TABLE L_INVESTIGATION_REPEAT AS SELECT DISTINCT PAGE_CASE_UID FROM S_INVESTIGATION_REPEAT;
QUIT;
%ASSIGN_KEY (L_INVESTIGATION_REPEAT, D_INVESTIGATION_REPEAT_KEY);

%DBLOAD (L_INVESTIGATION_REPEAT, L_INVESTIGATION_REPEAT);
PROC SQL;
CREATE TABLE D_INVESTIGATION_REPEAT AS
SELECT A.*, B.D_INVESTIGATION_REPEAT_KEY FROM L_INVESTIGATION_REPEAT B LEFT OUTER JOIN S_INVESTIGATION_REPEAT A
ON A.PAGE_CASE_UID=B.PAGE_CASE_UID;
QUIT;
DATA D_INVESTIGATION_REPEAT;
	SET D_INVESTIGATION_REPEAT(DROP=NBS_CASE_ANSWER_UID);
RUN;
PROC SQL;
DROP TABLE NBS_RDB.D_INVESTIGATION_REPEAT;
QUIT;
PROC SQL;
CREATE TABLE 
	NBS_RDB.D_INVESTIGATION_REPEAT AS 
SELECT 
	* 
FROM 
	D_INVESTIGATION_REPEAT;
QUIT;
PROC DATASETS LIBRARY = WORK NOLIST;
DELETE 
	S_INVESTIGATION_REPEAT
	NUMERIC_BASE_METADATA
	DATE_DATA_PIVOT
	CODED_DATA_PIVOT
	CODED_DATA
	TEXT_DATA TEXT_DATA_OUT
	CODED_DATA_OUT CODED_TABLE 
	DATE_DATA DATE_DATA_OUT 
	TEXT_METADATA NUMERIC_METADATA 
	CODED_METADATA DATE_METADATA
	NUMERIC_BASE_DATA NUMERIC_DATA1 
	NUMERIC_DATA2 NUMERIC_DATA_MERGED 
	NUMERIC_DATA_OUT NUMERIC_DATA_PIVOT 
	NUMERIC_DATA_TRANS NUMERIC_DATA_TRANS1
	STAGING_KEY D_INVESTIGATION_REPEAT INVESTIGATION_REPEAT_INIT RUN;
QUIT;