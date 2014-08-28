/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/samples/Attic/UserAssignLeaveProcessTest.java,v 1.1.2.9 2011/12/06 07:00:42 neo-cvs Exp $
 * $Revision: 1.1.2.9 $
 * $Date: 2011/12/06 07:00:42 $
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
package com.yttimes.dolphin.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.yttimes.dolphin.ProcessSample;
import com.yttimes.dolphin.definition.Activity;
import com.yttimes.dolphin.definition.Task.AssignmentPattern;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.ProcessInstance.State;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: UserAssignLeaveProcessTest.java,v 1.1.2.9 2011/12/06 07:00:42 neo-cvs Exp $
 */
public class UserAssignLeaveProcessTest  extends ProcessSample{

	@Test
	public  void testWorkItemCompleteStrategyAll() {
		System.out.println("test workitem complete strategy..");
		
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave_user", 0);
		processIns.setDataVariable("leaveDayNum", "5");
		processIns.setDataVariable("approveFlag", "true");
		processIns.start();
		JPAService.getInstance().commitTransaction();		
  
		long processInstId = processIns.getId();
		
		JPAService.getInstance().beginTransaction();
		WorkItem workItem = manager.getUndoneWorkItem(10310000000001L, processInstId);
		assertNotNull(workItem);
		List<Activity> activities = workItem.getNextManualActivities();
		assertEquals(1, activities.size());
		Activity activity = activities.get(0);
		long[] users = activity.getTask("deptApprove").getAssignedPerformers();
		assertEquals(2, users.length);
		assertEquals(1001,users[0]);
		assertEquals(1002,users[1]);
		assertEquals("act_2",activity.getId());
		workItem.complete();
		JPAService.getInstance().commitTransaction();		

		WorkItem workItems1001 = manager.getUndoneWorkItem(1001, processInstId);
		assertNotNull(workItems1001);
		WorkItem workItems1002 = manager.getUndoneWorkItem(1002, processInstId);
		assertNotNull(workItems1002);
		
		long[] nextUserIds = {8888};
		
		JPAService.getInstance().beginTransaction();		
		workItems1001.getProcessInstance().assignUsersToNextActivity(nextUserIds);
		workItems1001.complete();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();
		
		workItem = manager.getUndoneWorkItem(8888, processInstId);		
		assertNull(workItem);
		
		activities = workItems1002.getNextManualActivities();
		assertEquals(1, activities.size());
		assertEquals("act_3", activities.get(0).getId());
		AssignmentPattern pattern = activities.get(0).getTask("generalApprove").getAssignmentPattern();
		assertEquals("ALL",pattern.name());		
		
		JPAService.getInstance().beginTransaction();		
		workItems1002.complete();			
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();

		workItem = manager.getUndoneWorkItem(8888, processInstId);		
		assertNotNull(workItem);
	}
	
	@Test
	public  void testWorkflowEntryURL() {
		System.out.println("test workflow entry URL..");
		String url = manager.getProcessEntryURL("leave_user", 2222);
		assertEquals("/apply.jsp", url);
	}
	
	@Test
	public  void testSetNextActivity() {
		System.out.println("test set process's next activity..");
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave_user", 0);
		processIns.setDataVariable("leaveDayNum", "5");
		processIns.setDataVariable("approveFlag", "true");
		processIns.start();
		JPAService.getInstance().commitTransaction();		
  
		long processDefId = processIns.getProcessDefinition().getId();
		
		JPAService.getInstance().beginTransaction();
		List<WorkItem> workItems = manager.getUndoneWorkItemsByDef(10310000000001L, processDefId);
		WorkItem current = workItems.get(0);		
		long[] userIds = {2222};
		current.getProcessInstance().assignUsersToNextActivity(userIds);
		current.complete();
		JPAService.getInstance().commitTransaction();	
		
		JPAService.getInstance().beginTransaction();
		workItems = manager.getUndoneWorkItemsByDef(2222, processDefId);
		assertEquals(1, workItems.size());
		current = workItems.get(0);		
		current.getProcessInstance().setNextActivity("act_4");
		current.complete();
		assertEquals(processIns.getState(), State.TERMINATED);		
		JPAService.getInstance().commitTransaction();			

	}
	
	
}
