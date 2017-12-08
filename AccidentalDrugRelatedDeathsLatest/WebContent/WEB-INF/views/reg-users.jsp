<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Registered Users | Admin</title>
<link href='<c:url value="/resources/css/bootstrap.min.css"/>'
	rel="stylesheet">
<link href='<c:url value="/resources/css/jquery.dataTables.min.css"/>'
	rel="stylesheet">
<link href='<c:url value="/resources/css/style.css"/>' rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src='<c:url value="/resources/js/bootstrap.min.js"/>'></script>
<script src='<c:url value="/resources/js/jquery.dataTables.min.js"/>'></script>
<script src='<c:url value="/resources/js/custom.js"/>'></script>
</head>
<body>
	<section> <jsp:include page="admin-menu.jsp"></jsp:include>

	<div class="side-body">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="sidebody-content">
			<div class="text-center">
				
				<c:if test="${sessionScope.bOrub ne null && sessionScope.bOrub eq true}">
					<p class="text-success">
						User ${sessionScope.email} has been <c:if test="${sessionScope.status eq 2}"> " Blocked "</c:if> 
						<c:if test="${sessionScope.status eq 1}"> " Unblocked "</c:if> 
					</p>
				</c:if>
				<c:if test="${sessionScope.bOrub ne null && sessionScope.bOrub eq false}">
					<p class="text-danger">
						Failed to <c:if test="${sessionScope.status eq 2}"> " Block "</c:if> 
						<c:if test="${sessionScope.status eq 1}"> " Unblock "</c:if> the user ${sessionScope.email} 
					</p>
				</c:if>
				<c:remove var="bOrub" scope="session"/>
				<c:remove var="email" scope="session"/>
				<c:remove var="status" scope="session"/>
			</div>
			<h1 class="text-center">Registered Users</h1>
			<table id="example" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>Name</th>
						<th>Email</th>
						<th>Contact Number</th>
						<th>Options</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Name</th>
						<th>Email</th>
						<th>Contact Number</th>
						<th>Options</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${regUsers}" var="user">
						<tr>
							<td>${user.firstName}${user.lastName}</td>
							<td>${user.email}</td>
							<td>${user.contactNum}</td>
							<td><c:choose>
									<c:when test="${user.userStatus eq 1}">
										<a href="${pageContext.request.contextPath}/block-or-unblock/2/${user.id}/${user.email}" class="btn btn-primary">Block</a>
									</c:when>
									<c:otherwise>
										<a href="${pageContext.request.contextPath}/block-or-unblock/1/${user.id}/${user.email}" class="btn btn-danger">Unblock</a>
									</c:otherwise>
								</c:choose>
								<a href="<c:out value="${pageContext.request.contextPath}/admin/user-files/${user.email}"/>" class="btn btn-info">View files</a>	
							</td>
								
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</div>
	</section>
</body>
</html>