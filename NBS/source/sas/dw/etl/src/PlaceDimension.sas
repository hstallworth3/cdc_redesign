PROC DATASETS LIB=WORK MEMTYPE=DATA
		KILL;
RUN;
QUIT;
  %macro assign_additional_key (ds, key);
 data &ds;
  if &key=1 then output;
  set &ds;
	&key+1;
	output;
 run;
%mend assign_additional_key;

PROC SQL;

DROP TABLE NBS_RDB.S_PLACE;
DROP TABLE NBS_RDB.L_PLACE;
DROP TABLE NBS_RDB.D_PLACE;
QUIT;
options fmtsearch=(nbsfmt);
PROC SQL;
CREATE TABLE S_PLACE_INIT AS 
SELECT 
	PLACE.PLACE_UID AS PLACE_UID 'PLACE_UID',
	PLACE.ADD_TIME AS PLACE_ADD_TIME 'PLACE_ADD_TIME',
	PLACE.ADD_USER_ID AS PLACE_ADD_USER_ID 'PLACE_ADD_USER_ID',
	PLACE.CD,
	PLACE.DESCRIPTION AS PLACE_GENERAL_COMMENTS 'PLACE_GENERAL_COMMENTS',      
	PLACE.LAST_CHG_TIME AS PLACE_LAST_CHANGE_TIME 'PLACE_LAST_CHANGE_TIME',
	PLACE.LAST_CHG_USER_ID AS PLACE_LAST_CHG_USER_ID 'PLACE_LAST_CHG_USER_ID',
	PLACE.LOCAL_ID AS PLACE_LOCAL_ID 'PLACE_LOCAL_ID',              
	PLACE.NM AS PLACE_NAME 'PLACE_NAME',
	PLACE.RECORD_STATUS_CD AS PLACE_RECORD_STATUS 'PLACE_RECORD_STATUS',
	PLACE.RECORD_STATUS_TIME AS PLACE_RECORD_STATUS_TIME 'PLACE_RECORD_STATUS_TIME',
	PLACE.STATUS_CD AS PLACE_STATUS_CD 'PLACE_STATUS_CD',
	PLACE.STATUS_TIME AS PLACE_STATUS_TIME 'PLACE_STATUS_TIME'
FROM
	NBS_ODS.PLACE;

CREATE TABLE 
	PLACE_UID_COLL 
AS 
SELECT 
	PLACE_UID  
FROM 
	S_PLACE_INIT;


CREATE TABLE  S_INITPLACE 
AS SELECT A.*, 
	B.FIRST_NM AS ADD_USER_FIRST_NAME 'ADD_USER_FIRST_NAME', 
	B.LAST_NM AS ADD_USER_LAST_NAME 'ADD_USER_LAST_NAME', 
	C.FIRST_NM AS CHG_USER_FIRST_NAME 'CHG_USER_FIRST_NAME', 
	C.LAST_NM AS CHG_USER_LAST_NAME 'CHG_USER_LAST_NAME' 
FROM
	S_PLACE_INIT A 
LEFT OUTER JOIN 
	NBS_RDB.USER_PROFILE B
ON 
	A.PLACE_ADD_USER_ID=B.NEDSS_ENTRY_ID
LEFT OUTER JOIN 
	NBS_RDB.USER_PROFILE C
ON 
	A.PLACE_ADD_USER_ID=C.NEDSS_ENTRY_ID;
QUIT;
DATA S_INITPLACE;
SET S_INITPLACE;
  	IF 
		PLACE_RECORD_STATUS = '' 
		THEN 
			PLACE_RECORD_STATUS = 'ACTIVE';
  	IF 
		PLACE_RECORD_STATUS = 'LOG_DEL' 
		THEN 
			PLACE_RECORD_STATUS = 'INACTIVE' ;
  	IF 
		LENGTH(COMPRESS(ADD_USER_FIRST_NAME))> 0 AND LENGTHN(COMPRESS(ADD_USER_LAST_NAME))>0 
		THEN 
			PLACE_ADDED_BY= TRIM(ADD_USER_LAST_NAME)|| ', ' ||TRIM(ADD_USER_FIRST_NAME);
	ELSE IF 
		LENGTHN(COMPRESS(ADD_USER_FIRST_NAME))> 0 
		THEN 
			PLACE_ADDED_BY= TRIM(ADD_USER_FIRST_NAME);
	ELSE IF 
		LENGTHN(COMPRESS(ADD_USER_LAST_NAME))> 0 
		THEN 
			PLACE_ADDED_BY= TRIM(ADD_USER_LAST_NAME);
	IF 
		LENGTH(COMPRESS(CHG_USER_FIRST_NAME))> 0 AND LENGTHN(COMPRESS(CHG_USER_LAST_NAME))>0 
		THEN 
			PLACE_LAST_UPDATED_BY= TRIM(CHG_USER_LAST_NAME)|| ', ' ||TRIM(CHG_USER_FIRST_NAME);
		ELSE IF 
			LENGTHN(COMPRESS(CHG_USER_FIRST_NAME))> 0 
		THEN 
			PLACE_LAST_UPDATED_BY= TRIM(CHG_USER_FIRST_NAME);
		ELSE IF 
			LENGTHN(COMPRESS(CHG_USER_LAST_NAME))> 0 
		THEN 
			PLACE_LAST_UPDATED_BY= TRIM(CHG_USER_LAST_NAME);
	PLACE_TYPE_DESCRIPTION=PUT(CD,$PL_TPE.);
