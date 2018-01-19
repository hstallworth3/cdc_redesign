package gov.cdc.nedss.webapp.nbs.form.page;

import gov.cdc.nedss.act.publichealthcase.vo.PublicHealthCaseVO;
import gov.cdc.nedss.exception.NEDSSAppException;
import gov.cdc.nedss.page.ejb.pageproxyejb.vo.act.PageActProxyVO;
import gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil;
import gov.cdc.nedss.systemservice.nbscontext.NBSContext;
import gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.nbssecurity.ProgramAreaJurisdictionUtil;
import gov.cdc.nedss.systemservice.util.DropDownCodeDT;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.webapp.nbs.action.page.clientvo.PageClientVO;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageConstants;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.rendering.util.PageLoadUtil;
import gov.cdc.nedss.webapp.nbs.action.pagemanagement.util.common.PageManagementCommonActionUtil;
import gov.cdc.nedss.webapp.nbs.action.pam.util.DWRUtil;
import gov.cdc.nedss.webapp.nbs.action.pam.util.PamConstants;
import gov.cdc.nedss.webapp.nbs.action.util.CallProxyEJB;
import gov.cdc.nedss.webapp.nbs.action.util.FileUploadUtil;
import gov.cdc.nedss.webapp.nbs.form.managectassociation.ManageCTAssociateForm;
import gov.cdc.nedss.webapp.nbs.form.pam.FormField;
import gov.cdc.nedss.webapp.nbs.form.util.BaseForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.CachedDropDownValues;
import gov.cdc.nedss.webapp.nbs.logicsheet.helper.QuestionsCache;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 *Name: PageForm.java Description: Form class for Case Object(for Dynamic
 * Pages) 
 * Note: A number of items were moved to the BaseForm for the 4.5 release
 * Copyright(c) 2010 Company: CSC
 *
 * @since: NBS4.0
 * @author Pradeep Sharma
 */
public class PageForm extends BaseForm {
	private static final long serialVersionUID = 1L;
	static final LogUtils logger = new LogUtils(PageForm.class.getName());

	private PageClientVO pageClientVO = new PageClientVO();
	private PageClientVO pageClientVO2 = new PageClientVO();
	

	private ArrayList<Object> conditionFamilyList = new ArrayList<Object>();

	private String newConditionCd;

	private String noNotfReqFieldsCheck;

	private String formName;
	
	private String formContent;
	
	private String coinfCondInvUid;
	
	private Map coinfectionCondMap =new HashMap<String,String>();
	
	private String coinfInvList;
 

	public PageClientVO getPageClientVO() {
		return pageClientVO;
	}
	
