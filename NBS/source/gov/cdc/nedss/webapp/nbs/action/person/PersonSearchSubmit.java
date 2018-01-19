package gov.cdc.nedss.webapp.nbs.action.person;


import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.*;
import gov.cdc.nedss.systemservice.nbscontext.*;
import gov.cdc.nedss.systemservice.nbssecurity.*;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.*;
import gov.cdc.nedss.webapp.nbs.form.homepage.HomePageForm;
import gov.cdc.nedss.webapp.nbs.form.person.*;
import gov.cdc.nedss.entity.person.util.DisplayPersonList;
import gov.cdc.nedss.entity.person.vo.*;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.webapp.nbs.action.person.util.PersonUtil;
import gov.cdc.nedss.webapp.nbs.action.person.util.SearchResultPersonUtil;
import gov.cdc.nedss.webapp.nbs.action.util.PaginationUtil;

import gov.cdc.nedss.util.NEDSSConstants;
//for the old way using entity
import gov.cdc.nedss.util.PropertyUtil;

/**
 * Title:        Actions
 * Description:  Person Search Submit
 * Copyright:    Copyright (c) 2001
 * Company:      CSC
 * @author:
 * @version 1.0
 */
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;


public class PersonSearchSubmit
    extends Action
{
	private static PropertyUtil propertyUtil= PropertyUtil.getInstance();
	public PersonSearchSubmit()
    {
    }
    static final LogUtils logger = new LogUtils(PersonSearchSubmit.class.getName());
    public ActionForward execute(ActionMapping mapping, ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
			  throws IOException, ServletException
    {

    SearchResultPersonUtil srpUtil = new SearchResultPersonUtil();
	HttpSession session = request.getSession(false);
	NBSSecurityObj secObj = (NBSSecurityObj)session.getAttribute("NBSSecurityObject");

	String contextAction = request.getParameter("ContextAction");
	boolean homePageSrch =  request.getParameter("homePageSrch") == null ? false : true;
	if(homePageSrch && !"sortingByColumn".equalsIgnoreCase(contextAction))
		contextAction = "Submit";

	if (contextAction == null)
	    contextAction = (String)request.getAttribute("ContextAction");
     request.setAttribute("PersonName",session.getAttribute("PersonName"));

         //pass the VOLookup for doing VO for entity search
      request.setAttribute("VOLookup",
                                  (request.getAttribute("VOLookup") == null
                                   ? "" : (String)request.getAttribute("VOLookup")));


	/*****************************
	 * SUBMIT ACTION
	 */

        String mode = request.getParameter("mode");




      if( (contextAction.equalsIgnoreCase("Submit") || contextAction.equalsIgnoreCase("EntitySearch")) && mode == null)
      {

	    findPeople(form, session, request, homePageSrch, false);

	    // add button security
	    boolean bAddButton = secObj.getPermission(NBSBOLookup.PATIENT,
						      NBSOperationLookup.ADD);
	    request.setAttribute("addButton", String.valueOf(bAddButton));

	    String strCurrentIndex = (String)request.getParameter("currentIndex");


		request.setAttribute("DSFromIndex",strCurrentIndex);
		request.setAttribute("mode",request.getParameter("mode"));

            return mapping.findForward(contextAction);

	}
	/****************************
	 * NEXT action
	 */
	else if (contextAction.equalsIgnoreCase("Next")  && mode == null)
	{
	    String strCurrentIndex = (String)request.getParameter(
					     "currentIndex");

	    NBSContext.store(session,"DSFromIndex",strCurrentIndex);
	    return mapping.findForward(contextAction);
	}
	/*****************************************
	 * PREVIOUS ACTION
	 */
	else if (contextAction.equalsIgnoreCase("Prev"))
	{
	    String strCurrentIndex = (String)request.getParameter(
					     "currentIndex");

	    NBSContext.store(session,"DSFromIndex",strCurrentIndex);
	    return mapping.findForward(contextAction);
	}
	/******
	/****************************
	 * ADD action
	 */
	else if (contextAction.equalsIgnoreCase("Add"))
	{

	    return mapping.findForward(contextAction);
	}
	/******************************
	 * refine search action
	 */
	else if (contextAction.equalsIgnoreCase("RefineSearch"))
	{
		PersonSearchForm psForm = (PersonSearchForm)form;
		psForm.setSearchCriteriaArrayMap(new HashMap<Object,Object>());
	    return mapping.findForward(contextAction);
	}
  	/******************************
  	 * NEW SEARCH ACTION
  	 */
  	else if (contextAction.equalsIgnoreCase("NewSearch"))
  	{
  		PersonSearchForm psForm = (PersonSearchForm)form;
  		psForm.clearAll();
  	    return mapping.findForward(contextAction);
  	}
      
    /******************************
  	* VIEW INVESTIGATION
  	*/
      
  	else if (contextAction.equalsIgnoreCase("InvestigationID"))
  	{
  		String publicHealthCaseUid = (String)request.getParameter("publicHealthCaseUID");
        NBSContext.store(session, "DSInvestigationUID", publicHealthCaseUid);
  	    return mapping.findForward(contextAction);
  	}
	/*********************************
	 * VIEW ACTION
	 */
	else if (contextAction.equalsIgnoreCase("View"))
	{
            Long personUID = new Long((String)request.getParameter("uid"));
            NBSContext.store(session, "DSPatientPersonUID", personUID);
	    return mapping.findForward(contextAction);
	}
    //View File Action
    else if (contextAction.equalsIgnoreCase("ViewFile"))
    {
        Long personUID = new Long((String)request.getParameter("uid"));
        NBSContext.store(session, "DSPatientPersonUID", personUID);
        NBSContext.store(session,"DSFileTab","2");
        return mapping.findForward(contextAction);
    }//View Lab
    else if (contextAction.equalsIgnoreCase("ViewLab"))
    {
        Long observationUid = new Long((String)request.getParameter("observationUID"));
        NBSContext.store(session, "DSObservationUID", observationUid);
        //NBSContext.store(session,"DSFileTab","2");
        return mapping.findForward(contextAction);
    }
    //View Inv
    else if (contextAction.equalsIgnoreCase("ViewInv"))
    {
        Long observationUid = new Long((String)request.getParameter("publicHealthCaseUID"));
        NBSContext.store(session, "DSObservationUID", observationUid);
        //NBSContext.store(session,"DSFileTab","2");
        return mapping.findForward(contextAction);
    }

        else if("Next".equalsIgnoreCase(mode))
		{
			String strCurrentIndex = (String)request.getParameter("currentIndex");
			request.setAttribute("mode",request.getParameter("mode"));


			request.setAttribute("DSFromIndex",strCurrentIndex);
			request.setAttribute("mode",request.getParameter("mode"));


			return mapping.findForward(contextAction);
		}
	    else if("ReturnToSearchResults".equalsIgnoreCase(contextAction) || "Cancel".equalsIgnoreCase(contextAction))
	    {
	    	PersonSearchForm psForm = (PersonSearchForm)form;
	    	psForm.clearSelections();
	    	return mapping.findForward("Submit");
	    }
	    else if("filterPatientSubmit".equalsIgnoreCase(contextAction)){
	    	PersonSearchForm psForm = (PersonSearchForm)form;
	    	srpUtil.showButton(request,(String) psForm.getAttributeMap().get("DSSearchCriteriaString"));
	    	return srpUtil.filterPatientSubmit( mapping,  form,  request,  response);
	      }
		else if("removeFilter".equalsIgnoreCase(contextAction))
		{
			PersonSearchForm psForm = (PersonSearchForm)form;
			srpUtil.showButton(request,(String) psForm.getAttributeMap().get("DSSearchCriteriaString"));
			String scString = (String) psForm.getAttributeMap().get("DSSearchCriteriaString");
			String reportTp = (String) psForm.getAttributeMap().get("reportType");
			ArrayList<Object> investigatorDDList = (ArrayList<Object>) psForm.getAttributeMap().get("investigatorDDList");
			ArrayList<Object> jurisdictionDDList = (ArrayList<Object>) psForm.getAttributeMap().get("jurisdictionDDList");
			ArrayList<Object> conditionDDList = (ArrayList<Object>) psForm.getAttributeMap().get("conditionDDList");
			ArrayList<Object> CaseStatusDDList = (ArrayList<Object>) psForm.getAttributeMap().get("CaseStatusDDList");
			ArrayList<Object> startDateDDList = (ArrayList<Object>) psForm.getAttributeMap().get("startDateDDList");
			ArrayList<Object> notificationDDList = (ArrayList<Object>) psForm.getAttributeMap().get("notificationDDList");
	    	
			ArrayList<Object> conditionDD = (ArrayList<Object>) psForm.getAttributeMap().get("conditionDD");
			ArrayList<Object> observationTypeDDList = (ArrayList<Object>) psForm.getAttributeMap().get("observationTypeDDList");
			ArrayList<Object> descriptionDDList = (ArrayList<Object>) psForm.getAttributeMap().get("descriptionDDList");
			String totalRecords = (String) psForm.getAttributeMap().get("totalRecords");
			psForm.clearAll();
			
			psForm.getAttributeMap().put("DSSearchCriteriaString", scString);
	    	psForm.getAttributeMap().put("reportType", reportTp);
	    	psForm.getAttributeMap().put("investigatorDDList", investigatorDDList);
	    	psForm.getAttributeMap().put("jurisdictionDDList", jurisdictionDDList);
	    	psForm.getAttributeMap().put("conditionDDList", conditionDDList);
	    	psForm.getAttributeMap().put("CaseStatusDDList", CaseStatusDDList);
	    	psForm.getAttributeMap().put("startDateDDList", startDateDDList);
	    	psForm.getAttributeMap().put("notificationDDList", notificationDDList);
	    	
	    	psForm.getAttributeMap().put("conditionDD", conditionDD);
	    	psForm.getAttributeMap().put("observationTypeDDList", observationTypeDDList);
	    	psForm.getAttributeMap().put("descriptionDDList", descriptionDDList);
	    	psForm.getAttributeMap().put("totalRecords", totalRecords);
	    	//request.setAttribute("ActionMode", "InitLoad");	
	    	return srpUtil.filterPatientSubmit( mapping,  form,  request,  response);
		}
	    else if("sortingByColumn".equalsIgnoreCase(contextAction)){
	    	ArrayList<Object> patientVoCollection = (ArrayList )request.getSession().getAttribute("personList");
	    	PersonSearchForm psForm = (PersonSearchForm)form;
	    	try{
	    		srpUtil.showButton(request,(String) psForm.getAttributeMap().get("DSSearchCriteriaString"));
	    		String reportType = (String)psForm.getAttributeMap().get("reportType");
				if(reportType != null && reportType.equalsIgnoreCase("I")){
					srpUtil.sortInvs(psForm,patientVoCollection,false,request);
				}
				else if(reportType != null && reportType.equalsIgnoreCase("LR")){
					srpUtil.sortObservations(psForm,patientVoCollection,false,request);
				}
				else{
					srpUtil.sortPatientLibarary(psForm,patientVoCollection,false,request);
					
				}
				
	    		psForm.getAttributeMap().put("queueCount", String.valueOf(patientVoCollection.size()));
	    		
				request.setAttribute("queueCount", String.valueOf(patientVoCollection.size()));
				request.setAttribute("personList", patientVoCollection);
	    	}catch(Exception e){

	    	}
	    	return PaginationUtil.personPaginate(psForm, request, "searchResultLoad",mapping);
	      }
		return null;
    }

    private void findPeople(ActionForm form, HttpSession session,
			    HttpServletRequest request, boolean homePageSrch, boolean bEntitySearch)
    {
    	ArrayList<?> personList = new ArrayList<Object> ();
    	PatientSearchVO psVO = null;
    	Boolean patientSearch = false;
    	PersonSearchForm psForm = (PersonSearchForm)form;
    	String reportT = (String)psForm.getAttributeMap().get("reportType");
		if(reportT == null || reportT.trim().length() == 0 || reportT.equalsIgnoreCase("N"))
			psForm.clearAll();
		if(reportT != null && reportT.equalsIgnoreCase("N")) 
			patientSearch = true;
		
    	SearchResultPersonUtil srpUtil = new SearchResultPersonUtil();
    	
        //civil00018034 To Implement PatientSearch Functionality from HomePage (SimpleSearch), handle here
    	if(homePageSrch) {
    		HomePageForm hForm = (HomePageForm)request.getSession().getAttribute("homepageForm");
    		psVO = (PatientSearchVO) hForm.getPatientSearchVO();
    		//If BirthTime entered as part of search, Split it to 3 different Fields (to fit old screen format)
    		srpUtil._populateBirthTime(psVO);


    	} else {
    		psVO = (PatientSearchVO)psForm.getPersonSearch();//From the UI
    		psForm.setOldPersonSearch(psVO);

    	}
    	
		if(patientSearch){
			psVO.setReportType("N");;
		}
		
    	if ( (psVO.getBirthTimeDay() != null && psVO.getBirthTimeDay().trim().length() != 0) &&
    		 (psVO.getBirthTimeMonth() != null && psVO.getBirthTimeMonth().trim().length() != 0) &&
    		 (psVO.getBirthTimeYear() != null && psVO.getBirthTimeYear().trim().length() != 0))
	     {
    		psVO.setBirthTime(PersonUtil.getBirthDate(psVO.getBirthTimeMonth(),
	                                                 psVO.getBirthTimeDay(),
	                                                 psVO.getBirthTimeYear()));
    		srpUtil.setAgeAndUnits(psVO);
	     }
	     if (psVO.getLocalID() != null && !psVO.getLocalID().trim().equals(""))
	     {
	    	 psVO.setOldLocalID(psVO.getLocalID());
	    	 psVO.setPatientIDOperator("IN");
	    	 srpUtil.replaceDisplayLocalIDWithActualLocalID(psVO);
	     }
	     //Trim the Act ID for empty spaces

	     if(psVO.getActId() != null && !psVO.getActId().trim().equals("")) {
	    	 srpUtil.trimEventID(psVO);
	     }

	     String contextAction = request.getParameter("ContextAction");

	     if (contextAction == null){
	    	 contextAction = (String) request.getAttribute("ContextAction");
	     }



	       //Set temperory for mergeperson
	     if (request.getParameter("Mode1") != null &&
	         request.getParameter("Mode1").equals("ManualMerge"))
	     {
	    	 psVO.setActive(true);
	    	 psVO.setLogicalDeleted(false);
	    	 psVO.setSuperceded(false);
	     }
	     if(homePageSrch) {
	     	session.setAttribute("DSSearchCriteria", psVO);
	     }

	     NBSContext.store(session, "DSSearchCriteria", psVO);

		if (psVO != null)
		{
			String existing = request.getParameter("ContextAction") == null ? (String)request.getParameter("ContextAction") : null;
	        boolean initLoad = request.getParameter("initLoad") == null ? false: Boolean.valueOf(request.getParameter("initLoad")).booleanValue();

	        ArrayList list = new ArrayList();
	        if("ReturnToSearchResults".equalsIgnoreCase(existing)|| initLoad){
	           	list = (ArrayList)session.getAttribute("personList");
	           	NBSSecurityObj secObj = (NBSSecurityObj) session.getAttribute("NBSSecurityObject");
	           	boolean checkPermission = secObj.getPermission(NBSBOLookup.PATIENT,NBSOperationLookup.VIEWWORKUP);
	           	//boolean checkPermissionInvestigation = secObj.getPermission(NBSBOLookup.INVESTIGATION,NEDSSConstants.VIEW, programArea, jurisdiction, sharedInd);
	           	boolean checkPermissionInvestigation =true;

	           	if(psVO.getReportType()!=null && psVO.getReportType().equalsIgnoreCase("I")){
	           		srpUtil.setDisplayInfoInvestigation(list,checkPermissionInvestigation);
	           		try {
						srpUtil.sortInvs(psForm, list, false, request);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	           	}
	           	else
	           		if(psVO.getReportType()!=null && psVO.getReportType().equalsIgnoreCase("LR")){
		           		srpUtil.setDisplayInfoLaboratoryReport(list,checkPermissionInvestigation, request);
		           		try {
							srpUtil.sortObservations(psForm, list, false, request);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		           		}
		           	else{
		           		srpUtil.setDisplayInfo(list,checkPermission);
				           	try{
				           		srpUtil.sortPatientLibarary(psForm, list, false, request);
				           	}catch(Exception e){
			
				           	}
		           	}
	        }else{
			    try
			    {
			    	NedssUtils nedssUtils = null;
				    MainSessionCommand msCommand = null;
				    String sBeanJndiName = "";
				    String sMethod = "";
				    Object[] oParams = null;
				    sBeanJndiName = JNDINames.ENTITY_PROXY_EJB;
				    sMethod = "findPatient";
				    oParams = new Object[]{
				    		psVO, new Integer(propertyUtil.getNumberOfRows()),
				    		new Integer(0) };

					MainSessionHolder holder = new MainSessionHolder();
					msCommand = holder.getMainSessionCommand(session);
			        ArrayList<?> arrList = msCommand.processRequest(sBeanJndiName,  sMethod, oParams);
			        personList = (ArrayList<?> )arrList.get(0);

					
			    }
			    catch (Exception e)
			    {
		    		logger.warn("Exception in PersonSearchSubmit: " + e.getMessage());
		    		e.printStackTrace();

			    }
			    if( personList != null && personList.size() > 0  &&  personList.get(0)!=null){
			    	DisplayPersonList displayPersonList = (DisplayPersonList) personList.get(0);
		        	list = displayPersonList.getList();
					try{
					      NBSSecurityObj secObj = (NBSSecurityObj) session.getAttribute(
					          "NBSSecurityObject");
						boolean checkSummaryPermission = secObj.getPermission(NBSBOLookup.PATIENT,
						          NBSOperationLookup.VIEWWORKUP);
						
						if(psVO.getReportType()!=null && psVO.getReportType().equalsIgnoreCase("I")){
							
							srpUtil.setDisplayInfoInvestigation(list,checkSummaryPermission);
							srpUtil.sortInvs(psForm, list, false, request);
						}else
							if(psVO.getReportType()!=null && psVO.getReportType().equalsIgnoreCase("LR")){
								
								srpUtil.setDisplayInfoLaboratoryReport(list,checkSummaryPermission, request);
								srpUtil.sortObservations(psForm, list, false, request);
							}
							else{
							
								srpUtil.setDisplayInfo(list,checkSummaryPermission);
								srpUtil.sortPatientLibarary(psForm, list, false, request);
							}
						
						psForm.setIntqueueCount(displayPersonList.getTotalCounts());
						psForm.setPatientVoCollection(list);
						psForm.initializeDropDowns();
					
					/*	psVO.setJurisdictionsDD(psForm.getJurisdictionsDD());
						psVO.setNotifications(psForm.getNotifications());
						psVO.setCaseStatuses(psForm.getCaseStatusesDD());
						
						psVO.setConditions(psForm.getConditions());
						psVO.setDateFilterList(psForm.getStartDateDropDowns());*/
						psForm.getAttributeMap().put("notificationDDList", psForm.getNotifications());
						psForm.getAttributeMap().put("investigatorDDList", psForm.getInvestigators());
						psForm.getAttributeMap().put("CaseStatusDDList", psForm.getCaseStatusesDD());
						psForm.getAttributeMap().put("jurisdictionDDList", psForm.getJurisdictionsDD());
						psForm.getAttributeMap().put("startDateDDList", psForm.getStartDateDropDowns());
						psForm.getAttributeMap().put("conditionDDList", psForm.getConditions());
						
						psForm.getAttributeMap().put("observationTypeDDList", psForm.getObservationTypesDD());
						psForm.getAttributeMap().put("descriptionDDList", psForm.getDescriptionDD());
						psForm.getAttributeMap().put("conditionDD", psForm.getConditionDD());
						
						psForm.getAttributeMap().put("InvestigatorsCount",new Integer(psForm.getInvestigators().size()));
						psForm.getAttributeMap().put("JurisdictionsCount",new Integer(psForm.getJurisdictionsDD().size()));
						psForm.getAttributeMap().put("ConditionsCount",new Integer(psForm.getConditions().size()));
						psForm.getAttributeMap().put("caseStatusCount",new Integer(psForm.getCaseStatusesDD().size()));
						psForm.getAttributeMap().put("dateFilterListCount",new Integer(psForm.getStartDateDropDowns().size()));
						psForm.getAttributeMap().put("notificationsCount",new Integer(psForm.getNotifications().size()));
						
						psForm.getAttributeMap().put("DescriptionCount",new Integer(psForm.getDescriptionDD().size()));
						psForm.getAttributeMap().put("observationTypeCount",new Integer(psForm.getObservationTypesDD().size()));
						psForm.getAttributeMap().put("ConditionsDDCount",new Integer(psForm.getConditionDD().size()));
						
						psForm.getAttributeMap().put("TOTALCOUNT", psForm.getIntqueueCount());
						//session.setAttribute("notificationDDList", psForm.getNotifications());
						NBSContext.store(session, "attributeMap", psForm.getAttributeMap());
						session.setAttribute("attributeMap", psForm.getAttributeMap());
					}catch(Exception e){

			        }
			    }

	        }

	        //set temperory for merge person
	        session.setAttribute("PerMerge",personList);
	        psForm.getAttributeMap().put("queueCount", String.valueOf(list.size()));
	        String reportType = psForm.getPersonSearch().getReportType();
	        
	        if(reportType!=null && (reportType.equalsIgnoreCase("LR") || reportType.equalsIgnoreCase("I")))
	        	request.setAttribute("PageTitle", "Event Search Results ");
	        else
	        	request.setAttribute("PageTitle", "Search Results ");
	        request.setAttribute("personList", list);
	        session.setAttribute("personList", list);
	        //psForm.getAttributeMap().get("TOTALCOUNT");
	        String error = null;
	        if(reportType!=null && (reportType.equalsIgnoreCase("LR") || reportType.equalsIgnoreCase("I"))){
	        int totalAllowedRecords = 0;
	        int totalNumberOfRecords = 0;
	        if (propertyUtil.getNumberOfRows() != 0) {
	        	totalAllowedRecords =  propertyUtil.getNumberOfRows();
    		} else
    			throw new NEDSSSystemException(
    					"Expected the number of patient search "
    							+ "results returned to the caller defined in the property file.");
    		
	    		
	        
			if(psForm.getAttributeMap().get("TOTALCOUNT")!= null)
	        	 totalNumberOfRecords = (int)psForm.getAttributeMap().get("TOTALCOUNT");
	      
	       
			if(totalNumberOfRecords > totalAllowedRecords )
	        	error = "error";
			else 
				error = "N";
	        }
	        else 
				error = "N";
			psForm.getAttributeMap().put("totalRecords",error);
	        request.setAttribute("totalRecords",error);
	        
	        request.setAttribute("queueCount", list.size());
	        request.setAttribute("personSearchForm", psForm);
	    	//To make sure SelectAll is checked, see if no criteria is applied
			if(psForm.getSearchCriteriaArrayMap().size() == 0)
				request.setAttribute("ActionMode", "InitLoad");		
	        
	        //No event search
	        if(reportType==null || reportType.isEmpty())
	        	psForm.getAttributeMap().put("reportType","N");
	        else//Lab report or Morbidity report or Case report
	        	 if(reportType.equalsIgnoreCase("LR") || reportType.equalsIgnoreCase("MR") || reportType.equalsIgnoreCase("CR"))
	 	        	psForm.getAttributeMap().put("reportType","LMC");
	 	        else//Investigation
	 	        	psForm.getAttributeMap().put("reportType", psForm.getPersonSearch().getReportType());
	        
	        if(homePageSrch) {
	        	session.setAttribute("DSSearchResults", personList);
	        	request.setAttribute("homePageSrch", "true");
	        }
	     //   NBSContext.store(session, "DSSearchCriteria", personList);
		    NBSContext.store(session, "DSSearchResults", personList);
		}
    }




}