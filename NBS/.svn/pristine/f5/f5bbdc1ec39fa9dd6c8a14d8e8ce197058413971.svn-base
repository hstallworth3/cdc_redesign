<%@ page language="java" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<script type="text/javascript" src="eCSStender.js"></script>
<%@ page import="gov.cdc.nedss.webapp.nbs.helper.CachedDropDowns" %>   
<jsp:useBean id="BaseForm" scope="session" class="gov.cdc.nedss.webapp.nbs.form.util.BaseForm" />
            
<!-- Page Meta data Block -->

<table width="100%" class="OrangeSection">
	<tr>
		<td class="OrangeHeader" >
			<div id="investigationName">${condition}
				(${createdDate2})</div></td>
		<td align="right">
		
		<logic:equal name="BaseForm" property="mode" value="print">
		<input type="checkbox" id="survivingRecord2" title="surviving Record 2"
			name="survivingRecord2" disabled = "disabled" value="">
			</logic:equal>
			<logic:notEqual name="BaseForm" property="mode" value="print">
					<input type="checkbox" id="survivingRecord2" title="surviving Record 2"
			name="survivingRecord2" value="">
			</logic:notEqual>
			
			 <label id="survivingRecordL2">Surviving
				Record</label></td>
	</tr>
</table>
 <table class="style">
   <tr class="cellColor">
        <td class="border" colspan="2">
              <%
            String name = (String) request.getAttribute("patientLocalName2") == null ? "---" :  (String) request.getAttribute("patientLocalName2");
            name = name.trim(); 
            if (name.length() != 0) {
            	name = name;
            }else
            	name="---";
            
            String suffix = (String) request.getAttribute("patientSuffixName2") == null ? "" : (String) request.getAttribute("patientSuffixName2");
            if (suffix.length() != 0)
            {
            	name = name + ", "+suffix;
            }
            String currentSex = (String) request.getAttribute("patientCurrSex2") == null ? "---" :  (String) request.getAttribute("patientCurrSex2");
            currentSex = currentSex.trim(); 
            if(currentSex.length() !=0){
            	currentSex = currentSex;
            }else
            	currentSex="---";
            
            String DOB = (String) request.getAttribute("patientDOB2") == null ? "---" :  (String) request.getAttribute("patientDOB2");
            if(DOB.length() !=0){
            	DOB = DOB;
            }else
            	DOB="---";
            //DOB = currentsex.trim();
           
         %>
            <span class="valueTopLine"> <%= name %> </span>
            <span class="valueTopLine">|</span>
            <span class="valueTopLine"> <%= currentSex %> </span>
            <span class="valueTopLine">|</span>
       		<%if(request.getAttribute("currentAgeUnitCd") != null){%>
            <span class="valueTopLine"> <%= DOB %> (<%=request.getAttribute("currentAge2")%> <%=CachedDropDowns.getCodeDescTxtForCd((String)request.getAttribute("currentAgeUnitCd2"),"P_AGE_UNIT")%>) </span>
            <%}else{ %>
            <span class="valueTopLine"> <%= DOB %>  </span>
            <%} %>
        </td>
        <td style="padding:0.15em;width:24%; border-style:solid;border-color:#AFAFAF;text-align:right;}">
        	<span class="valueTopLine"> Patient ID: </span>
            <span style="font:16px Arial; margin-left:0.2em;"> ${BaseForm.attributeMap2.patientLocalId} </span>
            <span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
        </td>
    </tr>
 <% if (BaseForm.getActionMode().equals("Edit") ||
           BaseForm.getActionMode().equals("View") ||
           BaseForm.getActionMode().equals("EDIT_SUBMIT") ||
           BaseForm.getActionMode().equals("Compare") ||
           BaseForm.getActionMode().equals("Merge")) { %>
     <tr class="cellColor">
		<td class="border1">
		    <span class="label"> Investigation ID: </span>
		    <span class="value">${BaseForm.attributeMap2.caseLocalId}</span>
		    <%String coInfectionConditionList="";
		    	coInfectionConditionList=(String)BaseForm.getAttributeMap2().get("coInfectionConditionList");
		    	if (coInfectionConditionList != null && !coInfectionConditionList.isEmpty()){%>
		    <span class="value">|</span>
		    <span class="value">${BaseForm.attributeMap2.coInfectionID}</span>
		    <%} %>
		</td>
		<td class="border2">
		    <span class="label"> Created: </span>
		    <span class="value"> ${createdDate2}  </span>
		</td>
		<td class="border3">
		    <span class="label">By: </span>
		    <span class="value">  ${createdBy2}</span>
		</td> 
    </tr>
    
    <tr class="cellColor">
		<td class="border1">
		    <span class="label"> Investigation Status: </span>
		    <span class="value" id="headerInvestigationStatus">${BaseForm.attributeMap2.investigationStatus}</span>
		</td>
		<td class="border2">
		    <span class="label"> Last Updated: </span>
		    <span class="value"> ${updatedDate2}  </span>
		</td>
		<td class="border3">
		    <span class="label"> By: </span>
            <span class="value"> ${updatedBy2} </span>
        </td>
    </tr>
    <tr class="cellColor">
		<td class="border1">
		    <span class="label"> Investigator: </span>
		    <span class="value" id="headerCurrentInvestigator">${BaseForm.attributeMap2.investigatorName}</span>
		</td>
		<td class="border2">
		    <span class="label"> Case Status: </span>
            <span class="value" id="headerCaseStatus"> ${BaseForm.attributeMap2.caseClassCd} </span>
		</td>
		<td class="border3">
		    <span class="label">Notification Status: </span>
		    <span class="value" id="patientSummaryJSP_view_notificationStatus">
              <% 
                  String notificationStatus = (String) request.getAttribute("notificationStatus2") == null ? "" :  (String) request.getAttribute("notificationStatus2");
                  notificationStatus = notificationStatus.trim(); 
                  if (notificationStatus.length() != 0) { 
              %>  
                  <%= notificationStatus %>
              <% } %>                                    
          </span>
		</td>
    </tr>
   <% } %>
    <tr style="display:none">
    	<td><span class="value" id="headerConditionCode">${BaseForm.attributeMap2.headerConditionCode}</span>
    	<td><span class="value" id="headerProcessingDecision">${BaseForm.attributeMap2.ProcessingDecision}</span>
    </td></tr>

</table>
