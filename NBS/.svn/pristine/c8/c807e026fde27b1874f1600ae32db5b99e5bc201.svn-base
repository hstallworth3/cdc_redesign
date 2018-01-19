package gov.cdc.nedss.systemservice.ejb.edxdocumentejb.labutil;

import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.bean.EdxPHCRDocument;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.bean.EdxPHCRDocumentHome;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxLabInformationDT;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommandHome;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.NedssUtils;

import javax.rmi.PortableRemoteObject;

/**
 * @author Pradeep Kumar Sharma
 * @since 2012 
 */
public class EdxAutoLabInvFromInterface {
	static final LogUtils logger = new LogUtils(EdxAutoLabInvFromInterface.class.getName());

	public static void main(String args[]) {
		try {
			getSecurityObj(NEDSSConstants.ELR_LOAD_USER_ACCOUNT);
		} catch (Exception e) {
			System.out.println("EdxAutoLabInvFromInterface.main Method Exception thrown " + e.getMessage());
			e.printStackTrace();	
		}
	}

	public static void nbsCaseImportScheduler(String userName) {
		try {
			getSecurityObj(userName);
		} catch (Exception e) {
			System.out.println("EdxAutoLabInvFromInterface.nbsCaseImportScheduler Method Exception thrown " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void prcoessMessageColl(NBSSecurityObj nbsSecurityObj) {
		try {
			NedssUtils nedssUtils = new NedssUtils();
			Object phcrLookedUpObject = nedssUtils.lookupBean(JNDINames.EDX_PHCR_DOCUMENT_EJB);
			EdxPHCRDocumentHome edxPHCRDocumentHome = (EdxPHCRDocumentHome) PortableRemoteObject.narrow(phcrLookedUpObject, EdxPHCRDocumentHome.class);
			EdxPHCRDocument edxPHCRDocument = edxPHCRDocumentHome.create();

			int successCounter = 0;
			int failureCounter = 0;
			int numberOfPendingELRCases = edxPHCRDocument.getUnProcessedELRCount(nbsSecurityObj);;
			
			if (numberOfPendingELRCases ==0) {
				
			}else{
				edxPHCRDocument.resetElrAlgorithmCache(nbsSecurityObj); //in case stale
				for(int i=0; i<numberOfPendingELRCases; i++){
					EdxLabInformationDT edxLabInformationDT  = edxPHCRDocument.getUnProcessedELR(nbsSecurityObj);
					System.out.println("Processed case #"+ (i+1) +" of "+numberOfPendingELRCases+": "+edxLabInformationDT.getLocalId()+" - "+edxLabInformationDT.getStatus());
					System.out.println("Nbs_interface_uid  : "+edxLabInformationDT.getEdxActivityLogDT().getSourceUid());
					System.out.println("Message Control Id : "+edxLabInformationDT.getMessageControlID());
					System.out.println("Filler Number :      "+ edxLabInformationDT.getFillerNumber());
					System.out.println("Patient Name :       "+edxLabInformationDT.getEntityName());
					System.out.println("Final Output :       "+edxLabInformationDT.getEdxActivityLogDT().getException());
					System.out.println("");
					System.out.println("");
					System.out.println("");
					//log the system out println here
					
				}
			}
			if (successCounter > 0) {
				System.out.println(successCounter + " case(s) imported successfully.");
			}
			if (failureCounter > 0) {
				System.out.println(failureCounter + " case(s) could not be imported. Please check EDX_activity_log table for details.");
			}
		} catch (Exception e) {
			System.out.println("EdxAutoInvFromInterface.prcoessMessageColl Exception thrown " + e.getMessage());
			e.printStackTrace();
		}

	}

	private static void getSecurityObj(String userName) throws Exception {
			NedssUtils nedssUtils = new NedssUtils();
			String sBeanName = JNDINames.MAIN_CONTROL_EJB;
			Object objref = nedssUtils.lookupBean(sBeanName);
			MainSessionCommandHome home = (MainSessionCommandHome) PortableRemoteObject.narrow(objref, MainSessionCommandHome.class);
			MainSessionCommand msCommand = home.create();

			NBSSecurityObj nbsSecurityObj = msCommand.nbsSecurityLogin(userName, userName);
			prcoessMessageColl(nbsSecurityObj);
	}

}
