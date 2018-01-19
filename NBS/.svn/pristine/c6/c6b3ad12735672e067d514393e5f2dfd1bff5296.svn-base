package gov.cdc.nedss.webapp.nbs.action.pagemanagement.managepage;

import gov.cdc.nedss.page.ejb.portproxyejb.dt.ManagePageDT;
import gov.cdc.nedss.pagemanagement.ejb.pagemanagementproxyejb.vo.PageManagementProxyVO;
import gov.cdc.nedss.pagemanagement.ejb.pagemanagementproxyejb.vo.WaTemplateVO;
import gov.cdc.nedss.pagemanagement.wa.dt.BatchEntry;
import gov.cdc.nedss.pagemanagement.wa.dt.PageCondMappingDT;
import gov.cdc.nedss.pagemanagement.wa.dt.WaTemplateDT;
import gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil;
import gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.util.DropDownCodeDT;
import gov.cdc.nedss.systemservice.vo.ParticipationTypeVO;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.NedssUtils;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.portpage.util.PortPageUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageLoadUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.util.PageManagementActionUtil;
import gov.cdc.nedss.webapp.nbs.action.util.CallProxyEJB;
import gov.cdc.nedss.webapp.nbs.action.util.NBSPageConstants;
import gov.cdc.nedss.webapp.nbs.action.util.PaginationUtil;
import gov.cdc.nedss.webapp.nbs.action.util.QueueUtil;
import gov.cdc.nedss.webapp.nbs.form.page.PageForm;
import gov.cdc.nedss.webapp.nbs.form.pagemanagement.managepage.PageBuilderForm;
import gov.cdc.nedss.webapp.nbs.form.util.BaseForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.QuestionsCache;
import gov.cdc.nedss.webapp.nbs.queue.GenericForm;
import gov.cdc.nedss.webapp.nbs.queue.GenericQueueUtil;
import gov.cdc.nedss.webapp.nbs.queue.QueueColumnDT;
import gov.cdc.nedss.webapp.nbs.queue.QueueDT;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

public class ManagePageAction extends DispatchAction
{
	static final LogUtils logger = new LogUtils(ManagePageAction.class.getName());
	QueueUtil queueUtil = new QueueUtil();
	PropertyUtil properties = PropertyUtil.getInstance();
	public static PropertyUtil propertyUtil= PropertyUtil.getInstance();
    public ManagePageAction() {}

