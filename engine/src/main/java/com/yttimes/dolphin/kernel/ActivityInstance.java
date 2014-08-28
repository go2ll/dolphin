/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/ActivityInstance.java,v 1.2.2.14 2011/12/06 07:01:00 neo-cvs Exp $
 * $Revision: 1.2.2.14 $
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

import com.yttimes.dolphin.definition.Activity;
import com.yttimes.dolphin.definition.Activity.JoinType;
import com.yttimes.dolphin.definition.Activity.Type;
import com.yttimes.dolphin.exception.DolphinException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Neo
 * @version $Id: ActivityInstance.java,v 1.2.2.14 2011/12/06 07:01:00 neo-cvs Exp $
 */
@Entity
@Table(name = "DOL_ACTIVITY_INSTANCE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "TYPE",
    discriminatorType = DiscriminatorType.STRING
)
public abstract class ActivityInstance implements Comparable<ActivityInstance> {

  public enum State {
    UNACTIVATED, ACTIVATED, RUNNING, SUSPENDED, CANCELD, TERMINATED
  }

  @Transient
  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  @Id
  @GeneratedValue
  @Column(name = "ID", nullable = false)
  private long id;

  @Column(name = "UPDATED_TIME")
  protected Date updatedTime;

  @Column(name = "SUPERVISOR")
  private long supervisorId;

  @ManyToOne
  @JoinColumn(name = "PROCESS_INS_ID")
  protected ProcessInstance processInst;

  @Column(name = "ACTIVITY_ID")
  private String activityId;

  @Enumerated(EnumType.STRING)
  @Column(name = "ACTIVITY_STATE")
  private State state;

  @OneToMany(mappedBy = "activityInstance", cascade = CascadeType.ALL)
  protected List<TaskInstance> taskInstances = new ArrayList<TaskInstance>();

  ActivityInstance() {
  }

  ActivityInstance(ProcessInstance processInst, Activity activityDef) {
    this.processInst = processInst;
    this.activityId = activityDef.getId();
    setState(State.UNACTIVATED);
  }

  public Activity getDefinition() {
    return this.processInst.getProcessDefinition().getActivity(activityId);
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return this.getDefinition().getName();
  }

  public Date getUpdatedTime() {
    return updatedTime;
  }

  public String getActivityId() {
    return activityId;
  }

  public ProcessInstance getProcessInstance() {
    return processInst;
  }

  public State getState() {
    return this.state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public boolean ishalted() {
    return this.state == State.TERMINATED || this.state == State.CANCELD;
  }

  public Type getType() {
    return this.getDefinition().getType();
  }

  public long getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(long supervisorId) {
    this.supervisorId = supervisorId;
  }

  public JoinType getJoinType() {
    return this.getDefinition().getJoinType();
  }

  public boolean isSplitedToNext() {
    return this.getDefinition().isSplit();
  }

  public void start() {
    if (getState() == State.UNACTIVATED) {
      this.processInst.activateActivity(this);
    } else {
      throw new DolphinException("Activity " + getActivityId() + " not initialized!");
    }
  }

  public abstract void undo();

  public abstract boolean execute();

  public abstract void terminate();

  public void cancel() {
    if (getState() == State.SUSPENDED || getState() == State.RUNNING) {
      setState(State.CANCELD);
    } else {
      throw new DolphinException(
          "Activity " + getActivityId() + " is not ready to be canceld, state=" + getState());
    }
  }

  public void resume() {
    this.processInst.resume(getActivityId());
  }

  //called from client
  public boolean complete() {
    throw new DolphinException("cannot complete an activity which is not manual");
  }

  public int compareTo(ActivityInstance ins) {
    return this.updatedTime.compareTo(ins.updatedTime);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((activityId == null) ? 0 : activityId.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((state == null) ? 0 : state.hashCode());
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
    ActivityInstance other = (ActivityInstance) obj;
    if (activityId == null) {
      if (other.activityId != null) {
        return false;
      }
    } else if (!activityId.equals(other.activityId)) {
      return false;
    }
    if (id != other.id) {
      return false;
    }
    if (state != other.state) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ActivityInstance [id=" + getId() + ", actId=" + getActivityId() + ", type=" + getType()
           + ", state=" + state + "]";
  }
}
