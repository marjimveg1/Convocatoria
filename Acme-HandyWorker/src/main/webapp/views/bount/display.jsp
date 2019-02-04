<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:if test="${!bount.finalMode}">
	<p style="color:blue;"><spring:message code="bount.info"/></p>
</jstl:if>

<strong><spring:message code="bount.ticker"/>:</strong>
<jstl:out value="${bount.ticker}"/>
<br/>


<strong><spring:message code="bount.publicationMoment"/>:</strong>
<spring:message code="bount.formatMoment1" var="formatMoment"/>
<fmt:formatDate value="${bount.publicationMoment}" pattern="${formatMoment}"/>
<br/>

<strong><spring:message code="bount.body"/>:</strong>
<jstl:out value="${bount.body}"/>
<br/>
	

	
	
<strong><spring:message code="bount.finalMode"/>:</strong>
<jstl:out value="${bount.finalMode}"/>
<br/>


<strong> <spring:message code="bount.picture" />: </strong>

<img src="${bount.picture}"  width="300" height="400">
<br />

<!-- Links -->

<br>

<security:authorize access="hasRole('CUSTOMER')">
	<jstl:set var="back_link" value="bount/customer/list.do"/>
</security:authorize>
<security:authorize access="hasAnyRole('HANDYWORKER')">
	<jstl:set var="back_link" value="fixUpTask/handyWorker/listAll.do"/>
</security:authorize>

<a href="${back_link}"><spring:message code="bount.back"/></a>
