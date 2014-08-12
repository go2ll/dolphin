/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/TaskActivityInstance.java,v 1.2.2.4 2011/12/06 07:01:00 neo-cvs Exp $
 * $Revision: 1.2.2.4 $
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
import com.yttimes.dolphin.definition.Task;

/**
 * @author Neo
 * @version $Id: TaskActivityInstance.java,v 1.2.2.4 2011/12/06 07:01:00 neo-cvs Exp $
 */
abstract class TaskActivityInstance extends ActivityInstance {

  TaskActivityInstance() {
  }


  TaskActivityInstance(ProcessInstance processInst, Activity activityDef) {
    super(processInst, activityDef);
  }

  public void start() {
    super.start();
    if (getDefinition() != null) {
      for (Task task : getDefinition().getTasks()) {
        logger.debug("create new task instance " + task.getId());
        TaskInstance taskInstance = createTaskInstance(task);
        taskInstance.start();
        taskInstances.add(taskInstance);
      }
    } else {
      logger.warn("TaskActivityInstance " + getDefinition().getId() + " doesn't have any task!");
    }
  }


  /*
   *  factory method for creating taskinstance
   */
  abstract TaskInstance createTaskInstance(Task task);

}