	public void setPageClientVO(PageClientVO pageClientVO) {
		this.pageClientVO = pageClientVO;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		pageClientVO.reset(mapping, request);
	//	batchEntryMap = new HashMap<Object,ArrayList<BatchEntry>>();
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);
		if (errors == null)
			errors = new ActionErrors();
		return errors;
	}


	public ArrayList<Object> getConditionFamilyList() {
		if (this.conditionFamilyList.size() > 0)
			return this.conditionFamilyList; //set in page load
		ArrayList<Object> aList = new ArrayList<Object>();
		String conditionCd = pageClientVO.getAnswer(PageConstants.CONDITION_CD);
		if (conditionCd == null || conditionCd.isEmpty())
			conditionCd = newConditionCd;
		aList = CachedDropDowns.getConditionFamilyList(conditionCd);
		return aList;
	}

	public ArrayList<Object> getConditionFamilyList(String conditionCd) {
		ArrayList<Object> aList = new ArrayList<Object>();
		aList = CachedDropDowns.getConditionFamilyList(conditionCd);
		return aList;
	}

	public ArrayList<Object> getConditionFamilyList(String conditionCd, NBSSecurityObj nbsSecurityObj, HttpServletRequest req) {
		ArrayList<Object> condFamilyList = new ArrayList<Object>();
		condFamilyList = CachedDropDowns.getConditionFamilyList(conditionCd);
		if (condFamilyList.size() > 1) {
			CachedDropDownValues cdv = new CachedDropDownValues();
			//some of the conditions could be in program areas which the user
			//does not have add investigation permission
			//remove any the user does not have permission
			Iterator<Object> ite = condFamilyList.iterator();
			while (ite.hasNext()) {
				DropDownCodeDT dropDownDT = (DropDownCodeDT) ite.next();
				String thisCondCd = dropDownDT.getKey();
				if (thisCondCd.isEmpty())  //blank
					continue;
				String programArea = cdv.getProgramAreaCd(thisCondCd);
				//we can't create a new investigation for the changed condition
				//if the user doesn't have add permission
				boolean addInvestigationPermission =
					nbsSecurityObj.getPermission(NBSBOLookup.INVESTIGATION,
						NBSOperationLookup.ADD, programArea,
						ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
				if (addInvestigationPermission == false)
						ite.remove();
			}
		}
		return condFamilyList;
	}
	public void setConditionFamilyList(ArrayList<Object> conditionFamilyList) {
		this.conditionFamilyList = conditionFamilyList;
	}
	public void setNewConditionCd(String conditionCd) {
		this.newConditionCd = conditionCd;
	}

	public String getNewConditionCd() {
		return newConditionCd;
	}

	public ArrayList<?> clearDetailsAsian() {
		getPageClientVO().getArrayAnswerMap().remove(
				PageConstants.DETAILED_RACE_ASIAN);
		return (ArrayList<?>) getRaceList("2028-9");
	}

	public ArrayList<?> clearDetailsHawaii() {
		getPageClientVO().getArrayAnswerMap().remove(
				PageConstants.DETAILED_RACE_HAWAII);
		return (ArrayList<?>) getRaceList("2076-8");
	}

	public ArrayList<Object> validateNotificationReqFields(HttpServletRequest req) 
	{
		//WebContext ctx = WebContextFactory.get();
		// HttpServletRequest req = ctx.getHttpServletRequest();
		Map<?,?> questionMap = (TreeMap<?,?>)QuestionsCache.getQuestionMap().get(this.getPageFormCd());
		ArrayList<Object> returnList = new ArrayList<Object> ();
		String notifErrorFieldList = "";
		try {
			Map<?,?> reqMap = DWRUtil.createNotification(((PageActProxyVO)getPageClientVO().getOldPageProxyVO()).getPublicHealthCaseVO() ,questionMap, this.getPageFormCd(), req);
			if(reqMap != null && reqMap.size() > 0) 
			{
				Iterator<?>  iter = reqMap.keySet().iterator();
				while(iter.hasNext()) 
				{
					String key = (String) iter.next();
					String keyVal = (String) reqMap.get(key);
					notifErrorFieldList = notifErrorFieldList.concat(key + " "); //used to highlight missing fields on edit
					returnList.add(keyVal);
				}
				this.setErrorFieldList(notifErrorFieldList); //set fields to hilight on edit
				return returnList;
			}
		}
		catch (Exception e) 
		{
			logger.error("Error while Validating Notification Required Fields: " + e.toString());
			returnList.add("ERROR");
			return returnList;
		}
		return null;
	}
	

	public static void partnerServicesNotifications(ArrayList<PublicHealthCaseVO> partnerServicesPHCCollection) throws NEDSSAppException
    {
    	
      //for each publichealthcaseVO  {   
    	String comments="Partner Services File Notification Created";
    	String invFormCd=null;
    	HttpServletRequest request=null;
    	
    	Iterator phcCollIter = partnerServicesPHCCollection.iterator();
    	while (phcCollIter.hasNext()) {
    		Object publicHealthCaseVO1=phcCollIter.next();
    		PublicHealthCaseVO publicHealthCaseVO=(PublicHealthCaseVO) publicHealthCaseVO1;
    		DWRUtil.updateNotifications(publicHealthCaseVO, comments, request, invFormCd);
    	}
        
    //}
    }


	public ArrayList<Object> updateNotifications(String comments) throws NEDSSAppException {

		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		
		return DWRUtil.updateNotifications(((PageActProxyVO)getPageClientVO().getOldPageProxyVO()).getPublicHealthCaseVO(), comments, req, super.getPageFormCd());
	}
	
	public void initializeForm(ActionMapping mapping, HttpServletRequest request) {
		// QECodes autocomplete
		ArrayList<Object> qecList = CachedDropDowns.getAllQECodes(false, "PRV");
		request.getSession().setAttribute("qecList", qecList);
		ArrayList<Object> qecListORG = CachedDropDowns.getAllQECodes(false,
				"ORG");
		request.getSession().setAttribute("qecListORG", qecListORG);
	}

	public void setNotfReqFields() {
		getAttributeMap().put(PamConstants.REQ_FOR_NOTIF, "true");
		getAttributeMap().put(PamConstants.NO_REQ_FOR_NOTIF_CHECK, "false");

	}

	public String isNoNotfReqFieldsCheck() {
		return noNotfReqFieldsCheck;
	}

	public void setNoNotfReqFieldsCheck(String noNotfReqFieldsCheck) {
		this.noNotfReqFieldsCheck = noNotfReqFieldsCheck;
	}


	public void callChildForm(String filler) {

		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		ManageCTAssociateForm childForm = (ManageCTAssociateForm) req
				.getSession().getAttribute("manageCTAssociateForm");
		childForm.updateCheckboxIds(filler);

	}



	/**
	 * dwr call to check the Program Area for a new condition changed to
	 *     Used to warn the user that they will not be able to view the
	 *     new case.
	 *     MOTE: This method currently not implemented by Change Condition
	 * @param new condition cd
	 * @return T or F
	 */
	public String checkProgramAreaPermissions(String conditionCd) {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		NBSSecurityObj nbsSecurityObj = (NBSSecurityObj) req.getSession()
				.getAttribute("NBSSecurityObject");
		CachedDropDownValues cdv = new CachedDropDownValues();
		String programArea = cdv.getProgramAreaCd(conditionCd);
		boolean progAreaSec = nbsSecurityObj.getPermission(
				NBSBOLookup.OBSERVATIONLABREPORT, NBSOperationLookup.VIEW,
				programArea, ProgramAreaJurisdictionUtil.ANY_JURISDICTION);
		if (progAreaSec)
			return "T"; //user has permission
		else
			return "F"; // user does not have permission
	}

	/**
	 * dwr call to delete the attachment and return String
	 * @param uid
	 * @return
	 */
	public boolean deleteAttachment(String uid) {
		boolean isDeleted = false;
		Long nbsCaseAttachUid = Long.valueOf(uid);
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		FileUploadUtil fileUpUtil = new FileUploadUtil();
		try {
			fileUpUtil.processRequest(nbsCaseAttachUid, req.getSession());
			isDeleted = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while deleting NBS Case Attachment for uid: " + uid);
		}

		return isDeleted;
	}

   
    /*
     * getDsrStdDiagnosis()
     * Used by STD program area for their Case Diagnosis and Physician Diagnosis.
     * Convention is that CASE_DIAGNOSIS is the master code list and the relevant options
     * for each of the relevant NBS Condition Codes exists in a codeset that is CASE_DIAGNOSIS_SUBSET<CODE>
     * i.e. CASE_DIAGNOSIS_SUBSET10313
     */
    public ArrayList<Object>  getDwrStdDiagnosis(String fieldId) {
    	ArrayList<Object> diagnosisList = new ArrayList<Object> ();
    	String conditionCd = "";
		if (fieldId != null && fieldId.isEmpty())
			return diagnosisList;
		//no condition in Page Preview
		if (super.getActionMode() != null && super.getActionMode().equalsIgnoreCase(NEDSSConstants.PREVIEW_ACTION))
			return diagnosisList;
		
		//Get the condition code we are working under
		if (getPageClientVO().getAnswerMap().get(PageConstants.CONDITION_CD) != null)
			conditionCd = getPageClientVO().getAnswerMap().get(PageConstants.CONDITION_CD).toString();
		else { //for a new create - get it from the session
			WebContext ctx = WebContextFactory.get();
			HttpServletRequest req = ctx.getHttpServletRequest();
			try
			{
			conditionCd = (String) NBSContext.retrieve(req.getSession(), NBSConstantUtil.DSInvestigationCondition);
			} catch (Exception e) {
				logger.warn("PageForm.getDwrStdDiagnosis called but No Condition exists?");
			}
		}
		if (conditionCd == null || conditionCd.isEmpty())
			return diagnosisList;
		
		//get the code set name from the FormFieldMap
		HashMap<Object,Object> formMap = new HashMap<Object,Object>();
		FormField ff = (FormField) this.getFormFieldMap().get(fieldId);
		
		//several types are associated with a code list
		ArrayList<Object> srtValues = new ArrayList<Object> ();
		String codeSetNm = ff.getCodeSetNm();
		if (codeSetNm == null || codeSetNm.isEmpty())
			return diagnosisList; 
		
		//see if there is a codeset with '_SUBSET'+ condition code
		//if that exists - return it
		srtValues = (ArrayList<Object>)this.getCodedValue(ff.getCodeSetNm() + "_SUBSET" + conditionCd);
		//if the codeset isn't setup up we get only one blank option
		if (srtValues != null && !srtValues.isEmpty() && srtValues.size() > 1) {
			java.util.Date date= new java.util.Date();
			Timestamp effectiveStartDate = new Timestamp(date.getTime());
			//remove values which are no longer valid unless this is an old investigation
			if (getPageClientVO().getAnswerMap().get(PageConstants.INV_START_DATE) != null) {
			    try {
			        DateFormat formatter;
			        formatter = new SimpleDateFormat("MM/dd/yyyy");
			        Date startDate = (Date) formatter.parse((String)getPageClientVO().getAnswerMap().get(PageConstants.INV_START_DATE));
			        effectiveStartDate = new Timestamp(startDate.getTime());
			      } catch (ParseException e) {
			    	  logger.warn("in getDwrStdDiagnosis( ) error parsing Inv Start Date string " + getPageClientVO().getAnswerMap().get(PageConstants.INV_START_DATE));
			        return srtValues;
			      }
			}

		    srtValues = CachedDropDowns.copyDropDown(srtValues); //we don't want to alter the cache
			Iterator<Object> dropItr = srtValues.iterator();
			boolean listAltered = false;
			while (dropItr.hasNext()){
				DropDownCodeDT dropDownCode = (DropDownCodeDT) dropItr.next();
				if (dropDownCode.getEffectiveToTime() != null) {
					if (effectiveStartDate != null && effectiveStartDate.after(dropDownCode.getEffectiveToTime())) {
						dropItr.remove();
						listAltered = true; 
					}
				}
			}
			return srtValues;	
		}
		logger.info("Note: CASE_DIAGNOSTIC_SUBSET" + conditionCd + " is not in SRT??");
		
		return diagnosisList;
	}    



	/*
     * getDwrStdInitialFollowup()
     * If referred from a lab or a morb - the codeset changes.
     * There is a codeset for Syphillis and a codeset for Non-Syphillis
     */
    public ArrayList<Object>  getDwrStdInitialFollowup(String referralBasis) {
    	ArrayList<Object> initialFollowupList = new ArrayList<Object> ();
    	String conditionCd = "";
		if (referralBasis != null && referralBasis.isEmpty())
			return initialFollowupList;
		//no condition in Page Preview
		if (super.getActionMode() != null && super.getActionMode().equalsIgnoreCase(NEDSSConstants.PREVIEW_ACTION))
			return initialFollowupList;
		
		//Get the condition code we are working under
		if (getPageClientVO().getAnswerMap().get(PageConstants.CONDITION_CD) != null)
			conditionCd = getPageClientVO().getAnswerMap().get(PageConstants.CONDITION_CD).toString();
		else { //for a new create - get it from the session
			WebContext ctx = WebContextFactory.get();
			HttpServletRequest req = ctx.getHttpServletRequest();
			try
			{
			conditionCd = (String) NBSContext.retrieve(req.getSession(), NBSConstantUtil.DSInvestigationCondition);
			} catch (Exception e) {
				logger.warn("PageForm.getDwrStdInitialFollowup called but No Condition exists?");
			}
		}
		if (conditionCd == null || conditionCd.isEmpty())
			return initialFollowupList;
	
		ArrayList<Object> srtValues = new ArrayList<Object> ();
		//if we have a syphillis condition and referral basis is lab report(TI)or morb report(T2)..
		if (referralBasis.equals("T1") || referralBasis.equals("T2")) {

			if (isSyphilisCondition(conditionCd) && referralBasis.equals("T1"))  //syphillis and Lab
				srtValues = (ArrayList<Object>)this.getCodedValue(NEDSSConstants.STD_CREATE_INV_LAB_SYPHILIS_PROC_DECISION);
			else if (isSyphilisCondition(conditionCd) && referralBasis.equals("T2"))  //syphillis and Morb
				srtValues = (ArrayList<Object>)this.getCodedValue(NEDSSConstants.STD_CREATE_INV_MORB_SYPHILIS_PROC_DECISION);
			else
				srtValues = (ArrayList<Object>)this.getCodedValue(NEDSSConstants.STD_CREATE_INV_LABMORB_NONSYPHILIS_PROC_DECISION);
		} else {
			//partner contact list of referral basis
			srtValues = (ArrayList<Object>)this.getCodedValue(NEDSSConstants.STD_CR_PROC_DECISION_NOINV);
		}
		
		//if the codeset isn't setup up we get only one blank option
		if (srtValues != null && !srtValues.isEmpty() && srtValues.size() > 1)
			return srtValues;
		logger.info("Note: getDwrStdInitialFollowup codeset for " + conditionCd + " is not in SRT??");
		
		
		
		return initialFollowupList;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormContent() {
		return formContent;
	}

	public void setFormContent(String formContent) {
		this.formContent = formContent;
	}



	public String getCoinfCondInvUid() {
		return coinfCondInvUid;
	}

	public void setCoinfCondInvUid(String coinfCondInvUid) {
		this.coinfCondInvUid = coinfCondInvUid;
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getCoinfectionCondMap() {
		return coinfectionCondMap;
	}

	public void setCoinfectionCondMap(Map coinfectionCondMap) {
		this.coinfectionCondMap = coinfectionCondMap;
	}

	public String getCoinfInvList() {
		return coinfInvList;
	}

	public void setCoinfInvList(String coinfInvList) {
		this.coinfInvList = coinfInvList;
	}

	/**
	 * Used to check association before Edit and Delete
	 * @param actUidStr
	 * @param busObjectType
	 * @return
	 */
	public ArrayList<String> checkAssociationBeforeDelete(String actUidStr, String busObjectType){
		ArrayList<String> msgList = new ArrayList<String>();
		try{
			WebContext context = WebContextFactory.get();
			HttpServletRequest request = context.getHttpServletRequest();
			Map<Object,Object> returnMap =  PageManagementCommonActionUtil.checkAssociationBeforeDelete(actUidStr, busObjectType, request.getSession());
			
			Integer caseCount = (Integer)returnMap.get(NEDSSConstants.CASE);
			logger.debug(" checkAssociationBeforeDelete:  caseCount returned is "+ caseCount);
			
			if(caseCount!= null && caseCount.intValue()>0)
			{
				String deleteString="You cannot Delete this Vaccination Record as there is:\r\n\r\n " +
				caseCount+ " Associated Investigation(s)\r\n\n "
				+"Disassociate the Investigation(s) and try again. \r\n Note: You can only disassociate Investigations you have access to disassociate.";

	             msgList.add(deleteString);
			}else{
				 msgList.add("");
			}
		}catch(Exception ex){
			logger.error("Error in calling the checkAssociationBeforeDelete, actUidStr: "+actUidStr+", Exception: "+ex.getMessage(),ex);
		}
		return msgList;
	}

	/**
	 * @param actUidStr
	 * @param busObjectType
	 * @return
	 */
	public ArrayList<String> checkForExistingNotificationsByCdsAndUid(String actUidStr, String busObjectType){
		ArrayList<String> msgList = new ArrayList<String>();
		try{
			WebContext context = WebContextFactory.get();
			HttpServletRequest request = context.getHttpServletRequest();
			
			String sBeanJndiName = JNDINames.PAGE_PROXY_EJB;
	        String sMethod = "checkForExistingNotificationsByCdsAndUid";
	        
	        String classCd = NEDSSConstants.CLASS_CD_INTV;
	        String typeCd = NEDSSConstants.TYPE_CD;
	        if(NEDSSConstants.VACCINATION_BUSINESS_OBJECT_TYPE.equals(busObjectType)){
	        	classCd = NEDSSConstants.CLASS_CD_INTV;
	        	typeCd = NEDSSConstants.TYPE_CD;
	        }
	        Long uid = null;
	        if(actUidStr!=null){
	        	uid = Long.parseLong(actUidStr);
	        }
	        Object[] oParams = new Object[] {classCd, typeCd,uid};
	        Object obj = CallProxyEJB.callProxyEJB(oParams,sBeanJndiName,sMethod,request.getSession());
	        boolean isNotificationExist = (boolean) obj;
	        if(isNotificationExist)
	        	msgList.add("True");
	        else
	        	msgList.add("");
		}catch(Exception ex){
			logger.error("Error in calling the checkForExistingNotificationsByCdsAndUid, actUidStr: "+actUidStr+", Exception: "+ex.getMessage(),ex);
		}
		return msgList;
	}
	
	public PageClientVO getPageClientVO2() {
		return pageClientVO2;
	}

	public void setPageClientVO2(PageClientVO pageClientVO2) {
		this.pageClientVO2 = pageClientVO2;
	}
    
}
