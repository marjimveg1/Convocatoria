<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="category/administrator/edit.do" modelAttribute="categoryForm">
	<form:hidden path="id" />
	
	<fieldset>
		<legend> <spring:message code="category" /> </legend>
		
		<form:label path="parent">
			<spring:message code="category.parent" />:
		</form:label>
		<form:select path="parent">
			<!--<form:option label="----" value="0" />-->
			<jstl:forEach var="categoryId" items="${parents.keySet()}">
				<form:option label="${parents.get(categoryId)[0]}" value="${categoryId}"/>
			</jstl:forEach>
		</form:select>
		<form:errors cssClass="error" path="parent" />
		<br/>
	</fieldset>

	<fieldset>
		<legend> <spring:message code="categoriesTranslation" /> </legend>
		
		<form:label path="en_name">
			<spring:message code="english_name" />:
		</form:label>
		<form:input path="en_name"/>
		<form:errors cssClass="error" path="en_name" />
		<br />
		
		<form:label path="es_name">
			<spring:message code="spanish_name" />:
		</form:label>
		<form:input path="es_name"/>
		<form:errors cssClass="error" path="es_name" />
		<br />
	</fieldset>

	<input type="submit" name="save" value="<spring:message code="category.save" />" />
	<jstl:if test="${categoryForm.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="category.delete" />"
			onclick="return confirm('<spring:message code="category.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"	value="<spring:message code="category.cancel"/>" 
		onclick="javascript: relativeRedir('category/administrator/list.do');" />
	<br />
</form:form>