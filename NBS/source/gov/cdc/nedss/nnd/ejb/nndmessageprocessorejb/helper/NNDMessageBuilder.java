package gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.helper;

import gov.cdc.nedss.act.notification.dt.NotificationDT;
import gov.cdc.nedss.exception.NEDSSConcurrentDataException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.nnd.dt.CaseNotificationDataDT;
import gov.cdc.nedss.nnd.dt.CnTransportQOutDT;
import gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.dao.CnTransportQOutDAOImpl;
import gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.dao.NotificationMessageDAOImpl;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.CeDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.CweDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.IdDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.IsDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.NmDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.SnDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.SnunitDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.StDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.TsDataType;
import gov.cdc.nedss.nnd.intermediarymessage.NBSNNDIntermediaryMessageDocument.NBSNNDIntermediaryMessage.MessageElement.DataElement.TxDataType;
import gov.cdc.nedss.nnd.util.NNDConstantUtil;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.util.Coded;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
/**
 * Name: NNDMessageBuilder.java Description: Construct NNDIntermediaryMessage XML message using
 * XMLBeans derived from NNDIntermediaryMessage.xsd and populating with data from CaseNotificationDataDT's 
 * 
 * @author Beau Bannerman
 */
public class NNDMessageBuilder {
	private static LogUtils logger = new LogUtils(NNDMessageBuilder.class.getName());
	private static PropertyUtil propertyUtil = PropertyUtil.getInstance();
	private static MessageBuilderHelper messageBuilderHelper = new MessageBuilderHelper();
	

	
	
	
	public void createNotificationMessage(NotificationDT notificationDT,
			Long publicHealthCaseUid, String publicHealthCaseLocalId,
			String nndEntityIdentifier, NBSSecurityObj nbsSecurityObj)
			throws NEDSSConcurrentDataException, NEDSSSystemException {

		// EDX Activity log 
		NotificationMessageDAOImpl notificationMessageDAOImpl = new NotificationMessageDAOImpl();
		Long notificationUid = notificationDT.getNotificationUid();
		String notificationLocalId = notificationDT.getLocalId();
		Long interfaceUid = null;
		
		// Retrieve Participations associated with the current Investigation (via publicHealthCaseUid)
		TreeMap<Object, Object> phcEntityParticipationsMap = notificationMessageDAOImpl.getPublicHealthCaseEntityParticipations(publicHealthCaseUid);

		Collection<Object>  caseDataDTCollection  = null;

		caseDataDTCollection  = notificationMessageDAOImpl.getCaseNotificationDataDTCollection(publicHealthCaseUid, notificationDT);

		messageBuilderHelper.retrieveNonNBSAnswerData(caseDataDTCollection, phcEntityParticipationsMap, publicHealthCaseUid, notificationUid);
		
		messageBuilderHelper.checkForRequiredMessageElements(caseDataDTCollection, phcEntityParticipationsMap);

		messageBuilderHelper.codeLookupforMessageData(caseDataDTCollection);
		
		/************************Begin: New methods for inserting Vaccination data in the Intermediary message************************/
		
		if (nndEntityIdentifier != null
				&& !(nndEntityIdentifier.equals("Arbo_Case_Map_v1.0")
						|| nndEntityIdentifier.equals("Gen_Case_Map_v1.0")
						|| nndEntityIdentifier.equals("TB_Case_Map_v2.0")
						|| nndEntityIdentifier.equals("Var_Case_Map_v2.0") 
						|| nndEntityIdentifier.equals("MAL_Case_Map_v1.0"))) {

			// Read data from the nnd_metadata table, nbs_ui_metadata table,
			// etc., for the investigation_form ='VACC_FORM'
			Collection<Object> caseDataDTCollectionVacc = null;
			caseDataDTCollectionVacc = notificationMessageDAOImpl
					.getCaseNotificationDataDTCollectionVacc(
							publicHealthCaseUid, notificationDT);

			// Read data from the view
			Collection<Object> vaccionationDTCollection = null;
			vaccionationDTCollection = notificationMessageDAOImpl
					.retrieveVaccionationViewData(publicHealthCaseUid);

			// Create one caseNotificationDT per each vaccination
			Collection<Object> caseDataDTCollectionVaccWithAnswers = messageBuilderHelper
					.retrieveVaccinationAnswersFromView(
							caseDataDTCollectionVacc, vaccionationDTCollection);

			messageBuilderHelper
					.codeLookupforMessageDataVaccination(caseDataDTCollectionVaccWithAnswers);

			caseDataDTCollection.addAll(caseDataDTCollectionVaccWithAnswers);
		}
		
		/************************End: New methods for inserting Vaccination data in the Intermediary message************************/
		
		
		/************************Begin: New methods for inserting Lab data in the Intermediary message************************/
		
		//This is a flag for Pruthvi and I while we are implementing it
		boolean executeOrNot=false;
		
		if(executeOrNot){
			
			if (nndEntityIdentifier != null
					&& !(nndEntityIdentifier.equals("Arbo_Case_Map_v1.0")
							|| nndEntityIdentifier.equals("Gen_Case_Map_v1.0")
							|| nndEntityIdentifier.equals("TB_Case_Map_v2.0")
							|| nndEntityIdentifier.equals("Var_Case_Map_v2.0") 
							|| nndEntityIdentifier.equals("MAL_Case_Map_v1.0"))) {
				
			// Read data from the nnd_metadata table, nbs_ui_metadata table,
			// etc., for LABS
			Collection<Object> caseDataDTCollectionLab = null;
			caseDataDTCollectionLab = notificationMessageDAOImpl.getCaseNotificationDataDTCollectionLab(publicHealthCaseUid, notificationDT);
			
			//Retrieve data in the LaboratoryDT
			notificationMessageDAOImpl.retrieveDataInFlatTableForLabNotification(publicHealthCaseUid);
			
			//Insert data in the new flat table
			notificationMessageDAOImpl.insertDataInFlatTableForLabNotification(caseDataDTCollectionLab);
			
			//This is for SPRINT 4:			
			
			// Read data from the flat table and create the labDTCollection dynamically (like we is done for investigation data)
			Collection<Object> labDTCollection = null;
			labDTCollection = notificationMessageDAOImpl.retrieveLabViewData(publicHealthCaseUid);
				
			// Create one caseNotificationDT per each lab
			Collection<Object> caseDataDTCollectionVaccWithAnswers = messageBuilderHelper.retrieveLabAnswersFromView(caseDataDTCollectionLab, labDTCollection);
						
			messageBuilderHelper.codeLookupforMessageDataLab(caseDataDTCollectionVaccWithAnswers);
	
			caseDataDTCollection.addAll(caseDataDTCollectionVaccWithAnswers);
			}
		}
			/************************End: New methods for inserting Lab data in the Intermediary message************************/
			
			
			String nbsIntermediaryMessageXML = buildMessage(caseDataDTCollection, notificationDT, nndEntityIdentifier, publicHealthCaseLocalId);
	
			// Write NND message to file and/or CN_Transportq_out table
			messageBuilderHelper.writeNBSIntermediaryMessage(nbsIntermediaryMessageXML, nbsSecurityObj, publicHealthCaseLocalId, notificationDT, NNDConstantUtil.NND_ROOT_DIR);
		
			
		
	}

