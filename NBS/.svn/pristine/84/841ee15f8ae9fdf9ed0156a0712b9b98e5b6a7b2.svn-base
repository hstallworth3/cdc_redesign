package gov.cdc.nedss.webapp.nbs.form.myinvestigation;

import gov.cdc.nedss.proxy.ejb.investigationproxyejb.vo.InvestigationSummaryVO;
import gov.cdc.nedss.webapp.nbs.action.myinvestigation.ProgramAreaUtil;
import gov.cdc.nedss.webapp.nbs.action.util.QueueUtil;
import gov.cdc.nedss.webapp.nbs.form.util.BaseForm;
import gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author habraham2
 *
 */
public class ProgramAreaForm extends BaseForm {
	private static final long serialVersionUID = 1L;
	Map searchCriteriaArrayMap = new HashMap<Object,Object>();
	private Collection<Object>  invSummaryVOColl ;
	Map searchMap = new HashMap<Object,Object>();
	ProgramAreaUtil prgAreaUtil = new ProgramAreaUtil();

	private ArrayList<Object> investigators = new ArrayList<Object> ();
	private ArrayList<Object> jurisdictions = new ArrayList<Object> ();
	private ArrayList<Object> conditions = new ArrayList<Object> ();
	private ArrayList<Object> caseStatus = new ArrayList<Object> ();
	private ArrayList<Object> dateFilterList = new ArrayList<Object> ();
	private ArrayList<Object> notifications = new ArrayList<Object> ();
	
	private InvestigationSummaryVO investigationSummaryVO = new InvestigationSummaryVO();
	
	/**
	 * initializeDropDowns returns the latest set of distinct dropdown values for Investigator, 
	 * Patient etc Dropdown Lists in queue
	 */
	public void initializeDropDowns() {
		QueueUtil queueUtil = new QueueUtil();
		investigators = prgAreaUtil.getInvestigatorDropDowns(getInvSummaryVOColl());
		jurisdictions = prgAreaUtil.getJurisDropDowns(getInvSummaryVOColl());
		conditions = prgAreaUtil.getConditionDropDowns(getInvSummaryVOColl());
		caseStatus = prgAreaUtil.getCaseStatusDropDowns(getInvSummaryVOColl());
		dateFilterList = queueUtil.getStartDateDropDownValues();
		notifications = prgAreaUtil.getNotificationValuesfromColl(getInvSummaryVOColl());
	}
	
	public InvestigationSummaryVO getInvestigationSummaryVO() {
		return investigationSummaryVO;
	}

	public void setInvestigationSummaryVO(InvestigationSummaryVO investigationSummaryVO) {
		this.investigationSummaryVO = investigationSummaryVO;
	}
	
	public Collection<Object>  getInvSummaryVOColl() {
		return invSummaryVOColl;
	}

	/**
	 * @param invSummaryVOColl
	 */
	public void setInvSummaryVOColl(Collection<Object> invSummaryVOColl) {
		this.invSummaryVOColl = invSummaryVOColl;
	}
 
	public ArrayList<Object> getInvestigators(){
		return investigators;		
	}

	
	public ArrayList<Object> getJurisdictions(){
		return jurisdictions;
		
	}
	public ArrayList<Object> getConditions(){
		return conditions;
		
	}
	
	public ArrayList<Object> getCaseStatuses(){
		return caseStatus;
		
	}
	
	public ArrayList<Object> getStartDateDropDowns(){
		return dateFilterList;
	}

	/**
	 * @param key
	 * @return
	 */
	public String getSearchCriteria(String key) {
		return (String) searchMap.get(key);
	}

	
	public void setSearchCriteria(String key, String answer) {
		searchMap.put(key, answer);
	}
	
	public Object getCodedValue(String key) {
		ArrayList<Object> aList = (ArrayList<Object> ) CachedDropDowns.getCodedValueForType(key);
		return aList;
	}	
	public String[] getAnswerArray(String key) {
		return (String[])searchCriteriaArrayMap.get(key);
	}

	/**
	 * @param key
	 * @param answer
	 */
	public void setAnswerArray(String key, String[] answer) {
		if(answer.length > 0) {
			String [] answerList = new String[answer.length];
			boolean selected = false;
			for(int i=1; i<=answer.length; i++) {
				String answerTxt = answer[i-1];
				if(!answerTxt.equals("")) {
					selected = true;
					answerList[i-1] = answerTxt;
				}
			}
			if(selected)
				searchCriteriaArrayMap.put(key,answerList);
		}
	}

	public void setAnswerArrayText(String key, String answer) {
		if(answer!=null && answer.length() > 0) {
			String newKey = key+"_FILTER_TEXT";
				searchCriteriaArrayMap.put(newKey,answer);
		}
	}
	
	public Map<Object,Object> getSearchCriteriaArrayMap() {
		return searchCriteriaArrayMap;
	}

	public void setSearchCriteriaArrayMap(Map searchCriteriaArrayMap) {
		this.searchCriteriaArrayMap = searchCriteriaArrayMap;
	}


	public void clearAll() {
		getAttributeMap().clear();
		searchCriteriaArrayMap = new HashMap<Object,Object>();
	}

	public ArrayList<Object> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<Object>  notifications) {
		this.notifications = notifications;
	}
}
