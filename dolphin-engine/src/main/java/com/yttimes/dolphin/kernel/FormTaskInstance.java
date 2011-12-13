/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/FormTaskInstance.java,v 1.2.2.11 2011/12/05 03:12:26 neo-cvs Exp $
 * $Revision: 1.2.2.11 $
 * $Date: 2011/12/05 03:12:26 $
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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.definition.Task;
import com.yttimes.dolphin.exception.AssignmentException;

/**
 * 
 * @author Neo
 * @version $Id: FormTaskInstance.java,v 1.2.2.5 2011/09/16 08:46:28 neo-cvs Exp
 *          $
 */
@Entity
@DiscriminatorValue("FORM")
public class FormTaskInstance extends TaskInstance {
	@Transient
	Logger logger = LoggerFactory.getLogger(FormTaskInstance.class);

	@OneToMany(mappedBy = "taskInstance", cascade = CascadeType.ALL)
	private Set<WorkItem> assignedWorkItems = new HashSet<WorkItem>();

	@Column(name = "FORM_URL")
	private String formUrl;

	@Transient
	AssignmentHandler assignmentHandler;

	FormTaskInstance() {

	}

	/**
	 * @param activityInstance
	 * @param task
	 */
	FormTaskInstance(ActivityInstance activityInstance, Task task) {
		super(activityInstance, task);
	}

	public CompletedStrategy getTaskCompleteStrategy() {
		if ("ALL".equalsIgnoreCase(getDefinition().getStrategy())) {
			return CompletedStrategy.ALL;
		}
		// default complete strategy is ANY
		return CompletedStrategy.ANY;
	}

	public Set<WorkItem> getAssignedWorkItems() {
		return assignedWorkItems;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	public void assignToUser(long userid) {
		logger.debug("assign task " + getTaskId() + " to user " + userid);
		logger.debug("task form url:" + getFormUrl());
		WorkItem wi = new WorkItem(this, userid);
		wi.setUrgentLevel(this.getActivityInstance().getProcessInstance().getUrgentLevel());
		logger.debug("create new work item:" + wi);
		assignedWorkItems.add(wi);
	}

	public AssignmentHandler getAssignmentHandler() {
		return assignmentHandler;
	}

	public void setAssignmentHandler(AssignmentHandler assignmentHandler) {
		this.assignmentHandler = assignmentHandler;
	}

	public void assignToUsers(final long[] users) {
		AssignmentHandler handler = new AssignmentHandler() {
			public long[] findUserIds(TaskInstance taskInstance) {
				return users;
			}

			public boolean isOptional() {
				return false;
			}
		};
		setAssignmentHandler(handler);
	}

	@Override
	public void complete() {
		logger.debug("task complete strategy is " + getTaskCompleteStrategy());
		if (getTaskCompleteStrategy().isTaskCompleted(assignedWorkItems)) {
			setState(State.COMPLETED);
			for (WorkItem wi : this.assignedWorkItems) {
				if (wi.getState() != WorkItem.State.CANCELED && wi.getState() != WorkItem.State.COMPLETED) {
					wi.cancel();
				}
			}
			getActivityInstance().complete();
		} else {
			logger.debug("task is suspended for unfinished workitems!");
			setState(State.SUSPENDED);
		}
	}

	@Override
	public void start() {
		logger.debug("start formtask,task id=" + getTaskId());
		AssignmentHandler dynamicHandler = this.getProcessInstance()
				.getDynamicAssignmentHandler(this.getActivityInstance().getActivityId());
		if (dynamicHandler == null) {
			dynamicHandler = this.getProcessInstance().getDynamicAssignmentHandler("DOL_ANY");
		}
		if (dynamicHandler != null) {
			logger.info("we got dynamic handler!");
			this.setAssignmentHandler(dynamicHandler);
		}
		if (getAssignmentHandler() == null) {
			throw new AssignmentException("found no assignment handler!", this);
		} else {
			if (getAssignmentHandler().isOptional()&&!(getAssignmentHandler() instanceof PatternAssignmentHandler)) {
				throw new AssignmentException(null, getAssignmentHandler().findUserIds(this));
			}
			for (long userId : getAssignmentHandler().findUserIds(this)) {
				assignToUser(userId);
			}
		}
	}

	@Override
	public void undo() {
		setState(State.INITIALIZED);
		for (WorkItem wi : this.assignedWorkItems) {
			if (wi.getState() != WorkItem.State.COMPLETED) {
				throw new IllegalStateException("cannot undo task instance with workitem not completed!");
			}
			wi.undo();
		}

	}

	@Override
	public void terminate() {
		setState(State.COMPLETED);
		for (WorkItem wi : this.assignedWorkItems) {
			if (wi.getState() != WorkItem.State.COMPLETED) {
				wi.setState(WorkItem.State.COMPLETED);
			}
		}
	}

	public enum CompletedStrategy {
		ANY {
			public boolean isTaskCompleted(Set<WorkItem> workItems) {
				for (WorkItem wi : workItems) {
					if (wi.isCompleted())
						return true;
				}
				return false;
			}
		},
		ALL {
			public boolean isTaskCompleted(Set<WorkItem> workItems) {
				for (WorkItem wi : workItems) {
					if (!wi.isCompleted())
						return false;
				}
				return true;
			}
		};
		public abstract boolean isTaskCompleted(Set<WorkItem> workItems);
	}

}
