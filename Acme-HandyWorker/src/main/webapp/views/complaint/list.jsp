<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="complaints" id="row" requestURI="${requestURI}" class="displaytag">
	<display:column>
		<a href="complaint/customer,handyWorker,referee/display.do?complaintId=${row.id}"><spring:message code="complaint.display"/></a>
	</display:column>
	
	<display:column property="ticker" titleKey="complaint.ticker"/>
	
	<spring:message code="complaint.date.format" var="tableDateFormat"/>
	<display:column property="moment" titleKey="complaint.moment" sortable="true"  format="${tableDateFormat}"/>
</display:table>

<jstl:if test="${fixUpTaskId ne null}">
	<security:authorize access="hasRole('CUSTOMER')">
		<a href="complaint/customer/create.do?fixUpTaskId=${fixUpTaskId}"><spring:message code="complaint.create"/></a>
	</security:authorize>
</jstl:if>