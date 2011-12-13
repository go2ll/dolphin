/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/AssignmentHandler.java,v 1.2.2.1 2011/09/20 01:49:47 neo-cvs Exp $
 * $Revision: 1.2.2.1 $
 * $Date: 2011/09/20 01:49:47 $
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
 * @author Ivan Li
 * @version $Id: AssignmentHandler.java,v 1.2.2.1 2011/09/20 01:49:47 neo-cvs Exp $
 */
public interface AssignmentHandler {

	/**
	 * 
	 * @param taskInstance
	 * @return
	 */
	public long[] findUserIds(TaskInstance taskInstance);
	
	
	/**
	 * 
	 * @return true if user assignment decision is made by client
	 */
	public boolean isOptional();
}
