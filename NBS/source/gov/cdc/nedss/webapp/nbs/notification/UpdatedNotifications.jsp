<%@ include file="/jsp/tags.jsp" %>
<%@ include file="/jsp/tags.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page import="java.util.*,
                gov.cdc.nedss.util.*, 
                gov.cdc.nedss.systemservice.nbssecurity.*,
                gov.cdc.nedss.webapp.nbs.action.person.util.*,
                gov.cdc.nedss.proxy.ejb.notificationproxyejb.vo.*"%>
                
<%@ page isELIgnored ="false" %>
<%@ page buffer = "16kb" %>
<html>
    <head>
        <title><%= request.getAttribute("PageTitle") %></title>
        <%@ include file="../../jsp/resources.jsp" %>
        <script language="JavaScript" src="/nbs/dwr/interface/JUpdatedNotificationsQueueForm.js"></script>
        <script language="JavaScript" src="jquery.dimensions.js"></script>
        <script language="JavaScript" src="jqueryMultiSelect.js"></script>
        <script Language="JavaScript" src="UpdatedNotificationsSpecific.js"></script>
        <link href="jqueryMultiSelect.css" rel="stylesheet" type="text/css"/>
        <style type="text/css">
            table.dtTable thead tr th.selectAll {text-align:center;}
            .removefilter { background-color:#003470; width:100%; height:25px; line-height:25px;float:right;text-align:right;margin-top:1%;}
            .removefilterLink {vertical-align:bottom; }
            .hyperLink { font-size : 10pt; font-family : Geneva, Arial, Helvetica, sans-serif; color : #FFFFFF; text-decoration: none;}
        </style>
        <script type="text/javascript">
        
        blockEnterKey();
        
       		 <!-- For resolving issue with IE11 -->
			function removeMargin(){
				document.getElementById("removeFilters").style.marginTop="0px";
			}
		    function uncheckSelectAll() {
		        $j("INPUT[name='selectAll'][type='checkbox']").attr('checked', false);
		    }
		    
		    function convertToJqueryMultiSelect()
		    {
		        // attach jquery multi-select plugins to select boxes
		        $j("#testConditionMS, #caseStatusMS, #submittedByMS, #notificationCodeMS, #recipientMS, #updateDateMS").multiSelect({actionMode: '<%= request.getAttribute("ActionMode") %>'});
		        $j("#patient").text({actionMode: '<%= request.getAttribute("ActionMode") %>'});        
		    }
		    
		    function startCountdown()
		    {
		        var sessionTimeout = <%= request.getSession().getMaxInactiveInterval()%>;
		        min = sessionTimeout / 60;
		        sec = 0;                
		        getTimerCountDown();
		    }
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
		</script>
    </head>  
    
    <body onload="attachIcons();convertToJqueryMultiSelect();showCount();displayTooltips();startCountdown();uncheckSelectAll();removeMargin();">
        <div id="blockparent"></div>
        <div id="doc3">
            <html:form action="/ManageUpdatedNotifications.do?method=loadQueue">
                <input type="hidden" id="queueCnt" value="<%= request.getAttribute("queueCount") %>"/>
	            <div id="bd">
	                <!-- Top nav bar -->
	                <%@ include file="/jsp/topNavFullScreenWidth.jsp" %>

                    <!-- Feedback messages bar -->
                    <%@ include file="/jsp/feedbackMessagesBar.jsp" %>

                    <!-- Msg explaining the use of asterisk -->                 
                    <div class="requiredFieldIndicatorLink"> 
                        <span class="boldTenRed"> * </span>
                        <span class="boldTenBlack" style="font-weight:normal; font-style:italic;"> Indicates that the case status has changed </span>  
                    </div>

                    <!-- Print and Export links -->
                    <logic:notEmpty name="updatedNotifications">
	                    <div class="printexport" id="printExport" align="right">
	                        <img class="cursorHand" src="print.gif" alt="Print Queue to PDF" onclick="printQueue();"/>
	                        <img class="cursorHand" src="export.gif" alt="Export Queue to CSV" onclick="exportQueue();"/>
	                    </div>
                    </logic:notEmpty>

                    <!-- Div block to act as a place holder for all error/feedback messages
                        as a result of filtering the tables. This will be hidden from the 
                        user's view at all times -->
                    <logic:present name="updatedNotificationsQueueForm" property="attributeMap.searchCriteria">    
	                    <div style="display: none;visibility: none;" id="errorMessages">
	                        <b> <a name="errorMessagesHref"></a>Queue is sorted/filtered by :</b> <br/>
	                        <ul>
	                            <logic:iterate id="errors" name="updatedNotificationsQueueForm" 
	                                    property="attributeMap.searchCriteria">
	                                <li id="${errors.key}">${errors.value}</li>
	                            </logic:iterate>
	                        </ul>
	                    </div>
                    </logic:present>

                    <% 
                        int maxRowCount = request.getAttribute("maxRowCount") != null ? ((Integer)(request.getAttribute("maxRowCount"))).intValue() : 10;
                        ArrayList updatedNotificationList = new ArrayList();
                        updatedNotificationList = (ArrayList) request.getAttribute("updatedNotifications");
                        String viewInvestigationHref= request.getAttribute("ViewInvestigationHref") != null ? (String)request.getAttribute("ViewInvestigationHref") : "";
                    %>
                    
					<div id="whitebar" style="background-color:#FFFFFF; width: 100%; height:1px;" align="right"></div>
                    <div class="removefilter" id="removeFilters">
                        <table style="width:100%;">
                            <tr>
                                <td style="width:50%; text-align:left;padding:0.2em;">
                                    <a title="Remove all the notifications selected below" 
                                        href="javascript:void(0)" onclick="return removeUpdatedNotificationsInQueue();"><font class="hyperLink"> Remove </font></a>
                                </td>
                                <td style="width:50%; text-align:right;padding:0.2em;">
                                    <a class="removefilterLink" 
                                        href="javascript:clearFilter()"><font class="hyperLink"> | Remove All Filters/Sorts&nbsp;</font></a>            
                                </td>
                            </tr>
                        </table>
                        
                    </div>
	                <table width="100%">
	                    <tr>
	                        <td align="center">
	                            <display:table name="updatedNotifications" class="dtTable" id="parent" 
	                                    style="margin-top:0em;" pagesize="${maxRowCount}" 
	                                    requestURI="/ManageUpdatedNotifications.do?method=loadQueue&existing=true" 
	                                    sort="external" export="true" 
	                                    excludedParams="answerArray(UPDATEDATE) answerArray(SUBMITTEDBY) answerArray(RECIPIENT) answerArray(NOTIFICATIONCODE) answerArray(CONDITION) answerArray(CASESTATUS) answerArrayText(SearchText1)">
	                                <display:column 
	                                       title="<input type=\"checkbox\" name=\"selectAll\" onclick=\"javascript:toggleNotificationSelections(this)\"/>" 
	                                       headerClass="selectAll" style="width:3%;" media="html">
	                                    <div align="center">
	                                        <input type="checkbox" class="isRemoved" value="${parent.removeFlag}" 
	                                            name="isRemoved-${parent.notificationUid}_${parent.versionCtrlNbr}"
	                                            onclick="javascript:updateSelectAllCheckBox()"
	                                        />
	                                    </div>
	                                </display:column>
                                    <display:column title="" style="width:3%; text-align:center;" media="html">
                                        <div align="center">
                                            <a title="Remove this notification" 
                                                href="javascript:removeSingleNotification('isRemoved-${parent.notificationUid}_${parent.versionCtrlNbr}');"><img src="doc_page.jpg"> </a>
                                        </div>
                                    </display:column>
	                                <display:column property="addTime" defaultorder="descending" sortable="true" sortName= "getAddTime" format="{0,date,MM/dd/yyyy}" title="Update Date" style="width:12%;"/>
	                                <display:column property="addUserName" defaultorder="descending" sortable="true" sortName= "getAddUserName" title="Updated By" style="width:12%;"/>
									<display:column property="recipient" defaultorder="descending" sortable="true" sortName= "getRecipient" title="Recipient" style="width:12%;"/>
									<display:column property="notificationCd" defaultorder="descending" sortable="true" sortName= "getNotificationCd" title="Type" style="width:12%;"/>
	                                <display:column property="patientFullName" defaultorder="descending" sortable="true" sortName= "getPatientFullName"  title="Patient" style="width:12%;"/>
	                                <display:column property="cdTxt" media="csv pdf" defaultorder="descending" sortable="true" sortName= "getCdTxt"  title="Condition" style="width:12%;"/>
	                                <display:column property="conditionLink" media="html" defaultorder="descending" sortable="true" sortName= "getCdTxt"  title="Condition" style="width:12%;"/>
	                                <display:column property="caseClassCdTxt" media="csv pdf" defaultorder="descending" sortable="true" sortName= "getCaseClassCdTxt" title="Case Status" style="width:12%;"/>
	                                <display:column property="decoratedCaseClassCdTxt" media="html" defaultorder="descending" sortable="true" sortName= "getCaseClassCdTxt" title="Case Status" style="width:12%;"/>
	                                <display:setProperty name="basic.empty.showtable" value="true"/>
	                            </display:table>
	                        </td>
	                    </tr>
	                </table>
	                
                    <!-- Print and Export links -->
                    <div class="printexport" id="printExport" align="right">
                        <img class="cursorHand" src="print.gif" alt="Print Queue to PDF" onclick="printQueue();"/>
                        <img class="cursorHand" src="export.gif" alt="Export Queue to CSV" onclick="exportQueue();"/>
                    </div>
	            </div>
	            
	            <!-- Include the filter dropdowns for this display tag table. This is hidden when the page
	            loads in order to avoid the page flickering. Once the page finishes loading, the attachIcons() method
	            will be used to remove the hidden property of this filter dropdowns. --> 
	            <div id="filterDropDowns" style="display:none;">
                    <%@ include file="UpdatedNotificationDropDowns.jsp" %>                    
                    <input type='hidden' id='SearchText1' value="<%= request.getAttribute("PATIENT")!=null? request.getAttribute("PATIENT"):""%>"/>
                </div>                    
	        </html:form>
	    </div>
    </body>
</html>