<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Dashboard</title>
<link href='<c:url value="/resources/css/bootstrap.min.css"/>' rel="stylesheet">
<link href='<c:url value="/resources/css/jquery.dataTables.min.css"/>' rel="stylesheet">  
<link href='<c:url value="/resources/css/style.css"/>' rel="stylesheet"> 
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src='<c:url value="/resources/js/bootstrap.min.js"/>'></script>
<script src='<c:url value="/resources/js/jquery.dataTables.min.js"/>'></script>

<script src='<c:url value="/resources/js/custom.js"/>'></script>
</head>
<body>
<section>
	<jsp:include page="admin-menu.jsp"></jsp:include>
	
<div class="side-body">
<jsp:include page="header.jsp"></jsp:include>
<div class="sidebody-content">
			<h1 class="text-center">Uploaded files</h1>
			<table id="example" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>File Name</th>
						<th>File Status</th>
						<th>Visibility</th>
						<th>Reject Reason</th>
						<th>Options</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>File Name</th>
						<th>File Status</th>
						<th>Visibility</th>
						<th>Reject Reason</th>
						<th>Options</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${userFiles}" var="file">
						<tr>
							<td>${file.fileName}</td>
							<td><c:if test="${file.status eq 1}">Accepted</c:if>
							<c:if test="${file.status eq 2}">Rejected</c:if></td>
							<td>${file.visibility}</td>
							<td>${file.rejectReason ne null ?file.rejectReason:'none'}</td>
							<td>
							<a href="${pageContext.request.contextPath}/admin/${file.id}/download" class="btn btn-info">Download</a>
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