    GenericQueueUtil genericQueueUtil = new GenericQueueUtil();
    /**
     * Go to managepages/AddPage.jsp where the user can enter the unique page name,
     * select the template, mapping guide and related conditions (if any)
     * for a new page.
     *
     * <br/>
     * After adding the page the user can customize the page using Edit Page.
     * Managing the actual page contents (for example, adding tabs, sections,
     * subsections etc...) is handled by the editPageContentsLoad() method.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
  
    
    public ActionForward addPageLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
    	pageBuildForm.clearAll();

    	WaTemplateVO vo = new WaTemplateVO();
    	WaTemplateDT templateDT = new WaTemplateDT();
    	vo.setWaTemplateDT(templateDT);
    	vo.getWaTemplateDT().setBusObjType(NEDSSConstants.INV);
    	pageBuildForm.setSelection(vo);
    	pageBuildForm.setSelectedCondList(new ArrayList<Object> ());
    	pageBuildForm.setSelectedConditionCodes(null);
    	pageBuildForm.setActionMode(NEDSSConstants.CREATE_LOAD_ACTION);
    	ArrayList<Object> list = pageBuildForm.getMessageIdList();

       	Collections.sort(list, new Comparator<Object>(){
    		 
            public int compare(Object emp1, Object emp2) {
                 return (((DropDownCodeDT)emp1).getValue().compareToIgnoreCase(((DropDownCodeDT)emp2).getValue()));
            }
 
        });
       	
        pageBuildForm.setMessageIdList(list);
    	request.setAttribute("PageTitle", "Manage Pages: Add Page");
        return mapping.findForward("addPage");
    }



    /**
     * Save the meta data about a page.
     * <br/> Note that saving the contents of a page (for example, tab, section,
     * subsection etc...) is handled via a DWR AJAX call and hence there is no
     * relevant dispatch method found.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public ActionForward addPageSubmit(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
	     	Long waTemplateUid = null;
	
	    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
	    	pageBuildForm.setActionMode(NEDSSConstants.CREATE_SUBMIT_ACTION);
	    	WaTemplateVO waTempVo = (WaTemplateVO)pageBuildForm.getSelection();
	    	PageManagementActionUtil util = new PageManagementActionUtil();
	    	WaTemplateDT dt = waTempVo.getWaTemplateDT();
	    	//check if the page name exists on another page..
	    	dt.setTemplateNm(dt.getTemplateNm().trim()); //Trim and set the template name
	    	ArrayList<Object> errorMsgs = util.validatePageNmUniqueness(dt.getTemplateNm(), dt.getUid(),NEDSSConstants.PAGE_ELEMENT_TYPE_PAGE );
	    	if(pageBuildForm.getSelectedConditionCodes() != null && pageBuildForm.getSelectedConditionCodes().length>0)
	    	   errorMsgs =  util.validateCondition(pageBuildForm.getSelectedConditionCodes(), errorMsgs, dt.getBusObjType());
	    	if (!errorMsgs.isEmpty()){
	    		//Iterator iter = errorMsgs.iterator();
	    		//request.setAttribute("CreateError",(String) iter.next());
	    		pageBuildForm.setErrorList(errorMsgs);
	    		request.setAttribute("errors", pageBuildForm.getErrorList());
	    		
	    		//populate selected list (if any) so right side shows up
	    		String[] strArr = pageBuildForm.getSelectedConditionCodes();
	    		ArrayList<Object> selCondList = pageBuildForm.getSelectedCondList();
	    		selCondList.clear();
	    		for (int i = 0; strArr != null && i < strArr.length; ++i) {
	    			Iterator iter = pageBuildForm.getConditionAllListByBO(dt.getBusObjType()).iterator();
	    			while (iter.hasNext()) {
	    				DropDownCodeDT dDownDT = (DropDownCodeDT) iter.next();
	    				if (dDownDT.getKey().equalsIgnoreCase(strArr[i])) {
	    					DropDownCodeDT theDropDownDT = new DropDownCodeDT();
	    					theDropDownDT.setKey(dDownDT.getKey());
	    					theDropDownDT.setValue(dDownDT.getValue());
	    					selCondList.add(theDropDownDT);
	    					break;
	    			    }
	    		    }
	    	    }
	    	    pageBuildForm.setSelectedConditionCodes(null);
	    	    request.setAttribute("PageTitle", "Manage Pages: Add Page");
	    		return mapping.findForward("addPage");
	    	} //end if errorMsgs duplicate page name

	    	//restricting user to create another vaccination page.
	    	if(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equals(dt.getBusObjType())){
	    		Long waTemplateUidOfVacPage = util.findPageExistenceByBusinessObjType(dt.getBusObjType(), request);
		    	if (waTemplateUidOfVacPage!=null){
		    		request.setAttribute("PageTitle", "Manage Pages: Add Page");
		    		request.setAttribute("preventAnotherVACPage",true);
		    		return mapping.findForward("addPage");
		    	}
	    	}
	    	dt = util.setTheValuesForWaTemplateDT(dt, request, NEDSSConstants.CREATE_SUBMIT_ACTION);
	    	dt.setTemplateType(NEDSSConstants.DRAFT);
	    	dt.setXmlPayload("XML Payload");
	    	String pgPageName = dt.getTemplateNm();
	    	dt.setFormCd(getPgFormCodeFromPageName(pgPageName,dt.getBusObjType()));
	    	dt.setPublishIndCd(NEDSSConstants.FALSE);
	
	    	Collection<Object> pageCondMappDTColl = new ArrayList<Object>();
	    	if (pageBuildForm.getSelectedConditionCodes()!= null && pageBuildForm.getSelectedConditionCodes().length>0) //can be no conditions selected at this time..
	    	{
	    		String[] strArr = pageBuildForm.getSelectedConditionCodes();
	    		for (int i=0; i < strArr.length; i++) {
	    			String  result = strArr[i] ;
	    			//place first condition code in the waTemplateDT - may remove this column at later date
	    			if (waTempVo.getWaTemplateDT().getConditionCd() == null)
	    				waTempVo.getWaTemplateDT().setConditionCd(result);
	
	    			PageCondMappingDT pageCondMapdt = new PageCondMappingDT();
	    			pageCondMapdt.setConditionCd(result);
	    			java.util.Date startDate= new java.util.Date();
	    			Timestamp aAddTime = new Timestamp(startDate.getTime());
	    			pageCondMapdt.setAddTime(aAddTime);
	    			pageCondMapdt.setLastChgTime(aAddTime);
	    			NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) request.getSession().getAttribute("NBSSecurityObject");
	    			pageCondMapdt.setAddUserId(Long.valueOf(nbsSecurityObj.getEntryID()));
	    			pageCondMapdt.setLastChgUserId(Long.valueOf(nbsSecurityObj.getEntryID()));
	    			pageCondMappDTColl.add(pageCondMapdt);
	    		}
	    		waTempVo.setWaPageCondMappingDTColl(pageCondMappDTColl) ;
	    	}//if no conditions
	
	    	HttpSession session = request.getSession();
	    	//Call the EJB here
	    	try{
	    		waTemplateUid = util.setWATemplate(session, waTempVo,NEDSSConstants.CREATE_SUBMIT_ACTION);
	    		request.getSession().setAttribute("waTemplateUid",waTemplateUid);
	    		//update pgMap with the new page name
	    		if(request.getSession().getAttribute("pgMap")!=null){
		    		HashMap<Object,Object> pgMap = (HashMap <Object,Object>)request.getSession().getAttribute("pgMap");
		    		dt.setWaTemplateUid(waTemplateUid);
		    		pgMap.put(dt.getWaTemplateUid(),pgPageName);
		    		session.setAttribute("pgMap", pgMap);
	    		}
	    	}catch(Exception e){
				logger.error(" Error while Submitting the Page Details "+e.getMessage(),e);
				throw new ServletException(e.getMessage());
			}
	
	    	//Update toPageFromCode in nbs_conversion_page_mgmt table for pre-loaded mapping
	    	if(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equals(dt.getBusObjType())){
	    		String formPageFormCd = PortPageUtil.LEGACY_PAGE_START_WITH+dt.getBusObjType();
	    		PortPageUtil.updateToPageFormCdInNbsConversionPageMgmt(dt.getFormCd(), formPageFormCd, PortPageUtil.LEGACY_EVENT_DUMMY_CONDITION_CD, request);
	    	}
	    	
	    	request.setAttribute("PageTitle", "Manage Pages:Add New Page");
	    	request.setAttribute("pgPageName",pgPageName);
	    	request.getSession().setAttribute("from","A");
	        return mapping.findForward("previewPage");
    	}catch(Exception ex){
    		logger.error("Error in the Add Page Submit method of ManagePageAction class :" +ex.getMessage(), ex);
    		throw new ServletException(ex.getMessage());
    	}
    }

    /**
     * Return to a view where the meta data about a page is displayed. This resulting
     * view will contain links to actions like:
     * a) Editing meta data about the page.
     * b) Editing the actual page contents (like tab, section, subsection etc...)
     *      for this page.
     * c) Publishing the page.
     * d) Adding rules to the page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewPageLoad(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
	    	Long waTemplateUid = null;
	    	PageForm pageForm = (PageForm)form;
	    	//To avoid showing the patient local name in the investigation page, contact record tab (from page management)
	    	pageForm.getAttributeMap().remove("patientLocalName");
	    	pageForm.setSelection(new WaTemplateDT());
	    	 PageManagementProxyVO pmProxyVO = new PageManagementProxyVO();
	    	 PageManagementActionUtil util = new PageManagementActionUtil();
	    	if(request.getParameter("waTemplateUid") != null)
	    		waTemplateUid= (Long)Long.parseLong((request.getParameter("waTemplateUid")));
	    	else {
	    		waTemplateUid= (Long)request.getSession().getAttribute("waTemplateUid");
	    	}
	    	request.getSession().setAttribute("waTemplateUid", waTemplateUid);
	    	
	    	HashMap<Object, Object> pgMap = (HashMap)request.getSession().getAttribute("pgMap");
	    	String pgPageName = (String)pgMap.get(waTemplateUid);


	        try{
	        	pmProxyVO =  util.getPageDraft(waTemplateUid, request.getSession());
	        	
	        }catch(Exception ex){
	        	logger.error("Error in getting the page draft "+ ex.getMessage());
	        	throw new ServletException(ex.getMessage());
	        }

	        String result = "";
	        String type = "PAGE";
	        try{
	        	 Map<Object,Object> batchMap = util.getBatchMap(waTemplateUid, request.getSession());
	        	 pageForm.setSubSecStructureMap(batchMap);
	        	 request.setAttribute("SubSecStructureMap", batchMap);
	        	 request.setAttribute("disableSubmitButton", "yes");
	        	 pageForm.setBatchEntryMap(new HashMap<Object,ArrayList<BatchEntry>>());

	        	 result = util.generateXMLandJSP(pmProxyVO, type);

	        }catch(Exception ex){
	        	logger.error("Error in genarating the JSP : "+ ex.getMessage());
	        	throw new ServletException(ex.getMessage());
	        }
	        if(result!= null && !result.equals("failure"))
	        	pageForm.setActionMode(NEDSSConstants.PREVIEW_ACTION);
	        _setRenderDir(request,pmProxyVO);
	        if(pgPageName == null || pgPageName.isEmpty()){
	        	pgPageName = pmProxyVO.getWaTemplateDT().getTemplateNm();
	        }      
			pageForm.setPageTitle("View "+pgPageName, request);
			request.setAttribute("PageTitle", "Manage Pages: View Page");
			request.setAttribute("pgPageName", pgPageName);
	        request.getSession().setAttribute("pgPageName", pgPageName);
	        request.getSession().setAttribute("PageForm", pageForm); //discuss: GT
	        request.setAttribute("mode", request.getParameter("mode"));
	        BaseForm baseform = pageForm;
	        request.getSession().setAttribute("BaseForm", baseform); //discuss: GT
	    	request.setAttribute("published",result);
	    	//This is for the CreateNewDraft message
	    	if(pmProxyVO.getWaTemplateDT().getTemplateType().equals(NEDSSConstants.PUBLISHED))
	    		request.setAttribute(NEDSSConstants.CREATEDRAFTIND, NEDSSConstants.CREATEDRAFTINDPUB);
	    	if(pmProxyVO.getWaTemplateDT().getTemplateType().equals(NEDSSConstants.DRAFT))
	    		request.setAttribute(NEDSSConstants.CREATEDRAFTIND, NEDSSConstants.CREATEDRAFTINDDRT);
	    	String strInd=pmProxyVO.getWaTemplateDT().getPortReqIndCd()!=null?pmProxyVO.getWaTemplateDT().getPortReqIndCd():"";
	    	String strType=pmProxyVO.getWaTemplateDT().getTemplateType()!=null?pmProxyVO.getWaTemplateDT().getTemplateType():"";
	    	//get values for Contact Tracing so Contact Tracing and Contact Records
	    	//             tabs display if enabled

	    	HttpSession session = request.getSession();
	    	NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) session.getAttribute("NBSSecurityObject");
	    	boolean HIVQuestionPermission = nbsSecurityObj.getPermission(NBSBOLookup.GLOBAL,
					NBSOperationLookup.HIVQUESTIONS);
			if(HIVQuestionPermission)
				pageForm.getSecurityMap().put("hasHIVPermissions", NEDSSConstants.TRUE);
	    	Collection<Object> pageCondMapDTColl = pmProxyVO.getPageCondMappingColl();
			String conditionCd = NEDSSConstants.NONE;
			if (!pageCondMapDTColl.isEmpty()) {
				Iterator<Object> pgIter = pageCondMapDTColl.iterator();
				PageCondMappingDT pageCondMapDt = (PageCondMappingDT) pgIter.next();
				conditionCd = pageCondMapDt.getConditionCd();
			}
			String ContactTracingByConditionCd = "false";
			boolean viewContactTracing = nbsSecurityObj.getPermission(NBSBOLookup.CT_CONTACT, NBSOperationLookup.VIEW);
			if (!conditionCd.equals(NEDSSConstants.NONE))  //note - condition code doesn't have to be selected until published..
				ContactTracingByConditionCd = PageLoadUtil.getConditionTracingEnableInd(conditionCd);
			pageForm.getSecurityMap().put("ContactTracingEnableInd", ContactTracingByConditionCd);
			pageForm.getSecurityMap().put("checkToViewContactTracing", String.valueOf(viewContactTracing));

	    	String messageInd="";
	    	String from = request.getSession().getAttribute("from")!=null?(String)request.getSession().getAttribute("from"):"";
	    	request.getSession().removeAttribute("from");
	    	if(request.getParameter("from")!=null){ // For link from list page
	    			from = (String)request.getParameter("from");
	    	}
	    	String deleteMsg = (String)request.getSession().getAttribute("deleteMsg")!=null?(String)request.getSession().getAttribute("deleteMsg"):"";


	    	if(from.equalsIgnoreCase("A") && strType.equalsIgnoreCase("Draft")){
	    		messageInd="ATD";
	    	}
	    	if(from.equalsIgnoreCase("E") && strType.equalsIgnoreCase("Draft")){
	    		messageInd="ETD";
	    	}
	    	if(from.equalsIgnoreCase("E") && strInd.equalsIgnoreCase("T") && strType.equalsIgnoreCase("Published")){
	    		messageInd="ETP";
	    	}
	    	if(from.equalsIgnoreCase("E") && strInd.equalsIgnoreCase("F") && strType.equalsIgnoreCase("Published")){
	    		messageInd="EFP";
	    	}

	    	if(from.equalsIgnoreCase("SAT")){
	    		messageInd="SAT";
	    	}
	    	if(from.equalsIgnoreCase("CN")){
	    		messageInd="CND";
	    	}
	    	if(from.equalsIgnoreCase("CNP")){
	    		messageInd="CNP";
	    	}
	    	if(deleteMsg.equals("true")){
	    		messageInd = "DEL";
	    		request.getSession().removeAttribute("deleteMsg");
	    	}
	    	if(strType.equalsIgnoreCase("Draft") && (pmProxyVO.getWaTemplateDT().getPublishVersionNbr() != null && pmProxyVO.getWaTemplateDT().getPublishVersionNbr()==0 ))
	    		request.getSession().setAttribute(NEDSSConstants.DELETECONTEXT, NEDSSConstants.PUBWITHDRAFT);
	    	clearAllParticipations(pageForm); //from AttributeMap
	    	request.setAttribute("messageInd", messageInd);
	    	
	    	request.setAttribute("isDisablePublish", false);
	    	//For Vaccination set conditionCd Empty so that it allows to publish the page.
	    	if(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equalsIgnoreCase(pmProxyVO.getWaTemplateDT().getBusObjType())){
	    		conditionCd = "";
	    		
	    		PortPageUtil.restrictPagePublishUntillPortingCompleted(pmProxyVO.getWaTemplateDT().getBusObjType(), request);
	    	}
    		
	    	request.setAttribute("relatedConditionCode", conditionCd );
	        return mapping.findForward("viewPage");
    	}catch(Exception ex){
    		logger.error("Error in the preview of the page :" + ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}

    }

    /**
     * Return to a view where the user can edit the contents (tabs, sections,
     * subsections) of the page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward editPageContentsLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {

    	try{
	    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
	    	pageBuildForm.setActionMode(NEDSSConstants.EDIT_LOAD_ACTION);
	    	PageManagementActionUtil util = new PageManagementActionUtil();
	    	HttpSession session  = request.getSession();
	    	Long waTemplateUid = null;
	    	request.getSession().removeAttribute("from"); //clear success msg attrib
	    	//boolean isSavedtoDB = false;
	    	// form the list?
	    	if(request.getParameter("waTemplateUid") != null)
	    		waTemplateUid= (Long)Long.parseLong((request.getParameter("waTemplateUid")));
	    	else
	    	    //from the edit?
	    		waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
	    	    // This is for the viewLoad
	    		request.getSession().setAttribute("waTemplateUid", waTemplateUid);
	    	String context = "";
	    	if(request.getParameter(NEDSSConstants.FROM_CONTEXT) !=null)
	    		context = (String)request.getParameter(NEDSSConstants.FROM_CONTEXT);
	    	request.setAttribute(NEDSSConstants.FROM_CONTEXT, context);
	    	PageManagementProxyVO pmProxyVO = new PageManagementProxyVO();

	    	try {
	    	     pmProxyVO =  util.getPageDraft(waTemplateUid, session);

	    	}
	    	catch(Exception e) {
	    		logger.error("Error in getting the Page for editPageContentsLoad() " +e.getMessage());
	    		throw new ServletException(e.getMessage());
	    	}

	    	// set the page in session
	    	session.setAttribute("pageData", pmProxyVO);

	    	// set title for current condition being edited
	    	request.setAttribute("pageConditionTitle",pmProxyVO.getWaTemplateDT().getTemplateNm());

	    	// set the starting UId for all the elements that will be newly
	    	// added during this page builder session
	    	session.setAttribute("genUid", new Long(-1));

	    	request.setAttribute("PageTitle", "Manage Pages: Edit Page");
	     	if (pmProxyVO.getWaTemplateDT().getTemplateNm()!=null)
	     		request.setAttribute("pgPageName",pmProxyVO.getWaTemplateDT().getTemplateNm());

	     	String busObjTypeDescTxt="";
	     	if (pmProxyVO.getWaTemplateDT().getBusObjType()!=null){
	     		busObjTypeDescTxt = CachedDropDowns.getCodeDescTxtForCd(pmProxyVO.getWaTemplateDT().getBusObjType(), "BUS_OBJ_TYPE");
	     	}
     		request.setAttribute("busObjTypeDescTxt",busObjTypeDescTxt);
	     	
	        return mapping.findForward("editPage");
    	}catch(Exception ex){
    		logger.error("Error while loading the contants of the page "+ ex.getMessage(), ex);
    		throw new ServletException(ex.getMessage());
    	}
    }

    /**
     * Retrieve a list of all pages available in the system
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public ActionForward list(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	PageBuilderForm pageBuilderForm = (PageBuilderForm)form;
    	Collection<Object> pageList = new ArrayList<Object> ();

    	try{
    		boolean initLoad = request.getParameter("initLoad") == null ? false: Boolean.valueOf(request.getParameter("initLoad")).booleanValue();
    		String context = request.getParameter("context") == null ? null: (String)request.getParameter("context");
    		boolean forceEJBcall = false;
    		String flag = (String)request.getSession().getAttribute("deleteDraft");
    		if(flag!= null && flag.equalsIgnoreCase("true")){
    			request.getSession().removeAttribute("deleteDraft");
    			/*if(request.getParameterMap() != null)
    				request.getParameterMap().clear();*/
    			forceEJBcall=true;
    			request.setAttribute("ActionMode", "InitLoad");
    			//pageBuilderForm = new PageBuilderForm();
    			if(request.getSession().getAttribute("deleteMsg") != null){
    				String deleteMsg = (String)request.getSession().getAttribute("deleteMsg");
    				request.getSession().removeAttribute("deleteMsg");
	    			if(deleteMsg.equals("true")){
	    				String templateNm = (String)request.getSession().getAttribute("pgPageName");
	    				request.setAttribute("deleteMsg", templateNm +" page draft has been successfully  deleted.");
	    			}
    			}
    		}
    		String PageNumber = (String) pageBuilderForm.getAttributeMap().get("PageNumber");
    		String fromPage = request.getParameter("fromPage")!= null ?(String)request.getParameter("fromPage"):null;

	    	if(initLoad && !PaginationUtil._dtagAccessed(request)){
	    		pageBuilderForm.clearAll();
	    		request.setAttribute("ActionMode", "InitLoad");
	    		//sortPageLibarary(pageBuilderForm,pageList,false, request);
	    		forceEJBcall = true;
	    	}

	    	if(context != null){
	    		forceEJBcall = true;
    	    }

	    	if(forceEJBcall){
		    	PageManagementActionUtil util = new PageManagementActionUtil();
		    	HttpSession session = request.getSession();
		    	session.removeAttribute("pageData");
		    	CachedDropDownValues cdv = new CachedDropDownValues();
		    	HashMap<Object,Object> pgMap = new HashMap<Object,Object>();

		    	if(PageNumber!= null && fromPage!= null && fromPage.equalsIgnoreCase("returnToPage")){
		    		pageBuilderForm.getAttributeMap().put("PageNumber", PageNumber);
		    	}
		    	try{
		    		pageList = (Collection<Object>)util.getPageSummaries(session);
			    	}catch(Exception e){
		    		logger.error("Error while getting the page Library");
		    		throw new ServletException(e.getMessage());
			    	}
			    	pageBuilderForm.getAttributeMap().put("managePageList", pageList);

			    	pageBuilderForm.setWaTemplateDTColl(pageList);
			    	pageBuilderForm.initializeDropDowns();
			    	pageBuilderForm.getAttributeMap().put("busObjType",new Integer(pageBuilderForm.getEventType().size()));
			    	pageBuilderForm.getAttributeMap().put("lastUpdated",new Integer(pageBuilderForm.getLastUpdated().size()));
			    	pageBuilderForm.getAttributeMap().put("lastUpdatedBy",new Integer(pageBuilderForm.getLastUpdatedBy().size()));
			    	pageBuilderForm.getAttributeMap().put("status",new Integer(pageBuilderForm.getStatus().size()));
			    	}else{
	    		// If condition will satisfy sorting method functionality
	    		pageList = (ArrayList) pageBuilderForm.getAttributeMap().get("managePageList");
	    	}

	    	Collection sortedList = filterPageLibrary(pageBuilderForm,request);
	    	if(sortedList != null){
	    		pageList = sortedList;
	    		boolean existing = request.getParameter("existing") == null ? false : true;
	    		sortPageLibarary(pageBuilderForm,pageList,existing, request);
	    	}
	    	//To make sure SelectAll is checked, see if no criteria is applied
			if(pageBuilderForm.getSearchCriteriaArrayMap().size() == 0)
				request.setAttribute("ActionMode", "InitLoad");

			pageBuilderForm.getAttributeMap().put("queueCount", String.valueOf(pageList.size()));
			pageBuilderForm.setPageList((ArrayList<Object>)pageList);
			request.setAttribute("PageTitle", "Manage Pages: Page Library");
          	Integer queueSize = properties.getQueueSize(NEDSSConstants.PAGE_MANAGEMENT_PAGE_LIBRARY_QUEUE_SIZE);
			pageBuilderForm.getAttributeMap().put("queueSize", queueSize);
			request.setAttribute("manageFormList", pageList);
    	}catch (Exception ex){
    		logger.error("Error in Page Library Action list() class : "+ ex.getMessage(), ex);
    		throw new ServletException(ex.getMessage());
    	}

		return PaginationUtil.paginate(pageBuilderForm, request, "listAllPages",mapping);

    }

    /**
     * Submit the page present in session as a draft and return to the view mode of this
     * submitted page. <br/>
     * <b> Note: </b> The action form is not populated during this call as the information
     * that is used to submit the data is present in session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward editPageBuilderSubmit(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
	        String pageElementsInOrderCsv = request.getParameter("pageElementsInOrderCsv") != null ?
	                request.getParameter("pageElementsInOrderCsv") : "";

	        // save the page as draft
	         Long draftId= null;
	        try{
		        PageManagementActionUtil pmaUtil = new PageManagementActionUtil();
		        draftId = pmaUtil.savePageAsDraft(request, pageElementsInOrderCsv, null);
	        }catch(Exception ex){
	        	logger.error("Error in Saving the Template from the backend call : "+ex.getMessage());
	        	throw new ServletException(ex.getMessage());
	        }

	        // redirect to the preview page
	        if (draftId != null) {
	            ActionRedirect redirect = new ActionRedirect(mapping.findForward("previewPage"));
	            redirect.addParameter("waTemplateUid", draftId);
	            PageManagementProxyVO pmProxyVO = (PageManagementProxyVO) request.getSession().getAttribute("pageData");
	            String strInd=pmProxyVO.getWaTemplateDT().getPortReqIndCd()!=null?pmProxyVO.getWaTemplateDT().getPortReqIndCd():"";
	        	String strType=pmProxyVO.getWaTemplateDT().getTemplateType()!=null?pmProxyVO.getWaTemplateDT().getTemplateType():"";
	        	request.getSession().setAttribute("from","E");
	        	if (pmProxyVO.getWaTemplateDT().getTemplateNm()!=null)
	        		request.setAttribute("pgPageName",pmProxyVO.getWaTemplateDT().getTemplateNm());
	            return redirect;
	        }
	        else {
	            // save as draft failed.
	            return null;
	        }
    	}catch(Exception ex){
    		logger.error("Error in Edit Submit of the Page in Action class: "+ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}

    }

    public ActionForward publishPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

    	    String strPublished ="";
    	    PageManagementProxyVO pmProxyVO = new PageManagementProxyVO();

    	    Map<Object,Object> batchMap = new HashMap<Object,Object>();
    	    try {
    	    	Long waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");

    			PageBuilderForm pageBuildForm = (PageBuilderForm)form;
    			WaTemplateDT waTemplateDT = new WaTemplateDT();
    			waTemplateDT.setWaTemplateUid(waTemplateUid);
    			waTemplateDT.setVersionNote(((WaTemplateDT)pageBuildForm.getSelection()).getVersionNote());

	    		PageManagementActionUtil util = new PageManagementActionUtil();
	        	HttpSession session  = request.getSession();
		       	try {

		       		 Map<Object,Object> subbatchMap = util.getBatchMap(waTemplateUid, request.getSession());
		        	 //pageForm.setSubSecStructureMap(batchMap);
		        	 request.setAttribute("SubSecStructureMap", subbatchMap);
		        	 request.setAttribute("disableSubmitButton", "yes");
		       		pmProxyVO =  util.publishPage(waTemplateDT, session);
	           	    //  batchMap = util.getBatchMap(pmProxyVO.getWaTemplateDT().getWaTemplateUid(), request);
	           	     strPublished = "published";

	           		if(propertyUtil.getServerRestart()!=null && propertyUtil.getServerRestart().equals("F") ){
	           			Map<Object,Object>	questionMap = (Map)QuestionsCache.getDMBQuestionMapAfterPublish().get(pmProxyVO.getWaTemplateDT().getFormCd());
	           		}

	           	}
	           	catch(Exception e) {
	           		logger.error("Error in publishing the page " +e.getMessage());
	           		throw new ServletException(e.getMessage());
	           	}

    		//TO DO EJB Call here


           	request.setAttribute("published",strPublished);
        	//request.setAttribute("conditiondesc",CachedDropDowns.getConditionDesc(pmProxyVO.getWaTemplateDT().getConditionCd()));
           	String pgPageName = pmProxyVO.getWaTemplateDT().getTemplateNm()!=null?pmProxyVO.getWaTemplateDT().getTemplateNm():"";
           	request.setAttribute("pgPageName", pgPageName);
           	request.getSession().setAttribute("pgPageName", pgPageName);
          //This is for the CreateNewDraft message
	    	if(pmProxyVO.getWaTemplateDT().getTemplateType().equals(NEDSSConstants.PUBLISHED))
	    		request.setAttribute(NEDSSConstants.CREATEDRAFTIND, NEDSSConstants.CREATEDRAFTINDPUB);
	    	if(pmProxyVO.getWaTemplateDT().getTemplateType().equals(NEDSSConstants.DRAFT))
	    		request.setAttribute(NEDSSConstants.CREATEDRAFTIND,NEDSSConstants.CREATEDRAFTINDDRT);
           	request.setAttribute("PageTitle", "Manage Pages: View Page");
           	_setRenderDir(request,pmProxyVO);

           	request.setAttribute("BatchSubSection", batchMap);

           	String strInd=pmProxyVO.getWaTemplateDT().getPortReqIndCd()!=null?pmProxyVO.getWaTemplateDT().getPortReqIndCd():"";
        	String strType=pmProxyVO.getWaTemplateDT().getTemplateType()!=null?pmProxyVO.getWaTemplateDT().getTemplateType():"";
        	String messageInd="";
           	request.getSession().setAttribute("from","P");
           	if(strInd.equalsIgnoreCase("T") && strType.equalsIgnoreCase("Published")){
        		messageInd="PTP";
        	}
        	if(strInd.equalsIgnoreCase("F") && strType.equalsIgnoreCase("Published")){
        		messageInd="PFP";
        	}
        	if(strInd.equals("") && strType.equalsIgnoreCase("Published")){
	    		messageInd = "PUB";
	    	}
        	request.setAttribute("messageInd", messageInd);
            return mapping.findForward("viewPage");
    	}catch(Exception ex){
    		logger.error("Error in publishPage action class : "+ ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}
    }
    public ActionForward deleteDraft(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    		PageManagementActionUtil util = new PageManagementActionUtil();
    		HttpSession session  = request.getSession();

    		Long prevTemplateUid = null;

    		try{

    		Long waTemplateUid = (Long)session.getAttribute("waTemplateUid");
    		if (waTemplateUid == null)
          		logger.error("waTemplateUid is missing in Session?? ");

    		try {
    			prevTemplateUid = util.deleteDraft(session, waTemplateUid);
           	}
           	catch(Exception e) {
           		logger.error("Error in getting the Template " +e.getMessage());
           		throw new ServletException(e.getMessage());
           	}

           	session.setAttribute("deleteDraft", "true");
           	session.setAttribute("deleteMsg", "true");

           //	Long oldWaTemplateUid = (Long)request.getSession().getAttribute("oldWaTemplateUid")!=null?(Long)request.getSession().getAttribute("oldWaTemplateUid"):null;

           	String deleteContext = (String)request.getSession().getAttribute(NEDSSConstants.DELETECONTEXT)!= null? (String)request.getSession().getAttribute(NEDSSConstants.DELETECONTEXT):"";

           	String retPage = "";
           	if(deleteContext != null  && deleteContext.equalsIgnoreCase(NEDSSConstants.PUBWITHDRAFT)){
           		retPage = "previewPage";
           		request.getSession().setAttribute("waTemplateUid",prevTemplateUid);
           		request.getSession().removeAttribute(NEDSSConstants.DELETECONTEXT);
           	}
           	else
           		retPage = "loadList";
            return mapping.findForward(retPage);
    		}catch(Exception ex){
    			logger.error("Error in delete darft in action class: "+ ex.getMessage());
    			throw new ServletException(ex.getMessage());
    		}
    }
	 /**
     * _setRenderDir - place the name of the directory
     *     where the JSPs are generated for this page into request
     * @param requestre
     * @param pmProxyVO
     */
    private void _setRenderDir(HttpServletRequest request,PageManagementProxyVO pmProxyVO) {
		String formCd = "";
		String conditionCd = "";
		String renderDir = "";
		if(pmProxyVO.getWaTemplateDT()!= null) {
			formCd = pmProxyVO.getWaTemplateDT().getFormCd();
			conditionCd = pmProxyVO.getWaTemplateDT().getConditionCd();  //TODO: check for preview
		}
		if(formCd != null && !formCd.equals("")) {
			renderDir = "preview"+"/" + formCd;
			request.getSession().setAttribute(NBSConstantUtil.DSInvestigationCondition, conditionCd);
		}else {
			renderDir = "preview"+"/" +"PG_"+pmProxyVO.getWaTemplateDT().getWaTemplateUid();
		}
		request.setAttribute("renderDir", renderDir);
	}

    public ActionForward saveAsTemplateLoad(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	PageBuilderForm pbForm = (PageBuilderForm)form;
    	try{
	    	pbForm.setSelection(new WaTemplateDT());
	    	Long waTemplateuid = null;
	    	Long waTemplateUid =(Long) request.getSession().getAttribute("waTemplateUid");
	        return mapping.findForward("saveAsTemplate");
    	}catch(Exception ex){
    		logger.error("Error on  saveAsTemplateLoad method in  action class :" + ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}
    }

    public ActionForward publishPopUpLoad(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	PageManagementActionUtil util = new PageManagementActionUtil();
    	try{
    	PageBuilderForm pbForm = (PageBuilderForm)form;
    	WaTemplateDT waDt =  new WaTemplateDT();
    	pbForm.setSelection(new WaTemplateDT());
    	String codeSet ="NBS_MSG_PROFILE";
    	String  conditionCd = "";

    	Long waTemplateUid =(Long) request.getSession().getAttribute("waTemplateUid");
    	Object[] oParams = new Object[] {waTemplateUid};
		String sBeanJndiName = JNDINames.PAGE_MANAGEMENT_PROXY_EJB;
		String sMethod = "getPageDetails";
		Object obj = CallProxyEJB.callProxyEJB(oParams, sBeanJndiName, sMethod, request.getSession());
		WaTemplateVO waTemplateVO = (WaTemplateVO) obj;
		((WaTemplateDT)pbForm.getSelection()).setBusObjType(waTemplateVO.getWaTemplateDT().getBusObjType());
		pbForm.setWaPageCondMappingDTColl(waTemplateVO.getWaPageCondMappingDTColl()); // this is contain of PageCondMappindDT
		request.setAttribute("conditionList", (ArrayList)waTemplateVO.getWaPageCondMappingDTColl());

		//setting the request Attribute for port Indicator
    	setMsgForPortInd(waTemplateVO,request);

        return mapping.findForward("publishPopUpLoad");
    	}catch (Exception ex){
    		logger.error("Error on  publishPopUpLoad method in action class :" + ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}
    }

    public ActionForward saveAsTemplate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
    	    PageBuilderForm pbForm = (PageBuilderForm)form;
    		Long waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
    		String templateNm = ((WaTemplateDT)(pbForm.getSelection())).getTemplateNm();
    		String adminComments = ((WaTemplateDT)(pbForm.getSelection())).getDescTxt();

    		PageManagementActionUtil util = new PageManagementActionUtil();
    		CachedDropDownValues cdv = new CachedDropDownValues();
        	HashMap<Object,Object> pgMap = new HashMap<Object,Object>();
        	ArrayList<Object> pageList = new ArrayList<Object> ();
        	HttpSession session  = request.getSession();
        	Long newWaTemplateUid=null;

    		try {

    			//PageManagementProxyVO pmProxyVO = new PageManagementProxyVO();
    			newWaTemplateUid =  util.saveAsTemplate(waTemplateUid,templateNm,adminComments, session);
           	}
           	catch(Exception e) {
           		logger.error("Error in saving the Templte " +e.getMessage());
           		throw new ServletException(e.getMessage());
           	}
           	try{
        		pageList = (ArrayList)util.getPageSummaries(session);
    	    	if(pageList != null && pageList.size()>0){
    	    		 for (Object singleObj : pageList) {
    	    			WaTemplateDT dt = (WaTemplateDT)singleObj;
    	    			if(dt.getBusObjType().equals("INV"))
    	    				dt.setBusObjType("INVESTIGATION");
    	    			if(dt.getTemplateNm() != null){
    	    				//dt.setConditionCdDesc(cdv.getConditionDesc(dt.getConditionCd()));
    	    				pgMap.put(dt.getWaTemplateUid(),dt.getTemplateNm());
    	    			}

    	    			util.makePageLibraryLink(dt, "VIEW");
    	    			util.makePageLibraryLink(dt, "EDIT");

    	    		 }


    	    	}
    	    	//update pgMap with the new template
    	    	pgMap.put(newWaTemplateUid,templateNm);
    	    	session.setAttribute("pgMap", pgMap);
    	    	}catch(Exception e){
        		logger.error("Error while getting the page Library");
        		throw new ServletException(e.getMessage());
        	}
    	    	ActionMessages messages = new ActionMessages();
    	    	messages.add(NBSPageConstants.SUCCESS_MESSAGES_PROPERTY,
    					new ActionMessage(NBSPageConstants.CREATE_TEMPLATE_SUCCESS_MESSAGE_KEY, templateNm +" "+NBSPageConstants.INVESTIGATION_TEXT));
    			request.setAttribute("success_messages", messages);
    	    pbForm.setPageList(pageList);
    	    request.setAttribute("manageFormList", pageList);
            request.setAttribute("PageTitle", "Manage Pages:Page Library");
            request.getSession().setAttribute("waTemplateUid", waTemplateUid);
            request.getSession().setAttribute("from","SAT");
            request.getSession().setAttribute("templateNm",templateNm);
           // request.getSession().setAttribute("pgPageName",templateNm);
    	}catch(Exception ex){
    		logger.error("Error in the action class in saveAsTemplate method : " + ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}
            return mapping.findForward("previewPage");

    }

    public ActionForward rulesListLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	logger.debug("Begin ManagePageAction View Rules List Load ");

    	request.setAttribute("PageTitle", "Manage Rules List");
        return mapping.findForward("rulesListLoad");
    }

    public ActionForward createNewDraft(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {

    	PageManagementActionUtil util = new PageManagementActionUtil();
    	HttpSession session  = request.getSession();
    	Long waTemplateUid = null;
    	Long newWaTemplateUid = null;

        try{

	    	if(request.getParameter("waTemplateUid") != null){
	    		waTemplateUid= (Long)Long.parseLong((request.getParameter("waTemplateUid")));
	    		request.getSession().setAttribute("from","CNP");
	    	}

	    	else
	    		waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
	    	String createDraftInd = request.getParameter("createDraftInd")!=null? request.getParameter("createDraftInd"):"";
	    	if(createDraftInd.equals("PUB"))
	    		request.getSession().setAttribute("from","CNP");
	    	else if (createDraftInd.equals("DRT"))
	    		request.getSession().setAttribute("from","CN");

	    	/*
	    	 *  Creating a new draft from the published version of a page
	    	 */
	    	try{
	    		newWaTemplateUid = util.createNewDraft(waTemplateUid, session);
	    	}catch(Exception e) {
	    		logger.error("Error in creating the new Draft .. Backend error:" +e.getMessage());
	    		throw new ServletException(e.getMessage());
	    	}
	       // request.getSession().setAttribute("oldWaTemplateUid", waTemplateUid);
	    	request.getSession().setAttribute("waTemplateUid", newWaTemplateUid);
	        return mapping.findForward("previewPage");
        }catch(Exception ex){
        	logger.error("Error in getting the new draft Draft in ManagePageAction class " +ex.getMessage());
        	throw new ServletException(ex.getMessage());
        }


    }


	private void sortPageLibarary(PageBuilderForm manageForm, Collection<Object>  codesetList, boolean existing, HttpServletRequest request) throws Exception {

		// Retrieve sort-order and sort-direction from display tag parameters
		String sortMethod = getSortMethod(request, manageForm);
		String direction = getSortDirection(request, manageForm);

		boolean invDirectionFlag = true;
		if (direction != null && direction.equals("2"))
			invDirectionFlag = false;

		NedssUtils util = new NedssUtils();

		//Read from properties file to determine default sort order
		if (sortMethod == null || (sortMethod != null && sortMethod.equals("none"))) {
			sortMethod = "getTemplateNm";
			invDirectionFlag = true;
		}

		if (sortMethod != null && codesetList != null && codesetList.size() > 0) {
			util.sortObjectByColumnGeneric(sortMethod,(Collection<Object>) codesetList, invDirectionFlag);
		}
		if(!existing) {
			//Finally put sort criteria in form
			String sortOrderParam = PageManagementActionUtil.getSortPageLibrary(invDirectionFlag == true ? "1" : "2", sortMethod);
			Map<Object, Object> searchCriteriaColl = new TreeMap<Object, Object>();
			searchCriteriaColl.put("sortSt", sortOrderParam);
			manageForm.getAttributeMap().put("searchCriteria", searchCriteriaColl);
		}
	}

	private String getSortMethod(HttpServletRequest request, PageBuilderForm manageForm ) {
		if (PaginationUtil._dtagAccessed(request)) {
			return request.getParameter((new ParamEncoder("parent")).encodeParameterName(TableTagParameters.PARAMETER_SORT));
	} else{
		return manageForm.getAttributeMap().get("methodName") == null ? null : (String) manageForm.getAttributeMap().get("methodName");
		}

	}
	private String getSortDirection(HttpServletRequest request, PageBuilderForm manageForm) {
		if (PaginationUtil._dtagAccessed(request)) {
			return request.getParameter((new ParamEncoder("parent")).encodeParameterName(TableTagParameters.PARAMETER_ORDER));
		} else{
			return manageForm.getAttributeMap().get("sortOrder") == null ? "1": (String) manageForm.getAttributeMap().get("sortOrder");
		}

	}

	public ActionForward filterPageLibrarySubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

		PageBuilderForm manageForm  = (PageBuilderForm) form;
		Collection<Object>  waTemplateDtColl = filterPageLibrary(manageForm, request);

		if(waTemplateDtColl != null){
			request.setAttribute("ActionMode", "Search");
		//When all filters are applied (equivalent to no filter applied), Collection<Object>  is set to null in which case retrieve it from Form.
		}else{
			waTemplateDtColl = (ArrayList<Object> ) manageForm.getWaTemplateDTColl();
		}
		manageForm.getAttributeMap().put("managePageList",waTemplateDtColl);
		manageForm.getAttributeMap().put("queueList",waTemplateDtColl);
		request.setAttribute("queueList", waTemplateDtColl);
		sortPageLibarary(manageForm, waTemplateDtColl, true, request);
		request.setAttribute("manageFormList", waTemplateDtColl);
		request.setAttribute("queueCount", String.valueOf(waTemplateDtColl.size()));
		manageForm.getAttributeMap().put("queueCount", String.valueOf(waTemplateDtColl.size()));
		request.setAttribute("PageTitle","Manage Pages: Page Library");
		manageForm.getAttributeMap().put("PageNumber", "1");

		return PaginationUtil.paginate(manageForm, request, "listAllPages",mapping);

	}

	private Collection<Object>  filterPageLibrary(PageBuilderForm manageForm, HttpServletRequest request) throws Exception {

		Collection<Object>  manageCondlist = new ArrayList<Object> ();


		String sortOrderParam = null;
		try {

			Map<Object, Object> searchCriteriaMap = manageForm.getSearchCriteriaArrayMap();
			ArrayList<Object> waTemplateDtColl = (ArrayList<Object> ) manageForm.getWaTemplateDTColl();

			manageCondlist = getFilteredCodesetLib(waTemplateDtColl, searchCriteriaMap,manageForm);
			//EVENTTYPE LASTUPDATED LASTUPDATEDBY STATUS REQUIRESPORTING

			String[] lastUpdate = (String[]) searchCriteriaMap.get("LASTUPDATED");
			String[] lastUpdateBy = (String[]) searchCriteriaMap.get("LASTUPDATEDBY");
			String[] status = (String[]) searchCriteriaMap.get("STATUS");
			String[] busObjType = (String[]) searchCriteriaMap.get("BUSOBJTYPE");

			Integer lastUpdatedCount = new Integer(lastUpdate == null ? 0 : lastUpdate.length);
			Integer lastUpdatedByCount = new Integer(lastUpdateBy == null ? 0 : lastUpdateBy.length);
			Integer statusCount = new Integer(status == null ? 0 : status.length);
			Integer busObjTypeCount = new Integer(busObjType == null ? 0 : busObjType.length);

			// Do not filter if the selected values for filter is same as filtered list, but put the sortMethod, direction and criteria stuff
			if(lastUpdatedCount.equals(manageForm.getAttributeMap().get("lastUpdate")) &&
					lastUpdatedByCount.equals((manageForm.getAttributeMap().get("lastUpdateBy"))) &&
					statusCount.equals((manageForm.getAttributeMap().get("status"))) &&
					busObjTypeCount.equals((manageForm.getAttributeMap().get("busObjType"))))
				 {

				String sortMethod = getSortMethod(request, manageForm);
				String direction = getSortDirection(request, manageForm);
				if(sortMethod == null || (sortMethod != null && sortMethod.equals("none"))) {
					Map<?,?> sColl =  manageForm.getAttributeMap().get("searchCriteria") == null ? new TreeMap<Object, Object>() : (TreeMap<?,?>) manageForm.getAttributeMap().get("searchCriteria");
					sortOrderParam = sColl.get("sortSt") == null ? "" : (String) sColl.get("sortSt");
				} else {
					sortOrderParam = PageManagementActionUtil.getSortPageLibrary(direction, sortMethod);
				}
				Map<Object, Object> searchCriteriaColl = new TreeMap<Object, Object>();
				searchCriteriaColl.put("sortSt", sortOrderParam);
				manageForm.getAttributeMap().put("searchCriteria", searchCriteriaColl);
				return null;
			}

			ArrayList<Object> lastUpdatedList = manageForm.getLastUpdated();
			ArrayList<Object> lastUpdatedByList = manageForm.getLastUpdatedBy();
			ArrayList<Object> statusList = manageForm.getStatus();
			ArrayList<Object> busObjTypeList = manageForm.getEventType();

			Map<Object, Object> searchCriteriaColl = new TreeMap<Object, Object>();
			String sortMethod = getSortMethod(request, manageForm);
			String direction = getSortDirection(request, manageForm);
			if(sortMethod == null || (sortMethod != null && sortMethod.equals("none"))) {
				Map<Object, Object> sColl =  manageForm.getAttributeMap().get("searchCriteria") == null ? new TreeMap<Object, Object>() : (TreeMap<Object, Object>) manageForm.getAttributeMap().get("searchCriteria");
				sortOrderParam = sColl.get("sortSt") == null ? "" : (String) sColl.get("sortSt");
			} else {
				sortOrderParam = PageManagementActionUtil.getSortPageLibrary(direction, sortMethod);
			}

			String srchCriteriaEventType = null;
			String srchCriteriaLastUpdate = null;
			String srchCriteriaLastUpdatedBy = null;
			String srchCriteriaStatus = null;
			String srchCriteriaTemplateNm = null;
			String srchCriteriaRelatedConditions = null;

			srchCriteriaLastUpdate = queueUtil.getSearchCriteria(lastUpdatedList, lastUpdate, NEDSSConstants.FILTERBYDATE);
			srchCriteriaLastUpdatedBy = queueUtil.getSearchCriteria(lastUpdatedByList, lastUpdateBy, NEDSSConstants.FILTERBYSUBMITTEDBY);
			srchCriteriaStatus = queueUtil.getSearchCriteria(statusList, status, NEDSSConstants.FILTERBYSUBMITTEDBY);
			srchCriteriaEventType = queueUtil.getSearchCriteria(busObjTypeList, busObjType, NEDSSConstants.FILTERBYTYPE);
			if(searchCriteriaMap.get("SearchText1_FILTER_TEXT")!=null){
				srchCriteriaTemplateNm = (String) searchCriteriaMap.get("SearchText1_FILTER_TEXT");
			}
			if(searchCriteriaMap.get("SearchText2_FILTER_TEXT")!=null){
				srchCriteriaRelatedConditions = (String) searchCriteriaMap.get("SearchText2_FILTER_TEXT");
			}
			//set the error message to the form
			if(sortOrderParam != null)
				searchCriteriaColl.put("sortSt", sortOrderParam);
			if(srchCriteriaEventType != null)
				searchCriteriaColl.put("INV666", srchCriteriaEventType);
			if(srchCriteriaLastUpdate != null)
				searchCriteriaColl.put("INV222", srchCriteriaLastUpdate);
			if(srchCriteriaLastUpdatedBy != null)
				searchCriteriaColl.put("INV333", srchCriteriaLastUpdatedBy);
			if(srchCriteriaStatus != null)
				searchCriteriaColl.put("INV444", srchCriteriaStatus);
			if(srchCriteriaTemplateNm != null)
				searchCriteriaColl.put("INV111", srchCriteriaTemplateNm);
			if(srchCriteriaRelatedConditions != null)
				searchCriteriaColl.put("INV555", srchCriteriaRelatedConditions);

			manageForm.getAttributeMap().put("searchCriteria", searchCriteriaColl);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while filtering the filterQuestionLib: "+ e.toString());
			throw new Exception(e.getMessage());

		}
		return manageCondlist;
	}

	public Collection<Object>  getFilteredCodesetLib(Collection<Object>  waTemplateDtColl,
			Map<Object, Object> searchCriteriaMap,PageBuilderForm manageForm) throws Exception{

      try{
    	String[] lastUpdated = (String[]) searchCriteriaMap.get("LASTUPDATED");
    	String[] lastUpdatedBy = (String[]) searchCriteriaMap.get("LASTUPDATEDBY");
		String[] status = (String[]) searchCriteriaMap.get("STATUS");
		String[] busObjType = (String[]) searchCriteriaMap.get("BUSOBJTYPE");
		String filterByTemplateNmText = null;
		String filterByRelatedCondintionsText = null;
		if(searchCriteriaMap.get("SearchText1_FILTER_TEXT")!=null){
			filterByTemplateNmText = (String) searchCriteriaMap.get("SearchText1_FILTER_TEXT");
		}
		if(searchCriteriaMap.get("SearchText2_FILTER_TEXT")!=null){
			filterByRelatedCondintionsText = (String) searchCriteriaMap.get("SearchText2_FILTER_TEXT");
		}

		Map<Object, Object> lastUpdatedMap = new HashMap<Object,Object>();
		Map<Object, Object> lastUpdatedByMap = new HashMap<Object,Object>();
		Map<Object, Object> statusMap = new HashMap<Object,Object>();
		Map<Object, Object> busObjTypeMap = new HashMap<Object,Object>();
	    try{

			if (lastUpdated != null && lastUpdated.length >0)
				lastUpdatedMap = queueUtil.getMapFromStringArray(lastUpdated);

			if (lastUpdatedBy != null && lastUpdatedBy.length >0)
				lastUpdatedByMap = queueUtil.getMapFromStringArray(lastUpdatedBy);

			if (status != null && status.length >0)
				statusMap = queueUtil.getMapFromStringArray(status);

			if (busObjType != null && busObjType.length >0)
				busObjTypeMap = queueUtil.getMapFromStringArray(busObjType);

			/**
			 * Following methods are helping for page sorting
			 */
			PageManagementActionUtil pageActionUtil = new PageManagementActionUtil();
			if(lastUpdated != null && lastUpdatedMap != null && lastUpdatedMap.size()>0){
				waTemplateDtColl = pageActionUtil.filterLastUpdate(waTemplateDtColl, lastUpdatedMap);
			}

			if(lastUpdatedBy != null && lastUpdatedByMap != null && lastUpdatedByMap.size()>0){
				waTemplateDtColl = pageActionUtil.filterLastUpdateBy(waTemplateDtColl, lastUpdatedByMap);
			}

			if (status != null && statusMap != null && statusMap.size()>0)
				waTemplateDtColl = pageActionUtil.filterStatus(waTemplateDtColl, statusMap);

			if (busObjType != null && busObjTypeMap != null && busObjTypeMap.size()>0)
				waTemplateDtColl = pageActionUtil.filterBusObjType(waTemplateDtColl, busObjTypeMap);

			if(filterByTemplateNmText!= null){
				waTemplateDtColl = pageActionUtil.filterByText(waTemplateDtColl, filterByTemplateNmText, NEDSSConstants.TEMPLATE_NM);
			}
			if(filterByRelatedCondintionsText!= null){
				waTemplateDtColl = pageActionUtil.filterByText(waTemplateDtColl, filterByRelatedCondintionsText, NEDSSConstants.RELATED_CONDITIONS);
			}
	       }catch (Exception e) {
				e.printStackTrace();
				logger.error("Error while filtering the getFilteredCodesetLib: "+ e.toString());
				throw new Exception(e.getMessage());

	        }
        }catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while filtering the getFilteredCodesetLib: "+ e.toString());
			throw new Exception(e.getMessage());

		}

		return waTemplateDtColl;

	}

	 /**
     * Return to a view where the user can edit the contents (tabs, sections,
     * subsections) of the page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewHistoryPopUpLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
    	//pageBuildForm.setActionMode(NEDSSConstants.EDIT_LOAD_ACTION);
    	PageManagementActionUtil util = new PageManagementActionUtil();
    	HttpSession session  = request.getSession();
    	String  pageNm = "";
    	Collection<Object> pagehistList = new ArrayList<Object> ();
    	//boolean isSavedtoDB = false;
    	// form the list?
    	if (request.getAttribute("pgPageName") != null)
    		pageNm = request.getAttribute("pgPaqeNm").toString();
    	else if(request.getSession().getAttribute("pgPageName") != null)
    		pageNm= request.getSession().getAttribute("pgPageName").toString();

    	PageManagementProxyVO pmProxyVO = new PageManagementProxyVO();

    	try {
    		pagehistList = (Collection<Object>)util.getPageHistory(pageNm,session);

    	}
    	catch(Exception e) {
    		logger.error("Error in getting the Page History for  " +pageNm + e.getMessage());
    		throw new ServletException(e.getMessage());
    	}

    	request.setAttribute("pageHistoryList", pagehistList);
    	// set title for current condition being edited
    //	request.setAttribute("pageConditionTitle", CachedDropDowns.getConditionDesc(pmProxyVO.getWaTemplateDT().getConditionCd()));

    	request.setAttribute("PageTitle", "Manage Pages");
        return mapping.findForward("viewPageHistory");
    }




    /**
     * View Page Details Load
     * @param mapping
     * @param form
     * @param requestad
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewPageDetailsLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception{

    	try{
	    	Long waTemplateUid = (Long)request.getAttribute("waTemplateUid");
	    	if (waTemplateUid == null)
	    	 	waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
	    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
	    	//clear lists in the form
    		if (pageBuildForm.getSelectedCondList() != null)
    			pageBuildForm.getSelectedCondList().clear();
    		if (pageBuildForm.getRelatedConditions() != null)
    			pageBuildForm.getRelatedConditions().clear();
    		if (pageBuildForm.getSelectedConditionCodes() != null)
    			pageBuildForm.setSelectedConditionCodes(new String[0]);

	    	WaTemplateVO waTemplateVO = new WaTemplateVO();
	    	PageManagementActionUtil util = new PageManagementActionUtil();
	    	HttpSession session = request.getSession();
	    	try{
	    		waTemplateVO = util.getPageDetails(waTemplateUid, session);
	    	}catch(Exception e){
	    		logger.error("Error in calling the getPageDetails ", e.getMessage());
	    		throw new ServletException(e.getMessage());
	    	}
	    	//setting the request Attribute for port Ind
	    	setMsgForPortInd(waTemplateVO,request);
	    	pageBuildForm.setSelection(waTemplateVO);
	    	Collection<Object> pageCondMapDTColl = waTemplateVO.getWaPageCondMappingDTColl();
	    	pageBuildForm.setWaPageCondMappingDTColl(pageCondMapDTColl);
	    	pageBuildForm.setActionMode(NEDSSConstants.VIEW_LOAD_ACTION);
	    	request.setAttribute("conditionList", (ArrayList)waTemplateVO.getWaPageCondMappingDTColl());
    		request.setAttribute("waTemplateUid",waTemplateUid);
	    	request.setAttribute("PageTitle", "Manage Pages: View Page Details");
	    	request.setAttribute("templateType", waTemplateVO.getWaTemplateDT().getTemplateType());
	        request.setAttribute("mode", request.getParameter("mode"));  // for print
	        
	        boolean showCloneButton = true;
	        //Hide clone button for Vaccination page
	        if(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equals(waTemplateVO.getWaTemplateDT().getBusObjType())){
	        	showCloneButton = false;
	        }
	        request.setAttribute("showCloneButton", showCloneButton);
	        
	        return mapping.findForward("viewPageDetails");
    	}catch(Exception e){
    		logger.error("Unexpected error in getPageDetails ", e.getMessage());
    		throw new ServletException(e.getMessage());
    	}
    }


    /**
     * Edit Page Details Load
     *
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward editPageDetailsLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception{

    	try{
	    	Long waTemplateUid = (Long)request.getAttribute("waTemplateUid");
	    	if (waTemplateUid == null)
	    	 	waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
	    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
	    	pageBuildForm.clearAll();
    		//pageBuildForm.setSelectedCondList(null);  //clear list in form
     		pageBuildForm.setSelectedConditionCodes(null); //clear selected codes
     		request.getSession().removeAttribute("from"); //clear success msg attrib
	    	WaTemplateVO waTemplateVO = new WaTemplateVO();

	    	PageManagementActionUtil util = new PageManagementActionUtil();
	    	HttpSession session = request.getSession();
	    	// Call getPage Details method here.
	    	try{
	    		waTemplateVO = util.getPageDetails(waTemplateUid, session);
	    	}catch(Exception e){
	    		logger.error("Error in calling the getPageDetails in editPageDetailsLoad ", e.getMessage());
	    		throw new ServletException(e.getMessage());
	    	}
	    	//set selection to the VO
	    	pageBuildForm.setSelection(waTemplateVO);


	    	Collection<Object> pageCondMapDTColl = waTemplateVO.getWaPageCondMappingDTColl();
    		// String[] starraySelectedConditionCodes = pageBuildForm.getSelectedConditionCodes();
    		ArrayList<Object> ddCodeDTColl = new ArrayList<Object>();
    		pageBuildForm.setSelectedCondList(ddCodeDTColl);  //clear list
    		//if never published..
	    	if (waTemplateVO.getWaTemplateDT().getPublishVersionNbr() == null) {
		    	if(pageCondMapDTColl != null){
		    		Iterator<Object> iter = pageCondMapDTColl.iterator();
		    		while(iter.hasNext()){
		    			PageCondMappingDT dt = (PageCondMappingDT)iter.next();
		    			DropDownCodeDT dDownDT = new DropDownCodeDT();
		    			dDownDT.setKey(dt.getConditionCd());
		    			dDownDT.setValue(dt.getConditionDesc());
		    			ddCodeDTColl.add(dDownDT);
		    		}
		    	}
	    		pageBuildForm.setSelectedCondList(ddCodeDTColl);
    		} else //page has been published..
		    	request.setAttribute("conditionList", (ArrayList)waTemplateVO.getWaPageCondMappingDTColl());

	    	pageBuildForm.setActionMode(NEDSSConstants.EDIT_LOAD_ACTION);
	    	request.setAttribute("PageTitle", "Manage Pages: Edit Page Details");
	        return mapping.findForward("editPageDetails");
    	}catch(Exception e){
    		throw new ServletException(e.getMessage());
    	}
    }

    /**
     * Edit page details submit return to the view mode of this
     * submitted page. If the page has not been published, the user can
     * edit the page name and/or remove associated conditions. If the page has
     * been published, the user can not edit the page name or remove conditions
     * but the user can add conditions. <br/>
     * <b> Note: </b> The action form is not populated during this call as the information
     * that is used to submit the data is present in session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward editPageDetailsSubmit(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
	    	Long waTemplateUid = null;
	    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
        	pageBuildForm.setActionMode(NEDSSConstants.EDIT_SUBMIT_ACTION);
        	WaTemplateVO waTemplateVO = (WaTemplateVO)pageBuildForm.getSelection();
        	PageManagementActionUtil util = new PageManagementActionUtil();
        	WaTemplateDT waTemplateDT = waTemplateVO.getWaTemplateDT();
        	waTemplateUid = waTemplateDT.getUid();
	    	if (waTemplateUid == null) {  //gt: check SQL why null
	    	 	waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
	    	 	waTemplateDT.setWaTemplateUid(waTemplateUid);
	    	}

        	//if not yet published, can change page name..
	    	//check if the page name exists on another page..
	    	if (waTemplateDT.getPublishVersionNbr() == null) {
	    		ArrayList<Object> errorMsgs = util.validatePageNmUniqueness(waTemplateDT.getTemplateNm(), waTemplateUid,waTemplateDT.getTemplateType());
	    		if (!errorMsgs.isEmpty()){
	    			Iterator iter = errorMsgs.iterator();
	    			request.setAttribute("EditDetailError",(String) iter.next());
	    			//populate selected list (if any) so right side shows up
	    			String[] strArr = pageBuildForm.getSelectedConditionCodes();
	    			ArrayList<Object> selCondList = pageBuildForm.getSelectedCondList();
	    			selCondList.clear();
	    			for (int i = 0; strArr != null && i < strArr.length; ++i) {
	    				iter = pageBuildForm.getConditionAllListByBO(waTemplateDT.getBusObjType()).iterator();
	    				while (iter.hasNext()) {
	    					DropDownCodeDT dDownDT = (DropDownCodeDT) iter.next();
	    					if (dDownDT.getKey().equalsIgnoreCase(strArr[i])) {
	    						DropDownCodeDT theDropDownDT = new DropDownCodeDT();
	    						theDropDownDT.setKey(dDownDT.getKey());
	    						theDropDownDT.setValue(dDownDT.getValue());
	    						selCondList.add(theDropDownDT);
        					break;
        			    }
        		    }
        	    }
        		return mapping.findForward("editPageDetails");
	    		} //end if errorMsgs duplicate page name
	    	}//if not yet published

        	waTemplateVO.setItNew(false);
        	waTemplateVO.setItDirty(true);

        	waTemplateDT  = util.setTheValuesForWaTemplateDT(waTemplateDT, request, NEDSSConstants.EDIT_SUBMIT_ACTION);

        	Collection<Object> pageCondMappDTColl = new ArrayList<Object>();
        	if (pageBuildForm.getSelectedConditionCodes()!= null) //can be no conditions selected at this time..
        	{
        		String[] strArr = pageBuildForm.getSelectedConditionCodes();
        		for (int i=0; i<strArr.length; i++) {
        			String  result = strArr[i] ;
        			//place condition code in the waTemplateDT for display purposes
        			if (result != null && !result.isEmpty())
        				waTemplateVO.getWaTemplateDT().setConditionCd(result);

        			PageCondMappingDT pageCondMapdt = new PageCondMappingDT();
        			util.setTheValuesForPageCondMappingDT(pageCondMapdt, request, NEDSSConstants.CREATE_SUBMIT_ACTION);
        			pageCondMapdt.setConditionCd(result);
        			pageCondMapdt.setWaTemplateUid(waTemplateDT.getWaTemplateUid());
        			pageCondMappDTColl.add(pageCondMapdt);
        		}
         	}//if not published could be no conditions
        	//
       		if (waTemplateVO.getWaTemplateDT().getPublishVersionNbr() != null) {
    			//add existing back in
    			if (waTemplateVO.getWaPageCondMappingDTColl() != null)
    					pageCondMappDTColl.addAll(waTemplateVO.getWaPageCondMappingDTColl());
    			else
    					logger.error("Published page has no conditions???");
    		}

        		//overlay existing regardless - could go from one to zero
        	waTemplateVO.setWaPageCondMappingDTColl(pageCondMappDTColl) ;


        	// if not yet published - template name could have changed
        	// so update the form code
        	if (waTemplateVO.getWaTemplateDT().getPublishVersionNbr() == null) {
        		String templateNm = waTemplateVO.getWaTemplateDT().getTemplateNm();
        		String pgFormCode = getPgFormCodeFromPageName(templateNm, waTemplateVO.getWaTemplateDT().getBusObjType());
        		waTemplateVO.getWaTemplateDT().setFormCd(pgFormCode);
        	}

	        try{
		        PageManagementActionUtil pmaUtil = new PageManagementActionUtil();
		        pmaUtil.updatePageDetails(waTemplateVO);
	        }catch(Exception ex){
	        	logger.error("Error in updating Page Details from the backend call : "+ex.getMessage());
	        	throw new ServletException(ex.getMessage());
	        }

	        //confirmation message
	    	StringBuffer sb = new StringBuffer();
			sb.append("The page details for <b>").append(waTemplateVO.getWaTemplateDT().getTemplateNm()).append("</b> page have been successfully updated.");
			request.setAttribute("ConfirmMesg", sb.toString());
			//request.getSession().setAttribute("from","E");

			//if never published - page name could have changed...
			if (waTemplateDT.getPublishIndCd()== null || (waTemplateDT.getPublishIndCd() != null && waTemplateDT.getPublishIndCd().equals(NEDSSConstants.FALSE))) {
				//reset the pgPageName in case the user changed the name
				String pgPageName = waTemplateDT.getTemplateNm();
				request.setAttribute("pgPageName", pgPageName);
				request.getSession().setAttribute("pgPageName", pgPageName);

				//update pgMap in case page name was changed
				if(request.getSession().getAttribute("pgMap")!=null){
					HashMap<Object,Object> pgMap = (HashMap <Object,Object>)request.getSession().getAttribute("pgMap");
					pgMap.put(waTemplateDT.getWaTemplateUid(),pgPageName);
					request.getSession().setAttribute("pgMap", pgMap);
				}
			}
	        //back to the view page
	    	pageBuildForm.setActionMode(NEDSSConstants.VIEW_LOAD_ACTION);
	        return mapping.findForward("viewPageDetailsLoad");
			// return mapping.findForward("viewPageDetails");

    	}catch(Exception ex){
    		logger.error("Error in Edit Page Details Submit in Manage Page Action class: "+ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}

    }

    /**
     * Clone Page Load
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward clonePageLoad(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception{

    	try{
	    	Long waTemplateUid = (Long)request.getSession().getAttribute("waTemplateUid");
	    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;
	    	WaTemplateVO waTemplateVO = new WaTemplateVO();
	    	// Call getPage Details method here.
	    	PageManagementActionUtil util = new PageManagementActionUtil();
	    	HttpSession session = request.getSession();

	    	try{
	    		waTemplateVO = util.getPageDetails(waTemplateUid, session);
	    	}catch(Exception e){
	    		logger.error("Error in calling the getPageDetails for Clone ", e.getMessage());
	    		throw new ServletException(e.getMessage());
	    	}
	    	// Setting the template list
	    	/*DropDownCodeDT dropDownCodeDT = new DropDownCodeDT();
	    	dropDownCodeDT.setKey(waTemplateUid.toString());
	    	dropDownCodeDT.setValue(waTemplateVO.getWaTemplateDT().getTemplateNm());
	    	ArrayList<Object> templateList =  new ArrayList<Object>();
	    	templateList.add(dropDownCodeDT);
	    	pageBuildForm.setTemplateList(templateList);*/

	    //	waTemplateVO.getWaTemplateDT().setTemplateNm(waTemplateVO.getWaTemplateDT().getPageNm());
	    	waTemplateVO.getWaTemplateDT().setMessageId(null);

	    	if(waTemplateVO.getWaTemplateDT().getPageNm() == null || (waTemplateVO.getWaTemplateDT().getPageNm() != null && waTemplateVO.getWaTemplateDT().getPageNm().equals(""))){
	    		waTemplateVO.getWaTemplateDT().setPageNm(waTemplateVO.getWaTemplateDT().getTemplateNm());
	    	    waTemplateVO.getWaTemplateDT().setTemplateNm(null);
	    	    
    		}
		    	waTemplateVO.getWaTemplateDT().setDescTxt(null);
		    	waTemplateVO.getWaTemplateDT().setWaTemplateRefUid(waTemplateUid);
		    	pageBuildForm.setSelection(waTemplateVO);
		    	Collection<Object> pageCondMapDTColl = waTemplateVO.getWaPageCondMappingDTColl();
		    	ArrayList<Object> ddCodeDTColl = new ArrayList<Object>();
			    	DropDownCodeDT dDownDT1 = new DropDownCodeDT();
		    		//dDownDT1.setKey("");
		    		//dDownDT1.setValue(" ");
		    		//ddCodeDTColl.add(dDownDT1);
		    	if(pageCondMapDTColl != null){
		    		Iterator<Object> iter = pageCondMapDTColl.iterator();
		    		while(iter.hasNext()){
		    			PageCondMappingDT dt = (PageCondMappingDT)iter.next();
		    			DropDownCodeDT dDownDT = new DropDownCodeDT();
		    			dDownDT.setKey(dt.getConditionCd());
		    			dDownDT.setValue(dt.getConditionDesc());
		    			ddCodeDTColl.add(dDownDT);
		    		}
		    	}
		    	pageBuildForm.setConditionList(ddCodeDTColl);

		    	pageBuildForm.setActionMode(NEDSSConstants.CLONE_LOAD_ACTION);
		    	request.setAttribute("PageTitle", "Manage Pages: Clone Page");
		        return mapping.findForward("clonePage");
	    	}catch(Exception e){
	    		logger.error("Error in clonePageLoad method in ManagePageAction"+e.getMessage());
	    		throw new ServletException(e.getMessage());
	    	}
	    }

    
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward loadManagePagePort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		

			
    	try{
			PageBuilderForm pageBuildForm = (PageBuilderForm)form;
			Collection<Object> portPageList= new ArrayList<Object>();
			QueueDT queueDT = fillQueueDT();
			pageBuildForm.setQueueDT(queueDT);
			ArrayList<QueueColumnDT> queueCollection = genericQueueUtil.convertQueueDTToList(queueDT);
			pageBuildForm.setQueueCollection(queueCollection);
	    	String queueString = genericQueueUtil.convertToString(queueDT);
	    	pageBuildForm.setStringQueueCollection(queueString);
	    	request.setAttribute("queueCollection",queueCollection);
	    	request.setAttribute("stringQueueCollection",queueString);
	    	
			boolean initLoad = request.getParameter("initLoad") == null ? false: Boolean.valueOf(request.getParameter("initLoad")).booleanValue();
			String context = request.getParameter("context") == null ? null: (String)request.getParameter("context");
			boolean forceEJBcall = false;
			
			if(initLoad && !PaginationUtil._dtagAccessed(request)){
	    		pageBuildForm.clearAll();
	    		request.setAttribute("ActionMode", "InitLoad");	// selects all the check boxes.
	    		forceEJBcall = true;
			}
			if(context != null){
	    		forceEJBcall = true;
    	    }
			
			GenericForm genericForm= translateFromObservationSummaryDisplayVOToGenericSummaryDisplayVO(pageBuildForm);
			if(forceEJBcall){
				Object[] oParams = new Object[] {};
				String sBeanJndiName = JNDINames.PORT_PAGE_PROXY_EJB;
				String sMethod = "getPortPageList";
				Object portPageListObj = CallProxyEJB.callProxyEJB(oParams, sBeanJndiName, sMethod, request.getSession());
				portPageList = (ArrayList) portPageListObj;
				
				
				ArrayList<Long> lockedMappingUids = new ArrayList<Long>();
				for(Iterator<Object> iterator = portPageList.iterator(); iterator.hasNext();){
					ManagePageDT pageDT = (ManagePageDT) iterator.next();
					if(PortPageUtil.NBS_PAGE_MAPPING_STATUS_COMPLETE.equals(pageDT.getMappingStatusCd())){
						//For legacy pages (i.e. Vaccinations) condition code does not exist in condition_code table so condition description is not available. Setting condition_cd as condition description.
						if(pageDT.getConditionDescText() == null){
							pageDT.setConditionDescText(pageDT.getConditionCd());
						}
						//URL to jump to Port Condition screen
						pageDT.setConditionDescWithLink("<a href=/nbs/PortPage.do?method=viewPageMapping&nbsConversionPageMgmtUid="+pageDT.getNbsConversionPageMgmtUid()+"&fromPageWaTemplateUid="+pageDT.getFromPageWaTemplateUid()+"&toPageWaTemplateUid="+pageDT.getToPageWaTemplateUid()+"&mapName="+java.net.URLEncoder.encode(pageDT.getMapName())+"&conditionCd="+java.net.URLEncoder.encode(pageDT.getConditionCd())+"&context="+PortPageUtil.CONTEXT_LOCK_MAPPING+"&fromPageFormCd="+pageDT.getFromPageFormCd()+">"+pageDT.getConditionDescText()+"</a> ");
						
						if(!lockedMappingUids.contains(pageDT.getNbsConversionPageMgmtUid()))
							lockedMappingUids.add(pageDT.getNbsConversionPageMgmtUid());
						pageDT.setMappingLocked(true);
					}else{
						//For in-progress status do not show Port Date
						pageDT.setLastChgTime(null);
						if(PortPageUtil.NBS_PAGE_MAPPING_STATUS_PORTING_IN_PROGRESS.equals(pageDT.getMappingStatusCd())){//After porting 1st case lock mapping
							pageDT.setMappingLocked(true);
							
							if(!lockedMappingUids.contains(pageDT.getNbsConversionPageMgmtUid()))//to lock same mapping for other conditions.
								lockedMappingUids.add(pageDT.getNbsConversionPageMgmtUid());
						}
					}
					
					//In case of Legacy to PB conversion it set FromPageFormCd as FromPageName
					if(pageDT.getFromPageName()==null){
						pageDT.setFromPageName(pageDT.getFromPageFormCd());
					}
				}
				// For Ported Condition's NBSConversionPageMgmtUid lock all related conditions mapping view icon.
				for(int i=0;i<lockedMappingUids.size();i++){
					Long lockedMappingUid = lockedMappingUids.get(i);
					for(Iterator<Object> iterator = portPageList.iterator(); iterator.hasNext();){
						ManagePageDT pageDT = (ManagePageDT) iterator.next();
						if(pageDT!=null && lockedMappingUid!=null && PortPageUtil.NBS_PAGE_MAPPING_STATUS_MAPPING_IN_PROGRESS.equals(pageDT.getMappingStatusCd()) && pageDT.getNbsConversionPageMgmtUid()!=null && pageDT.getNbsConversionPageMgmtUid().longValue()==lockedMappingUid.longValue()){
							pageDT.setMappingLocked(true);
						}
					}
				}
				
				pageBuildForm.getAttributeMap().put("portPageList",portPageList);
				pageBuildForm.setPortPageList(portPageList);
				pageBuildForm.initializeDropDowns(portPageList);
				Map<Object, Object> map = genericQueueUtil.setQueueCount(genericForm, genericForm.getQueueDT());
				pageBuildForm.setAttributeMap(map);

			}else{
				portPageList=(ArrayList) pageBuildForm.getAttributeMap().get("portPageList");
			}
			
			
			boolean existing = request.getParameter("existing") == null ? false : true;
			String defaultSort = "getFromPageName";
			genericQueueUtil.sortQueue(genericForm, portPageList, existing, request,defaultSort);
			
			if(existing)
				genericQueueUtil.filterQueue(genericForm, request,pageBuildForm.getCLASS_NAME());
			
			//To make sure SelectAll is checked, see if no criteria is applied
			if(pageBuildForm.getSearchCriteriaArrayMap().size() == 0)
				request.setAttribute("ActionMode", "InitLoad");
			
			pageBuildForm.setPortPageList(portPageList);
			pageBuildForm.getAttributeMap().put("queueCount", String.valueOf(portPageList.size()));
			request.setAttribute("queueCount", String.valueOf(portPageList.size()));
			request.setAttribute("portPageList", portPageList);
			Integer queueSize = properties.getQueueSize(NEDSSConstants.PAGE_MANAGEMENT_PAGE_LIBRARY_QUEUE_SIZE);
			request.setAttribute("PageTitle", "Manage Pages: Manage Page Porting");
			pageBuildForm.getAttributeMap().put("queueSize",queueSize);
			
			return PaginationUtil.paginate(pageBuildForm,request,"managePortPage",mapping);
		}catch(Exception ex){
			logger.error("Error in loadManagePagePort method in ManagePageAction class :" +ex.getMessage(), ex);
			throw new ServletException(ex.getMessage());
		}
	}
    
    public void setQueueCount(PageBuilderForm pageBuilderForm, QueueDT queueDT){
		
		Map<Object, Object> map = pageBuilderForm.getAttributeMap();
		
		
		String method ="getColumn";
    	QueueColumnDT result;
    	
    	Class tClass = queueDT.getClass();
    	
    	for(int i=1; i<=10; i++){
    		try{
				method+=i;
				Method gs1Method = tClass.getMethod(method, new Class[] {});
				result = (QueueColumnDT) gs1Method.invoke(queueDT, new Object[] {});
				
				
				String constantCount=result.getConstantCount();
				String method2=result.getMethodGeneralFromForm();
				String filterType = result.getFilterType();
				
				
				if(filterType!=null && (filterType=="0" || filterType=="2")){
					try{
					Class tClass2 = pageBuilderForm.getClass();
					Method gs1Method2 = tClass2.getMethod(method2, new Class[] {});
					ArrayList<Object> result2 = (ArrayList<Object>) gs1Method2.invoke(pageBuilderForm, new Object[] {});
					
					int size=result2.size();
					map.put(constantCount, new Integer (size));
					
					}catch(Exception e){
						//TODO
					}
					
					
				}
    		}catch(Exception e){
    			
    		}
    	}
    }

    public QueueDT fillQueueDT(){
    	
    	QueueDT queueDT = new QueueDT();

    	//First column: Event Type
    	QueueColumnDT queue = new QueueColumnDT();
    	queue.setColumnId("column1");
    	queue.setColumnName("Event Type");
    	queue.setBackendId("EVENT_TYPE");
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getEventTypeDescTxt");
    	queue.setMedia("html pdf csv");
    	queue.setMediaHtmlProperty("eventTypeDescTxt");
    	queue.setMediaPdfProperty("eventTypeDescTxt");
    	queue.setMediaCsvProperty("eventTypeDescTxt");
    	
    	queue.setColumnStyle("width:8%");
    	queue.setFilterType("2");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("PORTEVENTTYPE");
    	queue.setDropdownStyleId("portEventTypeID");
    	queue.setDropdownsValues("portEventType");	// Getter exist in specific actionForm. It used to hold check box values.
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("PORTEVENTTYPE_COUNT");
    	queue.setMethodFromElement("getEventTypeDescTxt");
    	queue.setMethodGeneralFromForm("getColumn1List");//Only for date/multiselect
    	queue.setFilterByConstant(NEDSSConstants.FILTERBYEVENTTYPE);
    	queueDT.setColumn1(queue);

    	//Second column: Map Name
    	queue = new QueueColumnDT();
    	queue.setColumnId("column2");
    	queue.setColumnName("Map Name");
    	queue.setBackendId("MAP_NAME");
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getMapName");
    	queue.setMedia("html pdf csv");
    	queue.setMediaHtmlProperty("mapName");
    	queue.setMediaPdfProperty("mapName");
    	queue.setMediaCsvProperty("mapName");
    	
    	queue.setColumnStyle("width:17%");
    	queue.setFilterType("1");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("MAPNAME");
    	queue.setDropdownStyleId("mapNameID");
    	queue.setDropdownsValues("noDataArray");
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("??");
    	queue.setMethodFromElement("getMapName");
    	queueDT.setColumn2(queue);

    	//Third column: From Page Name
    	queue = new QueueColumnDT();
    	queue.setColumnId("column3");
    	queue.setColumnName("From Page Name");
    	queue.setBackendId("FROM_PAGE_NAME");//??
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getFromPageName");
    	queue.setMedia("html pdf csv");
    	queue.setMediaHtmlProperty("fromPageName");
    	queue.setMediaPdfProperty("fromPageName");
    	queue.setMediaCsvProperty("fromPageName");
    	
    	queue.setColumnStyle("width:17%");
    	queue.setFilterType("1");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("FROMPAGENAME");
    	queue.setDropdownStyleId("fromPageNameID");
    	queue.setDropdownsValues("noDataArray");
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("??");
    	queue.setMethodFromElement("getFromPageName");
    	queueDT.setColumn3(queue);

    	//Fourth column: To Page Name
    	queue = new QueueColumnDT();
    	queue.setColumnId("column4");
    	queue.setColumnName("To Page Name");
    	queue.setBackendId("TO_PAGE_NAME");//??
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getToPageName");
    	queue.setMedia("html pdf csv");
    	queue.setMediaHtmlProperty("toPageName");
    	queue.setMediaPdfProperty("toPageName");
    	queue.setMediaCsvProperty("toPageName");
    	queue.setColumnStyle("width:17%");
    	queue.setFilterType("1");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("TOPAGENAME");
    	queue.setDropdownStyleId("toPageNameID");
    	queue.setDropdownsValues("noDataArray");
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("??");
    	queue.setMethodFromElement("getToPageName");
    	queueDT.setColumn4(queue);

    	//Fifth column: Mapping Status
    	queue = new QueueColumnDT();
    	queue.setColumnId("column5");
    	queue.setColumnName("Mapping Status");
    	queue.setBackendId("MAPPINGSTATUSDESCTEXT");//??
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getMappingStatusDescText");
    	queue.setMedia("html pdf csv");
    	queue.setMediaHtmlProperty("mappingStatusDescText");
    	queue.setMediaPdfProperty("mappingStatusDescText");
    	queue.setMediaCsvProperty("mappingStatusDescText");
    	queue.setColumnStyle("width:10%");
    	queue.setFilterType("2");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("MAPPINGSTATUS");
    	queue.setDropdownStyleId("mappingStatusDescTextID");
    	queue.setDropdownsValues("mappingStatus"); // Getter exist in specific actionForm. It used to hold check box values.
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("MAPPINGSTATUS_COUNT");
    	queue.setMethodFromElement("getMappingStatusDescText");
    	queue.setMethodGeneralFromForm("getColumn2List");//Only for date/multiselect
    	queue.setFilterByConstant(NEDSSConstants.FILTERBYDESCRIPTION);
    	
    	queueDT.setColumn5(queue);

    	//Sixth column: Port Condition
    	queue = new QueueColumnDT();
    	queue.setColumnId("column6");
    	queue.setColumnName("Port Condition");
    	queue.setBackendId("CONDITION_DESC");
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getConditionDescText");
    	queue.setMedia("html pdf cvs");
    	queue.setMediaHtmlProperty("conditionDescWithLink");
    	queue.setMediaPdfProperty("conditionDescText");
    	queue.setMediaCsvProperty("conditionDescText");
    	queue.setColumnStyle("width:10%");
    	queue.setFilterType("2");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("CONDITIONDESCTEXT");
    	queue.setDropdownStyleId("conditionDescTextID");
    	queue.setDropdownsValues("portCondition"); // Getter exist in specific actionForm. It used to hold check box values.
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("CONDITIONDESCTEXT_COUNT");
    	queue.setMethodFromElement("getConditionDescText");
    	queue.setMethodGeneralFromForm("getColumn3List");//Only for date/multiselect
    	queue.setFilterByConstant(NEDSSConstants.FILTERBYCONDITION);
    	queueDT.setColumn6(queue);
    	
    	//Seventh column: Port Date
    	queue = new QueueColumnDT();
    	queue.setColumnId("column7");
    	queue.setColumnName("Port Date");
    	queue.setBackendId("PORT_DATE");
    	queue.setDefaultOrder("ascending");
    	queue.setSortable("true");
    	queue.setSortNameMethod("getLastChgTime");
    	queue.setMedia("html pdf csv");
    	queue.setMediaHtmlProperty("lastChgTime");
    	queue.setMediaPdfProperty("lastChgTime");
    	queue.setMediaCsvProperty("lastChgTime");
    	
    	queue.setColumnStyle("width:10%");
    	queue.setFilterType("0");//0 date, 1 text, 2 multiselect
    	queue.setDropdownProperty("LASTCHANGEDATE");
    	queue.setDropdownStyleId("portDateID");
    	queue.setDropdownsValues("portDate"); // Getter exist in specific actionForm. It used to hold check box values.
    	queue.setErrorIdFiltering("??");//Same than BackendId?
    	queue.setConstantCount("DATE_FILTER_COUNT");
    	queue.setMethodFromElement("getLastChgTime");
    	queue.setMethodGeneralFromForm("getColumn4List");//Only for date/multiselect
    	queue.setFilterByConstant(NEDSSConstants.FILTERBYDATE);
    	queueDT.setColumn7(queue);
    	
    	return queueDT;
    	
    }
    
    /**
     * Clone Page Submit
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	public ActionForward clonePageSubmit(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	try{
    	Long waTemplateUid = null;

    	PageBuilderForm pageBuildForm = (PageBuilderForm)form;

    	WaTemplateVO waTempVo = (WaTemplateVO)pageBuildForm.getSelection();
    	PageManagementActionUtil util = new PageManagementActionUtil();
    	WaTemplateDT dt = waTempVo.getWaTemplateDT();

    	//check that the new clone page name does not already exist
      	ArrayList<Object> errorMsgs = util.validatePageNmUniqueness(dt.getTemplateNm(), 0L,dt.getTemplateType());
    	if (!errorMsgs.isEmpty()){

    		Iterator iter = errorMsgs.iterator();
    		request.setAttribute("CloneError",(String) iter.next());
    		//populate selected list (if any) so right side shows up
    		ArrayList<Object> selCondList = new  ArrayList<Object>() ;
    		String[] strArr = pageBuildForm.getSelectedConditionCodes();
    		for (int i=0; i<strArr.length; i++) {
    			String  result = strArr[i] ;
    			//place first condition code in the waTemplateDT - may remove this column at later date
    			if (result != null && !result.isEmpty()){

    					DropDownCodeDT theDropDownDT = new DropDownCodeDT();
    					theDropDownDT.setKey(result);
    					theDropDownDT.setValue(CachedDropDowns.getConditionDesc(result));
    					selCondList.add(theDropDownDT);

    			    }
    		    }
    		pageBuildForm.setSelectedCondList(selCondList);
    		return mapping.findForward("clonePage");
       }//return to clone page if duplicate page name

    	pageBuildForm.setActionMode(NEDSSConstants.CLONE_SUBMIT_ACTION);
    	dt = util.setTheValuesForWaTemplateDT(dt, request, NEDSSConstants.CREATE_SUBMIT_ACTION);
    	//pageBuildForm.setSelection(dt);

    	//TO DO  Remove this hard coded  values once the table is ready
    	dt.setTemplateType(NEDSSConstants.DRAFT);
    	dt.setXmlPayload("XML Payload");
    	String pgPageName = dt.getTemplateNm();
    	dt.setFormCd(getPgFormCodeFromPageName(pgPageName, dt.getBusObjType()));
    	dt.setPublishVersionNbr(null);

    	Collection<Object> pageCondMappDTColl = new ArrayList<Object>();
    	if (pageBuildForm.getSelectedConditionCodes()!= null) //error if not at least one condition
    	{
    		String[] strArr = pageBuildForm.getSelectedConditionCodes();
    		for (int i=0; i<strArr.length; i++) {
    			String  result = strArr[i] ;
    			//place first condition code in the waTemplateDT - may remove this column at later date
    			if (result != null && !result.isEmpty())
    				waTempVo.getWaTemplateDT().setConditionCd(result);
    			PageCondMappingDT pageCondMapdt = new PageCondMappingDT();
    			pageCondMapdt.setConditionCd(result);
    			java.util.Date startDate= new java.util.Date();
    			Timestamp aAddTime = new Timestamp(startDate.getTime());
    			pageCondMapdt.setAddTime(aAddTime);
    			pageCondMapdt.setLastChgTime(aAddTime);
    			NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) request.getSession().getAttribute("NBSSecurityObject");
    			pageCondMapdt.setAddUserId(Long.valueOf(nbsSecurityObj.getEntryID()));
    			pageCondMapdt.setLastChgUserId(Long.valueOf(nbsSecurityObj.getEntryID()));
    			pageCondMappDTColl.add(pageCondMapdt);
    		}
    		waTempVo.setWaPageCondMappingDTColl(pageCondMappDTColl) ;
    	}//if no conditions

    	HttpSession session = request.getSession();
    	//Call the EJB here
    	try{
    		waTemplateUid = util.setWATemplate(session, waTempVo,NEDSSConstants.CLONE_SUBMIT_ACTION);
    		request.getSession().setAttribute("waTemplateUid",waTemplateUid);
    		request.setAttribute("pgPageName",pgPageName);
    		//update pgMap with the new page name
    		if(request.getSession().getAttribute("pgMap")!=null){
	    		HashMap<Object,Object> pgMap = (HashMap <Object,Object>)request.getSession().getAttribute("pgMap");
	    		dt.setWaTemplateUid(waTemplateUid);
	    		pgMap.put(dt.getWaTemplateUid(),pgPageName);
	    		session.setAttribute("pgMap", pgMap);
    		}
    	}
    	catch(Exception e){
			logger.error(" Error while Submitting the Clone page " );
			throw new ServletException(e.getMessage());
		}

    	request.setAttribute("PageTitle", "Manage Pages:Add New Page");
    	request.getSession().setAttribute("from","A");
        return mapping.findForward("previewPage");
    	}catch(Exception ex){
    		logger.error("Error in creating the page clonePageSubmit method of ManagePageAction class :" +ex.getMessage());
    		throw new ServletException(ex.getMessage());
    	}
    }

   
	 /**
     * Build the pgFormCode i.e. PG_Hodgins_Diseases from the Page Name
     * @param templateNm (page name)
     * @return strFormCode
     */
    private String getPgFormCodeFromPageName(String templateNm, String busObjType) {
    	String pgFormCd = templateNm.trim();
    	pgFormCd = pgFormCd.replaceAll("[\\/:;*?\"<>|' ]","_");
    	
    	if (busObjType != null && busObjType.equalsIgnoreCase(NEDSSConstants.CONTACT_BUSINESS_OBJECT_TYPE))
    		pgFormCd = "CT_" + pgFormCd;
    	else if (busObjType != null && busObjType.equalsIgnoreCase(NEDSSConstants.INTERVIEW_BUSINESS_OBJECT_TYPE))
    		pgFormCd = NEDSSConstants.INTERVIEW_BUSINESS_OBJECT_TYPE + "_" + pgFormCd;
    	else if (busObjType != null && busObjType.equalsIgnoreCase(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE))
    		pgFormCd = NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE + "_" + pgFormCd;
    	else 
    		pgFormCd = "PG_" + pgFormCd;
    	
    	if(pgFormCd.length()>50){
    		pgFormCd = pgFormCd.substring(0, 49);
    	}
    	
    	return pgFormCd;
    }

    private void setMsgForPortInd(WaTemplateVO waTemplateVO, HttpServletRequest request){
    	Collection<Object> conditionList = waTemplateVO.getWaPageCondMappingDTColl();
		if(conditionList!=null && conditionList.size()>0){
			Iterator<Object> condIt = conditionList.iterator();
			while(condIt.hasNext()){
				PageCondMappingDT pageConDt = (PageCondMappingDT)condIt.next();
          	    	if(waTemplateVO.getWaTemplateDT().getBusObjType()!=null && waTemplateVO.getWaTemplateDT().getBusObjType().equals("INV") && pageConDt.getPortReqIndCd()!=null && pageConDt.getPortReqIndCd().equalsIgnoreCase("T")){
          	    		request.setAttribute("messageInd", "T");
          	    	}
			}
		}
    }
    /**
     * Remove any Investigators or Providers left in form.
     * Defect #1504
     * @param pageForm
     */
    private void clearAllParticipations(PageForm pageForm) {
    	CachedDropDownValues cdv = new CachedDropDownValues();
    	ArrayList<Object> parTypes = CachedDropDowns.getParticipationTypes();
    	Iterator parIter = parTypes.iterator();
    	while (parIter != null && parIter.hasNext()) {
    		ParticipationTypeVO parTypeVO = (ParticipationTypeVO) parIter.next();
    		if (parTypeVO.getQuestionIdentifier() != null)
    			pageForm.clearDWRInvestigator(parTypeVO.getQuestionIdentifier());
    	}
    }
    //Filtering/sorting code for Port Page Management(Port Page Library)
    
    public ActionForward filterPortPageLibSubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		PageBuilderForm pageBuildForm = (PageBuilderForm) form;
		
		try{
			//Map<Object, Object> searchCriteriaMap = pageBuildForm.getSearchCriteriaArrayMap();
			ArrayList<Object> portPageList = (ArrayList<Object> ) pageBuildForm.getPortPageList();
			pageBuildForm.setPortPageList(portPageList);
			GenericForm genericForm= translateFromObservationSummaryDisplayVOToGenericSummaryDisplayVO(pageBuildForm);
			Collection<Object>  filteredPortPageList = genericQueueUtil.filterQueue(genericForm, request ,pageBuildForm.getCLASS_NAME());
			
			if(filteredPortPageList==null){
				filteredPortPageList = pageBuildForm.getPortPageList();
			}
			
			request.setAttribute("ActionMode", "Search"); // Selects checkboxes based on applied filter
			pageBuildForm.getAttributeMap().put("portPageList",filteredPortPageList);
			pageBuildForm.getAttributeMap().put("queueList",filteredPortPageList);
			request.setAttribute("queueList", filteredPortPageList);
			request.setAttribute("portPageList", filteredPortPageList);
			request.setAttribute("queueCount", String.valueOf(filteredPortPageList.size()));
			pageBuildForm.getAttributeMap().put("queueCount", String.valueOf(filteredPortPageList.size()));
			request.setAttribute("PageTitle","Manage Pages: Manage Page Porting");
			pageBuildForm.getAttributeMap().put("PageNumber", "1");
			request.setAttribute("queueCollection",pageBuildForm.getQueueCollection());
            request.setAttribute("stringQueueCollection",pageBuildForm.getStringQueueCollection());
		}catch(Exception e){
			logger.error("Error in filterPortPageLibSubmit in ManagePageAction class: "+ e.toString(), e);
			throw new Exception(e.getMessage());
		}
		return PaginationUtil.paginate(pageBuildForm, request, "managePortPage",mapping);
		
	}
	
	private GenericForm translateFromObservationSummaryDisplayVOToGenericSummaryDisplayVO(PageBuilderForm pageBuilderForm){
		
		GenericForm genericForm = new GenericForm();
		try{
			genericForm.setSearchCriteriaArrayMap(pageBuilderForm.getSearchCriteriaArrayMap());
			genericForm.setAttributeMap(pageBuilderForm.getAttributeMap());
			genericForm.setQueueDT(pageBuilderForm.getQueueDT());
			
			genericForm.setColumn1List(pageBuilderForm.getPortEventType());
			genericForm.setColumn2List(pageBuilderForm.getMappingStatus());
			genericForm.setColumn3List(pageBuilderForm.getPortCondition());
			genericForm.setColumn4List(pageBuilderForm.getPortDate());
			
			genericForm.setElementColl(pageBuilderForm.getPortPageList());
		}catch(Exception ex){
			logger.error("Error in translateFromObservationSummaryDisplayVOToGenericSummaryDisplayVO in ManagePageAction class: "+ ex.getMessage(), ex);
		}
		return genericForm;
	}
	
}