	private String buildMessage(Collection<Object> caseNotificationDataDTCollection, NotificationDT notificationDT, 
								String nndEntityIdentifier, String publicHealthCaseLocalId) {
		// Add necessary NND message elements
		addNNDNotificationInfo(caseNotificationDataDTCollection, notificationDT, nndEntityIdentifier);

		// Sort by NND Identifier and QuestionGroupSeqNbr
		List<Object> list = (ArrayList<Object> )caseNotificationDataDTCollection;
		Collections.sort(list, CaseNotificationDataDT.HL7MessageSort);

		messageBuilderHelper.populateUnits(caseNotificationDataDTCollection);

		return messageBuilderHelper.createNBSNNDIntermediaryMessageXML(caseNotificationDataDTCollection);
	}


	private void addNNDNotificationInfo(Collection<Object> caseNotificationDataDTCollection, NotificationDT notificationDT, String nndEntityIdentifier) {

		// Create DT's for notification.add_time and last_chg_time, Result Status and Message Control ID
		CaseNotificationDataDT notAddTime = new CaseNotificationDataDT();
		CaseNotificationDataDT notLastChgTime = new CaseNotificationDataDT();
		CaseNotificationDataDT notResultStatus = new CaseNotificationDataDT();
		CaseNotificationDataDT notMsgControlId = new CaseNotificationDataDT();
		CaseNotificationDataDT notNNDEntityIdentifier1 = new CaseNotificationDataDT();
		CaseNotificationDataDT notNNDEntityIdentifier2 = new CaseNotificationDataDT();

		// MSH 10.0 - Message Control ID - need Notification local id
		notMsgControlId.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_MSH_10);
		notMsgControlId.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_ST);
		notMsgControlId.setAnswerTxt(notificationDT.getLocalId());
		caseNotificationDataDTCollection.add(notMsgControlId);

		// MSH 21.0 - Entity Identifier - Condition specific
		// Note: MMG v2.0 requires a Message Profile ID (NOT115) like:
		// NND_ORU_v2.1^PHINProfileID^2.16.840.1.114222.4.10.3^ISO~GEN_Case_Map_v2.0^PHINMsgMapID^2.16.840.1.114222.4.10.4^ISO~STD_Case_Map_v1.0^PHINMsgMapID^2.16.840.1.114222.4.10.4^ISO
		
		String msgPrfl1 = null;
		String msgPrfl2 = null;
		
		Coded coded = new Coded(nndEntityIdentifier,
				"code_value_general", "NBS_MSG_PROFILE");
		 String code  = coded.getCode();
		if(code!=null && code.indexOf("^")>1){
			String[] msgProfileArray = code.split("\\^");
			msgPrfl1 = msgProfileArray[0];
			msgPrfl2 = msgProfileArray[1];
		}
		else
			msgPrfl2 = code;
		
		if (msgPrfl1!=null) {
			notNNDEntityIdentifier1.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_MSH_21);
			notNNDEntityIdentifier1.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_ST);
			notNNDEntityIdentifier1.setOrderGroupId("1"); //First member of MSH 21.1 array
			notNNDEntityIdentifier1.setAnswerTxt(msgPrfl1);
			caseNotificationDataDTCollection.add(notNNDEntityIdentifier1);
		}
		
		notNNDEntityIdentifier2.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_MSH_21);
		notNNDEntityIdentifier2.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_ST);
		notNNDEntityIdentifier2.setOrderGroupId("2"); //Second member of MSH 21.1 array
		notNNDEntityIdentifier2.setAnswerTxt(msgPrfl2);
		caseNotificationDataDTCollection.add(notNNDEntityIdentifier2);

		// OBR 25.0 - result status
		notResultStatus.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_OBR_25);
		notResultStatus.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_ID);
		notResultStatus.setAnswerTxt(messageBuilderHelper.getResultStatus(notificationDT));
		caseNotificationDataDTCollection.add(notResultStatus);

		// OBR 7.0 - add time
		notAddTime.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_OBR_7);
		notAddTime.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_TS);
		notAddTime.setAnswerTxt(notificationDT.getAddTime().toString());
		caseNotificationDataDTCollection.add(notAddTime);
		
		// OBR 22.0 - add time, if first notification to CDC (auto_resend_ind=F), otherwise last change time (Jira Task# ND-1379)
		if(notificationDT.getAutoResendInd()!=null && notificationDT.getAutoResendInd().equals(NEDSSConstants.FALSE)){
			notLastChgTime.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_OBR_22);
			notLastChgTime.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_TS);
			notLastChgTime.setAnswerTxt(notificationDT.getAddTime().toString());
			caseNotificationDataDTCollection.add(notLastChgTime);
		}else{		
		// OBR 22.0 - last change time
		notLastChgTime.setHl7SegmentField(NEDSSConstants.NND_HL7_SEGMENT_OBR_22);
		notLastChgTime.setQuestionDataTypeNND(NEDSSConstants.NND_HL7_DATATYPE_TS);
		notLastChgTime.setAnswerTxt(notificationDT.getLastChgTime().toString());
		caseNotificationDataDTCollection.add(notLastChgTime);
		}
	}

	private CnTransportQOutDT prepareCnTransportQOut(String messagePayload, 
													 NBSSecurityObj nbsSecurityObj,
													 NotificationDT notificationDT,
													 String publicHealthCaseLocalId) throws NEDSSConcurrentDataException {
		CnTransportQOutDT cnTransportQOutDT = new CnTransportQOutDT();
		Date dateTime = new Date();
		Timestamp systemTime = new Timestamp(dateTime.getTime());

		cnTransportQOutDT.setMessagePayload(messagePayload);

		cnTransportQOutDT.setVersionCtrlNbr(new Integer(1));
		cnTransportQOutDT.setRecordStatusCd(NEDSSConstants.NND_UNPROCESSED_MESSAGE);

		cnTransportQOutDT.setReportStatus(messageBuilderHelper.getResultStatus(notificationDT));

		cnTransportQOutDT.setAddUserId(new Long(Long.parseLong(nbsSecurityObj.getEntryID())));
		cnTransportQOutDT.setAddTime(systemTime);
		cnTransportQOutDT.setLastChgUserId(new Long(Long.parseLong(nbsSecurityObj.getEntryID())));
		cnTransportQOutDT.setLastChgTime(systemTime);
		cnTransportQOutDT.setRecordStatusTime(systemTime);

		cnTransportQOutDT.setNotificationUID(notificationDT.getNotificationUid());
		cnTransportQOutDT.setNotificationLocalId(notificationDT.getLocalId());
		cnTransportQOutDT.setPublicHealthCaseLocalId(publicHealthCaseLocalId);

		return cnTransportQOutDT;
	}
}