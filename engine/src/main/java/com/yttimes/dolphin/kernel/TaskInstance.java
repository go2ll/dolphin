/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/TaskInstance.java,v 1.2.2.6 2011/10/09 02:15:54 neo-cvs Exp $
 * $Revision: 1.2.2.6 $
 * $Date: 2011/10/09 02:15:54 $
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

import com.yttimes.dolphin.definition.Task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Neo
 * @version $Id: TaskInstance.java,v 1.2.2.6 2011/10/09 02:15:54 neo-cvs Exp $
 */
@Entity
@Table(name = "DOL_TASK_INSTANCE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "TYPE",
    discriminatorType = DiscriminatorType.STRING
)
public abstract class TaskInstance {

  @Id
  @GeneratedValue
  @Column(name = "ID", nullable = false)
  private long id;

  @Column(name = "TASK_ID")
  private String taskId;

  @Column(name = "LIMIT_TIME")
  private long limitTime;

  @Column(name = "CREATE_TIME")
  private Date createTime;

  @Column(name = "START_TIME")
  private Date startTime;

  @Column(name = "END_TIME")
  private Date endTime;

  @Column(name = "FINAL_TIME")
  private Date finalTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATE")
  private State state;

  @ManyToOne
  @JoinColumn(name = "ACTIVITY_INS_ID")
  private ActivityInstance activityInstance;


  TaskInstance() {
  }

  TaskInstance(ActivityInstance activityInstance, Task task) {
    this.activityInstance = activityInstance;
    this.taskId = task.getId();
    this.createTime = new Date();
    this.state = State.INITIALIZED;
  }

  public Task getDefinition() {
    return this.activityInstance.getDefinition().getTask(this.taskId);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getTaskName() {
    return this.getDefinition().getName();
  }


  public ActivityInstance getActivityInstance() {
    return activityInstance;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public long getLimitTime() {
    return limitTime;
  }

  public void setLimitTime(long limitTime) {
    this.limitTime = limitTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getFinalTime() {
    return finalTime;
  }

  public void setFinalTime(Date finalTime) {
    this.finalTime = finalTime;
  }

  public ProcessInstance getProcessInstance() {
    return this.activityInstance.getProcessInstance();
  }


  public abstract void start();

  public abstract void undo();

  public abstract void complete();

  public abstract void terminate();

  public boolean isCompleted() {
    return this.state == State.COMPLETED;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((activityInstance == null) ? 0 : activityInstance.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TaskInstance other = (TaskInstance) obj;
    if (activityInstance == null) {
      if (other.activityInstance != null) {
        return false;
      }
    } else if (!activityInstance.equals(other.activityInstance)) {
      return false;
    }
    if (taskId == null) {
      if (other.taskId != null) {
        return false;
      }
    } else if (!taskId.equals(other.taskId)) {
      return false;
    }
    if (id != other.id) {
      return false;
    }
    return true;
  }

  public enum State {
    INITIALIZED, RUNNING, SUSPENDED, CANCELED, COMPLETED
  }

}
