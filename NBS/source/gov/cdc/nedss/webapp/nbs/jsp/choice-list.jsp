<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

    <dl style="margin-left:4px; padding-left:0px;">
      <c:forEach items="${list}" var="dropDownCodeDT">
        <dt style="margin-left:0px; font: normal 12px arial;"><c:out value="${dropDownCodeDT.value}"/></dt>
      </c:forEach>
    </dl>