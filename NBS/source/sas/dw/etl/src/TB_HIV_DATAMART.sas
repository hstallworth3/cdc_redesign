PROC SQL;
CREATE TABLE TB_HIV_WITH_UID AS 
SELECT D_TB_HIV.TB_PAM_UID,
D_TB_HIV.TB_PAM_UID,

D_TB_HIV.HIV_STATE_PATIENT_NUM,
D_TB_HIV.HIV_STATUS,
D_TB_HIV.HIV_CITY_CNTY_PATIENT_NUM,
D_TB_HIV_KEY
/*, 
D_TB_PAM.INVESTIGATION_LOCAL_ID*/
FROM nbs_rdb.D_TB_HIV INNER JOIN nbs_rdb.D_TB_PAM
ON 
D_TB_HIV.TB_PAM_UID=D_TB_PAM.TB_PAM_UID;
QUIT;
PROC SQL;
CREATE TABLE TB_HIV_DATAMART AS
SELECT *         
FROM TB_HIV_WITH_UID RIGHT OUTER JOIN   TB_DATAMART
	ON TB_HIV_WITH_UID.TB_PAM_UID=TB_DATAMART.TB_PAM_UID;
QUIT;
data TB_HIV_DATAMART;
set TB_HIV_DATAMART ;
IF D_TB_HIV_KEY >0 THEN D_TB_HIV_KEY = D_TB_HIV_KEY ;
else D_TB_HIV_KEY =1;
RUN;
%DBLOAD (TB_HIV_DATAMART , TB_HIV_DATAMART );