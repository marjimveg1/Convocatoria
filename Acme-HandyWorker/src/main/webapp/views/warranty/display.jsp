<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:if test="${!warranty.finalMode}">
	<p style="color:blue;"><spring:message code="warranty.info"/></p>
</jstl:if>

<strong><spring:message code="warranty.title"/>:</strong>
<jstl:out value="${warranty.title}"/>
<br/>

<strong><spring:message code="warranty.terms"/>:</strong>
<jstl:out value="${warranty.terms}"/>
<br/>

<strong><spring:message code="warranty.laws"/>:</strong>
<jstl:out value="${warranty.laws}"/>
<br/>
	
	
<!-- Links -->

<br>

<security:authorize access="hasRole('ADMIN')">
	<jstl:set var="back_link" value="warranty/administrator/list.do"/>
</security:authorize>
<security:authorize access="hasAnyRole('CUSTOMER','HANDYWORKER','REFEREE')">
	<jstl:set var="back_link" value="fixUpTask/customer,handyWorker,referee/display.do?fixUpTaskId=${fixUpTaskId}"/>
</security:authorize>

<a href="${back_link}"><spring:message code="warranty.back"/></a>

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${!warranty.finalMode}">
		&nbsp;
		<a href="warranty/administrator/edit.do?warrantyId=${warranty.id}"><spring:message code="warranty.edit.extended"/></a>
		&nbsp;
		<a href="warranty/administrator/makeFinal.do?warrantyId=${warranty.id}" 
		   onclick="return confirm('<spring:message code="warranty.confirm.make.final"/>')"><spring:message code="warranty.make.final"/></a>
	</jstl:if>
</security:authorize>