/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/com/yttimes/dolphin/play/Attic/Dolphin.java,v 1.1.2.4 2011/11/30 06:27:59 neo-cvs Exp $
 * $Revision: 1.1.2.4 $
 * $Date: 2011/11/30 06:27:59 $
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
package com.yttimes.dolphin.play;

import java.util.List;

import com.yttimes.dolphin.WorkflowManager;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.kernel.ActivityInstance;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.Reflections;


/**
 *
 * @author Neo
 * @version $Id: Dolphin.java,v 1.1.2.4 2011/11/30 06:27:59 neo-cvs Exp $
 */
public class Dolphin {
	public static WorkflowManager workflowManager = new WorkflowManager();
	public static long CONSOLE_USER_ID = 9000L;
	
	public static void runProcess(long instId) throws Throwable {
		try {
			ProcessInstance ins = workflowManager.getProcessInstanceById(instId);
			Reflections.invokeMethod(ins,"run");
		} catch (Exception e) {
			throw e.getCause()==null?e:e.getCause();
		}
	}
	
	public static void runProcess(ProcessInstance ins) throws Throwable {
		try {
			Reflections.invokeMethod(ins,"run");
		} catch (Exception e) {
			throw e.getCause()==null?e:e.getCause();
		}
	}
	
	public static void undoProcess(long processInsId) throws Throwable {
		try {
			ProcessInstance ins = workflowManager.getProcessInstanceById(processInsId);
			Reflections.invokeMethod(ins,"undo");
		} catch (Exception e) {
			throw e.getCause()==null?e:e.getCause();
		}
	}
	
	public static void cancelProcess(long instId) throws Throwable {
		try {
			ProcessInstance ins = workflowManager.getProcessInstanceById(instId);
			Reflections.invokeMethod(ins,"cancel");
		} catch (Exception e) {
			throw e.getCause()==null?e:e.getCause();
		}
	}
	
	public static void assignActivityToUsers(ProcessInstance ins,String activityId, long[] users) throws Throwable {
		try {
			Object[] args = {activityId,users};
			Reflections.invokeMethod(ins,"assignUsers",args);
		} catch (Exception e) {
			throw e.getCause()==null?e:e.getCause();
		}
	}
	
	public static void reAssignWorkItem(WorkItem workItem,long userId) throws Throwable {
		try {
			Object[] args = {userId};
			Reflections.invokeMethod(workItem,"setUserId",args);
		} catch (Exception e) {
			throw e.getCause()==null?e:e.getCause();
		}
	}
	
	public static List<ProcessDefinition> getUsedProcessDefinitions() throws Exception {
		return (List<ProcessDefinition>) Reflections.invokeMethod(workflowManager,"getUsedProcessDefinitions");
	}
	
	public static ProcessInstance createProcessInstance(long processDefId,long userId) throws Exception {
		Object[] args = {processDefId,userId};
		return (ProcessInstance) Reflections.invokeMethod(workflowManager,"createProcessInstance",args,true);
	}
	
	public static List<ProcessInstance> getProcessInstancesByDef(long processDefId) throws Exception {
		Object[] args = {processDefId};
		ProcessDefinition def = (ProcessDefinition) Reflections.invokeMethod(workflowManager,"getProcessDefinition",args,true);
		return def.getProcessInstances();
	}
	
	
	public static List<ProcessDefinition> searchProcessDefinitions(String key) throws Exception {
		Object[] args = {key};
		return (List<ProcessDefinition>) Reflections.invokeMethod(workflowManager,"searchProcessDefinitions",args);
	}	
	
	public static List<ProcessDefinition> getAllProcessDefinitions() throws Exception {
		return (List<ProcessDefinition>) Reflections.invokeMethod(workflowManager,"getAllProcessDefinitions");
	}
	
	public static List<ActivityInstance> getActivities(long processInsId) throws Exception {
		//Object[] args = {processInsId};
		ProcessInstance ins = workflowManager.getProcessInstanceById(processInsId);
		return (List<ActivityInstance>)Reflections.invokeMethod(ins,"getAllActivities");
		//return (List<ActivityInstance>) Reflections.invokeMethod(workflowManager,"getActivities",args,true);		
	}	
	
	
	public static List<WorkItem> getAllWorkItems(long processInsId) throws Exception {
		Object[] args = {processInsId};
		return (List<WorkItem>) Reflections.invokeMethod(workflowManager,"getAllWorkItems",args,true);
	}
	
	public static void saveProcessDefinition(ProcessDefinition processDef) throws Exception {
		Object[] args = {processDef};
		Reflections.invokeMethod(workflowManager,"saveProcessDefinition",args);
	}
	
	public static void deleteProcessDefinition(long  processDefId) throws Exception {
		Object[] args = {processDefId};
		Reflections.invokeMethod(workflowManager,"deleteProcessDefinition",args,true);
	}
	
	public static void deleteProcessInstance(long  processInsId) throws Exception {
		Object[] args = {processInsId};
		Reflections.invokeMethod(workflowManager,"deleteProcessInstance",args,true);
	}
	
	
	
}
