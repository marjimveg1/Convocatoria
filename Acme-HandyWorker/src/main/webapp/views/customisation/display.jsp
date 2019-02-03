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
	<strong> <spring:message code="customisation.systemName" />: </strong>
	<jstl:out value="${customisation.systemName}" />
</p>

<div class="banner" >
	<p> <strong> <spring:message code="customisation.banner" />: </strong> </p>
	<img src="${customisation.banner}" alt="Banner" width="500" height="200" />
</div>

<p>
	<strong> <spring:message code="customisation.welcomeMessageEn" />: </strong>
	<jstl:out value="${customisation.welcomeMessageEn}" />
</p>

<p>
	<strong> <spring:message code="customisation.welcomeMessageEs" />: </strong>
	<jstl:out value="${customisation.welcomeMessageSp}" />
</p>

<p>
	<strong> <spring:message code="customisation.vat" />: </strong>
	<jstl:out value="${customisation.VAT*100}" />
	<jstl:out value=" %" />
</p>

<p>
	<strong> <spring:message code="customisation.countryCode" />: </strong>
	<jstl:out value="${customisation.countryCode}" />
</p>

<p>
	<strong> <spring:message code="customisation.timeCached" />: </strong>
	<jstl:out value="${customisation.timeCachedFinderResults}" />
</p>

<p>
	<strong> <spring:message code="customisation.maxResults" />: </strong>
	<jstl:out value="${customisation.maxFinderResults}" />
</p>

<display:table name="customisation.creditCardMakes" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.creditCardMakes" />
</display:table>

<display:table name="customisation.positiveWords" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.positiveWords"/>
</display:table>

<display:table name="customisation.negativeWords" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.negativeWords"/>
</display:table>

<display:table name="customisation.spamWords" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.spamWords"/>
</display:table>

<display:table name="customisation.languages" id="row" requestURI="customisation/administrator/display.do" pagesize="5" class="displaytag">
	<display:column value="${row}" titleKey="customisation.languages"/>
</display:table>

<a href="customisation/administrator/edit.do">
	<spring:message code="customisation.edit" />
</a>
<br />


<p>
	<a href="welcome/index.do">
		<spring:message code="customisation.return" />
	</a>
</p>


	   
	   