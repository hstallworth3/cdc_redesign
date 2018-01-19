package gov.cdc.nedss.nnd.ejb.nndmessageprocessorejb.helper;

import gov.cdc.nedss.act.actid.dt.ActIdDT;
import gov.cdc.nedss.act.notification.dt.NotificationDT;
import gov.cdc.nedss.act.observation.dt.ObsValueCodedDT;
import gov.cdc.nedss.act.observation.dt.ObsValueDateDT;
import gov.cdc.nedss.act.observation.dt.ObsValueNumericDT;
import gov.cdc.nedss.act.observation.dt.ObsValueTxtDT;
import gov.cdc.nedss.act.observation.dt.ObservationDT;
import gov.cdc.nedss.act.observation.vo.ObservationVO;
import gov.cdc.nedss.act.publichealthcase.dt.PublicHealthCaseDT;
import gov.cdc.nedss.association.dt.ActRelationshipDT;
import gov.cdc.nedss.association.dt.ParticipationDT;
import gov.cdc.nedss.entity.person.vo.PersonVO;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dao.PamConversionDAO;
import gov.cdc.nedss.systemservice.ejb.pamconversionejb.dt.NBSConversionMappingDT;
import gov.cdc.nedss.systemservice.ejb.questionmapejb.dt.NbsQuestionMetadata;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.uidgenerator.UidClassCodes;
import gov.cdc.nedss.systemservice.uidgenerator.UidGeneratorHelper;
import gov.cdc.nedss.systemservice.util.DropDownCodeDT;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.NedssUtils;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageConstants;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageLoadUtil;
import gov.cdc.nedss.webapp.nbs.action.place.PlaceUtil;
import gov.cdc.nedss.webapp.nbs.form.page.PageForm;
import gov.cdc.nedss.webapp.nbs.form.pam.FormField;
import gov.cdc.nedss.webapp.nbs.form.util.BaseForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.QuestionsCache;
import gov.cdc.nedss.nnd.util.NNDConstantUtil;
import gov.cdc.nedss.page.ejb.pageproxyejb.bean.PageProxy;
import gov.cdc.nedss.page.ejb.pageproxyejb.bean.PageProxyHome;
import gov.cdc.nedss.page.ejb.pageproxyejb.dt.NbsAnswerDT;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.PageProxyVO;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.pagemanagement.ejb.pagemanagementproxyejb.bean.PageManagementProxy;
import gov.cdc.nedss.pagemanagement.ejb.pagemanagementproxyejb.bean.PageManagementProxyHome;
import gov.cdc.nedss.pagemanagement.util.PageMetaConstants;
import gov.cdc.nedss.pagemanagement.wa.dao.PageManagementDAOImpl;
import gov.cdc.nedss.pagemanagement.wa.dt.BatchEntry;
import gov.cdc.nedss.pagemanagement.wa.dt.WaTemplateDT;
import gov.cdc.nedss.pam.act.NbsCaseAnswerDT;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.InvestigationProxyVO;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpSession;


/**
 * PageBuilderToMasterMessageUtil - New Message Mapping Guides such as Generic V2 and Hepatitis
 *  are not available and/or supported by the CDC. As a stop gap measure, we are using the old
 *  transport mechanism, the Master Message to send notifications.
 *  The key is the nndEntityIdentifier in the condition table. If the key == 'LGCY_MSTR_MSG'
 *  the nbs_conversion_mapping table is used to map identifiers and codes to the
 *  ones expected by the Master Message. This table is usually used for porting the reverse is used
 *  in this case to take the Case from the new Page Proxy VO format to the old Investigation Proxy VO format.
 *  Note that we are not making any changes to the Master Message processing. NBS_MSGOUTE database
 *  MsgOut_Message table stores the XML.The NNDM process takes this XML and applies XSLs in
 *  \source\nndm\styles to filter out questions which are not sent. A batch file automatically runs based on
 *  the parameters in the nndmConfig.properties file. The result is stored in the NBS_MSGOUTE TransportQ_out table.
 *  From there it is periodically picked up by the CDC.
 *
 * @author Gregory Tucker
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: SRA International</p>
 * PageBuilderToMasterMessageUtil.java
 * Jan 17, 2015
 * @version 0.9
 */

public class PageBuilderToMasterMessageUtil {


	static final LogUtils logger = new LogUtils(PageBuilderToMasterMessageUtil.class.getName());
	private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
	private static Map<Object, Object> questionMap = null;  //Page Builder Question metadata
	private static Map<Object, Object> questionKeyMap = null; //Page Builder UID to Key
	private static Map<String, ArrayList> batchListMap = null; //list Of conversion metadata where conversion type is "ToBatch"
	private static Map<Object, Object> fromQuesIdMap = null; // conversion metadata in from question ID[value], seldom used

	/**
	 * migratePageProxyToLegacyInvestigationProxy - this is the top level control routine,
	 * we add elements from the Page Proxy back into the legacy Investigation Proxy in case
	 * the elements are needed by the Master Message.
	 *
	 * @param publicHealthCaseUid
	 * @param notificationDT
	 * @param investigationProxyVO
	 * @param nbsSecurityObj
	 * @throws NEDSSAppException
	 */
	public static void migratePageProxyToLegacyInvestigationProxy(
				Long publicHealthCaseUid,
				NotificationDT notificationDT,
				InvestigationProxyVO investigationProxyVO,
				NBSSecurityObj nbsSecurityObj) throws NEDSSAppException {

		logger.debug("\n migratePageProxyToLegacyInvestigationProxy: begin process to reverse migrate some of the question answers \n");


		//add the Observations from the Case Answers
		String formCd = getPageFormCode(notificationDT.getCaseConditionCd(),nbsSecurityObj);
		if (formCd == null || formCd.isEmpty()) {
			logger.error("NND PageBuilder to Master Message - Error -Can't get form code for Condition code " + notificationDT.getCaseConditionCd());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Error -Can't get form code for Condition code");
		}
	  // we need the nbs_conversion_mapping table populated to do the reverse mapping
		HashMap<String,NBSConversionMappingDT> toQuesIdMap = getPamComversionMetadata(notificationDT.getCaseConditionCd());
		if (toQuesIdMap == null || toQuesIdMap.isEmpty()) {
			logger.error("NND PageBuilder to Master Message - Error -NBS Conversion Mapping table not populated for Condition code " + notificationDT.getCaseConditionCd());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Error -NBS Conversion Mapping table not populated for Condition code " + notificationDT.getCaseConditionCd());
		}
	// we also need the PageBuilder wa_ui_question metadata in a few places
		loadQuestionMetadata(formCd);

	// get the PageBuilder proxy
		PageActProxyVO pageActProxyVO = (PageActProxyVO)getPageProxyObject(publicHealthCaseUid, nbsSecurityObj);
		if (pageActProxyVO == null) {
			logger.error("NND PageBuilder to Master Message - Can't retrieve PageProxyVO for  " + publicHealthCaseUid.toString());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Can't retrieve PageProxyVO for  " + publicHealthCaseUid.toString());
		}

		
		if (investigationProxyVO.getTheObservationVOCollection() == null)
			investigationProxyVO.setTheObservationVOCollection(new ArrayList <ObservationVO> ());
		String formCode = getLegacyFormCode(investigationProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getCd()); //i.e.  INV_FORM_HEPA;
		ObservationVO formLevelObservation = createFormLevelObservationVO(formCode, investigationProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLastChgTime());
		investigationProxyVO.getTheObservationVOCollection().add(formLevelObservation);
		if (investigationProxyVO.getPublicHealthCaseVO().getTheActRelationshipDTCollection() == null)
				investigationProxyVO.getPublicHealthCaseVO().setTheActRelationshipDTCollection(new ArrayList <Object> ());
		ActRelationshipDT formActRelationshipDT = populateActRelationshipForFormToCase(
				formLevelObservation.getTheObservationDT().getUid(),
				investigationProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT());
		investigationProxyVO.getPublicHealthCaseVO().getTheActRelationshipDTCollection().add(formActRelationshipDT);


 	//cycle through looking for Page Builder Page Answers and Public Health Case fields to Obs
 	//and create any observations that may be needed
		try {
			processConversionMapValuesToObservations(investigationProxyVO,
								formLevelObservation,
								pageActProxyVO,
								toQuesIdMap);
		} catch (Exception e) {
			logger.error("Exception processing Conversion Map Values to Observations: " + e.getMessage());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Exception processing Conversion Map Values to Observations for PHCUid = " + publicHealthCaseUid.toString());
		}

		//cycle through looking for Repeating Batch Answers looking for Batch to Multi and Batch to Question
	 	//and create any observations that may be needed
		try {
			processRepeatingBatchMapValuesToObservations (formCd,
								investigationProxyVO,
								formLevelObservation,
								pageActProxyVO,
								toQuesIdMap);
		} catch (Exception e) {
			logger.error("Exception processing Repeating Batch Items to Observations" + e.getMessage());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Exception processing Repeating Batch Items for PHCUid = " + publicHealthCaseUid.toString());
		}

		//Look for specific fields that need special processing
	 	//and create any observations that may be needed
		try {
			checkIfAnySpecialFieldsPresent(investigationProxyVO,
								formLevelObservation,
								pageActProxyVO,
								toQuesIdMap);
		} catch (Exception e) {
			logger.error("Exception processing Special Field Items to Observations" + e.getMessage());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Exception processing Special Field Items for PHCUid = " + publicHealthCaseUid.toString());
		}
		try {
			cleanupActIds(investigationProxyVO);
		} catch (Exception e) {
			logger.error("Exception removing Act Ids with no rootExtentionTxt " + e.getMessage());
			throw new NEDSSAppException("NND PageBuilder to Master Message - Exception cleaning ActIdsDT for PHCUid = " + publicHealthCaseUid.toString());
		}
		
		logger.debug("\n migratePageProxyToLegacyInvestigationProxy: end of process to reverse migrate question answers to observations \n");
		return;
	}



	/**
	 * PageBuilder creates empty ActIds which have no id (rootExtensionTxt);
	 * @param investigationProxyVO
	 */
	private static void cleanupActIds(InvestigationProxyVO investigationProxyVO) {
		Collection<Object> actIdColl = investigationProxyVO.getPublicHealthCaseVO().getTheActIdDTCollection();
		Iterator actIdIter = actIdColl.iterator();
		while (actIdIter.hasNext()) {
			ActIdDT actIdDT = (ActIdDT) actIdIter.next();
			if (actIdDT.getRootExtensionTxt() == null || actIdDT.getRootExtensionTxt().isEmpty()) {
				actIdIter.remove();
				logger.debug("cleanupActIds - removing ActId with no identifier");
			}
		}
	}

