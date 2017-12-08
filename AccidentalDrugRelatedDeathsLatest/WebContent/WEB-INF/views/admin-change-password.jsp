<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Change Password</title>
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
			<div class="col-md-6">
				<c:if test="${sessionScope.pswdUpdateFlag}">
					<div class="text-center">
						<span class="text-success">Password Updated Successfully</span>
					</div>
				</c:if>
				<c:if test="${sessionScope.pswdUpdateFlag ne null && sessionScope.pswdUpdateFlag eq false}">
					<div class="text-center">
						<span class="text-danger">Password Update Failed</span>
					</div>
				</c:if>
				<c:remove var="pswdUpdateFlag" scope="session" />
				<h1 class="text-center">Change Password</h1>
				<form
					action="${pageContext.request.contextPath}/admin/update-password"
					method="post">
					<div class="form-group">
						<label for="Old Password">Old Password:</label> <input
							type="password" name="oldPswd" required class="form-control">
					</div>
					<div class="form-group">
						<label for="pwd">New Password:</label> <input type="password"
							name="newPswd" required class="form-control" id="pwd"> <a
							href="javascript:void(0);" onclick="togglePassword(pwd, this)">View
							Password</a>
					</div>
					<button type="submit" class="btn btn-success">Submit</button>
				</form>
			</div>
		</div>
	</div>

	</section>
	<script>
		function togglePassword(pwd, that) {
			if (pwd.type === 'password') {
				pwd.type = 'text';
				that.innerHTML = 'Hide Password';
			} else {
				pwd.type = 'password';
				that.innerHTML = 'View Password';
			}
		}
	</script>
</body>
</html>