package gov.cdc.nedss.pagemanagement.wa.xml.util;

import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.pagemanagement.ejb.pagemanagementproxyejb.wa.dt.WaRuleMetadataDT;
import gov.cdc.nedss.util.LogUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;

/**
 * MarshallPageRules - This is called by MarshalPageXML to add rule logic to the XML.
 *  We append to a onLoad and OnSubmit string creating the string here.
 * @author Gregory Tucker
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: CSC</p>
 * MarshallPageXML.java
 * May 18, 2010
 * @version 0.9
 */


public class MarshallPageRules {

	static final LogUtils logger = new LogUtils(MarshallPageRules.class.getName());
	String onLoadFunctions = "";


	/**
	 * Put info from the rule metadata into the XML
	 *
	 * @param collection of WaRuleMetadataDT from the wa_rule_metadata
	 * @param quesMap - (question id, page element)
	 * @param batchQuesMap - (question id, subsection id) - will be empty if no batch subsections
	 * @param pageInfo - root of the XML we are building - the allocated PageType
	 *
	 */
	public void addRulesToXML(Collection <Object> pageRuleCollection, 
			HashMap<String, PageElementType> quesMap, 
			HashMap<String, String> batchQuesMap, 
			HashMap<String, OnBatchAddFunctionType> batchQuesAddFunctionMap, 
			PageInfoType pageInfo)	throws NEDSSSystemException {

		PageType page = pageInfo.getPage();  //get the page from the root element
	try {
		//we'll build a list of the batch functions to call on Add button -- if there are any 
		Iterator<Object> iter = pageRuleCollection.iterator(); //iter through the DT
		
		while (iter.hasNext()) {
			Object nextRule = iter.next();
		    WaRuleMetadataDT ruleMeta = (WaRuleMetadataDT) nextRule;
		    addRuleToBucket(ruleMeta, pageInfo);
		    if (ruleMeta.getRuleCd().equals("Date Compare")) {
		    	//if source or target is in a batch - add to special addBatchFunction otherwise
		    	//add to the usual OnSubmit function set.
		    	String strBatchId = sourceOrTargetInBatch(ruleMeta, batchQuesMap);
		    	if (strBatchId.isEmpty())
		    		addDateRule(ruleMeta, page);
		    	else
		    		addBatchDateRule(batchQuesAddFunctionMap, strBatchId, ruleMeta, page);
		    } else if (ruleMeta.getRuleCd().equals("Enable") || ruleMeta.getRuleCd().equals("Disable")) {
		    	addEnableDisableRule(ruleMeta,quesMap, page, pageRuleCollection);
		    } else if (ruleMeta.getRuleCd().equals("Require If")) {
		    	addRequireIfRule(ruleMeta,quesMap, page, pageRuleCollection);
		    } else if (ruleMeta.getRuleCd().equals("Hide") || ruleMeta.getRuleCd().equals("Unhide")) {
		    	addHideOrShowRule(ruleMeta,quesMap, page, pageRuleCollection);
		    }
		}
		//Look in the NBSFixedRule.xml file for any fixed functions for OnLoad or OnSubmit
		FixedRuleEventBuilder fixedRuleEventBuilder = new FixedRuleEventBuilder(pageInfo.getTemplate().getTemplateBusinessObjectType());
		String fixedRuleOnLoad = fixedRuleEventBuilder.getFixedRuleOnloadFunctions(quesMap);
		if (fixedRuleOnLoad != null && !fixedRuleOnLoad.isEmpty())
			onLoadFunctions = onLoadFunctions.concat(fixedRuleOnLoad);
		//check for any fixed rule on submit functions
		fixedRuleEventBuilder.getFixedRuleOnSubmitFunctions(quesMap, page);
		//set any onblur, onchange
		fixedRuleEventBuilder.setFixedRuleDynamicEventFunctionsOnPage(quesMap, page);
		//iteration complete - add onLoad and Submit string
		page.setOnLoadFunctions(onLoadFunctions);
		
	  } catch (Exception e) {
		logger.error("Marshaller error adding Page Rule sections to XML " +e.getMessage());
		e.printStackTrace();
		throw new NEDSSSystemException("Error adding Page Rules to XML " + e.toString());
      }	
	}


