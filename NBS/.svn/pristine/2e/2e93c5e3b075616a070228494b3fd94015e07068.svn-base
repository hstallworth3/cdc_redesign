<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>   
    
    <xsl:template match="Page" xml:space="preserve">
       <xsl:comment> ### DMB: BEGIN JSP Generic PAGE GENERATE ###--</xsl:comment>
    <html>
    <head>
    <title>NBS: Window</title>
    <xsl:text disable-output-escaping="yes"><![CDATA[<%@ page isELIgnored ="false" %>]]></xsl:text>
    <xsl:text disable-output-escaping="yes"><![CDATA[<%@ include file="/jsp/tags.jsp" %>]]></xsl:text>
    <xsl:text disable-output-escaping="yes"><![CDATA[<%@ include file="/jsp/resources.jsp" %> ]]></xsl:text>
    <xsl:text disable-output-escaping="yes"><![CDATA[<%@ page import="java.util.*" %>]]></xsl:text>
    <xsl:text disable-output-escaping="yes"><![CDATA[<%@ page import="gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj, gov.cdc.nedss.systemservice.nbssecurity.NBSBOLookup, gov.cdc.nedss.systemservice.nbssecurity.NBSOperationLookup, gov.cdc.nedss.util.PageConstants, gov.cdc.nedss.util.PropertyUtil" %>]]></xsl:text>
    <xsl:text disable-output-escaping="yes"><![CDATA[<SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JPageForm.js"></SCRIPT>]]></xsl:text>  
    <xsl:text disable-output-escaping="yes"><![CDATA[<script language="JavaScript" src="Coinfection.js"></script>]]></xsl:text>     
        <script language="JavaScript"> 
        <xsl:text disable-output-escaping="yes"><![CDATA[  
	<%
           response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
           response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
    	%>   
        function cancelForm()
        {
            var confirmMsg="If you continue with the Cancel action, you will lose any information you have entered. Select OK to continue, or Cancel to not continue.";
            if (confirm(confirmMsg)) {
                document.forms[0].action ="${PageForm.attributeMap.Cancel}";
                // pass control to parent's call back handler
  
		var opener = getDialogArgument();                    
		// refresh parent's form
		opener.document.forms[0].action ="/nbs/PageAction.do?method=closeWindow";
                opener.document.forms[0].submit();
                window.returnValue = "windowClosed"; 
		window.close();                
            } else {
                return false;
            }
        }
         var closeWinCalled = false;
         var saveWinCalled = false;
        function handlePageUnload(closePopup, e)
        {
                // This check is required to avoid duplicate invocation 
                // during close button clicked and page unload.
                if (closeWinCalled == false && saveWinCalled == false) {
                    closeWinCalled = true;
                    
            if (e.clientY < 0 || closePopup == true) {
                    	   // pass control to parent's call back handler
			   // refresh parent's form
			   // get reference to opener/parent 

			   var opener = getDialogArgument(); 
			   opener.document.forms[0].action ="/nbs/PageAction.do?method=closeWindow";
			   opener.document.forms[0].submit();          	                    
			   window.returnValue = "windowClosed"; 
			   window.close();
                }
             }
        }
        function saveForm() 
        {
        if (saveWinCalled == false) { //don't submit twice
          saveWinCalled = true;
          var method="${PageForm.attributeMap.method}";  
           
          if(method=="createSubmit"){
                if (pgCheckForErrorsOnSubmit() == true) {
                             saveWinCalled = false;
                             return false;
                }
				CheckIfCoinfectionsShouldBeAssociatedToInterview();
                
          } else   {       
                         if (pgCheckForErrorsOnSubmit() == true) {
                             saveWinCalled = false;
                             return false;
                         } else {
                             document.forms[0].action ="/nbs/PageAction.do?method=editGenericSubmit&ContextAction=Submit";	
                             document.forms[0].submit();          	   
                         }
          }
           
	          var msg = '<div class="submissionStatusMsg"> <div class="header"> Page Builder </div>' +  
	          '<div class="body"> Please wait...  The system is loading the requested page. </div> </div>';         
			  $j.blockUI({  
			            message: msg,  
			     		css: {  
			               top:  ($j(window).height() - 100) /2 + 'px', 
			     		   left: ($j(window).width() - 500) /2 + 'px', 
			     			width: '500px'
			     	    }  
	          });
          }//saveWinCalled = false
        }
        
        function submitForm() 
        {
        
          saveWinCalled = true;
          var method="${PageForm.attributeMap.method}";  
           
          if(method=="createSubmit"){
                         if (pgCheckForErrorsOnSubmit() == true) {
                             saveWinCalled = false;
                             return false;
                         } else {
                              document.forms[0].action ="/nbs/PageAction.do?method=createGenericSubmit&ContextAction=Submit";
                             document.forms[0].submit();
                         }
          } else   {       
                         if (pgCheckForErrorsOnSubmit() == true) {
                             saveWinCalled = false;
                             return false;
                         } else {
                             document.forms[0].action ="/nbs/PageAction.do?method=editGenericSubmit&ContextAction=Submit";	
                             document.forms[0].submit();          	   
                         }
          } 
        }

// validate Interview Date on submit
// from values passed in request
function ixsValidateInterviewDateOnSubmit() {
		var i = 0;
		var errorElts = new Array();
		var errorMsgs = new Array();
  		//check if interview status conflicts with interview Date
  		var ixsStatusElement =getElementByIdOrByName('IXS100');
		if (ixsStatusElement == null || typeof(ixsStatusElement) == 'undefined') {
  			return {elements : errorElts, labels : errorMsgs}
  		}
  		var ixsDateStr =getElementByIdOrByName('IXS101');
		if (ixsDateStr == null || typeof(ixsDateStr) == 'undefined') {
		  	return {elements : errorElts, labels : errorMsgs}
  		}
		var theDateString  =   $j('#IXS101').val();
		if (ixsDateStr == null || ixsDateStr == '') {
			return {elements : errorElts, labels : errorMsgs}
  		}
  		var interviewDate = new Date(theDateString);
  		var interviewStatus = $j("#IXS100 :selected").val();
  		var curDate = new Date();
  		if ((interviewStatus == "COMPLETE") && (interviewDate > curDate)) {
  				var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The " + a1Str +  " cannot be a future date since the Interview Status is Closed/Completed";
	   			errorElts[i] =getElementByIdOrByName('IXS101');
	   			$j('#IXS101L').css("color", "990000");
	   			i++;
	   	}
	   	
	   	var originalInterviewDateStr = '<%=request.getAttribute("ixsOriginalInterviewDate") == null ? "" : request.getAttribute("ixsOriginalInterviewDate")%>';     
		var presumptiveInterviewDateStr = '<%=request.getAttribute("ixsLatestPresumptiveInterviewDate") == null ? "" : request.getAttribute("ixsLatestPresumptiveInterviewDate")%>';     
		var ixsEarliestReInterviewDateStr = '<%=request.getAttribute("ixsEarliestReInterviewDate") == null ? "" : request.getAttribute("ixsEarliestReInterviewDate")%>';
		
		var interviewType = $j("#IXS105 :selected").val();
		if (interviewType == null || interviewType == '') {
			return {elements : errorElts, labels : errorMsgs}
		}
       		var interviewDate = new Date(theDateString);
       		//check if interview type is Original but Original already exists
       		if (originalInterviewDateStr != "") {
       			if (interviewType == "INITIAL") {
       				var a1Str = buildErrorAnchorLink(document.getElementById('IXS105L'), "Interview Type");
				errorMsgs[i] ="Only one Original Interview may occur with the Subject of the Investigation. Please correct the " + a1Str + " and try again.";
	   			errorElts[i] =getElementByIdOrByName('IXS105');
	   			$j('#IXS105L').css("color", "990000");
	   			i++;       			
       			}
       		}
	   	
	   	if (presumptiveInterviewDateStr != "") {
	   		var presumptiveDate = new Date(presumptiveInterviewDateStr);
	   		if ((interviewType == "INITIAL") && (interviewDate.getTime() < presumptiveDate.getTime())){
	   			var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The Interview Date of an Original Interview cannot be prior to the Interview Date of existing Presumptive Interview(s). Please correct the " + a1Str + " and try again.";
	   			errorElts[i] =getElementByIdOrByName('IXS101');
	   			$j('#IXS101L').css("color", "990000");
	   			i++;       			
       			}	   		
	   	}
	   	
	   	if (ixsEarliestReInterviewDateStr != "") {
	   		var reinterviewDate = new Date(ixsEarliestReInterviewDateStr);
	   		if ((interviewType == "INITIAL") && (interviewDate.getTime() > reinterviewDate.getTime())){
	   			var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The Interview Date of an Original Interview cannot be after the Interview Date of existing Re-Interviews. Please correct the " + a1Str + " and try again.";
	   			$j('#IXS101L').css("color", "990000");
	   			i++;       			
       			}	   		
	   	}	
	   	if (originalInterviewDateStr != "") {
	   		var originalDate = new Date(originalInterviewDateStr);
	   		if ((interviewType == "PRESMPTV") && (originalDate.getTime() < interviewDate.getTime())){
	   			var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The Interview Date of a Presumptive Interview cannot be after the Interview Date of the existing Original Interview. Please correct the " + a1Str + " and try again.";
	   			errorElts[i] =getElementByIdOrByName('IXS101');
	   			$j('#IXS101L').css("color", "990000");
	   			i++;       			
       			}	   		
	   	}
	   	if (ixsEarliestReInterviewDateStr != "")  {
	   		var reinterviewDate = new Date(ixsEarliestReInterviewDateStr);
	   		if ((interviewType == "PRESMPTV") && (interviewDate.getTime() > reinterviewDate .getTime())){
	   			var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The Interview Date of a Presumptive Interview cannot be after the Interview Date of the existing Re-interview(s). Please correct the " + a1Str + " and try again.";
	   			errorElts[i] =getElementByIdOrByName('IXS101');
	   			$j('#IXS101L').css("color", "990000");
	   			i++;       			
       			}	   		
	   	}
       		if (originalInterviewDateStr != "") {
       			var originalDate = new Date(originalInterviewDateStr);
       			if ((interviewType == "REINTVW") && (interviewDate.getTime() < originalDate .getTime())){
       				var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The Interview Date of a Re-interview cannot be prior to the Interview Date of the Original Interview. Please correct the " + a1Str + " and try again.";
	   			errorElts[i] =getElementByIdOrByName('IXS101');
	   			$j('#IXS101L').css("color", "990000");
	   			i++;       			
       			}
       		}
	   	if (presumptiveInterviewDateStr != "") {
	   		var presumptiveDate = new Date(presumptiveInterviewDateStr);
	   		if ((interviewType == "REINTVW") && (interviewDate.getTime() < presumptiveDate.getTime())){
	   			var a1Str = buildErrorAnchorLink(document.getElementById('IXS101'), "Interview Date");
				errorMsgs[i] ="The Interview Date of a Re-interview cannot be prior to the Interview Date of existing Presumptive Interview(s). Please correct the " + a1Str + " and try again.";
	   			errorElts[i] =getElementByIdOrByName('IXS101');
	   			$j('#IXS101L').css("color", "990000");
	   			i++;       			
       			}	   		
	   	}
       		if ((interviewType == "REINTVW") && (originalInterviewDateStr == "" && presumptiveInterviewDateStr == "") ) {
       			var a1Str = buildErrorAnchorLink(document.getElementById('IXS105L'), "Interview Type");
			errorMsgs[i] ="A Re-Interview may occur only after a Presumptive or an Original Interview has been created. Please correct the " + a1Str + " and try again.";
	   		errorElts[i] =getElementByIdOrByName('IXS105');
	   		$j('#IXS105L').css("color", "990000");
	   		i++;      			
       		}	   	
	   	if (i == 0) {
	   		$j('#IXS101L').css("color", "black"); //clear if color present
	   		$j('#IXS105L').css("color", "black"); //clear if color present
	   	}
	 	return {elements : errorElts, labels : errorMsgs}
	}         
 
     ]]> </xsl:text>      
              
          
     <xsl:comment> =========Begin Javascript Functions for Dynamic Rules==========</xsl:comment>
     <xsl:for-each select="AddedJavaScriptFunction">
  <xsl:value-of select="." disable-output-escaping="yes"/>
      </xsl:for-each>
     function pgCheckForDynamicRuleErrorsOnSubmit() {
     	var errorElts = new Array();
    	var errorLabels = new Array();
    	var retVal;
     	<xsl:for-each select="OnSubmitFunction"><xsl:text disable-output-escaping="yes">retVal = </xsl:text><xsl:value-of select="FunctionName"/><xsl:text disable-output-escaping="yes">;</xsl:text>
     	   <xsl:text disable-output-escaping="yes"><![CDATA[if (retVal != null && retVal.elements != undefined) { errorElts = errorElts.concat(retVal.elements); errorLabels = errorLabels.concat(retVal.labels); } ]]> </xsl:text>
        </xsl:for-each>
        <xsl:text disable-output-escaping="yes">return {elements : errorElts, labels : errorLabels};</xsl:text>

      }
      
      function pgCheckForFieldsToHighlightOnEdit() {
          //only check if we are in edit mode
          var actionMode =getElementByIdOrByName("actionMode") == null ? "" :getElementByIdOrByName("actionMode").value;
          if(actionMode != 'Edit') { 
              return;
          }
          <xsl:text disable-output-escaping="yes"><![CDATA[
          var strFields = "<%=request.getAttribute("field_list_to_hilight")%>";
          ]]> </xsl:text>
          if(strFields == "null") {
          	return;
          }
          pgProcessErrorFieldsToHilight(strFields);
          
      }      
      
      function pgCheckDynamicRulesOnLoad() {
           <xsl:if test="OnLoadFunctions">
           	<xsl:value-of select="OnLoadFunctions" disable-output-escaping="yes"/>
          </xsl:if>
		  csMotherInfantRequired();
       return;
      }

	<xsl:comment> === batch subsection add edit check functions (if any) follow ===</xsl:comment>
     	<xsl:for-each select="OnBatchAddFunction"><xsl:text disable-output-escaping="yes">function pg</xsl:text><xsl:value-of select="BatchSubsectionIdentifier"/><xsl:text disable-output-escaping="yes">BatchAddFunction()</xsl:text>
     	   <xsl:text disable-output-escaping="yes"><![CDATA[ { 
    			var errorLabels = new Array();
    			var retVal;
    			var retRule;
    			]]> </xsl:text>
     	        <xsl:for-each select="FunctionToCall">
     	   		<xsl:text disable-output-escaping="yes"><![CDATA[retRule=]]></xsl:text><xsl:value-of select="FunctionName"/><xsl:text disable-output-escaping="yes"><![CDATA[; ]]></xsl:text>
     	   		<xsl:text disable-output-escaping="yes"><![CDATA[if (retRule != null && retRule.labels != undefined) { errorLabels = errorLabels.concat(retRule.labels); } ]]></xsl:text>
        	</xsl:for-each>
        	
        	<xsl:text disable-output-escaping="yes"><![CDATA[retVal=pgCheckForErrorsOnBatchSubsection(']]></xsl:text><xsl:value-of select="BatchSubsectionIdentifier"/><xsl:text disable-output-escaping="yes"><![CDATA[');]]></xsl:text>
         	<xsl:text disable-output-escaping="yes"><![CDATA[if (retVal != null && retVal != undefined) { errorLabels = errorLabels.concat(retVal); } ]]></xsl:text>

        
        	<xsl:text disable-output-escaping="yes"><![CDATA[if (errorLabels.length > 0) {]]></xsl:text>
        	<xsl:text disable-output-escaping="yes"><![CDATA[displayErrors(']]></xsl:text><xsl:value-of select="BatchSubsectionIdentifier"/><xsl:text disable-output-escaping="yes"><![CDATA[errorMessages', errorLabels); return false;}]]></xsl:text>
		<xsl:text disable-output-escaping="yes"><![CDATA[$j('#]]></xsl:text><xsl:value-of select="BatchSubsectionIdentifier"/><xsl:text disable-output-escaping="yes"><![CDATA[errorMessages').css("display", "none");]]></xsl:text>
		<xsl:text disable-output-escaping="yes"><![CDATA[return true;]]></xsl:text>
		<xsl:text disable-output-escaping="yes"><![CDATA[}]]></xsl:text>
        </xsl:for-each>
      
     </script>
     
      
<xsl:text disable-output-escaping="yes"><![CDATA[ 
    <% 
        Map map = new HashMap();
        if(request.getAttribute("SubSecStructureMap") != null){
           map =(Map)request.getAttribute("SubSecStructureMap");              
        }
     %>
  ]]> </xsl:text>
  
  <script language="JavaScript"> 
  <xsl:text disable-output-escaping="yes"><![CDATA[      
    var answerCache = { }; //global for batch records
    var viewed = -1,count=0;   
                    
    function populateBatchRecords()
    {
       dwr.engine.beginBatch();
       var map,ans;          
       JPageForm.getBatchEntryMap(function(map) {
          for (var key in map) {
            count++;
            fillTable(key,"pattern"+key ,"questionbody"+key );
          } 		 
        }); 	
        dwr.engine.endBatch();
    }		  

    function writeQuestion( subSecNm,pattern,questionbody) {	
	    var t =getElementByIdOrByName(subSecNm); 	
        var len=0;
	    //t.style.display = "block";
	    <% String[][]  batchrecinsert  =new String[20][7];  
	       if(map != null){ 
		     Iterator itLab1 = map.entrySet().iterator(); 
		     while(itLab1.hasNext()){  
			   Map.Entry pair = (Map.Entry)itLab1.next();
		%>
							     
	     if(subSecNm == "<%=pair.getKey().toString()%>"){
		<% batchrecinsert  =  (String[][])pair.getValue();  
		 for(int i=0;i<batchrecinsert.length;i++){   
			String checknull1 = batchrecinsert[i][0]; 
			if(checknull1 != null && checknull1 != ""){%>
		var key =   "<%=batchrecinsert[i][0]%>";
		if(key != null && key != 'undefined' && key != ''){
			len =  len +1; 
		}
		<%} }%>
	    }
	<%} }%>
		for (var i = 0; i <len+1; i++){
		   $j($j(t).find("tbody > tr:odd").get(i)).css("background-color","#DCE7F7");
		   $j($j(t).find("tbody > tr:even").get(i)).css("background-color","#DCE7F7");
		}
		var map = {};	var emptyrow="yes";
		var code = "1013";
		dwr.engine.beginBatch(); 
		<% batchrecinsert  =new String[20][7];  
		if(map != null){ 
			Iterator itSSMap = map.entrySet().iterator();
			while(itSSMap.hasNext()){ 
				Map.Entry pair = (Map.Entry)itSSMap.next();%>
				if(subSecNm == "<%=pair.getKey().toString()%>"){
					<% batchrecinsert  =  (String[][])pair.getValue();
					   for(int i=0;i<batchrecinsert.length;i++){
				            String checkifnull  = batchrecinsert[i][0];					
					    if(checkifnull  != null && checkifnull  != "null" && checkifnull  != ""){ %>
						var qId= "<%=batchrecinsert[i][0]%>";
						var componentId = "<%=batchrecinsert[i][5]%>";
						if(qId != null && qId != "null" && qId != ' '){
							if(document.getElementById(qId) != null){
								map[qId] = getRepeatingBlockUtilDispText(qId, componentId);
								<%if( "1017".equalsIgnoreCase(batchrecinsert[i][5]) ){%>
                                    map[qId + "Disp"] = $j("#" + qId + "Disp").html();
                                <%}%>
								emptyrow = repeatingBlockCheckForEmptyRow(qId, emptyrow);
							}
			 			}
			<%} }%>
				} 
			<%} }%>
			var batchentry = { subsecNm:subSecNm, id:viewed,answerMap:map};  
			 if(emptyrow=="yes"){
					var errorrow= subSecNm+"errorMessages";
					displayErrors(errorrow, " At least one field must be entered when adding a repeating block.");
		            dwr.engine.endBatch();	
		            return false;
			 }  				

			JPageForm.setAnswer(batchentry,"<%=request.getSession()%>");
			fillTable(subSecNm,pattern,questionbody);
			<%  if(map != null){ 
				Iterator itSSMap = map.entrySet().iterator();
				while(itSSMap.hasNext()){ 
					Map.Entry pair = (Map.Entry)itSSMap.next();%>
			if(subSecNm == "<%=pair.getKey().toString()%>"){
				<% batchrecinsert  =  (String[][])pair.getValue();
					 for(int i=0;i<batchrecinsert.length;i++){  
					String checknull1 = batchrecinsert[i][0];
					if(checknull1 != null && checknull1 != ""){%> 
				var key =   "<%=batchrecinsert[i][0]%>";
				if(key != null &&getElementByIdOrByName(key) != null){
					dwr.util.setValue(key, "");
					if(key+"Oth" != null &&getElementByIdOrByName(key+"Oth") != null){
						dwr.util.setValue(key+"Oth", "");
					}
					if(key+"UNIT" != null &&getElementByIdOrByName(key+"UNIT") != null){
						dwr.util.setValue(key+"UNIT", "");
					}
					if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){
						displaySelectedOptions(document.getElementById(key), key+"-selectedValues");
					}
					var type = "<%=batchrecinsert[i][5]%>"; 
					if(type == "1007" || type == "1013"){	
						if(document.getElementById(key) != null){ 
							autocompTxtValuesForJSPByElement(key);
						}
					}
					if (type == '1017') {
						repeatingBlockClearParticipant(key);
					}
					if(document.getElementById(key+"UNIT") != null ) {
						autocompTxtValuesForJSPByElement(key+"UNIT"); 
		            }
		        }
				<%} }%> 
			 }
			<%} }%> 
			var rowhide =getElementByIdOrByName("AddButtonToggle"+subSecNm);
			if(rowhide!=null){
			rowhide.style.display = '';
			}
			var rowshow =getElementByIdOrByName("AddNewButtonToggle"+subSecNm);
			if(rowshow!=null){
			rowshow.style.display = 'none';
			}
			var rowshow1 =getElementByIdOrByName("UpdateButtonToggle"+subSecNm);
			if(rowshow1!=null){
			rowshow1.style.display = 'none';
			}
			
			viewed = -1;
			dwr.engine.endBatch();
    }


    function editClicked(eleid,subSecNm) {
		unhideBatchImg(subSecNm);
		dwr.engine.beginBatch(); 
		clearClicked(subSecNm); 
		// id of the form "edit{id}", eg "edit42". We lookup the "42"
		var answer = answerCache[eleid.substring(4+subSecNm.length)];
		viewed = answer.id;	 
		var map = answer.answerMap;
		var mulVal;
		var partVal;
		var othVal;
		var selectedmulVal;
		var handlemulVal;
		var code = "1013";	
	  //Specific code for  country to state and state to county mapping
	var stateCode = answer.answerMap['INV503'];
	if(stateCode != null && stateCode != "" &&getElementByIdOrByName('INV505') != null){
		stateCode = stateCode.substring(0, stateCode.indexOf("$$"));
		JPageForm.getDwrCountiesForState(stateCode, function(data) {
		DWRUtil.removeAllOptions("INV505");
		DWRUtil.addOptions("INV505", data, "key", "value" );
		});
	}
	var countryCode = answer.answerMap['INV502'];
	if(countryCode != null && countryCode != "" &&getElementByIdOrByName('INV503') != null){
		countryCode = countryCode.substring(0, countryCode.indexOf("$$"));
		JPageForm.getfilteredStatesByCountry(countryCode, function(data) {
		DWRUtil.removeAllOptions("INV503");
		DWRUtil.addOptions("INV503", data, "key", "value" );
		});
	}
	<% String[][] batchrecedit  =new String[20][7]; 
	if(map != null) {
		Iterator  itLab2 = map.entrySet().iterator(); 
		while(itLab2.hasNext()){  
			Map.Entry pair = (Map.Entry)itLab2.next();%>
			if(subSecNm == "<%=pair.getKey().toString()%>"){
		<% batchrecedit  =  (String[][])pair.getValue();
		    for(int i=0;i<batchrecedit.length;i++){
			if(  null != batchrecedit[i][0]) { 
				 String str1 = batchrecedit[i][0] + "UNIT" ;  %>
			dwr.util.setValue( "<%=batchrecedit[i][0]%>","");
			<%  str1 = batchrecedit[i][0] + "UNIT" ;  %>
                        dwr.util.setValue( "<%=str1%>","");
                        dwr.util.setValue( "<%=batchrecedit[i][0]%>"+"Oth","");
			var type = "<%=batchrecedit[i][5]%>"; 
			if( type == "1007" || type == "1013"){
				if(document.getElementById("<%=batchrecedit[i][0]%>") != null){
					autocompTxtValuesForJSPByElement("<%=batchrecedit[i][0]%>");
				}
			}
			if (type == "1017") {
				repeatingBlockReadyParticipantEdit("<%=batchrecedit[i][0]%>");
			}
			<%  str1 = batchrecedit[i][0] + "UNIT"; %>
			if(document.getElementById("<%=str1%>") != null ) {
				   autocompTxtValuesForJSPByElement("<%=str1%>"); 
			}
			<%}}%>
			JPageForm.updateAnswer(answer,function(answer) { 
			
			   for (var key in answer.answerMap) { 
	    			var uiComponent = "";
	    			<% for(int i=0;i<batchrecedit.length;i++){
	    		    	if(  null != batchrecedit[i][0]) { %> 
					if(key == "<%=batchrecedit[i][0]%>" )
						uiComponent = "<%=batchrecedit[i][5]%>";
				<%}}%>					
			    if(answer.answerMap[key] != null && answer.answerMap[key] != '' &&  (uiComponent == "1013" || uiComponent == "1017")){
				<% for(int i=0;i<batchrecedit.length;i++){
					  if(  null != batchrecedit[i][0]) { %> 
				if(key == "<%=batchrecedit[i][0]%>" && code == "<%=batchrecedit[i][5]%>"){
					mulVal = answer.answerMap[key]; 
					repeatingBlockHandleMultiVal (mulVal, key);
				} 
				else if (key == "<%=batchrecedit[i][0]%>" && "1017" == "<%=batchrecedit[i][5]%>"){
					    			partVal = answer.answerMap[key];
					    			repeatingBlockHandleEditParticipant (partVal, key, answer.answerMap[key + "Disp"]);
	    			}
				if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){
							displaySelectedOptions(document.getElementById(key), key+"-selectedValues");
				}

			<% }}%>	
			}else if(answer.answerMap[key] != null && answer.answerMap[key] != '' && answer.answerMap[key].indexOf(":") !=  -1 &&getElementByIdOrByName(key+"Oth") != undefined){	
					var otherVal = answer.answerMap[key];			 
					dwr.util.setValue(key,otherVal.substring(0,otherVal.indexOf(":")));
					dwr.util.setValue(key+"Oth", otherVal.substring(otherVal.indexOf(":")+1));
					document.getElementById(key+"Oth").disabled=false;
			} else if(answer.answerMap[key] != null && answer.answerMap[key] != '' && answer.answerMap[key].indexOf("$sn$") !=  -1){
					var fval = answer.answerMap[key];								 
					dwr.util.setValue(key,fval.substring(0,fval.indexOf("$sn$"))); 
					// alert(fval.substring(structVal.length+1,fval.length));
					dwr.util.setValue(key+"UNIT", fval.substring(fval.indexOf("$sn$")+4,fval.length));
			} else {    
				// alert( answer.answerMap[key] + "....");
				mulVal = answer.answerMap[key];
				if(mulVal  != null && mulVal.indexOf("$MulOth$") != -1){ 
					othVal =  mulVal.substring(mulVal.indexOf("$MulOth$")+8, mulVal.indexOf("#MulOth#"));
			                mulVal = mulVal.substring(0,mulVal.indexOf("$MulOth$") );
			                if(mulVal  != null && mulVal != ''){	
			                       getElementByIdOrByName(key).value = mulVal ;
					}
                                        if(othVal != null && othVal != ''){	 
			                        getElementByIdOrByName(key+"Oth").value = othVal;
			                }
			          }else{
					if(answer.answerMap[key] != null && answer.answerMap[key] != ''){ 
						document.getElementById(key).value = answer.answerMap[key];
					}
			} 
			//getElementByIdOrByName(key).value=answer.answerMap[key];        
			// alert("id = " +key+ " val = "+document.getElementById(key));
		}
		if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){
				displaySelectedOptions(document.getElementById(key), key+"-selectedValues");
		}
	}
	for (var key in answer.answerMap) {
	     <% for(int i=0;i<batchrecedit.length;i++){
		  if(  null != batchrecedit[i][0]) { %> 
		var type = "<%=batchrecedit[i][5]%>"; 
		if(key == "<%=batchrecedit[i][0]%>" &&( type == "1007" || type == "1013")){
			if(document.getElementById(key) != null){
				autocompTxtValuesForJSPByElement(key);
			}
		}
		<% String str1 = batchrecedit[i][0] + "UNIT" ;  %>
		if(document.getElementById("<%=str1%>") != null &&getElementByIdOrByName("<%=str1%>").value != null &&getElementByIdOrByName("<%=str1%>").value != '') {
			autocompTxtValuesForJSPByElement("<%=str1%>"); 
		}
	<%}}%> 		
	}    
	var rowhide =getElementByIdOrByName("AddButtonToggle"+subSecNm);
	if(rowhide!=null){
	rowhide .style.display = 'none';	
	}
	var rowshow =getElementByIdOrByName("AddNewButtonToggle"+subSecNm);
	if(rowshow!=null){
	rowshow.style.display = 'none';
	}
	var rowshow1 =getElementByIdOrByName("UpdateButtonToggle"+subSecNm);
	if(rowshow1!=null){
	rowshow1.style.display = '';
	}
	<% if(batchrecedit != null && batchrecedit.length > 0 ){
	    for(int i=0;i<batchrecedit.length;i++){
		  if(null != batchrecedit[i][0]) {  
			 String str = batchrecedit[i][0] +"L"; 
			 String str1 = batchrecedit[i][0] +"Oth"; %>
		var key = "<%=batchrecedit[i][0]%>";
		var keyL =  "<%=str%>";
		var keyOth = "<%=str1%>";
		if(document.getElementById(key) != null){
			// alert(document.getElementById(key).value);
			document.getElementById(key).disabled = false;
			document.getElementById(keyL).disabled = false;
			$j("#"+key).parent().parent().find("img").attr("disabled", false);
			$j("#"+key).parent().parent().find("input").attr("disabled", false);
		}
		if(document.getElementById(key+"Oth") != null){
			//alert(document.getElementById(key+"Oth").value);
			//alert(document.getElementById(key+"Oth").disabled);
			document.getElementById(key+"Oth").disabled = false;
			//alert(document.getElementById(key+"OthL").disabled);
			document.getElementById(key+"OthL").disabled = false;
			$j("#"+key+"Oth").parent().parent().find("input").attr("disabled", false);
			enableOrDisableOther(key) ;
		}
		<%}}}%>
	  	});
     		}
    	<%}}%>
     	dwr.engine.endBatch(); 
	}
	
	function fillTable(subSecNm,pattern,questionbody) {
	  JPageForm.getAllAnswer(subSecNm,function(answer) {
		  // Delete all the rows except for the "pattern" row
		  dwr.util.removeAllRows(questionbody, { filter:function(tr) { return (tr.id != pattern); }});
          dwr.util.setEscapeHtml(false);
		  // Create a new set cloned from the pattern row
		  var ans, id,rowclass="";
		<% if(map !=  null){
			Iterator    itLab3 = map.entrySet().iterator(); 
			String[][] batchrecview  = null; 
			while(itLab3.hasNext()){  
				Map.Entry pair = (Map.Entry)itLab3.next();%>
		if(subSecNm == "<%=pair.getKey().toString()%>"){
			<% batchrecview =  (String[][])pair.getValue();%>  
		if(answer !=null && answer.length != 0){
			for (var i = 0; i < answer.length; i++){
				ans = answer[i];		; 
				id = ans.id;	     
				dwr.util.cloneNode(pattern, { idSuffix:id });
			  <% for(int i=0;i<batchrecview.length;i++){
				String checknull = batchrecview[i][0]; 
				if(checknull != null && checknull != ""){ %>
				for (var key in ans.answerMap) {
					if(!(key == null || key == 'null') && key == "<%=batchrecview[i][0]%>"){
					    var val = ans.answerMap[key];
						<%if( "1017".equalsIgnoreCase(batchrecview[i][5]) ){%>
					    val = ans.answerMap[key + "Disp"] ? ans.answerMap[key + "Disp"] : val ;
                        <%}%>
					    val = repeatingBlockFillValue(val);
					    dwr.util.setValue("table" + key + id, val);
					}
				}
		<%}}%>
		$(pattern + id).style.display = "";   
		answerCache[id] = ans;
		if(rowclass=="")
			rowclass="odd";	
			document.getElementById(pattern  + id).setAttribute("className",rowclass);
			// alert("Alert new "+document.getElementById(pattern  + id).getAttribute("className"));
			$j("#" + pattern  + id).css("background-color","white");
			// alert("Alert new "+document.getElementById(pattern  + id));
			if(rowclass=="odd"){
				rowclass = "odd";
			} else if(rowclass=="even"){
				rowclass = "odd";
			} 
		  }
		  $j("#no"+pattern).hide();
	    } else{	
		  $j("#no"+pattern).show(); 
	    }  //if else answer.length ==0 ends 
	  }
	<%}}%>
	    clearClicked(subSecNm); 
	  });
    }

function deleteClicked(eleid,subSecNm,pattern,questionbody) { 
                		
      var t =getElementByIdOrByName(subSecNm); 	
      var len=0;
      //t.style.display = "block";    
      <%    String[][]  batchrecinsert2  =new String[20][7];  
       if(map != null){ 
		Iterator itLab1 = map.entrySet().iterator(); 
		while(itLab1.hasNext()){ 		   
		Map.Entry pair = (Map.Entry)itLab1.next();%>
	   if(subSecNm == "<%=pair.getKey().toString()%>"){
		<% batchrecinsert2  =  (String[][])pair.getValue();  
		 for(int i=0;i<batchrecinsert2.length;i++){   
		String checknull1 = batchrecinsert2[i][0];  
		if(checknull1 != null && checknull1 != ""){%> 
			var key =   "<%=batchrecinsert2[i][0]%>";
			if(key != null && key != 'undefined' && key != ''){
				len =  len +1;
			}
		<%} }%>
	   	}
	   <%} }%>
	for (var i = 0; i <len+1; i++){
		$j($j(t).find("tbody > tr:odd").get(i)).css("background-color","#DCE7F7");
		$j($j(t).find("tbody > tr:even").get(i)).css("background-color","#DCE7F7");
	}
	for (var i = 0; i < len+1; i ++)   {
		$j($j("#" + "questionbody"  +subSecNm).find("tr").get(i)).css("background-color","white");	
	}		
        // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
        var answer = answerCache[eleid.substring(6+subSecNm.length)];
        if (confirm("You have indicated that you would like to delete this row. Would you like to continue with this action?")) {
             dwr.engine.beginBatch();
             JPageForm.deleteAnswer(answer);
             fillTable(subSecNm,pattern,questionbody);
         <%  String[][] batchrecdel  =new String[20][7];  
	     if(map != null){ 
		Iterator itLab8 = map.entrySet().iterator();
		while(itLab8.hasNext()){		   
		   Map.Entry pair = (Map.Entry)itLab8.next();%>
	if(subSecNm == "<%=pair.getKey().toString()%>"){
	<% batchrecdel  =  (String[][])pair.getValue();
           for(int i=0;i<batchrecdel.length;i++){   
	   String delstr =  batchrecdel[i][0];
	   if(delstr != null && delstr != ""){ %>	
	   var key =   "<%=batchrecdel[i][0]%>";
	   if(key != null &&getElementByIdOrByName(key) != null){
		dwr.util.setValue(key, "");
		if(key+"Oth" != null &&getElementByIdOrByName(key+"Oth") != null){
			dwr.util.setValue(key+"Oth", "");
		}
		if(key+"UNIT" != null &&getElementByIdOrByName(key+"UNIT") != null){
			dwr.util.setValue(key+"UNIT", "");
		}	     
		if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){
			displaySelectedOptions(document.getElementById(key), key+"-selectedValues")
		}  
		var type = "<%=batchrecdel[i][5]%>";
		if(type == "1007" || type == "1013"){
			autocompTxtValuesForJSPByElement(key);
		}
		if(document.getElementById(key+"UNIT") != null ) {
			autocompTxtValuesForJSPByElement(key+"UNIT"); 
		}
	}
	<%}}%>
	}
	<%}}%>
	var rowhide =getElementByIdOrByName("AddButtonToggle"+subSecNm);
	if(rowhide!=null){
	rowhide.style.display = '';
	}		                          
	var rowshow =getElementByIdOrByName("AddNewButtonToggle"+subSecNm);
	if(rowshow!=null){
	rowshow.style.display = 'none';
	}
	var rowshow1 =getElementByIdOrByName("UpdateButtonToggle"+subSecNm);
	if(rowshow1!=null){
	rowshow1.style.display = 'none';
	}
	clearClicked(subSecNm);
	viewed = -1;
        dwr.engine.endBatch();
    }
} 


