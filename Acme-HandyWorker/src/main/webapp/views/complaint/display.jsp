<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<fieldset>
	<legend><spring:message code="complaint.attributes"/></legend>

	<strong><spring:message code="complaint.ticker"/>:</strong>
	<jstl:out value="${complaint.ticker}"/>
	<br/>
	
	<spring:message code="complaint.date.format2" var="dateFormat"/>
	<strong><spring:message code="complaint.moment"/>:</strong>
	<fmt:formatDate value="${complaint.moment}" pattern="${dateFormat}"/>
	<br/>
	
	<strong><spring:message code="complaint.description"/>:</strong>
	<jstl:out value="${complaint.description}"/>
	<br/>
	
	<jstl:if test="${not empty attachments}">
		<strong><spring:message code="complaint.attachments"/>:</strong>
		<br>
		<ul>
			<jstl:forEach var="attachment" items="${attachments}">
				<li> <a href="${attachment}"><jstl:out value="${attachment}"/></a> </li>
			</jstl:forEach>
		</ul>
	</jstl:if>
</fieldset>

<fieldset>
	<legend><spring:message code="complaint.references"/></legend>

	<jstl:if test="${complaint.report ne null && (complaint.report.finalMode || reportCreationPerm)}">
		<strong><spring:message code="complaint.report"/>:</strong>
		<a href="report/customer,handyWorker,referee/display.do?reportId=${complaint.report.id}"><spring:message code="complaint.report.display"/></a>
		<br/>
	</jstl:if>
	
	<strong><spring:message code="complaint.fixUpTask"/>:</strong>
	<a href="fixUpTask/customer,handyWorker,referee/display.do?fixUpTaskId=${complaint.fixUpTask.id}"><jstl:out value="${complaint.fixUpTask.ticker}"/></a>
	<br/>
</fieldset>
	
	
<!-- Links -->
<br>

<security:authorize access="hasRole('REFEREE')">
	<a href="complaint/referee/listSelfAssigned.do"><spring:message code="complaint.back"/></a>
	&nbsp;
	<jstl:if test="${not isAssigned}">
		<a href="complaint/referee/selfAssign.do?complaintId=${complaint.id}"><spring:message code="complaint.self.assign"/></a>
		&nbsp;
	</jstl:if>
	<jstl:if test="${reportCreationPerm && complaint.report == null}">
		<a href="report/referee/create.do?complaintId=${complaint.id}"><spring:message code="complaint.create.report"/></a>
		&nbsp;
	</jstl:if>
</security:authorize>

<security:authorize access="hasAnyRole('CUSTOMER','HANDYWORKER')">
	<a href="complaint/customer,handyWorker,referee/list.do?fixUpTaskId=${complaint.fixUpTask.id}"><spring:message code="complaint.back"/></a>
</security:authorize>