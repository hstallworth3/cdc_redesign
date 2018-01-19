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
<script language="javascript">
	function AddVaccinationPopUp()
    {
     	var urlToOpen = "/nbs/PageAction.do?method=createGenericLoad&mode=Create&Action=InvPath&businessObjectType=VAC";
     	var divElt = getElementByIdOrByName("pageview");
     	if (divElt == null) {
     		divElt = getElementByIdOrByName("blockparent");
     	}
     	divElt.style.display = "block";
     	var o = new Object();
     	o.opener = self;
     	
     	var modWin = openWindow(urlToOpen, o, GetDialogFeatures(980, 900, false, true), divElt, "");
    }
</script>
           
<html:hidden name="manageEventsForm" property="selectedcheckboxIds" styleId="chkboxIds"/>
<div id="pageview"></div>

<% 
int  subSectionIndex= (new Integer(request.getParameter("param2").toString())).intValue();
String tabId = request.getParameter("param3").toString();
%>

<% String sectionId = "vaccination"; %>

<nedss:container id="<%= tabId + (++subSectionIndex) %>" name="Vaccinations" classType="subSect">     
    <tr>
	    <td colspan="2">
            <!-- Display tab table for listing all Vaccination reports -->
            <display:table name="vaccinationList" class="dtTable" id="vaccilist"  style="margin-top:0.15em;">
	            <display:column style="width:5%;" >
	            	<div align="center" style="margin-top: 3px">
	            		<input type="checkbox" value="${vaccilist.isAssociated}" name="${vaccilist.localId}" ${vaccilist.checkBoxId}  />
	            	</div>
	            </display:column>
	            <display:column property="actionLink" title="Date Administered" style="width:10%;"/>
	            <display:column property="vaccineAdministered" title="Vaccine Administered" style="width:60%;"/>
	            <display:column property="localId" title="Vaccination ID" style="width:15%;"/>
	            <display:setProperty name="basic.empty.showtable" value="true"/>
	        </display:table>
	        <!-- Button to add new lab reports -->
		    <%          
            if(((String)request.getAttribute("AddVaccPermission")).equals("true")) 
            { %> 
            <div style="text-align:right; margin-top:0.5em;">
                <input type="button" value="Add Vaccination" onclick="AddVaccinationPopUp();">
            </div>        
            <% 
            } %>
	    </td>
    </tr>
</nedss:container>
<img style="background-color: #5F8DBF;width: 100%;" alt="" border="0" height="1" width="200" src="transparent.gif">
 
   