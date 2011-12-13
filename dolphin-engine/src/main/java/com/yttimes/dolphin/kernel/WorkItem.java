/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/WorkItem.java,v 1.2.2.16 2011/12/06 07:01:00 neo-cvs Exp $
 * $Revision: 1.2.2.16 $
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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.definition.Activity;

/**
 *
 * @author Neo
 * @version $Id: WorkItem.java,v 1.2.2.16 2011/12/06 07:01:00 neo-cvs Exp $
 */
@Entity
@Table(name = "DOL_WORK_ITEM")
public class WorkItem implements Comparable<WorkItem> {

	public enum State {
		INITIALIZED, RUNNING, CANCELED, COMPLETED
	}

	@Transient
	Logger logger = LoggerFactory.getLogger(WorkItem.class);

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TASK_INS_ID")
	private FormTaskInstance taskInstance;

	@Column(name = "USER_ID", nullable = false)
	private long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATE", nullable = false)
	private State state;

	@Column(name = "STARTED_TIME")
	private Date startedTime;
	
	@Column(name = "CHECKED_TIME")
	private Date checkedTime;

	@Column(name = "END_TIME")
	private Date endTime;
	
	@Column(name = "URGENT_LEVEL")
	private long urgentLevel;

	WorkItem() {
	}

	WorkItem(FormTaskInstance taskInstance, long userId) {
		this.userId = userId;
		this.taskInstance = taskInstance;
		this.state = State.INITIALIZED;
		this.startedTime = new Date();
	}

	public long getId() {
		return id;
	}

	TaskInstance getTaskInstance() {
		return taskInstance;
	}

	public String getTaskName() {
		return taskInstance.getTaskName();
	}

	public String getTaskFormUrl() {
		return taskInstance.getFormUrl();
	}

	public ProcessInstance getProcessInstance() {
		return getTaskInstance().getActivityInstance().getProcessInstance();
	}

	public String getProcessDefId() {
		return getProcessInstance().getProcessDefId();
	}

	public ActivityInstance getActivityInstance() {
		return getTaskInstance().getActivityInstance();
	}

	public long getUserId() {
		return userId;
	}

	void setUserId(long userId) {
		this.userId = userId;
	}

	public State getState() {
		return state;
	}

	void setState(State state) {
		this.state = state;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public Date getEndTime() {
		return endTime;
	}
	
	public long getUrgentLevel() {
		return urgentLevel;
	}

	public void setUrgentLevel(long urgentLevel) {
		this.urgentLevel = urgentLevel;
	}

	public Date getCheckedTime() {
		return checkedTime;
	}

	public void setCheckedTime(Date checkedTime) {
		this.checkedTime = checkedTime;
	}

	public void cancel() {
		setState(State.CANCELED);
	}

	public boolean isCompleted() {
		return State.COMPLETED == getState();
	}

	public synchronized void complete() {
		logger.debug("complete workitem, taskId=" + getTaskInstance().getTaskId());
		if (getState() != State.INITIALIZED) {
			throw new IllegalStateException("cannot complete a workitem under " + getState() + " state");
		}
		setState(State.COMPLETED);
		this.endTime = new Date();
		if (!taskInstance.isCompleted()) {
			taskInstance.complete();
		}
	}

	public void undo() {
		if (getState() != State.COMPLETED) {
			throw new IllegalStateException("cannot undo an uncompleted workitem");
		}
		setState(State.INITIALIZED);
	}

	public List<Activity> getNextManualActivities() {
		return this.getTaskInstance().getProcessInstance().getNextManualActivities(this.getActivityInstance());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((taskInstance == null) ? 0 : taskInstance.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkItem other = (WorkItem) obj;
		if (id != other.id)
			return false;
		if (taskInstance == null) {
			if (other.taskInstance != null)
				return false;
		} else if (!taskInstance.equals(other.taskInstance))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "WorkItem [id=" + id + ", taskInstance=" + taskInstance + ", userId=" + userId + ", state=" + state + ", startedTime="
				+ startedTime + ", checkedTime=" + checkedTime + ", endTime=" + endTime + ", urgentLevel=" + urgentLevel + "]";
	}

	@Override
	public int compareTo(WorkItem o) {		
		return this.startedTime.compareTo(o.startedTime);
	}



}
