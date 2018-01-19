package gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.bean;


import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.systemservice.dt.EDXActivityLogDT;
import gov.cdc.nedss.systemservice.dt.NbsInterfaceDT;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.vo.NBSDocumentVO;
import gov.cdc.nedss.systemservice.genericXMLParser.MsgXMLMappingDT;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBObject;

public interface NbsDocument  extends EJBObject {
	
	public Collection<Object>  extractNBSInterfaceUids(NBSSecurityObj nbsSecurityObj) throws RemoteException, Exception;
	public NBSDocumentVO getNBSDocument(Long nbsDocUid, NBSSecurityObj nbsSecurityObj) throws RemoteException, Exception;
	public Long updateDocument(NBSDocumentVO nbsDocVO, NBSSecurityObj nbsSecurityObj) throws RemoteException, Exception;
	public NBSDocumentVO createNBSDocument(NbsInterfaceDT nbsInterfaceDT, NBSSecurityObj nbsSecurityObj) throws RemoteException, NEDSSAppException;
	public void createEDXActivityLog(NbsInterfaceDT nbsInterfaceDT,EDXActivityLogDT eDXActivityLogDT, NBSSecurityObj nbsSecurityObj) throws RemoteException;
	public NBSDocumentVO getNBSDocumentWithoutActRelationship(Long nbsDocUid, NBSSecurityObj nbsSecurityObj) throws RemoteException, Exception;
	public Long updateDocumentWithOutthePatient(NBSDocumentVO nbsDocVO, NBSSecurityObj nbsSecurityObj) throws RemoteException, Exception;
	public Boolean getInvestigationAssoWithDocumentColl(Long nbsDocuUid, NBSSecurityObj nbsSecurityObj) throws RemoteException, Exception;
	public void transferOwnership(Long documentUID, String newProgramAreaCode, String newJurisdictionCode,NBSSecurityObj nbsSecurityObj)throws RemoteException,NEDSSAppException;
	public Collection<Object> getUnProcessedDocuement(NBSSecurityObj nbsSecurityObj) throws RemoteException,NEDSSAppException;
	public NbsInterfaceDT extractXMLDocument(Long uid, NBSSecurityObj nbsSecurityObj)throws RemoteException, Exception;
	public ArrayList<MsgXMLMappingDT> getXMLMapping(String documentType,NBSSecurityObj nbsSecurityObj) throws RemoteException,	NEDSSAppException;
	public void parseOriginalXML(ArrayList<MsgXMLMappingDT> genericMappingList, NbsInterfaceDT nbsInterfaceDT,NBSSecurityObj nbsSecurityObj) throws RemoteException,NEDSSAppException;
	public void updateNBSInterface(NbsInterfaceDT nbsInterfaceDT,NBSSecurityObj nbsSecurityObj) throws RemoteException,	NEDSSAppException;
}



