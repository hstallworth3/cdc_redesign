set date2=%date:~10,4%%date:~4,2%%date:~7,2%

@REM SET SAS REPORT DATABASE TYPE HERE
@set SAS_REPORT_DBTYPE=SQLSERVER
@IF %SAS_REPORT_DBTYPE%==NOTSET  (ECHO Cannot run ETL process. SAS_REPORT_DB_TYPE is not configured in MASTERETL.BAT.)
@IF %SAS_REPORT_DBTYPE%==NOTSET  GOTO cannotproceed
@rem Drop and Create tables prior to MasterEtl execution   
IF %SAS_REPORT_DBTYPE%==ORACLE  (GOTO true) ELSE GOTO false
:true
@rem ORACLE SETTINGS
sqlplus NBS_ODS/ods@NBSDB @D:\wildfly-10.0.0.Final\nedssdomain\Nedss\BatchFiles\ClearPHCMartTableORA.SQL > D:\wildfly-10.0.0.Final\nedssdomain\Nedss\report\log\Clean_PHC_Tables.log

GOTO END
:false 
@rem SQL SERVER SETTINGS
osql -U nbs_ods -P ods -S nedss-dbsql\tst -d nbs_odse -i D:\wildfly-10.0.0.Final\nedssdomain\Nedss\BatchFiles\ClearPHCMartTableSQL.sql > D:\wildfly-10.0.0.Final\nedssdomain\Nedss\report\log\Clean_PHC_Tables.log
GOTO END
:END	
%SAS_HOME%\sas.exe -sysin D:\wildfly-10.0.0.Final/nedssdomain/Nedss/report\dm\etl\phcmartETL.sas -print D:\wildfly-10.0.0.Final/nedssdomain/Nedss/report\log\phcmartETL.lst -log D:\wildfly-10.0.0.Final/nedssdomain/Nedss/report\log\phcmartETL.log -config  %SAS_HOME%\SASV9.CFG -autoexec D:\wildfly-10.0.0.Final/nedssdomain/Nedss/report\autoexec.sas
 
type D:\wildfly-10.0.0.Final\nedssdomain\Nedss\report\log\phcmartETL.log 

type d:\wildfly-10.0.0.Final\nedssdomain\Nedss\report\log\phcmartETL.log 