<%@ include file="/jsp/tags.jsp" %>
<html>
    <head>
        <title> NBS Page Management </title>
    </head>
                    <% String renderDir = request.getAttribute("renderDir").toString();
                        String cond = renderDir + "/index.jsp";
                        String viewCond = renderDir + "/view/index.jsp";
                    %>
        <div id="doc3">
             <div id="bd">
                    <html:form action="/ManagePage.do">
                       
                    <logic:equal name="PageForm" property="actionMode" value="Preview">
                            <jsp:include page="<%=viewCond%>" flush="true"/>
                   </logic:equal>
                        
                </html:form>
        <div id="bd">
       </div>
       </div>
  </body>
</html>