	/**
	 * Add batch date rule
	 *	A batch subsection calls a procedure when the Add button is clicked
	 *  which is the Subsection QuestionId followed by the words AddFunction.
	 *  Each rule which is associated with a subsection is grouped under the 
	 *  the function as well as the function placed on the page.
	 *  
	 * @param batchQuesAddFunctionMap (Subsection questionId, OnBatchAddFunction - see Schema)
	 * @param strBatchId - Subsection Question Id
	 * @param WaRuleMetadataDT from the PageElementVO
	 * @param alllocated PageType
	 *
	 */
	private void addBatchDateRule(
			HashMap<String, OnBatchAddFunctionType> batchQuesAddFunctionMap,
			String strBatchId, WaRuleMetadataDT ruleMeta, PageType page) {
		OnBatchAddFunctionType onBatchAddFunction = batchQuesAddFunctionMap.get(strBatchId);
		if (onBatchAddFunction == null) {
			//not in the map??
			logger.error("Batch Subsection not in map??");
			return;
		}
		FunctionToCallType newFunction = onBatchAddFunction.addNewFunctionToCall();
		newFunction.setFunctionName(ruleMeta.getJavascriptFunctionNm());
		newFunction.setErrorMessage(ruleMeta.getErrMsgTxt());
		
		//add the function to the page as well
		//get the function itself to add to the body
		if (ruleMeta.getJavascriptFunction() != null)
			page.addAddedJavaScriptFunction(ruleMeta.getJavascriptFunction());
	}


	private void addDateRule(WaRuleMetadataDT ruleMeta, PageType page) {
			//get the function name and error message for the onSubmit
			if (ruleMeta.getJavascriptFunctionNm() != null) {
				OnSubmitFunctionType onSub = page.addNewOnSubmitFunction();
				onSub.setFunctionName(ruleMeta.getJavascriptFunctionNm());
				onSub.setErrorMessage(ruleMeta.getErrMsgTxt());
			}
			//get the function itself to add to the body
			if (ruleMeta.getJavascriptFunction() != null)
				page.addAddedJavaScriptFunction(ruleMeta.getJavascriptFunction());
	}

