package gov.cdc.nedss.systemservice.ejb.edxdocumentejb.beanhelper;

import gov.cdc.nedss.act.notification.dt.NotificationDT;
import gov.cdc.nedss.act.notification.vo.NotificationVO;
import gov.cdc.nedss.act.publichealthcase.vo.PublicHealthCaseVO;
import gov.cdc.nedss.association.dt.ActRelationshipDT;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.proxy.ejb.notificationproxyejb.vo.NotificationProxyVO;
import gov.cdc.nedss.proxy.ejb.pamproxyejb.vo.PamProxyVO;
import gov.cdc.nedss.systemservice.dt.EDXActivityDetailLogDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.dt.EdxRuleAlgorothmManagerDT;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.util.EdxPHCRConstants;
import gov.cdc.nedss.systemservice.ejb.edxdocumentejb.util.EdxPHCRDocumentUtil;
import gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.util.NEDSSConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJBException;

/**
 * This is a helper class where common methods for PHCR and ELR can be pooled.
 * @author Pradeep Kuamr Sharma
 *
 */
public class EdxCommonHelper {

	public static void createNotification
	(
		EdxRuleAlgorothmManagerDT edxRuleAlgorithmManagerDT, 
		Object pageObj, 
		NBSSecurityObj nbsSecurityObj
	) throws Exception, NEDSSAppException, RemoteException {
		EDXActivityDetailLogDT edxActivityDetailLogDT;
		try{
		boolean authorized = nbsSecurityObj.getPermission(NBSBOLookup.NOTIFICATION, NBSOperationLookup.CREATE); // ADD CREATE AUTOCREATE
		if (authorized) {
			edxActivityDetailLogDT = sendNotification(pageObj, edxRuleAlgorithmManagerDT.getNndComment(), nbsSecurityObj);
			if (edxActivityDetailLogDT!=null && edxActivityDetailLogDT.getLogType().equals(EdxRuleAlgorothmManagerDT.STATUS_VAL.Failure.name())) {
				edxRuleAlgorithmManagerDT.setErrorText(EdxPHCRConstants.SUM_MSG_MISSING_FLDS+EdxPHCRConstants.NOT_STR);
				edxRuleAlgorithmManagerDT.setStatus(EdxRuleAlgorothmManagerDT.STATUS_VAL.Failure);
			}
		} else {									
			edxRuleAlgorithmManagerDT.setStatus(EdxRuleAlgorothmManagerDT.STATUS_VAL.Failure);
			edxRuleAlgorithmManagerDT.setErrorText(EdxPHCRConstants.SUM_MSG_NOTIFICATION_FAIL);
			edxActivityDetailLogDT = new EDXActivityDetailLogDT();
			String uid = nbsSecurityObj.getTheUserProfile().getTheUser().getUserID();
			edxActivityDetailLogDT.setRecordId(uid);
			edxActivityDetailLogDT.setRecordType(EdxPHCRConstants.MSG_TYPE.Notification.name());
			edxActivityDetailLogDT.setRecordName("PHCR_IMPORT");
			edxActivityDetailLogDT.setLogType(EdxRuleAlgorothmManagerDT.STATUS_VAL.Failure.name());
			edxActivityDetailLogDT.setComment(EdxPHCRConstants.DET_MSG_NOT_AUTH_1+uid+EdxPHCRConstants.DET_MSG_NOT_AUTH_2+EdxPHCRConstants.NOT_STR);
		}
		edxRuleAlgorithmManagerDT.getEdxActivityLogDT().getEDXActivityLogDTWithVocabDetails().add(edxActivityDetailLogDT);
		}
		catch(Exception ex){
			String errortext = "Exception while creating notification for PHCUid: "+edxRuleAlgorithmManagerDT.getPHCUid()+" Message: "+ex.getMessage();
			ex.printStackTrace();
			throw new NEDSSAppException(errortext,ex);
		}
	}
	public static EDXActivityDetailLogDT sendNotification(Object pageObj, String nndComment, NBSSecurityObj nbsSecurityObj) throws EJBException, RemoteException, NEDSSAppException {
		NotificationProxyVO notProxyVO = null;
		// Create the Notification object
		PublicHealthCaseVO publicHealthCaseVO;
		if (pageObj instanceof PageActProxyVO) {
			publicHealthCaseVO = ((PageActProxyVO) pageObj).getPublicHealthCaseVO();
		} else if (pageObj instanceof PamProxyVO) {
			publicHealthCaseVO = ((PamProxyVO) pageObj).getPublicHealthCaseVO();
		} else if (pageObj instanceof PublicHealthCaseVO) {
			publicHealthCaseVO = ((PublicHealthCaseVO) pageObj);
		} else {
			throw new NEDSSAppException("Cannot create Notification for unknown page type: " + pageObj.getClass().getCanonicalName());
		}
		NotificationDT notDT = new NotificationDT();
		notDT.setItNew(true);
		notDT.setNotificationUid(new Long(-1));
		notDT.setAddTime(new java.sql.Timestamp(new Date().getTime()));
		notDT.setTxt(nndComment);
		notDT.setStatusCd("A");
		notDT.setCaseClassCd(publicHealthCaseVO.getThePublicHealthCaseDT().getCaseClassCd());
		notDT.setStatusTime(new java.sql.Timestamp(new Date().getTime()));
		notDT.setVersionCtrlNbr(new Integer(1));
		notDT.setSharedInd("T");
		notDT.setCaseConditionCd(publicHealthCaseVO.getThePublicHealthCaseDT().getCd());
		notDT.setAutoResendInd("F");

		NotificationVO notVO = new NotificationVO();
		notVO.setTheNotificationDT(notDT);
		notVO.setItNew(true);

		// create the act relationship between the phc & notification
		ActRelationshipDT actDT1 = new ActRelationshipDT();
		actDT1.setItNew(true);
		actDT1.setTargetActUid(publicHealthCaseVO.getThePublicHealthCaseDT().getPublicHealthCaseUid());
		actDT1.setSourceActUid(notDT.getNotificationUid());
		actDT1.setAddTime(new java.sql.Timestamp(new Date().getTime()));
		actDT1.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
		actDT1.setSequenceNbr(new Integer(1));
		actDT1.setStatusCd("A");
		actDT1.setStatusTime(new java.sql.Timestamp(new Date().getTime()));
		actDT1.setTypeCd(NEDSSConstants.ACT106_TYP_CD);
		actDT1.setSourceClassCd(NEDSSConstants.ACT106_SRC_CLASS_CD);
		actDT1.setTargetClassCd(NEDSSConstants.ACT106_TAR_CLASS_CD);

		notProxyVO = new NotificationProxyVO();
		notProxyVO.setItNew(true);
		notProxyVO.setThePublicHealthCaseVO(publicHealthCaseVO);
		notProxyVO.setTheNotificationVO(notVO);

		ArrayList<Object> actRelColl = new ArrayList<Object>();
		actRelColl.add(0, actDT1);
		notProxyVO.setTheActRelationshipDTCollection(actRelColl);

		EDXActivityDetailLogDT eDXActivityDetailLogDT = EdxPHCRDocumentUtil.sendProxyToEJB(notProxyVO, pageObj, nbsSecurityObj);
		return eDXActivityDetailLogDT;
	}

}