DROP CD ADD_USER_FIRST_NAME ADD_USER_LAST_NAME CHG_USER_LAST_NAME CHG_USER_FIRST_NAME;
RUN;
PROC DATASETS 
LIBRARY = WORK NOLIST;
	DELETE S_place_init;
	RUN;
QUIT;
PROC SORT DATA=S_INITPLACE NODUPKEY; BY PLACE_UID; RUN;
PROC SQL;
CREATE TABLE 
	QEC_ENTITY_ID 
AS 
SELECT 
DISTINCT 
	PLACE_UID, 
	ROOT_EXTENSION_TXT, 
	ASSIGNING_AUTHORITY_CD  
FROM 
	PLACE_UID_COLL 
LEFT OUTER JOIN 
	NBS_ODS.ENTITY_ID
ON 
	PLACE_UID_COLL.PLACE_UID=ENTITY_ID.ENTITY_UID
AND 
	ENTITY_ID.TYPE_CD = 'QEC';
QUIT;
PROC SORT DATA=QEC_ENTITY_ID NODUPKEY; BY PLACE_UID; RUN;
PROC SQL;
CREATE TABLE 
	S_INITPLACE_B 
AS 
SELECT 
	S_INITPLACE.*, 
	QEC_ENTITY_ID.ROOT_EXTENSION_TXT 
AS 
	PLACE_QUICK_CODE 'PLACE_QUICK_CODE'
FROM 
	S_INITPLACE 
LEFT OUTER JOIN 
	QEC_ENTITY_ID
ON  
	S_INITPLACE.PLACE_UID= QEC_ENTITY_ID.PLACE_UID;
QUIT;
PROC SORT DATA=S_INITPLACE_B NODUPKEY; BY PLACE_UID; RUN;
PROC SQL;
CREATE TABLE 
	S_PLACE_POSTALA AS
SELECT 
PLACE_UID_COLL.PLACE_UID, 
	POSTAL_LOCATOR.POSTAL_LOCATOR_UID 
		AS PLACE_POSTAL_UID 'PLACE_POSTAL_UID',
	POSTAL_LOCATOR.CITY_DESC_TXT 
		AS PLACE_CITY 'PLACE_CITY',             
	POSTAL_LOCATOR.CNTRY_CD	
		AS PLACE_COUNTRY 'PLACE_COUNTRY',            
	POSTAL_LOCATOR.CNTY_CD	
		AS PLACE_COUNTY_CODE 'PLACE_COUNTY_CODE',              
	POSTAL_LOCATOR.STATE_CD	
		AS PLACE_STATE_CODE 'PLACE_STATE_CODE',               
	POSTAL_LOCATOR.STREET_ADDR1 
		AS PLACE_STREET_ADDRESS_1 'PLACE_STREET_ADDRESS_1',
	POSTAL_LOCATOR.STREET_ADDR2	
		AS PLACE_STREET_ADDRESS_2 'PLACE_STREET_ADDRESS_2',
	POSTAL_LOCATOR.ZIP_CD 
		AS PLACE_ZIP 'PLACE_ZIP',
	STATE_CODE.CODE_DESC_TXT 
		AS PLACE_STATE_DESC 'PLACE_STATE_DESC',
	STATE_COUNTY_CODE_VALUE.CODE_DESC_TXT 
		AS PLACE_COUNTY_DESC 'PLACE_COUNTY_DESC',
	COUNTRY_CODE.CODE_SHORT_DESC_TXT 
		AS PLACE_COUNTRY_DESC 'PLACE_COUNTRY_DESC',
	ENTITY_LOCATOR_PARTICIPATION.LOCATOR_DESC_TXT 
		AS PLACE_ADDRESS_COMMENTS 'PLACE_ADDRESS_COMMENTS',
	ENTITY_LOCATOR_PARTICIPATION.ENTITY_UID
