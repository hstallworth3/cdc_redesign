<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/nedss.tld" prefix="nedss"%>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout"%>
<%@ page import="gov.cdc.nedss.util.NEDSSConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="gov.cdc.nedss.webapp.nbs.action.decisionsupportmanagement.util.DecisionSupportConstants" %>
<%@ page
	import="gov.cdc.nedss.webapp.nbs.form.decisionsupportmanagement.DecisionSupportForm"%>
<%@ page
	import="gov.cdc.nedss.webapp.nbs.action.decisionsupportmanagement.DecisionSupportClientVO.DecisionSupportClientVO"%>
<%@ page import="gov.cdc.nedss.pagemanagement.wa.dt.BatchEntry"%>
<%@ page
	import="gov.cdc.nedss.webapp.nbs.action.decisionsupportmanagement.util.DecisionSupportUtil"%>
	
<%@ page isELIgnored="false"%>
<% DecisionSupportForm fform = (DecisionSupportForm)request.getSession().getAttribute("DSForm");
 	DecisionSupportClientVO dsVo = fform.getDecisionSupportClientVO();
 	DecisionSupportUtil dsutil = new DecisionSupportUtil();
  %>

		<!-- Subsection: Investigation Default Values -->
		<nedss:container id="IdAdvancedSubSection" classType="subSect"
			name="Advanced Criteria" addedClass="batchSubSection">
			<tr>
				<td colspan="2" width="100%">
						<table class="dtTable" align="center" >
							<thead>
								<tr>
									<th width="5%"></th>
									<th width="40%"><b> Question </b></th>
									<th width="15%"><b> Logic </b></th>
									<th width="40%"><b> Value </b></th>
								</tr>
							</thead>
							<%ArrayList  batchList = dsVo.getAdvancedCriteriaBatchEntryList();
					  		Iterator  iter = batchList.iterator();
					  		int count=0;%>
							<% while (iter.hasNext()) {
					 
			            	BatchEntry batchEnt = (BatchEntry) iter.next();
			            	if (batchEnt != null) {
			            	if(count ==0 || count%2==0){%>
							<tr class="odd">
								<%}else{%>
								<tr class="even">
									<% } %>
									<td style="width:3%;text-align:center;"><input id="viewIdAdvancedSubSection" type="image"
										src="page_white_text.gif"
										onclick="viewClickedOnAdvancedCriteriaViewPage('<%=count%>','advancedCriteriaTable');return false"
										name="image" align="middle" cellspacing="2" cellpadding="3"
										border="55" class="cursorHand" title="View"></td>
									<td>
									<% String question = (String) batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.CRITERIA_QUESTION);%>
									<%String questionlabel = dsutil.getQueLabelForId(question, fform); %>
									<%=questionlabel%></td>
									<td>
									<% String logic= (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.CRITERIA_LOGIC);
									 String logicValSet = dsutil.getLogicValueSet(question, fform);
									 String logicDesc = dsutil.getCodedescForCode(logic, logicValSet); %>
									<%=logicDesc%></td>
									<td>
									<%
			            	String value = "";
			            	String valSet = dsutil.getQueValueSet(question, fform);
			            	if(valSet != null && valSet.indexOf("^CODED") == -1)// Multiselect
			            	{

			            		if(valSet.indexOf("^^") == -1){// Multi select
			            			String[] valCode =(String[])batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.CRITERIA_VALUE);
			            			
			            			for(int i=0; i<valCode.length; i++)
			            			{
			            				value = value+DecisionSupportUtil.getCodedescForCode(valCode[i], valSet)+", ";
			            			}
			            		}else// Single select
			            		{
			            			String valCode = (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.CRITERIA_VALUE);
			            			String codeSet = valSet.substring(0,valSet.indexOf("^^"));
			            			value = DecisionSupportUtil.getCodedescForCode(valCode, codeSet);
			            		}
			            	}else if(valSet != null && valSet.indexOf("^CODED") != -1){//Structure Numeric Coded
			            		String codedVal = (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.CRITERIA_VALUE);
			            		String val = codedVal.substring(0, codedVal.indexOf("^"));
			            		String code = codedVal.substring(codedVal.indexOf("^")+1, codedVal.length());
			            		String codeSet = valSet.substring(0, valSet.indexOf("^CODED"));
			            		String codeDesc = dsutil.getCodedescForCode(code, codeSet);
			            		value = val + codeDesc;
			            	}
			            	else
			            	{
			            		value= (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.CRITERIA_VALUE);// Text, Date, TextArea, Literal
			            	}
			            	%> <%=value%></td>
									
								</tr>
								<%count=count+1; %>
								<% }
				        }
					  %>
							
						</table>

						<table id="advancedCriteriaTable" width="100%">
							<tr>
								<td class="fieldName" width="100%">Question:</td>
								<td id="advQuestion" width="100%">&nbsp;</td>
							</tr>
							<tr>
								<td class="fieldName">logic:</td>
								<td id="advLogic">&nbsp;</td>
							</tr>
							<tr>
								<td class="fieldName">Value:</td>
								<td id="advValue">&nbsp;</td>
							</tr>
						</table>
						</td>
			</tr>
		</nedss:container>