	private void addEnableDisableRule(WaRuleMetadataDT ruleMeta, HashMap<String, PageElementType> quesMap, PageType page, Collection <Object> pageRuleCollection) {
		//we need to enable or disable when the page is in Edit Mode
		if (ruleMeta.getJavascriptFunctionNm() == null)
		{
			logger.error("Enable or disable rule has no function name??");
			return;
		}
		if (onLoadFunctions.isEmpty())
			onLoadFunctions =  ruleMeta.getJavascriptFunctionNm() + ";" ;
		else
			onLoadFunctions = onLoadFunctions + ruleMeta.getJavascriptFunctionNm() + ";";
		page.addAddedJavaScriptFunction(ruleMeta.getJavascriptFunction());
		String questionIdentifier = ruleMeta.getSourceQuestionIdentifierString();
		PageElementType element = null;
		element = quesMap.get(questionIdentifier);
		if (element == null){
			logger.error("Marshalling Page Rules: The Rule source question identifier <" + questionIdentifier + "> not found in the map.");
			return;
		}
		//find out the type of question and add the onblur/onchange
		if 	(element.getTextQuestion() != null) {
			String curOnBlur = element.getTextQuestion().getOnBlur();
			if (curOnBlur == null || curOnBlur.isEmpty())
				element.getTextQuestion().setOnBlur(ruleMeta.getJavascriptFunctionNm());
			else
				element.getTextQuestion().setOnBlur(curOnBlur + ";" + ruleMeta.getJavascriptFunctionNm());
		} else if (element.getCodedQuestion() != null) {
			String curOnChange = element.getCodedQuestion().getOnChange();
			if (curOnChange == null || curOnChange.isEmpty())
					element.getCodedQuestion().setOnChange(ruleMeta.getJavascriptFunctionNm());
			else
					element.getCodedQuestion().setOnChange(curOnChange + ";" + ruleMeta.getJavascriptFunctionNm());
			
			
			//Nested Business rule new code
			addNestedFunctions(ruleMeta,element,quesMap, pageRuleCollection);
			
		} else if (element.getDateQuestion() != null) {
			String curOnBlur = element.getDateQuestion().getOnBlur();
			if (curOnBlur == null || curOnBlur.isEmpty())
				element.getDateQuestion().setOnBlur(ruleMeta.getJavascriptFunctionNm());
			else 
				element.getDateQuestion().setOnBlur(curOnBlur + ";" + ruleMeta.getJavascriptFunctionNm());
			String curOnChange = element.getDateQuestion().getOnChange();
			if (curOnChange == null || curOnChange.isEmpty())
				element.getDateQuestion().setOnChange(ruleMeta.getJavascriptFunctionNm());
			else
				element.getDateQuestion().setOnChange(curOnChange + ";" + ruleMeta.getJavascriptFunctionNm());
		} else if (element.getNumericQuestion() != null) {
			String curOnBlur = element.getNumericQuestion().getOnBlur();
			if (curOnBlur == null || curOnBlur.isEmpty())
				element.getNumericQuestion().setOnBlur(ruleMeta.getJavascriptFunctionNm());
			else
				element.getNumericQuestion().setOnBlur(curOnBlur + ";" + ruleMeta.getJavascriptFunctionNm());
		}

	}
	
	
	private void addRequireIfRule(WaRuleMetadataDT ruleMeta, HashMap<String, PageElementType> quesMap, PageType page, Collection <Object> pageRuleCollection) {
		//we need to Require a element when another element is a particular value when the page is in Edit Mode
		if (ruleMeta.getJavascriptFunctionNm() == null)
		{
			logger.error("Require If rule has no function name??");
			return;
		}
		if (onLoadFunctions.isEmpty())
			onLoadFunctions =  ruleMeta.getJavascriptFunctionNm() + ";" ;
		else
			onLoadFunctions = onLoadFunctions + ruleMeta.getJavascriptFunctionNm() + ";";
		page.addAddedJavaScriptFunction(ruleMeta.getJavascriptFunction());
		String questionIdentifier = ruleMeta.getSourceQuestionIdentifierString();
		PageElementType element = null;
		element = quesMap.get(questionIdentifier);
		if (element == null){
			logger.error("Marshalling Page Rules: The Require If rule source question identifier <" + questionIdentifier + "> not found in the map.");
			return;
		}
		//find out the type of question and add the onblur/onchange
		if (element.getCodedQuestion() != null) {
			String curOnChange = element.getCodedQuestion().getOnChange();
			if (curOnChange == null || curOnChange.isEmpty())
					element.getCodedQuestion().setOnChange(ruleMeta.getJavascriptFunctionNm());
			else
					element.getCodedQuestion().setOnChange(curOnChange + ";" + ruleMeta.getJavascriptFunctionNm());
			
			
			//Nested Business rule new code
			//addNestedFunctions(ruleMeta,element,quesMap, pageRuleCollection);
			
		}

	} //addRequireIf
	/*
	 *  Add the Hide/Unhide rule to the onload and onchange.
	 */
	private void addHideOrShowRule(WaRuleMetadataDT ruleMeta, HashMap<String, PageElementType> quesMap, PageType page, Collection <Object> pageRuleCollection) {
		//we need to Hide or Unhide an element when another element is a particular value when the page is in Edit Mode
		if (ruleMeta.getJavascriptFunctionNm() == null)
		{
			logger.error("Hide/Show rule has no function name??");
			return;
		}
		if (onLoadFunctions.isEmpty())
			onLoadFunctions =  ruleMeta.getJavascriptFunctionNm() + ";" ;
		else
			onLoadFunctions = onLoadFunctions + ruleMeta.getJavascriptFunctionNm() + ";";
		page.addAddedJavaScriptFunction(ruleMeta.getJavascriptFunction());

		String questionIdentifier = ruleMeta.getSourceQuestionIdentifierString();
		PageElementType element = null;
		element = quesMap.get(questionIdentifier);
		if (element == null){
			logger.error("Marshalling Page Rules: The Hide/Show Rule source question identifier <" + questionIdentifier + "> not found in the map. Delete the Hide/Show rule.");
			return;
		}
		//find out the type of question and add the onblur/onchange
		if 	(element.getTextQuestion() != null) {
			String curOnBlur = element.getTextQuestion().getOnBlur();
			if (curOnBlur == null || curOnBlur.isEmpty())
				element.getTextQuestion().setOnBlur(ruleMeta.getJavascriptFunctionNm());
			else
				element.getTextQuestion().setOnBlur(curOnBlur + ";" + ruleMeta.getJavascriptFunctionNm());
		} else if (element.getCodedQuestion() != null) {
			String curOnChange = element.getCodedQuestion().getOnChange();
			if (curOnChange == null || curOnChange.isEmpty())
					element.getCodedQuestion().setOnChange(ruleMeta.getJavascriptFunctionNm());
			else
					element.getCodedQuestion().setOnChange(curOnChange + ";" + ruleMeta.getJavascriptFunctionNm());
			
			//Fatima code
			addNestedFunctions(ruleMeta,element,quesMap, pageRuleCollection);
			
			
		} else if (element.getDateQuestion() != null) {
			String curOnBlur = element.getDateQuestion().getOnBlur();
			if (curOnBlur == null || curOnBlur.isEmpty())
				element.getDateQuestion().setOnBlur(ruleMeta.getJavascriptFunctionNm());
			else 
				element.getDateQuestion().setOnBlur(curOnBlur + ";" + ruleMeta.getJavascriptFunctionNm());
			String curOnChange = element.getDateQuestion().getOnChange();
			if (curOnChange == null || curOnChange.isEmpty())
				element.getDateQuestion().setOnChange(ruleMeta.getJavascriptFunctionNm());
			else
				element.getDateQuestion().setOnChange(curOnChange + ";" + ruleMeta.getJavascriptFunctionNm());
		} else if (element.getNumericQuestion() != null) {
			String curOnBlur = element.getNumericQuestion().getOnBlur();
			if (curOnBlur == null || curOnBlur.isEmpty())
				element.getNumericQuestion().setOnBlur(ruleMeta.getJavascriptFunctionNm());
			else
				element.getNumericQuestion().setOnBlur(curOnBlur + ";" + ruleMeta.getJavascriptFunctionNm());
		}

	}//addHideOrShowRule
	
	

	
	//Nested Business rule new function: for now, just one level
	public void addNestedFunctions(WaRuleMetadataDT ruleMeta, PageElementType element, HashMap<String, PageElementType> quesMap, Collection <Object> pageRuleCollection){
		
		String nestedRuleID = ruleMeta.getTargetQuestionIdentifierString();

		if(nestedRuleID.contains(",")){
			List<String> items = Arrays.asList(nestedRuleID.split("\\s*,\\s*"));
			
			for(int i=0; i<items.size(); i++)
				getNestedFunction(items.get(i), element,pageRuleCollection, quesMap);//For more than one nested rule
				
			
		}else
			getNestedFunction(nestedRuleID, element,pageRuleCollection, quesMap);
		
	}
	
