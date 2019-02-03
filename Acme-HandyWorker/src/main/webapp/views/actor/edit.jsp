<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="confirm.telephone" var="confirmTelephone"/>
<form:form action="actor/administrator,customer,handyWorker,referee,sponsor/edit.do" modelAttribute="actor" onsubmit="javascript:calcMD5();">
	<jstl:choose>
		<jstl:when test="${role == 'customer'}">
			<h2><spring:message code="header.customer"/></h2>
		
			<form:hidden path="fixUpTasks"/>
		</jstl:when>
		<jstl:when test="${role == 'handyworker'}">
			<h2><spring:message code="header.handyworker"/></h2>
		
			<form:hidden path="applications"/>
			
		</jstl:when>
		
		<jstl:when test="${role == 'administrator'}">
			<h2><spring:message code="header.administrator"/></h2>
		</jstl:when>
		
	</jstl:choose>
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isSuspicious"/>
	<form:hidden path="userAccount"/>
	
	<fieldset>
		<legend><spring:message code="actor.legend"/></legend>
	
		<form:label path="name">
			<spring:message code="actor.name" />
		</form:label>
		<form:input path="name"/>
		<form:errors cssClass="error" path="name" />
		<br />
		
		<form:label path="middleName">
			<spring:message code="actor.middlename" />
		</form:label>
		<form:input path="middleName"/>
		<form:errors cssClass="error" path="middleName" />
		<br />
		
		<form:label path="surname">
			<spring:message code="actor.surname" />
		</form:label>
		<form:input path="surname"/>
		<form:errors cssClass="error" path="surname" />
		<br />
		
		<form:label path="photoLink">
			<spring:message code="actor.photoLink" />
		</form:label>
		<form:input path="photoLink"/>
		<form:errors cssClass="error" path="photoLink" />
		<br />
		
		<form:label path="email">
			<spring:message code="actor.email" />
		</form:label>
		<form:input path="email" placeholder="email@email.com"/>
		<form:errors cssClass="error" path="email" />
		<br />
		
		<form:label path="phoneNumber">
			<spring:message code="actor.phoneNumber" />
		</form:label>
		<form:input path="phoneNumber" placeholder="+34 (111) 654654654"/>
		<form:errors cssClass="error" path="phoneNumber" />
		<br />
		
		<form:label path="address">
			<spring:message code="actor.address" />
		</form:label>
		<form:input path="address"/>
		<form:errors cssClass="error" path="address" />
		<br /> 
		
		<jstl:if test="${role == 'handyworker'}">
			<form:label path="make">
				<spring:message code="actor.make.requested" />
			</form:label>
			<form:input path="make"/>
			<form:errors cssClass="error" path="make" />
			<br /> 
		
		</jstl:if>
	</fieldset>
 
	<fieldset>
		<legend><spring:message code="userAccount.legend"/></legend>
	
		<label for="newusernameId">
			<spring:message code="userAccount.newUsername" />
		</label>
		<input type="text" name="newUsername" id="newusernameId" value="${userAccount.username}"/>
		<br />
		
		<label for="newPasswordId">
			<spring:message code="userAccount.newPassword" />
		</label>
		<input type="password" name="newPassword" id="passwordId"/>
		<br />
		
		<label for="confirmPasswordId">
			<spring:message code="userAccount.confirmPassword" />
		</label>
		<input type="password" name="confirmPassword" id="confirmPasswordId"/>
		<br />
		
	</fieldset>
	
 
 
 	<jstl:choose>
		<jstl:when test="${role == 'customer'}">
			<input type="submit" name="saveCustomer" value="<spring:message code="actor.save" />"  onclick ="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${role == 'handyworker'}">
			<input type="submit" name="saveHw" value="<spring:message code="actor.save" />"  onclick ="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${role == 'sponsor'}">
			<input type="submit" name="saveSponsor" value="<spring:message code="actor.save" />"  onclick ="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${role == 'administrator'}">
			<input type="submit" name="saveAdmin" value="<spring:message code="actor.save" />" onclick ="javascript: return checkTelephone('${confirmTelephone}');" />
		</jstl:when>
		<jstl:when test="${role == 'referee'}">
			<input type="submit" name="saveReferee" value="<spring:message code="actor.save" />" onclick ="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
	</jstl:choose>
  <!-- 
 	<input type="submit" name="save" value="<spring:message code="actor.save" />" />
-->
	<input type="button" name="cancel" value="<spring:message code="actor.cancel" />"
		onclick="javascript: relativeRedir('actor/administrator,customer,handyWorker,referee,sponsor/display.do')" />
	
	<hr>
	
</form:form>