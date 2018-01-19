<%@page import="gov.cdc.nedss.report.javaRepot.dt.ReportPlaceDT"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page isELIgnored="false"%>
<%@ page
	import="gov.cdc.nedss.webapp.nbs.diseaseform.util.InvestigationFieldConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="gov.cdc.nedss.util.NEDSSConstants"%>
<%@ page import="gov.cdc.nedss.report.javaRepot.vo.CR3VO"%>
<%@ page import="gov.cdc.nedss.report.javaRepot.vo.CR4VO"%>
<%@ page import="gov.cdc.nedss.report.javaRepot.dt.ReportPatientDT"%>
<html>
<head>
<title>NBS: Manage Questions</title>
<base target="_self">
<%@ include file="../../../jsp/resources.jsp"%>
<script language="JavaScript">
var closeCalled = false;
function handlePageUnload(closePopup,e)
{
    // This check is required to avoid duplicate invocation 
    // during close button clicked and page unload.
    if (closeCalled == false) {
        closeCalled = true;
        
        if (e.clientY < 0 || closePopup == true) {
            // pass control to parent's call back handler
           var opener = getDialogArgument();
            
            window.returnValue = "windowClosed";
            window.close();
        }
    }
}

function handlePrinterFriendlyPageClose()
{
    self.close();
    return false;   
}


function showPrintFriendlyPage()
{
    var o = new Object();
    o.opener = self;
    var URL = "/nbs/RunReport.do?method=LoadReport&mode=print";
    var dialogFeatures = "dialogWidth:900px;dialogHeight:500px;status:no;unadorned:yes;scroll:yes;scrollbars:yes;help:no;resizable:yes;max:1;min:1";
    
    var modWin = openWindow(URL, o,dialogFeatures, null, "");
	
    return false;
}

	    </script>
<style type="text/css">
table.FORM {
	width: 100%;
	margin-top: 15em;
}
</style>
</head>
<%
	   String printMode = (request.getAttribute("mode") == null) ? "" : ((String)request.getAttribute("mode")); 
       if (printMode.equals("print")) { %>
	       <body class="popup" onunload="return handlePrinterFriendlyPageClose();">
	   <% } else { %>
	       <body class="popup" onunload="handlePageUnload(); return false;">
        <% } 
    %>
	<html:form>


		<!-- Button bar -->
		<div class="grayButtonBar">
		<input type="submit" name="Submit" value="Print" onclick="return showPrintFriendlyPage();"/>
			<input type="submit" id="submitB" value="Close"
				onclick="return handlePageUnload(true,event);" />
		</div>
		        <!-- Tool bar for print friendly mode -->
        <div class="printerIconBlock screenOnly">
		    <table style="width:98%; margin:3px;">
		        <tr>
		            <td style="text-align:right; font-weight:bold;"> 
		                <a href="#" onclick="return printPage();"> <img src="printer_icon.gif" alt=""/> Print Page </a> 
		            </td>
		        </tr>
		    </table>
		</div>
		<div class="reportTitle">
			<span> <%=request.getAttribute("reportName") %> </span>
		</div>
		<div class="reportCriteria">
			<span><%=request.getAttribute("reportCriteria") %></span>
		</div>
		
		<%
		
					ArrayList data = (ArrayList) request.getAttribute("CR4");
					if(data!=null && data.size()>0){
				    Iterator dataIterator = data.iterator();
					while (dataIterator.hasNext()) 
					{
					 CR4VO cr4VO = (CR4VO)dataIterator.next();
					 ReportPlaceDT rpDT = cr4VO.getReportPlaceDT();
					 ArrayList associatedCaseCount = new ArrayList();
					 associatedCaseCount.add(cr4VO);
					 request.setAttribute("associatedCaseCount",associatedCaseCount);
					 ArrayList associatedCases = (ArrayList)cr4VO.getAssociatedCases();
					 request.setAttribute("associatedCases",associatedCases);
					 ArrayList associatedCohortCases = (ArrayList)cr4VO.getAssociatedCohortCases();
					 request.setAttribute("associatedCohortCases",associatedCohortCases);
           %>
		<nedss:container id="subsect_report_filter" name="<%= rpDT.getPlaceName()%>"
			classType="sect" displayImg="false" includeBackToTopLink="no" displayLink="no">
			<tr>
				<td class="fieldName"><span>Location/Physical Address: </span></td>
				<td><%=rpDT.getPlaceAddress()%></td>
			</tr>
			<tr>
				<td colspan="2">
					<table width="98%">
		             <tr>
		             <td align="center" >
						  	<display:table name="associatedCaseCount" class="dtTable" style="margin-top:0em;"  id="parent">
								<display:column property="count200" title="200" style="width:8%;"/>
								<display:column property="count300" title="300" style="width:8%;"/>
								<display:column property="count710" title="710" style="width:8%;"/>
								<display:column property="count720" title="720" style="width:8%;"/>
								<display:column property="count730" title="730" style="width:8%;"/>
								<display:column property="count740" title="740" style="width:8%;"/>
								<display:column property="count745" title="745" style="width:8%;"/>
								<display:column property="count900" title="900" style="width:8%;"/>
								<display:column property="count950" title="950" style="width:8%;"/>
								<display:column property="cohortCount" title="# Cohorts" style="width:8%;"/>
							 <display:setProperty name="basic.empty.showtable" value="true"/>
					       </display:table>
				      </td>
				     </tr>
				  </table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="fieldName"><span>Associated Cases</span></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="associatedCases" class="dtTable" style="margin-top:0em;"  id="parent">
						  	    <display:column property="patientName" title="Name" style="width:12%;"/>
								<display:column property="dx" title="Diagnosis" style="width:12%;"/>
								<display:column property="caseID" title="Case Id" style="width:12%;"/>
								<display:column property="nineHundredStatus" title="900 Status" style="width:14%;"/>
	     						<display:setProperty name="basic.empty.showtable" value="true"/>
					       </display:table>
				      </td>
				     </tr>
				  </table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
						<tr>
				<td class="fieldName"><span>Cohorts</span></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="associatedCohortCases" class="dtTable" style="margin-top:0em;"  id="parent">
						  	    <display:column property="patientName" title="Cohort Name" style="width:12%;"/>
								<display:column property="gender" title="Gender" style="width:12%;"/>
								<display:column property="dispositionCode" title="Dispo" style="width:12%;"/>
								<display:column property="dispositionDate" title="Dispo Date" format="{0,date,MM/dd/yyyy}" style="width:14%;"/>
								<display:column property="dx" title="Diagnosis" style="width:14%;"/>
	     						<display:setProperty name="basic.empty.showtable" value="true"/>
					       </display:table>
				      </td>
				     </tr>
				  </table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</nedss:container>
		<%}}else{
		%>
		<div class="reportNoData">There is no data available for
					the selected criteria</div>
		<%
			}
		%>
		<!-- Bottom button bar -->
		<div class="grayButtonBar">
		<input type="submit" name="Submit" value="Print" onclick="return showPrintFriendlyPage();"/>
			<input type="submit" id="submitB" value="Close"
				onclick="return handlePageUnload(true,event);" />
		</div>
	</html:form>
</body>
</html>