<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<html>
	<head>
		<meta http-equiv="MSThemeCompatible" content="yes"/>
		<title>NBS: Create Notification</title>
		   <%@ include file="../../jsp/resources.jsp" %>     
		<script type='text/javascript' src='/nbs/dwr/engine.js'></script>
		<script type='text/javascript' src='/nbs/dwr/util.js'></script>	
		<script type='text/javascript' src='/nbs/dwr/interface/JPageForm.js'></script>
		<SCRIPT Language="JavaScript" Src="PageSpecific.js"></SCRIPT>
	
                     		
		<script language="JavaScript">
			function createPageNotif() {
				var opener = getDialogArgument();
				var comments = getElementByIdOrByName("NTF137").value;
				opener.createNotifications(comments);
				var pview = getElementByIdOrByNameNode("pageview", opener.document)
				pview.style.display = "none";				
				window.close();
			}
			
		
			
			function validateNotifReqFieldsDWR() {	
			            
                         	JPageForm.validateNotificationReqFields(function(data) {							
					if(data == null) {
					      getElementByIdOrByName("topButtIdMisgFlds").style.display="none";        					 
        					getElementByIdOrByName("botButtIdMisgFlds").style.display="none";
        					getElementByIdOrByName("headMisgFlds").style.display="none";
					 
						var comments = getElementByIdOrByName("notificationComments");
						comments.style.display='';	
						getElementByIdOrByName("NTF137").focus();
						 $j("#notificationRequiredtds").hide();				
        					 $j("#notificationCommentstr").show();	
        					 
          					// $j("#topButtIdMisgFlds").hide();	
        					// $j("#botButtIdMisgFlds").hide();        					 		
					} else if(data == "ERROR") {
							getElementByIdOrByName("notificationError").style.display="";
					} else {
					     // alert("Missing Fields");
						var listing = getElementByIdOrByName("notifReqFlds");
						
						for (var i = 0; i < data.length; i++) {
							//alert("Item " + i + " is " + data[i]);
							var newLI = document.createElement("li");
							newLI.innerHTML = data[i];
							listing.appendChild(newLI);						
						}
						
						 getElementByIdOrByName("topcreatenotId").style.display="none";
        					 getElementByIdOrByName("botcreatenotId").style.display="none";
       					 getElementByIdOrByName("headNotComments").style.display="none";

						 $j("#notificationCommentstr").hide();	
						 $j("#notificationRequiredtds").show();	
						  //$j("#topcreatenotId").hide();	
        					  //$j("#botcreatenotId").hide();        	
						var reqFields = getElementByIdOrByName("notificationRequiredFields");
						reqFields.style.display='';
					}
				});	
			}
			
			function validateNotifReqFields() {	
			
                      <% 
                        ArrayList list = new ArrayList();
                        if(request.getAttribute("NotificationReqFieldsValList") != null){
                        
                      // String watemplateUid="1000879";
                      // map = util.getBatchMap(new Long(watemplateUid));
                          list =(ArrayList)request.getAttribute("NotificationReqFieldsValList");
                      }%>
			              
                  var  data = "<%=request.getAttribute("NotificationReqFieldsValList")%>";
                   
			   
			                          					
					if("<%=list%>" == null || "<%=list.size()%>" == 0) {
					      getElementByIdOrByName("topButtIdMisgFlds").style.display="none";        					 
        					getElementByIdOrByName("botButtIdMisgFlds").style.display="none";
        					getElementByIdOrByName("headMisgFlds").style.display="none";
					 
						var comments = getElementByIdOrByName("notificationComments");
						comments.style.display='';	
						getElementByIdOrByName("NTF137").focus();
						 $j("#notificationRequiredtds").hide();				
        					 $j("#notificationCommentstr").show();	
        					 
          					// $j("#topButtIdMisgFlds").hide();	
        					// $j("#botButtIdMisgFlds").hide();        					 		
					} else if("<%=list%>" == "ERROR") {
							getElementByIdOrByName("notificationError").style.display="";
					} else {
					     // alert("Missing Fields");
						var listing = getElementByIdOrByName("notifReqFlds");
						
						<%
						  for(int j=0;j<list.size();j++){%>
							
							 // alert("Item " + "<%=j%>" + " is " + "<%=list.get(j)%>");
							  if("<%=list.get(j)%>" != 'Undefined' || "<%=list.get(j)%>" != 'undefined'){
								var newLI = document.createElement("li");
								newLI.innerHTML = "<%=list.get(j)%>";
								listing.appendChild(newLI);	
								}
							  
						 <% } %>
						
						
						 getElementByIdOrByName("topcreatenotId").style.display="none";
        					 getElementByIdOrByName("botcreatenotId").style.display="none";
       					 getElementByIdOrByName("headNotComments").style.display="none";

						 $j("#notificationCommentstr").hide();	
						 $j("#notificationRequiredtds").show();	
						  //$j("#topcreatenotId").hide();	
        					  //$j("#botcreatenotId").hide();        	
						var reqFields = getElementByIdOrByName("notificationRequiredFields");
						reqFields.style.display='';
					}
				
			}
						
			function closePageNotif() {
			    
				var opener = getDialogArgument();			
			     var pview =getElementByIdOrByNameNode("pageview", opener.document);
				//alert("Hello3: " +pview );
	
				pview .style.display = "none";
				 self.close();
	  		    return false;					
			}
			
			function editPageNotif() {
				var opener = getDialogArgument();		
				JPageForm.setNotfReqFields(function(data) {
					var result = opener.editForm();
					if (result)
						opener.document.forms[0].submit();	
					self.close();
				});				
			}



			function checkMaxLength(sTxtBox) {				
				maxlimit = 1000;					
				if (sTxtBox.value.length > maxlimit)		
					sTxtBox.value = sTxtBox.value.substring(0, maxlimit);
			}	
			
					
		</script>
	</head>

	<body onload="validateNotifReqFields()">
	   <div id="headMisgFlds" style="width:100%; margin:0 auto; margin-top:3px; margin-bottom:3px; text-align:left; font-size:1.1em; font-weight:bold;color:white;background:#003470; padding:3px 0px">Create Notification: Missing Data</div>
   	   <div id="headNotComments" style="width:100%; margin:0 auto; margin-top:3px; margin-bottom:3px; text-align:left; font-size:1.1em; font-weight:bold;color:white;background:#003470; padding:3px 0px">Create Notification: Notification Comments</div>
		<div  id="topButtIdMisgFlds" class="grayButtonBar" style="text-align: right;">
			<input type="Button" align="right"  value="   Edit   " onclick="editPageNotif()"/>
			<input type="Button" align="right"  value= "Cancel" onclick="closePageNotif()"/>	
	      </div>							
		<div id="topcreatenotId" class="grayButtonBar" style="text-align: right;">
			<input type="Button" align="right"  value="Submit" onclick="createPageNotif()"/>
			<input type="Button" align="right"  value= "Cancel" onclick="closePageNotif()"/>	
		</div>	
  <div style="padding:0.5em 0em;"></div>
	<nedss:container id="sect_createNotif" name="Create Notification" classType="sect" displayImg="false" includeBackToTopLink="no" displayLink="no">
	         <nedss:container id="subsect_createNotif" name="" classType="subSect" displayImg="false" includeBackToTopLink="no">
       		
       	<tr>  <td class="fieldName" id="notificationError"> 
		<span style="display:none;font-size:10pt;font-family:Geneva,Arial,Helvetica,sans-serif;font-weight:bold;color:red;">
				<br/>
				<p align="left"> Error while Validating Notification Required Fields.</p>
			  <p align="center"><input type="Button" class="Button" value="Close" onclick="closePageNotif()"/></p>				
		</span></td></tr>  
		<tr><td colspan="2" id="notificationRequiredtds">
  	         <div class="infoBox errors" id="notificationRequiredFields">
		       <b> <a name="errorMessagesHref"></a>A notification cannot be created because data is missing for the following field(s):</b> <br/>						
				<ul id="notifReqFlds"></ul>				
		    </div>
		  </td></tr>
			 <tr id="notificationCommentstr">
	                        <td class="fieldName" id="notificationComments"> 
	                            <b>Notification Comments:</b>  
	                        </td>
	                        <td>
	                           <textarea rows="6" cols="60" id="NTF137" onkeydown="checkMaxLength(this)" onkeyup="checkMaxLength(this)"></textarea>
	  	                  </td>
	                    </tr>
	                    </nedss:container>
	        </nedss:container>
	        </div>
	 

	<div   id="botButtIdMisgFlds" class="grayButtonBar" style="text-align: right;">
		<input type="Button" align="right"  value="   Edit   " onclick="editPageNotif()"/>
		<input type="Button" align="right"  value= "Cancel" onclick="closePageNotif()"/>	
	</div>
			<div id="botcreatenotId" class="grayButtonBar" style="text-align: right;">
				<input type="Button" align="right"  value="Submit" onclick="createPageNotif()"/>
				<input type="Button" align="right"  value= "Cancel" onclick="closePageNotif()"/>	
			</div>	
			
		
</body>	
</html>