function clearQuestion() {
    viewed = -1;
    dwr.util.setValues({subsecNm:"Others", id:viewed,answerMap:null });
}

function getDropDownValues(newValue)
{
    JPageForm.getDropDownValues(newValue, function(data) {
        dwr.util.removeAllOptions(newValue);  
        dwr.util.addOptions(newValue,data,"key","value"); 
    });
}
                  
function viewClicked(eleid,subSecNm) {	
	var t =getElementByIdOrByName(subSecNm);
	var len=0;
	//t.style.display = "block";
     <% String[][]  batchrecinsert1  =new String[20][7];  
	if(map != null){
		Iterator itLab1 = map.entrySet().iterator(); 
		while(itLab1.hasNext()){  
			Map.Entry pair = (Map.Entry)itLab1.next();%>
	if(subSecNm == "<%=pair.getKey().toString()%>"){
     <% batchrecinsert1  =  (String[][])pair.getValue();
        for(int i=0;i<batchrecinsert1.length;i++){   
            String checknull1 = batchrecinsert1[i][0]; 
            if(checknull1 != null && checknull1 != ""){%> 
		var key =   "<%=batchrecinsert1[i][0]%>";
		if(key != null && key != 'undefined' && key != ''){
			len =  len +1;
		}
    <%}}%>
        }
    <%}}%>
	for (var i = 0; i <len+1; i++){
		$j($j(t).find("tbody > tr:odd").get(i)).css("background-color","#DCE7F7");
		$j($j(t).find("tbody > tr:even").get(i)).css("background-color","#DCE7F7");
	}
	for (var i = 0; i < len+1; i ++) {
		$j($j("#" + "questionbody"  +subSecNm).find("tr").get(i)).css("background-color","white");
	}
	var key;
	dwr.engine.beginBatch(); 
	clearClicked(subSecNm); 
	// id of the form "edit{id}", eg "edit42". We lookup the "42"
	var answer = answerCache[eleid.substring(4+subSecNm.length)];
	viewed = answer.id;	 
	var map = answer.answerMap;	 
	var mulVal;
	var partVal;
	var selectedmulVal;
	var handlemulVal;
	var code = "1013";	
	<% String[][] batchrecedit1  = null;
	if(map != null) {
		Iterator  itLab2 = map.entrySet().iterator(); 
		while(itLab2.hasNext()){  
		Map.Entry pair = (Map.Entry)itLab2.next();%>
	if(subSecNm == "<%=pair.getKey().toString()%>"){
	<% batchrecedit1  =  (String[][])pair.getValue(); 
	     for(int i=0;i<batchrecedit1.length;i++){
		 if(  null != batchrecedit1[i][0]) {  
		 	String str1 = batchrecedit1[i][0] + "UNIT" ;  %>
		dwr.util.setValue( "<%=batchrecedit1[i][0]%>","");
		<%  str1 = batchrecedit1[i][0] + "UNIT" ;  %>
                dwr.util.setValue( "<%=str1%>","");
                dwr.util.setValue( "<%=batchrecedit1[i][0]%>"+"Oth","");
		var type = "<%=batchrecedit1[i][5]%>";
		if( type == "1007" || type == "1013"){
		    if(document.getElementById("<%=batchrecedit1[i][0]%>") != null){
			autocompTxtValuesForJSPByElement("<%=batchrecedit1[i][0]%>");
		    }
		}
		<%  str1 = batchrecedit1[i][0] + "UNIT" ;  %>
		if(document.getElementById("<%=str1%>") != null ) {
			autocompTxtValuesForJSPByElement("<%=str1%>"); 
		}
	<%}}%>
	JPageForm.updateAnswer(answer,function(answer) {
	    for (var key in answer.answerMap) {
	    	var uiComponent = "";
	    		<% for(int i=0;i<batchrecedit1.length;i++){
	    		    if(  null != batchrecedit1[i][0]) { %> 
				if(key == "<%=batchrecedit1[i][0]%>" )
					uiComponent = "<%=batchrecedit1[i][5]%>";
			<%}}%>		
		if(answer.answerMap[key] != null && answer.answerMap[key] != '' && (uiComponent == "1013" || uiComponent == "1017")){
			<% for(int i=0;i<batchrecedit1.length;i++){
			     if(  null != batchrecedit1[i][0]) { %> 
			if(key == "<%=batchrecedit1[i][0]%>" && code == "<%=batchrecedit1[i][5]%>"){
				mulVal = answer.answerMap[key]; 
				repeatingBlockHandleMultiVal (mulVal, key);
	    		} else if (key == "<%=batchrecedit1[i][0]%>" && "1017" == "<%=batchrecedit1[i][5]%>"){
	    			partVal = answer.answerMap[key];
	    			repeatingBlockHandleViewParticipant (partVal, key, answer.answerMap[key + "Disp"]);
	    		}
	    			
	    if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){
		displaySelectedOptions(document.getElementById(key), key+"-selectedValues")
	    }					
	<%}}%>					
	}else if(answer.answerMap[key] != null && answer.answerMap[key] != '' && answer.answerMap[key].indexOf(":") !=  -1 &&getElementByIdOrByName(key+"Oth") != undefined){	
		var otherVal = answer.answerMap[key];
		dwr.util.setValue(key,otherVal.substring(0,otherVal.indexOf(":")));
		dwr.util.setValue(key+"Oth", otherVal.substring(otherVal.indexOf(":")+1));
		document.getElementById(key+"Oth").disabled=false;
	}else if(answer.answerMap[key] != null && answer.answerMap[key] != '' && answer.answerMap[key].indexOf("$sn$") !=  -1){	
		var fval = answer.answerMap[key];
		dwr.util.setValue(key,fval.substring(0,fval.indexOf("$sn$"))); 
		dwr.util.setValue(key+"UNIT", fval.substring(fval.indexOf("$sn$")+4,fval.length));
	}else {    
		mulVal = answer.answerMap[key]; 						
		if(mulVal != null && mulVal.indexOf("$MulOth$") != -1){
			othVal =  mulVal.substring(mulVal.indexOf("$MulOth$")+8, mulVal.indexOf("#MulOth#"));
			mulVal = mulVal.substring(0,mulVal.indexOf("$MulOth$") );
			if(mulVal  != null && mulVal  != ''){	
			    getElementByIdOrByName(key).value  = othVal ;
			}
                 if(othVal != null && othVal != ''){
                       getElementByIdOrByName(key+"Oth").value = othVal ;
		}
	}else{
		if(answer.answerMap[key] != null && answer.answerMap[key] != ''){
			document.getElementById(key).value  = answer.answerMap[key];
		}
	}

	}
	if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){
		displaySelectedOptions(document.getElementById(key), key+"-selectedValues");
	}
	}
	for (var key in answer.answerMap) {
		<% for(int i=0;i<batchrecedit1.length;i++){
		     if(  null != batchrecedit1[i][0]) { %>
	    var type = "<%=batchrecedit1[i][5]%>";
	    if(key == "<%=batchrecedit1[i][0]%>" &&( type == "1007" || type == "1013")){
		if(document.getElementById(key) != null){
			autocompTxtValuesForJSPByElement(key);
		}
	    }
	<% String str1 = batchrecedit1[i][0] + "UNIT" ;  %>
	if(document.getElementById("<%=str1%>") != null &&getElementByIdOrByName("<%=str1%>").value != null &&getElementByIdOrByName("<%=str1%>").value != '') {
		autocompTxtValuesForJSPByElement("<%=str1%>"); 
	}	
	<%}}%> 	
	}
	var rowhide =getElementByIdOrByName("AddButtonToggle"+subSecNm);
	if(rowhide!=null){
	rowhide .style.display = 'none';
	}
	var rowshow =getElementByIdOrByName("AddNewButtonToggle"+subSecNm);
	if(rowshow!=null){
	rowshow.style.display = 'none';
	}
	var rowshow1 =getElementByIdOrByName("UpdateButtonToggle"+subSecNm);
	if(rowshow1!=null){
	rowshow1.style.display = '';
	}
	<% if(batchrecedit1 != null && batchrecedit1.length > 0 ){
	     for(int i=0;i<batchrecedit1.length;i++){
		  if(null != batchrecedit1[i][0]) { 
			 String str = batchrecedit1[i][0] +"L";
		 	String str1 = batchrecedit1[i][0] +"Oth"; %>
		var key = "<%=batchrecedit1[i][0]%>";
		var keyL =  "<%=str%>";
		var keyOth = "<%=str1%>";
		if(document.getElementById(key) != null){
			document.getElementById(key).disabled = false;
			document.getElementById(keyL).disabled = false;
			$j("#"+key).parent().parent().find("img").attr("disabled", false);
			$j("#"+key).parent().parent().find("input").attr("disabled", false);
		}
		if(document.getElementById(key+"Oth") != null){					
			document.getElementById(key+"Oth").disabled = true;
			$j("#"+key+"Oth").parent().parent().find("input").attr("disabled", false);
		}
	<%}}}%>		

	<% String[][] batchrecview  = null;
	if(map != null) {
		Iterator  itLab21 = map.entrySet().iterator(); 
		while(itLab21.hasNext()){  
			pair = (Map.Entry)itLab21.next();%>
	if(subSecNm == "<%=pair.getKey().toString()%>"){
	    <%  batchrecview  =  (String[][])pair.getValue(); 
	    for(int i=0;i<batchrecview.length;i++){
		 if(null != batchrecview[i][0]) {  
		     String str = batchrecview[i][0] +"L"; 
		     String str1 = batchrecview[i][0] +"Oth"; %>
	    key = "<%=batchrecview[i][0]%>";
	    component = "<%=batchrecview[i][5]%>";
	    var keyL =  "<%=str%>";
	    var keyOth = "<%=str1%>";
	    if(document.getElementById(key) != null){
	    	if (component != "1019") 
			document.getElementById(key).disabled = true;
		document.getElementById(keyL).disabled = true;
		$j("#"+key).parent().parent().find("img").attr("disabled", true);
		$j("#"+key).parent().parent().find("input").attr("disabled", true);
	    }
	if(key+"-selectedValues" != null &&getElementByIdOrByName(key+"-selectedValues") != null){		
		document.getElementById(key+"-selectedValues").disabled = true;
	}
	if(document.getElementById(key+"Oth") != null){
		document.getElementById(key+"Oth").disabled = true;
		document.getElementById(key+"OthL").disabled = true;
		$j("#"+key+"Oth").parent().parent().find("input").attr("disabled", 	true);
	}

	<%}}%>
	}		        
	<%}}%>				               
	var rowhide =getElementByIdOrByName("AddButtonToggle"+subSecNm);
	if(rowhide!=null){
	rowhide .style.display = 'none';		
	}
	var rowshow =getElementByIdOrByName("AddNewButtonToggle"+subSecNm);
	if(rowshow!=null){
	rowshow.style.display = '';
	}
	var rowshow1 =getElementByIdOrByName("UpdateButtonToggle"+subSecNm);
	if(rowshow1!=null){
	rowshow1.style.display = 'none'; 
	}
	});
	}
	<%}}%>
	dwr.engine.endBatch();
        }      //viewClicked
        
   function clearClicked(subSecNm) {
	var key;
	var subsectionDisabled = $j("#"+subSecNm).hasClass("batchSubSectionDisabled");
	<% String[][] batchrecclear  = null;
	if(map != null) {
		Iterator  itLab5 = map.entrySet().iterator(); 
		while(itLab5.hasNext()){  
			Map.Entry pair = (Map.Entry)itLab5.next();%>
	if(subSecNm == "<%=pair.getKey().toString()%>"){
	<%  batchrecclear  =  (String[][])pair.getValue();
	 for(int i=0;i<batchrecclear.length;i++){ 
	 	if (batchrecclear[i][0] == null) continue;
		String checknull1 = batchrecclear[i][0];%>    
	    var key =   "<%=batchrecclear[i][0]%>";
	    var componentType = "<%=batchrecclear[i][5]%>";
	<%if(checknull1 != null && checknull1 != "" ){%>
	    if(key != null &&getElementByIdOrByName(key) != null && !subsectionDisabled){
		repeatingBatchClearFields(key, componentType);
	}   
	<%}}%>
	viewed = -1;
	}
	<%}}%>
	var rowhide =getElementByIdOrByName("AddButtonToggle"+subSecNm);
	if(rowhide!=null){
	rowhide.style.display = ''; 
	}
	var rowshow =getElementByIdOrByName("AddNewButtonToggle"+subSecNm);
	if(rowshow!=null){
	rowshow.style.display = 'none';             
	}
	var rowshow1 =getElementByIdOrByName("UpdateButtonToggle"+subSecNm);
	if(rowshow1!=null){
	rowshow1.style.display = 'none';
	}
    } //clearClicked       

   function clearRepeatingblk(subSecNm)    {
	JPageForm.clearRepeatingblk(subSecNm);           		
	fillTable(subSecNm,"pattern"+subSecNm,"questionbody"+subSecNm);	
   }
   function unhideBatchImg(subSecNm)
   {
    	var t =getElementByIdOrByName(subSecNm); 	
        var len=0;
        
    	//t.style.display = "block";    
    	<% if(map != null){ 
		Iterator itLab1 = map.entrySet().iterator();
		while(itLab1.hasNext()){   
		Map.Entry pair = (Map.Entry)itLab1.next();%>
	if(subSecNm == "<%=pair.getKey().toString()%>"){
	<% batchrecinsert  =  (String[][])pair.getValue();   
         for(int i=0;i<batchrecinsert.length;i++){  
	    String checknull1 = batchrecinsert[i][0];  
	    if(checknull1 != null && checknull1 != ""){%> 
		var key =   "<%=batchrecinsert[i][0]%>";
		if(key != null && key != 'undefined' && key != ''){
			len = len +1;
		}
	<%}}%>
	 }
	<%}}%>
	for (var i = 0; i < len+1; i ++)   {
		//alert($j($j(t).find("tbody > tr:odd").get(i)).css("background-color","#95BAEF"));
		$j($j(t).find("tbody > tr:odd").get(i)).css("background-color","#95BAEF");
		$j($j(t).find("tbody > tr:even").get(i)).css("background-color","#95BAEF");
	}
	//  	alert( $j("#" + "questionbody"  +subSecNm));
	for (var i = 0; i < len+1; i ++)   {
		$j($j("#" + "questionbody"  +subSecNm).find("tr").get(i)).css("background-color","white");	
	}					       
	$j("#" + "nopattern"  +subSecNm).css("background-color","white");
    } //unhide batch image		                  
                     	
   function rollingNoteSetUserDate(elementId) {
		             <%
		             String theUserName = "";
		               try {
		                   NBSSecurityObj so = (NBSSecurityObj) request.getSession().getAttribute("NBSSecurityObject");
		                   if (so != null) {
		                         theUserName = so.getTheUserProfile().getTheUser().getFirstName() + " " + so.getTheUserProfile().getTheUser().getLastName();
		                   }   
		               }
		               catch (Exception e) {
		               }           
		            %>
		          	var currentUser = "<%=theUserName%>";
		          	dwr.util.setValue(elementId+"User",currentUser);
		    
		    
		    	var todayDT = new Date();
		    	thedd = todayDT.getDate().toString();
		    	if (parseInt(thedd) < 10) thedd = "0" + thedd;
		    	themm = todayDT.getMonth()+1;//January is 0!
		    	if (parseInt(themm) < 10) themm = "0" + themm;
		    	theyyyy = todayDT.getFullYear();
		    	var theDate = themm + "/" + thedd + "/" + theyyyy;
		    	var theMinutes = todayDT.getMinutes();
		    	if (theMinutes < 10){
				theMinutes = "0" + theMinutes;
			}
		    	var theTime = " "+ todayDT.getHours() + ":" + theMinutes;
		    	dwr.util.setValue(elementId+"Date",theDate+theTime);
      }
        
  ]]> </xsl:text>   
               
          </script>  
        
     <style type="text/css">
            body.popup div.popupTitle {width:100%; background:#185394; padding:3px; color:#FFF; text-align:left; font-size:110%; font-weight:bold;}	
            body.popup div.popupButtonBar {text-align:right; width:100%; background:#EEE; border-bottom:1px solid #DDD;}
            table.searchTable {width:98%; margin:0 auto; margin-top:1em; border-spacing:4px; margin-bottom:5px; margin-top:5px;}
     </style>
    </head>
      
     <xsl:text disable-output-escaping="yes"><![CDATA[ 
     <% 
    int subSectionIndex = 0;

    String tabId = "";
      String [] sectionNames  = {]]></xsl:text><xsl:for-each select="descendant-or-self::*/SectionName"><xsl:text disable-output-escaping="yes">"</xsl:text><xsl:value-of select="."/><xsl:text disable-output-escaping="yes">"</xsl:text><xsl:if test="not(position() = last())"><xsl:text disable-output-escaping="yes">,</xsl:text></xsl:if></xsl:for-each><xsl:text disable-output-escaping="yes"><![CDATA[};]]></xsl:text>
    <xsl:text disable-output-escaping="yes"><![CDATA[ ;
  
    int sectionIndex = 0;
    String sectionId = "";

%> 

    <body class="popup" onunload="handlePageUnload(true, event); return false;" onload="startCountdown();autocompTxtValuesForJSP();pageCreateLoad('${PageForm.attributeMap.selectEltIdsArray}');
    populateBatchRecords();
    attachMoveFocusFunctionToTabKey();
    pgOnLoadCalcRptAge('DEM115','INV2001','INV2002','NBS096','NBS104');
    
    pgCheckOnloadOtherEntryAllowedFields();pgCheckDynamicRulesOnLoad();pgCheckForFieldsToHighlightOnEdit();pgPopulateCounties();">
        <div id="pageview"></div>
        <!-- Container Div: To hold top nav bar, button bar, body and footer -->
        <div id="doc3">
        <html:hidden name="PageForm" property="pageClientVO.answer(DEM165)" styleId="DEM165_Hidden"/>
		<html:hidden name="PageForm" property="pageClientVO.answer(DEM165_W)" styleId="DEM165_W_Hidden"/>
		
            <html:form action="/PageAction.do"> 
                <!-- Page title -->
	        <div class="popupTitle"> ${BaseForm.pageTitle} </div>
            <%String phcUID = (String) request.getAttribute("phcUID");%>
	        <!-- Top button bar -->
	      	<div class="popupButtonBar">
	            <input type="button" name="Submit" value="Submit" onclick="saveForm();"/>
	            <input type="button" name="Cancel" value="Cancel" onclick="cancelForm()" />
	        </div>            
            <!-- Body div -->
            <div id="bd">
                       <!-- Page Errors -->
                       <%@ include file="/jsp/feedbackMessagesBar.jsp" %>
					   
                        <!-- Note: No Patient Summary at top of Generic Window  -->
                        <logic:equal name="PageForm" property="businessObjectType" value="VAC">
							<%@ include file="/pagemanagement/GenericEventSummary.jsp" %>
						</logic:equal>
						                     
                        <!-- Required Field Indicator -->
                        <div style="text-align:right; width:100%;"> 
                            <span class="boldTenRed"> * </span>
                            <span class="boldTenBlack"> Indicates a Required Field </span>  
                        </div>

                     <!-- Error Messages using Action Messages-->
				    <div id="globalFeedbackMessagesBar" class="screenOnly">
				        <logic:messagesPresent name="error_messages">
				        <div class="infoBox errors" id="errorMessages">
				            <b> <a name="errorMessagesHref"></a> Please fix the following errors:</b> <br/>
				            <ul>
				                <html:messages id="msg" name="error_messages">
				                    <li> <bean:write name="msg" /> </li>
				                </html:messages>
				            <ul>
				        </div>
				    </logic:messagesPresent>
				    </div> 
              ]]>  </xsl:text>
                        <!-- Tab container -->
      <!-- <layout:tabs width="100%" styleClass="tabsContainer"> -->
     <xsl:comment> ################### PAGE TAB ###################### --</xsl:comment>
      <xsl:text disable-output-escaping="yes"><![CDATA[<layout:tabs width="100%" styleClass="tabsContainer">]]></xsl:text>
         <xsl:for-each select="PageTab">		<!-- Setup Include for Tab on the Page -->
         	<xsl:variable name="thisTab" select="TabName" />
         	 <!-- Layout the Tab Key -->
         	 <xsl:variable name="TabVisible" select="Visible" />
		 <xsl:choose>
	         <xsl:when test="$TabVisible= 'T'">
      		<xsl:text disable-output-escaping="yes"> <![CDATA[<layout:tab key="]]></xsl:text><xsl:value-of select="normalize-space(TabName)"/><xsl:text disable-output-escaping="yes"><![CDATA[">]]></xsl:text>     
      		<xsl:text disable-output-escaping="yes"><![CDATA[<jsp:include page="]]></xsl:text><xsl:value-of select="translate(TabName,' ','')"/><xsl:text disable-output-escaping="yes"><![CDATA[.jsp"/>]]></xsl:text>
             	<xsl:text disable-output-escaping="yes"><![CDATA[</layout:tab>]]></xsl:text>  <!-- End Layout Tab -->             	
             	</xsl:when>
                 </xsl:choose>
        </xsl:for-each>   <!-- PageTab  -->     
    
      <xsl:text disable-output-escaping="yes"><![CDATA[</layout:tabs>]]></xsl:text>  <!-- End Layout Tabs -->
	  <xsl:text disable-output-escaping="yes"><![CDATA[
			 <!-- Bottom button bar -->
	  	    <div class="popupButtonBar">
		        <input type="button" name="Submit" value="Submit" onclick="saveForm()"/>
		        <input type="button" name="Cancel" value="Cancel" onclick="cancelForm()" />
		    </div>
	    	
               </html:form>
          </div> <!-- Container Div -->
    </body>
    ]]></xsl:text> 
</html>
		

	</xsl:template>
</xsl:stylesheet>