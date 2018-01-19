<%@ include file="/jsp/tags.jsp" %>
<%@ include file="/jsp/tags.jsp" %>
<%@ taglib prefix="d" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="gov.cdc.nedss.util.*, gov.cdc.nedss.systemservice.nbssecurity.*,java.util.*"%>
<%@ page import="gov.cdc.nedss.proxy.ejb.observationproxyejb.vo.*"%>
<%@ page isELIgnored ="false" %>
<%@ page buffer = "16kb" %>
<html>
    <head>
        <title><%= request.getAttribute("PageTitle") %></title>
                <%@ include file="/jsp/resources.jsp" %>
               <SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JObservationSecurityAssgnReviewForm.js"></SCRIPT>
                <SCRIPT Language="JavaScript" Src="jquery.dimensions.js"></SCRIPT>
                <SCRIPT Language="JavaScript" Src="jqueryMultiSelect.js"></SCRIPT>
                <SCRIPT Language="JavaScript" Src="observationSecurityAssgnReview.js"></SCRIPT>
       			<SCRIPT Language="JavaScript" Src="genericQueue.js"></SCRIPT>
        <link href="jqueryMultiSelect.css" rel="stylesheet" type="text/css"/>
        <script language="JavaScript">
	
        var array2 = new Array();                  
        blockEnterKey();
    	
        /**
         * enableFiltering:
         */

        //Added for resolving an issue with IE11
        function enableFiltering(){
        	document.getElementById("dropdownsFiltering").style.display="block";
        	document.getElementById("removeFilters").style.marginTop="0px";
        }

       
            
    

		 
 /**
 * disableChecks(): disable checks for the document type documentType
 */
 
 function disableChecks(documentType){
	 
	var sb = "";	
	var checkboxes = document.getElementsByName('selectCheckBox');
	var documentTypeRow="";
	var table = document.getElementsByClassName("dtTable")[0];
	var indexDocumentType = 1;
	var indexLocalId = 8;
	
	for (var i = 1; i < checkboxes.length+1; i++) {
		var row = table.getElementsByTagName("tr")[i];
		if(row!=null && row!='undefined'){
		   documentTypeRow = row.getElementsByTagName("td")[indexDocumentType].getElementsByTagName("a")[0].innerHTML;
		   if(documentTypeRow.indexOf(documentType)!=-1)		
				row.getElementsByTagName("input")[0].disabled=true;
	    }
	}
 }
 
 /**
 * checkPermissions: 
 */
 
 function checkPermissions(){
	 
	 var permissionLab = ${observationSecurityReviewForm.attributeMap.permissionLab};
	 var permissionMorb = ${observationSecurityReviewForm.attributeMap.permissionMorb};
	 var permissionCase = ${observationSecurityReviewForm.attributeMap.permissionCase};

	 if(permissionLab==false)
		 disableChecks("Lab Report");
	 if(permissionMorb==false)
		 disableChecks("Morbidity Report");
	 if(permissionCase==false)
		 disableChecks("Case Report");
 }
            
        </script>   
		
		<style type="text/css">
		
		
	    div#addAttachmentBlock {margin-top:10px; display:none; width:100%; text-align:center;}
		div#addProgramAreaBlock {margin-top:10px; display:none; width:100%; text-align:center;}
		 div#addJurisdictionBlock {margin-top:10px; display:none; width:100%; text-align:center;}
		 
		div.messages { background:#E4F2FF; color:#000; padding:0.5em; border-width:1px 1px 1px 1px; border-color:#7AA6D5; border-style:solid; font-size:95%;}
        div.popupButtonBar {text-align:right; width:100%; background:#EEE; border-bottom:1px solid #DDD;}
         .boxed {
          border: 1px solid #5F8DBF ;background:#E4F2FF
             }
			 
        .removefilter{
            background-color:#003470; width:100%; height:25px;
            line-height:25px;float:right;text-align:right;
			margin-top: 1%;
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
  
   
   <body onload="attachIcons();makeMSelects();showCount();displayTooltips();startCountdown();enableFiltering();uncheckHeader();resetDropdowns();showHideMessage();checkPermissions();">
       <div id="blockparent"></div>
 
       <div id="doc3">
       <html:form action="/LoadObsNeedingAssignment1.do">
   


			
    
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
			
								<div id="addProgramAreaBlock" class="boxed">

			
				                        <table class="formTable" style=" width:100%; margin:0px;">

								
		
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsLabs" styleId="chkboxIdsLabs1"/>
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsMorbs" styleId="chkboxIdsMorbs1"/>
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsCases" styleId="chkboxIdsCases1"/>   
											
									        
									        <tr>
									            <td colspan="3">
									                <div id="errorBlockP" class="screenOnly infoBox errors" style="display:none; width:99%"> </div>
									                
			
									                 
<!--    <div id="msgBlockP" class="screenOnly infoBox success" style="display:none; width:99%"> The selected * documents have been successfully transfered to * program area.</div>-->
									            </td>
									        </tr>
									        
									    
									        <tr>
									               <td colspan="2" style="text-align:left;"><span style="color:black; font-weight:bold;"> &nbsp;&nbsp;Please select the appropriate Program Area and/or Jurisdiction applicable to the selected documents.</a></td>
									        </tr>
									       
									        <tr>
									         
									            <td colspan ="2" class="fieldName" style="text-align:right; width:50%" id="chooseFileLabelP"> <span style="color:red; font-weight:bold;">*</span> Program Area:</td>
												<td class="InputField">
												
	
												<html:select property="programAreaSelected" styleId ="programAreaStyleP">
													<html:optionsCollection property="programAreaList" value="key" label="value"/>
												</html:select>
											
										
												<html:hidden property="jurisdictionSelected" styleId ="jurisdictionStyleP" value=""/>					
			
												</td>
									            
										
											</tr>
									        <tr>
									          
									            <td colspan="3" style="text-align:right;">
									             
									                <input type="button" value="Submit" name="submitButton" onclick="return transferProgramArea();"/>
			
									               
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
					   
					            <div id="addJurisdictionBlock" class="boxed">
				                           <table class="formTable" style=" width:100%; margin:0px;">
				                            
				                            
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsLabs" styleId="chkboxIdsLabs2"/>
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsMorbs" styleId="chkboxIdsMorbs2"/>
																				       
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsCases" styleId="chkboxIdsCases2"/>        									       
									        <tr>
									            <td colspan="3">
									                <div id="errorBlockJ" class="screenOnly infoBox errors" style="display:none; width:99%"> </div>
									                
									        
									              
 <!-- <div id="msgBlockJ" class="screenOnly infoBox success" style="width:99%"> The selected * documents have been successfully transfered to * program area and * jurisdiction.</div>-->									            </td>
									        </tr>
									        
									    
									        <tr>
									               <td colspan="2" style="text-align:left;"><span style="color:black; font-weight:bold;"> &nbsp;&nbsp;Please select the appropriate Program Area and/or Jurisdiction applicable to the selected documents.</a></td>
									        </tr>
									       
									       <tr> 
									           
												<td colspan ="2" class="fieldName" style="text-align:right; width:50%" id="chooseFileLabelJ"> <span style="color:red; font-weight:bold;">*</span> Jurisdiction: </td>
									            <td class="InputField">
												<logic:notEmpty name="observationSecurityReviewForm" property="jurisdictions">
												
												<html:select property="jurisdictionSelected" styleId ="jurisdictionStyleJ">
													<html:optionsCollection property="jurisdictionList" value="key" label="value"/>
												</html:select>
												</logic:notEmpty>
												
												<html:hidden property="programAreaSelected" styleId ="programAreaStyleJ" value=""/>
									
												
		              						 </td>
												
									           
									        </tr> 
									        <tr>
									          
									            <td colspan="3" style="text-align:right;">
									            
									             	
													
									                <input type="button" value="Submit" name="submitButton" onclick="return transferJurisdiction();"/>
									        

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
									
							  <div id="addAttachmentBlock" class="boxed">
							  
				                        <table class="formTable" style=" width:100%; margin:0px;">
				                                
				                            <html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsLabs" styleId="chkboxIdsLabs3"/>
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsMorbs" styleId="chkboxIdsMorbs3"/>
											<html:hidden name="observationSecurityReviewForm" property="selectedcheckboxIdsCases" styleId="chkboxIdsCases3"/>      
									       
									        <tr>
									            <td colspan="3">
									                <div id="errorBlock" class="screenOnly infoBox errors" style="display:none; width:99%"> </div>
									                
									        
									                
 <!--   <div id="msgBlock" class="screenOnly infoBox success" style="display:none; width:99%"> The selected * documents have been successfully transfered to * program area and * jurisdiction.</div>-->
									            </td>
									        </tr>
									        
									    
									        <tr>
									               <td colspan="2" style="text-align:left;"><span style="color:black; font-weight:bold;"> &nbsp;&nbsp;Please select the appropriate Program Area and/or Jurisdiction applicable to the selected documents.</a></td>
									        </tr>
									       
									        <tr>
									          
									            <td colspan ="2" class="fieldName" style="text-align:right; width:50%" id="chooseFileLabel1"> <span style="color:red; font-weight:bold;">*</span> Program Area:</td>
												<td class="InputField">
												
												<html:select property="programAreaSelected" styleId ="programAreaStyle">
													<html:optionsCollection property="programAreaList" value="key" label="value"/>
												</html:select>
											
												</td>
									            
												</tr> <tr> 
												<td colspan ="2" class="fieldName" style="text-align:right;" id="chooseFileLabel2"> <span style="color:red; font-weight:bold;">*</span> Jurisdiction: </td>
									            <td class="InputField">
												<logic:notEmpty name="observationSecurityReviewForm" property="jurisdictions">
												
												<html:select property="jurisdictionSelected" styleId ="jurisdictionStyle">
													<html:optionsCollection property="jurisdictionList" value="key" label="value"/>
												</html:select>
												</logic:notEmpty>
		              						 </td>
												
									           
									        </tr> 
									        <tr>
									          
									            <td colspan="3" style="text-align:right;">
									             
									                <input type="button" value="Submit" name="submitButton" onclick="return transferOwnerShip();"/>
									        
									              
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
			<!-- Moved here to resolve an issue with IE 11 -->
			<div id="whitebar" style="background-color:#FFFFFF; width: 100%; height:1px;" align="right"></div>
            <div class="removefilter" id="removeFilters">
            			
			
			<table style="width: 100%;" class="TLinks">
				<tr>
					<td style="width: 50%; text-align: left; padding: 0.2em; padding-left:0.2%">	
					
						
					
					<logic:match name="observationSecurityReviewForm" property="attributeMap.permissionTransfer" value="true">
					 	<a class="reviewedLink" href="#"><font class="hyperLink" onclick="transferProgramAreaSection()"> Transfer Program Area </font></a>
						<a class="reviewedLink" href="#"><font class="hyperLink" onclick="transferJurisdictionSection()">| Transfer Jurisdiction</font></a>
						<a class="reviewedLink" href="#"><font class="hyperLink" onclick="transferOwnershipSection()">| Transfer Ownership</font></a>  
					</logic:match>
					<!-- 	<a class="reviewedLink" href="#"><font class="hyperLink">| Delete</font></a>  -->
					</td>
					<td style="width: 50%; text-align: right; padding: 0.2em;">
						<a class="removefilerLink" href="javascript:clearFilter()"><font class="hyperLink"> | Remove All Filters/Sorts&nbsp;</font></a>
					</td>
				</tr>
			</table>
            </div>
              
             
			 
                
                <display:table name="observationList" class="dtTable" style="margin-top:0em;" pagesize="${observationSecurityReviewForm.attributeMap.maxRowCount}"   id="parent" requestURI="/LoadObsNeedingAssignment1.do?method=loadQueue&existing=true" sort="external" export="true" excludedParams="answerArray(STARTDATE) answerArray(OBSERVATIONTYPE) answerArray(JURISDICTION) answerArray(CONDITION) answerArray(PROGRAMAREA) answerArray(DESCRIPTION) answerArrayText(SearchText1) answerArrayText(SearchText2) method">
                  <display:setProperty name="export.csv.filename" value="ObservationNeedingReviewQueue.csv"/>
                  <display:setProperty name="export.pdf.filename" value="ObservationNeedingReviewQueue.pdf"/>   
                 
  
  
					<logic:match name="observationSecurityReviewForm" property="attributeMap.permissionTransfer" value="true">
					 <display:column style="width:1.5%;text-align:center" media="html" title="<input type='checkbox' style='margin-left:15%' name='selectAllValue' onchange='checkAll(this)' />" ><input type="checkbox" name="selectCheckBox" class="selectCheckBox"/> </display:column>                
					</logic:match>

 				  <d:forEach items="${observationSecurityReviewForm.queueCollection}" var="item">

					                            
					
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
                    <logic:iterate id="errors" name="observationSecurityReviewForm" property="attributeMap.searchCriteria">
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
            <d:forEach items="${observationSecurityReviewForm.queueCollection}" var="item">
					<bean:define id="filterType" value="${item.filterType}" />
					<logic:match name="filterType" value="1">
						<input type='hidden' id="${item.dropdownProperty}"
						value="<%=request.getAttribute("${item.backendId}") != null ? request.getAttribute("${item.backendId}") : ""%>" />
					</logic:match>
			</d:forEach>
			<input type='hidden' id='actionMode' value="<%=request.getAttribute("ActionMode") != null ? request.getAttribute("ActionMode") : ""%>" />


			
			<%-- <input type='hidden' id='Description' value="<%=request.getAttribute("DESCRIPTION") != null ? request.getAttribute("DESCRIPTION") : ""%>" /> --%>
       
			
			<%--<html:hidden name="observationSecurityReviewForm" property="programAreaSelected" styleId="programAreaSelected"/>--%>
			

			
			
            </html:form> 
        </div>
        <input type="hidden" id="queueCnt" value="<%= request.getAttribute("queueCount") %>"/>
      </body>
</html>	