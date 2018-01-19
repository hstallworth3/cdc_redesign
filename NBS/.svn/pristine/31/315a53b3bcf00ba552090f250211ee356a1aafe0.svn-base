<%@ include file="/jsp/tags.jsp" %>
<%@ page import="gov.cdc.nedss.util.*, gov.cdc.nedss.systemservice.nbssecurity.*"%>
<%@ page isELIgnored ="false" %>
<%@ page buffer = "16kb" %>
<html>
    <head>
		<title><%= request.getAttribute("PageTitle") %></title>
        <%@ include file="/jsp/resources.jsp" %>
       <SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JProgramAreaForm.js"></SCRIPT>
		<SCRIPT Language="JavaScript" Src="jquery.dimensions.js"></SCRIPT>
		<SCRIPT Language="JavaScript" Src="jqueryMultiSelect.js"></SCRIPT>
		<SCRIPT Language="JavaScript" Src="openInvestigations.js"></SCRIPT>
		<link href="jqueryMultiSelect.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript">
		
		blockEnterKey();
		
			//Added for resolving an issue with IE11
			function enableFiltering(){
				document.getElementById("dropdownsFiltering").style.display="block";
			}
			
		
		//document.onkeypress = function (e) { //commented out unnecessary GST 2016-5-13
		//	if (e.which=='13')
		//		return false;
		//}
		
		 	function onKeyUpValidate()
	 		{      	  
	        	if(getElementByIdOrByName("SearchText1").value != ""){
	         		getElementByIdOrByName("b1SearchText1").disabled=false;
	         		getElementByIdOrByName("b2SearchText1").disabled=false;
	         	   }else if(getElementByIdOrByName("SearchText1").value == ""){
	         		getElementByIdOrByName("b1SearchText1").disabled=true;
	         		getElementByIdOrByName("b2SearchText1").disabled=true;
	         	   }
	         	   
			}
			function makeMSelects() {
				$j("#sdate").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
				$j("#inv").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
				$j("#juris").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
				$j("#cond").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
				$j("#stat").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
				$j("#notif").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
				$j("#patient").text({actionMode: '<%= request.getAttribute("ActionMode") %>'});				
			}						
			function showCount() {
				$j(".pagebanner b").each(function(i){ 
					$j(this).append(" of ").append($j("#queueCnt").attr("value"));
				});
				$j(".singlepagebanner b").each(function(i){ 
					var cnt = $j("#queueCnt").attr("value");
					if(cnt > 0)
						$j(this).append(" Results 1 to ").append(cnt).append(" of ").append(cnt);
				});				
			}
			function createLink(element, url)
			{
				// call the JS function to block the UI while saving is on progress.
				blockUIDuringFormSubmissionNoGraphic();
                document.forms[0].action= url;
                document.forms[0].submit();  
			}
		</script>
		<style type="text/css">
		.removefilter{
			background-color:#003470; width:100%; height:25px;
			line-height:25px;float:right;text-align:right;
			}
			removefilerLink {vertical-align:bottom;  }
			.hyperLink
			{
			    font-size : 10pt;
			    font-family : Geneva, Arial, Helvetica, sans-serif;
			    color : #FFFFFF;
				text-decoration: none;
			}
		</style>
		
   </head>
    <body onload="attachIcons();makeMSelects();showCount();displayTooltips();startCountdown();enableFiltering();">
    <div id="blockparent"></div>
   <div id="doc3">
    <html:form action="/LoadMyProgramAreaInvestigations1.do">
	 <tr> <td>                    
 
  <!-- Container Div: To hold top nav bar, button bar, body and footer -->
     