	/**
	 * processConversionMapValuesToObservations - Read through the nbs_conversion_mapping table and
	 * map from the 'to location' to the 'from location'.
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param pageActProxyVO
	 * @param toQuesIdMap
	 */
	private static void processConversionMapValuesToObservations(
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation, PageActProxyVO pageActProxyVO,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap) {

		HashSet<String> listOfProcessedQuestions = new HashSet<String>();
		Timestamp addTime = pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getAddTime();
	    Timestamp lastChgTime = pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT().getLastChgTime();

		Iterator<String> convertIter = toQuesIdMap.keySet().iterator();
		while (convertIter.hasNext()) {
			String convertMapKey = (String) convertIter.next();
			NBSConversionMappingDT convertMetaData = toQuesIdMap.get(convertMapKey);
			//if already processed this one - skip
			if (listOfProcessedQuestions.contains(convertMetaData.getToQuestionId().trim()))
				continue;
			//if batch item skip, we'll process these later walking through the repeating batch map
			if (convertMetaData.getConversionType() != null && convertMetaData.getConversionType().contains("ToBatch"))
				continue;
			logger.debug("processConversionMapValuesToObservations: start convert of " + convertMetaData.getToQuestionId() + "\n");
			NbsQuestionMetadata quesMetaData = (NbsQuestionMetadata) questionMap.get(convertMetaData.getToQuestionId());
			String fromLoc = convertMetaData.getFromDbLocation();
			String toLoc = convertMetaData.getToDbLocation();
			//The From for us is the To because we are doing a reverse migration (To -> From)
			if (fromLoc.toLowerCase().startsWith("obs")) {
				// the data is going to an Observation
				Object fieldValue = null;

				if (toLoc.toLowerCase().startsWith("public_health_case")) {
						logger.debug("Move data from PublicHealthCase to Observation ");
						fieldValue = getValueForDataLocation(toLoc,pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT());
						if (fieldValue != null)
							addObservationFromFieldValue(fieldValue, convertMetaData, toQuesIdMap, quesMetaData, addTime, lastChgTime, formLevelObservation, investigationProxyVO);

				} else if (toLoc.toLowerCase().contains("answer") &&
						pageActProxyVO.getPageVO().getPamAnswerDTMap() != null) { //could be nothing in the map
					String questionIdentifier = convertMetaData.getToQuestionId();
					if (quesMetaData == null) {
						logger.warn("**** Conversion Metadata has invalid To Field for " + convertMetaData.getToQuestionId());
						continue;
					}
					Long nbsQuestionUid = quesMetaData.getNbsQuestionUid();
					ArrayList<NbsCaseAnswerDT> theAnswerArray = null;
					NbsCaseAnswerDT theAnsDT = null;
					//see if there is data for this question - will be arrayList if multi-select
					if (pageActProxyVO.getPageVO().getPamAnswerDTMap().get(nbsQuestionUid) != null) {
							String otherValue = null;
							if (pageActProxyVO.getPageVO().getPamAnswerDTMap().get(nbsQuestionUid).getClass().getName().equals("java.util.ArrayList")) {
								theAnswerArray = (ArrayList<NbsCaseAnswerDT>) pageActProxyVO.getPageVO().getPamAnswerDTMap().get(nbsQuestionUid);
							    addMultiSelectObservation(investigationProxyVO, formLevelObservation, quesMetaData, convertMetaData, toQuesIdMap, theAnswerArray);
							} else {
								theAnsDT = (NbsCaseAnswerDT) pageActProxyVO.getPageVO().getPamAnswerDTMap().get(nbsQuestionUid);
								addObservationFromCaseAnswer(investigationProxyVO, formLevelObservation, toQuesIdMap, convertMetaData, quesMetaData, theAnsDT);
							}

					} //if answer present in Map

				} //if answer present in Map

			}  else if (fromLoc.toLowerCase().startsWith("participation")) {
				if (toLoc.toLowerCase().startsWith("public_health_case")) {
					logger.debug("Move data from PublicHealthCase to Participation ");
					Object fieldValue = getValueForDataLocation(toLoc,pageActProxyVO.getPublicHealthCaseVO().getThePublicHealthCaseDT());
					if (fieldValue != null)
						updateParticipationFromFieldValue(investigationProxyVO, convertMetaData, quesMetaData, fieldValue);
				} else logger.warn("Warning: participation type not supported for: " + toLoc ); //from public_health_case

			} else logger.warn("Warning: in processConversionMapValuesToObservations found unsupported From type: " + fromLoc );

		listOfProcessedQuestions.add(convertMetaData.getToQuestionId().trim());
		logger.debug("processConversionMapValuesToObservations: end convert of " + convertMetaData.getToQuestionId() + "\n");
		} // nbs_conversion_mapping has next
		return;
	}

