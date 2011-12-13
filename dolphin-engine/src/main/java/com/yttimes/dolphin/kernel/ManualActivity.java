/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/ManualActivity.java,v 1.2.2.17 2011/12/06 07:01:00 neo-cvs Exp $
 * $Revision: 1.2.2.17 $
 * $Date: 2011/12/06 07:01:00 $
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

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.yttimes.dolphin.definition.Activity;
import com.yttimes.dolphin.definition.Task;
import com.yttimes.dolphin.exception.AssignmentException;
import com.yttimes.dolphin.support.Reflections;

/**
 *
 * @author Neo
 * @version $Id: ManualActivity.java,v 1.2.2.17 2011/12/06 07:01:00 neo-cvs Exp $
 */

@Entity
@DiscriminatorValue("MANUAL")
public class ManualActivity extends TaskActivityInstance {

	ManualActivity() {
	}

	ManualActivity(ProcessInstance processInst, Activity activityDef) {
		super(processInst, activityDef);
	}

	public boolean execute() {
		logger.info("activity " + this.getActivityId() + " is executing..");
		logger.debug("activity state:" + getState());
		logger.debug("activity type: Manual");

		this.updatedTime = new Date();
		if (this.processInst.isHalted()) {
			setState(State.CANCELD);
			return true;
		}

		if (getState() == State.UNACTIVATED) {
			throw new IllegalStateException("cannot execute unactivated activity!");
		} else if (getState() == State.ACTIVATED) {
			setState(State.SUSPENDED);
		} else if (getState() == State.RUNNING) {
			setState(State.TERMINATED);
			getProcessInstance().activateNextActivities(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean complete() {
		for (TaskInstance taskInstance : taskInstances) {
			if (!taskInstance.isCompleted())
				return false;
		}
		this.resume();
		return true;
	}

	public void terminate() {
		for (TaskInstance taskInstance : taskInstances) {
			if (!taskInstance.isCompleted())
				taskInstance.terminate();
		}
		setState(State.RUNNING);
	}

	@Override
	public void undo() {
		logger.info("undo activity " + this.getActivityId());
		if (getState() == State.TERMINATED) {
			for (TaskInstance taskInstance : taskInstances) {
				if (!taskInstance.isCompleted())
					throw new IllegalStateException("cannot undo activity with uncompleted task, task id=" + taskInstance.getTaskId());
				taskInstance.undo();
			}
			setState(State.ACTIVATED);
		} else {
			throw new IllegalStateException("cannot undo an activity not terminated");
		}
	}

	public AssignmentHandler getAssignmentHandler() {
		for (TaskInstance taskIns : taskInstances) {
			if (taskIns instanceof FormTaskInstance) {
				return ((FormTaskInstance)taskIns).getAssignmentHandler();
			}
		}
		return null;
	}
	


	@Override
	TaskInstance createTaskInstance(Task taskDef) {
		FormTaskInstance formTaskInst = new FormTaskInstance(this, taskDef);
		//logger.debug("Task :"+taskDef);

		formTaskInst.setFormUrl(taskDef.getFormURL());
		String assignment = taskDef.getAssignment();

		//get assignment handler
		if (assignment != null && !assignment.isEmpty()) {
			if (Task.isUserAssignment(assignment)) {
				String[] users = assignment.split(",");
				final long[] userIds = new long[users.length];
				int i = 0;
				for (String user : users) {
					userIds[i++] = Long.parseLong(user);
					//logger.debug(user);
				}
				formTaskInst.assignToUsers(userIds);
			} else if(Task.AssignmentPattern.isMember(assignment)) {
				formTaskInst.setAssignmentHandler(new PatternAssignmentHandler(assignment));
			} else {
				Object assignmentObj;
				try {
					assignmentObj = Reflections.newObject(assignment);
					if (assignmentObj instanceof AssignmentHandler) {
						formTaskInst.setAssignmentHandler((AssignmentHandler) assignmentObj);
					} else {
						throw new AssignmentException("this class must implement AssignmentHandler");
					}
				} catch (Exception e) {
					logger.warn("AssignmentHandler class error!");
					//throw new AssignmentException("AssignmentHandler class error!",e);
				}
			}

		}

		//if we got no assignment in xml then assign task to process creator
//		if (formTaskInst.getAssignmentHandler() == null) {
//			long[] userIds = { processInst.getCreatorId() };
//			formTaskInst.assignToUsers(userIds);
//		}

		return formTaskInst;
	}



}
