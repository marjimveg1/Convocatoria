<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="box/administrator,customer,handyWorker,referee,sponsor/edit.do" modelAttribute="box" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="messages"/>
	<form:hidden path="actor"/>
	
	
	<form:label path="name">
		<spring:message code="box.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br />
	
	<!-- Buttons -->
	
	<input type="submit" name="save" value="<spring:message code="box.save"/>" />
	
	<jstl:choose>
		<jstl:when test="${box.id != 0}">
			<input type="submit" name="delete" value="<spring:message code="box.delete"/>" 
				onclick="return confirm('<spring:message code="box.confirm.delete" />')"/>
		
		<input type="button" name="cancel" value="<spring:message code="box.cancel" />" 
			onclick="javascript: relativeRedir('box/administrator,customer,handyWorker,referee,sponsor/display.do?boxId=${box.id}');" />
		</jstl:when>
		<jstl:otherwise>
			<input type="button" name="cancel" value="<spring:message code="box.cancel" />" 
			onclick="javascript: relativeRedir('box/administrator,customer,handyWorker,referee,sponsor/list.do');" />
		</jstl:otherwise>
		
	</jstl:choose>
	
	

</form:form>