	/**
	 * processRepeatingBatchMapValuesToObservations -
	 * This reuses some code from PageLoadUtil.populateBatchEntry.
	 * Get the SubSecStructure map and the Repeating Batch answers The structure
	 * of the SubSecStructure map (batchMap)is: 0=QuesId, 1=NBS Ques Uid,
	 * 2=Appear in header Y,N, 3=header label 4=%ColWidth 5=component uid,
	 * 6=code set nm The Batch Entry Map (batchMapList) is the set of Batch
	 * Entries with the subsection name, id, ques id and value. Multi Select
	 * example value: 3$$Falciparum||1$$Not Determined$MulOth$1#MulOth#|| Select
	 * example: 45$$South Carolina Other examples: [TRAVEL12=4$sn$W$val$Weeks,
	 * TRAVEL11=324$$Guinea, TRAVEL08=08/20/2012]
	 * @param invFormCd
	 * @param session
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param pageActProxyVO
	 * @param toQuesIdMap
	 */
	private static void processRepeatingBatchMapValuesToObservations
			(String invFormCd,
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			PageActProxyVO pageActProxyVO,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap) throws Exception {

		Map<String, ObservationVO> batchObservationVOList = new HashMap<String, ObservationVO>();

		Map<Object,Object>batchAnswerMap = pageActProxyVO.getPageVO().getPageRepeatingAnswerDTMap();

	try {
		Map<Object, ArrayList<BatchEntry>> batchMaplist = new HashMap<Object, ArrayList<BatchEntry>>();
		//get the BatchMap
		// structure is: question_identifier, nbs_question_uid, show in header, header text, header col with, ui component, codeset
		Map<Object, Object> batchMap = findBatchRecords(invFormCd);

		String SubSectionNm = "";
		if (batchMap != null && batchMap.size() > 0) {
			Iterator<Entry<Object, Object>> ite = batchMap.entrySet()
					.iterator();
			while (ite.hasNext()) {
				// walk through each subsection that is in the batchmap structure map
				ArrayList<BatchEntry> alist = new ArrayList<BatchEntry>();
				Map.Entry<Object, Object> pairs1 = (Map.Entry<Object, Object>) ite
						.next();
				SubSectionNm = pairs1.getKey().toString();
				String batch[][] = (String[][]) pairs1.getValue();
				// get the structure map for the questions in the subsection
				int numBatchRecordsinSubSec = 0;
				try {
					if (batchAnswerMap != null
							&& batchAnswerMap.size() > 0) {
						// walk through and see if there are any answers for
						// the questions in this subsection
						for (int num = 0; num < batchAnswerMap.size(); num++) {
							for (int batchsize = 0; batchsize < batch.length; batchsize++) {
								ArrayList<NbsAnswerDT> repeatColl = new ArrayList<NbsAnswerDT>();

								if (batch[batchsize][1] != null) {
									// if the nbs_question_uid is present,
									// there could be an answer
									// get the answer if it is present based
									// on the nbs_question_uid
									// multiselects store a DT for each
									// selected value hence the arraylist
									// Find out if we have any answers out
									// there
									repeatColl = (ArrayList<NbsAnswerDT>) batchAnswerMap
											.get(new Long(
													batch[batchsize][1]));
									if (repeatColl != null
											&& repeatColl.size() > 0) {
										for (int c1 = 0; c1 < repeatColl
												.size(); c1++) {
											NbsAnswerDT dt = repeatColl
													.get(c1);
											if (numBatchRecordsinSubSec < dt
													.getAnswerGroupSeqNbr())
												numBatchRecordsinSubSec = dt
														.getAnswerGroupSeqNbr();
										}
									}
								}

							}
						}
						// look again to see if we have any multi-select
						// present in the answer map
						if (numBatchRecordsinSubSec == 0) {
							for (int num = 0; num < batchAnswerMap
									.size(); num++) {
								for (int batchsize = 0; batchsize < batch.length; batchsize++) {
									ArrayList<NbsAnswerDT> repeatColl = new ArrayList<NbsAnswerDT>();
									if (batch[batchsize][1] != null
											&& batch[batchsize][5]
													.equals("1013")) {
										repeatColl = (ArrayList<NbsAnswerDT>) batchAnswerMap
												.get(new Long(
														batch[batchsize][1]));
									}
									if (repeatColl != null
											&& repeatColl.size() > 0) {
										for (int c1 = 0; c1 < repeatColl
												.size(); c1++) {
											NbsAnswerDT dt = repeatColl
													.get(c1);
											if (dt.getSeqNbr() != null
													&& 0 == dt.getSeqNbr())
												numBatchRecordsinSubSec = numBatchRecordsinSubSec + 1;

										}
									}
								}
								if (numBatchRecordsinSubSec > 0)
									break;
							}
						}
					}
				} catch (Exception e) {
					logger.error("populateBatchRecords Exception thrown in internal batch: "
							+ e);
					throw new Exception(e);
				}
				try {
					int countloop = 0;
					// if we have any answers process them into the be answermap
					for (int count = 0; count < numBatchRecordsinSubSec; count++) {
						BatchEntry be = new BatchEntry();
						NbsAnswerDT dt = new NbsAnswerDT();
						ArrayList<NbsAnswerDT> dtList = new ArrayList<NbsAnswerDT>();
						int preSeqNbr = -1;

						// walk through the subsection batch
						for (int i = 0; i < batch.length; i++) {
							if (batchAnswerMap != null
									&& batchAnswerMap.size() > 0) {
								ArrayList<NbsAnswerDT> repeatColl = new ArrayList<NbsAnswerDT>();
								if (batch[i][1] != null)
									// if nbs_question_uid present, try to
									// get the answer if it is there
									repeatColl = (ArrayList<NbsAnswerDT>) batchAnswerMap
											.get(new Long(batch[i][1]));
								// We have an answer - process it
								if (repeatColl != null
										&& repeatColl.size() > 0) {
									// If it NOT a multiselect
									if (!batch[i][5].equals("1013")) {
										for (int arrSize = 0; arrSize < repeatColl
												.size(); arrSize++) {
											dt = repeatColl.get(arrSize);
											if (dt.getAnswerGroupSeqNbr() == count + 1)
												arrSize = repeatColl.size();
										}
										if (dt.getAnswerGroupSeqNbr() != null
												&& dt.getAnswerGroupSeqNbr() != count + 1)
											dt = new NbsAnswerDT();
									} // not multiselect
									if (batch[i][5].equals("1013")) {
										// if MultiSelect
										for (int arrSize = 0; arrSize < repeatColl
												.size(); arrSize++) {
											dt = repeatColl.get(arrSize);
											if (dt.getAnswerGroupSeqNbr() == count + 1) {
												dtList.add(dt);
											}
										}
									} // is multiselect

									if (batch[i][5] != null) {
										String str = dt.getAnswerTxt();


											be.getAnswerMap().put(
													batch[i][0],
													dt.getAnswerTxt());
									}
									be.setId(1);
								}
							}
						}
						if (be.getId() == 1) {
							be.setSubsecNm(SubSectionNm);
							be.setId(PageForm.getNextId());

							processBatchEntry(be,
									investigationProxyVO,
									formLevelObservation,
									toQuesIdMap,
									batchObservationVOList);
						}
					}// number of batch records in subsection

					} catch (Exception e) {
						logger.error("processBatchRecords Exception thrown in internal batch2: "
							+ e);
						throw new Exception(e);
					}
				}// walking through the Batchmap

			}
	 	} catch (Exception e) {
		logger.error("provessBatchRecords Exception thrown: " + e);
		throw new Exception(e);
	 	}
	}
	/**
	 * findBatchRecords - find any Repeating Batch records for the specified Page Form Code
	 *
	 * @param pageFormCd
	 * @return batchMap
	 */
	public static Map findBatchRecords(String invFrmCd) {
		Map<Object, Object> batchMap = new HashMap<Object, Object>();
		try{

			PageManagementDAOImpl pageManagementDAOImpl = new PageManagementDAOImpl();
			batchMap = pageManagementDAOImpl.findBatchRecords(invFrmCd);

		}catch (Exception ex) {
			logger.fatal("Error in calling pageManagementDAOImpl.findBatchRecords for PageBuilder to Master Message: ", ex);
			throw new NEDSSSystemException(ex.getMessage());

		}
		return batchMap;
	}
	/**
	 * addObservationFromCaseAnswer -if the To location is a answer type,
	 * and the from is an observation type - create the observation and the
	 * act relationship.
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param toQuesIdMap
	 * @param convertMetaData
	 * @param quesMetaData
	 * @param theAnsDT
	 */
	private static void addObservationFromCaseAnswer (
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap,
			NBSConversionMappingDT convertMetaData,
			NbsQuestionMetadata quesMetaData,
			NbsCaseAnswerDT theAnsDT) {

	        Long targetActUid = formLevelObservation.getTheObservationDT().getUid();
	        boolean selectType = false;
			ObservationVO observationVO  = null;
			ObservationVO existingObservationVO = checkIfObservationAlreadyExists(investigationProxyVO, convertMetaData.getFromQuestionId());
			
			if (existingObservationVO != null)
				observationVO = existingObservationVO;
			else
				observationVO = populateGenericObservationVO(convertMetaData.getFromQuestionId(), convertMetaData.getFromLabel());

			Long nbsComponentType = quesMetaData.getNbsUiComponentUid();
			if (convertMetaData.getConversionType() != null && !convertMetaData.getConversionType().isEmpty() && convertMetaData.getConversionType().contains("SelectToNumeric")) {
				nbsComponentType = 1007L;
			}

			String dataType = quesMetaData.getDataType();  //Coded, Text, Date, Numeric

			switch(nbsComponentType.intValue()) {
					case PageMetaConstants.aSINGLESELECT:
					case PageMetaConstants.aSINGLESELECTREADONLYSAVE:
						selectType = true;
						logger.debug("addObservationFromCaseAnswer: processing Single Select for " + quesMetaData.getQuestionIdentifier());
						String otherVal = addSingleSelectObservation(
															toQuesIdMap,
															formLevelObservation,
															observationVO,
															quesMetaData,
															convertMetaData,
															theAnsDT);
						if (otherVal != null) {
							addOtherTextObservation(
									investigationProxyVO,
									formLevelObservation,
									toQuesIdMap,
									convertMetaData,
									quesMetaData,
									theAnsDT.getAddTime(),
									theAnsDT.getLastChgTime(),
									otherVal);
						} //other is there
						break;
		    		case PageMetaConstants.aINPUT:
		    		case PageMetaConstants.aINPUTREADONLYSAVE:
		    		case PageMetaConstants.aINPUTREADONLYNOSAVE:
		    			if (dataType != null && (dataType.equalsIgnoreCase("Date") || dataType.equalsIgnoreCase("DateTime"))) {
		    				addDateObservation(observationVO, quesMetaData, theAnsDT);
		    				break;
		    			} else if (dataType != null && dataType.equalsIgnoreCase("Numeric")) {
		    				addNumericObservation(observationVO, quesMetaData, theAnsDT);
		    				break;
		    			} else if (dataType != null && dataType.equalsIgnoreCase("Text")) {
		    				addTextObservation(observationVO, quesMetaData, theAnsDT);
		    				break;
		    			}

					 default:
						 logger.error("Error: addObservationFromCaseAnswer: unhandled answer for "
								 + quesMetaData.getQuestionIdentifier() + " for component " + nbsComponentType.toString() );
				}
			if (existingObservationVO == null) {
				if (selectType && (observationVO.getTheObsValueCodedDTCollection() == null || observationVO.getTheObsValueCodedDTCollection().isEmpty()))
						return; //don't add an empty observation
				investigationProxyVO.getTheObservationVOCollection().add(observationVO);
				Long sourceActUid = observationVO.getTheObservationDT().getUid();
				ActRelationshipDT actRelationshipDT = populateActRelationshipToJoinToForm(sourceActUid, targetActUid, theAnsDT);
				logger.debug("Created act_relationship for To_ques=" + quesMetaData.getQuestionIdentifier());
				formLevelObservation.getTheActRelationshipDTCollection().add(actRelationshipDT); //act relationship for this obs
			}
			return;
	}




/**
 * Look to see if the observation is already in the list.
 * This would be true for MultiToSingle.
 * @param investigationProxyVO
 * @param fromQuestionId
 * @return
 */
private static ObservationVO checkIfObservationAlreadyExists(
			InvestigationProxyVO investigationProxyVO, String fromQuestionId) {
	
		Iterator iterObs = investigationProxyVO.getTheObservationVOCollection().iterator();
		while (iterObs.hasNext()) {
			ObservationVO theObservation = (ObservationVO) iterObs.next();
			if (theObservation.getTheObservationDT().getCd().equalsIgnoreCase(fromQuestionId.trim()))
				return theObservation; //already in list
		}
				
		return null;
	}

/**
 * addObservationFromFieldValue -take the object we got from reflection and create an observation.
 * @param fieldValue
 * @param convertMetaData
 * @param toQuesIdMap
 * @param quesMeta
 * @param addTime
 * @param lastChgTime
 * @param formLevelObservation
 * @param investigationProxyVO
 */
	private static void addObservationFromFieldValue(
			Object fieldValue,
			NBSConversionMappingDT convertMetaData,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap,
			NbsQuestionMetadata quesMeta,
			Timestamp addTime,
			Timestamp lastChgTime,
			ObservationVO formLevelObservation,
			InvestigationProxyVO investigationProxyVO) {

		ObservationVO observationVO = null;
		ObservationVO existingObservationVO = checkIfObservationAlreadyExists(investigationProxyVO, convertMetaData.getFromQuestionId());
		if (existingObservationVO != null)
			observationVO = existingObservationVO;
		else
			observationVO = populateGenericObservationVO(convertMetaData.getFromQuestionId(), quesMeta.getQuestionLabel());
		Boolean processed = false;
		if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.CODED)) {
			String theCode = null;
			try {
				theCode = (String) fieldValue;
			} catch (Exception ex) {
				logger.warn("PageBuilderToMasterMessage Error mapping Coded String field");
				return;
			}
			//need to convert code if mapping is present
			if (!theCode.trim().isEmpty() &&
					convertMetaData.getFromCode() != null &&
					!convertMetaData.getFromCode().trim().isEmpty()) {
				String theKey = convertMetaData.getToQuestionId().trim() + theCode.trim();
				if (!theCode.trim().isEmpty() && toQuesIdMap.containsKey(theKey)) {
					NBSConversionMappingDT convertMetaDataFromKey = toQuesIdMap.get(theKey);
					theCode = convertMetaDataFromKey.getFromCode().trim();
					observationVO.getTheObservationDT().setCd(convertMetaDataFromKey.getFromQuestionId()); //could change
				}
			}
			if (!theCode.trim().isEmpty())
				processed = addSingleSelectObservation(observationVO, theCode);

		} else if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.DATE_DATATYPE)) {
			Timestamp theDate = null;
			try {
				theDate = (Timestamp) fieldValue;
			} catch (Exception ex) {
				logger.warn("PageBuilderToMasterMessage Error mapping Timestamp date field");
				return;
			}
			processed = addDateObservation(observationVO, theDate);

		} else if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.TEXT_DATATYPE)) {
			String theText = null;
			try {
				theText = (String) fieldValue;
			} catch (Exception ex) {
				logger.warn("PageBuilderToMasterMessage Error mapping Text field");
				return;
			}
			processed = addTextObservation(observationVO, theText);

		} else if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.NUMERIC_DATATYPE)) {
			BigDecimal theNumber = null;
			try {
				theNumber = (BigDecimal) fieldValue;
			} catch (Exception ex) {
				logger.warn("PageBuilderToMasterMessage Error mapping Numeric field");
				return;
			}
			processed = addNumericObservation(observationVO, theNumber);
		}

		if (processed && existingObservationVO == null) {
			linkQuestionToForm(investigationProxyVO, observationVO, formLevelObservation, addTime, lastChgTime);
		}

	}


	/**
	 * getPageFormCode - get the PageBuilder form code for the production phc page
	 * @param - formCd i.e. PG_SYPH_GEN
	 * @return - questionMap and questionKeyMap populated
	 */
	public static void loadQuestionMetadata (String formCd) {
		questionMap = new HashMap<Object, Object>();
		questionKeyMap = new HashMap<Object, Object>();
    	try {
    		if (QuestionsCache.dmbMap.containsKey(formCd))
    			questionMap = (Map<Object, Object>) QuestionsCache.dmbMap
					.get(formCd);
    		else if (!QuestionsCache.dmbMap.containsKey(formCd))
			questionMap = (Map<Object, Object>) QuestionsCache
					.getDMBQuestionMapAfterPublish().get(formCd);
    	} catch (Exception ex) {
			logger.error("NNDMessageProcessor: Error while retreiving PageBuilder questions for formCd: " + formCd);
		}
		if (questionMap == null)
			return;

		Iterator iter = questionMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			NbsQuestionMetadata metaData = (NbsQuestionMetadata) questionMap
					.get(key);
			questionKeyMap.put(metaData.getNbsQuestionUid(), key);
		}
		return;
	}

	/**
	 * getPageProxyObject - get the PageBuilder PageProxyObject for the Public Health Case
	 * @param - public health case uid (Long)
	 * @param - nbsSecurityObj
	 * @return - PageProxyObject
	 */
	public static PageActProxyVO getPageProxyObject(Long publicHealthCaseUid,
			NBSSecurityObj nbsSecurityObj) {
		PageProxyVO pageProxyVO = null;
		NedssUtils nedssUtils = new NedssUtils();
		try {

        	Object object = nedssUtils.lookupBean(JNDINames.PAGE_PROXY_EJB);
	        PageProxyHome home = (PageProxyHome) PortableRemoteObject.narrow(object, PageProxyHome.class);
	        PageProxy pageproxy = home.create();
	        pageProxyVO =  pageproxy.getPageProxyVO(NEDSSConstants.CASE, publicHealthCaseUid, nbsSecurityObj);
	        translatePageCaseAnswerDTsToAnswerDTs(pageProxyVO);

		} catch (Exception ex) {
			logger.fatal("NNDMessageProcessor:getPageProxyObject: ", ex);
		}

		return (PageActProxyVO) pageProxyVO;
	}
