<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:if test="${!quolet.finalMode}">
	<p style="color:blue;"><spring:message code="quolet.info"/></p>
</jstl:if>

<strong><spring:message code="quolet.ticker"/>:</strong>
<jstl:out value="${quolet.ticker}"/>
<br/>


<strong><spring:message code="quolet.publicationMoment"/>:</strong>
<spring:message code="quolet.formatMoment1" var="formatMoment"/>
<fmt:formatDate value="${quolet.publicationMoment}" pattern="${formatMoment}"/>
<br/>

<strong><spring:message code="quolet.body"/>:</strong>
<jstl:out value="${quolet.body}"/>
<br/>
	

	
	
<strong><spring:message code="quolet.finalMode"/>:</strong>
<jstl:out value="${quolet.finalMode}"/>
<br/>


<strong> <spring:message code="quolet.picture" />: </strong>

<img src="${quolet.picture}"  width="300" height="400">
<br />

<!-- Links -->

<br>

<security:authorize access="hasRole('CUSTOMER')">
	<jstl:set var="back_link" value="quolet/customer/list.do"/>
</security:authorize>
<security:authorize access="hasAnyRole('HANDYWORKER')">
	<jstl:set var="back_link" value="fixUpTask/handyWorker/listAll.do"/>
</security:authorize>

<a href="${back_link}"><spring:message code="quolet.back"/></a>
