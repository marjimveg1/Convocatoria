<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="actors" id="row" requestURI="${requestURI}" class="displaytag">
	<jstl:if test="${!isEndorsable}">
		<display:column>
			<jstl:choose>
				<jstl:when test="${row.userAccount.isBanned}">
					<a href="actor/administrator/changeBan.do?actorId=${row.id}"><spring:message code="suspicious.table.unban"/></a>
				</jstl:when>
				<jstl:otherwise>
					<a href="actor/administrator/changeBan.do?actorId=${row.id}"><spring:message code="suspicious.table.ban"/></a>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</jstl:if>
	
	<display:column property="fullname" titleKey="actor.fullname" />
	
	<display:column property="email" titleKey="suspicious.table.email" />
	
	<display:column property="phoneNumber" titleKey="suspicious.table.phoneNumber" />
	
	<display:column property="address" titleKey="suspicious.table.address" />
	
	<display:column property="userAccount.username" titleKey="suspicious.table.username" />
	
	<display:column property="userAccount.authorities" titleKey="suspicious.table.authority" />
	
	<jstl:if test="${isEndorsable}">
		<display:column property="score" titleKey="suspicious.table.score" />	
	</jstl:if>
</display:table>

<jstl:if test="${isEndorsable}">
	<form:form action="endorsable/administrator/computeScore.do">
		<input type="submit" name="compute" value="<spring:message code="endorsable.compute" />" />
	</form:form>
<!--  	
	<a href="endorsable/administrator/computeScore.do">
 		<spring:message code="endorsable.compute" />
	</a>
-->
</jstl:if>