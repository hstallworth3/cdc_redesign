<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" %>
<%@ page isELIgnored ="false" %>
<% 

String method = request.getParameter("method") == null ? "" : (String) request.getParameter("method");
if(! method.equals("manageAdmin") )	
 {
%>
    <table style="width:100%;">
		<tr>
		    <td style="text-align:right"  id="iaeLink"> 
		        <a id="manageLink" href="/nbs/SrtAdministration.do?method=manageAdmin&focus=systemAdmin3">
		        Return to System Management Main Menu
		        </a>  
		       <%--  <input type="hidden" id="actionMode" value="${triggerCodeForm.actionMode}"/> --%>
		    </td>
		</tr>
	</table>
<%} %>
<logic:empty name="triggerCodeForm" property="actionMode">
	<tr><td><%@ include file="/jsp/errors.jsp" %></td></tr>
</logic:empty>