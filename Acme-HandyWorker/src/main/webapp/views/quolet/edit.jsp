<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="quolet/customer/edit.do" modelAttribute="quolet">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="finalMode"/>
	<form:hidden path="publicationMoment"/>
	<input type="hidden" name="fixUpTaskId" value="${fixUpTaskId}"/>
	
	
	<form:label path="ticker">
		<spring:message code="quolet.ticker"/>:
	</form:label>
	<form:input path="ticker" readonly="true"/>
	<form:errors cssClass="error" path="ticker"/>
	<br/>
	
	<form:label path="body">
		<spring:message code="quolet.body"/>:
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body"/>
	<br/>
	
		<form:label path="picture">
		<spring:message code="quolet.picture"/>:
	</form:label>
	<form:textarea path="picture"/>
	<form:errors cssClass="error" path="picture"/>
	<br/>
	
	
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="application.save" />" />
	
	<jstl:if test="${quolet.id != 0 && quolet.finalMode==false}">
		<input type="submit" name="delete" value="<spring:message code="quolet.delete"/>" onclick="return confirm('<spring:message code="quolet.confirm.delete"/>')"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="quolet.cancel"/>" onclick="javascript: relativeRedir('quolet/customer/list.do');"/>
	<br />

</form:form>