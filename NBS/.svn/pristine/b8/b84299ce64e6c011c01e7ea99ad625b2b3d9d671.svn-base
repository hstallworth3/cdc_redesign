
package gov.cdc.nedss.webapp.nbs.action.person;

import gov.cdc.nedss.systemservice.nbscontext.*;
import gov.cdc.nedss.systemservice.nbssecurity.*;
import gov.cdc.nedss.util.*;
import gov.cdc.nedss.webapp.nbs.action.util.ErrorMessageHelper;
import gov.cdc.nedss.webapp.nbs.action.util.PaginationUtil;
import gov.cdc.nedss.webapp.nbs.form.person.*;
import gov.cdc.nedss.webapp.nbs.action.person.util.PersonUtil;
import gov.cdc.nedss.entity.person.vo.*;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;

import java.io.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

/**
 * This class is for preparing the Person Search Results page
 * for display of search results.
 */
public class PersonSearchResultsLoad
    extends Action {

	static final LogUtils logger = new LogUtils(PersonSearchResultsLoad.class.getName());
   /**
    * This is the constructor for the PersonSearchResultsLoad
    * class
    */
   public PersonSearchResultsLoad() {
   }

   /**
    * This method is controls the execution of the
    * PersonSearchResultsLoad logic, and dictates
    * the navigation.
    *
    * @param mapping ActionMapping
    * @param form ActionForm
    * @param request HttpServletRequest
    * @param response HttpServletResponse
    * @exception IOException
    * @exception ServletException
    * @return ActionForward
    */
   public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws
       IOException, ServletException {

	  
      HttpSession session = request.getSession(false);
      NBSSecurityObj secObj = (NBSSecurityObj) session.getAttribute(
          "NBSSecurityObject");
      String contextAction = request.getParameter("ContextAction");
	  boolean homePageSrch =  request.getParameter("homePageSrch") == null ? false : true;
	  if(homePageSrch) {
		  request.setAttribute("ContextAction", "Submit");
		  contextAction = "Submit";			
	  }
	  
      request.setAttribute("PersonName",session.getAttribute("PersonName"));
      if (contextAction == null) {
         contextAction = (String) request.getAttribute("ContextAction");
         /***************************************************
          * SUBMIT ACTION
          */
      }
      
     // PersonSearchForm personSearchForm = (PersonSearchForm)request.getAttribute("personSearchForm");
      
      PersonSearchForm personSearchForm =(PersonSearchForm) form;
     
      if (contextAction.equalsIgnoreCase("Submit") ||
          contextAction.equalsIgnoreCase("Next") ||
          contextAction.equalsIgnoreCase("Prev") ||
          contextAction.equalsIgnoreCase("ReturnToSearchResults") ||
          contextAction.equalsIgnoreCase("Cancel")) {

         TreeMap<Object,Object> tm = NBSContext.getPageContext(session, "PS090", contextAction);
         ErrorMessageHelper.setErrMsgToRequest(request, "PS090");
         String sCurrTask = NBSContext.getCurrentTask(session);
         NBSContext.lookInsideTreeMap(tm);

         // add button security
         boolean bAddButton = secObj.getPermission(NBSBOLookup.PATIENT,
             NBSOperationLookup.ADD);
         request.setAttribute("addButton", String.valueOf(bAddButton));

         if (secObj.getPermission(NBSBOLookup.PATIENT, NBSOperationLookup.ADD)) {
            request.setAttribute("addButtonHref",
                                 "/nbs/" + sCurrTask + ".do?ContextAction=" +
                                 tm.get("Add"));
         }
        boolean permissionLab = secObj.getPermission(NBSBOLookup.OBSERVATIONLABREPORT,NBSOperationLookup.VIEW);
 		boolean permissionMorb = secObj.getPermission(NBSBOLookup.OBSERVATIONMORBIDITYREPORT,NBSOperationLookup.VIEW);
 		boolean permissionCase = secObj.getPermission(NBSBOLookup.DOCUMENT,NBSOperationLookup.VIEW);
 		boolean permissionInvestigation = secObj.getPermission(NBSBOLookup.INVESTIGATION,NBSOperationLookup.VIEW);
 		
 		personSearchForm.getAttributeMap().put("permissionLab",permissionLab);
 		personSearchForm.getAttributeMap().put("permissionMorb",permissionMorb);
 		personSearchForm.getAttributeMap().put("permissionCase",permissionCase);
 		personSearchForm.getAttributeMap().put("permissionInvestigation",permissionInvestigation);
 		
         request.setAttribute("refineSearchHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("RefineSearch"));
         request.setAttribute("newSearchHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("NewSearch"));
         request.setAttribute("viewHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("View"));

         request.setAttribute("viewFileHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("ViewFile"));
         
         request.setAttribute("addPatHref",
                 "/nbs/" + sCurrTask + ".do?ContextAction=Add");

         // View File Link permission
         if (! (secObj.getPermission(NBSBOLookup.PATIENT,
                                     NBSOperationLookup.VIEWWORKUP))) {
            request.setAttribute("viewFileHref", "");

         }

         request.setAttribute("viewHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("View"));
         request.setAttribute("nextHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("Next"));
         request.setAttribute("prevHref",
                              "/nbs/" + sCurrTask + ".do?ContextAction=" +
                              tm.get("Prev"));

         // retrieve the from index if there is one
         try {
            request.setAttribute("DSFromIndex",
                                 NBSContext.retrieve(session, "DSFromIndex"));

         }
         catch (Exception e) {
         }

         if(!homePageSrch) 
        	 request.setAttribute("DSSearchResults", NBSContext.retrieve(session, "DSSearchResults"));
         else {        	 
        	 request.setAttribute("DSSearchResults", session.getAttribute("DSSearchResults"));        	 
         }

      }
      /*********************************************************
       * ENTITY SEARCH
       */
      else if (contextAction.equalsIgnoreCase("EntitySearch")) {

         request.setAttribute("refineSearchHref",
             "/nbs/LoadFindPatient3.do?ContextAction=EntitySearch");
         request.setAttribute("newSearchHref",
             "/nbs/LoadFindPatient3.do?ContextAction=EntitySearch&mode=new");
         request.setAttribute("nextHref",
             "/nbs/FindPatient3.do?ContextAction=EntitySearch&mode=Next");
         request.setAttribute("prevHref",
             "/nbs/FindPatient3.do?ContextAction=EntitySearch&mode=Next");

         request.setAttribute("DSSearchResults",
                              NBSContext.retrieve(session, "DSSearchResults"));        
      }
      String scString ="";
      try {
    	  PatientSearchVO psVO = null;
          if(!homePageSrch) 
        	  psVO = (PatientSearchVO) NBSContext.retrieve(session,"DSSearchCriteria");
          else {
        	  psVO = (PatientSearchVO) session.getAttribute("DSSearchCriteria");        	 
          }    	  
         scString = this.buildSearchCriteriaString(psVO);
         request.setAttribute("DSSearchCriteriaString", scString);
      }
      catch (Exception e) {
    	  logger.warn("PersonSearchResults search criteria missing");
         session.setAttribute("error",
                              "DSSearchCriteria not available in Object Store");
         e.printStackTrace();
      }
      if(("ReturnToSearchResults").equalsIgnoreCase(contextAction) || ("Cancel").equalsIgnoreCase(contextAction)){
  		 ArrayList list = new ArrayList();
  		 if(session.getAttribute("personList") != null)
  			 list = (ArrayList)session.getAttribute("personList");
  		 request.setAttribute("personList", list);
  		 request.setAttribute("queueCount", list.size());
  		 personSearchForm.getAttributeMap().put("queueCount", list.size());
  		 personSearchForm.clearSelections(); 
  	 }
      if(contextAction.equalsIgnoreCase("EntitySearch"))
    	  return mapping.findForward("XSP");
      else{
    	  
    	  personSearchForm.setAttributeMap((Map)session.getAttribute("attributeMap"));
    	  personSearchForm.getAttributeMap().put("DSSearchCriteriaString", scString);
    	//  personSearchForm.getPatientVoCollection();
    	 // personSearchForm.initializeDropDowns();
			
    		//Put the pagination, Because return from different form.. form will lose pagination AttributeMap. 
    		// due to this reason we put into session and take it back in pagination.
    		 String pageParam = new ParamEncoder("searchResultsTable").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
    		 if(pageParam != null && request.getParameter(pageParam) != null) {
    			String pageNo = request.getParameter(pageParam);
    			personSearchForm.getAttributeMap().put("PageNumber", pageNo);
    			
    		 }else{
    			 if(session.getAttribute("personPageNo")!=null){
    				 String pageNumber ="1";
    				 pageNumber = (String)session.getAttribute("personPageNo");
    				 session.removeAttribute("personPageNo");
    				 personSearchForm.getAttributeMap().put("PageNumber", pageNumber); 
    			 }    			
    		 }

    	  return PaginationUtil.personPaginate(personSearchForm, request, "searchResultLoad",mapping);
      }
      
   }

   
   
   private String buildSearchCriteriaString(PersonSearchVO psVO) {

      //  build the criteria string
      StringBuffer sQuery = new StringBuffer("");
      CachedDropDownValues cache = new CachedDropDownValues();

      if (psVO.getLastName() != null && !psVO.getLastName().equals("")) {
         sQuery.append("Last Name").append(" " +
             cache.getDescForCode("SEARCH_SNDX", psVO.getLastNameOperator()) +
             " ").append("'" + psVO.getLastName() + "'").append(", ");

      }
      if (psVO.getFirstName() != null && !psVO.getFirstName().equals("")) {
         sQuery.append("First Name").append(" " +
             cache.getDescForCode("SEARCH_SNDX", psVO.getFirstNameOperator()) +
             " ").append("'" + psVO.getFirstName() + "'").append(", ");

      }
      if ( (psVO.getBirthTimeMonth() != null &&
            !psVO.getBirthTimeMonth().equals("")) ||
          (psVO.getBirthTimeDay() != null && !psVO.getBirthTimeDay().equals("")) ||
          (psVO.getBirthTimeYear() != null && !psVO.getBirthTimeYear().equals("")))
      {
        sQuery.append("DOB").append(" " +
                                    cache.getDescForCode("SEARCH_NUM",
            psVO.getBirthTimeOperator()) +
                                    " ").append("'" +
                                                PersonUtil.getBirthDate(psVO.getBirthTimeMonth(),
            psVO.getBirthTimeDay(), psVO.getBirthTimeYear()) + "'").append(", ");

      }
      else if ( (psVO.getAfterBirthTime() != null &&
                 psVO.getAfterBirthTime().trim().length() != 0) ||
               (psVO.getBeforeBirthTime() != null &&
                psVO.getBeforeBirthTime().trim().length() != 0))
      {
        sQuery.append("DOB").append(" Between ").append("'" +
            psVO.getBeforeBirthTime() + "' and '").append(
            psVO.getAfterBirthTime() + "'").append(", ");

      }
      if (psVO.getCurrentSex() != null && !psVO.getCurrentSex().equals("")) {
         sQuery.append("Current Sex Equal ").append("'" +
             cache.getDescForCode("SEX", psVO.getCurrentSex()) +
             "'").append(", ");

      }
      if (psVO.getStreetAddr1() != null && !psVO.getStreetAddr1().equals("")) {
         sQuery.append("Street Address").append(" " +
             cache.getDescForCode("SEARCH_SNDX", psVO.getStreetAddr1Operator()) +
             " ").append("'" + psVO.getStreetAddr1() + "'").append(", ");

      }
      if (psVO.getCityDescTxt() != null && !psVO.getCityDescTxt().equals("")) {
         sQuery.append("City").append(" " +
             cache.getDescForCode("SEARCH_SNDX", psVO.getCityDescTxtOperator()) +
             " ").append("'" + psVO.getCityDescTxt() + "'").append(", ");

      }
      if (psVO.getState() != null && !psVO.getState().equals("")) {
         sQuery.append("State Equal ").append("'" +
             getStateDescTxt(psVO.getState()) + "'").append(", ");

      }
      if (psVO.getZipCd() != null && !psVO.getZipCd().equals("")) {
         sQuery.append("Zip").append(" Equal ").append("'" + psVO.getZipCd() +
             "' ").append(",");

      }
      String codeDesc = cache.getDescForCode("SEARCH_ALPHA", psVO.getPatientIDOperator());
      if (codeDesc == null || codeDesc.equals("")) {
    	  codeDesc = "Equal";
      }
      if (psVO.getOldLocalID() != null && !psVO.getOldLocalID().equals("")) {
        sQuery.append("Patient ID ").append(" " +
            codeDesc +
            " ").append("'" + psVO.getOldLocalID() +"'").append(", ");
      }
      if (psVO.getRootExtensionTxt() != null &&
          !psVO.getRootExtensionTxt().equals("")) {

         if (psVO.getTypeCd() != null && !psVO.getTypeCd().equals("")) {
            sQuery.append(cache.getDescForCode("EI_TYPE_PAT", psVO.getTypeCd())).
                append(" Equal ").append("'" + psVO.getRootExtensionTxt() + "'").
                append(", ");
         }
      }

      if (psVO.getSsn() != null && !psVO.getSsn().trim().equals("")) {
         sQuery.append("SSN").append(" Equal ").append("'" + psVO.getSsn() +
             "' ").append(",");

      }

      if (psVO.getPhoneNbrTxt() != null && !psVO.getPhoneNbrTxt().equals("")) {
         sQuery.append("Telephone").append(" Contains ").append("'" +
             psVO.getPhoneNbrTxt() + "' ").append(",");

      }
      String findRace = getRaceDescTxt(psVO.getRaceCd(),
                                       cache.getRaceCodes("ROOT"));

      if (psVO.getRaceCd() != null && !psVO.getRaceCd().equals("")) {
         sQuery.append("Race Equal ").append("'" +
             cache.getDescForCode("ROOT", psVO.getRaceCd()) + "'").append(", ");

      }
      if (psVO.getEthnicGroupInd() != null &&
          !psVO.getEthnicGroupInd().equals("")) {
         sQuery.append("Ethnicity Equal ").append("'" +
             cache.getDescForCode("P_ETHN_GRP", psVO.getEthnicGroupInd()) + "'").
             append(", ");

      }
      String findRole = getRoleDescTxt(psVO.getRole(),
                                       cache.getCodedValues("RL_TYPE_PAT"));

      if (psVO.getRole() != null && !psVO.getRole().equals("")) {
         sQuery.append("Role Equal ").append("'" +
             cache.getDescForCode("RL_TYPE_PAT", psVO.getRole()) + "'").append(", ");
      }
  	if (psVO.getReportType() != null && psVO.getReportType().trim().length() != 0) {
  		
  		if(psVO.getReportType().equals(PersonSearchVO.LAB_REPORT)){
        	
  			sQuery.append("Event Type Equals ").append("'Lab report'").append(", ");
          	
          } else if(psVO.getReportType().equals(PersonSearchVO.MORBIDITY_REPORT)){
          	
        	  sQuery.append("Event Type Equals ").append("'Morbidity Report'").append(", ");
          	
          } else if(psVO.getReportType().equals(PersonSearchVO.CASE_REPORT)){
          	
        	  sQuery.append("Event Type Equals ").append("'Case Report'").append(", ");
          	
          } else if(psVO.getReportType().equals(PersonSearchVO.INVESTIGATION_REPORT)){
          	
        	  sQuery.append("Event Type Equals ").append("'Investigation'").append(", ");
          	
          }
  		
  	  if (psVO.getConditionSelected() != null && !psVO.getConditionSelected().equals("")){
  		 sQuery.append("Condition Equals ").append("'" + cache.getConditionDesc(psVO.getConditionSelected()) + "'").append(", ");
  	  }
  	  if (psVO.getProgramArea() != null && !psVO.getProgramArea().equals("")){
   		 sQuery.append("Program Area Equals ").append("'" + psVO.getProgramArea() + "'").append(", ");
   	  }
  	 if (psVO.getJurisdictionSelected() != null && !psVO.getJurisdictionSelected().equals("")){
   		 sQuery.append("Jurisdiction Equals ").append("'" + cache.getJurisdictionDesc(psVO.getJurisdictionSelected()) + "'").append(", ");
   	  }
  	 if (psVO.getPregnantSelected() != null && !psVO.getPregnantSelected().equals("")){
  		 sQuery.append("Pregnancy Status Equals ").append("'" + cache.getDescForCode("YNU", psVO.getPregnantSelected()) + "'").append(", ");
  	  }
  	
  	
  /*	 
  	if (psVO.getDateType() != null && !psVO.getDateType().equals("")){
 		 sQuery.append("Document Updated By User Equal ").append("'" + psVO.getDocumentUpdateFullNameSelected() + "'").append(", ");
 	  }
 	 */
  	 if(psVO.getActType() != null && psVO.getActType().trim().length() != 0 
   		  && psVO.getDocOperator() != null && psVO.getDocOperator().trim().length() != 0
   		  && psVO.getActId() != null && psVO.getActId().trim().length() != 0
   		  ){
   	  String codeDescDoc = cache.getDescForCode("PHVS_EVN_SEARCH_ABC", psVO.getActType());
   	  sQuery.append(codeDescDoc);
   	  
   	  if(psVO.getDocOperator().equals("=")){
   		  sQuery.append(" equals ").append("'" + psVO.getActId() + "'").append(", ");
   	  }
   	  else if(psVO.getDocOperator().equals("CT")){
   		sQuery.append(" contains ").append("'" + psVO.getActId() + "'").append(", ");
   	  }
   	  else if(psVO.getDocOperator().equals("!=")){
   		sQuery.append(" not equals ").append("'" + psVO.getActId() + "'").append(", ");
   	  }
     }
  	 /*
      if(psVO.getActType() != null && psVO.getActType().trim().length() != 0 &&
      		psVO.getActId() != null && psVO.getActId().trim().length() != 0){
      	
        if(psVO.getActType().equals(PersonSearchVO.INVESTIGATION_LOCAL_ID)){
        	
            sQuery.append("Investigation ID Equals ").append(
                    "'" + psVO.getActId() + "'").append(", ");
          	
          } else if(psVO.getActType().equals(PersonSearchVO.CITY_COUNTY_CASE_ID)){
          	
            sQuery.append("City/County Case ID Equal ").append(
                    "'" + psVO.getActId() + "'").append(", ");
            
          }else if(psVO.getActType().equals(PersonSearchVO.ABC_STATE_CASE_ID)){
          	
            sQuery.append("ABC Case ID Equals ").append(
                    "'" + psVO.getActId() + "'").append(", ");
          	
          } else if(psVO.getActType().equals(PersonSearchVO.STATE_CASE_ID)){
          	
            sQuery.append("State Case ID Equals ").append(
                    "'" + psVO.getActId() + "'").append(", ");
          	
          } else if(psVO.getActType().equals(PersonSearchVO.LAB_REPORT_LOCAL_ID)){
          	
            sQuery.append("Lab ID Equals ").append(
                    "'" + psVO.getActId() + "'").append(", ");
          	
          } else if(psVO.getActType().equals(PersonSearchVO.MORBIDITY_REPORT_LOCAL_ID)){
          	
            sQuery.append("Morbidity Report ID Equals ").append(
                    "'" + psVO.getActId() + "'").append(", ");
          	
          } else if(psVO.getActType().equals(PersonSearchVO.VACCINATION_LOCAL_ID)){
          	
            sQuery.append("Vaccination ID Equal ").append(
                    "'" + psVO.getActId() + "'").append(", ");
            
          } else if(psVO.getActType().equals(PersonSearchVO.NOTIFICATION_ID)){
            	
              sQuery.append("Notification ID Equals ").append(
                      "'" + psVO.getActId() + "'").append(", ");
          	
          } else if(psVO.getActType().equals(PersonSearchVO.TREATMENT_LOCAL_ID)){
          	
            sQuery.append("Treatment ID Equals ").append(
                    "'" + psVO.getActId() + "'").append(", ");
          	
          }
          //changes made for event search based on accession number
          else if(psVO.getActType().equals(PersonSearchVO.Accession_Number_LOCAL_ID)){
          	
              sQuery.append("Accession Number Equals ").append(
                      "'" + psVO.getActId() + "'").append(", ");
            	
            } 
        
        //changes made for event search based on doc number
          else if(psVO.getActType().equals(PersonSearchVO.Doc_Number_LOCAL_ID)){
          	
              sQuery.append("Doc Number Equals ").append(
                      "'" + psVO.getActId() + "'").append(", ");
            	
            } 
       
      	}*/
      if(psVO.getDateType() != null && psVO.getDateType().trim().length() != 0 
    		  && psVO.getDateOperator() != null && psVO.getDateOperator().trim().length() != 0
    		  && psVO.getDateFrom() != null && psVO.getDateFrom().trim().length() != 0
    		  ){
    	  String codeDescDate = cache.getDescForCode("NBS_EVENT_SEARCH_DATES", psVO.getDateType());
    	  sQuery.append(codeDescDate);
    	  
    	  if(psVO.getDateOperator().equals("=")){
    		  sQuery.append(" equals ").append("'" + psVO.getDateFrom() + "'").append(", ");
    	  }
    	  else if(psVO.getDateOperator().equals("BET") && psVO.getDateTo() != null && psVO.getDateTo().trim().length() != 0){
    		  sQuery.append(" between ").append("'" + psVO.getDateFrom() + "'").append(" and ").append("'" + psVO.getDateTo() + "'").append(", ");
    	  }
    	  
      }
      //Electronic and Manual check-box
      if (psVO.getElectronicValueSelected().trim().equalsIgnoreCase("true") && psVO.getManualValueSelected().trim().equalsIgnoreCase("false")){
 		 sQuery.append("Entry Method equals ").append("'Electronic'").append(", ");
 	  }
   else if (psVO.getElectronicValueSelected().trim().equalsIgnoreCase("false") && psVO.getManualValueSelected().trim().equalsIgnoreCase("true")){
		 sQuery.append("Entry Method equals ").append("'Manual'").append(", ");
	  }
      
    //Entered by check-box
      if (psVO.getInternalValueSelected().trim().equalsIgnoreCase("true") && psVO.getExternalValueSelected().trim().equalsIgnoreCase("false")){
 		 sQuery.append("Entered By equals ").append("'Internal User'").append(", ");
 	  }
   else if (psVO.getInternalValueSelected().trim().equalsIgnoreCase("false") && psVO.getExternalValueSelected().trim().equalsIgnoreCase("true")){
		 sQuery.append("Entered By equals ").append("'External User'").append(", ");
	  }
      
      //Event Status new/initial or update
      if (psVO.getEventStatusInitialSelected().trim().equalsIgnoreCase("true") && psVO.getEventStatusUpdateSelected().trim().equalsIgnoreCase("false")){
    		 sQuery.append("Document State equals ").append("'New/Initial'").append(", ");
    	  }
      else if (psVO.getEventStatusInitialSelected().trim().equalsIgnoreCase("false") && psVO.getEventStatusUpdateSelected().trim().equalsIgnoreCase("true")){
 		 sQuery.append("Document State equals ").append("'Update'").append(", ");
 	  }
      
      if (psVO.getDocumentCreateSelected() != null && !psVO.getDocumentCreateSelected().equals("")){
   		 sQuery.append("Event Created By User equals ").append("'" + psVO.getDocumentCreateFullNameSelected() + "'").append(", ");
   	  }
   	 if (psVO.getDocumentUpdateSelected() != null && !psVO.getDocumentUpdateSelected().equals("")){
   		 sQuery.append("Event Last Updated By User equals ").append("'" + psVO.getDocumentUpdateFullNameSelected() + "'").append(", ");
   	  }
   	 
   	if(psVO.getProviderFacilitySelected() != null && psVO.getProviderFacilitySelected().trim().length() != 0){
   	 String codeDescPF = cache.getDescForCode("NBS_REP_ORD_TYPE", psVO.getProviderFacilitySelected());
   	sQuery.append(codeDescPF).append(" equals ");
   	 if(psVO.getProviderFacilitySelected().equals(PersonSearchVO.ORDERING_FACILITY)){
   	   String desc = psVO.getOrderingFacilityDescSelected();
   		
   		String[] dArray = desc.split("<br>");
		
		if(dArray.length>0)
			sQuery.append("'" + dArray[0] + "'").append(", ");
          } 
     else if(psVO.getProviderFacilitySelected().equals(PersonSearchVO.ORDERING_PROVIDER)){
    	 String desc = psVO.getOrderingProviderDescSelected();
    		
    		String[] dArray = desc.split("<br>");
 		
 		if(dArray.length>0)
 			sQuery.append("'" + dArray[0]+ "'").append(", ");
           } 
           
     else if(psVO.getProviderFacilitySelected().equals(PersonSearchVO.REPORTING_FACILITY)){
    	 String desc = psVO.getReportingFacilityDescSelected();
    		
    		String[] dArray = desc.split("<br>");
 		
 		if(dArray.length>0)
 			sQuery.append("'" + dArray[0]+ "'").append(", ");
           } 
      
     else if(psVO.getProviderFacilitySelected().equals(PersonSearchVO.REPORTING_PROVIDER)){
    	 String desc = psVO.getReportingProviderDescSelected();
    		
    		String[] dArray = desc.split("<br>");
 		
 		if(dArray.length>0)
 			sQuery.append("'" + dArray[0] + "'").append(", ");
           } 
   	}
 	if(psVO.getInvestigatorSelected() != null && psVO.getInvestigatorDescSelected() != null && psVO.getInvestigatorDescSelected().trim().length() != 0){
 		 String desc = psVO.getInvestigatorDescSelected();


 		String[] dArray = desc.split("<br>");
		
		if(dArray.length>0)
			sQuery.append("Investigator equals ").append("'" + dArray[0] + "'").append(", ");
 	}
 	if(psVO.getInvestigationStatusSelected() != null && psVO.getInvestigationStatusSelected().trim().length() != 0){
 		//PHC_IN_STS
 		sQuery.append("Investigation Status equals ").append("'" +	cache.getDescForCode("PHC_IN_STS", psVO.getInvestigationStatusSelected())+ "'").append(", ");
 	}
 	if(psVO.getOutbreakNameSelected() != null && psVO.getOutbreakNameSelected().trim().length() != 0){
 		//PHC_IN_STS
 		sQuery.append("Outbreak Name equals ").append("'" +	cache.getDescForCode("OUTBREAK_NM", psVO.getOutbreakNameSelected())+ "'").append(", ");
 	}
 	
 	if(psVO.getCaseStatusListValuesSelected() != null && psVO.getCaseStatusListValuesSelected().trim().length() != 0){
 		//PHC_IN_STS
 		String caseStatus = psVO.getCaseStatusListValuesSelected().trim();
 		if(psVO.getCaseStatusListValuesSelected().contains("ed Values:")){
 			caseStatus = caseStatus.replace("ed Values:", "");
 		}
		if(psVO.getCaseStatusListValuesSelected().contains("UNASSIGNED") && 
				!((psVO.getCaseStatusCodedValuesSelected() != null)
						&& (psVO.getCaseStatusCodedValuesSelected().trim().length() != 0)
						)){
			caseStatus = caseStatus.replace(", UNASSIGNED", "Unassigned");
			sQuery.append("Case Status equals ").append("'" +	caseStatus.trim() + "'").append(", ");
		}
			
		else
			if(psVO.getCaseStatusListValuesSelected().contains("UNASSIGNED"))
			{
				caseStatus = caseStatus.replace(", UNASSIGNED", ", Unassigned");
				String[] cArray = caseStatus.split(",");
				String prefix = "";
				sQuery.append("Case Status in ").append("(");
				for(int i = 0; i< (cArray.length);i++){
					sQuery.append(prefix);
					  prefix = ", ";
					sQuery.append("'" +	cArray[i].trim() + "'");
				}
				sQuery.append(")").append(", ");
			}
			else if(caseStatus.length() != 0)
			{
				String[] cArray = caseStatus.split(",");
				String prefix = "";
				sQuery.append("Case Status in ").append("(");
				for(int i = 0; i< (cArray.length);i++){
					sQuery.append(prefix);
					  prefix = ", ";
					sQuery.append("'" +	cArray[i].trim() + "'");
				}
				sQuery.append(")").append(", ");
			}
 		
 	}
 	
 	if(psVO.getCurrentProcessStateValuesSelected() != null && psVO.getCurrentProcessStateValuesSelected().trim().length() != 0){
 		//PHC_IN_STS
 		String currentStatus = psVO.getCurrentProcessStateValuesSelected().trim();
 		if(psVO.getCurrentProcessStateValuesSelected().contains("ed Values:")){
 			currentStatus = currentStatus.replace("ed Values:", "");
 		}
		if(psVO.getCurrentProcessStateValuesSelected().contains("UNASSIGNED") && 
				!((psVO.getCurrentProcessCodedValuesSelected() != null)
						&& (psVO.getCurrentProcessCodedValuesSelected().trim().length() != 0)
						)){
			currentStatus = currentStatus.replace(", UNASSIGNED", "Unassigned");
			sQuery.append("Current Processing Status equals ").append("'" +	currentStatus.trim() + "'").append(", ");
		}
			
		else
			if(psVO.getCurrentProcessStateValuesSelected().contains("UNASSIGNED"))
			{
				currentStatus = currentStatus.replace(", UNASSIGNED", ", Unassigned");
				String[] cArray = currentStatus.split(",");
				String prefix = "";
				sQuery.append("Current Processing Status in ").append("(");
				for(int i = 0; i< (cArray.length);i++){
					sQuery.append(prefix);
					  prefix = ", ";
					sQuery.append("'" +	cArray[i].trim() + "'");
				}
				sQuery.append(")").append(", ");
			}
			else if(currentStatus.length() != 0)
			{
				String[] cArray = currentStatus.split(",");
				String prefix = "";
				sQuery.append("Current Processing Status in ").append("(");
				for(int i = 0; i< (cArray.length);i++){
					sQuery.append(prefix);
					  prefix = ", ";
					sQuery.append("'" +	cArray[i].trim() + "'");
				}
				sQuery.append(")").append(", ");
			}
 		
 	}
 	if(psVO.getNotificationValuesSelected() != null && psVO.getNotificationValuesSelected().trim().length() != 0){
 		//PHC_IN_STS
 		String NotiStatus = psVO.getNotificationValuesSelected().trim();
 		if(psVO.getNotificationValuesSelected().contains("ed Values:")){
 			NotiStatus = NotiStatus.replace("ed Values:", "");
 		}
		if(psVO.getNotificationValuesSelected().contains("UNASSIGNED") && 
				!((psVO.getNotificationCodedValuesSelected() != null)
						&& (psVO.getNotificationCodedValuesSelected().trim().length() != 0)
						)){
			NotiStatus = NotiStatus.replace(", UNASSIGNED", "Unassigned");
			sQuery.append("Notification Status equals ").append("'" +	NotiStatus.trim() + "'").append(", ");
		}
			
		else
			if(psVO.getNotificationValuesSelected().contains("UNASSIGNED"))
			{
				NotiStatus = NotiStatus.replace(", UNASSIGNED", ", Unassigned");
				String[] cArray = NotiStatus.split(",");
				String prefix = "";
				sQuery.append("Notification Status in ").append("(");
				for(int i = 0; i< (cArray.length);i++){
					sQuery.append(prefix);
					  prefix = ", ";
					sQuery.append("'" +	cArray[i].trim() + "'");
				}
				sQuery.append(")");
			}
			else if(NotiStatus.length() != 0)
			{
				String[] cArray = NotiStatus.split(",");
				String prefix = "";
				sQuery.append("Notification Status in ").append("(");
				for(int i = 0; i< (cArray.length);i++){
					sQuery.append(prefix);
					  prefix = ", ";
					sQuery.append("'" +	cArray[i].trim() + "'");
				}
				sQuery.append(")");
			}
 		
 	}
 	
 	//Resulted Test
	if (!psVO.getResultedTestDescriptionWithCodeValue().isEmpty() || !psVO.getTestDescription().isEmpty()) {
		

		String resultedTestDescriptionWithCode = psVO.getResultedTestDescriptionWithCodeValue().trim();
		String specialCharacter;

		if (resultedTestDescriptionWithCode.indexOf("'") > 0) {
			specialCharacter = "'";
			resultedTestDescriptionWithCode = replaceCharacters(resultedTestDescriptionWithCode,
					specialCharacter, "''");
		}
		
		String testDescription = psVO.getTestDescription().trim();

		if (testDescription.indexOf("'") > 0) {
			specialCharacter = "'";
			testDescription = replaceCharacters(testDescription,
					specialCharacter, "''");
		}
		String description="";
		
		if(!testDescription.isEmpty()){
			//int index = testDescription.lastIndexOf("(");
			description=testDescription.trim();
		
		}else
			description=resultedTestDescriptionWithCode;
		
		if(description != null && psVO.getResultedTestCodeDropdown() != null){
		sQuery.append("Resulted Test ");
		String codeDescDropdown = cache.getDescForCode("SEARCH_TEXT", psVO.getResultedTestCodeDropdown());
			sQuery.append(codeDescDropdown.toLowerCase());
		
		sQuery.append(" '"+description+"'").append(", ");
		}
	}
	
	//Codeed Result/Organism
		if (!psVO.getCodeResultOrganismDescriptionValue().isEmpty() || !psVO.getResultDescriptionValue().isEmpty()) {
			

			String codeResultOrganismDescriptionValue = psVO.getCodeResultOrganismDescriptionValue().trim();
			String specialCharacter;

			if (codeResultOrganismDescriptionValue.indexOf("'") > 0) {
				specialCharacter = "'";
				codeResultOrganismDescriptionValue = replaceCharacters(codeResultOrganismDescriptionValue,
						specialCharacter, "''");
			}
			
			String resultDescriptionValue = psVO.getResultDescriptionValue().trim();

			if (resultDescriptionValue.indexOf("'") > 0) {
				specialCharacter = "'";
				resultDescriptionValue = replaceCharacters(resultDescriptionValue,
						specialCharacter, "''");
			}
			String description="";
			
			if(!resultDescriptionValue.isEmpty()){
				
				description=resultDescriptionValue.trim();
			
			}else
				description=codeResultOrganismDescriptionValue;
			
			if(description != null && psVO.getCodeResultOrganismDropdown() != null){
			sQuery.append("Coded Result/Organism ");
			String codeDescDropdown = cache.getDescForCode("SEARCH_TEXT", psVO.getCodeResultOrganismDropdown());
				sQuery.append(codeDescDropdown.toLowerCase());
			
			sQuery.append(" '"+description+"'").append(", ");
			}
		}
  	}
       // System.out.println("Search String: " + sQuery.toString());
      
        return sQuery.toString();
    } //buildSearchCriteriaString

   /**
      * Formats a String by removing specified characters with another set of specified characters
      * @param   toBeRepaced  is the characters to be replaced
      * @param   specialCharacter
      * @param   replacement  are the characters to replace the characters being removed
      * @return  String with characters replaced.
      */
     private String replaceCharacters(String toBeRepaced, String specialCharacter, String replacement) {
     	int s = 0;
         int e = 0;
         StringBuffer result = new StringBuffer();
     	try{
 	      
 	
 	      while ((e = toBeRepaced.indexOf(specialCharacter, s)) >= 0) {
 	          result.append(toBeRepaced.substring(s, e));
 	          result.append(replacement);
 	          s = e+specialCharacter.length();
 	      }
 	      result.append(toBeRepaced.substring(s));
     	}catch(Exception ex){
     		logger.error("Exception ="+ex.getMessage(), ex);
     		throw new NEDSSSystemException(ex.toString(), ex);
     	}
 	      return result.toString();
     }
   private String getStateDescTxt(String sStateCd) {

      CachedDropDownValues srtValues = new CachedDropDownValues();
      TreeMap<Object,Object> treemap = srtValues.getStateCodes2("USA");
      String desc = "";

      if (sStateCd != null && treemap.get(sStateCd) != null) {
         desc = (String) treemap.get(sStateCd);

      }
      return desc;
   }

   private String getRaceDescTxt(String raceCode, String descTxt) {

      String returnStr = "";
      if (raceCode != null) {
         if (!raceCode.equals("") && descTxt != null) {
            String findRace = descTxt.substring(descTxt.indexOf(raceCode));
            String s = findRace.substring(findRace.indexOf("$") + 1);
            return s.substring(0, s.indexOf("|") + 1);
         }
      }
      return returnStr;
   }

   private String getRoleDescTxt(String role, String descTxt) {

      String returnStr = "";
      if (role != null) {
         if (!role.equals("") && descTxt != null) {
            String findRole = descTxt.substring(descTxt.indexOf(role));
            String s = findRole.substring(findRole.indexOf("$") + 1);
            return s.substring(0, s.indexOf("|") + 1);
         }
      }
      return returnStr;
   }
} //PersonSearchResultsLoad
