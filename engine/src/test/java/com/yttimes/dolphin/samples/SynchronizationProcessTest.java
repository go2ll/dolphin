/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/samples/Attic/SynchronizationProcessTest.java,v 1.1.4.9 2011/12/02 07:23:37 neo-cvs Exp $
 * $Revision: 1.1.4.9 $
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

import org.junit.Test;

import com.yttimes.dolphin.ProcessSample;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.ProcessInstance.State;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: SynchronizationProcessTest.java,v 1.1.4.9 2011/12/02 07:23:37 neo-cvs Exp $
 */
public class SynchronizationProcessTest extends ProcessSample {
	@Test
	public void testSynchronizationProcess() {
		System.out.println("test Synchronization Process..");
		JPAService.getInstance().beginTransaction();
		ProcessInstance processIns = manager.createProcessInstance("leave_synchronization", 1000);
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
		currentWorkItem.complete();
		assertEquals(processIns.getState(), State.SUSPENDED);
		JPAService.getInstance().commitTransaction();

		JPAService.getInstance().beginTransaction();
		workItems = manager.getUndoneWorkItems(3333);
		currentWorkItem = workItems.get(0);
		currentWorkItem.complete();
		assertEquals(processIns.getState(), State.TERMINATED);
		JPAService.getInstance().commitTransaction();
	}
}
