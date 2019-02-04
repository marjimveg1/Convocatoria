<%--
 * header.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme-HandyWorker Co., Inc."  width="500" height="200"/></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
<!-- if actor is Anonymous -->	
	<security:authorize access="isAnonymous()">
		<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
	
		<li><a class="fNiv"><spring:message	code="master.page.register" /></a>	
			<ul>
				<li class="arrow"></li>
				<li><a href="actor/create.do?role=customer"><spring:message code="master.page.customer.create" /></a></li>
				<li><a href="actor/create.do?role=handyWorker"><spring:message code="master.page.handyworker.create" /></a></li>
				<li><a href="actor/create.do?role=sponsor"><spring:message code="master.page.sponsor.create" /></a></li>
			</ul>
		</li>
	</security:authorize>	
	

<!-- if actor is ADMINISTRATOR -->	
	<security:authorize access="hasRole('ADMIN')">
	
		<li><a class="fNiv"><spring:message	code="master.page.warranty" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="warranty/administrator/list.do"><spring:message code="master.page.warranty.list" /></a></li>
			</ul>
		</li>
		
		<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="actor/administrator/create.do?role=administrator"><spring:message code="master.page.administrator.create" /></a></li>
				<li><a href="actor/administrator/list.do"><spring:message code="master.page.administrator.list" /></a></li>
				<li><a href="message/administrator/broadcast.do"><spring:message code="master.page.administrator.broadcast" /></a></li>					
				<li><a href="customisation/administrator/display.do"> <spring:message code="master.page.customisation.customisation" /> </a></li>
				<li><a href="category/administrator/list.do"> <spring:message code="master.page.administrator.category" /> </a></li>
				<li><a href="dashboard/administrator/display.do"> <spring:message code="master.page.administrator.dashboard" /> </a></li>					
			</ul>
		</li>		
	</security:authorize>
	
<!-- if actor is HANDYWORKER -->
	<security:authorize access="hasRole('HANDYWORKER')">
		<li><a class="fNiv"><spring:message	code="master.page.application" /></a>					
			<ul>
				<li class="arrow"></li>
				<li><a href="application/handyWorker/list.do"><spring:message code="master.page.handyWorker.application.list" /></a></li>					
			</ul>
		</li>
		<li><a class="fNiv"><spring:message	code="master.page.fixUpTask" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="fixUpTask/handyWorker/listAll.do"><spring:message code="master.page.fixUpTask.all.list" /></a></li>
				<li><a href="fixUpTask/handyWorker/listInvolved.do"><spring:message code="master.page.fixUpTask.involved.list" /></a></li>
			</ul>
		</li>		
		
	</security:authorize>
	
<!-- if actor is CUSTOMER -->
<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message	code="master.page.fixUpTask" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="fixUpTask/customer/list.do"><spring:message code="master.page.fixUpTask.own.list" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.bount" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="bount/customer/list.do"><spring:message code="master.page.bount.own.list" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
	
<!-- if actor is authenticated -->		
	<security:authorize access="isAuthenticated()">
		<li>
			<a class="fNiv"> 
				<spring:message code="master.page.profile" /> 
		        (<security:authentication property="principal.username" />)
			</a>
			<ul>
				<li class="arrow"></li>
				<li><a href="actor/administrator,customer,handyWorker,referee,sponsor/display.do"><spring:message code="master.page.actor.display" /></a></li>
				<li><a href="box/administrator,customer,handyWorker,referee,sponsor/list.do"><spring:message code="master.page.box.list" /></a></li>
				<li><a href="message/administrator,customer,handyWorker,referee,sponsor/send.do"><spring:message code="master.page.message.send" /></a></li>
				<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
			</ul>
		</li>
	</security:authorize>
	
	
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

