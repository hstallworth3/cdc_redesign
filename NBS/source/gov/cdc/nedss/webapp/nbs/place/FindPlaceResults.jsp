<%@ include file="/jsp/tags.jsp" %>
<%@ page import="java.util.*" %>
<%@ page isELIgnored="false"%>                     

<style>
.container{

	overflow-x: hidden;
	
}

</style>
<html>
    
    <title>Place Search Results</title>
    <head> 
        <base target="_self">
        <%@ include file="/jsp/resources.jsp" %>
        <script>
           var refineSearch = <%=request.getAttribute("RefineSearch")%>;
           <logic:notEqual value="GlobalPlace" parameter="ContextAction" scope="request"> 
              var identifier  = "<%=request.getParameter("identifier")%>"; 
           </logic:notEqual>
           
        </script>
        <script language="javascript" src="place.js"></script>
        <SCRIPT Language="JavaScript" Src="/nbs/dwr/interface/JBaseForm.js"></SCRIPT>
    </head>
    <body class="container">    
      
          <logic:equal value="GlobalPlace" name="placeForm" property="contextAction" > 
             <%@ include file="/jsp/topNavFullScreenWidth.jsp" %>
        </logic:equal>
      
       <div style="width: 100%; text-align: right; margin: 4px 0px 4px 0px;">
            <a href="javascript:showSearch()">New Search</a>&nbsp;|&nbsp; 
            <a href="javascript:showRefineSearch()">Refine Search</a>
        </div> 
        
        <logic:equal value="GlobalPlace" name="placeForm" property="contextAction" > 
	        <div class="grayButtonBar">
	            <input type="button"  name="Add" value="Add Place" id="Add" onclick="showAddPlace()"/>&nbsp;&nbsp; 
	        </div> 
        </logic:equal>
         
        <div style="width: 100%; text-align: center;">
            <div style="margin-left: 2px; margin-right: 2px; width: 100%">
                <form method="post" name="placeForm" action="/nbs/Place.do">
                    <input name="method" type="hidden" value="find" />
                    <input name="identifier" type="hidden" value='<%=request.getParameter("identifier") %>'/>
                   
                    <div class="infoBox messages">
                        Your Search Criteria: <i> <%=request.getAttribute("SearchCriteria")%>
                        </i> resulted in <b> <%=request.getAttribute("ResultsCount")%>
                        </b> possible matches.
                    </div>
    
                    <table width="100%"  style="align: center">
                      <tr>
                        <td style="align:center; left-margin: 2px; right-margin: 2px">
                          <display:table name="placeList" class="dtTable" pagesize="20" id="parent" requestURI="">
                            <display:column  style="width:2%; text-align:center;" media="html"> 
                           <!-- <a href="javascript:viewPlace(${parent.placeUid})">View</a>-->
                                 <logic:equal value="GlobalPlace" name="placeForm" property="contextAction" > 
                                    &nbsp;&nbsp;<a href="javascript:viewPlace(${parent.placeUid})">View</a>&nbsp;&nbsp; 
                                 </logic:equal > 
                                <logic:notEqual value="GlobalPlace" name="placeForm" property="contextAction" > 
                                    &nbsp;&nbsp;<a href='javascript:selectPlaceByUid("${parent.placeUid}", "<%=request.getParameter("identifier") %>")'>Select</a>&nbsp;&nbsp; 
                                 </logic:notEqual> 
                            </display:column>
                            <display:column property="typeName" title="Name" class="dstag" />
                            <display:column property="address" title="Address" class="dstag" />
                            <display:column property="telephone" title="Telephone" class="dstag" />
                            <display:column property="quickEntryCode" title="Quick Code" class="dstag" />
                            <display:setProperty name="basic.empty.showtable" value="true" />
                          </display:table>
                        </td>
                      </tr>
                    </table>
                </form>
            </div>
        </div>
        <logic:equal value="GlobalPlace" name="placeForm" property="contextAction" > 
	        <div class="grayButtonBar">
	            <input type="button"  name="Add" value="Add Place" id="Add1" onclick="showAddPlace()"/>&nbsp;&nbsp; 
	        </div>
        </logic:equal>
    </body>
</html>