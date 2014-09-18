/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/samples/Attic/LoopProcessTest.java,v 1.1.2.8 2011/12/02 07:23:37 neo-cvs Exp $
 * $Revision: 1.1.2.8 $
 * $Date: 2011/12/02 07:23:37 $
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

import java.util.List;

import com.yttimes.dolphin.ProcessSample;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.ProcessInstance.State;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: LoopProcessTest.java,v 1.1.2.8 2011/12/02 07:23:37 neo-cvs Exp $
 */
public class LoopProcessTest extends ProcessSample {

	public void testLoopProcess() {
		System.out.println("test LoopProcessTest..");
		long userId = 1000;
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave_loop", userId);

		assertEquals(processIns.getState(), State.INITIALIZED);
		processIns.start();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();
		
		JPAService.getInstance().beginTransaction();
		List<WorkItem> workItems = manager.getUndoneWorkItems(1111);
		assertEquals(workItems.size(), 1);
		WorkItem currentWorkItem = workItems.get(0);
		currentWorkItem.getProcessInstance().setDataVariable("applier", "Ivan");
		processIns.setDataVariable("leaveDayNum", "4");
		currentWorkItem.complete();
		JPAService.getInstance().commitTransaction();
		
		assertEquals(currentWorkItem.getProcessInstance().getState(), State.SUSPENDED);
		
		workItems = manager.getUndoneWorkItems(1111);
		assertEquals(workItems.size(), 0);

		JPAService.getInstance().beginTransaction();
		workItems = manager.getUndoneWorkItems(2222);
		currentWorkItem = workItems.get(0);
		currentWorkItem.getProcessInstance().setDataVariable("approveFlag", "false");
		currentWorkItem.complete();
		JPAService.getInstance().commitTransaction();
		
		assertEquals(currentWorkItem.getProcessInstance().getState(), State.SUSPENDED);		
		
		workItems = manager.getUndoneWorkItems(1111);
		assertEquals(workItems.size(), 1);
		workItems = manager.getUndoneWorkItems(2222);
		assertEquals(workItems.size(), 0);
		
	}
	
	
	public void testSelfLoopProcess() {
		System.out.println("test self Loop ProcessTest..");
		long userId = 1000;
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave_self_loop", userId);

		assertEquals(processIns.getState(), State.INITIALIZED);
		processIns.start();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();
		
		JPAService.getInstance().beginTransaction();
		List<WorkItem> workItems = manager.getUndoneWorkItems(1111);
		assertEquals(workItems.size(), 1);
		WorkItem currentWorkItem = workItems.get(0);
		currentWorkItem.complete();
		JPAService.getInstance().commitTransaction();
		
		assertEquals(currentWorkItem.getProcessInstance().getState(), State.SUSPENDED);
		

		JPAService.getInstance().beginTransaction();
		workItems = manager.getUndoneWorkItems(2222);
		assertEquals(workItems.size(), 1);
		currentWorkItem = workItems.get(0);
		currentWorkItem.getProcessInstance().setDataVariable("loop", "true");
		currentWorkItem.complete();
		JPAService.getInstance().commitTransaction();
		
		
		JPAService.getInstance().beginTransaction();
		workItems = manager.getUndoneWorkItems(2222);
		assertEquals(workItems.size(), 1);
		currentWorkItem = workItems.get(0);
		currentWorkItem.getProcessInstance().setDataVariable("loop", "false");
		currentWorkItem.complete();
		JPAService.getInstance().commitTransaction();
		
		assertEquals(currentWorkItem.getProcessInstance().getState(), State.SUSPENDED);		
		
		workItems = manager.getUndoneWorkItems(3333);
		assertEquals(workItems.size(), 1);		
	}
}