	public void getNestedFunction(String nestedRuleID, PageElementType element, Collection <Object> pageRuleCollection, HashMap<String, PageElementType> quesMap){
		
	String funcNested=getNestedRule(nestedRuleID, pageRuleCollection);
	
	if(!funcNested.equals("")){//add the function nested
		
		String curOnChange = element.getCodedQuestion().getOnChange();
		if(!curOnChange.contains(funcNested))//avoid repeats
			element.getCodedQuestion().setOnChange(curOnChange + ";" + funcNested);
		}
	
	
	//Add static rules for Oth questions
	PageElementType pageElement = quesMap.get(nestedRuleID);
	if(pageElement!=null){
		CodedQuestionType codedQues = pageElement.getCodedQuestion();
		if(codedQues!=null){
			String onchange = codedQues.getOnChange();
			
			if(onchange!=null && onchange.contains("enableOrDisableOther")){
				element.getCodedQuestion().setOnChange(element.getCodedQuestion().getOnChange()+";"+onchange);
			}
		}
	}
	
	}
	

	public String getNestedRule(String nestedRuleID, Collection <Object> pageRuleCollection){
		//pageRuleCollection.toArray()("WaRuleMetadataDT")
		pageRuleCollection.toArray();
		String nameFunction="";
		Object[] listRules = pageRuleCollection.toArray();
		for(int i=0; i<listRules.length; i++){
			WaRuleMetadataDT rule2 = (WaRuleMetadataDT)listRules[i];
			if(rule2.getSourceQuestionIdentifierString().equals(nestedRuleID))
				
				if(nameFunction=="")
					nameFunction = rule2.getJavascriptFunctionNm();
				else//added in case the same rule has more than one function (More than one object in pageRuleCollection)
					nameFunction = nameFunction + ";" + rule2.getJavascriptFunctionNm();
			
		}
		
		return nameFunction;
	}
	
	
	//Nested Business rule new function: for now, just one level
	public void addNestedFunctionsOld(WaRuleMetadataDT ruleMeta, PageElementType element, HashMap<String, PageElementType> quesMap, Collection <Object> pageRuleCollection){
		
		String functionNested="";
		//while(nestedBoolean==true){//add for the rest of nested rules
	
		String nestedRuleID = ruleMeta.getTargetQuestionIdentifierString();
		

		String funcNested=getNestedRule(nestedRuleID, pageRuleCollection);

	    
	    //String funcNested2=getNestedRuleOld(nestedRuleID,quesMap);
		//WaRuleMetadataDT nestedRule=getNestedRule(nestedRuleID, pageRuleCollection);
		

		
		
		
		if(!funcNested.equals("")){//add the function nested
			
			String curOnChange = element.getCodedQuestion().getOnChange();
		//	if (curOnChange == null || curOnChange.isEmpty())
					//element.getCodedQuestion().setOnChange(ruleMeta.getJavascriptFunctionNm());
		//	else//always here, delete previous??
			//String addvalue=curOnChange + ";" + functionNested;
			
			//Check if the function has not been already added to curOnChange
			element.getCodedQuestion().setOnChange(curOnChange + ";" + funcNested);
			
		}
	//}
}
	
