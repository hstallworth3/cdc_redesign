<%@ page language="java" %>
<%@ page isELIgnored ="false" %>
<%@ page import="gov.cdc.nedss.util.NEDSSConstants" %>

<style type="text/css">
    option.imagebacked 
    {
        padding: 2px 0 2px 20px;
        background-repeat: no-repeat;
        background-position: 1px 2px;
        vertical-align: middle;
    }
</style>
<!-- Bottom Button Bar -->
<table style="background-image: url('task_button/tb_cel_bak.jpg');background-repeat: repeat-x;" class="bottomButtonBar">
    <tr>
        <!-- Investigation related actions -->
        <td style="vertical-align:top; padding:0px;">
            <logic:equal name="BaseForm" property="actionMode" value="View">
                <table align="left">
                    <tr>
                        <logic:equal name="BaseForm" property="securityMap(checkManageEvents)" value="true">
                        <logic:notEqual name="BaseForm" property="attributeMap.ManageEventsDisplay" value="NOT_DISPLAYED">
                            <td style="vertical-align:top; padding-top:0px;">
                                <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="manageAssociations"  id="manageAssociations"  alt="Manage Associations" 
                                        class="cursorHand" onclick="manageAssociations();"> <br/>
                                Manage <br/> Associations
                            </td>
                             </logic:notEqual>
                        </logic:equal>
                        <logic:equal name="BaseForm" property="securityMap(checkManageNotific)" value="true">
                        <logic:notEqual name="BaseForm" property="attributeMap.CreateNotificationDisplay" value="NOT_DISPLAYED">
                            <td style="vertical-align:top; padding-top:0px;">
                                <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="createNoti"     id="createNoti" alt="Create Notification" 
                                        class="cursorHand" onclick="return createPamNotification();"> <br/>
                                Create <br/> Notifications
                            </td>
                             </logic:notEqual>
                        </logic:equal>
                        <logic:equal name="BaseForm" property="securityMap(checkCaseReporting)" value="true">
                        <logic:notEqual name="BaseForm" property="attributeMap.CaseReportingDisplay" value="NOT_DISPLAYED">
                         <logic:notEqual name="BaseForm" property="securityMap(shareButton)" value="NOT_DISPLAYED">
                            <td style="vertical-align:top; padding-top:0px;">
                                <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="caseRep"     id="caseRep" alt="Case Reporting" 
                                        class="cursorHand" onclick="return sharePamCaseLoad();"> <br/>Share <br/> Document
                            </td>
                                  </logic:notEqual>
                             </logic:notEqual>
                        </logic:equal>
                        
                        <logic:equal name="BaseForm" property="securityMap(checkTransfer)" value="true">       
                            <logic:notEqual name="BaseForm" property="attributeMap.TransferOwnershipDisplay" value="NOT_DISPLAYED">
                                <td style="vertical-align:top; padding-top:0px;">
                                    <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="createNoti" id="createNoti" alt="Transfer Ownership" 
                                        class="cursorHand" onclick="return transferPamOwnership();"> <br/>
                                    Transfer <br/> Ownership
                                </td>
                            </logic:notEqual>
                        </logic:equal>
                        
                        <logic:equal name="BaseForm" property="securityMap(checkChangeCondition)" value="true">       
                                <td style="vertical-align:top; padding-top:0px;">
                                    <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40"
                                        border="0" name="changeCond" id="changeCond" alt="Change Condition" 
                                        class="cursorHand" onclick="return pgChangeCondition();"> <br/>
                                        Change <br/> Condition
                                </td>
                        </logic:equal>                           
                    </tr>
                </table>
            </logic:equal>
        </td>

        <!-- General page actions like create, edit, delete and print --> 
        <td style="vertical-align:top; padding:0px;">
            <table align="right">
                <tr>
                    <logic:notEqual name="BaseForm" property="actionMode" value="View">
                        <td style="vertical-align:top; padding-top:0px;">
                            <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                    border="0" name="Submit" id="Submit" alt="Submit button" 
                                    class="cursorHand" onclick="return saveForm();"> <br/>
                             Submit
                        </td>
                        <td style="vertical-align:top; padding-top:0px;">
                            <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                    border="0" name="Cancel" id="Cancel" alt="cancel button" 
                                    class="cursorHand" onclick="return cancelForm();"> <br/>
                            Cancel
                        </td>
                    </logic:notEqual>
                    <logic:equal name="BaseForm" property="actionMode" value="View">
                        <logic:equal name="BaseForm" property="securityMap(editInv)" value="true">
                            <td style="vertical-align:top; padding-top:0px;">
                                <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="Delete" id="delete" 
                                        alt="Edit button" class="cursorHand" 
                                        onclick="return editForm();" /><br/>Edit
                            </td>
                        </logic:equal>
                        <logic:equal name="BaseForm" property="securityMap(deleteInvestigation)" value="true">
                            <td style="vertical-align:top; padding-top:0px;">
                                <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="Delete" id="delete" 
                                        alt="Delete button" class="cursorHand" 
                                        onclick="return deleteForm();" /><br/>Delete
                            </td>
                        </logic:equal>
                        <td style="vertical-align:top; padding-top:0px;">
                            <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40" 
                                        border="0" name="Print" id="print" 
                                        alt="Print page button" class="cursorHand" 
                                        onclick="return showPrintFriendlyPage();" /><br/>Print                                   
                        </td>
                    
                         <logic:equal name="BaseForm" property="securityMap(printCDCFRForm)" value="true">
							<td style="vertical-align:top; padding-top:0px;">
								<input type="image" src="task_button/fa_submit.jpg"  width="30" height="40"
											border="0" name="printcdcforms" id="printcdcforms" 
											alt="Print cdc form button" class="cursorHand" 
											onclick="return printAllForms();" /><br/>Print CDC Forms
							</td>
                        </logic:equal>
                        <logic:notEqual name="BaseForm" property="securityMap(printCDCForm)" value="NOT_DISPLAYED">
                        <td style="vertical-align:top; padding-top:0px;">
                            <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40"
                                        border="0" name="Delete" id="delete" 
                                        alt="Print form button" class="cursorHand" 
                                        onclick="return printForm();" /><br/>Print CDC Form
                        </td>
                        </logic:notEqual>                        
                        
                    </logic:equal>
                    <logic:equal name="BaseForm" property="actionMode" value="CREATE_EXTEND">
                           <td style="vertical-align:top; padding-top:0px;">
		                  <input type="image" src="task_button/fa_submit.jpg"  width="30" height="40"
		                  border="0" name="Add Extended Data" id="delete" 
		                  alt="Add extended button" class="cursorHand" 
		            onclick="return addExtendedForm();" /><br/>Add<br/>Extended<br/>Data
                        </td>
                    </logic:equal>                    
                </tr>            
            </table>
        </td>
    </tr>
</table>