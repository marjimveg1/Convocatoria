<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="phase/handyWorker/edit.do" modelAttribute="phase">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:label path="title">
		<spring:message code="phase.title"/>:
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="phase.description"/>:
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	<form:label path="startMoment">
		<spring:message code="phase.startMoment"/>:
	</form:label>
	<form:input path="startMoment" placeholder = "dd/mm/yyyy hh:mm"/>
	<form:errors cssClass="error" path="startMoment"/>
	<br/>
	
	<form:label path="endMoment">
		<spring:message code="phase.endMoment"/>:
	</form:label>
	<form:input path="endMoment" placeholder = "dd/mm/yyyy hh:mm"/>
	<form:errors cssClass="error" path="endMoment"/>
	<br/>
	
	<input type="hidden" name="fixUpTaskId" value="${fixUpTaskId}"/>
	
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="phase.save"/>"/>
	
	<jstl:if test="${phase.id != 0}">
		<input type="submit" name="delete" value="<spring:message code="phase.delete"/>" onclick="return confirm('<spring:message code="phase.confirm.delete"/>')"/>
		<jstl:set var="back_redir" value="phase/customer,handyWorker,referee/back.do?phaseId=${phase.id}"/>
	</jstl:if>
	
	<jstl:if test="${phase.id == 0}">
		<jstl:set var="back_redir" value="phase/customer,handyWorker,referee/list.do?fixUpTaskId=${fixUpTaskId}"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="phase.cancel"/>" onclick="javascript: relativeRedir('${back_redir}');"/>
	<br />

</form:form>