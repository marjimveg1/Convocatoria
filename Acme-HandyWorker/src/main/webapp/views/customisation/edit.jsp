<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="customisation/administrator/edit.do" modelAttribute="customisation">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:label path="systemName">
		<spring:message code="customisation.systemName" />:
	</form:label>
	<form:input path="systemName" />
	<form:errors cssClass="error" path="systemName" />
	<br/>
	
	<form:label path="banner">
		<spring:message code="customisation.banner" />:
	</form:label>
	<form:input path="banner" />
	<form:errors cssClass="error" path="banner" />
	<br/>
	
	<form:label path="welcomeMessageEn">
		<spring:message code="customisation.welcomeMessageEn" />:
	</form:label>
	<form:input path="welcomeMessageEn" />
	<form:errors cssClass="error" path="welcomeMessageEn" />
	<br/>
	
	<form:label path="welcomeMessageSp">
		<spring:message code="customisation.welcomeMessageEs" />:
	</form:label>
	<form:input path="welcomeMessageSp" />
	<form:errors cssClass="error" path="welcomeMessageSp" />
	<br/>
	
	<form:label path="VAT">
		<spring:message code="customisation.vat" />:
	</form:label>
	<form:input path="VAT" />
	<form:errors cssClass="error" path="VAT" />
	<br/>
	
	<form:label path="countryCode">
		<spring:message code="customisation.countryCode" />:
	</form:label>
	<form:input path="countryCode" />
	<form:errors cssClass="error" path="countryCode" />
	<br/>
	
	<form:label path="timeCachedFinderResults">
		<spring:message code="customisation.timeCached" />:
	</form:label>
	<form:input path="timeCachedFinderResults" />
	<form:errors cssClass="error" path="timeCachedFinderResults" />
	<br/>
	
	<form:label path="maxFinderResults">
		<spring:message code="customisation.maxResults" />:
	</form:label>
	<form:input path="maxFinderResults" />
	<form:errors cssClass="error" path="maxFinderResults" />
	<br/>
	
	<form:label path="creditCardMakes">
		<spring:message code="customisation.creditCardMakes" />:
	</form:label>
	<form:textarea path="creditCardMakes" />
	<form:errors cssClass="error" path="creditCardMakes" />
	<br/>
	
	<form:label path="positiveWords">
		<spring:message code="customisation.positiveWords" />:
	</form:label>
	<form:input path="positiveWords" />
	<form:errors cssClass="error" path="positiveWords" />
	<br/>
	
	<form:label path="negativeWords">
		<spring:message code="customisation.negativeWords" />:
	</form:label>
	<form:input path="negativeWords" />
	<form:errors cssClass="error" path="negativeWords" />
	<br/>
	
	<form:label path="spamWords">
		<spring:message code="customisation.spamWords" />:
	</form:label>
	<form:input path="spamWords" />
	<form:errors cssClass="error" path="spamWords" />
	<br/>
	
	<form:label path="languages">
		<spring:message code="customisation.languages" />:
	</form:label>
	<form:input path="languages" />
	<form:errors cssClass="error" path="languages" />
	<br/>
	
	<input type="submit" name="save" value="<spring:message code="customisation.save" />" />
	<input type="button" name="cancel"	value="<spring:message code="customisation.cancel" /> "onclick="javascript: relativeRedir('customisation/administrator/display.do');" />
	<br />
</form:form>