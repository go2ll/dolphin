/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/kernel/PrintLeaveInfoAppHandler.java,v 1.2 2011/07/25 06:53:33 ivan-cvs Exp $
 * $Revision: 1.2 $
 * $Date: 2011/07/25 06:53:33 $
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


/**
 *
 * @author Neo
 * @version $Id: PrintLeaveInfoAppHandler.java,v 1.2 2011/07/25 06:53:33 ivan-cvs Exp $
 */
public class PrintLeaveInfoAppHandler implements ApplicationHandler {

	/* (non-Javadoc)
	 * @see com.yttimes.dolphin.ApplicationHandler#execute(com.yttimes.dolphin.TaskInstance)
	 */
	@Override
	public void execute(TaskInstance taskInstance) {
		ProcessInstance instance = taskInstance.getProcessInstance();
		System.out.println("Print Leave Infomation:");
		System.out.println("applier:" + instance.getDataVariable("applier"));
		System.out.println("leaveDayNum:" + instance.getDataVariable("leaveDayNum"));
		System.out.println("startDay:" + instance.getDataVariable("startDay"));
	}

}
