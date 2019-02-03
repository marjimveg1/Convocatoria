<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p>
	<strong> <spring:message code="categoryTranslation.name" />: </strong>
	<jstl:out value="${mapa.get(category.id)[0]}" />
</p>

<p>
	<strong> <spring:message code="category.parent" />: </strong>
	<jstl:if test="${category.parent != null}">
		<a href="category/administrator/display.do?categoryId=${category.parent.id}">
			<jstl:out value="${mapa.get(category.id)[1]}" />
		</a>
	</jstl:if>
	<jstl:if test="${category.parent == null}">
		<spring:message code="category.messageParent" />
	</jstl:if>
</p>

<jstl:if test="${not empty category.descendants}">
	<strong> <spring:message code="category.descendant" />: </strong>
	<display:table name="category.descendants" id="row" requestURI="category/administrator/display.do" pagesize="5" class="displaytag">
		<display:column>
			<a href="category/administrator/display.do?categoryId=${row.id}">
				<spring:message code="category.display" />
			</a>
		</display:column>
		<display:column>
			<a href="category/administrator/edit.do?categoryId=${row.id}">
				<spring:message code="category.edit" />
			</a>
		</display:column>
		<display:column value="${mapa.get(row.id)[0]}" titleKey="categoryTranslation.name" />
	</display:table>
</jstl:if>

<p>
	<a href="category/administrator/list.do">
		 <spring:message code="category.return" />
	</a>
</p>

	   
	   