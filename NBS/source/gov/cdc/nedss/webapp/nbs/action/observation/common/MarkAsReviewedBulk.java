package gov.cdc.nedss.webapp.nbs.action.observation.common;




/**

 * Title:        TransferOwnershipLoadLab
 * Description: This class places appropriate attributes in request object when
 * user persses submit on transfer ownership for Lab page.
 * Copyright:    Copyright (c) 2001
 * Company:      CSC
 * @version 1.0

 */
import gov.cdc.nedss.act.observation.dt.ObservationDT;
import gov.cdc.nedss.act.observation.vo.ObservationVO;
import gov.cdc.nedss.entity.person.vo.PersonVO;
import gov.cdc.nedss.exception.NEDSSAppConcurrentDataException;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.CoinfectionSummaryVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.LabResultProxyVO;
import gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.MorbidityProxyVO;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.ejb.nbsdocumentejb.vo.NBSDocumentVO;
import gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil;
import gov.cdc.nedss.systemservice.nbscontext.NBSContext;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.util.DropDownCodeDT;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.ObservationUtil;
import gov.cdc.nedss.webapp.nbs.action.nbsDocument.util.NBSDocumentActionUtil;
import gov.cdc.nedss.webapp.nbs.action.observation.labreport.CommonLabUtil;
import gov.cdc.nedss.webapp.nbs.action.observation.morbidityreport.util.FieldMappingBuilder;
import gov.cdc.nedss.webapp.nbs.action.observation.morbidityreport.util.MorbidityUtil;
import gov.cdc.nedss.webapp.nbs.form.morbidity.MorbidityForm;
import gov.cdc.nedss.webapp.nbs.form.nbsdocument.NbsDocumentForm;
import gov.cdc.nedss.webapp.nbs.form.observationreview.ObservationNeedingReviewForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;


