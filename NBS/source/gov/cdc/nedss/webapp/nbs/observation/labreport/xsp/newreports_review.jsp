<%@ include file="/jsp/tags.jsp" %>
<%@ include file="/jsp/tags.jsp" %>
<%@ page import="gov.cdc.nedss.util.*, gov.cdc.nedss.systemservice.nbssecurity.*,java.util.*"%>
<%@ page import="gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.*"%>
<%@ page isELIgnored ="false" %>
<%@ page buffer = "16kb" %>
<%@ taglib prefix="d" uri="http://java.sun.com/jsp/jstl/core" %> 
<html>
    <head>
        <title><%= request.getAttribute("PageTitle") %></title>
		      
		       <%@ include file="/jsp/resources.jsp" %>
		       <SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JObservationReviewForm.js"></SCRIPT>
				<SCRIPT Language="JavaScript" Src="jquery.dimensions.js"></SCRIPT>
				<SCRIPT Language="JavaScript" Src="jqueryMultiSelect.js"></SCRIPT>
				<SCRIPT Language="JavaScript" Src="observationsReview.js"></SCRIPT>
				<SCRIPT Language="JavaScript" Src="genericQueue.js"></SCRIPT>
				
		<link href="jqueryMultiSelect.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript">
		
		blockEnterKey();

		

			function checkAll(ele) {
			     var checkboxes = document.getElementsByTagName('input');
			     if (getCorrectAttribute(ele,  "checked", ele.checked)) {
			         for (var i = 0; i < checkboxes.length; i++) {
			        	if(checkboxes[i].name == "selectCheckBox" && !checkboxes[i].disabled){
			                 checkboxes[i].checked = true;
			        	}
			    	}
			   	} else {
			         for (var i = 0; i < checkboxes.length; i++) {
			             console.log(i)
			           if(checkboxes[i].name == "selectCheckBox"){
			                 checkboxes[i].checked = false;
			             }
			        }
			   	}
			 }
			
			function checkDocsNotProcessed(){
				
				var docsNotProcessed = document.getElementById("notProcessedDocUid").innerText;
				var docs = docsNotProcessed.split("|");
				var checkboxes = document.getElementsByName('selectCheckBox');
				var table = document.getElementsByClassName("dtTable")[0];
				var indexDocumentType = 1;
				
		        for (var i = 1; i <= checkboxes.length; i++) {
					var row = table.getElementsByTagName("tr")[i];
		        	if(row!=null && row!='undefined'){

						   
						   var link = row.getElementsByTagName("td")[indexDocumentType].getElementsByTagName("a")[0].getAttribute("onclick")
						   var indexObservationUid=link.indexOf("observationUID");
						   var observationUid=link.substring(indexObservationUid+15,link.length-2);

						   if(docs.indexOf(observationUid)!=-1)
						   		row.getElementsByTagName("input")[0].checked=true;
						}
		    	}
			}
			
			/**
			 * disableChecks(): disable checks for the document type documentType
			 */
			 
			 function disableChecks(permissionLab, permissionMorb, permissionCase){
				 
				var checkboxes = document.getElementsByName('selectCheckBox');
				var table = document.getElementsByClassName("dtTable")[0];
				var indexDocumentType = 1;
				var indexLocalId = 8;
				
				var labs, morbs, docs;
				
				if(permissionLab!=true)
					labs = permissionLab.split("|");
				if(permissionLab!=true)
					morbs = permissionMorb.split("|");
				if(permissionLab!=true)
					docs = permissionCase.split("|");
				
				for (var i = 1; i < checkboxes.length+1; i++) {
					var row = table.getElementsByTagName("tr")[i];
					if(row!=null && row!='undefined'){
					       var link = row.getElementsByTagName("td")[indexDocumentType].getElementsByTagName("a")[0].getAttribute("onclick")
						   var indexObservationUid=link.indexOf("observationUID");
						   var observationUid=link.substring(indexObservationUid+15,link.length-2);
						
					   if(permissionLab.indexOf(observationUid)!=-1
						   || permissionMorb.indexOf(observationUid)!=-1
						   || permissionCase.indexOf(observationUid)!=-1)	
							row.getElementsByTagName("input")[0].disabled=true;
				    }
				}
			 }
			 
			 /**
			 * checkPermissions: 
			 */
			 
			 function checkPermissions(){
				 var permissionLab = "${observationReviewForm.attributeMap.permissionLab}";
				 var permissionMorb = "${observationReviewForm.attributeMap.permissionMorb}";
				 var permissionCase = "${observationReviewForm.attributeMap.permissionCase}";
				 
				 disableChecks(permissionLab, permissionMorb, permissionCase);
			 }
			 
		</script>	

		 <style type="text/css">
			
			
			div#addMarkAsReviewedBlock {margin-top:10px; display:none; width:100%; text-align:center;}
			div#addMarkAsReviewedBlockSTD {margin-top:10px; display:none; width:100%; text-align:center;}
			
            table.dtTable thead tr th.selectAll {text-align:center;}
			
			.boxed {
                 border: 1px solid #5F8DBF ;background:#E4F2FF
            }
			 
            .removefilter { background-color:#003470; width:100%; height:25px; line-height:25px;float:right;text-align:right;}
            .removefilterLink {vertical-align:bottom; }
            .reviewedLink {vertical-align:bottom;}
            .hyperLink { font-size : 10pt; font-family : Geneva, Arial, Helvetica, sans-serif; color : #FFFFFF; text-decoration: none;}
        </style>
	

      </head>
   <body onload="attachIcons();makeMSelects();showCount();displayTooltips();startCountdown();removeMargin();uncheckHeader();resetDropdowns();showHideMessage();checkDocsNotProcessed();checkPermissions();">
       <div id="blockparent"></div>
      <div id="doc3">
       <html:form action="/LoadNewLabReview1.do">
	 <tr> <td>
            <!-- Body div -->
                <div id="bd">
                    <!-- Top Nav Bar and top button bar -->
                     <%@ include file="/jsp/topNavFullScreenWidth.jsp" %>
                     <!-- Container Div -->
           <!-- Page Code Starts here -->

			<div class="printexport" id="printExport" align="right">
				<img class="cursorHand" src="print.gif" alt="Print Queue to PDF" onclick="printQueue();"/>
				<img class="cursorHand" src="export.gif" alt="Export Queue to CSV" onclick="exportQueue();"/>
			</div>
			<% if (request.getAttribute("ERR144") != null) { %>
				  <div class="infoBox messages">
				       The number of documents that you can access in this queue has been exceeded. You will not be able to access the most recently created documents in this queue until the existing documents are processed.
				  </div>    
			<% } %>
			<% if (request.getAttribute("DocTOwnershipMsg") != null) { %>
			  <div class="infoBox success">
			  	<%=(String)request.getAttribute("DocTOwnershipMsg")%>&nbsp;<a href='<%=request.getAttribute("clickHereLk")%>' >Click Here</a> to view the patient's File.<br/>
			  </div>    
           <% } %>

	 		
	 		<div id="msgBlock" class="screenOnly infoBox success" style="width:99%;display:none;">${msgBlock}</div>
	 		<div id="errorBlock" class="screenOnly infoBox errors" style="width:99%;display:none;">${errorBlock}</div>
	 		<div id="notProcessedDocUid" style="width:99%;display:none;">${notProcessedDocUid}</div>
	 		
			<div id="addMarkAsReviewedBlock" class="boxed">

			
				                        <table class="formTable" style=" width:100%; margin:0px;">

								
		
											<html:hidden name="observationReviewForm" property="selectedcheckboxIdsLabs" styleId="chkboxIdsLabs1"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxMprIdsLabs" styleId="chkboxMprIdsLabs1"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxIdsMorbs" styleId="chkboxIdsMorbs1"/>
									
											<html:hidden name="observationReviewForm" property="selectedcheckboxMprIdsMorbs" styleId="chkboxMprIdsMorbs1"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxIdsCases" styleId="chkboxIdsCases1"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxMprIdsCases" styleId="chkboxMprIdsCases1"/>
											
											
											
											
									        <tr>
									            <td colspan="3">
									                <div id="errorBlockP" class="screenOnly infoBox errors" style="display:none; width:99%"> </div>
									                
									                 
<!--    <div id="msgBlockP" class="screenOnly infoBox success" style="display:none; width:99%"> The selected * documents have been successfully transfered to * program area.</div>-->
									            </td>
									        </tr>
									        
									    
									        <tr>
									               <td colspan="3" style="text-align:left;"><span style="color:black; font-weight:bold;"> &nbsp;&nbsp;Please select a reason for taking no further action and enter any additional
									                comments that help to explain why no further action is required.
			 										This reason will be applied to all the records that have been selected below.
													 Documents that are marked as reviewed will remain on the patient's file, and if previously associated to an investigation will remain associated to
													 an investigation. Select Submit to continue or select Cancel to cancel this action.</td>
									        </tr>
									       
									        <tr>
									         
									            <td colspan ="2" class="fieldName" style="text-align:right; width:50%" id="reasonLabel"> <span style="color:red; font-weight:bold;">*</span> Reason For No Further Action:</td>
												<td class="InputField">
												

												<html:select property="processingDecisionSelected" styleId ="processingDecisionStyle">
													<html:optionsCollection property="nonSTDProcessingDecisionList" value="key" label="value"/>
												</html:select>
											<%-- <logic:equal name="associateToInvestigationsForm" property="processingDecisionLogic" value="STD_UNKCOND_PROC_DECISION">       
	            								     <html:optionsCollection property="codedValue(STD_UNKCOND_PROC_DECISION)" value="key" label="value"/>
	 											</logic:equal> --%>
												<%--
												<html:hidden property="jurisdictionSelected" styleId ="jurisdictionStyleP" value=""/>					
												--%>
												</td>
									            
										
											</tr>
											<tr><td colspan ="2" id="reasonComments" class="fieldName">Comments:</td><td>
											<textarea rows="6" cols="60" id="markAsReviewedComments" name ="markAsReviewedComments" onkeydown="checkMaxLength(this)" onkeyup="checkMaxLength(this)"></textarea>
											</td></tr>
									        <tr>
									          
									            <td colspan="3" style="text-align:right;">
									             
									                <input type="button" value="Submit" name="submitButton" onclick="return markAsReviewedSubmit();"/>
									               
									                <input type="button" value="Cancel" name="cancelButton" onclick="javascript:cancelAttachment()"/>
									            </td>
									        </tr>      
									    </table>
									    <p><span align='left' class='status-text' id='updateStatusMsg'></span>
									    <div id="progressBar" style="display: none;">
									        <div id="theMeter">
									            <div id="progressBarText"></div>
									            <div id="progressBarBox">
									                <div id="progressBarBoxContent"></div>
									            </div>
									        </div>
									    </div>
									    </p>
						
					            </div>
								
			<div id="addMarkAsReviewedBlockSTD" class="boxed">

			
				                        <table class="formTable" style=" width:100%; margin:0px;">

								
		
											<html:hidden name="observationReviewForm" property="selectedcheckboxIdsLabs" styleId="chkboxIdsLabs2"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxMprIdsLabs" styleId="chkboxMprIdsLabs2"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxIdsMorbs" styleId="chkboxIdsMorbs2"/>
									
											<html:hidden name="observationReviewForm" property="selectedcheckboxMprIdsMorbs" styleId="chkboxMprIdsMorbs2"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxIdsCases" styleId="chkboxIdsCases2"/>
											<html:hidden name="observationReviewForm" property="selectedcheckboxMprIdsCases" styleId="chkboxMprIdsCases2"/>
									        <tr>
									            <td colspan="3">
									                <div id="errorBlockSTD" class="screenOnly infoBox errors" style="display:none; width:99%"> </div>
									                
									                 
<!--    <div id="msgBlockP" class="screenOnly infoBox success" style="display:none; width:99%"> The selected * documents have been successfully transfered to * program area.</div>-->
									            </td>
									        </tr>
									        
									    
									        <tr>
									               <td colspan="3" style="text-align:left;"><span style="color:black; font-weight:bold;"> &nbsp;&nbsp;Please select a reason for taking no further action and enter any additional
									                comments that help to explain why no further action is required.
			 										This reason will be applied to all the records that have been selected below.
													 Documents that are marked as reviewed will remain on the patient's file, and if previously associated to an investigation will remain associated to
													 an investigation. Select Submit to continue or select Cancel to cancel this action.</td>
									        </tr>
									       
									        <tr>
									         
									            <td colspan ="2" class="fieldName" style="text-align:right; width:50%" id="processingDecisionLabel"> <span style="color:red; font-weight:bold;">*</span> Processing Decision:</td>
												<td class="InputField">
												

												<html:select property="processingDecisionSelected" styleId ="processingDecisionStyleSTD">
													<html:optionsCollection property="processingDecisionList" value="key" label="value"/>
												</html:select>
											
												<%--
												<html:hidden property="jurisdictionSelected" styleId ="jurisdictionStyleP" value=""/>					
												--%>
												</td>
									            
										
											</tr>
											<tr><td colspan ="2" id="reasonCommentsSTD" class="fieldName">Comments:</td><td>
											<textarea rows="6" cols="60" id="markAsReviewedCommentsSTD" name="markAsReviewedComments" onkeydown="checkMaxLength(this)" onkeyup="checkMaxLength(this)"></textarea>
											</td></tr>
									        <tr>
									          
									            <td colspan="3" style="text-align:right;">
									             
									                <input type="button" value="Submit" name="submitButton" onclick="return markAsReviewedSubmitSTD();"/>
									               
									                <input type="button" value="Cancel" name="cancelButton" onclick="javascript:cancelAttachment()"/>
									            </td>
									        </tr>      
									    </table>
									    <p><span align='left' class='status-text' id='updateStatusMsg'></span>
									    <div id="progressBar" style="display: none;">
									        <div id="theMeter">
									            <div id="progressBarText"></div>
									            <div id="progressBarBox">
									                <div id="progressBarBoxContent"></div>
									            </div>
									        </div>
									    </div>
									    </p>
						
					            </div>
			<table width="100%">
			<tr> <td align="center">
				<div id="whitebar" style="background-color: #FFFFFF; width: 100%; height: 1px;" align="right"></div>
					<div class="removefilter" id="removeFilters" >
						<table style="width: 100%;">
						<tr>
							<td style="width: 50%; text-align: left; padding: 0.2em; padding-left:0.2%">
								<logic:match name="observationReviewForm" property="attributeMap.permissionMarkAsReviewed" value="true">	
								<a class="reviewedLink" href="#"><font class="hyperLink" onclick="markAsReviewedSection()"> Mark As Reviewed </font></a> 
								</logic:match>
							<!-- 	<a class="reviewedLink" href="#"><font class="hyperLink">| Delete</font></a>  -->
							</td>
							<td style="width: 50%; text-align: right; padding: 0.2em;">
								<a class="removefilerLink" href="javascript:clearFilter()"><font class="hyperLink"> | Remove All Filters/Sorts&nbsp;</font></a>
							</td>
						</tr>
						</table>

				 </div> 
				
				  <display:table name="observationList" class="dtTable" style="margin-top:0em;" pagesize="${observationReviewForm.attributeMap.maxRowCount}"   id="parent" requestURI="/LoadNewLabReview1.do?method=loadQueue&existing=true" sort="external" export="true" excludedParams="stringQueueCollection answerArray(STARTDATE) answerArray(OBSERVATIONTYPE) answerArray(JURISDICTION) answerArray(CONDITION) answerArrayText(SearchText1) answerArrayText(SearchText2) answerArrayText(Provider) answerArray(DESCRIPTION) method">
			      <display:setProperty name="export.csv.filename" value="DocumentsRequiringReviewQueue.csv"/>
			      <display:setProperty name="export.pdf.filename" value="DocumentsRequiringReviewQueue.pdf"/> 	
   				  <logic:match name="observationReviewForm" property="attributeMap.permissionMarkAsReviewed" value="true">
   				    <display:column style="width:1.5%;text-align:center" media="html" title="<input type='checkbox' style='margin-left:15%' name='selectAllValue' onchange='checkAll(this)' />" ><input type="checkbox" name="selectCheckBox"/> </display:column>
   				  </logic:match>

				  
				  <d:forEach items="${observationReviewForm.queueCollection}" var="item">
				  
					<bean:define id="media" value="${item.media}" />
					<logic:match name="media" value="html">
						<display:column property="${item.mediaHtmlProperty}" defaultorder="${item.defaultOrder}" sortable="${item.sortable}" sortName= "${item.sortNameMethod}" media="html" title="${item.columnName}" style="${item.columnStyle}" class ="${item.className}" headerClass="${item.headerClass}" />
					</logic:match>
					<logic:match name="media" value="pdf">
						<display:column property="${item.mediaPdfProperty}" defaultorder="${item.defaultOrder}" sortable="${item.sortable}" sortName= "${item.sortNameMethod}" media="pdf" title="${item.columnName}" style="${item.columnStyle}" class ="${item.className}" headerClass="${item.headerClass}" />
					</logic:match>
					<logic:match name="media" value="csv">
						<display:column property="${item.mediaCsvProperty}" defaultorder="${item.defaultOrder}" sortable="${item.sortable}" sortName= "${item.sortNameMethod}" media="csv" title="${item.columnName}" style="${item.columnStyle}" class ="${item.className}" headerClass="${item.headerClass}" />
					</logic:match>


	<br>
				</d:forEach>

		          <display:setProperty name="basic.empty.showtable" value="true"/>
		       </display:table>
		       </td>
		     </tr>
		     </table>
				<div style="display: none;visibility: none;" id="errorMessages">
				<b> <a name="errorMessagesHref"></a>Queue is sorted/filtered by :</b> <br/>
				<ul>
					<logic:iterate id="errors" name="observationReviewForm" property="attributeMap.searchCriteria">
						<li id="${errors.key}">${errors.value}</li>
					</logic:iterate>
				</ul>
			</div> 
			<br/>
		    <div class="printexport" id="printExport" align="right">
				<img class="cursorHand" src="print.gif" alt="Print Queue to PDF" onclick="printQueue();"/>
				<img class="cursorHand" src="export.gif" alt="Export Queue to CSV" onclick="exportQueue();"/>
			</div>

			</div>
			</div>
			<div id="dropdownsFiltering" style="Display:none"><!-- Added to resolve an issue with IE11 -->
				<%@ include file="/jsp/dropDowns_Generic_Queue.jsp" %>
			</div>
			
			
			<d:forEach items="${observationReviewForm.queueCollection}" var="item">
					<bean:define id="filterType" value="${item.filterType}" />
					<logic:match name="filterType" value="1">
						<input type='hidden' id="${item.dropdownProperty}"
						value="<%=request.getAttribute("${item.backendId}") != null ? request.getAttribute("${item.backendId}") : ""%>" />
					</logic:match>
			</d:forEach>
			
	<input type='hidden' id='actionMode'
		value="<%=request.getAttribute("ActionMode") != null ? request.getAttribute("ActionMode") : ""%>" />



	</html:form>
		     </div>
		     <input type="hidden" id="queueCnt" value="<%= request.getAttribute("queueCount") %>"/>
      </body>
</html>