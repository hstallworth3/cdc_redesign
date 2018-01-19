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
<%@ page import="gov.cdc.nedss.report.javaRepot.vo.CR1VO"%>
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
			<span><%=request.getAttribute("reportName") %></span>
		</div>
		<div class="reportCriteria">
			<span><%=request.getAttribute("reportCriteria") %></span>
		</div>
		<%
		           ArrayList data = (ArrayList) request.getAttribute("CR1");
					if(data!=null && data.size()>0){
				    Iterator dataIterator = data.iterator();
					while (dataIterator.hasNext()) 
					{
					 CR1VO cr1VO = (CR1VO)dataIterator.next();
					 ReportPatientDT rpDT = cr1VO.getPatientDT();
					 ArrayList patientCollection = new ArrayList();
					 patientCollection.add(rpDT);
					 request.setAttribute("patientCollection",patientCollection);
					 ArrayList treatments = (ArrayList)cr1VO.getTreatments();
					 request.setAttribute("Treatments",treatments);
					 ArrayList signAndSymptoms = (ArrayList)cr1VO.getSignsAndSymptoms();
					 request.setAttribute("signAndSymptoms",signAndSymptoms);
					 ArrayList named = (ArrayList)cr1VO.getNamed();
					 request.setAttribute("named",named);
					 ArrayList namedBackedBy = (ArrayList)cr1VO.getNamedBackedBy();
					 request.setAttribute("namedBackedBy",namedBackedBy);
					 ArrayList namedByButNotBackedBy = (ArrayList)cr1VO.getNamedByButNotBackedBy();
					 request.setAttribute("namedByButNotBackedBy",namedByButNotBackedBy);
           %>
		<nedss:container id="subsect_report_filter" name="<%= rpDT.getPatientName()%>"
			classType="sect" displayImg="false" includeBackToTopLink="no" displayLink="no">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="patientCollection" class="dtTable" style="margin-top:0em;"  id="parent">
								<display:column property="patientName" title="Case Name" style="width:12%;"/>
								<display:column property="dx" title="Dx" style="width:14%;"/>
								<display:column property="caseID" title="Case Number" style="width:12%;"/>
								<display:column property="contacts" title="# Cts" style="width:5%;"/>
								<display:column property="socialANAssociateContacts" title="# S/As" style="width:6%;"/>
								<display:column property="originalInterviewDate" title="OI Date" format="{0,date,MM/dd/yyyy}" style="width:10%;"/>
								<display:column property="patientCurrentSex" title="Gender" style="width:8%;"/>
								<display:column property="pregnantIndicator" title="Preg." style="width:8%;"/>
								<display:column property="nineHundredStatus" title="900" style="width:8%;"/>
								<display:column property="maritalStatus" title="Marital" style="width:10%;"/>
								<display:column property="dateClosed" title="Date Closed" format="{0,date,MM/dd/yyyy}" style="width:13%;"/>
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
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="Treatments" class="dtTable" style="margin-top:0em;"  id="parent">
								<display:column property="treatmentName" title="Treatment" style="width:12%;"/>
								<display:column property="rxDate" title="Rx Date" style="width:14%;"/>
								<display:column property="provider" title="Provider" style="width:12%;"/>
							 <display:setProperty name="basic.empty.showtable" value="true"/>
					       </display:table>
				      </td>
				      <td>&nbsp;</td>
				      <td align="center">
						  	<display:table name="signAndSymptoms" class="dtTable" style="margin-top:0em;"  id="parent">
								<display:column property="signSymptoms" title="Sign/Sympton" style="width:10%;"/>
								<display:column property="obsOnsetDate" title="Onset date" format="{0,date,MM/dd/yyyy}" style="width:8%;"/>
								<display:column property="duration" title="Duration" style="width:10%;"/>
								<display:column property="clinicianObservedCd" title="Clin. Obs." style="width:21%;"/>
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
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="named" class="dtTable" style="margin-top:0em;"  id="parent">
						  	    <display:column property="contactName" title="Named" style="width:12%;"/>
								<display:column property="contactReferralBasis" title="Ref. Basis" style="width:12%;"/>
								<display:column property="gender" title="Gender" style="width:12%;"/>
								<display:column property="contactFirstSexExpoDate" title="1st Expose" format="{0,date,MM/dd/yyyy}" style="width:14%;"/>
								<display:column property="contactSexExpFreq" title="Freq." style="width:12%;"/>
								<display:column property="contactLastSexExpDate" title="Last Expose" format="{0,date,MM/dd/yyyy}" style="width:10%;"/>
								<display:column property="dispoDescription" title="Dispo" style="width:10%;"/>
								<display:column property="dispositionDate" title="Dispo dt." format="{0,date,MM/dd/yyyy}" style="width:8%;"/>
								<display:column property="diagnosisCode" title="Dx" style="width:10%;"/>
								<display:column property="contactInvestigationCase" title="Case No." style="width:10%;"/>
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
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="namedBackedBy" class="dtTable" style="margin-top:0em;"  id="parent">
						  	    <display:column property="contactName" title="OP Named Back By" style="width:12%;"/>
								<display:column property="contactFirstSexExpoDate" title="1st Expose" format="{0,date,MM/dd/yyyy}" style="width:14%;"/>
								<display:column property="contactSexExpFreq" title="Freq." style="width:12%;"/>
								<display:column property="contactLastSexExpDate" title="Last Expose" format="{0,date,MM/dd/yyyy}" style="width:10%;"/>							 
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
				<td>
					<table width="98%">
		             <tr>
		             <td align="center">
						  	<display:table name="namedByButNotBackedBy" class="dtTable" style="margin-top:0em;"  id="parent">
						  	    <display:column property="contactName" title="OP Named by but did not name" style="width:20%;"/>
								<display:column property="contactReferralBasis" title="Ref. Basis" style="width:12%;"/>
								<display:column property="gender" title="Gender" style="width:12%;"/>
								<display:column property="contactFirstSexExpoDate" title="1st Expose" format="{0,date,MM/dd/yyyy}" style="width:14%;"/>
								<display:column property="contactSexExpFreq" title="Freq." style="width:12%;"/>
								<display:column property="contactLastSexExpDate" title="Last Expose" format="{0,date,MM/dd/yyyy}" style="width:10%;"/>
								<display:column property="dispoDescription" title="Dispo" style="width:10%;"/>
								<display:column property="dispositionDate" title="Dispo dt." format="{0,date,MM/dd/yyyy}" style="width:8%;"/>
								<display:column property="diagnosisCode" title="Dx" style="width:10%;"/>
								<display:column property="contactInvestigationCase" title="Case No." style="width:10%;"/>
							 <display:setProperty name="basic.empty.showtable" value="true"/>
					       </display:table>
				      </td>
				     </tr>
				  </table>
				</td>
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