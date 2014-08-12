/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/ApplicationTaskInstance.java,v 1.2.2.4 2011/09/26 07:10:16 neo-cvs Exp $
 * $Revision: 1.2.2.4 $
 * $Date: 2011/09/26 07:10:16 $
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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author Neo
 * @version $Id: ApplicationTaskInstance.java,v 1.2.2.4 2011/09/26 07:10:16 neo-cvs Exp $
 */
@Entity
@DiscriminatorValue("APPLICATION")
class ApplicationTaskInstance extends TaskInstance {


  @Transient
  private ExecuteStrategy executeStrategy = ExecuteStrategy.SYN;
  @Transient
  private ApplicationHandler appHandler;

  ApplicationTaskInstance() {
  }

  ApplicationTaskInstance(ActivityInstance activityInstance, Task task) {
    super(activityInstance, task);
  }

  public ExecuteStrategy getExecuteStrategy() {
    return executeStrategy;
  }

  public void setExecuteStrategy(ExecuteStrategy executeStrategy) {
    this.executeStrategy = executeStrategy;
  }

  public ApplicationHandler getAppHandler() {
    return appHandler;
  }

  public void setAppHandler(ApplicationHandler appHandler) {
    this.appHandler = appHandler;
  }

  @Override
  public void complete() {
    executeStrategy.execute(this);
  }

  @Override
  public void start() {
    // TODO Auto-generated method stub

  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException();

  }

  @Override
  public void terminate() {
    setState(State.COMPLETED);

  }

  public enum ExecuteStrategy {

    SYN {
      public void execute(ApplicationTaskInstance instance) {
        if (instance.getAppHandler() != null) {
          instance.getAppHandler().execute(instance);
        }
        instance.setState(State.COMPLETED);
      }
    },
    ASYN {
      public void execute(ApplicationTaskInstance instance) {
        instance.setState(State.COMPLETED);
      }
    };

    public abstract void execute(ApplicationTaskInstance instance);
  }

}
