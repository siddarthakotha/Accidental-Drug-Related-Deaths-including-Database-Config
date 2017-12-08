
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title>
<link href='<c:url value="/resources/css/bootstrap.min.css"/>'
	rel="stylesheet">
<link href='<c:url value="/resources/css/style.css"/>' rel="stylesheet">
</head>
<body>
	<!-- login start-->
	<section>
		<div class="container">

			<div class="login-container">
				<c:if test="${loginError}">
					<div class="text-center">
						<p class="text-danger">Invalid username/password</p>
					</div>
				</c:if>
				<c:remove var="loginError" scope="session"/>
				<h1>Login to Your Account</h1>
				<br>
				<form
					action="${pageContext.request.contextPath}/admin/login-process"
					method="post">
					<input type="text" name="username" placeholder="Username" required>
					<input type="password" name="password" placeholder="Password"
						required> <input type="submit" name="login"
						class="login loginmodal-submit" value="Login">
				</form>

				<div class="login-help "></div>
			</div>
		</div>
	</section>
	<!-- login end -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src='<c:url value="/resources/js/bootstrap.min.js"/>'></script>
	<%-- <script src='<c:url value="/resources/js/custom.js"/>'></script> --%>
</body>
</html>