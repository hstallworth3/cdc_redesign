<%@ include file="/jsp/errors.jsp" %>
<title>Manage Link between Lab Result and Snomed</title>
<script type="text/javascript">
	function cancelForm(){
		var confirmMsg="If you continue with the Cancel action, you will lose any information you have entered. Select OK to continue, or Cancel to not continue.";
		if (confirm(confirmMsg)) {
			document.forms[0].action ="${SRTAdminManageForm.attributeMap.cancel}";
		} else {
			return false;
		}
	}	
	
	function submitForm(){
			if(LabResultSnomedReqFlds()) {
				return false;
			} else {
				document.forms[0].action ="${SRTAdminManageForm.attributeMap.submit}";
			}
	}	
</script>
      	
<html:form styleId="createForm" action="/ExistingResultsSnomedLink.do">
    <nedss:container id="section3" name="${SRTAdminManageForm.actionMode} Lab Result And SNOMED Link "  classType="sect" displayImg ="false" displayLink="false">
        <fieldset style="border-width:0px;" >
            <nedss:container id="section3" classType="subSect" displayImg ="false">
                <!-- Form Entry Errors -->
                <tr style="background:#FFF;">
                    <td colspan="2">
                        <div class="infoBox errors" style="display:none;" id="srtDataFormEntryErrors">
                        </div>                        
                    </td>
                </tr>
                
                <tr>
                    <td class="fieldName" id="snomCd"><font class="boldTenRed" > * </font><span>SNOMED Code:</span></td>
                    <td>
                        <logic:equal  name="SRTAdminManageForm" property="actionMode" value="Create">
                            <span id="snomed">${SRTAdminManageForm.selection.snomedCd}</span>
                            <input type="button" name="submit" id="submit" value="Search" onClick="searchSnomed();"/>
                        </logic:equal>
                        <logic:equal  name="SRTAdminManageForm" property="actionMode" value="View">              
                            <nedss:view name="SRTAdminManageForm" property="selection.snomedCd"/>
                        </logic:equal>
                    </td>
                </tr>
                <tr>
                    <td class="fieldName"><span>Lab Result Code:</span></td>
                    <td>
                        <nedss:view name="SRTAdminManageForm" property="selection.labResultCd"/>
                    </td>
                </tr>
                <tr>
                    <td class="fieldName"><span>Lab ID:</span></td>
                    <td><nedss:view name="SRTAdminManageForm" property="selection.laboratoryID"/></td>
                </tr>
                <tr>
                    <td class="fieldName"><span>Lab Name:</span></td>
                    <td>
                        <nedss:view name="SRTAdminManageForm" property="selection.laboratoryID" methodNm="LaboratoryIds"/>
                    </td>
                </tr>
                
                <logic:equal  name="SRTAdminManageForm" property="actionMode" value="Create">	
                    <tr>
                        <td colspan="2" align="right">
                            <table>
                                <tr>
                                    <td class="InputField">
                                        <input type="submit" name="submit" id="submit" value="Submit" onClick="return submitForm();"/>
                                        <input type="submit" name="submit" id="submit" value="Cancel" onClick="return cancelForm();"/>			  
                                        &nbsp;
                                    </td>	  	
                                </tr>  
                            </table>
                        </td>
                    </tr>
                </logic:equal>  	
            </nedss:container>
        </fieldset>	
    </nedss:container>
</html:form>