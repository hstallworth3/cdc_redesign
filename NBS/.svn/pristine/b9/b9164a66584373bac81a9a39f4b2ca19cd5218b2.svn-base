<%@ include file="/jsp/tags.jsp" %>
<%@ page import="java.util.*" %>      
<%@ page import="gov.cdc.nedss.entity.person.dt.*" %>      

<html>
    <title>Provider Search Results</title>
	<head>
	<base target="_self">
		<%@ include file="/jsp/resources.jsp" %>
		<script type='text/javascript' src='/nbs/dwr/interface/JPamForm.js'></script>		
		<SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JCTContactForm.js"></SCRIPT>	
		<SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JPageForm.js"></SCRIPT>
		<SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JDecisionSupport.js"></SCRIPT>
		<SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JBaseForm.js"></SCRIPT>
		
 	    <SCRIPT LANGUAGE="JavaScript">
			function populateInvestigator(uid, identifier)
			{
				 var opener = getDialogArgument();
	
                 var pview = getElementByIdOrByNameNode("pamview", opener.document);
                 
                 if(identifier == "INV180"){
                	 pview = getElementByIdOrByNameNode("pageview", opener.document); 
                 }
			
                 //var parent = window.opener;
				var parent = opener;
				var parentDoc = parent.document;					
			  	dwr.util.setValue(identifier, "");

		  	if(identifier == "INV207" || identifier == "INV225"  || identifier == "INV247"){
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
				else
			  	if(identifier == "INV208" || identifier == "INV209" || identifier == "INV210"){
			     JBaseForm.getDwrInvestigatorDetailsByUid(uid, identifier,function(data) {
				   	dwr.util.setEscapeHtml(false);
				   	dwr.util.setValue(parentDoc.getElementById("pamClientVO.answer(INV207)"), "");
				   	dwr.util.setValue(parentDoc.getElementById(identifier), data);
				   	dwr.util.setValue(parentDoc.getElementById("investigator.personUid"), uid);
				   	parentDoc.getElementById(identifier+"Text").style.visibility="hidden";
					parentDoc.getElementById(identifier+"Icon").style.visibility="hidden";
					parentDoc.getElementById(identifier+"CodeLookupButton").style.visibility="hidden";
					parentDoc.getElementById("clear"+identifier).className="";
					parentDoc.getElementById(identifier+"SearchControls").className="none";
					parentDoc.getElementsByName("attributeMap."+identifier+"Uid")[0].value=uid;
					
			   	self.close();
			    });
			  	}
			  	else if(identifier == "CON137" || identifier == "CONINV180" ){

			  		JCTContactForm.getDwrInvestigatorDetailsByUid(uid, identifier,function(data) {
					   	dwr.util.setEscapeHtml(false);
					   	dwr.util.setValue(parentDoc.getElementById("cTContactClientVO.answer(" +identifier + ")"), "");
					   	dwr.util.setValue(parentDoc.getElementById(identifier), data);
					   	dwr.util.setValue(parentDoc.getElementById("investigator.personUid"), uid);
					   	parentDoc.getElementById(identifier+"Text").style.visibility="hidden";
						parentDoc.getElementById(identifier+"Icon").style.visibility="hidden";
						parentDoc.getElementById(identifier+"CodeLookupButton").style.visibility="hidden";
						parentDoc.getElementById("clear"+identifier).className="";
						parentDoc.getElementById(identifier+"SearchControls").className="none";
						
						// show the date assigned to investigator field
						if (identifier == "CON137") {
	                        parent.enabledDateAssignedToInvestigation();
	                    }

				   	self.close();
				    });
				// }else if(identifier == "INV180" || identifier == "INV181" || identifier == "INV182")
			  	}
			  	else if(identifier == "PartPer"){
			  		JDecisionSupport.getDwrInvestigatorDetailsByUid(uid, identifier,function(data) {
					   	dwr.util.setEscapeHtml(false);
					     $j("#PartPerSearchControls", parent.document.body).children().hide();   
					     dwr.util.setValue(parentDoc.getElementById(identifier), data);
					     dwr.util.setValue(parentDoc.getElementById(identifier+"Uid"), uid);
						// parentDoc.getElementById(identifier+"Text").style.visibility="hidden";
						// parentDoc.getElementById(identifier+"Icon").style.visibility="hidden";
					 	    $j("#PartPerS",parent.document.body).children().show();
					        //getElementByIdOrByName(identifier+"Text").style.visibility="hidden";
					        //getElementByIdOrByName(identifier+"Icon").style.visibility="hidden";
					        //getElementByIdOrByName(identifier+"CodeLookupButton").style.visibility="hidden";
					        //getElementByIdOrByName("clear"+identifier).className="";
					    
					        //getElementByIdOrByName(identifier+"SearchControls").className="none";
					     $j("#PartPerCodeClearButton",parent.document.body).val("Clear/Reassign");
					     $j("#PartPerCodeClearButton",parent.document.body).css("color", "#000");
					     $j("#PartPerCodeClearButton",parent.document.body).attr("disabled", false);
					     $j("#clearPartPer",parent.document.body).children().show();  
						  // parentDoc.getElementById("PartPer").style.display = "";	
						  // parentDoc.getElementById("PartPerText").style.visibility="hidden";
						  // parentDoc.getElementById("PartPerIcon").style.visibility="hidden";
						  // parentDoc.getElementById("PartPerCodeLookupButton").style.visibility="hidden";
						  // parentDoc.getElementById("clearPartPer").style.display = "";
						  // parentDoc.getElementById("clearPartPer").value="Clear/Reassign";
						  //getElementByIdOrByName("selectedPartPer").className="none";
				        	  //getElementByIdOrByName("PartPerSearchControls").className="none";
						  //getElementByIdOrByName("PartPerS").className="none"; //white line						 
						 			     
					     //$j("#PartPerCodeClearButton").val("Clear/Reassign");
					     //$j("#PartPerCodeClearButton").css("color", "#000");
					     //$j("#PartPerCodeClearButton").attr("disabled", false);
					     ///$j("#clearPartPer").children().show();   
				   	self.close();
				    });	
			  	}
				else 
			  	{
			  		JPageForm.getDwrInvestigatorDetailsByUid(uid, identifier,function(data) {
					   	dwr.util.setEscapeHtml(false);
					   	dwr.util.setValue(parentDoc.getElementById("pageClientVO.answer(" +identifier + ")"), "");
					   	dwr.util.setValue(parentDoc.getElementById(identifier), data);
					   	dwr.util.setValue(parentDoc.getElementById("investigator.personUid"), uid);
					   	parentDoc.getElementById(identifier+"Text").style.visibility="hidden";
						parentDoc.getElementById(identifier+"Icon").style.visibility="hidden";
						parentDoc.getElementById(identifier+"CodeLookupButton").style.visibility="hidden";
						parentDoc.getElementById("clear"+identifier).className="";
						parentDoc.getElementById(identifier+"SearchControls").className="none";
						//store UID in hidden field if present
						if (parentDoc.getElementById("attributeMap."+identifier+"Uid") != null) {
							  parentDoc.getElementById("attributeMap."+identifier+"Uid").value = uid;
							  if (identifier == "NBS139" || identifier == "NBS145" || identifier == "NBS161" || identifier == "NBS186" || identifier == "NBS197") {
							  	parent.stdUpdateCurrentProvider(identifier);
							  }
	        				}
						// show the date assigned to investigator field
						if (identifier == "INV180" ) {
	                        		parent.enabledDateAssignedToInvestigation();
	                        		}
	                        		if (identifier == "NBS186") {  //Inteviewer
	                        			parent.pgRequireElement("NBS187"); //Date
	                    			}
	         
				   	self.close();
				    });	
			  	}
			  	if (pview != null) {
	                pview.style.display = "none";                  
	                            }

			}
					
	  		function createProvider(id){
	  			document.forms[0].action ="/nbs/AddProvider.do?method=addProvider&identifier="+id;
	  			document.forms[0].submit();
	  		}
	  		function closePopup()
            {              
                    self.close();
                    var opener = getDialogArgument();          
                    var invest = getElementByIdOrByNameNode("pamview", opener.document);
                    if (invest != null) {
                         invest.style.display = "none";  
                    }                   
                
            }   
			
		</SCRIPT>		
	</head>
	<body class="popup" style="overflow-x:hidden">
        <!-- Page title -->
        <div class="popupTitle">
            Provider Search Results
        </div>
        
        <!-- Top button bar -->
        <%String queId = (String) request.getAttribute("identifier");%>
		
        <div class="popupButtonBar">
  
            <input type="button" style="<%=request.getAttribute("addButton")%>" name="Add" value="Add Provider" id="Add" onclick="return createProvider('<%=queId%>')"/>


            <input type="button"  name="Cancel" value="Cancel" id="Cancel" onclick="closePopup();"/>
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
	                               <display:table name="providersList" class="dtTable" pagesize="20"  id="parent" requestURI="">
	                                  <display:column property="actionLink" title="" class="dstag"/> 
	                                  <display:column property="fullName" title="Full Name" class="dstag"/>
	                                  <display:column property="address" title="Address" class="dstag"/>
	                                  <display:column property="telephone" title="Telephone" class="dstag"/>
	                                  <display:column property="id" title="ID" class="dstag"/>
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
	        <input type="button"  name="Add" style="<%=request.getAttribute("addButton")%>" value="Add Provider" id="Add" onclick="return createProvider('<%=queId%>')"/>
	        <input type="button"  name="Cancel" value="Cancel" id="Cancel" onclick="closePopup();"/>
	    </div>
    </body>
</html>