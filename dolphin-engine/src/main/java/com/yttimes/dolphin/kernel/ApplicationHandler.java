/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/ApplicationHandler.java,v 1.2 2011/07/25 06:53:29 ivan-cvs Exp $
 * $Revision: 1.2 $
 * $Date: 2011/07/25 06:53:29 $
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
 * @version $Id: ApplicationHandler.java,v 1.2 2011/07/25 06:53:29 ivan-cvs Exp $
 */
public interface ApplicationHandler {
	public void execute(TaskInstance taskInstance);
}
