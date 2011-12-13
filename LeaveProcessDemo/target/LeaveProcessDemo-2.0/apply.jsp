<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="com.yttimes.dolphin.kernel.WorkItem"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>leave application form  page</title>
</head>
<body>
<%
String userId = request.getParameter("userid");
String workItemId = request.getParameter("workItemId");
%>
<form action="leave?action=apply" method="post">
     <h1>Please fill the form:</h1>
     applier:  <input type="text" name="applier" value="<%=userId%>"><br>
     days to leave: <input type="text" name="daysNum"><br>
     reason:<input type="text" size="100" name="reason"><br>
     <input type=submit value="Apply">
     <input type="hidden" name="workItemId" value="<%=workItemId%>" />
 </form>

</body>
</html>