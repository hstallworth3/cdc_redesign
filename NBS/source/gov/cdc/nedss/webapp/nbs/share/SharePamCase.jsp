<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ page isELIgnored ="false" %>
<%@ page import = "gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="gov.cdc.nedss.util.*" %>
<html>
	<head>		
		<title>Share Case Report</title>
		<%@ include file="/jsp/resources.jsp" %>
		<SCRIPT Language="JavaScript" Src="NotificationSpecific.js"></SCRIPT>	
		
	</head>
	<script language="JavaScript">
	
	blockEnterKey();
	
	function tOwnership() {
		
		getElementByIdOrByName("errorBlock").style.display="none";
	    
	    var errors = new Array();
	    var index = 0;
	    var isError = false;
	    var opener = getDialogArgument();
		var recipient= trim(getElementByIdOrByName("recipient").value);
		var comment = trim(getElementByIdOrByName("NTF137").value);

		if( recipient != null && recipient == 0) {
			errors[index++] =  "Recipient is required";
			getElementByIdOrByName("shareRecipient").style.color="red";
			isError = true;		
		}
		else {
		   getElementByIdOrByName("shareRecipient").style.color="black";
		}
		if( comment != null && comment== 0) {
			errors[index++] =  "Share Comments is required";
			getElementByIdOrByName("shareComments").style.color="red";
			isError = true;		
		}
		else {
		   getElementByIdOrByName("shareComments").style.color="black";
		}
	    if(isError) {
			displayErrors("errorBlock", errors);
		}
		else {
			opener.sharePamCaseSubmit(recipient,comment);
			var invest = getElementByIdOrByNameNode("pamview", opener.document)
			invest.style.display = "none";				
			window.close();
		}
	}
	function trim(str)
	{
	    while (str.charAt(0) == " ") {
	        // remove leading spaces
	        str = str.substring(1);
	    }
	    
	    while (str.charAt(str.length - 1) == " "){
	        // remove trailing spaces
	        str = str.substring(0,str.length - 1);
	    } 
	    return str;
	}
	
	function closeTOwnership() {
		self.close();
		var opener = getDialogArgument();			
		var invest = getElementByIdOrByNameNode("pamview", opener.document)
		invest.style.display = "none";					
	}
	function checkMaxLength(sTxtBox) {				
		maxlimit = 1000;					
		if (sTxtBox.value.length > maxlimit)		
		sTxtBox.value = sTxtBox.value.substring(0, maxlimit);
	}			
</script>
	<body onunload="closeTOwnership()">		
		<html:form>
			
			  <table cellspacing="15" cellpadding="20" style="font-family : Geneva, Arial, Helvetica, sans-serif; font-size : 10pt;">
			  
			  <!-- Form Entry Errors -->
              <tr style="background:#FFF;">
                  <td colspan="2">
                      <div class="infoBox errors" style="display:none;" id="errorBlock">
                      </div>
                  </td>
              </tr>
		           <tr id="exportList">
		               <td class="InputFieldLabel" id="transferTo">
		                      <span style="color:#FF0000">*</span>
			                <span style="" id="shareRecipient">
			                        
			                    Recipient:
			                </span> 
			        </td>
		               <td class="InputField">
			               	<html:select  property="pamClientVO.oldPamProxyVO.notificationVO_s.theNotificationDT.exportReceivingFacilityUid"  styleId ="recipient">
			               		<html:optionsCollection property="exportFacilityList" value="key" label="value"/>
		                  	</html:select>
		               </td>
		          </tr>
      		            <tr id="comment">
      		           <td class="InputFieldLabel" id=" TransferComments">
		                      <span style="color:#FF0000">*</span>
			                <span style=""  id="shareComments">
			                       
			                    Share Comments:
			                </span> 
			        </td>
		               <td>
				 	<textarea rows="6" cols="60" id="NTF137" onkeydown="checkMaxLength(this)" onkeyup="checkMaxLength(this)"></textarea>
				 </td>
		          </tr>
     		          <tr>
				      <td align="right" colspan="2">
				            <input type="Button" class="Button" value="Submit" onclick="tOwnership()"/>
				            <input type="Button" class="Button" value="Cancel" onclick="closeTOwnership()"/>
				      </td>
			     </tr>																				  
			  </table>
		</span>
	</html:form>
</body>	
</html>