<nedss:container id="ElrTheAdvancedSubSection" classType="subSect" 	name="Advanced Criteria">
					<tr>
						<td colspan="2" width="100%">
										<span><b>If the number of days falls within the timeframe specified below, the action specified in the algorithm will be 
																applied.</b> The number of days is calculated as the difference between the incoming electronic lab report (specimen collection date) 
																and existing investigations for the same patient and condition (specimen collection date of earliest associated lab or investigation create date if no associated lab). 
													</span></td>
											</tr>
											<tr>
												<td id="useEventDateLogicLabel" class="fieldName">
												<span>Apply timeframe logic to this algorithm:</span></td>
												<td id="useEventDateLogic"><nedss:view name="decisionSupportForm"
														property='<%="decisionSupportClientVO.answer("+DecisionSupportConstants.USE_EVENT_DATE_LOGIC+")"%>' 
														codeSetNm="YN"/>
											   </td>
											</tr>											
											<tr>
												<td class="fieldName">
												<span>Event Date:</span>
												</td>
												<td><nedss:view name="decisionSupportForm"
														property='<%="decisionSupportClientVO.answer("+DecisionSupportConstants.NBS_EVENT_DATE_SELECTED+")"%>' 
														codeSetNm="NBS_EVENT_DATE"/>
											   </td>
											</tr>
											<tr>
														<td class="fieldName">
														<span>Timeframe: </span></td>
													<td><nedss:view name="decisionSupportForm" property='<%="decisionSupportClientVO.answer("+DecisionSupportConstants.TIMEFRAME_OPERATOR_SELECTED+")"%>'/>
													<nedss:view name="decisionSupportForm" property='<%="decisionSupportClientVO.answer("+DecisionSupportConstants.TIMEFRAME_DAYS+")"%>' />
													<span> Days</span>
													</td>
								          </tr>
						<tr><td align="left" colspan=2"><hr/></td></tr>  											
						<tr>
							<td colspan=2"><span>The type of logic
								that should be applied to the lab criteria entered in the
								table below. All test/result data entered below will have
								either AND or OR logic applied.</span></td>
							</tr>
										<tr>
											<td nowrap class="fieldName">And/Or Logic Setting:</td>
												<td> <nedss:view name="decisionSupportForm" property='<%="decisionSupportClientVO.answer("+DecisionSupportConstants.AND_OR_LOGIC+")"%>'  /> 
											   </td>
				</tr>
</nedss:container>
																						

<nedss:container id="ElrIdAdvancedSubSection" classType="subSect"
			name="Lab Criteria" addedClass="batchSubSection">
	<tr>
		<td colspan="2" width="100%">
				<table class="dtTable" align="center" >
					<thead>
						<tr>
							<th width="5%"> &nbsp;</th>
						    <th width="40%"><b>Resulted Test</b></th>
						    <th width="15%"><b>Operator</b></th> 
						    <th width="40%"><b>Result</b></th>
						</tr>
					</thead>
					<tbody>
					<%ArrayList  batchList = dsVo.getAdvancedCriteriaBatchEntryList();
					  		Iterator  iter = batchList.iterator();
					  		int count=0;%>
							<% while (iter.hasNext()) {
					 
			            	BatchEntry batchEnt = (BatchEntry) iter.next();
			            	if (batchEnt != null) {
			            	if(count ==0 || count%2==0){%>
							<tr class="odd">
								<%}else{%>
								<tr class="even">
									<% } %>
									<td style="width:3%;text-align:center;"><input id="viewIdAdvancedSubSection" type="image"
										src="page_white_text.gif"
										onclick="viewClickedOnElrAdvancedCriteriaViewPage('<%=count%>','elrAdvancedCriteriaTable');return false"
										name="image" align="middle" cellspacing="2" cellpadding="3"
										border="55" class="cursorHand" title="View"></td>
									<td>
									<% String resultedTestName = (String) batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.RESULTEDTEST_NAME);%>
									<%=resultedTestName%></td>
									<td>
									<% 
										String resultOperatorTxt= (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.TEXT_RESULT_CRITERIA_TXT);
										if(resultOperatorTxt == null)
											      resultOperatorTxt= (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.NUMERIC_RESULT_CRITERIA_TXT);
										if(resultOperatorTxt == null)
											resultOperatorTxt="";
									%>
									<%=resultOperatorTxt%></td>
									<td>
									<% 
										String resultNameTxt= (String)batchEnt.getDsmAnswerMap().get(DecisionSupportConstants.RESULT_NAME);
										if(resultNameTxt == null)
											resultNameTxt="";
									%>
									<%=resultNameTxt%>
									</td>
								</tr>
								<%count=count+1; %>
								<% }
				        }
					  %>
					  </tbody>
				</table>

				<table id="elrAdvancedCriteriaTable" width="100%">
					<tr>
						<td class="fieldName" width="100%">Resulted Test:</td>
						<td id="resultedTest" width="100%">&nbsp;</td>
					</tr>
					<tr>
						<td class="fieldName">Coded Result:</td>
						<td id="codedResult">&nbsp;</td>
					</tr>
					<tr>
						<td class="fieldName">Numeric Result:</td>
						<td id="numericResult">&nbsp;</td>
					</tr>
					<tr>
						<td class="fieldName">Text Result:</td>
						<td id="textResult">&nbsp;</td>
					</tr>
					<tr>
						<td class="fieldName">Operator:</td>
						<td id="resultOperatorList">&nbsp;</td>
					</tr>
				</table>
				</td>
	</tr>
</nedss:container>
