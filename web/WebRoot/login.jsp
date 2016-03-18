<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'index.jsp' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="${pageContext.request.contextPath}/js/jquery-1.8.0.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/json_parse_state.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/json_parse.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/json2.js" type="text/javascript"></script>
		<script type="text/javascript">
			function changeVerifyCode(){
				var url = "${pageContext.request.contextPath}/yzm?" + Math.floor(Math.random()*100);
				$("#verifyCodeImg").attr("src",url);
				//img.src = "${pageContext.request.contextPath}/yzm?" + Math.floor(Math.random())*100);
			}
		</script>
	</head>

	<body>
		<form action="${pageContext.request.contextPath}/auth/login.app" method="post">
			<h1>
				Login
			</h1>
			<div id="login-error">${error}</div>
			Username
			<input type="text" id="userName" name="userName"/>
			<br />
			Password
			<input type="password" id="password" name="password"/>
			<br />
			VerifyCode
			<input type="text" id="verifyCode" name="verifyCode"/>
			<img id="verifyCodeImg" title="换一张" alt="" src="${pageContext.request.contextPath}/yzm" style="cursor:pointer;" onclick="changeVerifyCode();">
			<br/>
			<P>${message }</P>
			<input type="submit" value="Login" />
		</form>
	</body>
</html>