<!-- Body div -->
<div id="bd">
   <!-- Top Nav Bar and top button bar -->
    <%@ include file="/jsp/topNavFullScreenWidth.jsp" %>

           <!-- Page Code Starts here -->
            <div class="printexport" id="printExport" align="right">
				<img class="cursorHand" src="print.gif" alt="Print Queue to PDF" onclick="printQueue();"/>
             <img class="cursorHand" src="export.gif" alt="Export Queue to CSV" onclick="exportQueue();"/>
			</div>	
           
          <table width="100%">
             <tr>
             <td align="center">
             <!-- Moved here to resolve an issue with IE 11 -->
             <div id="whitebar" style="background-color:#FFFFFF; width: 100%; height:1px;" align="right"></div>
			<div class="removefilter" id="removeFilters">
				<a class="removefilerLink" href="javascript:clearFilter()"><font class="hyperLink"> | Remove All Filters/Sorts&nbsp;</font></a>
			</div>
				  	<display:table name="investigationList" class="dtTable" style="margin-top:0em;" pagesize="${programAreaForm.attributeMap.queueSize}"  id="parent" requestURI="/LoadMyProgramAreaInvestigations1.do?method=loadQueue&existing=true" sort="external" export="true" excludedParams="answerArray(STARTDATE) answerArray(INVESTIGATOR) answerArray(JURISDICTION) answerArray(CONDITION) answerArray(CASESTATUS) answerArray(NOTIFICATION) answerArrayText(SearchText1) method">
					  	<display:setProperty name="export.csv.filename" value="OpenInvestigationsQueue.csv"/>
					  	<display:setProperty name="export.pdf.filename" value="OpenInvestigationsQueue.pdf"/>
						<display:column property="activityFromTime_s" title="Start Date" format="{0,date,MM/dd/yyyy}" sortable="true" sortName="getActivityFromTime" defaultorder="descending" style="width:12%;"/>
						<display:column property="investigatorFullName" title="Investigator" sortable="true" sortName="getInvestigatorFullName" defaultorder="descending" style="width:14%;"/>
						<display:column property="jurisdictionDescTxt" title="Jurisdiction" sortable="true" sortName="getJurisdictionDescTxt" defaultorder="descending" style="width:14%;"/>
						<display:column property="patientFullNameLnk" title="Patient" media="html" sortable="true" sortName="getPatientFullName" defaultorder="descending" style="width:16%;"/>
						<display:column property="patientFullName" title="Patient" media="csv pdf" sortable="true" sortName="getPatientFullName" defaultorder="descending" style="width:20%;"/>
						<display:column property="conditionCodeTextLnk" title="Condition" media="html" sortable="true" sortName="getConditionCodeText" defaultorder="descending" style="width:18%;"/>
						<display:column property="conditionCodeText" title="Condition" media="csv pdf" sortable="true" sortName="getConditionCodeText" defaultorder="descending" style="width:21%;"/>
						<display:column property="caseClassCodeTxt" title="CaseStatus" sortable="true" sortName="getCaseClassCodeTxt" defaultorder="descending" style="width:13%;"/>
						<display:column property="notifStatusTransCd" title="Notification" sortable="true" sortName="getNotifRecordStatusCd" defaultorder="descending" style="width:13%;"/>						
						<display:setProperty name="basic.empty.showtable" value="true"/>
			       </display:table>
		      </td>
		     </tr>
		  </table>
				<div style="display: none;visibility: none;" id="errorMessages">
				<b> <a name="errorMessagesHref"></a>Queue is sorted/filtered by :</b> <br/>
				<ul>
					<logic:iterate id="errors" name="programAreaForm" property="attributeMap.searchCriteria">
						<li id="${errors.key}">${errors.value}</li>
					</logic:iterate>
				</ul>
			</div> 
            <div class="printexport" id="printExport" align="right">
				<img class="cursorHand" src="print.gif" alt="Print Queue to PDF" onclick="printQueue();"/>
				<img class="cursorHand" src="export.gif" alt="Export Queue to CSV" onclick="exportQueue();"/>
			</div>	

			</div>
		   <div id="dropdownsFiltering" style="Display:none"><!-- Added to resolve an issue with IE11 -->
		   	<%@ include file="OpenInvestigationsDropDowns.jsp" %>
		   </div>	
		   <input type='hidden' id='SearchText1' value="<%= request.getAttribute("PATIENT")!=null? request.getAttribute("PATIENT"):""%>"/>
	  </html:form>
      </div>
      <input type="hidden" id="queueCnt" value="<%= request.getAttribute("queueCount") %>"/>
</body>
</html>