public class MarkAsReviewedBulk extends Action{

	 
    //For logging
    static final LogUtils logger = new LogUtils(MarkAsReviewedBulk.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
     throws IOException, ServletException{
		//This is temporary until we decide where to save the 'Reason for not further action'
		
		ObservationNeedingReviewForm observationGeneralForm = (ObservationNeedingReviewForm)form;
		String processingDecisionSelected = observationGeneralForm.getProcessingDecisionSelected();
		String processingDecisionSelectedText="";
		ArrayList<Object> dropdownsProcessingDecision = CachedDropDowns.getCodedValueForType(observationGeneralForm.getProcessingDecisionLogic());
		ArrayList<Object> dropdownsNonSTDHIVProcessingDecision = CachedDropDowns.getCodedValueForType(NEDSSConstants.NBS_NO_ACTION_RSN);
		
		/*
		if(processingDecisionSelected==null){
			processingDecisionSelected = observationGeneralForm.getNonSTDProcessingDecisionSelected();
			dropdownsProcessingDecision = CachedDropDowns.getCodedValueForType(observationGeneralForm.getNonSTDProcessingDecisionLogic());
		}*/
			
		for(int i=0; i<dropdownsProcessingDecision.size();i++){
			if(((DropDownCodeDT)dropdownsProcessingDecision.get(i)).getKey().equalsIgnoreCase(processingDecisionSelected))
				processingDecisionSelectedText=((DropDownCodeDT)dropdownsProcessingDecision.get(i)).getValue();
		}
		if(processingDecisionSelectedText.isEmpty()){
			for(int i=0; i<dropdownsNonSTDHIVProcessingDecision.size();i++){
				if(((DropDownCodeDT)dropdownsNonSTDHIVProcessingDecision.get(i)).getKey().equalsIgnoreCase(processingDecisionSelected))
					processingDecisionSelectedText=((DropDownCodeDT)dropdownsNonSTDHIVProcessingDecision.get(i)).getValue();
			}
			
		}
		
		String markAsReviewedComments = observationGeneralForm.getMarkAsReviewedComments();
	
		
		
		String checkBoxIdsLabs = observationGeneralForm.getSelectedcheckboxIdsLabs();
		String checkBoxMprIdsLabs = observationGeneralForm.getSelectedcheckboxMprIdsLabs();
		String checkBoxIdsMorbs = observationGeneralForm.getSelectedcheckboxIdsMorbs();
		String checkBoxMprIdsMorbs = observationGeneralForm.getSelectedcheckboxMprIdsMorbs();
		String checkBoxIdsCases = observationGeneralForm.getSelectedcheckboxIdsCases();
		String checkBoxMprIdsCases = observationGeneralForm.getSelectedcheckboxMprIdsCases();
		
		
		ArrayList<String> UIDLabs = convertFromStringToArray(checkBoxIdsLabs);
		ArrayList<String> mprUIDLabs = convertFromStringToArray(checkBoxMprIdsLabs);
		
		ArrayList<String> UIDMorb = convertFromStringToArray(checkBoxIdsMorbs);
		ArrayList<String> mprUIDMorbs = convertFromStringToArray(checkBoxMprIdsMorbs);
		
		ArrayList<String> UIDCase = convertFromStringToArray(checkBoxIdsCases);
		ArrayList<String> mprUIDCases = convertFromStringToArray(checkBoxMprIdsCases);
		
		
		String processingDecisionBFP="BFP";
		String processingDecisionPriority="NPP";
		
		int labErrorBFP=0, labErrorPriority=0, labProcessed=0;
		int morbErrorBFP=0, morbErrorPriority=0, morbProcessed=0;
		
		String notProcessedDocUid="";
		
		String observationUIDString, mprUIDString;
		Long observationUID, mprUID;		
	
		//LOOP FOR PROCESSING LABS
		for(int i=0; i<UIDLabs.size(); i++){
			observationUIDString = UIDLabs.get(i);
			mprUIDString = mprUIDLabs.get(i);
			if(!observationUIDString.isEmpty()){
				
				CommonLabUtil labUtil = new CommonLabUtil();
		    	LabResultProxyVO labResultProxyVO = labUtil.getLabResultProxyVO(new Long(observationUIDString), request.getSession());
		    	
		    	ArrayList<String> conditionList = labResultProxyVO.getTheConditionsList();
		    	
		    	boolean validated=true;
		    	
		    	if(conditionList!=null){
			    	for(int j=0; j<conditionList.size() && validated; j++){
			    		String labCondition=conditionList.get(j);
			    		labCondition=CachedDropDowns.getConditionDesc(labCondition);
							if(processingDecisionSelected.equals(processingDecisionBFP) && !labCondition.contains("Syphilis")){
								labErrorBFP++;
								validated=false;
								notProcessedDocUid+=observationUIDString+"|";
							}else
								if(processingDecisionSelected.equals(processingDecisionPriority) && labCondition.contains("Syphilis")){
									labErrorPriority++;
									validated=false;
									notProcessedDocUid+=observationUIDString+"|";
								}
			    	}
		    	}
		    	
				if(validated){
					observationUID=Long.parseLong(observationUIDString);
					mprUID = Long.parseLong(mprUIDString);
					//This is temporary until we decide where to save the 'Reason for not further action'

					markAsReviewLab(mapping, observationGeneralForm, request, response, observationUID, mprUID, processingDecisionSelected, markAsReviewedComments);
					labProcessed++;
				}
			}
		}
		
		//LOOP FOR PROCESSING MORBS
		for(int i=0; i<UIDMorb.size(); i++){
			observationUIDString = UIDMorb.get(i);
			mprUIDString = mprUIDMorbs.get(i);
			boolean validated = true;

	    	
	    	try{
		    	MorbidityUtil morbidityUtil = new MorbidityUtil();
		    	MorbidityProxyVO morbidityProxyVO = morbidityUtil.getProxyFromEJB(new Long(observationUIDString), request.getSession());
		    	
		    	Collection<ObservationVO> observationVOCollection = morbidityProxyVO.getTheObservationVOCollection();
		    	Iterator<ObservationVO> iter = observationVOCollection.iterator();
		    	
		    	while(iter.hasNext() && validated){
		    		ObservationVO obsVO=iter.next();
		    		String morbCondition = obsVO.getTheObservationDT().getCd();
		    		morbCondition=CachedDropDowns.getConditionDesc(morbCondition);
					if(processingDecisionSelected.equals(processingDecisionBFP)){
						morbErrorBFP++;
						validated=false;
						notProcessedDocUid+=observationUIDString+"|";
					}else
						if(processingDecisionSelected.equals(processingDecisionPriority) && morbCondition.contains("Syphilis")){
							morbErrorPriority++;
							validated=false;
							notProcessedDocUid+=observationUIDString+"|";
						}
		    	}
		    	
	    	}catch(Exception e){
	    		
	    		//TODO: handle exception
	    	}

			if(validated){
				if(!observationUIDString.isEmpty()){
					observationUID=Long.parseLong(observationUIDString);
					mprUID = Long.parseLong(mprUIDString);

					markAsReviewMorb(mapping, observationGeneralForm, request, response, observationUID, mprUID, processingDecisionSelected, markAsReviewedComments);
					morbProcessed++;
				}
			}
		}
		
		//LOOP FOR PROCESSING CASES
		for(int i=0; i<UIDCase.size(); i++){
			observationUIDString = UIDCase.get(i);
			mprUIDString = mprUIDCases.get(i);
			if(!observationUIDString.isEmpty()){
				observationUID=Long.parseLong(observationUIDString);

				markAsReviewCase(mapping, form, request, response, observationUID, processingDecisionSelected, markAsReviewedComments);  
			}
		}
		
		
		//Success and error messages
		
		int processedDocuments = labProcessed + morbProcessed + UIDCase.size();
		int errorDocuments = labErrorBFP+labErrorPriority+morbErrorBFP+morbErrorPriority;
		int totalDocuments = UIDLabs.size()+UIDMorb.size()+UIDCase.size();
		
		
		String confirmationMessage ="", errorMessage="";
		if(processedDocuments>1 && errorDocuments==0)
			confirmationMessage="The selected <b>"+processedDocuments+"</b> documents have been successfully marked as reviewed as <b>'"+processingDecisionSelectedText+"'</b>.";
		if(processedDocuments==1 && errorDocuments==0)
			confirmationMessage="The selected <b>"+processedDocuments+"</b> document has been successfully marked as reviewed as <b>'"+processingDecisionSelectedText+"'</b>.";
		if(processedDocuments==0 && errorDocuments>1)
			errorMessage="The selected <b>"+errorDocuments+"</b> documents can not have the processing decision of <b>'"+processingDecisionSelectedText+"'</b> applied. Please, select another processing decision and try again.";
		if(processedDocuments==0 && errorDocuments==1)
			errorMessage="The selected <b>"+errorDocuments+"</b> document can not have the processing decision of <b>'"+processingDecisionSelectedText+"'</b> applied. Please, select another processing decision and try again.";
		if(processedDocuments>0 && errorDocuments>0)
			confirmationMessage="<b>"+processedDocuments+"</b> of the selected <b>"+totalDocuments+"</b> documents were successfully marked as reviewed as <b>'"+processingDecisionSelectedText+"'</b>."+
		" The remaining documents could not be marked as reviewed and will need to have another processing decision selected.";

		
		request.getSession().setAttribute("msgBlock", confirmationMessage);
		request.getSession().setAttribute("errorBlock", errorMessage);
		request.getSession().setAttribute("notProcessedDocUid", notProcessedDocUid);
		
		return mapping.findForward("markAsReviewedBulkConfirmation");
	}
	
	public void markAsReviewLab(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            long obsUID, long mprUid, String processingDecision, String markAsReviewedComment)
     throws IOException, ServletException{
		
		 HttpSession session = request.getSession();
		 

		    if (session == null) {
		      logger.fatal("error no session");
		    //  return mapping.findForward("login");
		    }
		    /*if (request.getParameter("mode") != null && request.getParameter("mode").equals("print")) {
				return printELRDocument(mapping, form, request, response);
		    }
				if (request.getParameter("documentUid") != null) {
					return viewELRDocument(mapping, form, request, response);
		    }*/
				
				NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) session.getAttribute("NBSSecurityObject");
		    if (nbsSecurityObj == null) {
					logger.fatal("Error: No securityObj in the session, go back to login screen");
		     // return mapping.findForward("login");
		    }

		    String contextAction = "";
		    try {

		    	contextAction = request.getParameter("ContextAction");
		    	String sCurrTask = NBSContext.getCurrentTask(session);

		    	//Long mprUid = null;
		    	//Long obsUID = null;
		    	/*if(request.getParameter("PatientPersonUID") != null){
		    		mprUid =Long.valueOf(request.getParameter("PatientPersonUID"));
		    	}else{

		    		mprUid = (Long) NBSContext.retrieve(session, "DSPatientPersonUID");
		    	}

		    	if (mprUid != null) {
		    		request.setAttribute("personUID", mprUid);
		    	}*/

		    	//Object obsObj = NBSContext.retrieve(session, "DSObservationUID");
/*
		    	if (obsObj instanceof Long) {
		    		obsUID = (Long) NBSContext.retrieve(session, "DSObservationUID");
		    	} else if (obsObj instanceof String) {
		    		obsUID = new Long((String) NBSContext.retrieve(session,"DSObservationUID"));
		    	}*/
		    	CommonLabUtil labUtil = new CommonLabUtil();
		    	LabResultProxyVO labResultProxyVO = labUtil.getLabResultProxyVO(obsUID, session);
		    	List<ObservationVO> obsColl = (ArrayList<ObservationVO> ) labResultProxyVO.getTheObservationVOCollection();

		    	ObservationDT tempDT = new ObservationDT();
		    	ObservationDT obsDT = new ObservationDT();
		    	ObservationVO obsVO = new ObservationVO();
		    	Map<Object,Object> tMap = new TreeMap<Object,Object>();
		    	for (Iterator<ObservationVO> iter = obsColl.iterator(); iter.hasNext(); ) {
		    		obsVO = iter.next();
		    		tempDT = obsVO.getTheObservationDT();
		    		tMap.put(tempDT.getObservationUid(), tempDT);
		    	}

		    	obsDT = (ObservationDT) tMap.get(obsUID);
		    	String progAreaCd = obsDT.getProgAreaCd();
		    	String jurisdictionCd = obsDT.getJurisdictionCd();
		    	String sharedInd = obsDT.getSharedInd();
		    	String observationLocalUid = obsDT.getLocalId();

		    	NBSContext.store(session, "DSObservationUID", obsUID);
		    	NBSContext.store(session, "DSPatientPersonUID", mprUid);

		    	try {
		    	/*	if (contextAction.equalsIgnoreCase(NBSConstantUtil.EDIT)) {
		    			logger.info("\n\n####The contextaction in ViewLabReportSubmit class is :" + contextAction);
		    		} else if (contextAction.equalsIgnoreCase("ReturnToFileSummary") || contextAction.equalsIgnoreCase("ViewEventsPopup")) {
		    			NBSContext.store(session, NBSConstantUtil.DSFileTab, "1");
		    		} else if (contextAction.equalsIgnoreCase("ReturnToFileEvents")) {
		    			NBSContext.store(session, NBSConstantUtil.DSFileTab, "3");
		    		} else if (contextAction.equalsIgnoreCase("ManageEvents")) {
		    			String personLocalId = (String) session.getAttribute("DSPatientPersonLocalID");
		    			NBSContext.store(session, NBSConstantUtil.DSPatientPersonLocalID, personLocalId);
		    			session.removeAttribute("DSPatientPersonLocalID");
		    		} else if (contextAction.equalsIgnoreCase("CreateInvestigation")) {
		    			NBSContext.store(session, "DSInvestigationJurisdiction", jurisdictionCd);
		    			NBSContext.store(session, "DSObservationTypeCd", NEDSSConstants.LABRESULT_CODE);
		    			// NBSContext.store(session, "DSInvestigationCondition",
		    			// observationCd);
		    			NBSContext.store(session, "DSInvestigationProgramArea", progAreaCd);
		    			// for createInvestigation need to populate reporting source and
		    			// Ordering Provider
		    			//String processingDecision = (String) request
		    			//		.getParameter("markAsReviewReason");
		    			LabReportFieldMappingBuilder mapBuilder = new LabReportFieldMappingBuilder();
		    			TreeMap<Object,Object> loadTreeMap = mapBuilder.createLabReportLoadTreeMap(labResultProxyVO, obsUID, processingDecision);
		    			NBSContext.store(session, "DSLabMap", loadTreeMap);
		    		} else if (contextAction.equalsIgnoreCase("TransferOwnership")) {
		    			if (jurisdictionCd != null) {
		    				NBSContext.store(session, NBSConstantUtil.DSJurisdiction, jurisdictionCd);
		    			}
		    			if (progAreaCd != null) {
		    				NBSContext.store(session,NBSConstantUtil.DSProgramArea, progAreaCd);
		    			}
		    			if (sharedInd != null) {
		    				NBSContext.store(session, "DSObservationSharedInd", sharedInd);

		    			}

		    			NBSContext.store(session, "DSObservationLocalID", observationLocalUid);
		    			NBSContext.store(session, "DSPatientPersonUID", mprUid);
		    		}*/
		    		// ContextAction = Delete
		    	/*	else if (contextAction.equalsIgnoreCase(NBSConstantUtil.DELETE)) {
		    			if (sCurrTask.endsWith("Lab3")) {
		    				NBSContext.store(session, "DSFileTab", "3");
		    			} else if (sCurrTask.endsWith("Lab2")) {
		    				NBSContext.store(session, NBSConstantUtil.DSFileTab, "1");
		    			}
		    			String personLocalId = (String) session.getAttribute("DSPatientPersonLocalID");
		    			NBSContext.store(session, NBSConstantUtil.DSPatientPersonLocalID, personLocalId);

		    			// ##!!VOTester.createReport(form.getProxy(),
		    			// "obs-delete-store-pre");
		    			logger.debug("observationUID in Delete is :" + obsUID);
		    			String result = deleteHandler(obsUID, nbsSecurityObj, session, request, response);
		    			// ##!!VOTester.createReport(form.getProxy(),
		    			// "obs-delete-store-post");
		    			/*if (result.equals("viewDelete") && sCurrTask.endsWith("Lab10")) {
		    				logger.debug("ObservationSubmit: viewDelete");

		    				ActionForward af = mapping.findForward(contextAction);
		    				ActionForward forwardNew = new ActionForward();
		    				StringBuffer strURL = new StringBuffer(af.getPath());
		    				strURL.append("?ContextAction=" + contextAction);
		    				strURL.append("&method=loadQueue");
		    				forwardNew.setPath(strURL.toString());
		    				forwardNew.setRedirect(true);
		    				return forwardNew;
		    			} else if (result.equals("viewDelete")
		    					&& !sCurrTask.endsWith("Lab10")) {
		    				logger.debug("ObservationSubmit: viewDelete");

		    				ActionForward af = mapping.findForward(contextAction);
		    				ActionForward forwardNew = new ActionForward();
		    				StringBuffer strURL = new StringBuffer(af.getPath());
		    				if(strURL.indexOf(NEDSSConstants.ManageEvents)>0){
		    					strURL.append("&ContextAction=" + contextAction);
		    				} else
		    					strURL.append("?ContextAction=" + contextAction);
		    				forwardNew.setPath(strURL.toString());
		    				forwardNew.setRedirect(true);
		    				return forwardNew;

		    			} else if (result.equals("deleteDenied")) {
		    				logger.debug("ObservationSubmit: deleteDenied");
		    				NBSContext.store(session, "DSObservationUID", obsUID);
		    				NBSContext.store(session, "DSPatientPersonUID", mprUid);

		    				ActionForward af = mapping.findForward("DeleteDenied");
		    				ActionForward forwardNew = new ActionForward();
		    				StringBuffer strURL = new StringBuffer(af.getPath());
		    				strURL.append("?ContextAction=DeleteDenied");
		    				forwardNew.setPath(strURL.toString());
		    				forwardNew.setRedirect(true);
		    				return forwardNew;
		    			}
		    		} else if (contextAction
		    				.equalsIgnoreCase(NBSConstantUtil.MarkAsReviewed)) {
*/
		    			//String processingDecision = (String) request
		    			//		.getParameter("markAsReviewReason");

		    			String result = markAsReviewedLabHandler(obsUID,
		    					processingDecision, markAsReviewedComment, nbsSecurityObj, session, request,
		    					response);
		    			if (result.equals(NEDSSConstants.RECORD_STATUS_PROCESSED)) {
		    				String successMsg = "The Lab Report has been successfully marked as Reviewed";
		    				logger.info(successMsg);
		    				request.setAttribute("displayInformationExists", successMsg);
		    			} else {
		    				logger.info("The Lab Report was not able to be set to Processed");
		    			}
		    		//} else if (contextAction
		    			/*	.equalsIgnoreCase(NBSConstantUtil.ClearMarkAsReviewed)) {
		    			String result = clearMarkAsReviewedHandler(obsUID,
		    					nbsSecurityObj, session, request, response);

		    			if (result.equals(NEDSSConstants.RECORD_STATUS_UNPROCESSED)) {
		    				String successMsg = "The Lab Report has been successfully marked as Unreviewed";
		    				logger.info(successMsg);
		    				request.setAttribute("displayInformationExists", successMsg);
		    			} else {
		    				logger.info("The Lab Report was not able to be set to UnProcessed");
		    			}

		    		}*/
		    	}

		    	catch (Exception ncde) {
		    		logger.fatal("Data Concurrency Error being raised ", ncde);
		    		//return mapping.findForward("dataerror");
		    	} finally {
		    		session.setAttribute("DSPatientPersonLocalID", null);
		    		session.removeAttribute("DSPatientPersonLocalID");
		    	}
		    }catch (Exception e) {
		    	logger.error("General exception in View Lab Report Submit: " + e.getMessage());
		    	e.printStackTrace();
		    	throw new ServletException("An error occurred in View Lab Report Submit: "+e.getMessage());
		    }  
		//    return mapping.findForward(contextAction);
		    
	}
	

