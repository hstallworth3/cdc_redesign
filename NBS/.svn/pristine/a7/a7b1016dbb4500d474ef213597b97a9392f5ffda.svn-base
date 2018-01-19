<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" %>
<%@ page import="gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj" %>
<%@ page isELIgnored ="false" %>
<tr valign="top">
  <td><table align="left" cellpadding="2" cellspacing="0" bgcolor="#003470" border="0">
      <tbody>
        <tr>
          <td colspan="16" style="HEIGHT: 2px;"></td>
        </tr>
        <tr>
          <td style="WIDTH: 2px;"></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/LoadMyTaskList1.do?ContextAction=Initialize"><font class="boldTenWhite">Home</font></html:link></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/LoadNavbar.do?ContextAction=DataEntry"><font class="boldTenWhite">Data Entry</font></html:link></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/LoadNavbar1.do?ContextAction=MergePerson"><font class="boldTenWhite">Merge Patients</font></html:link></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/LoadNavbar.do?ContextAction=GlobalInvestigations"><font class="boldTenWhite">Investigations</font></html:link></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/nfc?ObjectType=7&amp;OperationType=116"><font class="boldTenWhite">Reports</font></html:link></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/SystemAdmin.do"><font class="boldTenWhite">System Management</font></html:link></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><a href="/nbs/UserGuide.do?method=open" target="_blank"> <font class="boldTenWhite">Help</font></a></td>
          <td><font class="boldTenWhite">&nbsp;|&nbsp;</font></td>
          <td align="left"><html:link styleClass="navbar"  href ="/nbs/logout"><font class="boldTenWhite">Logout</font></html:link></td>
          <td style="WIDTH: 175px;"></td>
        </tr>
        <tr>
          <td colspan="16" style="HEIGHT: 2px;"></td>
        </tr>
      </tbody>
    </table></td>
</tr>
<tr valign="top">
  <td><table width="750" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td width="750" height="15" bgcolor="#003470" colspan="4"></td>
        <td rowspan="2" height="35" width="84" align="left" valign="top"><img style="background: #DCDCDC" title="Logo" border="0" height="32" 		width="80" 		src="../../images/nedssLogo.jpg"></td>
      </tr>
      <tr>
        <td class="boldTwelveBlack" bgcolor="#DCDCDC" style="WIDTH: 2px;"></td>
        <logic:present name="BaseForm">
	        <td class="boldTwelveBlack" bgcolor="#DCDCDC">${BaseForm.pageTitle}</td>
        </logic:present>	        
        <logic:notPresent name="BaseForm">
   		     <td class="boldTwelveBlack" bgcolor="#DCDCDC"><%= request.getAttribute("PageTitle") %></td>
        </logic:notPresent>
        <%
        	String fullName = "";
				try {
						NBSSecurityObj so = (NBSSecurityObj) request.getSession().getAttribute("NBSSecurityObject");
						

						if (so != null) {
							
							 fullName = so.getTheUserProfile().getTheUser().getFirstName() + " " + so.getTheUserProfile().getTheUser().getLastName();
							 
						}   
						
				}
			
				catch (Exception e) {
					
				}        	
        %>
        <td class="boldTenWhite" bgcolor="#003470" align="right">User: <%= fullName %></td>
        <td class="boldTwelveBlack" bgcolor="#003470" width="3"></td>
      </tr>
      <tr>
        <td colspan="5"><img alt="" border="0" height="9" width="750" src="dropshadow.gif"></td>
      </tr>
    </table></td>
</tr>
