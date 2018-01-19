<%@ include file="/jsp/tags.jsp" %>
<html>
    <head>
        <title> NBS: Manage Templates </title>
    </head>
     <% String renderDir = request.getAttribute("renderDir").toString();
         String cond = renderDir + "/index.jsp";
         String viewCond = renderDir + "/view/index.jsp";
     %>
     <body>
        <div id="doc3">
             <div id="bd">
                <html:form action="/ManageTemplates.do">
                       
                    <logic:equal name="PageForm" property="actionMode" value="Preview">
                            <jsp:include page="<%=viewCond%>" flush="true"/>
                   </logic:equal>
                        
                </html:form>
       		</div>
       </div>
  </body>
</html>