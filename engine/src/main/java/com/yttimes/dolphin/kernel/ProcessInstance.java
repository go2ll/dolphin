package com.yttimes.dolphin.kernel;

import com.yttimes.dolphin.definition.Activity;
import com.yttimes.dolphin.definition.Activity.JoinType;
import com.yttimes.dolphin.definition.Activity.Type;
import com.yttimes.dolphin.definition.DefinitionCache;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.definition.Transition;
import com.yttimes.dolphin.exception.ConfusedTransitionException;
import com.yttimes.dolphin.exception.DolphinException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "DOL_PROCESS_INSTANCE")
public class ProcessInstance implements Comparable<ProcessInstance> {

  @Transient
  Logger logger = LoggerFactory.getLogger(ProcessInstance.class);

  @Transient
  public static String NEXT_ACTIVITY = "PROCESS_NEXT_ACTIVITY";

  @Id
  @GeneratedValue
  @Column(name = "ID", nullable = false)
  private long id;

  @ManyToOne
  @JoinColumn(name = "PROCESS_DEFINITION_ID")
  private ProcessDefinition processDef;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATE")
  private State state;

  @OneToMany(mappedBy = "processInst", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();

  @Transient
  private Map<String, AssignmentHandler>
      dynamicAssignmentMap =
      new HashMap<String, AssignmentHandler>();

  @Transient
  private boolean undoMode = false;

  @Transient
  private int urgentLevel;

  @Column(name = "CREATOR_ID")
  private long creatorId;

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "END_TIME")
  private Date endTime;

  @Column(name = "EXPIRED_TIME")
  private Date expiredTime;

  @Column(name = "PRIORITY")
  private int priority;

  @ElementCollection
  @CollectionTable(name = "DOL_PROCESS_VARIABLE")
  @PrimaryKeyJoinColumn(name = "id")
  @MapKeyColumn(name = "VARIABLE_NAME")
  @Column(name = "VARIABLE_VALUE")
  private Map<String, String> processDataFields = new HashMap<String, String>();

  ProcessInstance() {
  }

  public ProcessInstance(ProcessDefinition processDef, long creatorId) {
    this.processDef = processDef;
    this.creatorId = creatorId;
    this.createdTime = new Date();
    this.updatedTime = new Date();
    this.state = State.INITIALIZED;
  }

  public ProcessDefinition getProcessDefinition() {
    //logger.debug("ProcessInstance getProcessDefinition,processDef id="+this.processDef.getId());
    ProcessDefinition def = DefinitionCache.getProcessDefinition(this.processDef.getId());
    if (def == null || def.getStartActivity() == null) {
      def = processDef;
      def.rebuild();
      DefinitionCache.put(def);
    }
    return def;
  }

