<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="com.yttimes.dolphin.kernel.WorkItem"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>user todo index page</title>
</head>
<body>
<%
String userId = request.getParameter("userid");
String message = request.getParameter("msg");
%>
<font size='8' color='red'><%=message==null?"welcome to dolphin example!(Version:1.0)":message%></font>
<br>
<ul>
<li><b><a href="leave?action=todo&userid=1111">Login as demo user(1111)</a></b></li>
<BR>
<li><b><a href="leave?action=todo&userid=2222">Login as dept manager(2222)</a></b></li>
<BR>
<li><b><a href="leave?action=todo&userid=3333">Login as general manager(3333)</a></b></li>
</ul>
</body>
</html>