FROM 
	PLACE_UID_COLL 
LEFT OUTER JOIN 
	NBS_ODS.ENTITY_LOCATOR_PARTICIPATION
ON 
	PLACE_UID_COLL.PLACE_UID= ENTITY_LOCATOR_PARTICIPATION.ENTITY_UID
LEFT OUTER JOIN 
	NBS_ODS.POSTAL_LOCATOR 
ON 
	ENTITY_LOCATOR_PARTICIPATION.LOCATOR_UID=POSTAL_LOCATOR.POSTAL_LOCATOR_UID
LEFT OUTER JOIN 
	NBS_SRT.STATE_CODE
ON 
	STATE_CODE.STATE_CD=POSTAL_LOCATOR.STATE_CD
LEFT OUTER JOIN 
	NBS_SRT.COUNTRY_CODE
ON 
	COUNTRY_CODE.CODE=POSTAL_LOCATOR.CNTRY_CD
LEFT OUTER JOIN 
	NBS_SRT.STATE_COUNTY_CODE_VALUE
ON 
	STATE_COUNTY_CODE_VALUE.CODE=POSTAL_LOCATOR.CNTY_CD	
AND 
	ENTITY_LOCATOR_PARTICIPATION.USE_CD='WP'
AND 
	ENTITY_LOCATOR_PARTICIPATION.CD='PLC'
AND 
	ENTITY_LOCATOR_PARTICIPATION.CLASS_CD='PST';
QUIT;
DATA S_PLACE_POSTALB;
SET S_PLACE_POSTALA;
PLACE_POSTAL_UID='';
		  PLACE_CITY  ='';          
		  PLACE_COUNTRY  ='';            
		  PLACE_COUNTY_CODE  ='';              
		  PLACE_STATE_CODE  ='';               
		  PLACE_STREET_ADDRESS_1  ='';
		  PLACE_STREET_ADDRESS_2  ='';
		  PLACE_ZIP  ='';
		  PLACE_STATE_DESC  ='';
		  PLACE_COUNTY_DESC  ='';
		  PLACE_COUNTRY_DESC ='';
		  PLACE_ADDRESS_COMMENTS ='';
RUN;
PROC SORT DATA=S_PLACE_POSTALB NODUPKEY; BY PLACE_UID; RUN;
PROC SQL;
CREATE TABLE S_PLACE_POSTAL AS SELECT * FROM S_PLACE_POSTALA 
UNION SELECT * FROM S_PLACE_POSTALB;
QUIT;
options fmtsearch=(nbsfmt);
DATA S_PLACE_POSTAL;
SET S_PLACE_POSTAL;
IF 
	LENGTHN(TRIM(PLACE_STATE_DESC))>1 
	THEN 
		PLACE_STATE=PLACE_STATE_DESC;
IF 
	LENGTHN(TRIM(PLACE_COUNTY_DESC))>1 
	THEN 
		PLACE_COUNTY=PLACE_COUNTY_DESC;
IF 
	LENGTHN(TRIM(PLACE_COUNTRY_DESC))>1 
	THEN 
		PLACE_COUNTRY=PLACE_COUNTRY_DESC;
DROP ENTITY_UID;
RUN;
/*PROC SORT DATA=S_PLACE_POSTAL NODUPKEY; BY PLACE_POSTAL_UID; RUN;*/

PROC SQL;
CREATE TABLE 
	S_PLACE_TELE
AS
SELECT 
DISTINCT
	PLACE_UID_COLL.PLACE_UID,
	TELE_LOCATOR.TELE_LOCATOR_UID 
		AS PLACE_TELE_LOCATOR_UID 'PLACE_TELE_LOCATOR_UID',
	ENTITY_LOCATOR_PARTICIPATION.ENTITY_UID AS ENTITY_UID 'ENTITY_UID',
	TELE_LOCATOR.EXTENSION_TXT 
		AS PLACE_PHONE_EXT 'PLACE_PHONE_EXT',        
	TELE_LOCATOR.PHONE_NBR_TXT 
		AS PLACE_PHONE 'PLACE_PHONE', 
	TELE_LOCATOR.EMAIL_ADDRESS 
		AS PLACE_EMAIL 'PLACE_EMAIL',
	ENTITY_LOCATOR_PARTICIPATION.LOCATOR_DESC_TXT 
		AS PLACE_PHONE_COMMENTS 'PLACE_PHONE_COMMENTS',
		ENTITY_LOCATOR_PARTICIPATION.USE_CD,
		ENTITY_LOCATOR_PARTICIPATION.CD