  public State getState() {
    return state;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public Date getUpdatedTime() {
    return updatedTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public Date getExpiredTime() {
    return expiredTime;
  }

  public long getId() {
    return id;
  }

  public String getProcessDefId() {
    return getProcessDefinition().getProcessId();
  }

  public double getProcessVersion() {
    return getProcessDefinition().getVersion();
  }

  public long getCreatorId() {
    return creatorId;
  }

  public int getPriority() {
    return priority;
  }

  public int getUrgentLevel() {
    return urgentLevel;
  }

  public void setUrgentLevel(int urgentLevel) {
    this.urgentLevel = urgentLevel;
  }

  public Object getDataVariable(String fieldId) {
    return processDataFields.get(fieldId);
  }

  public Map<String, String> getDataVariableMap() {
    return processDataFields;
  }

  public void setDataVariable(String fieldId, String value) {
    processDataFields.put(fieldId, value);
  }

  public void setNextActivity(String nextActivityId) {
    setDataVariable(NEXT_ACTIVITY, nextActivityId);
  }

  public void clearDataVariables() {
    this.processDataFields.clear();
  }

  public Map<String, AssignmentHandler> getDynamicAssignmentMap() {
    return this.dynamicAssignmentMap;
  }

  AssignmentHandler getDynamicAssignmentHandler(String activityId) {
    return dynamicAssignmentMap.get(activityId);
  }

  void setDynamicAssignmentHandler(String activityId, AssignmentHandler dynamicHandler) {
    dynamicAssignmentMap.put(activityId, dynamicHandler);
  }

  void assignUsers(String activityId, final long[] userIds) {
    logger.debug("dynamic assign " + activityId + " to user " + Arrays.asList(userIds));
    setDynamicAssignmentHandler(activityId, new AssignmentHandler() {
      public long[] findUserIds(TaskInstance taskInstance) {
        return userIds;
      }

      public boolean isOptional() {
        return false;
      }
    });
  }

  /*
   * this operation assign users to any activity to be executed next.
   */
  public void assignUsersToNextActivity(final long[] userIds) {
    assignUsers("DOL_ANY", userIds);
  }

  public void assignUsersToNextActivity(String usersStr) {
    String[] users = usersStr.split(",");
    long[] userIds = new long[users.length];
    int i = 0;
    for (String user : users) {
      userIds[i++] = Long.parseLong(user);
    }
    assignUsersToNextActivity(userIds);
  }

  public boolean isHalted() {
    return getState() == State.CANCELED || getState() == State.TERMINATED;
  }

  public boolean isUndoable() {
    if (getState() != State.SUSPENDED) {
      return false;
    } else {
      List<ActivityInstance> suspendedActivities = new ArrayList<ActivityInstance>();
      for (ActivityInstance activityIns : activityInstances) {
        if (activityIns.getState() == ActivityInstance.State.SUSPENDED) {
          suspendedActivities.add(activityIns);
        }
      }
      if (suspendedActivities.size() != 1) {
        return false;
      }
      ActivityInstance currentActivity = suspendedActivities.get(0);
      List<String>
          preActiviesId =
          getProcessDefinition().getPreviousActivities(currentActivity.getActivityId());
      for (String activityId : preActiviesId) {
        ActivityInstance preActivityInstance = getLatestActivityInstance(activityId);
        if (preActivityInstance != null && !(preActivityInstance instanceof ManualActivity)) {
          return false;
        }
      }
    }
    return true;
  }

  public void start() {
    logger.debug("start process instance...");
    if (getState() == State.INITIALIZED) {
      this.state = State.RUNNING;
    } else {
      throw new IllegalStateException("Process " + getProcessDefId() + " is not ready for start!");
    }
    ActivityInstance
        startNode =
        createActivityInstance(getProcessDefinition().getStartActivity().getId());
    startNode.start();
    logger.debug("start process ");
    execute();
  }

  void run() {
    if (getState() == State.SUSPENDED) {
      resumeAll();
    } else if (getState() == State.INITIALIZED) {
      start();
    } else {
      throw new IllegalStateException("Process " + getProcessDefId() + " cannot  run!");
    }
  }

  void undo() {
    logger.debug("undo current process instance...");
    if (getState() == State.INITIALIZED || getState() == State.CANCELED
        || getState() == State.TERMINATED) {
      throw new IllegalStateException("cannot undo a process under " + getState() + " state");
    } else {
      this.undoMode = true;
      resumeAll();
    }
  }

  void resume(String activityId) {
    logger.debug("resume process(" + this.id + ") current activity=" + activityId);
    if (getState() == State.SUSPENDED) {
      this.state = State.RUNNING;
    }
    ActivityInstance currentActivity = getActiveActivityInstance(activityId);
    if (currentActivity == null) {
      logger.debug("process activatedActivities:" + activityInstances);
      throw new IllegalArgumentException(
          "cannot find activity in current process's activatedActivities,activityId=" + activityId);
    }
    currentActivity.setState(ActivityInstance.State.RUNNING);
    execute();
  }

  private ActivityInstance createActivityInstance(String activityId) {
    logger.debug("create new activity instance " + activityId);
    ActivityInstance instance = ActivityInstanceFactory.createInstance(this, activityId);
    return instance;
  }

  private void removeActivityInstance(ActivityInstance activityIns) {
    logger.debug("remove activity instance:" + activityIns);
    this.activityInstances.remove(activityIns);
  }

  private ActivityInstance getLatestActivityInstance(String activityId) {
    List<ActivityInstance> results = new ArrayList<ActivityInstance>();
    for (ActivityInstance ins : this.activityInstances) {
      if (activityId.equals(ins.getActivityId())) {
        results.add(ins);
      }
    }
    if (results.size() == 0) {
      return null;
    }
    Collections.sort(results);
    return results.get(0);
  }

  private ActivityInstance getActiveActivityInstance(String activityId) {
    List<ActivityInstance> results = new ArrayList<ActivityInstance>();
    for (ActivityInstance ins : this.activityInstances) {
      if (activityId.equals(ins.getActivityId()) && !ins.ishalted()) {
        results.add(ins);
      }
    }
    if (results.size() > 1) {
      throw new DolphinException(
          "cannot have more than one activity instance with same activity id");
    }
    if (results.size() == 0) {
      return null;
    }
    return results.get(0);
  }

  void resumeAll() {
    logger.debug("resume process instance ...");
    if (getState() == State.SUSPENDED) {
      this.state = State.RUNNING;
      for (ActivityInstance activityIns : activityInstances) {
        if (activityIns.getState() == ActivityInstance.State.SUSPENDED) {
          activityIns.terminate();
        }
      }
    } else {
      throw new IllegalStateException("Process " + getProcessDefId() + " is not ready for resume!");
    }
    execute();
  }

  void cancel() {
    logger.debug("cancel process instance...");
    if (getState() == State.RUNNING || getState() == State.INITIALIZED
        || getState() == State.SUSPENDED) {
      this.state = State.CANCELED;
    } else {
      throw new IllegalStateException("Process " + getProcessDefId() + " is not ready for cancel!");
    }
    this.updatedTime = new Date();
    this.endTime = new Date();
    //this.activityInstances.clear();
  }

  void end() {
    logger.debug("end process instance...");
    if (getState() == State.RUNNING || getState() == State.INITIALIZED
        || getState() == State.SUSPENDED) {
      this.state = State.TERMINATED;
    } else {
      throw new IllegalStateException("Process " + getProcessDefId() + " is not ready for end!");
    }
    this.updatedTime = new Date();
    this.endTime = new Date();
    execute();
  }

  void activateActivity(ActivityInstance activityins) {
    logger.debug("activate process activity " + activityins.getActivityId() + "...");
    //logger.debug("activity state:" + activity.getState());
    if (isActivityReady(activityins)) {
      logger.debug("activity is activated...");
      activityins.setState(ActivityInstance.State.ACTIVATED);
      activityInstances.add(activityins);
    }
  }

  private boolean isActivityReady(ActivityInstance activity) {
    if (activity.getState() == ActivityInstance.State.CANCELD
        || activity.getState() == ActivityInstance.State.TERMINATED) {
      return false;
    }
    if (activity instanceof StartActivity) {
      logger.debug("activity is start node..");
      return true;
    } else {
      List<String>
          activitiesId =
          getProcessDefinition().getPreviousActivities(activity.getActivityId());
      logger.debug("activity joinType:" + activity.getJoinType());
      logger.debug(
          "getPreviousActivities of activity " + activity.getActivityId() + ":" + activitiesId);
      if (activity.getJoinType() == JoinType.AND) {
        for (String activityId : activitiesId) {
          ActivityInstance instance = getLatestActivityInstance(activityId);
          if (instance != null && instance.getState() != ActivityInstance.State.TERMINATED) {
            logger.debug("we got activity not terminated,activityId=" + activityId);
            return false;
          }
        }
        return true;
      } else if (activity.getJoinType() == JoinType.OR) {
        for (String activityId : activitiesId) {
          ActivityInstance instance = getLatestActivityInstance(activityId);
          logger.debug("pre activity instance:" + instance);
          if (instance != null && instance.getState() == ActivityInstance.State.TERMINATED) {
            logger.debug("we got activity terminated,activityId=" + activityId);
            return true;
          }
        }
      }
    }
    return false;
  }

  void activateNextActivities(ActivityInstance activityIns) {
    if (undoMode) {
      logger.warn("activate Next Activities under undo mode....");
      List<Transition>
          transitions =
          getProcessDefinition().getPreviousTransitions(activityIns.getActivityId());
      for (Transition transition : transitions) {
        String fromActivityId = transition.fromActivity();
        if (transition.validate(processDataFields)) {
          ActivityInstance activityInstance = getLatestActivityInstance(fromActivityId);
          if (activityInstance == null) {
            throw new IllegalStateException(
                "cannot find an already executed activity instance,id=" + fromActivityId);
          }
          if (activityInstance instanceof ManualActivity) {
            removeActivityInstance(activityIns);
            activityInstance.undo();
          } else {
            throw new IllegalStateException(
                "cannot undo process instance,preivious activity is not manual");
          }
        }
      }
      this.undoMode = false;
    } else {
      logger.debug("activate next activities of activity " + activityIns.getActivityId());
      List<Transition>
          transitions =
          getProcessDefinition().getNextTransitions(activityIns.getActivityId());
      List<ActivityInstance> nextActivities = new ArrayList<ActivityInstance>();
      for (Transition transition : transitions) {
        String toActivityId = transition.toActivity();
        if (transition.validate(processDataFields)) {
          ActivityInstance activityInstance = getActiveActivityInstance(toActivityId);
          if (activityInstance == null || activityInstance.ishalted()) {
            activityInstance = createActivityInstance(toActivityId);
          }
          if (activityInstance.getState() != ActivityInstance.State.TERMINATED) {
            nextActivities.add(activityInstance);
          }
        }
      }
      if (nextActivities.size() == 0) {
        throw new DolphinException(
            "cannot find next activity for " + activityIns.getActivityId() + "!processDataFields="
            + processDataFields);
      } else if (!activityIns.isSplitedToNext() && nextActivities.size() > 1) {
        throw new ConfusedTransitionException(
            "Too many transactions valid,size=" + nextActivities.size(), nextActivities);
      } else {
        for (ActivityInstance instance : nextActivities) {
          instance.start();
        }
      }
    }
    this.setNextActivity(null);
    execute();
  }

  public List<ActivityInstance> getSuspendedActivities() {
    if (getState() != State.SUSPENDED) {
      throw new IllegalStateException("process state is not suspended! state=" + getState());
    }
    List<ActivityInstance> suspendedActivities = new ArrayList<ActivityInstance>(1);
    List<ActivityInstance> activatedActivities = new ArrayList<ActivityInstance>(activityInstances);
    for (ActivityInstance activityIns : activatedActivities) {
      if (activityIns.getState() == ActivityInstance.State.SUSPENDED) {
        suspendedActivities.add(activityIns);
      }
    }
    return suspendedActivities;
  }

  List<ActivityInstance> getAllActivities() {
    return this.activityInstances;
  }

  public List<Activity> getNextManualActivities(ActivityInstance activityInstance) {
    if (activityInstance.getState() != ActivityInstance.State.SUSPENDED) {
      throw new IllegalStateException("cannot get next performers when activity is nnot suspended");
    }
    List<Activity> nextManualActivities = new ArrayList<Activity>();
    List<Transition>
        transitions =
        getProcessDefinition().getNextTransitions(activityInstance.getActivityId());
    for (Transition transition : transitions) {
      String toActivityId = transition.toActivity();
      if (transition.validate(processDataFields)) {
        Activity activity = getProcessDefinition().getActivity(toActivityId);
        if (activity.getType() == Type.MANUAL) {
          nextManualActivities.add(activity);
        }
      }
    }
    return nextManualActivities;
  }

  private void execute() {
    logger.debug("process " + this.getProcessDefinition().getProcessId() + " is executing...");
    logger.debug("process state:" + this.getState());
    logger.debug("activityInstances:" + activityInstances);

    List<ActivityInstance> activatedActivities = new ArrayList<ActivityInstance>(activityInstances);
    for (ActivityInstance activityIns : activatedActivities) {
      if (activityIns.getState() == ActivityInstance.State.TERMINATED) {
        continue;
      } else {
        if (activityIns.execute()) {
          logger.debug("activity is executed to end");
        } else {
          logger.debug("activity is not executed to end");
        }
      }
    }
    //clear all the dynamic user assignments
    this.dynamicAssignmentMap.clear();
    if (getState() != State.CANCELED && getState() != State.TERMINATED) {
      this.state = State.SUSPENDED;
      this.updatedTime = new Date();
    }
  }

  public enum State {
    INITIALIZED, RUNNING, SUSPENDED, CANCELED, TERMINATED
  }

  @Override
  public int compareTo(ProcessInstance o) {
    int result = this.urgentLevel - o.urgentLevel;
    if (result == 0) {
      return this.getUpdatedTime().compareTo(o.getUpdatedTime());
    } else {
      return result;
    }
  }

}
