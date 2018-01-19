package gov.cdc.nedss.systemservice.ejb.edxdocumentejb.bean;

import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.systemservice.dt.NbsInterfaceDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxLabInformationDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxRuleAlgorothmManagerDT;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;
/**
 * NbsPHCRDocument: Remote interface that defines Service within NBS framework to import PHCR Document. 
 * The intent of this interface is to create NBS Objects from the predefined PHCR Document.  
 * @author: Pradeep Sharma
 * @Company: CSC
 * @since:Release 4.1
 */
public interface EdxPHCRDocument extends EJBObject{
	public EdxRuleAlgorothmManagerDT createNbsEventsFromPHCR(String datamigration, NbsInterfaceDT nbsInterfaceDT, NBSSecurityObj nbsSecurityObj)throws EJBException,RemoteException,NEDSSAppException;
	//public  Object createInvestigation(Long documentUid,  NBSSecurityObj nbsSecurityObj) throws EJBException,RemoteException,NEDSSAppException;				
	public Collection<Object> ConvertLabToInvestigation(Map<Object, Object> map, NBSSecurityObj nbsSecurityObj)throws EJBException,RemoteException,NEDSSAppException;
	//public Collection<Object> labToConditionMapping( NBSSecurityObj nbsSecurityObj)throws EJBException,RemoteException,NEDSSAppException;
	public void getEntityHashCd(NBSSecurityObj nbsSecurityObj) throws EJBException,RemoteException,NEDSSAppException;
	public void getPatientHashCd(NBSSecurityObj nbsSecurityObj) throws EJBException,RemoteException,NEDSSAppException;
	public void genPatientHashCd(NBSSecurityObj nbsSecurityObj) throws EJBException,RemoteException,NEDSSAppException;
	public EdxRuleAlgorothmManagerDT processELRService(NbsInterfaceDT nbsInterfaceDT, NBSSecurityObj nbsSecurityObj)throws EJBException,RemoteException,NEDSSAppException;
	public int getUnProcessedELRCount(NBSSecurityObj nbsSecurityObj)throws EJBException,RemoteException,NEDSSAppException;
	public EdxLabInformationDT getUnProcessedELR(NBSSecurityObj nbsSecurityObj)throws EJBException,RemoteException,NEDSSAppException;
	public void resetElrAlgorithmCache(NBSSecurityObj nbsSecurityObj) throws EJBException,RemoteException,NEDSSAppException;
	}