FROM 
	PLACE_UID_COLL 
LEFT JOIN 
	NBS_ODS.ENTITY_LOCATOR_PARTICIPATION
ON 
	PLACE_UID_COLL.PLACE_UID= ENTITY_LOCATOR_PARTICIPATION.ENTITY_UID
LEFT JOIN 
	NBS_ODS.TELE_LOCATOR 
ON 
	ENTITY_LOCATOR_PARTICIPATION.LOCATOR_UID=TELE_LOCATOR.TELE_LOCATOR_UID
AND/* 
	ENTITY_LOCATOR_PARTICIPATION.USE_CD='WP'
AND 
	ENTITY_LOCATOR_PARTICIPATION.CD='PH'
AND */
	ENTITY_LOCATOR_PARTICIPATION.CLASS_CD='TELE';
QUIT;
DATA S_PLACE_TELE;
SET S_PLACE_TELE;
PLACE_TELE_TYPE=PUT(CD,$T_TEL_PLC.);
PLACE_TELE_USE=PUT(USE_CD,$U_TEL_PLC.);

drop use_cd use_cd;
RUN;


/*PROC SQL;
CREATE TABLE 
	S_PLACE_TELE_CELL 
AS
SELECT 
DISTINCT
	TELE_LOCATOR.TELE_LOCATOR_UID 
		AS PLACE_TELE_LOCATOR_UID 'PLACE_TELE_LOCATOR_UID',
	ENTITY_LOCATOR_PARTICIPATION.ENTITY_UID,
	TELE_LOCATOR.PHONE_NBR_TXT 
		AS PLACE_PHONE_CELL 'PLACE_PHONE_CELL',
		PLACE_UID
FROM
	PLACE_UID_COLL 
INNER JOIN 
	NBS_ODS.ENTITY_LOCATOR_PARTICIPATION
ON 
	PLACE_UID_COLL.PLACE_UID= ENTITY_LOCATOR_PARTICIPATION.ENTITY_UID
INNER JOIN 
	NBS_ODS.TELE_LOCATOR 
ON 
	ENTITY_LOCATOR_PARTICIPATION.LOCATOR_UID=TELE_LOCATOR.TELE_LOCATOR_UID
WHERE  
	ENTITY_LOCATOR_PARTICIPATION.USE_CD='WP'
AND 
	ENTITY_LOCATOR_PARTICIPATION.CD='CP'
AND 
	ENTITY_LOCATOR_PARTICIPATION.CLASS_CD='TELE';
QUIT;
PROC SORT DATA=S_PLACE_TELE_CELL NODUPKEY; BY PLACE_TELE_LOCATOR_UID; RUN;
/*DATA S_PLACE_TELE;
MERGE S_PLACE_TELE_WORK S_PLACE_TELE_CELL;
drop ENTITY_UID;
RUN;
PROC SQL;
CREATE TABLE S_PLACE_TELE AS SELECT 
S_PLACE_TELE_WORK.*,  S_PLACE_TELE_CELL.PLACE_PHONE_CELL  FROM S_PLACE_TELE_WORK FULL JOIN S_PLACE_TELE_CELL
ON S_PLACE_TELE_WORK.PLACE_UID=S_PLACE_TELE_CELL.PLACE_UID;
QUIT;
*/

PROC SQL;
CREATE TABLE 
	S_PLACE_B 
AS SELECT 
 S_INITPLACE_B.PLACE_UID,S_INITPLACE_B.PLACE_ADD_TIME,S_INITPLACE_B.PLACE_ADD_USER_ID,
