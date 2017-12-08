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
<script>
	if (localStorage.getItem('loadedOnce') === 'true') {
		// don't reload page, but clear localStorage value so it'll get reloaded next time
		localStorage.removeItem('loadedOnce');
	} else {
		// set the flag and reload the page
		localStorage.setItem('loadedOnce', 'true');
		document.location.reload(true);
	}
</script>
</head>
<body>
<%-- 	Welcome ${sessionScope.admin.username} --%>
<section>
	<jsp:include page="admin-menu.jsp"></jsp:include>
	
<div class="side-body">
<jsp:include page="header.jsp"></jsp:include>
<div class="sidebody-content">
			<h1 class="text-center">Uploaded files</h1>
			<table id="example" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>Email</th>
						<th>File Name</th>
						<th>Visibility</th>
						<th>Options</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Email</th>
						<th>File Name</th>
						<th>Visibility</th>
						<th>Options</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${unverifiedFiles}" var="file">
						<tr>
							<td>${file.email} </td>
							<td>${file.fileName}</td>
							<td>${file.visibility}</td>
							<td>
							<a href="${pageContext.request.contextPath}/admin/${file.id}/accept" class="btn btn-primary">Accept</a>
							<a href="<c:out value="${pageContext.request.contextPath}/admin/${file.id}/reject/"/>" onclick="rejectFile(this)" class="btn btn-danger">Reject</a>
							<a href="${pageContext.request.contextPath}/admin/${file.id}/download" class="btn btn-info">Download</a>
							</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
</div>

</section>
<script type="text/javascript">
function rejectFile(that){
	event.preventDefault(); 
	var reason = prompt("Please enter reason:", "");
	console.log(reason, that.href);
	window.location.href = that.href+reason;
}
</script>
</body>
</html>