/**
 * translatePageCaseAnswerDTsToAnswerDTs
 * We now have answer types in several tables. Combine the answers into one table.
 * @param pageProxyVO
 * @throws NEDSSAppException
 */
	public static void translatePageCaseAnswerDTsToAnswerDTs(PageProxyVO pageProxyVO)
			throws NEDSSAppException {
		PageActProxyVO proxyVO = (PageActProxyVO) pageProxyVO;
		Map<Object, Object> answerMap = proxyVO.getPageVO().getAnswerDTMap();
		Map<Object, Object> pamAnswerMap = proxyVO.getPageVO()
				.getPamAnswerDTMap();
		Map<Object, Object> repeatingAnswerMap = proxyVO.getPageVO()
				.getPageRepeatingAnswerDTMap();
		try {
			if (answerMap != null && answerMap.keySet().size() > 0) {
				Iterator<Object> ite = answerMap.keySet().iterator();
				while (ite.hasNext()) {
					Object key = ite.next();
					if (answerMap.get(key) instanceof NbsCaseAnswerDT) {
						NbsCaseAnswerDT caseAnswerDT = (NbsCaseAnswerDT) answerMap
								.get(key);
						NbsAnswerDT ansDT = caseAnswerDT;
						answerMap.put(key, ansDT);
					}
				}
			}
			if (pamAnswerMap != null && pamAnswerMap.keySet().size() > 0) {
				Iterator<Object> ite = pamAnswerMap.keySet().iterator();
				while (ite.hasNext()) {
					Object key = ite.next();
					if (pamAnswerMap.get(key) instanceof NbsCaseAnswerDT) {
						NbsCaseAnswerDT caseAnswerDT = (NbsCaseAnswerDT) pamAnswerMap
								.get(key);
						NbsAnswerDT ansDT = caseAnswerDT;
						pamAnswerMap.put(key, ansDT);
					}
				}
			}
			if (repeatingAnswerMap != null
					&& repeatingAnswerMap.keySet().size() > 0) {
				Iterator<Object> ite = repeatingAnswerMap.keySet().iterator();
				while (ite.hasNext()) {
					Object key = ite.next();
					if (repeatingAnswerMap.get(key) instanceof NbsCaseAnswerDT) {
						NbsCaseAnswerDT caseAnswerDT = (NbsCaseAnswerDT) repeatingAnswerMap
								.get(key);
						NbsAnswerDT ansDT = caseAnswerDT;
						repeatingAnswerMap.put(key, ansDT);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("NNDMessageProcessor: Exception while translating AnswerDTs To CaseAnswerDTs :"
					+ ex.getMessage());
			throw new NEDSSAppException(ex.getMessage());
		}
	}
	/**
	 * getPageFormCode - get the PageBuilder form code for the production phc page
	 * @param - conditionCd i.e. 10280
	 * @param - nbsSecurityObj
	 * @return - formCd
	 */
	public static String getPageFormCode(String conditionCd,
			NBSSecurityObj nbsSecurityObj) {
		WaTemplateDT waTemplateDT = null;
		NedssUtils nedssUtils = new NedssUtils();
		try {

        	Object pageMgtRef = nedssUtils.lookupBean(JNDINames.PAGE_MANAGEMENT_PROXY_EJB);
        	PageManagementProxyHome home = (PageManagementProxyHome) PortableRemoteObject.narrow(pageMgtRef, PageManagementProxyHome.class);
        	PageManagementProxy pageMgtProxy = home.create();
        	waTemplateDT = (WaTemplateDT) pageMgtProxy.getWaTemplateByCondTypeBusObj(conditionCd,
        																NEDSSConstants.PUBLISHED,
        																NEDSSConstants.INVESTIGATION_BUSINESS_OBJECT_TYPE,
        																nbsSecurityObj);
		} catch (Exception ex) {
			logger.fatal("NNDMessageProcessor:getPageFormCode: ", ex);
		}
        return waTemplateDT.getFormCd();
	}

/**
 * populateGenericObservationVO - populate a base observation for use later
 * as a text, numeric, coded or date type.
 * @param questionIdentifier
 * @param questionLabel
 * @return
 */
	private static ObservationVO populateGenericObservationVO(
			String questionIdentifier,
			String questionLabel) {
		ObservationVO observationVO = new ObservationVO();
		observationVO.setTheActRelationshipDTCollection(new ArrayList<Object>());
		ObservationDT observationDT = new ObservationDT();
		observationVO.setTheObservationDT(observationDT);
		observationDT.setCd(questionIdentifier);
		observationDT.setCdDescTxt(questionLabel);
		observationDT.setCdSystemCd(NEDSSConstants.NBS);
		String localId = "";
		observationDT.setCdSystemDescTxt(NEDSSConstants.NEDSS_BASE_SYSTEM);
		observationDT.setCdVersion(NEDSSConstants.VERSION);
		observationDT.setItDelete(false);
		observationDT.setItNew(false);
		observationDT.setItDirty(true);
		observationDT.setSharedInd(NEDSSConstants.TRUE);
		observationDT.setVersionCtrlNbr(new Integer(1));
		//<ctrlCdDisplayForm>supplemental</ctrlCdDisplayForm>
		//supplemental appears in many of the legacy observations

		UidGeneratorHelper uidGen = null;
		Long newUid = 0L;
        try {
        	uidGen = new UidGeneratorHelper();
        	localId = uidGen.getLocalID(UidClassCodes.OBSERVATION_CLASS_CODE);
        	newUid = uidGen.getNbsIDLong(UidClassCodes.NBS_CLASS_CODE);
		} catch (Exception e) {
			logger.error("Error getting local id for PageBuilder Answer to Master Message Observation: " + e.getMessage());
		}
        observationDT.setObservationUid(newUid);
        observationDT.setLocalId(localId);


		return observationVO;
	}
/**
 * createFormLevelObservationVO - A form level observation ties the questions to the form.
 * @param formCd
 * @param phcLastChgTime 
 * @return
 */
	private static ObservationVO createFormLevelObservationVO(
			String formCd, Timestamp phcLastChgTime) {
		ObservationVO observationVO = new ObservationVO();
		ObservationDT observationDT = new ObservationDT();
		observationVO.setTheObservationDT(observationDT);
		observationDT.setCd(formCd);
		observationDT.setCdSystemCd(NEDSSConstants.NBS);
		observationDT.setCdSystemDescTxt(NEDSSConstants.NEDSS_BASE_SYSTEM);
		observationDT.setCdVersion(NEDSSConstants.VERSION);
		observationDT.setItDelete(false);
		observationDT.setItNew(false);
		observationDT.setItDirty(true);

		observationDT.setVersionCtrlNbr(new Integer(1));
		observationDT.setGroupLevelCd(NEDSSConstants.GROUPLEVELCD);
		observationDT.setObsDomainCd("CLN");
		observationDT.setSharedInd(NEDSSConstants.TRUE);
		observationDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);
		if (phcLastChgTime != null)
			observationDT.setStatusTime(phcLastChgTime);
		//programJurisdictionOid is in the legacy is a partial OID i.e. 4 - we are assuming this is not relevant skipping - 

		UidGeneratorHelper uidGen = null;
		String localId = "";
		Long newUid = 0L;
        try {
        	uidGen = new UidGeneratorHelper();
			localId = uidGen.getLocalID(UidClassCodes.OBSERVATION_CLASS_CODE);
			newUid = uidGen.getNbsIDLong(UidClassCodes.NBS_CLASS_CODE);
		} catch (Exception e) {
			logger.error("Error getting local id for PageBuilder Answer to Master Message Observation: " + e.getMessage());
		}
        observationDT.setLocalId(localId);
        observationDT.setObservationUid(newUid);
        observationVO.setTheActRelationshipDTCollection(new ArrayList<Object>()); //Store act relationship to other objects here
		return observationVO;
	}


	/**
	 * populateActRelationshipToJoinToForm - create the act relationship to join the
	 * answer question to the form.
	 * @param sourceActUid - question observation
	 * @param targetActUid - form observation
	 * @param ansDT
	 * @return
	 */
	private static ActRelationshipDT populateActRelationshipToJoinToForm(
			Long sourceActUid, Long targetActUid, NbsCaseAnswerDT ansDT) {
		ActRelationshipDT actRelationshipDT = new ActRelationshipDT();
		if (ansDT.getAddTime() != null)
			actRelationshipDT.setAddTime(ansDT.getAddTime());
		actRelationshipDT.setExportInd(false);
		actRelationshipDT.setNNDInd(false);
		actRelationshipDT.setShareInd(false);
		if (ansDT.getLastChgTime() != null) {
			actRelationshipDT.setLastChgTime(ansDT.getLastChgTime());
			actRelationshipDT.setRecordStatusTime(ansDT.getLastChgTime());
			actRelationshipDT.setStatusTime(ansDT.getLastChgTime());
		}

		actRelationshipDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
		actRelationshipDT.setSourceActUid(sourceActUid);
		actRelationshipDT.setSourceClassCd(NEDSSConstants.OBS);
		actRelationshipDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);

		actRelationshipDT.setTargetActUid(targetActUid);
		actRelationshipDT.setTargetClassCd(NEDSSConstants.OBS);
		actRelationshipDT.setTypeCd(NEDSSConstants.INV_FRM_Q); //form ques
		actRelationshipDT.setTypeDescTxt("Investigation Form Question");

		return actRelationshipDT;
	}
	/**
	populateActRelationshipToJoinToForm - create the act relationship to join the
	 * question to the form.
	 * @param sourceActUid - question observation
	 * @param targetActUid - form observation
	 * @param addTime
	 * @param lastChgTime
	 * @return
	 */
	private static ActRelationshipDT populateActRelationshipToJoinToForm(
						Long sourceActUid, Long targetActUid,
						Timestamp addTime, Timestamp lastChgTime) {
		ActRelationshipDT actRelationshipDT = new ActRelationshipDT();
		if (addTime != null)
			actRelationshipDT.setAddTime(addTime);
		actRelationshipDT.setExportInd(false);
		actRelationshipDT.setNNDInd(false);
		actRelationshipDT.setShareInd(false);
		if (lastChgTime != null) {
			actRelationshipDT.setLastChgTime(lastChgTime);
			actRelationshipDT.setRecordStatusTime(lastChgTime);
		}

		actRelationshipDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
		actRelationshipDT.setSourceActUid(sourceActUid);
		actRelationshipDT.setSourceClassCd(NEDSSConstants.OBS);
		actRelationshipDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);

		actRelationshipDT.setTargetActUid(targetActUid);
		actRelationshipDT.setTargetClassCd(NEDSSConstants.OBS);
		actRelationshipDT.setTypeCd(NEDSSConstants.INV_FRM_Q); //form ques
		actRelationshipDT.setTypeDescTxt("Investigation Form Question");

		return actRelationshipDT;
	}

	/**
	 * Attach the form to the Public Health Case.
	 * @param sourceActUid
	 * @param publicHealthCase
	 * @return
	 */
	private static ActRelationshipDT populateActRelationshipForFormToCase(
			Long sourceActUid, PublicHealthCaseDT publicHealthCase) {
		ActRelationshipDT actRelationshipDT = new ActRelationshipDT();
		if (publicHealthCase.getAddTime() != null) {
			actRelationshipDT.setAddTime(publicHealthCase.getAddTime());
		}
		actRelationshipDT.setExportInd(false);
		actRelationshipDT.setNNDInd(false);
		actRelationshipDT.setShareInd(false);
		if (publicHealthCase.getLastChgTime() != null) {
			actRelationshipDT.setLastChgTime(publicHealthCase.getLastChgTime());
			actRelationshipDT.setRecordStatusTime(publicHealthCase.getLastChgTime());
		}

		actRelationshipDT.setRecordStatusCd(NEDSSConstants.RECORD_STATUS_ACTIVE);
		actRelationshipDT.setSourceActUid(sourceActUid);
		actRelationshipDT.setSourceClassCd(NEDSSConstants.OBS);
		actRelationshipDT.setStatusCd(NEDSSConstants.STATUS_ACTIVE);

		actRelationshipDT.setTargetActUid(publicHealthCase.getPublicHealthCaseUid());
		actRelationshipDT.setTargetClassCd(NEDSSConstants.CASE);
		actRelationshipDT.setTypeCd(NEDSSConstants.PHC_INV_FORM); //form to case
		actRelationshipDT.setTypeDescTxt("PHC Investigation Form");

		return actRelationshipDT;
	}



	/**
	 * Add a date observation.
	 * Note: assume going to fromTime.
	 * But we may need to pass the conversion to check if from or to time.
	 * @param observationVO
	 * @param quesMeta
	 * @param ansDT
	 */
	private static void addDateObservation(
			ObservationVO observationVO, NbsQuestionMetadata quesMeta,
			NbsCaseAnswerDT ansDT) {
		String dateString = "";
		if (observationVO.getTheObsValueDateDTCollection() == null)
			observationVO.setTheObsValueDateDTCollection(new ArrayList<Object>());
		try {
			dateString = ansDT.getAnswerTxt();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date date = df.parse(dateString);
			java.sql.Timestamp timeStampDate = new 	Timestamp(date.getTime());
			ObsValueDateDT obsValueDateDT = new ObsValueDateDT();
			obsValueDateDT.setFromTime(timeStampDate);
			obsValueDateDT.setItDelete(false);
			obsValueDateDT.setItDirty(false);
			obsValueDateDT.setItNew(false);
			obsValueDateDT.setObservationUid(observationVO.getTheObservationDT().getUid());
			obsValueDateDT.setObsValueDateSeq(new Integer(1));
			observationVO.getTheObsValueDateDTCollection().add(obsValueDateDT);
		} catch (Exception ex) {
			logger.debug("AddDateObservation() exception parsing date: " + dateString + " " + ex.getMessage());
		}

	}
	/**
	 * addDateObservation - where timestamp is passed
	 * @param observationVO
	 * @param Timestamp date
	 * @return true if no error
	 */
	private static Boolean addDateObservation(
			ObservationVO observationVO,
			Timestamp timeStampDate) {

		if (observationVO.getTheObsValueDateDTCollection() == null)
			observationVO.setTheObsValueDateDTCollection(new ArrayList<Object>());
		try {
			ObsValueDateDT obsValueDateDT = new ObsValueDateDT();
			obsValueDateDT.setFromTime(timeStampDate);
			obsValueDateDT.setItDelete(false);
			obsValueDateDT.setItDirty(false);
			obsValueDateDT.setItNew(false);
			obsValueDateDT.setObservationUid(observationVO.getTheObservationDT().getUid());
			obsValueDateDT.setObsValueDateSeq(new Integer(1));
			observationVO.getTheObsValueDateDTCollection().add(obsValueDateDT);
		} catch (Exception ex) {
			logger.debug("AddDateObservation() exception parsing date: " + ex.getMessage());
			return false;
		}
	    return true; //processed ok
	}
	/**
	 * Add a text observation from a case answer.
	 * @param observationVO
	 * @param quesMeta
	 * @param ansDT
	 */
	private static void addTextObservation(ObservationVO observationVO,
			NbsQuestionMetadata quesMeta, NbsCaseAnswerDT ansDT) {
		addTextObservation(observationVO, ansDT.getAnswerTxt());
		return;
	}
	/**
	 * Add a text observation.
	 * @param observationVO
	 * @param theText
	 * @return
	 */
	private static Boolean addTextObservation(ObservationVO observationVO,
			String theText) {
		if (observationVO.getTheObsValueTxtDTCollection() == null)
			observationVO.setTheObsValueTxtDTCollection(new ArrayList<Object>());
		ObsValueTxtDT obsValueTxtDT = new ObsValueTxtDT();
		obsValueTxtDT.setValueTxt(theText);
		obsValueTxtDT.setItDelete(false);
		obsValueTxtDT.setItDirty(false);
		obsValueTxtDT.setItNew(false);
		obsValueTxtDT.setObsValueTxtSeq(new Integer(1));
		obsValueTxtDT.setObservationUid(observationVO.getTheObservationDT().getUid());
		observationVO.getTheObsValueTxtDTCollection().add(obsValueTxtDT);
		return true;

	}

	/**
	 * addNumericObservation - sets the numeric and the unit code if present.
	 * @param observationVO
	 * @param quesMeta
	 * @param ansDT
	 */
	private static void addNumericObservation(ObservationVO observationVO,
			NbsQuestionMetadata quesMeta, NbsCaseAnswerDT ansDT) {
		if (observationVO.getTheObsValueNumericDTCollection() == null)
			observationVO.setTheObsValueNumericDTCollection(new ArrayList<Object>());
		try {

			ObsValueNumericDT obsValueNumericDT = new ObsValueNumericDT();

			obsValueNumericDT.setItDelete(false);
			obsValueNumericDT.setItDirty(false);
			obsValueNumericDT.setItNew(false);
			obsValueNumericDT.setObsValueNumericSeq(new Integer(1));
			obsValueNumericDT.setObservationUid(observationVO.getTheObservationDT().getUid());
			String ansVal = ansDT.getAnswerTxt();
			String answer = ansVal.trim();
			String answerNumer = "";
			String answerUnit = "";
			if (answer.contains("^")) {
				//if Units, get the Units code
				if (answer.length() > answer.indexOf("^") + 1)
					answerUnit = answer.substring(answer.indexOf("^") + 1, answer.length());
				answer = answer.substring(0, answer.indexOf("^"));  //could be no units
			}
			if (observationVO.getTheObservationDT().getCd().equals(PageConstants.DURATION_OF_STAY))
				answerUnit = "D"; //legacy had units for Duration of Stay
			
			//legacy always has a decimal in the answer - <numericValue1>14.00000</numericValue1>
			//we don't..
			obsValueNumericDT.setNumericValue1_s(answer);
			if (!answerUnit.isEmpty())
				obsValueNumericDT.setNumericUnitCd(answerUnit);
			observationVO.getTheObsValueNumericDTCollection().add(obsValueNumericDT);
		} catch (Exception ex) {
			logger.debug("AddNumericObservation() exception: " + ex.getMessage());
		}

	}
	/**
	 * Add simple numeric observation.
	 * @param observationVO
	 * @param numericValue
	 * @return
	 */
	private static boolean addNumericObservation(ObservationVO observationVO,
			BigDecimal numericValue) {
		if (observationVO.getTheObsValueNumericDTCollection() == null)
			observationVO.setTheObsValueNumericDTCollection(new ArrayList<Object>());
		try {

			ObsValueNumericDT obsValueNumericDT = new ObsValueNumericDT();

			obsValueNumericDT.setItDelete(false);
			obsValueNumericDT.setItDirty(false);
			obsValueNumericDT.setItNew(false);
			obsValueNumericDT.setObsValueNumericSeq(new Integer(1));
			obsValueNumericDT.setObservationUid(observationVO.getTheObservationDT().getUid());
			obsValueNumericDT.setNumericValue1(numericValue);
			observationVO.getTheObsValueNumericDTCollection().add(obsValueNumericDT);
		} catch (Exception ex) {
			logger.debug("AddNumericObservation() exception: " + ex.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * Add a coded observation from a single select.
	 * Checks for Other convention.
	 * @param toQuesIdMap
	 * @param formLevelObservation
	 * @param observationVO
	 * @param quesMeta
	 * @param convertMetaData 
	 * @param ansDT
	 * @return
	 */
	private static String addSingleSelectObservation(
			HashMap<String, NBSConversionMappingDT> toQuesIdMap,
			ObservationVO formLevelObservation,
			ObservationVO observationVO,
			NbsQuestionMetadata quesMeta,
			NBSConversionMappingDT convertMetaData, 
			NbsCaseAnswerDT ansDT) {

		String answerOther = null;

		String theAns = ansDT.getAnswerTxt().trim();
		if (theAns.startsWith("OTH^")) {
			//later will put other value into an entirely new observation with a txtObs
			if (theAns.length() > theAns.indexOf("^") + 1) {
				answerOther = theAns.substring(theAns.indexOf("^") + 1, theAns.length());
			}
			theAns = theAns.substring(0, theAns.indexOf("^"));
		}
		//check and see if we have any conversion to do...

		String theKey = quesMeta.getQuestionIdentifier().trim() + theAns.trim();
		if (!theAns.trim().isEmpty() && toQuesIdMap.containsKey(theKey)) {
			NBSConversionMappingDT convMap = toQuesIdMap.get(theKey);
			if (convMap.getFromCode() != null)
				theAns = convMap.getFromCode(); //change the code to the legacy
		} else {
			if (convertMetaData.getTranslationRequiredInd().equals("Y")) { //sometimes only Yes answers are mapped
				logger.warn("Answer " + theAns + " for question " + convertMetaData.getFromQuestionId() + " is not in mapping and skipped.");
				return(null); //this answer shouldn't be included
			}
		}
		addSingleSelectObservation(observationVO, theAns);

		return answerOther;
	}

	/**
	 * addSingleSelectObservation - add from a field value
	 * Assuming don't have to worry about Other issue.
	 * @param new observationVO
	 * @param code
	 * @return processed without error = true
	 *
	 */
	private static Boolean addSingleSelectObservation(
			ObservationVO observationVO,
			String theCode) {

		if (observationVO.getTheObsValueCodedDTCollection() == null)
			observationVO.setTheObsValueCodedDTCollection(new ArrayList<Object>());
		ObsValueCodedDT obsValueCodedDT = new ObsValueCodedDT();
		obsValueCodedDT.setCode(theCode);
		obsValueCodedDT.setItDelete(false);
		obsValueCodedDT.setItDirty(false);
		obsValueCodedDT.setItNew(false);
		obsValueCodedDT.setObservationUid(observationVO.getTheObservationDT().getUid());
		observationVO.getTheObsValueCodedDTCollection().add(obsValueCodedDT);
		return true;
	}

	/**
	 *  addMultiSelectObservation
	 * A MultiSelect add one or more ObsValueCoded to a single observation.
	 * Note that we check for Other i.e. OTH^Vitamutazol
	 * @param 	investigationProxyVO - legacy investigation
	 * @param 	formLevelObservation - our form level to link questions to
	 * @param 	convertMetaData - the convert we are on
	 * @param 	toQuesIdMap the NBSConversionMappingDT map
	 * @param 	answerArray
	 * @return  String of Other Val - seldom returned, only if multiselect supports other and an other was selected
	 */
	private static void addMultiSelectObservation(
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			NbsQuestionMetadata quesMeta,
			NBSConversionMappingDT convertMetaData,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap,
			ArrayList<NbsCaseAnswerDT> answerArray) {

		ObservationVO observationVO  = populateGenericObservationVO(convertMetaData.getFromQuestionId(), quesMeta.getQuestionLabel());
		if (observationVO.getTheObsValueCodedDTCollection() == null)
			observationVO.setTheObsValueCodedDTCollection(new ArrayList<Object>());
		String answerOther = null;
		NbsCaseAnswerDT ansDT = null;
		for (int i = 0; i < answerArray.size(); ++i) {
			ansDT = answerArray.get(i);
			ObsValueCodedDT obsValueCodedDT = new ObsValueCodedDT();
			String theAns = ansDT.getAnswerTxt().trim();
			if (theAns.isEmpty())
				continue;
			if (theAns.startsWith("OTH^")) {
				//put other value into an entirely new observation with a txtObs
				if (theAns .length() > theAns .indexOf("^") + 1) {
					answerOther = theAns.substring(theAns.indexOf("^") + 1, theAns.length());
				}
				theAns = theAns.substring(0, theAns.indexOf("^"));
			}
			//check if from_code to_code mapping is present
			if (!theAns.trim().isEmpty()) {
				String theKey = quesMeta.getQuestionIdentifier().trim() + theAns.trim();
				if (toQuesIdMap.containsKey(theKey)) {
					NBSConversionMappingDT convertMetaDataFromKey = toQuesIdMap.get(theKey);
					theAns = convertMetaDataFromKey.getFromCode().trim();  //change new code to the legacy code
				}
			}
			obsValueCodedDT.setCode(theAns);
			obsValueCodedDT.setItDelete(false);
			obsValueCodedDT.setItDirty(false);
			obsValueCodedDT.setItNew(false);
			obsValueCodedDT.setObservationUid(observationVO.getTheObservationDT().getUid());
			if (notDuplicateCodedValue(observationVO.getTheObsValueCodedDTCollection(), obsValueCodedDT))
				observationVO.getTheObsValueCodedDTCollection().add(obsValueCodedDT);
			else {
				logger.error("Encountered duplicate observation value for " + convertMetaData.getFromQuestionId().trim() );
				logger.error("Duplicate value is " + theAns);
				return;
			}
		}
		//Link the question to the form
		linkQuestionToForm(investigationProxyVO, observationVO, formLevelObservation, ansDT.getAddTime(), ansDT.getLastChgTime());

		if (answerOther != null) {
			//if Other value is present, will need to create a text observation with the Other value
				addOtherTextObservation(
						investigationProxyVO,
						formLevelObservation,
						toQuesIdMap,
						convertMetaData,
						quesMeta,
						ansDT.getAddTime(),
						ansDT.getLastChgTime(),
						answerOther);
		}

		return;
	}


    /**
     * Make sure the code isn't there twice. If it is, it would cause a rejection at the CDC
     * @param theObsValueCodedDTCollection
     * @param obsValueCodedDT
     * @return
     */
	private static boolean notDuplicateCodedValue(
			Collection<Object> theObsValueCodedDTCollection,
			ObsValueCodedDT obsValueCodedDT) {
		Iterator obsIter = theObsValueCodedDTCollection.iterator();
		while (obsIter.hasNext()) {
			ObsValueCodedDT obsDT = (ObsValueCodedDT) obsIter.next();
			if (obsDT.getCode().equalsIgnoreCase(obsValueCodedDT.getCode()))
				return false;
		}
		return true;
	}



	/**
	 * Get the value from the object based on the data location.
	 * Uses reflection.
	 * @param dataLoc
	 * @param invokeObject
	 * @return Object the data value
	 */
	private static Object getValueForDataLocation(String dataLoc, Object invokeObject) {

		if (dataLoc == null || invokeObject == null || dataLoc.isEmpty())
			return null;
		String method1Nm = null;
		String method2Nm = null;

		try {
		String dataLocation = dataLoc.substring(dataLoc.indexOf(".") + 1, dataLoc.length());
		String[] dataLocAry = dataLocation.split("\\_");
		StringBuffer sb = new StringBuffer("get");
		for (int i = 0; i < dataLocAry.length; ++i) {
			String tmpStr = dataLocAry[i];
			sb.append(tmpStr.substring(0,1).toUpperCase() + tmpStr.substring(1).toLowerCase());
		}
		method1Nm = sb.toString();
		method2Nm = null;
		if (method1Nm.substring(method1Nm.length()-2).equals("Cd"))
			method2Nm = method1Nm.substring(0,method1Nm.length()-2) + "CD";

		} catch (Exception ex) {
			logger.warn("Exception processing conversion data " + dataLoc);
		}
	    //unfortunately standard conventions were not followed and some
		//variables end with CD.
		java.lang.reflect.Method method = null;
		try {
			method = invokeObject.getClass().getMethod(method1Nm);
		} catch (SecurityException e) {
			logger.debug("security exception getting method by reflection");
		} catch (NoSuchMethodException e) {
			logger.debug("Finding method " + method1Nm + "not found by reflection");
		}
		if (method == null && method2Nm != null) {
			try {
				method = invokeObject.getClass().getMethod(method2Nm);
			} catch (SecurityException e) {
				logger.debug("security exception getting method by reflection");
			} catch (NoSuchMethodException e) {
				logger.debug("Finding method " + method2Nm + "not found getting method by reflection");
			}
		}
		Object dataValue = null;
		if (method == null)
			return dataValue;

		//try to invoke the method
		try {
    	  dataValue =  method.invoke(invokeObject);
    	} catch (IllegalArgumentException e) {
    	} catch (IllegalAccessException e) {
    	} catch (InvocationTargetException e) {
    		logger.debug("Note: exception occurred calling method found by reflection" + e.getMessage());
    	}
		return dataValue;
	}
	/**
	 * getPamConversionMetadata - get data from the NBS_conversion_mapping table
	 * Reverse map the key to the To Question Identifier. (Or question id + to value if codesets differ.)
	 * @param condition code - Note- This code should be in the Conversion Condition table
	 * @returns - Hashmap of conversion metadata
	 */
	private static HashMap<String,NBSConversionMappingDT> getPamComversionMetadata(String conditionCd)	{
		PamConversionDAO pamConversionDAO = new PamConversionDAO();
		//fromQuesIdMap = pamConversionDAO.getCachedQuestionIdMap(conditionCd);
		fromQuesIdMap = pamConversionDAO.getNbsConversionMetadataForNND(conditionCd);
		HashMap<String,NBSConversionMappingDT> toQuesIdMap = new HashMap<String,NBSConversionMappingDT>();
		batchListMap = new HashMap<String, ArrayList>();
		//for our purposes we need the To Id in the map and not the From Key
		Iterator iter = fromQuesIdMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			NBSConversionMappingDT conversionMeta = (NBSConversionMappingDT)
					fromQuesIdMap.get(key);
			if (conversionMeta.getConditionCdGroupId() != null &&
					conversionMeta.getToQuestionId() != null && !conversionMeta.getToQuestionId().isEmpty()) {
				//check batchMap stuff first..
				if (conversionMeta.getConversionType() != null && !conversionMeta.getConversionType().isEmpty() && conversionMeta.getConversionType().contains("ToBatch")) {
					if (batchListMap.get(conversionMeta.getToQuestionId()) != null)
						batchListMap.get(conversionMeta.getToQuestionId()).add(conversionMeta);
					else {
						ArrayList theArrayList = new ArrayList<NBSConversionMappingDT>();
						theArrayList.add(conversionMeta); //first entry
						batchListMap.put(conversionMeta.getToQuestionId(), theArrayList);
					}
				} else 	if(conversionMeta.getToCode()!=null)
					key = conversionMeta.getToQuestionId().trim()+conversionMeta.getToCode().trim();
				else
					key = conversionMeta.getToQuestionId().trim();
				toQuesIdMap.put(key, conversionMeta);
			} //if
		} //while next
		return toQuesIdMap;
	}
	/**
	 * updateParticipationFromFieldValue - unfortunately we don't have the participation type
	 * in our metadata unless we were going to another participation. The only participation
	 * update in the conversion data is the Investigator Start Time. It is supported in this
	 * method.
	 * @param investigationProxyVO (to update)
	 * @param convertMetaData
	 * @param quesMetaData
	 * @param fieldValue (should be Timestamp)
	 */
	private static void updateParticipationFromFieldValue(
			InvestigationProxyVO investigationProxyVO,
			NBSConversionMappingDT convertMetaData,
			NbsQuestionMetadata quesMetaData, Object fieldValue) {
		String parType = getParticipationTypeForIdentifier(convertMetaData.getFromQuestionId());
		if (parType == null) {
			logger.warn("Unable to update participation value from field for Master Msg for question " + convertMetaData.getFromQuestionId());
			return;
		}
		Iterator<Object> parItr = investigationProxyVO.getPublicHealthCaseVO().getTheParticipationDTCollection().iterator();
		while (parItr.hasNext()) {
			ParticipationDT participationDT = (ParticipationDT) parItr.next();
			if (participationDT.getTypeCd().equalsIgnoreCase(parType)) {
				try {
					Timestamp fromTime = (Timestamp) fieldValue;
					participationDT.setFromTime(fromTime);
				} catch (Exception ex) {
					logger.debug("unable to update participation value for "   + convertMetaData.getFromQuestionId() + " for class " + fieldValue.getClass());
				}
				break;
			}

		}
		return;
	}
	private static String getParticipationTypeForIdentifier(String quesId) {
		if (quesId.equals(PageConstants.DATE_ASSIGNED_TO_INVESTIGATION))
			return NEDSSConstants.PHC_INVESTIGATOR;  //participation type
		return null;
	}

	/**
	 * Process a single set in a PageBuilder Batch Entry
	 * @param be
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param toQuesIdMap
	 * @param batchObservationVOList
	 */
	private static void processBatchEntry(BatchEntry be,
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap, Map<String,
			ObservationVO> batchObservationVOList) {
		logger.debug("in processBatchEntry()");
		Map <String,String> ansMap = be.getAnswerMap();
		//if no answers - error condition.
		Iterator ansIter = ansMap.keySet().iterator();
		if (!ansIter.hasNext()) {
			logger.debug("**** PageBuilderToMaster: processBatchEntry - Batch Entry answer set is empty");
			return;
		}

		//cycle through the answers to look for data in nbs_conversion_mapping to see what kind of conversion we have
		ArrayList<NBSConversionMappingDT> convertMetaDataToQuesList = null;
		while (ansIter.hasNext()) {
			String keyVal = (String) ansIter.next();
			convertMetaDataToQuesList = batchListMap.get(keyVal);
			if (convertMetaDataToQuesList != null && !convertMetaDataToQuesList.isEmpty())
				break;
		}
		if (convertMetaDataToQuesList == null)
				return;
		//find the conversion type
		NBSConversionMappingDT convertMetaData = convertMetaDataToQuesList.get(0);
		String conversionType = convertMetaData.getConversionType();
		if (conversionType == null || conversionType.isEmpty())
			return;

		if  (conversionType.equals(NNDConstantUtil.QUESTION_TO_BATCH))
			processBatchToQuestionBE( be, convertMetaData.getTriggerQuestionId(), investigationProxyVO, formLevelObservation, toQuesIdMap);
		else if (conversionType.equals(NNDConstantUtil.MULTI_TO_BATCH))
			processBatchToMulti( be, investigationProxyVO, formLevelObservation, toQuesIdMap, batchObservationVOList);
	}
	/**
	 *  QuestionToNulti-  A repeating batch goes to a multiselect in legacy.
	 * @param be
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param toQuesIdMap
	 * @param batchObservationVOList
	 */
	private static void processBatchToMulti(BatchEntry be,
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			HashMap<String,NBSConversionMappingDT> toQuesIdMap,
			Map<String,ObservationVO> batchObservationVOList) {
		logger.debug("in processBatchToMulti()");
		ObservationVO observationVO = null;
		Iterator ansIter = be.getAnswerMap().keySet().iterator();
		String fromQuestionId = null;
		while (ansIter.hasNext()) {
			Boolean wasProcessed = false;
			String theKey = (String) ansIter.next();
			String theVal = be.getAnswerMap().get(theKey);
			if (theVal == null) {
				logger.debug("processBatchToMulti - value is null for " + theKey);
				continue;
			}
			if (theVal.contains("^")) //OTH
				theVal = theVal.substring(0, theVal.indexOf("^"));

			//Look at the ArrayList to see where we are going
			ArrayList<NBSConversionMappingDT> convertQuesArray = batchListMap.get(theKey);
			if (convertQuesArray == null || convertQuesArray.isEmpty())
				continue;
			for (int i = 0; i < convertQuesArray.size(); ++i) {
				NBSConversionMappingDT convertDT = convertQuesArray.get(i);
				if (convertDT.getToCode() != null &&
						!convertDT.getToCode() .isEmpty() &&
						convertDT.getToCode().trim().equals(theVal))	 {
					fromQuestionId = convertDT.getFromQuestionId();
					if (batchObservationVOList.containsKey(convertDT.getFromQuestionId()))
						observationVO = batchObservationVOList.get(convertDT.getFromQuestionId());
					else
						observationVO  = populateGenericObservationVO(convertDT.getFromQuestionId(), convertDT.getFromLabel());
					wasProcessed = addMultiObservationFromBatchEntry(be.getAnswerMap().get(theKey), convertDT, observationVO, formLevelObservation, investigationProxyVO);

					break; // only one matches the trigger
				}
			}
			//There is only one observation for a legacy multiselect
			if (wasProcessed && !batchObservationVOList.containsKey(fromQuestionId)) {
					batchObservationVOList.put(fromQuestionId, observationVO);
					linkQuestionToForm(investigationProxyVO, observationVO, formLevelObservation, null, null);
			}
		} //while another answer
		return;
	}

	/**
	 * A repeating batch goes to a multiselect in legacy. QuestionToBatch
	 * @param theVal
	 * @param convertMetaData
	 * @param observationVO
	 * @param formLevelObservation
	 * @param investigationProxyVO
	 * @return
	 */
	private static Boolean addMultiObservationFromBatchEntry(String theVal,
			NBSConversionMappingDT convertMetaData,
			ObservationVO observationVO,
			ObservationVO formLevelObservation,
			InvestigationProxyVO investigationProxyVO) {
		logger.debug("in addMultiObservationFromBatchEntry(" + convertMetaData.getToQuestionId() + ")");
		Boolean processed = false;
		if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.CODED)) {
			String theCode = null;
			String valOther = null;

			if (theVal.startsWith("OTH^")) {
				//later will put other value into an entirely new observation with a txtObs
				if (theVal.length() > theVal.indexOf("^") + 1) {
					valOther = theVal.substring(theVal.indexOf("^") + 1, theVal.length());
				}
				theVal = theVal.substring(0, theVal.indexOf("^"));
				//go ahead and store the other if we have a From Other Ques Id in the Metadata
				if (convertMetaData.getFromOtherQuestionId() != null && !convertMetaData.getFromOtherQuestionId().isEmpty()) {
						//get the label if it is in the conversion metadata
					String otherDescription = getFromQuestionLabel(convertMetaData.getFromOtherQuestionId());
					if (otherDescription == null) {
					    	if (convertMetaData.getFromLabel() != null)
					    		otherDescription = "Other " + convertMetaData.getFromLabel();
					    	else
					    		otherDescription = "Other";
					}
					//add a text observation for the Other value
					addOtherTextObservation(valOther,
										convertMetaData.getFromOtherQuestionId(),
										otherDescription,
										formLevelObservation,
										investigationProxyVO);
				} //ID to store the question
			} //OTH is present

			//need to convert code if mapping is present
			if (convertMetaData.getFromCode() != null &&
						!convertMetaData.getFromCode().trim().isEmpty())
				theCode = convertMetaData.getFromCode().trim();
			else
				theCode = theVal.trim();

			if (!theCode.trim().isEmpty())
				processed = addSingleSelectObservation(observationVO, theCode);
		}
		return processed;
	}

	/**
	 * MultiToBatch - A list of batch lines is going to questions depending on the trigger question value.
	 * A BE represents a single line of a batch entry item. One of the answers may equate to a
	 * legacy question label. If so, move any others that map.
	 * @param be
	 * @param toTriggerQuesId
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param toQuesIdMap
	 */
	private static void processBatchToQuestionBE(BatchEntry be,
			String toTriggerQuesId,
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap) {
		logger.debug("in processBatchToQuestionBE(" + toTriggerQuesId + ")");
		// find the value of the Trigger Question
		Iterator ansIter = be.getAnswerMap().keySet().iterator();
		String triggerVal = "";
		while (ansIter.hasNext()) {
			String theKey = (String) ansIter.next();
			if (theKey.trim().equalsIgnoreCase(toTriggerQuesId.trim()))
				triggerVal = be.getAnswerMap().get(theKey);
		}
		//Look at the other answers and try to map them to legacy
		ansIter = be.getAnswerMap().keySet().iterator();
		while (ansIter.hasNext()) {
			String theKey = (String) ansIter.next();
			if (theKey.trim().equalsIgnoreCase(toTriggerQuesId.trim()))
				continue;
			String theVal = be.getAnswerMap().get(theKey);
			//Look at the ArrayList to see where we are going
			ArrayList<NBSConversionMappingDT> convertQuesArray = batchListMap.get(theKey);
			if (convertQuesArray == null || convertQuesArray.isEmpty())
				continue;
			for (int i = 0; i < convertQuesArray.size(); ++i) {
				NBSConversionMappingDT convertDT = convertQuesArray.get(i);
				if (convertDT.getTriggerQuestionValue() != null &&
						!convertDT.getTriggerQuestionValue().isEmpty() &&
						convertDT.getTriggerQuestionValue().trim().equals(triggerVal))	 {
					addObservationFromBatchEntry(theVal, convertDT, formLevelObservation, investigationProxyVO);
					break; // only one matches the trigger
				}
			} //for
		} //while answer in answer map
		return;
	}
	/**
	 * addObservationFromBatchEntry - add an observation for the repeating batch element
	 *
	 * @param theVal
	 * @param convertMetaData
	 * @param formLevelObservation
	 * @param investigationProxyVO
	 */
	private static void addObservationFromBatchEntry(String theVal,
			NBSConversionMappingDT convertMetaData,
			ObservationVO formLevelObservation,
			InvestigationProxyVO investigationProxyVO) {
		logger.debug("in addObservationFromBatchEntry(" + convertMetaData.getToQuestionId() + ")");
		ObservationVO observationVO  = populateGenericObservationVO(convertMetaData.getFromQuestionId(), convertMetaData.getFromLabel());
		Boolean processed = false;
		if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.CODED)) {
			String theCode = null;
			String valOther = null;

			if (theVal.startsWith("OTH^")) {
				//later will put other value into an entirely new observation with a txtObs
				if (theVal.length() > theVal.indexOf("^") + 1) {
					valOther = theVal.substring(theVal.indexOf("^") + 1, theVal.length());
				}
				theVal = theVal.substring(0, theVal.indexOf("^"));
			}
			//need to convert code if mapping is present
			if (convertMetaData.getFromCode() != null &&
						!convertMetaData.getFromCode().trim().isEmpty())
				theCode = convertMetaData.getFromCode().trim();
			else
				theCode = theVal.trim();

			if (!theCode.trim().isEmpty())
				processed = addSingleSelectObservation(observationVO, theCode);
			//Do we need to add an Other text observation?
			if (valOther != null && ! valOther.isEmpty()) {
				String otherDescription = getFromQuestionLabel(convertMetaData.getFromOtherQuestionId());
				if (otherDescription == null)
					otherDescription = "Other " + convertMetaData.getFromLabel();
				addOtherTextObservation(valOther,
									convertMetaData.getFromOtherQuestionId(),
									otherDescription,
									formLevelObservation,
									investigationProxyVO);
			}

		} else if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.DATE_DATATYPE)) {
			Timestamp timeStampDate = null;
			try {
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				Date date = df.parse(theVal);
				timeStampDate = new 	Timestamp(date.getTime());
			} catch (Exception ex) {
				logger.warn("PageBuilderToMasterMessage Error mapping batch Timestamp date field" +ex.getMessage());
				return;
			}
			processed = addDateObservation(observationVO, timeStampDate);

		} else if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.TEXT_DATATYPE)) {
			processed = addTextObservation(observationVO, theVal);

		} else if (convertMetaData.getToDataType().equalsIgnoreCase(NEDSSConstants.NUMERIC_DATATYPE)) {
			BigDecimal theNumber = null;
			try {
				theNumber = new BigDecimal(theVal);
			} catch (Exception ex) {
				logger.warn("PageBuilderToMasterMessage Error mapping batch Numeric field");
				return;
			}
			processed = addNumericObservation(observationVO, theNumber);
		}

		if (processed) {
			linkQuestionToForm(investigationProxyVO, observationVO, formLevelObservation, null, null);
		}
		return;
	}

	/**
	 * addOtherTextObservation - add a text obs for the Other value
	 * @param valOther
	 * @param fromOtherQuestionId
	 * @param fromOtherDescription
	 * @param formLevelObservation
	 * @param investigationProxyVO
	 */
	private static void addOtherTextObservation(String valOther,
			String fromOtherQuestionId,
			String fromOtherDescription,
			ObservationVO formLevelObservation,
			InvestigationProxyVO investigationProxyVO) {
		ObservationVO otherTxtObservation = populateGenericObservationVO(fromOtherQuestionId, fromOtherDescription);
		addTextObservation(otherTxtObservation, valOther);
		linkQuestionToForm(investigationProxyVO, otherTxtObservation, formLevelObservation, null, null);
		return;
	}

	/**
	 * Often where Other is valid in a single or multiselect question,
	 * the other value may be specified. If that is so, then we
	 * need to create a text observation for the Other value.
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param toQuesIdMap
	 * @param convertMetaData
	 * @param quesMetaData
	 * @param addTime
	 * @param lastChgTime
	 * @param theOtherVal
	 */
	private static void addOtherTextObservation(
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap,
			NBSConversionMappingDT convertMetaData,
			NbsQuestionMetadata quesMetaData,
			Timestamp addTime,
			Timestamp lastChgTime,
			String theOtherVal) {

			String otherIdentifier = quesMetaData.getQuestionIdentifier() + NEDSSConstants.OTHER;
			//There should be a mapping, legacy doesn't follow the PageBuilder other convention
			if (toQuesIdMap.containsKey(convertMetaData.getToQuestionId()+NEDSSConstants.OTHER)) {
				NBSConversionMappingDT convertMetaDataOther = toQuesIdMap.get(convertMetaData.getToQuestionId()+NEDSSConstants.OTHER);
				otherIdentifier = convertMetaDataOther.getFromQuestionId();
			}
			ObservationVO otherTxtObservation = populateGenericObservationVO(otherIdentifier, "Other "+quesMetaData.getQuestionLabel());
			addTextObservation(otherTxtObservation, theOtherVal);
			investigationProxyVO.getTheObservationVOCollection().add(otherTxtObservation);
			ActRelationshipDT actRelationshipDT =
					populateActRelationshipToJoinToForm(
							otherTxtObservation.getTheObservationDT().getUid(),
							formLevelObservation.getTheObservationDT().getUid(),
							addTime,
							lastChgTime);
			
			logger.debug("Info: Created act relationship for To_ques=" + otherIdentifier);
			formLevelObservation.getTheActRelationshipDTCollection().add(actRelationshipDT); //act relationship for Other txt obs
	}
	private static String getFromQuestionLabel (String questionId) {
		NBSConversionMappingDT convertMetaData = (NBSConversionMappingDT) fromQuesIdMap.get(questionId);
		if (convertMetaData == null)
			return null;
		return (convertMetaData.getFromLabel());
	}

	/**
	 * checkIfAnySpecialFieldsPresent - The logic behind these moves requires special processing.
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param pageActProxyVO
	 * @param toQuesIdMap
	 */
	private static void checkIfAnySpecialFieldsPresent(
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			PageActProxyVO pageActProxyVO,
			HashMap<String, NBSConversionMappingDT> toQuesIdMap) {
		logger.debug(" in checkIfAnySpecialFieldsPresent()");
		//check for Birth Country in the Patient Person
		String birthCountryCd = getBirthCountryCd(pageActProxyVO);
		if (birthCountryCd != null) {
			addGenericCodedObservation(NNDConstantUtil.HEP255,
					NNDConstantUtil.HEP255_DESC,
					birthCountryCd,
					investigationProxyVO,
					formLevelObservation,
					null,   //fromTime
					null);  //lastChgTime);
		} //HEP255 Birth Place


	}
	/**
	 * Get the Birth Country from the Subject's PersonDT
	 * @param pageActProxyVO
	 * @return BirthCountryCd or null
	 */
	private static String getBirthCountryCd(PageActProxyVO pageActProxyVO) {
		PersonVO patientVO = null;
		String birthCountryCd = null;
		try {
			patientVO = PageLoadUtil.getPersonVO("SubjOfPHC",
					(PageProxyVO) pageActProxyVO);
		} catch (NEDSSAppException e) {
			logger.error("Exception finding patient" + e.getMessage());
		}
		if (patientVO != null && patientVO.getThePersonDT() != null &&
				patientVO.getThePersonDT().getBirthCntryCd() != null)
			birthCountryCd = patientVO.getThePersonDT().getBirthCntryCd();
		return birthCountryCd;
	}
	/**
	 * Add a coded observation with the following values.
	 * @param theFromQuesId
	 * @param theFromQuesIdDesc
	 * @param theCode
	 * @param investigationProxyVO
	 * @param formLevelObservation
	 * @param fromTime
	 * @param lastChgTime
	 */
	private static void addGenericCodedObservation(String theFromQuesId,
			String theFromQuesIdDesc,
			String theCode,
			InvestigationProxyVO investigationProxyVO,
			ObservationVO formLevelObservation,
			Timestamp fromTime,
			Timestamp lastChgTime) {
		ObservationVO observationVO  = populateGenericObservationVO(theFromQuesId, theFromQuesIdDesc);
		observationVO.setTheObsValueCodedDTCollection(new ArrayList<Object>());
		addSingleSelectObservation(observationVO, theCode);
		linkQuestionToForm(investigationProxyVO, observationVO, formLevelObservation, fromTime, lastChgTime);
		return;
	}
	/**
	 * linkQUestionToForm - add the Observation and
	 * add an Act Relationship to the formLevelObservation for the question.
	 * Note that addTime and lastChngTime may be null.
	 * @param investigationProxyVO
	 * @param observationVO
	 * @param formLevelObservation
	 * @param addTime
	 * @param lastChgTime
	 */
	private static void linkQuestionToForm(InvestigationProxyVO investigationProxyVO,
			ObservationVO observationVO,
			ObservationVO formLevelObservation,
			Timestamp addTime,
			Timestamp lastChgTime) {
		investigationProxyVO.getTheObservationVOCollection().add(observationVO);
		Long targetActUid = formLevelObservation.getTheObservationDT().getUid();
		Long sourceActUid = observationVO.getTheObservationDT().getUid();
		ActRelationshipDT actRelationshipDT = populateActRelationshipToJoinToForm(sourceActUid, targetActUid, addTime, lastChgTime);
		logger.debug("Created act_relationship for from_ques=" + observationVO.getTheObservationDT().getCd());
		formLevelObservation.getTheActRelationshipDTCollection().add(actRelationshipDT);
	return;
	}


	/**
	 * TBD: Add structure numeric.
	 * @param observationVO
	 * @param quesMeta
	 * @param ansDT
	 */
	private static void addStructuredNumericObservation(
			ObservationVO observationVO, NbsQuestionMetadata quesMeta,
			NbsCaseAnswerDT ansDT) {
		if (observationVO.getTheObsValueCodedDTCollection() == null)
			observationVO.setTheObsValueCodedDTCollection(new ArrayList<Object>());

		String answer = ansDT.getAnswerTxt().trim();

		String answers[] = answer.split("\\^");
		for (int i = 0; i < answers.length; ++i) {
			if (!answers[i].trim().isEmpty()) {
				ObsValueCodedDT obsValueCodedDT = new ObsValueCodedDT();
				obsValueCodedDT.setCode(answers[i].trim());
				obsValueCodedDT.setItDelete(false);
				obsValueCodedDT.setItDirty(false);
				obsValueCodedDT.setItNew(false);
				obsValueCodedDT.setObservationUid(observationVO.getTheObservationDT().getUid());
				observationVO.getTheObsValueCodedDTCollection().add(obsValueCodedDT);
			}
		}
		return;
	}
	
	public static final HashMap formCodeForCond = new HashMap(){
		{
		   put("10030","INV_FORM_VAR");
		   put("10100","INV_FORM_HEPB");
		   put("10101","INV_FORM_HEPC");
		   put("10102","INV_FORM_HEPGEN");
		   put("10103","INV_FORM_HEPGEN");
		   put("10104","INV_FORM_HEPBV");
		   put("10105","INV_FORM_HEPGEN");
		   put("10106","INV_FORM_HEPCV");
		   put("10110","INV_FORM_HEPA");
		   put("10140","INV_FORM_MEA");
		   put("10150","INV_FORM_BMDNM");
		   put("10190","INV_FORM_PER");
		   put("10200","INV_FORM_RUB");
		   put("10220","INV_FORM_RVCT");
		   put("10370","INV_FORM_CRS");
		   put("10481","INV_FORM_HEPGEN");
		   put("10590","INV_FORM_BMDHI");
		   put("10650","INV_FORM_BMDGEN");
		   put("11700","INV_FORM_BMDGAS");
		   put("10716","INV_FORM_BMDGEN");
		   put("11717","INV_FORM_BMDSP");
		   put("11720","INV_FORM_BMDSP");
		   put("11723","INV_FORM_BMDSP");
		   put("999999","INV_FORM_HEPGEN");
		}
		};
		
	/**
	 * Get the Legacy form for the condition
	 * @param condCd
	 * @return Legacy Form Code (default to INV_FORM_GEN)
	 */
	private static String getLegacyFormCode(String condCd) {
		String formCd = "INV_FORM_GEN";
		if (formCodeForCond.containsKey(condCd))
			formCd = (String) formCodeForCond.get(condCd);			
		return formCd;
	}

}
