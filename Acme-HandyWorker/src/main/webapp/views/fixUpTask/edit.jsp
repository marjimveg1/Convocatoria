<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="fixUpTask/customer/edit.do" modelAttribute="fixUpTask">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="publicationMoment"/>
	<form:hidden path="phases"/>
	<form:hidden path="customer"/>
	<form:hidden path="applications"/>
		<form:hidden path="quolets"/>
	
	
	<form:label path="ticker">
		<spring:message code="fixUpTask.ticker"/>:
	</form:label>
	<form:input path="ticker" readonly="true"/>
	<form:errors cssClass="error" path="ticker"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="fixUpTask.description"/>:
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	<form:label path="address">
		<spring:message code="fixUpTask.address"/>:
	</form:label>
	<form:input path="address"/>
	<form:errors cssClass="error" path="address"/>
	<br/>
	
	<form:label path="maxPrice">
		<spring:message code="fixUpTask.maxPrice"/> (<spring:message code="fixUpTask.vat.info"/>):
	</form:label>
	<form:input path="maxPrice"/>
	<form:errors cssClass="error" path="maxPrice"/>
	<br/>
	
	<form:label path="startDate">
		<spring:message code="fixUpTask.startDate"/>:
	</form:label>
	<form:input path="startDate" placeholder = "dd/mm/yyyy"/>
	<form:errors cssClass="error" path="startDate"/>
	<br/>
	
	<form:label path="endDate">
		<spring:message code="fixUpTask.endDate"/>:
	</form:label>
	<form:input path="endDate" placeholder = "dd/mm/yyyy"/>
	<form:errors cssClass="error" path="endDate"/>
	<br/>
	
	<form:label path="warranty">
		<spring:message code="fixUpTask.warranty"/>:
	</form:label>
	<form:select path="warranty" multiple="false" size="1">
		<form:option label="----" value="0"/>
		<form:options items="${warranties}" itemLabel="title" itemValue="id"/>
	</form:select>
	<form:errors cssClass="error" path="warranty"/>
	<br/>
	
	<form:label path="category">
		<spring:message code="fixUpTask.category"/>:
	</form:label>
	<form:select path="category" multiple="false" size="1">
		<form:option label="----" value="0"/>
		<jstl:forEach var="categoryId" items="${categories.keySet()}">
			<form:option label="${categories.get(categoryId)[0]}" value="${categoryId}"/>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="category"/>
	<br/>		
	
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="fixUpTask.save"/>" />
	<jstl:if test="${fixUpTask.id != 0}">
		<input type="submit" name="delete" value="<spring:message code="fixUpTask.delete"/>" onclick="return confirm('<spring:message code="fixUpTask.confirm.delete"/>')"/>
	</jstl:if>
	<input type="button" name="cancel"	value="<spring:message code="fixUpTask.cancel"/>" onclick="javascript: relativeRedir('fixUpTask/customer/list.do');"/>
	<br />

</form:form>