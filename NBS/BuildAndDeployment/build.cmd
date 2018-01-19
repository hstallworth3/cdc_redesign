@echo off
cls
net stop "wildfly nedssdomain"
TortoiseProc.exe /command:update /closeonend:1 /path:"D:\SVN\NBS"
call ant -logger org.apache.tools.ant.listener.MailLogger
if exist D:\wildfly-10.0.0.Final\nedssdomain\log  rd /s /q D:\wildfly-10.0.0.Final\nedssdomain\log
if exist D:\wildfly-10.0.0.Final\nedssdomain\tmp  rd /s /q D:\wildfly-10.0.0.Final\nedssdomain\tmp
if exist D:\wildfly-10.0.0.Final\nedssdomain\work rd /s /q D:\wildfly-10.0.0.Final\nedssdomain\work
xcopy /E /y D:\NEDSS_BUILD_FILES_wildfly\Nedss D:\wildfly-10.0.0.Final\nedssdomain\Nedss
net start "wildfly nedssdomain"

::This .bat file will run the UserProfileUpdateProcess.bat, DeDuplicationSimilarBatchProcess.bat and MasterEtl.bat
D:
cd "D:\SVN\NBS\BuildAndDeployment"
call PostBuildBatchProcesses.bat
