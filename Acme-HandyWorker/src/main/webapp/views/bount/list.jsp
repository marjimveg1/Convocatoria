<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<display:table name="bounts" id="row" requestURI="bount/customer/list.do" class="displaytag" pagesize = "5">
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
	
	<display:column style="background-color:${colorValue }"  property="ticker" titleKey="bount.ticker"/>
	<display:column  style="background-color:${colorValue }" property="finalMode" titleKey="bount.finalMode"/>
</display:table>

