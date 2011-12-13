<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.yttimes.dolphin.example.Leave"%>
<%@page import="com.yttimes.dolphin.example.service.LeaveService"%>
<%@page import="com.yttimes.dolphin.support.annotation.DolphinJTAProxy"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>leave application form  page</title>
</head>
<body>
<%
String userId = request.getParameter("userid");
String workItemId = request.getParameter("workItemId");
Leave leaveInfo = DolphinJTAProxy.getProxy(LeaveService.class).getLeaveByWorkItemId(Long.parseLong(workItemId));
%>
<form action="leave?action=approve" method="post">
     <h1>Please approve the application:</h1>
     applier:  <%=leaveInfo.getApplierId()%><br>
     days to leave: <%=leaveInfo.getLeaveDayNum()%><br>
     reason: <%=leaveInfo.getReason()%><br>
     <br>
     <INPUT TYPE=RADIO NAME="approveFlag" VALUE="true">OK!<BR>
     <INPUT TYPE=RADIO NAME="approveFlag" VALUE="false">NO!<BR>
     
     <input type="hidden" name="workItemId" value="<%=workItemId%>" />
     <input type="hidden" name="userid" value="<%=userId%>" />
     
     <input type=submit value="Apply">
 </form>

</body>
</html>