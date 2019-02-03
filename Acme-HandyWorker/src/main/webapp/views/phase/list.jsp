<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<spring:message code="phase.date.format" var="tableDateFormat"/>
<jstl:choose>
	<jstl:when test="${phaseEditionPerm}">
		<jstl:set var="startMomentSort" value="${5}"/>
	</jstl:when>
	<jstl:otherwise>
		<jstl:set var="startMomentSort" value="${3}"/>
	</jstl:otherwise>
</jstl:choose>


<display:table name="phases" id="row" requestURI="phase/customer,handyWorker,referee/list.do" defaultsort="${startMomentSort}" class="displaytag">
	<display:column>
		<a href="phase/customer,handyWorker,referee/display.do?phaseId=${row.id}"><spring:message code="phase.display"/></a>
	</display:column>
	
	<jstl:if test="${phaseEditionPerm}">	
		<display:column>
			<a href="phase/handyWorker/edit.do?phaseId=${row.id}"><spring:message code="phase.edit"/></a>
		</display:column>
		
		<display:column>
			<a href="phase/handyWorker/delete.do?phaseId=${row.id}"><spring:message code="phase.delete"/></a>
		</display:column>
	</jstl:if>
	
	<display:column property="title" titleKey="phase.title"/>
	<display:column property="startMoment" titleKey="phase.startMoment" sortable="true"  format="${tableDateFormat}"/>
	<display:column property="endMoment" titleKey="phase.endMoment" sortable="true"  format="${tableDateFormat}"/>
</display:table>

<a href="fixUpTask/customer,handyWorker,referee/display.do?fixUpTaskId=${fixUpTaskId}"><spring:message code="phase.back"/></a>
<jstl:if test="${phaseEditionPerm}">
	&nbsp;
	<a href="phase/handyWorker/create.do?fixUpTaskId=${fixUpTaskId}"><spring:message code="phase.create"/></a>
</jstl:if>