	public String getNestedRuleOld(String nestedRuleID, HashMap<String, PageElementType> quesMap){
		
		String functionNested="";

		
		String xmlOnchangeStart="<OnChange>";
		String xmlOnchangeEnd="</OnChange>";
		int xmlOnchangeStartLength=xmlOnchangeStart.length();
		functionNested="";
		if(nestedRuleID!=null && nestedRuleID!=""){
			if(quesMap.get(nestedRuleID)!=null)//Do case for more than one restedRuleID
				if(quesMap.get(nestedRuleID).toString().indexOf(xmlOnchangeStart)!=-1)
					functionNested=quesMap.get(nestedRuleID).toString().substring(quesMap.get(nestedRuleID).toString().indexOf(xmlOnchangeStart)+xmlOnchangeStartLength,quesMap.get(nestedRuleID).toString().indexOf(xmlOnchangeEnd));
		
		}
		
		return functionNested;
		
	}
	
	private void addRuleToBucket(WaRuleMetadataDT ruleMeta, PageInfoType pageInfo) {
		// holder for the rules used
		PageRuleType pageRule = pageInfo.addNewPageRule();  //store rule information for review

		if (ruleMeta.getSourceQuestionIdentifierString() != null)
			pageRule.setQuestionIdentifier(ruleMeta.getSourceQuestionIdentifierString());
		if (ruleMeta.getRuleCd() != null)
			pageRule.setRuleCode(ruleMeta.getRuleCd());
		if (ruleMeta.getErrMsgTxt() != null)
			pageRule.setErrorMessage(ruleMeta.getErrMsgTxt());
		if (ruleMeta.getJavascriptFunction() != null)
			pageRule.setJavascriptFunction(ruleMeta.getJavascriptFunction());
		if (ruleMeta.getJavascriptFunctionNm() != null)
			pageRule.setJavascriptFunctionName(ruleMeta.getJavascriptFunctionNm());
	}
	
	/**
	 * See if any part of the rule is in a batch subsection
	 *
	 * @param WaRuleMetadataDT from the PageElementVO
	 * @param question identifier, subsection id map
	 *
	 */
	private String sourceOrTargetInBatch(WaRuleMetadataDT ruleMeta,
			HashMap<String, String> batchQuesMap) {
		String strBatchId = "";
		if (batchQuesMap.containsKey(ruleMeta.getSourceQuestionIdentifierString()))
			strBatchId = (String) batchQuesMap.get(ruleMeta.getSourceQuestionIdentifierString());
		Collection targetColl = ruleMeta.getTargetQuestionIdentifierDTColl();
		if (targetColl != null) {
			Iterator iter = targetColl.iterator();
			while (iter.hasNext()) {
				String targetQuesId = (String) iter.next();
				if (batchQuesMap.containsKey(targetQuesId))
					strBatchId = (String) batchQuesMap.get(targetQuesId);
			}
		}
		return strBatchId;
	}	
	
}
