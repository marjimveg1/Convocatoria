<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="fixUpTask.date.format2" var="momentFormat"/>
<spring:message code="fixUpTask.date.format3" var="dateFormat"/>

<fieldset>
	<legend><spring:message code="fixUpTask.attributes"/></legend>

	<strong><spring:message code="fixUpTask.ticker"/>:</strong>
	<jstl:out value="${fixUpTask.ticker}"/>
	<br/>
	
	<strong><spring:message code="fixUpTask.customer"/>:</strong>
	<a href="actor/administrator,customer,handyWorker,referee,sponsor/display.do?actorId=${fixUpTask.customer.id}"><jstl:out value="${fixUpTask.customer.name} ${fixUpTask.customer.surname}"/></a>
	<br/>
	
	<strong><spring:message code="fixUpTask.publicationMoment"/>:</strong>
	<fmt:formatDate value="${fixUpTask.publicationMoment}" pattern="${momentFormat}"/>
	<br/>
	
	<strong><spring:message code="fixUpTask.description"/>:</strong>
	<jstl:out value="${fixUpTask.description}"/>
	<br/>
	
	<strong><spring:message code="fixUpTask.address"/>:</strong>
	<jstl:out value="${fixUpTask.address}"/>
	<br/>
	
	<spring:message code="fixUpTask.vat" var="vatTag"/>
	<strong><spring:message code="fixUpTask.maxPrice"/>:</strong>
	<fmt:formatNumber type="number" maxFractionDigits="2" value="${fixUpTask.maxPrice * (1 + vat)}"/> &#8364; <jstl:out value="(${vat*100}% ${vatTag} Inc.)"/>
	<br/>
	
	<strong><spring:message code="fixUpTask.startDate"/>:</strong>
	<fmt:formatDate value="${fixUpTask.startDate}" pattern="${dateFormat}"/>
	<br/>
	
	<strong><spring:message code="fixUpTask.endDate"/>:</strong>
	<fmt:formatDate value="${fixUpTask.endDate}" pattern="${dateFormat}"/>
	<br/>
	
	<strong><spring:message code="fixUpTask.warranty"/>:</strong>
	<a href="warranty/customer,handyWorker,referee/display.do?warrantyId=${fixUpTask.warranty.id}&fixUpTaskId=${fixUpTask.id}"><jstl:out value="${fixUpTask.warranty.title}"/></a>
	<br/>
	
	<strong><spring:message code="fixUpTask.category"/>:</strong>
	<jstl:out value="${category}"/>
	<br/>
</fieldset>

<fieldset>
	<legend><spring:message code="fixUpTask.references"/></legend>

	<strong><spring:message code="fixUpTask.applications"/>:</strong>
	<a href="application/customer,handyWorker,referee/list.do?fixUpTaskId=${fixUpTask.id}"><spring:message code="fixUpTask.application.list"/></a>
	<br/>
	
	<jstl:if test="${isWorkable}">

		<strong><spring:message code="fixUpTask.phases"/>:</strong>
		<a href="phase/customer,handyWorker,referee/list.do?fixUpTaskId=${fixUpTask.id}"><spring:message code="fixUpTask.phase.list"/></a>
		<br/>
	</jstl:if>
</fieldset>



<fieldset>
	<legend><spring:message code="fixUpTask.bounts"/></legend>
<display:table name="bounts" id="row" requestURI="fixUpTask/customer,handyWorker,referee/display.do" class="displaytag" pagesize = "5">
	<jsp:useBean id="now" class="java.util.Date" />
	
	<!-- MAS DE DOS MESES -->
	<jstl:if test="${ row.publicationMoment.time<now.time - 5356800000}">
		<jstl:set var="colorValue" value="LightSlateGray" />
	</jstl:if>
	<!-- ENTRO UN MES Y DOS MESES-->
	<jstl:if test="${(now.time - 2678400000>=row.publicationMoment.time) && (row.publicationMoment.time>=now.time - 5356800000) }">
		<jstl:set var="colorValue" value="SlateBlue" />
	</jstl:if>
	<!-- MENOS DE UN MES-->
	<jstl:if test="${now.time - 2678400000<row.publicationMoment.time}">
		<jstl:set var="colorValue" value="ForestGreen" />
	</jstl:if>
	<!--SIN PUBLICAR-->
	<jstl:if test="${null==row.publicationMoment.time}">
		<jstl:set var="colorValue" value="LightSlateGray" />
	</jstl:if>
	
	
	
	<display:column style="background-color:${colorValue }">
		<a href="bount/handyWorker,customer/display.do?bountId=${row.id}"><spring:message code="bount.display"/></a>
	</display:column>
	
	
	<jstl:if test="${principal == fixUpTask.customer}">
	<display:column style="background-color:${colorValue }">
		<jstl:if test="${!row.finalMode}">
			<a href="bount/customer/edit.do?bountId=${row.id}"><spring:message code="bount.edit"/></a>
		</jstl:if>
	</display:column>
		
	<display:column style="background-color:${colorValue }"> 
		<jstl:if test="${!row.finalMode}">
			<a href="bount/customer/makeFinal.do?bountId=${row.id}"><spring:message code="bount.make.final"/></a>
		</jstl:if>
	</display:column>
	</jstl:if>
	
	<display:column style="background-color:${colorValue }"  property="ticker" titleKey="bount.ticker"/>
	<display:column  style="background-color:${colorValue }" property="finalMode" titleKey="bount.finalMode"/>
</display:table>

<br>

	<jstl:if test="${principal == fixUpTask.customer}">
		<a href="bount/customer/create.do?fixUpTaskId=${fixUpTask.id}"><spring:message code="fixUpTask.create.bount"/></a>
	</jstl:if>
	
</fieldset>





<security:authorize access="hasRole('CUSTOMER')">
	<a href="fixUpTask/customer/list.do"><spring:message code="fixUpTask.back"/></a>
</security:authorize>

<security:authorize access="hasRole('HANDYWORKER')">
	<a href="fixUpTask/handyWorker/listAll.do"><spring:message code="fixUpTask.back.listAll"/></a>
	<br>
	<a href="fixUpTask/handyWorker/listInvolved.do"><spring:message code="fixUpTask.back.involved"/></a>
	<br>
	<a href="application/handyWorker/list.do"><spring:message code="fixUpTask.back.applications"/></a>
</security:authorize>