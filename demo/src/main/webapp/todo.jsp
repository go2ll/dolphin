<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="com.yttimes.dolphin.kernel.WorkItem"%>
<%@page import="com.yttimes.dolphin.kernel.ProcessInstance"%>
<%@page import="com.yttimes.dolphin.example.Leave"%>
<%@page import="com.yttimes.dolphin.example.service.LeaveService"%>
<%@page import="com.yttimes.dolphin.support.annotation.DolphinJTAProxy"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>user todo index page</title>
</head>
<body>

<%
	String userId = request.getParameter("userid");
	String message = (String) request.getAttribute("msg");
	List<WorkItem> workItems = (List<WorkItem>) request.getAttribute("workItems");
	Leave leaveInfo = DolphinJTAProxy.getProxy(LeaveService.class).getLeaveByUserId(Long.parseLong(userId));
%>
<font size='8' color='red'><%=message == null ? "welcome!" + userId : message%></font>
<br>
<b>To do work items:</b>
<%
	if (workItems == null || workItems.size() == 0) {
		out.print("you have nothing to do!");
	} else {
%>
<TABLE>
	<%
		for (int row = 0; row < workItems.size(); row++) {
	%>
	<TR>
		<TD><%=row + 1%>.</TD>
		<TD><a
			href=".<%=workItems.get(row).getTaskFormUrl()%>?userid=<%=userId%>&workItemId=<%=workItems.get(row).getId()%>"><%=workItems.get(row).getTaskName()%>
		</a></TD>
	</TR>
	<%
		}
	%>
</TABLE>
<%
	}
%>
<br>
<br>
<b>Current Leave info:</b>
<%
	if (leaveInfo != null) {
		ProcessInstance processInstance = leaveInfo.getProcessInstance();
%>
<p>applier:<%=leaveInfo.getApplierId()%>
<p>apply date:<%=leaveInfo.getApplyDate()%>
<p>how many days to leave:<%=leaveInfo.getLeaveDayNum()%>
<p>leave reason:<%=leaveInfo.getReason()%>
<p>approve by:<%=leaveInfo.getApproverId()%>
<p>approveFlag:<%=leaveInfo.getApproveFlag()%>
<p>process status:<%=processInstance == null ? null : processInstance.getState()%>
<%
	} else {
		out.print("you have none!");
	}
%> <br>
<br>
<b><a href="leave?action=start&userid=<%=userId%>">Apply a leave
now!</a></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><a href="./main.jsp">Return
to homepage!</a></b>
</body>
</html>