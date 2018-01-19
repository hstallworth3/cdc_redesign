<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page isELIgnored ="false" %>
<%@ page import = "gov.cdc.nedss.webapp.nbs.diseaseform.util.InvestigationFieldConstants"%>
<%@ page import="java.util.*" %>
<html:hidden name="manageEventsForm" property="selectedcheckboxIds" styleId="chkboxIds"/>
<% 
int  subSectionIndex= (new Integer(request.getParameter("param2").toString())).intValue();
String tabId = request.getParameter("param3").toString();
%>

<% String sectionId = "document"; %>
<nedss:container id="<%= tabId + (++subSectionIndex) %>" name="Documents" classType="subSect">     
    <tr>
	    <td colspan="2">
            <!-- Display tab table for listing all lab reports -->
            <display:table name="documentSummaryList" class="dtTable" id="doclist" style="margin-top:0.15em;">
	            <display:column style="width:5%;">
	            	<div align="center" style="margin-top: 3px">
	                    <input type="checkbox" value="${doclist.isAssociated}" name="${doclist.localId}" ${doclist.checkBoxId}/>
	                </div>
	            </display:column>
	            <display:column property="actionLink" title="Date Received" style="width:10%;"/>
	            <display:column property="docTypeConditionDescTxt" title="Type" />
	            <display:column property="docPurposeCdConditionDescTxt" title="Purpose"/>
	            <display:column property="cdDescTxt" title="Description"/>
	            <display:column property="localIdForUpdatedAndNewDoc" title="Document ID" style="width:18%;"/>
		        <display:setProperty name="basic.empty.showtable" value="true"/>
	        </display:table>
	    </td>
    </tr>
</nedss:container>
<br>
