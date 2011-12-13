/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/example/servlet/Attic/LeaveExampleServlet.java,v 1.1.2.3 2011/08/02 09:20:51 neo-cvs Exp $
 * $Revision: 1.1.2.3 $
 * $Date: 2011/08/02 09:20:51 $
 * 
 * ==================================================================== 
 * 
 * Copyright (c) 2011 YT Times Co., Ltd All Rights Reserved. 
 * This software is the confidential and proprietary information of 
 * YT Times Co., Ltd. You shall not disclose such Confidential 
 * Information. 
 * 
 * ==================================================================== 
 * 
 */
package com.yttimes.dolphin.example.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.example.service.LeaveService;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.annotation.DolphinJTAProxy;

/**
 *
 * @author Neo
 * @version $Id: LeaveExampleServlet.java,v 1.1.2.3 2011/08/02 09:20:51 neo-cvs Exp $
 */
public class LeaveExampleServlet extends HttpServlet {

	private static final long serialVersionUID = -1327228685337743559L;
	private Logger logger = LoggerFactory.getLogger(LeaveExampleServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		LeaveService leaveService = DolphinJTAProxy.getProxy(LeaveService.class);
		try {
			if (action.equals("start")) {
				long userId = Long.parseLong(request.getParameter("userid"));
				leaveService.startLeave(userId);
				redirect("main.jsp?msg=You started a new leave process.", response);
			} else if (action.equals("todo")) {
				long userId = Long.parseLong(request.getParameter("userid"));
				List<WorkItem> workItems = leaveService.getTodoWorkItems(userId);
				request.setAttribute("workItems", workItems);
				forward("/todo.jsp", request, response);
			} else if (action.equals("apply")) {
				long workItemId = Long.parseLong(request.getParameter("workItemId"));
				long applierId = Long.parseLong(request.getParameter("applier"));
				long dayNum = Long.parseLong(request.getParameter("daysNum"));
				String reason = request.getParameter("reason");
				logger.info("workItemId:" + workItemId);
				logger.info("applierId:" + applierId);
				logger.info("dayNum:" + dayNum);
				logger.info("reason:" + reason);
				leaveService.applyLeave(workItemId, applierId, dayNum, reason);

				redirect("main.jsp?msg=Your application was submited, pls wait!", response);
			} else if (action.equals("approve")) {
				long workItemId = Long.parseLong(request.getParameter("workItemId"));
				long userId = Long.parseLong(request.getParameter("userid"));
				boolean approveFlag = Boolean.parseBoolean(request.getParameter("approveFlag"));
				logger.info("workItemId:" + workItemId);
				logger.info("userId:" + userId);
				logger.info("approveFlag:" + approveFlag);
				leaveService.approveLeave(workItemId, userId, approveFlag);
				redirect("main.jsp?msg=Your decision was submited!", response);
			} else {
				logger.error("unknown action type" + action);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex);
		}
	}

	private void redirect(String destination, HttpServletResponse aResponse) throws IOException {
		String urlWithSessionID = aResponse.encodeRedirectURL(destination);
		aResponse.sendRedirect(urlWithSessionID);
	}

	private void forward(String destination, HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = aRequest.getRequestDispatcher(destination);
		dispatcher.forward(aRequest, aResponse);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void destroy() {
		logger = null;
	}
}
