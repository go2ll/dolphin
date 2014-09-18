/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/StartActivity.java,v 1.2.2.5 2011/09/26 07:10:16 neo-cvs Exp $
 * $Revision: 1.2.2.5 $
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

import com.yttimes.dolphin.definition.Activity;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Neo
 * @version $Id: StartActivity.java,v 1.2.2.5 2011/09/26 07:10:16 neo-cvs Exp $
 */

@Entity
@DiscriminatorValue("START")
class StartActivity extends ActivityInstance {

  StartActivity() {
  }

  StartActivity(ProcessInstance processInst, Activity activityDef) {
    super(processInst, activityDef);
  }

  public boolean execute() {
    logger.debug("execute StartActivity...");
    if (getState() == State.ACTIVATED) {
      setState(State.TERMINATED);
      getProcessInstance().activateNextActivities(this);
    }
    this.updatedTime = new Date();
    return true;
  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void terminate() {
    setState(State.RUNNING);
  }
}
