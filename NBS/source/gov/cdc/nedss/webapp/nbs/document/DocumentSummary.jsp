<%@ page language="java" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<style type="text/css">
	span.label {font-weight:bold; margin-left:0.2em;}
	span.value {margin-right:0.2em;}
</style>
<jsp:useBean id="nbsDocumentForm" scope="session" class="gov.cdc.nedss.webapp.nbs.form.nbsdocument.NbsDocumentForm" />
                
<!-- Page Meta data Block -->
<table class="style">
    <tr class="cellColor">
	<td class="border">
	      <span class="valueTopLine"> ${nbsDocumentForm.attributeMap.FullName}</span>
            <span class="valueTopLine">|</span>
            <span class="valueTopLine">  ${nbsDocumentForm.attributeMap.Sex} </span>
            <span class="valueTopLine">|</span>
            <span class="valueTopLine"> ${nbsDocumentForm.attributeMap.DBO} </span>
        </td>
        <td style="padding:0.15em;width:24%;border-style:solid;border-color:#AFAFAF;text-align:right;}">
            <span class="valueTopLine"> Patient ID: </span>
            <span style="font:16px Arial; margin-left:0.2em;"> ${nbsDocumentForm.attributeMap.PatientId} </span>
            <span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
        </td>
    </tr>
   <tr class="cellColor">
	<td class="border1">
	    <span class="label">Jurisdiction: </span>
	    <span class="value">${nbsDocumentForm.attributeMap.Jurisdiction}</span>
	</td>
	<td class="border3">
	    <span class="label">Program Area: </span>
	    <span class="value">${nbsDocumentForm.attributeMap.ProgramArea}</span>
	</td>
    </tr>
    <tr class="cellColor">
	<td class="border1">
	    <span class="label"> Date Received: </span>
	    <span class="value"> ${nbsDocumentForm.attributeMap.DateReceived}</span>
	</td>
	<td class="border3">
	    <span class="label"> Document ID:</span>
	    <span class="value">  ${nbsDocumentForm.attributeMap.NbsDocumentUid}   </span>
	</td>
    </tr>
</table>
