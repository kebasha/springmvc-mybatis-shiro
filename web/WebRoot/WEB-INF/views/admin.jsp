<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script src="${pageContext.request.contextPath}/js/jquery-1.8.0.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/json_parse_state.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/json_parse.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/json2.js" type="text/javascript"></script>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
	var ctx = '${pageContext.request.contextPath}';
	var params = {
		"username":"1111",
		"password":"2222"
	}
	function testAjax(){
		$.ajax({
			url:ctx + "/auth/ajax.app",
			dataType:"json",
			type:"POST",
			data:params,
			success:function(data){
			alert(JSON.stringify(data));	
				/*if(data != null){
					if(data.code == '200'){
						callback(data);
					}else{
						alert(data.msg);
					}
				}*/
			},
			error:function(data){
				alert(JSON.stringify(data));
			}
		});	
	}
	</script>

  </head>
  
  <body>
    <h1>Admin Page</h1>  
    ${userName}
    <p>管理员页面</p>  
    <a href="javascript:void(0);" onclick="testAjax();">测试Ajax</a>
    <%@ include file="common/curd.jsp"%>
    <a href="javascript:void(0);" onclick="">退出登录</a> 
  </body>
</html>
