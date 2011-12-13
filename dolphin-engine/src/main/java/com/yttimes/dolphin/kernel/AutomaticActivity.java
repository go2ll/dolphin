/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/AutomaticActivity.java,v 1.2.2.7 2011/09/26 07:10:16 neo-cvs Exp $
 * $Revision: 1.2.2.7 $
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

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.yttimes.dolphin.definition.Activity;
import com.yttimes.dolphin.definition.Task;
import com.yttimes.dolphin.exception.ApplicationException;
import com.yttimes.dolphin.kernel.ApplicationTaskInstance.ExecuteStrategy;
import com.yttimes.dolphin.support.Reflections;

/**
 *
 * @author Neo
 * @version $Id: AutomaticActivity.java,v 1.2.2.7 2011/09/26 07:10:16 neo-cvs Exp $
 */

@Entity
@DiscriminatorValue("AUTOMATIC")
public class AutomaticActivity extends TaskActivityInstance {
	
	AutomaticActivity(){		
	}

	AutomaticActivity(ProcessInstance processInst, Activity activityDef) {
		super(processInst, activityDef);
	}

	public boolean execute() {
		logger.info("activity " + this.getActivityId() + " is executing..");
		logger.debug("activity state:" + getState());
		logger.debug("activity type: Automatic");
		if(getState() == State.UNACTIVATED) {
			throw new IllegalStateException("cannot execute unactivated activity!");
		} else if (getState() == State.ACTIVATED) {
			for (TaskInstance taskInst : taskInstances) {
				taskInst.isCompleted();
			}
			setState(State.TERMINATED);
			getProcessInstance().activateNextActivities(this);
		}
		this.updatedTime=new Date();
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

	@Override
	TaskInstance createTaskInstance(Task taskDef) {
		ApplicationTaskInstance appTaskInst = new ApplicationTaskInstance(this, taskDef);
		if ("ASYN".equalsIgnoreCase(taskDef.getStrategy())) {
			appTaskInst.setExecuteStrategy(ExecuteStrategy.ASYN);
		} else {
			//default execute strategy is SYN
			appTaskInst.setExecuteStrategy(ExecuteStrategy.SYN);
		}
		Object handlerObj;
		try {
			handlerObj = Reflections.newObject(taskDef.getHandlerClass());
			if (handlerObj instanceof ApplicationHandler) {
				appTaskInst.setAppHandler((ApplicationHandler) handlerObj);
			} else {
				throw new ApplicationException("this class must implement ApplicationHandler");
			}
		} catch (Exception e) {
			logger.warn("ApplicationHandler class error!");
			//throw new ApplicationException("ApplicationHandler class error!",e);
		}	
		return appTaskInst;
	}

}
