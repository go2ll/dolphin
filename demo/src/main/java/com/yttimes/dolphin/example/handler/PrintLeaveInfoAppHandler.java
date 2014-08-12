/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/example/handler/Attic/PrintLeaveInfoAppHandler.java,v 1.1.2.1 2011/07/27 03:00:29 ivan-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/07/27 03:00:29 $
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
package com.yttimes.dolphin.example.handler;

import com.yttimes.dolphin.kernel.ApplicationHandler;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.TaskInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neo
 * @version $Id: PrintLeaveInfoAppHandler.java,v 1.1.2.1 2011/07/27 03:00:29 ivan-cvs Exp $
 */
public class PrintLeaveInfoAppHandler implements ApplicationHandler {

  private Logger logger = LoggerFactory.getLogger(PrintLeaveInfoAppHandler.class);

  @Override
  public void execute(TaskInstance taskInstance) {
    ProcessInstance instance = taskInstance.getProcessInstance();
    logger.info("Print Leave Infomation:");
    logger.info("applier:" + instance.getDataVariable("applier"));
    logger.info("leaveDayNum:" + instance.getDataVariable("leaveDayNum"));
    logger.info("startDay:" + instance.getDataVariable("startDay"));
  }

}
