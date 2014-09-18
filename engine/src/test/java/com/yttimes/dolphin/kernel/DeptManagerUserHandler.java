/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/kernel/DeptManagerUserHandler.java,v 1.2.2.2 2011/09/20 03:12:39 neo-cvs Exp $
 * $Revision: 1.2.2.2 $
 * $Date: 2011/09/20 03:12:39 $
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
 * @version $Id: DeptManagerUserHandler.java,v 1.2.2.2 2011/09/20 03:12:39 neo-cvs Exp $
 */
public class DeptManagerUserHandler implements AssignmentHandler {

	@Override
	public long[] findUserIds(TaskInstance taskInstance) {
		return new long[] { 2222 };
	}

	/* (non-Javadoc)
	 * @see com.yttimes.dolphin.kernel.AssignmentHandler#isOptional()
	 */
	@Override
	public boolean isOptional() {
		// TODO Auto-generated method stub
		return false;
	}

}
