/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/Attic/DefinitionValidator.java,v 1.1.2.2 2011/10/12 06:25:57 neo-cvs Exp $
 * $Revision: 1.1.2.2 $
 * $Date: 2011/10/12 06:25:57 $
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
package com.yttimes.dolphin.definition;

import com.yttimes.dolphin.exception.InvalidDefinitionException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neo
 * @version $Id: DefinitionValidator.java,v 1.1.2.2 2011/10/12 06:25:57 neo-cvs Exp $
 */
public class DefinitionValidator {

  public void validate(ProcessDefinition definition) {
    validateStartActivity(definition);
    validateEndActivity(definition);
    validateTransitions(definition);
    validateActivities(definition);
    validateTransitions(definition);
  }

  private void validateStartActivity(ProcessDefinition definition) {
    checkIfHasStartActivity(definition);
    checkStartActivity(definition);
  }

  private void checkStartActivity(ProcessDefinition definition) {
    Activity activity = definition.getStartActivity();
    if (activity.getType() != Activity.Type.START) {
      throw new InvalidDefinitionException("Invalid start activity!");
    }
    if (ifHasTransitionTo(definition, activity)) {
      throw new InvalidDefinitionException("Transition to start activity " + activity.getId());
    }
    if (!ifHasTransitionFrom(definition, activity)) {
      throw new InvalidDefinitionException("No transition from start activity " + activity.getId());
    }
  }

  private boolean ifHasTransitionTo(ProcessDefinition definition, Activity activity) {
    for (Transition transition : definition.getTransitions()) {
      if (transition.toActivity().equals(activity.getId())) {
        return true;
      }
    }
    return false;
  }

  private boolean ifHasTransitionFrom(ProcessDefinition definition, Activity activity) {
    for (Transition transition : definition.getTransitions()) {
      if (transition.fromActivity().equals(activity.getId())) {
        return true;
      }
    }
    return false;
  }

  private void validateEndActivity(ProcessDefinition definition) {
    checkIfHasEndActivity(definition);
    for (Activity activity : definition.getEndActivities()) {
      checkEndActivity(definition, activity);
    }
  }

  private void checkEndActivity(ProcessDefinition definition, Activity activity) {
    if (activity.getType() != Activity.Type.END) {
      throw new InvalidDefinitionException("Invalid end activity!");
    }
    if (ifHasTransitionFrom(definition, activity)) {
      throw new InvalidDefinitionException("Transition from end activity " + activity.getId());
    }
    if (!ifHasTransitionTo(definition, activity)) {
      throw new InvalidDefinitionException("No transition to end activity " + activity.getId());
    }
  }

  private void checkIfHasStartActivity(ProcessDefinition definition) {
    if (definition.getStartActivity() == null) {
      throw new InvalidDefinitionException("No start activity!");
    }
  }

  private void checkIfHasEndActivity(ProcessDefinition definition) {
    if (definition.getEndActivities() == null || definition.getEndActivities().isEmpty()) {
      throw new InvalidDefinitionException("No end activity!");
    }
  }

  private void checkIfHasTransition(ProcessDefinition definition) {
    if (definition.getTransitions() == null || definition.getTransitions().isEmpty()) {
      throw new InvalidDefinitionException("No transition!");
    }
  }

  private void validateActivities(ProcessDefinition definition) {
    for (Activity activity : definition.getActivities()) {
      validateActivity(activity);
    }
  }

  private void validateTransitions(ProcessDefinition definition) {
    checkIfHasTransition(definition);
    for (Transition transition : definition.getTransitions()) {
      validateTransition(definition, transition);
    }
  }

  private void validateActivity(Activity activity) {
    switch (activity.getType()) {
      case MANUAL:
        validateManualActivity(activity);
        break;
      case ROUTE:
        validateRouteActivity(activity);
        break;
      case AUTOMATIC:
        validateAutomaticActivity(activity);
        break;
    }
  }

  private void validateManualActivity(Activity activity) {
    if (activity.getTasks().isEmpty()) {
      throw new InvalidDefinitionException("No task in manual activity " + activity.getId());
    }
    for (Task task : activity.getTasks()) {
      if (task.getType() != Task.Type.FORM) {
        throw new InvalidDefinitionException(
            "Manual activity should has form task, activity id=" + activity.getId());
      }
    }

  }

  private void validateAutomaticActivity(Activity activity) {
    if (activity.getTasks().isEmpty()) {
      throw new InvalidDefinitionException("No task in automatic activity " + activity.getId());
    }
    for (Task task : activity.getTasks()) {
      if (task.getType() != Task.Type.APPLICATION) {
        throw new InvalidDefinitionException(
            "Automatic activity should has application task, activity id=" + activity.getId());
      }
    }
  }

  private void validateRouteActivity(Activity activity) {

  }

  private void validateTransition(ProcessDefinition definition, Transition transition) {
    checkIfHasActivity(definition, transition.fromActivity());
    checkIfHasActivity(definition, transition.toActivity());
  }

  private void checkIfHasActivity(ProcessDefinition definition, String activityId) {
    if (!isActivityExist(definition, activityId)) {
      throw new InvalidDefinitionException("Activity " + activityId + " doesnot exist!");
    }
  }

  private boolean isActivityExist(ProcessDefinition definition, String activityId) {
    List<Activity> allActivities = new ArrayList<Activity>();
    allActivities.add(definition.getStartActivity());
    allActivities.addAll(definition.getActivities());
    allActivities.addAll(definition.getEndActivities());
    for (Activity activity : allActivities) {
      if (activity.getId().equals(activityId)) {
        return true;
      }
    }
    return false;
  }

}
