<%@ include file="/jsp/tags.jsp"%>
<%@ include file="/jsp/resources.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="gov.cdc.nedss.webapp.nbs.form.hivpartnerservices.HivPartnerServicesForm"%>
<html>


<head>
<title>Manage HIV Partner Services File</title>
<script type="text/javaScript" src="HivPartnerServices.js">
	
</script>
<SCRIPT LANGUAGE="JavaScript">
	function startCountdown() {
	autocompTxtValuesForJSP();
	var sessionTimeout = <%=request.getSession().getMaxInactiveInterval()%>
	min = sessionTimeout / 60;
	sec = 0;
	getTimerCountDown();
}
	
function cancelForm(){

   	document.forms[0].action ="${hivPartnerServicesForm.attributeMap.cancel}";
	document.forms[0].submit();
}

function submitForm(){

        if(validatePartnerServicesFileRequest()) {
        	return false;
	} else {
	        document.forms[0].action ="${hivPartnerServicesForm.attributeMap.submit}";
		document.forms[0].submit();
	}	
}
</SCRIPT>
</head>

<body onload="startCountdown()">
	<div id="doc3">
		<div id="bd">
			<%@ include file="../../jsp/topNavFullScreenWidth.jsp"%>
			<%@ include file="../../jsp/feedbackMessagesBar.jsp"%>
			<html:form >

				<!-- Form Entry Errors -->

				<tr style="background: #FFF;">
					<td colspan="4">
						<div class="infoBox errors" style="display: none;"
							id="hivPartnerServicesFormEntryErrors"></div>
					</td>
				</tr>

				<tr>

					<td>
						<table bgcolor="white" border="0" cellspacing="0" cellpadding="0"
							width="100%">
							<tr>
								<td align="right" style="padding-top: 5px;" id="srtLink"><a
									href="/nbs/SystemAdmin.do?focus=systemAdmin2">Return to System Management Main Menu</a></td>
							</tr>
						</table>
					</td>

				</tr>


				<tr>
					<td>
						<table bgcolor="white" border="0" cellspacing="0" cellpadding="0"
							width="100%">
							<tr>
								<td align="right" style="padding-top: 5px;"><i> <font
										class="boldTenRed"> * </font><font class="boldTenBlack">Indicates
											a Required Field </font>
								</i></td>
							</tr>
						</table>
					</td>
				</tr>
				<html:errors />
				
				<div class="sect" id="hivPartnerFileInfo">
					<h2 class="sectHeader">
						<a class="anchor"> Create File </a>
					</h2>

				<div class="sectBody">
						<table class="subSect">
							
										<tr>
												<td >
													<bean:message key="hivPartnerServices.infoMsg"  />
												
												</td>
											</tr>
						</table>
				</div>
					<div class="sectBody">
						<table class="subSect">
							<tr>
								<td colspan="2">
									<table bgcolor="white" border="0" cellspacing="0"
										cellpadding="0" width="100%">
										
										<logic:notEmpty name="hivPartnerServicesForm" property="confirmationMessage">
											<tr>
												<td id="Msg" >
													<div class="infoBox success">
														<nedss:view name="hivPartnerServicesForm" property="confirmationMessage" />
													</div>
												</td>
											</tr>
										</logic:notEmpty>
									</table>
								</td>
							</tr>

	                            	<td class="fieldName" nowrap id="RptMonL">
	                            	  <font class="boldTenRed"> * </font>Reporting Month:</td>
	                            	<td>
	                                 <html:select name="hivPartnerServicesForm" property="reportingMonth" styleId="RptMon" >
	                                    <option value=""></option>
	                                    <option value="3"> March </option>
	                                    <option value="9"> September </option>                      
	                                </html:select>
	                           </td>
	                        </tr>
	                         <td class="fieldName" nowrap id="RptYrL">
	                            	  <font class="boldTenRed"> * </font>Reporting Year:</td>
	                            <td>
	                                 <html:select name="hivPartnerServicesForm" property="reportingYear" styleId="RptYr" >
	                                    <option value=""></option>
	                                    <option value="2013"> 2013 </option> 
	                                    <option value="2014"> 2014 </option>  
	                                    <option value="2015"> 2015 </option>  
	                                    <option value="2016"> 2016 </option>
	                                    <option value="2017"> 2017 </option> 
	                                    <option value="2018"> 2018 </option> 
	                                    <option value="2019"> 2019 </option> 
	                                    <option value="2020"> 2020 </option> 
	                                    <option value="2021"> 2021 </option> 
	                                    <option value="2022"> 2022 </option>
	                                    <option value="2023"> 2023 </option> 
	                                </html:select>
	                           </td>
	                        </tr>	                        

				<tr>
					<td class="fieldName" nowrap id="ConPerL"><font class="boldTenRed"> * </font>Contact Person:</td>
								<td><html:text styleId="ConPer" name="hivPartnerServicesForm"
										property="contactPerson" maxlength="100" size="40" /></td>
							</tr>

							<!-- Update Buttion -->
							<tr>
								<td colspan="2" align="right">
									<table>
										<tr>
											<td><input id="updateBtn" type="button" value="Submit" onclick="submitForm()"> &nbsp;
											<td><input id="cancelBtn" type="button" value="Cancel" onclick="cancelForm()"> &nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</html:form>
		</div> <!-- bd --> 
	</div>  <!-- doc3 --> 
</body>
</html>