	  private String markAsReviewedLabHandler(Long observationUid, String processingDecision, 
			  String markAsReviewedComment,
				NBSSecurityObj nbsSecurityObj, HttpSession session,
				HttpServletRequest request, HttpServletResponse response)
				throws NEDSSAppConcurrentDataException, java.rmi.RemoteException,
	      javax.ejb.EJBException, Exception {

	    String markAsReviewedFlag = "";
	    /**
	     * Call the mainsessioncommand
	     */
	    MainSessionCommand msCommand = null;
	    //ObservationUtil obsUtil = new ObservationUtil();
	    
	    String sBeanJndiName = JNDINames.OBSERVATION_PROXY_EJB;
	    String sMethod="processObservation";
	    Object[] oParams = new Object[]{observationUid};
	    if(processingDecision!=null && !processingDecision.trim().equals("")){
	     sMethod = "processObservationWithProcessingDecision";
	     //Map<String, Object> observationMap = obsUtil.createProcessingDecisionObservation(observationUid,"Lab",processingDecision,request);
	     oParams = new Object[]{
	             observationUid, processingDecision, markAsReviewedComment};
	    }
	   /* if(markAsReviewedComment!=null && !markAsReviewedComment.trim().equals("")){
		     sMethod = "processObservationWithProcessingDecision";
		     //Map<String, Object> observationMap = obsUtil.createProcessingDecisionObservation(observationUid,"Lab",processingDecision,request);
		     oParams = new Object[]{
		             observationUid, processingDecision,markAsReviewedComment};
		    }*/
	    
			logger.debug("ObservationSubmit: markAsReviewedHandler with observationID = " + observationUid);

	    /**
	     * Output ObservationProxyVO for debugging
	     */
	    MainSessionHolder holder = new MainSessionHolder();
	    msCommand = holder.getMainSessionCommand(session);
	    List<?> resultUIDArr = new ArrayList<Object> ();
			resultUIDArr = msCommand
					.processRequest(sBeanJndiName, sMethod, oParams);

	    boolean result = false;
	    if ( (resultUIDArr != null) && (resultUIDArr.size() > 0)) {
				logger.debug("Marked as Reviewed result and arg = "
						+ resultUIDArr.get(0));
	      result = ( (Boolean) resultUIDArr.get(0)).booleanValue();
	      logger.debug("The Marked AS Reviewed result is:" + result);
	      if (result) {
	        markAsReviewedFlag = "PROCESSED";
				} else {
	        markAsReviewedFlag = "UNPROCESSED";
	      }
	    }
	    return markAsReviewedFlag;
	  }
	  
