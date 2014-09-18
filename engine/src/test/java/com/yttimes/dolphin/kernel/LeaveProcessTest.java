/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/kernel/Attic/LeaveProcessTest.java,v 1.1.2.4 2011/12/02 07:23:46 neo-cvs Exp $
 * $Revision: 1.1.2.4 $
 * $Date: 2011/12/02 07:23:46 $
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
package com.yttimes.dolphin.kernel;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.yttimes.dolphin.ProcessSample;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.ProcessInstance.State;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: LeaveProcessTest.java,v 1.1.2.4 2011/12/02 07:23:46 neo-cvs Exp $
 */
public class LeaveProcessTest extends ProcessSample {
	
	@Test
	public void testUndoProcess() {
		System.out.println("test undo Process..");
		
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave", 0);
		processIns.setDataVariable("applier", "Ivan");
		processIns.setDataVariable("leaveDayNum", "4");
		assertEquals(processIns.getState(), State.INITIALIZED);
		processIns.start();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();

		JPAService.getInstance().beginTransaction();
		List<WorkItem> workItems = manager.getUndoneWorkItems(1111);
		WorkItem currentWorkItem = workItems.get(0);
		currentWorkItem.complete();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();
		
		JPAService.getInstance().beginTransaction();
		workItems = manager.getUndoneWorkItems(2222);
		currentWorkItem = workItems.get(0);
		currentWorkItem.getProcessInstance().undo();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();
		
		workItems = manager.getUndoneWorkItems(1111);
		assertEquals(1, workItems.size());
		workItems = manager.getUndoneWorkItems(2222);
		assertEquals(0, workItems.size());
		workItems = manager.getUndoneWorkItems(3333);
		assertEquals(0, workItems.size());
	}
	
	@Test 
	public void testWorkItemCompleteStrategyAny() {
		System.out.println("test workitem complete strategy..");
		long[] userIds = {4444,5555,6666};
		
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave", 0);
		processIns.assignUsersToNextActivity(userIds);
		processIns.start();
		JPAService.getInstance().commitTransaction();
		
		long processDefId = processIns.getProcessDefinition().getId();
		List<WorkItem> workItems = manager.getUndoneWorkItemsByDef(4444,processDefId);
		assertEquals(1, workItems.size());
        workItems = manager.getUndoneWorkItemsByDef(5555, processDefId);
		assertEquals(1, workItems.size());
		workItems = manager.getUndoneWorkItemsByDef(6666, processDefId);
		assertEquals(1, workItems.size());
		
		long[] nextUserIds = {7777};
		
		JPAService.getInstance().beginTransaction();
		
		WorkItem currentWorkItem = workItems.get(0);
		currentWorkItem.getProcessInstance().assignUsersToNextActivity(nextUserIds);
		currentWorkItem.complete();
		JPAService.getInstance().commitTransaction();
		
		assertEquals(processIns.getState(), State.SUSPENDED);
		workItems = manager.getUndoneWorkItemsByDef(7777, processDefId);		
		assertEquals(1, workItems.size());
	}

}
