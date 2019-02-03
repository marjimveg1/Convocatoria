<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<spring:message code="phase.date.format2" var="dateFormat"/>


<strong><spring:message code="phase.title"/>:</strong>
<jstl:out value="${phase.title}"/>
<br/>

<strong><spring:message code="phase.description"/>:</strong>
<jstl:out value="${phase.description}"/>
<br/>

<strong><spring:message code="phase.startMoment"/>:</strong>
<fmt:formatDate value="${phase.startMoment}" pattern="${dateFormat}"/>
<br/>

<strong><spring:message code="phase.endMoment"/>:</strong>
<fmt:formatDate value="${phase.endMoment}" pattern="${dateFormat}"/>
<br/>
	
	
<!-- Links -->

<br>

<a href="phase/customer,handyWorker,referee/back.do?phaseId=${phase.id}"><spring:message code="phase.back"/></a>

<jstl:if test="${phaseEditionPerm}">
	&nbsp;
	<a href="phase/handyWorker/edit.do?phaseId=${phase.id}"><spring:message code="phase.edit.extended"/></a>
</jstl:if>