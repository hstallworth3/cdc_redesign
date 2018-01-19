<%@ include file="/jsp/tags.jsp" %>
<html>
    <head>
    <base target="_self">
    <title>Search for Test</title>
        <%@ include file="/jsp/resources.jsp" %>	  
        <script language="JavaScript">  

        
		document.onkeypress=function(e){
			if(e.keyCode==13){
				findResultedTest();
				return false;
    		}
	}
		
        
            function findResultedTest()
            {
                var index = 0;
                var errors = new Array();
                var isError = false;

                var labTest  = getElementByIdOrByName("labTest").value;
                var searchList  = getElementByIdOrByName("searchList").value;

                if(labTest.length == 0 || searchList.length == 0)
                {
                    errors[index++] = "Please enter item to search on and try again.";
                    isError = true;
                }					
                
                if(isError) {
                    $j("#zeroResultsMsg").hide();
                    displayGlobalErrorMessage(errors);
                    return false;   
                }
                
                blockUiDuringFormSubmission();
                document.forms[0].action ="/nbs/LabResultedTestLink.do?method=labTestSearchSubmit";
				document.forms[0].submit();				
            }

            function blockUiDuringFormSubmission()
    		{
    		    var saveMsg = '<div class="submissionStatusMsg"> <div class="header"> Loading Search Results </div>' +  
    		        '<div class="body"> <img src="saving_data.gif" alt=""/> Your search results are being loaded. Please wait ... </div> </div>';         
    			$j.blockUI({  
    			    message: saveMsg,  
    			    css: {  
    			        top:  ($j(window).height() - 500) /2 + 'px', 
    			        left: ($j(window).width() - 500) /2 + 'px', 
    			        width: '500px'
    			    }  
    			});
    		}
            
            function closePopup()
            {
                self.close();
                if(opener!=null){
	                var block= getElementByIdOrByNameNode("blockparent", opener.document)
	                block.style.display = "none";
                }
            }
            
            function bonload()
            {
                autocompTxtValuesForJSP();
                getElementByIdOrByName('labTest').focus();											
            }
            
		</script>
	</head>
    <body class="popup" onload="bonload();">
    <table id="container" height="500" width="760" >
    <tr>
    <td valign="top">
    	<div class="popupTitle">
            Search for Test
        </div>
        
        <div class="popupButtonBar">
            <input type="button" id="Submit" name="aSubmit" value="Submit" onclick="return findResultedTest();"/>
			<input type="button" name="Cancel" value="Cancel" onclick="return closePopup();" />
        </div>
        
        <%@ include file="../../../jsp/feedbackMessagesBar.jsp" %>
        <%@ include file="../../../jsp/errors.jsp" %>
        
        <!-- 0 search results message -->
        <logic:notEmpty name="labResultedTestForm" property="attributeMap.NORESULT">
            <table style="width:100%;">
                <tr>
                    <td>
                        <div class="infoBox messages" id="zeroResultsMsg">
                            Your Search Criteria resulted in 0 possible matches.
                            Please refine your search and try again.
                        </div>
                    </td>
                </tr>
            </table>
        </logic:notEmpty>
        
        <html:form action="/LabTestLoincLink.do">
       	<nedss:container id="subsec0" classType="subSect" displayImg="false">
        	<tr>
	        	<td class="fieldName" id="testName"><span title="Please enter all or part of a test name or the code associated with the test">Resulted Test:</span></td>
	            <td>				 
	            	<html:text styleId="labTest" name="labResultedTestForm" property="searchCriteria(LABTEST)" size="25" maxlength="100" title="Please enter all or part of a test name or the code associated with the test."/>
	            </td>
	        </tr>             
	        <tr>
				<td class="fieldName" id="queueAppLabel" valign="top">
					<span>Search:</span>
				</td>
				<td> 
					<input type="hidden" name="approval1" value="N">
					<table>
						<tr>
						<td>
							<html:radio name="labResultedTestForm" property="searchCriteria(SEARCHLIST)" styleId="searchList" value="LOCAL" >Short list, includes local tests</html:radio>
						</td>
						</tr>
						<tr>
						<td>
							<html:radio name="labResultedTestForm" property="searchCriteria(SEARCHLIST)" styleId="searchList" value="LOINC" >Long list, includes standard (LOINC) tests</html:radio>
						</td>
						</tr>
						<tr>
						<!--<tr>
						<td>
							<html:radio name="labResultedTestForm" property="searchCriteria(SEARCHLIST)" styleId="searchListRadio3" value="LOCAL" >Condition-specific, includes tests that have been mapped to the selected condition</html:radio>
						</td>
						</tr>-->
					</table> 
				</td>
			</tr>
		</nedss:container>			  	
        </html:form>
    </td>
    </tr> 
    <tr>
    <td valign="bottom"> 
        <div class="popupButtonBar">
	        <input type="button" id="Submit" name="Submit" value="Submit" onclick="return findResultedTest();"/>
	        <input type="button" name="Cancel" value="Cancel" onclick="return closePopup();" />
	    </div>
	</td>
    </tr> 
    </table>
    </body>
</html>