		public void markAsReviewMorb(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request,
	            HttpServletResponse response,
	            long obsUID, long mprUid, String processingDecision, String markAsReviewedComment)
	     throws IOException, ServletException{
	
	  HttpSession session = request.getSession();

	    //get contextAction and pass to forward
	  //  String contextAction = request.getParameter("ContextAction");

	  //  String sCurrTask = NBSContext.getCurrentTask(session);
/*
	    if (contextAction == null || sCurrTask == null) {
	      session.setAttribute("error",
	                           "contextAction is " + contextAction + " sCurrTask " +
	                           sCurrTask);
	      throw new ServletException("contextAction is " + contextAction + " sCurrTask " +
	              sCurrTask);
	    }*/
	    try {
	    	NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) session.getAttribute(
	    			"NBSSecurityObject");
	    	MorbidityForm morbidityForm = null;
	    	String investigationType = (String)request.getParameter("investigationType");
	    	if(investigationType!=null)
	    		NBSContext.store(session, NBSConstantUtil.DSInvestigationType, investigationType);
	    //	morbidityForm = (MorbidityForm) form;
	    	//MorbidityProxyVO morbidityProxyVO = morbidityForm.getMorbidityProxy();
	    	//MorbidityUtil morbidityUtil = new MorbidityUtil();
	    	//ObservationVO morbReportVO = morbidityForm.getMorbidityReport();
	    	//ObservationDT morbReportDT = morbReportVO.getTheObservationDT();
/*
	    	String progAreaCd = morbReportDT.getProgAreaCd();
	    	String jurisdictionCd = morbReportDT.getJurisdictionCd();
	    	String sharedInd = morbReportDT.getSharedInd();
	    	String observationLocalUid = morbReportDT.getLocalId();
	    	//Long observationUid = morbReportDT.getObservationUid();
	    	String observationCd = morbReportDT.getCd();
*/
	    /*	PersonVO personVO = morbidityUtil.findPatientRevision(morbidityProxyVO);
	    //	Long personUid = personVO.getThePersonDT().getPersonUid();
	    	Long personParentUid = personVO.getThePersonDT().getPersonParentUid();
	    	String personLocalId = personVO.getThePersonDT().getLocalId();

	    	if (progAreaCd != null)
	    		NBSContext.store(session, NBSConstantUtil.DSProgramArea, progAreaCd);
	    	if (jurisdictionCd != null)
	    		NBSContext.store(session, NBSConstantUtil.DSJurisdiction, jurisdictionCd);
*//*
	    	NBSContext.store(session, "DSObservationSharedInd", sharedInd);
	    	NBSContext.store(session, "DSObservationLocalID", observationLocalUid);
	    	NBSContext.store(session, "DSObservationUID", obsUID);
	    	NBSContext.store(session, "DSObservationTypeCd",
	    			NEDSSConstants.MORBIDITY_CODE);*/
	    	/*
	    	NBSContext.store(session, "DSPatientPersonUID", personParentUid);
	    	NBSContext.store(session, NBSConstantUtil.DSPatientPersonLocalID,
	    			personLocalId);
	    	*/
/*

	    	if (contextAction.equalsIgnoreCase("ReturnToFileEvents")) {
	    		NBSContext.store(session, NBSConstantUtil.DSFileTab, "3");
	    	}

	    	else if (contextAction.equalsIgnoreCase("ReturnToFileSummary") || contextAction.equalsIgnoreCase("ViewEventsPopup")) {
	    		NBSContext.store(session, NBSConstantUtil.DSFileTab, "1");
	    	}*/

	    //	else if (contextAction.equalsIgnoreCase("MarkAsReviewed")) {

	    		try {


	    			//String processingDecision = (String)request.getParameter("markAsReviewReason");

	    			String result = markAsReviewedMorbHandler(obsUID, processingDecision, markAsReviewedComment, nbsSecurityObj,
	    					session,
	    					request, response);

	    			if (result.equals(NEDSSConstants.RECORD_STATUS_PROCESSED)) {
	    				String successMsg = "The Morbidity Report has been successfully marked as Reviewed";
	    				logger.info(successMsg);
	    				request.setAttribute("displayInformationExists", successMsg);
	    			}
	    			else {
	    				logger.info("The Morb Report was not able to be set to Processed");
	    			}

	    		}
	    		catch (Exception e) {
	    			logger.fatal("Error being raised ", e);
	    		//return mapping.findForward("dataerror");
	    		}
	    //	}

	    /*	else if (contextAction
	    			.equalsIgnoreCase(NBSConstantUtil.ClearMarkAsReviewed)) {
	    		try {
	    			String result = clearMarkAsReviewedHandler(observationUid,
	    					nbsSecurityObj, session, request, response);

	    			if (result.equals(NEDSSConstants.RECORD_STATUS_UNPROCESSED)) {
	    				String successMsg = "The Morbidity Report has been successfully marked as Unreviewed";
	    				logger.info(successMsg);
	    				request.setAttribute("displayInformationExists", successMsg);
	    			} else {
	    				logger.info("The Morb Report was not able to be set to UnProcessed");
	    			}
	    		} catch (Exception e) {
	    			logger.fatal("Error being raised ", e);
	    			return mapping.findForward("dataerror");
	    		}

	    	}

	    	else if (contextAction.equalsIgnoreCase("CreateInvestigation")) {

	    		NBSContext.store(session, "DSInvestigationJurisdiction", jurisdictionCd);
	    		NBSContext.store(session, "DSInvestigationCondition", observationCd);
	    		NBSContext.store(session, "DSInvestigationProgramArea", progAreaCd);
	    		NBSContext.store(session, "DSInvestigationCondition", observationCd);
	    		NBSContext.store(session, "DSObservationTypeCd",
	    				NEDSSConstants.MORBIDITY_CODE);
	    		//NBSContext.store(session, "DSInvestigationCode", );
	    		String processingDecision = (String)request.getParameter("markAsReviewReason");
	    		FieldMappingBuilder mapBuilder = new FieldMappingBuilder();
	    		TreeMap<Object,Object> loadTreeMap = mapBuilder.createMorbidityLoadTreeMap(
	    				morbidityProxyVO,processingDecision);
	    		NBSContext.store(session, "DSMorbMap", loadTreeMap);
	    	}else if (contextAction.equalsIgnoreCase("CreateInvestigationFromMorbidity")){

	    		CoinfectionSummaryVO conifectionSummaryVO = (CoinfectionSummaryVO)NBSContext.retrieve(session,NBSConstantUtil.DSCoinfectionInvSummVO);
	    		try {
	    			Long phcUid = null;
	    			if(conifectionSummaryVO!=null){
	    				phcUid = conifectionSummaryVO.getPublicHealthCaseUid();
	    			}

	    			createInvestigationFromMorbidityHandler(observationUid, phcUid,nbsSecurityObj,session,request, response);

	    			String successMsg = "The Morbidity Report has been successfully associated with investigation";
	    			request.setAttribute("displayInformationExists", successMsg);

	    		}catch (Exception e) {
	    			e.printStackTrace();
	    			logger.error("Error in AssociateToInvestigationsLoad in storing new relations");
	    			session.setAttribute("error", "Application Error while associating Investigations to an Observation");
	    			return mapping.findForward(NEDSSConstants.ERROR_PAGE);
	    		}

	    	}
	    	else if (contextAction.equalsIgnoreCase("ReturnToManageTreatments")) {

	    	}
	    	else if(contextAction.equalsIgnoreCase("ManageEvents"))
	    	{
	    		return mapping.findForward(contextAction);
	    	}
	    	else if (contextAction.equalsIgnoreCase(NBSConstantUtil.DELETE)) {
	    		if (sCurrTask.endsWith("Morb3")) {
	    			NBSContext.store(session, "DSFileTab", "3");
	    		}
	    		else if (sCurrTask.endsWith("Morb10")) {
	    			NBSContext.store(session, NBSConstantUtil.DSFileTab, "1");
	    		}

	    		logger.debug("observationUID in Delete is :" + observationUid);
	    		try {
	    			String result = deleteHandler(observationUid, nbsSecurityObj, session,
	    					request, response);

	    			//##!!VOTester.createReport(form.getProxy(), "obs-delete-store-post");
	    			if(result.equals("viewDelete") && sCurrTask.endsWith("Morb9"))
	    			{
	    				logger.debug("ObservationSubmit: viewDelete");

	    				ActionForward af = mapping.findForward(contextAction);
	    				ActionForward forwardNew = new ActionForward();
	    				StringBuffer strURL = new StringBuffer(af.getPath());
	    				strURL.append("?ContextAction=" + contextAction);
	    				strURL.append("&method=loadQueue");
	    				forwardNew.setPath(strURL.toString());
	    				forwardNew.setRedirect(true);
	    				return forwardNew;
	    			}
	    			else if (result.equals("viewDelete")&& !sCurrTask.endsWith("Morb9")) {
	    				logger.debug("ObservationSubmit: viewDelete");

	    				ActionForward af = mapping.findForward(contextAction);
	    				ActionForward forwardNew = new ActionForward();
	    				StringBuffer strURL = new StringBuffer(af.getPath());
	    				if(strURL.indexOf(NEDSSConstants.ManageEvents)>0){
	    					strURL.append("&ContextAction=" + contextAction);
	    				}
	    				else
	    					strURL.append("?ContextAction=" + contextAction);
	    				forwardNew.setPath(strURL.toString());
	    				forwardNew.setRedirect(true);
	    				return forwardNew;

	    			}
	    			else if (result.equals("deleteDenied")) {
	    				logger.debug("ObservationSubmit: deleteDenied");
	    				NBSContext.store(session, "DSObservationUID", observationUid);

	    				ActionForward af = mapping.findForward("DeleteDenied");
	    				ActionForward forwardNew = new ActionForward();
	    				StringBuffer strURL = new StringBuffer(af.getPath());
	    				strURL.append("?ContextAction=DeleteDenied");
	    				forwardNew.setPath(strURL.toString());
	    				forwardNew.setRedirect(true);
	    				return forwardNew;
	    			}
	    		}
	    		catch (Exception e) {
	    			logger.fatal("Error being raised ", e);
	    			//return mapping.findForward("dataerror");
	    		}
	    	}*/
	    	//if (!contextAction.equalsIgnoreCase("ViewEventsPopup")) {
	    	//	morbidityForm.reset();
	    	//}

	    }catch (Exception e) {
	    	logger.error("Exception in View Morb: " + e.getMessage());
	    	e.printStackTrace();
	    	throw new ServletException("Error occurred in View Morbidity : "+e.getMessage());
	    }
	    //return (mapping.findForward(contextAction));
}
		
		private String markAsReviewedMorbHandler(Long observationUid, String processingDecision,
				String markAsReviewedComment, NBSSecurityObj nbsSecurityObj, HttpSession session,
				HttpServletRequest request, HttpServletResponse response)
				throws NEDSSAppConcurrentDataException, java.rmi.RemoteException,
	    javax.ejb.EJBException, Exception {

	  String markAsReviewedFlag = "";
	  /**
	   * Call the mainsessioncommand
	   */
	  MainSessionCommand msCommand = null;
	  ObservationUtil obsUtil = new ObservationUtil();
	  
	  String sBeanJndiName = JNDINames.OBSERVATION_PROXY_EJB;
	  String sMethod="processObservation";
	  Object[] oParams = new Object[]{observationUid};
	  if(processingDecision!=null && !processingDecision.trim().equals("")){
	   sMethod = "processObservationWithProcessingDecision";
	   //Map<String, Object> observationMap = obsUtil.createProcessingDecisionObservation(observationUid,"Morb",processingDecision,request);
	   oParams = new Object[]{
	           observationUid, processingDecision, markAsReviewedComment};
	  }
			logger.debug("ObservationSubmit: markAsReviewedHandler with observationID = " + observationUid);

	  /**
	   * Output ObservationProxyVO for debugging
	   */
	  MainSessionHolder holder = new MainSessionHolder();
	  msCommand = holder.getMainSessionCommand(session);
	  List<?> resultUIDArr = new ArrayList<Object> ();
			resultUIDArr = msCommand
					.processRequest(sBeanJndiName, sMethod, oParams);

	  boolean result = false;
	  if ( (resultUIDArr != null) && (resultUIDArr.size() > 0)) {
				logger.debug("Marked as Reviewed result and arg = "
						+ resultUIDArr.get(0));
	    result = ( (Boolean) resultUIDArr.get(0)).booleanValue();
	    logger.debug("The Marked AS Reviewed result is:" + result);
	    if (result) {
	      markAsReviewedFlag = NEDSSConstants.RECORD_STATUS_PROCESSED;
				} else {
	      markAsReviewedFlag = NEDSSConstants.RECORD_STATUS_UNPROCESSED;
	    }
	  }
	  return markAsReviewedFlag;
	}
	/**
	 * convertFromStringToArray: returns an ArrayList<String> with the UIDs parsed from the checkBoxIds string
	 * @param checkBoxIds
	 * @return
	 */

	public ArrayList<String> convertFromStringToArray(String checkBoxIds){
		
		ArrayList<String> UIDarray = new ArrayList<String>();
		if(checkBoxIds!=null){
			StringTokenizer st = new StringTokenizer(checkBoxIds,"|");
			
			while (st.hasMoreTokens()) { 
				String token = st.nextToken();
				UIDarray.add((token.toString()));
			}
		}
		return UIDarray;
	}
   
	public void markAsReviewCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            long documentUid, String processingDecision, String markAsReviewedComment)
     throws IOException, ServletException{
		try{
		NBSDocumentActionUtil util = new NBSDocumentActionUtil();
		//NbsDocumentForm docForm = (NbsDocumentForm)form;
		HttpSession session = request.getSession();
		logger.info("markAsReviewed() in ViewDocumentAction is being called");
		//Long documentUid = null;
	//	documentUid = (Long)NBSContext.retrieve(session, "DSDocumentUID");
		//if(documentUid == null )  
		//	documentUid = new Long((String)request.getAttribute("docUid"));
		//get the document from the NBSDocument table

		NBSDocumentVO nbsDocVO = new NBSDocumentVO();

		try{
			nbsDocVO = util.getNBSDocument(session,documentUid);
		}catch(Exception e){
			logger.error("Error in markAsReviewedLoad1: "+e.getMessage());
			e.printStackTrace();
			throw new ServletException(e.toString());
		}
		nbsDocVO.getNbsDocumentDT().setRecordStatusCd(NEDSSConstants.OBS_PROCESSED);
		Date dat = new Date();
		Timestamp aupdateTime = new Timestamp(dat.getTime());
		nbsDocVO.getNbsDocumentDT().setRecordStatusTime(aupdateTime);
		nbsDocVO.getNbsDocumentDT().setLastChgTime(aupdateTime);
		nbsDocVO.getNbsDocumentDT().setProcessingDecisionCd(processingDecision);
		if(!markAsReviewedComment.isEmpty())
			nbsDocVO.getNbsDocumentDT().setProcessingDecisiontxt(markAsReviewedComment);
		String DSDocumentLocalID = nbsDocVO.getNbsDocumentDT().getLocalId();
		String firstNm = nbsDocVO.getPatientVO().getThePersonDT().getFirstNm() == null ? "" : nbsDocVO.getPatientVO().getThePersonDT().getFirstNm();
		String	lastNm = nbsDocVO.getPatientVO().getThePersonDT().getLastNm() == null ? "" : nbsDocVO.getPatientVO().getThePersonDT().getLastNm();

		nbsDocVO = util.setNBSDocumentVOforUpdate(nbsDocVO);
		try{
			util.setDocumentforUpdate(session,nbsDocVO);
		}catch(Exception e){
			logger.error("Error in markAsReviewedLoad2: "+e.getMessage());
			e.printStackTrace();
			throw new ServletException(e.toString());
		}
		StringBuffer sb = new StringBuffer();
		if(DSDocumentLocalID != null)
		{
			sb.append("Case Report <b>").append(DSDocumentLocalID).append("</b> for <b>").append(firstNm).append("</b>  <b>").append(lastNm).append("</b> has been successfully Marked As Reviewed.");
			request.setAttribute("docMarkReviewConfMsg", sb.toString());
		}
		request.setAttribute("docUid",documentUid+"");
	} catch (Exception ex) {
		logger.error("Error in markAsReviewedLoad: "+ex.getMessage());
		ex.printStackTrace();
		throw new ServletException("Error while markAsReviewedLoad : "+ex.getMessage());
	}
//	return mapping.findForward("cdaview");
}
	
}
