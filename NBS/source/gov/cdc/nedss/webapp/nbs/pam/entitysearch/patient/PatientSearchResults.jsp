<%@ include file="/jsp/tags.jsp" %>
<%@ page import="java.util.*" %>      
<%@ page import="gov.cdc.nedss.entity.person.dt.*" %>      

<html>
    <title>Patient Search Results</title>
	<head>
		<%@ include file="/jsp/resources.jsp" %>
		<script type='text/javascript' src='/nbs/dwr/interface/JPamForm.js'></script>		
 	    <SCRIPT LANGUAGE="JavaScript">
			function populateInvestigator(uid, identifier)
			{
				var parent = window.opener;
				var parentDoc = parent.document;					
			  	dwr.util.setValue(identifier, "");
			     JPamForm.getDwrInvestigatorDetailsByUid(uid, identifier,function(data) {
				   	dwr.util.setEscapeHtml(false);
				   	dwr.util.setValue(parentDoc.getElementById("pamClientVO.answer(INV207)"), "");
				   	dwr.util.setValue(parentDoc.getElementById(identifier), data);
				   	dwr.util.setValue(parentDoc.getElementById("investigator.personUid"), uid);
				   	parentDoc.getElementById(identifier+"Text").style.visibility="hidden";
					parentDoc.getElementById(identifier+"Icon").style.visibility="hidden";
					parentDoc.getElementById(identifier+"CodeLookupButton").style.visibility="hidden";
					parentDoc.getElementById("clear"+identifier).className="";
					parentDoc.getElementById(identifier+"SearchControls").className="none";
					
					// show the date assigned to investigator field
					if (identifier == "INV207") {
                        parent.enabledDateAssignedToInvestigation();
                    }
			   	self.close();
			    });
			}
					
	  		function createProvider(id){
	  			document.forms[0].action ="/nbs/AddProvider.do?method=addProvider&identifier="+id;
	  			document.forms[0].submit();
	  		}
			
		</SCRIPT>		
	</head>
	<body class="popup">
        <!-- Page title -->
        <div class="popupTitle">
            Patient Search Results
        </div>
        
        <!-- Top button bar -->
        <%String queId = (String) request.getAttribute("identifier");%>
        <div class="popupButtonBar">
            <input type="button"  name="Cancel" value="Cancel" id="Cancel" onclick="self.close();"/>
        </div>
        
        <!-- Results block -->
        <div style="width:100%; text-align:center;">
            <div style="width:98%;">
	            <form method="post" id="nedssForm" action="">
	                <nedss:container id="section1" name="Search Results" classType="sect" 
	                        displayImg ="false" displayLink="false" includeBackToTopLink="no">
	                        
	                    <div style="width:100%; text-align:right;margin:4px 0px 4px 0px;">
	                        <a href="<%=request.getAttribute("NewSearchLink")%>">New Search</a>&nbsp;|&nbsp;
	                        <a href="<%=request.getAttribute("RefineSearchLink")%>">Refine Search</a>
	                    </div>
	                    
	                    <div class="infoBox messages">
	                        Your Search Criteria: <i> <%=request.getAttribute("SearchCriteria")%> </i>
	                        resulted in <b> <%=request.getAttribute("ResultsCount")%> </b> possible matches.
	                    </div>
	                    
	                    <table width="100%" border="0" cellspacing="0">
	                        <tr>
	                           <td align="center">              
	                               <display:table name="patientsList" class="dtTable" pagesize="20"  id="parent" requestURI="">
	                                  <display:column property="actionLink" title="" class="dstag"/> 
	                                  <display:column property="fullName" title="Full Name" class="dstag"/>
	                                  <display:column property="ageSexDOB" title="Age/DOB/Sex" class="dstag"/>
	                                  <display:column property="address" title="Address" class="dstag"/>
	                                  <display:column property="telephone" title="Telephone" class="dstag"/>
	                                  <display:column property="conditions" title="Conditions" class="dstag"/>
	                                  <display:setProperty name="basic.empty.showtable" value="true"/>
	                                </display:table> 
	                            </td>
	                        </tr>
	                    </table>
	                </nedss:container>
	            </form>
	        </div>    
        </div>
        
        <!-- Bottom button bar -->
	    <div class="popupButtonBar">
	        <input type="button"  name="Cancel" value="Cancel" id="Cancel" onclick="self.close();"/>
	    </div>
    </body>
</html>