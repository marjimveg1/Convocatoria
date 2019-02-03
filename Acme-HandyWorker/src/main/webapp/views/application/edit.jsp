<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<form:form action="application/handyWorker,customer/edit.do" modelAttribute="application">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="registerMoment" />
	<form:hidden path="handyWorker" />	
	<form:hidden path="fixUpTask" />
	<form:hidden path="status" />	

<security:authorize access="hasRole('HANDYWORKER')">
	
	<form:hidden path="customerComments" />	
	<form:hidden path="creditCard" />
	
	<jstl:if test="${application.status=='PENDING'}">
		<form:label path="offeredPrice">
			<spring:message code="application.offeredPrice" />:
		</form:label>
		<form:input path="offeredPrice" />&#8364; (<spring:message code = "application.vat.no"/>)
		<form:errors cssClass="error" path="offeredPrice" />
	<br />
	</jstl:if>
	
		<form:label path="handyWorkerComments">
			<spring:message code="application.handyWorkerComments" />:
		</form:label>
		<form:textarea path="handyWorkerComments"   />
		<form:errors cssClass="error" path="handyWorkerComments" />
		<br />
</security:authorize>
	
<security:authorize access="hasRole('CUSTOMER')">

	<form:hidden path="handyWorkerComments" />
		<form:label path="customerComments">
			<spring:message code="application.customerComments" />:
		</form:label>
		<form:textarea path="customerComments"  />
		<form:errors cssClass="error" path="customerComments" />
		<br /><br/>


	 <fieldset>
	<h2><strong> <spring:message code="application.creditCard" /> </strong></h2>

 		<form:label path="creditCard.holderName">
			<spring:message code="application.creditCard.holderName" />:
		</form:label>
		<form:input path="creditCard.holderName" />
		<form:errors cssClass="error" path="creditCard.holderName" />
	<br /><br/>
	
 
 	<form:label path="creditCard.brandName">
		<spring:message code="application.creditCard.brandName" />:
	</form:label> 
	<form:select path="creditCard.brandName">
		<form:options items="${brandName}" />
	</form:select>
	<form:errors cssClass="error" path="creditCard.brandName" />
	<br/><br/>


		<form:label path="creditCard.number">
			<spring:message code="application.creditCard.number" />:
		</form:label>
		<form:input path="creditCard.number" />
		<form:errors cssClass="error" path="creditCard.number" />
	<br /><br/>
	
		<form:label path="creditCard.expirationMonth">
			<spring:message code="application.creditCard.expirationMonth" />:
		</form:label>
		<form:input path="creditCard.expirationMonth" />
		<form:errors cssClass="error" path="creditCard.expirationMonth" />
	<br /><br/>
		<form:label path="creditCard.expirationYear">
			<spring:message code="application.creditCard.expirationYear" />:
		</form:label>
	
		<form:input path="creditCard.expirationYear" />
		<form:errors cssClass="error" path="creditCard.expirationYear" />
	<br /><br/>
		<form:label path="creditCard.cvvCode">
			<spring:message code="application.creditCard.cvvCode" />:
		</form:label>
			
		<form:input path="creditCard.cvvCode" />
		<form:errors cssClass="error" path="creditCard.cvvCode" /> 
 	</fieldset>
 	
	</security:authorize>
	<input type="submit" name="save" value="<spring:message code="application.save" />" />

<security:authorize access="hasRole('HANDYWORKER')">
		<input type="button" name="cancel"	value="<spring:message code="application.cancel"/>" 
		onclick="javascript: relativeRedir('application/handyWorker/list.do');" />
	<br />
</security:authorize>	
	

<security:authorize access="hasRole('CUSTOMER')">

	<input type="button" name="cancel"	value="<spring:message code="application.cancel"/>" 
		onclick="javascript: relativeRedir('application/customer,handyWorker,referee/list.do?fixUpTaskId=${application.fixUpTask.id}');" />
	<br />
</security:authorize>
	

</form:form>