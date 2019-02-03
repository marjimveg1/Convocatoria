<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="complaint/customer/edit.do" modelAttribute="complaint">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	<form:hidden path="report"/>
	<form:hidden path="fixUpTask"/>
	
	<form:label path="ticker">
		<spring:message code="complaint.ticker"/>:
	</form:label>
	<form:input path="ticker" readonly="true"/>
	<form:errors cssClass="error" path="ticker"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="complaint.description"/>:
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	<form:label path="attachments">
		<spring:message code="complaint.attachments"/>:
	</form:label>
	<form:textarea path="attachments"/>
	<form:errors cssClass="error" path="attachments"/>
	<br/>
	
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="complaint.save"/>" onclick="return confirm('<spring:message code="complaint.confirm.save"/>')"/>
	<input type="button" name="cancel"	value="<spring:message code="complaint.cancel"/>" onclick="javascript: relativeRedir('complaint/customer,handyWorker,referee/list.do?fixUpTaskId=${complaint.fixUpTask.id}');"/>
	<br />

</form:form>