<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="bount/customer/edit.do" modelAttribute="bount">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="finalMode"/>
	<form:hidden path="publicationMoment"/>
	<input type="hidden" name="fixUpTaskId" value="${fixUpTaskId}"/>
	
	
	<form:label path="ticker">
		<spring:message code="bount.ticker"/>:
	</form:label>
	<form:input path="ticker" readonly="true"/>
	<form:errors cssClass="error" path="ticker"/>
	<br/>
	
	<form:label path="body">
		<spring:message code="bount.body"/>:
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body"/>
	<br/>
	
		<form:label path="picture">
		<spring:message code="bount.picture"/>:
	</form:label>
	<form:textarea path="picture"/>
	<form:errors cssClass="error" path="picture"/>
	<br/>
	
	
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="application.save" />" />
	
	<jstl:if test="${bount.id != 0 && bount.finalMode==false}">
		<input type="submit" name="delete" value="<spring:message code="bount.delete"/>" onclick="return confirm('<spring:message code="bount.confirm.delete"/>')"/>
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="bount.cancel"/>" onclick="javascript: relativeRedir('bount/customer/list.do');"/>
	<br />

</form:form>