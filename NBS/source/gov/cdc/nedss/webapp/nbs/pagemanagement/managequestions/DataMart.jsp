<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page isELIgnored ="false" %>
<%@ page import="gov.cdc.nedss.util.NEDSSConstants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<tr>
    <td class="fieldName"> 
        <span class="boldRed">*</span>
        <span title="Default Label in Report" id="defaultLabelReportL">Default Label in Report:</span>
    </td>
    <td>
        <html:text property="selection.reportAdminColumnNm" size="50" maxlength="50" style="width:500px;" styleId="defaultLabelReport" />      
    </td>
</tr>

<tr>
    <td class="fieldName">
        <span class="boldRed">*</span> 
        <span title="RDBTable Name" id="rdbTableNmL">Default RDB Table Name:</span>
    </td>
    <td >
        <!-- for referenced question -->
        <logic:equal name="manageQuestionsForm" property="selection.referencedInd" value="<%= NEDSSConstants.TRUE %>">
            <html:hidden property="selection.rdbTableNm" />		 
			<nedss:view name ="manageQuestionsForm" property="selection.rdbTableNm"/>
        </logic:equal>
        <!-- for non-referenced question -->
        <logic:notEqual name="manageQuestionsForm" property="selection.referencedInd" value="<%= NEDSSConstants.TRUE %>">
        	
		  	<logic:equal name="manageQuestionsForm" property="actionMode" value="Edit">
		  		<nedss:view name ="manageQuestionsForm" property="selection.rdbTableNm"/>
		  	</logic:equal>
		  	<logic:equal name="manageQuestionsForm" property="actionMode" value="Create">
		       		<html:text property="selection.rdbTableNm" styleId="rdbTableNm" size="25" style="width:200px;"/>
			</logic:equal>
		    <input type="hidden" name="rdbTableNmHid" id = "rdbTableNmHid"/>

        </logic:notEqual>
    </td>
</tr>
<tr>
    <td class="fieldName">
        <span class="boldRed">*</span> 
        <span title="RDB Column Name" id="rdbcolumnNmL">RDB Column Name:</span>
    </td>
    <td>
        <!-- for referenced question -->
        <logic:equal name="manageQuestionsForm" property="selection.referencedInd" value="<%= NEDSSConstants.TRUE %>">
            <html:hidden property="selection.userDefinedColumnNm" />
            <bean:write name="manageQuestionsForm" property="selection.userDefinedColumnNm"/>
        </logic:equal>
        <!-- for non-referenced question -->
        <logic:notEqual name="manageQuestionsForm" property="selection.referencedInd" value="<%= NEDSSConstants.TRUE %>">
            <html:text property="selection.userDefinedColumnNm" size="25" maxlength="21" style="width:500px;" styleId="rdbcolumnNm" onkeyup="nospaces(this)"/>
        </logic:notEqual>      
    </td>
</tr>
