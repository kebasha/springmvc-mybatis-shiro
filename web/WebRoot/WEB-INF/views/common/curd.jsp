<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<shiro:hasRole name="admin">
	<shiro:hasPermission name="add">
	添加
	</shiro:hasPermission>
	<shiro:hasPermission name="update">
	修改
	</shiro:hasPermission>
	<shiro:hasPermission name="delete">
	删除
	</shiro:hasPermission>
	<shiro:hasPermission name="deletes">
	删除sssssssss
	</shiro:hasPermission>
</shiro:hasRole>
