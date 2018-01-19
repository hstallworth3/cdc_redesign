<%@page import="gov.cdc.nedss.report.javaRepot.vo.Pa3VO"%>
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
<html>
<head>
<title>NBS: Manage Questions</title>
<base target="_self">
<%@ include file="../../../jsp/resources.jsp"%>
<script language="JavaScript">
var closeCalled = false;
function handlePageUnload(closePopup)
{
    // This check is required to avoid duplicate invocation 
    // during close button clicked and page unload.
    if (closeCalled == false) {
        closeCalled = true;
        
        if (event.clientY < 0 || closePopup == true) {
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
    
    var modWin = openWindow(URL, o, dialogFeatures, null, "");
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
				onclick="return handlePageUnload(true);" />
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
					Pa3VO pa3VO = (Pa3VO) request.getAttribute("PA3");
					if(pa3VO!=null){
					 ArrayList numberOfCasesColl = (ArrayList)pa3VO.getNumberOfCasesColl();
					 request.setAttribute("numberOfCasesColl",numberOfCasesColl);
					 ArrayList internetOcByContType = (ArrayList)pa3VO.getInternetOcByContType();
					 request.setAttribute("internetOcByContType",internetOcByContType);
					 ArrayList ipsIndexColl = (ArrayList)pa3VO.getIpsColl();
					 request.setAttribute("ipsIndexColl",ipsIndexColl);

           %>
		<nedss:container id="subsect_report_filter" name=""
			classType="sect" displayImg="false" includeBackToTopLink="no" displayLink="no">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="50%">
		             <tr>
		             <td align="center">
						  	<display:table name="numberOfCasesColl" class="dtTable" style="margin-top:0em;"  id="parent">
								<display:column property="label" title="Count Type" style="width:12%;"/>
								<display:column property="count1" title="Count" style="width:14%;"/>
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
					<table width="50%">
		             <tr>
		             <td align="center">
						  	<display:table name="ipsIndexColl" class="dtTable" style="margin-top:0em;"  id="parent">
								<display:column property="label" title="Count Type" style="width:12%;"/>
								<display:column property="count1" title="Count" style="width:14%;"/>
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
		<%}else{
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
				onclick="return handlePageUnload(true);" />
		</div>
	</html:form>
</body>
</html>