S_INITPLACE_B.PLACE_GENERAL_COMMENTS,S_INITPLACE_B.PLACE_LAST_CHANGE_TIME,
S_INITPLACE_B.PLACE_last_chg_user_id,S_INITPLACE_B.PLACE_LOCAL_ID,
S_INITPLACE_B.PLACE_NAME,S_INITPLACE_B.PLACE_RECORD_STATUS,S_INITPLACE_B.PLACE_RECORD_STATUS_TIME,
S_INITPLACE_B.PLACE_STATUS_CD,S_INITPLACE_B.PLACE_STATUS_TIME,
S_INITPLACE_B.PLACE_ADDED_BY,S_INITPLACE_B.PLACE_LAST_UPDATED_BY,S_INITPLACE_B.PLACE_TYPE_DESCRIPTION,
S_INITPLACE_B.PLACE_QUICK_CODE,
	PLACE_ADDRESS_COMMENTS,
	PLACE_CITY,
	PLACE_COUNTRY,
	PLACE_COUNTRY_DESC,
	PLACE_COUNTY_CODE,
	PLACE_COUNTY_DESC,
	PLACE_EMAIL,
	PLACE_PHONE,
	PLACE_PHONE_COMMENTS,
	PLACE_PHONE_EXT,
	PLACE_POSTAL_UID,
	PLACE_STATE_CODE,
	PLACE_STATE_DESC,
	PLACE_STREET_ADDRESS_1,
	PLACE_STREET_ADDRESS_2,
	PLACE_TELE_LOCATOR_UID,
	PLACE_TELE_TYPE,
	PLACE_TELE_USE,
	PLACE_ZIP
FROM 
	S_INITPLACE_B
	LEFT OUTER JOIN  S_PLACE_POSTAL ON 
S_INITPLACE_B.PLACE_UID = S_PLACE_POSTAL.PLACE_UID 
	LEFT OUTER JOIN S_PLACE_TELE ON 
S_INITPLACE_B.PLACE_UID = S_PLACE_TELE.PLACE_UID;
QUIT;
DATA S_PLACE_B;
SET S_PLACE_B;
length PLACE_LOCATOR_UID $30;
	PLACE_LOCATOR_UID = COMPRESS(PLACE_UID) || '^' || COMPRESS(PLACE_POSTAL_UID) || '^' || COMPRESS(PLACE_TELE_LOCATOR_UID);
	 PLACE_LOCATOR_UID=tranwrd(PLACE_LOCATOR_UID, '^.', '^');
RUN;
PROC SQL;
CREATE TABLE S_PLACE AS 
SELECT 
PLACE_ADD_TIME,
PLACE_ADD_USER_ID,
PLACE_ADDED_BY,
PLACE_ADDRESS_COMMENTS,
PLACE_CITY,
PLACE_COUNTRY,
PLACE_COUNTRY_DESC,
PLACE_COUNTY_CODE,
PLACE_COUNTY_DESC,
PLACE_EMAIL,
PLACE_GENERAL_COMMENTS,
PLACE_LAST_CHANGE_TIME,
PLACE_LAST_CHG_USER_ID,
PLACE_LAST_UPDATED_BY,
PLACE_LOCAL_ID,
PLACE_LOCATOR_UID,
PLACE_NAME,
PLACE_PHONE,
PLACE_PHONE_COMMENTS,
PLACE_PHONE_EXT,
PLACE_POSTAL_UID,
PLACE_QUICK_CODE,
PLACE_RECORD_STATUS,
PLACE_RECORD_STATUS_TIME,
PLACE_STATE_CODE,
PLACE_STATE_DESC,
PLACE_STATUS_CD,
PLACE_STATUS_TIME,
PLACE_STREET_ADDRESS_1,
PLACE_STREET_ADDRESS_2,
PLACE_TELE_LOCATOR_UID,
PLACE_TELE_TYPE,
PLACE_TELE_USE,
PLACE_TYPE_DESCRIPTION,
PLACE_UID,
PLACE_ZIP
FROM S_PLACE_B;
QUIT;
PROC SORT DATA=S_PLACE NODUPKEY; BY PLACE_LOCATOR_UID; RUN;

%DBLOAD (S_PLACE, S_PLACE);

PROC SQL;
CREATE TABLE L_PLACE AS SELECT DISTINCT
PLACE_LOCATOR_UID, PLACE_UID
FROM S_PLACE;
QUIT;
%ASSIGN_ADDITIONAL_KEY (L_PLACE, PLACE_KEY);
%DBLOAD (L_PLACE, L_PLACE);
PROC SQL;
CREATE TABLE 
	D_PLACE 
AS SELECT 
	 PLACE_KEY, S_PLACE.*
FROM 
	S_PLACE INNER JOIN L_PLACE ON
S_PLACE.PLACE_LOCATOR_UID = L_PLACE.PLACE_LOCATOR_UID;
QUIT;
%DBLOAD (D_PLACE, D_PLACE);
/*
PROC DATASETS LIB=WORK MEMTYPE=DATA
		KILL;
RUN;
QUIT; */