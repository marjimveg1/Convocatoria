<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>



<display:table name="warranties" id="row" requestURI="warranty/administrator/list.do" class="displaytag">
	<display:column>
		<a href="warranty/administrator/display.do?warrantyId=${row.id}"><spring:message code="warranty.display"/></a>
	</display:column>
	
	<display:column>
		<jstl:if test="${!row.finalMode}">
			<a href="warranty/administrator/edit.do?warrantyId=${row.id}"><spring:message code="warranty.edit"/></a>
		</jstl:if>
	</display:column>
		
	<display:column>
		<jstl:if test="${!row.finalMode}">
			<a href="warranty/administrator/delete.do?warrantyId=${row.id}"><spring:message code="warranty.delete"/></a>
		</jstl:if>
	</display:column>
	
	<display:column property="title" titleKey="warranty.title"/>
	<display:column property="finalMode" titleKey="warranty.finalMode"/>
</display:table>

<a href="warranty/administrator/create.do"><spring:message code="warranty.create"/></a>