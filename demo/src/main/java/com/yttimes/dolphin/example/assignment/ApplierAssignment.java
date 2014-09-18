/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/example/assignment/ApplierAssignment.java,v 1.2.2.1 2011/09/23 02:39:01 neo-cvs Exp $
 * $Revision: 1.2.2.1 $
 * $Date: 2011/09/23 02:39:01 $
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
package com.yttimes.dolphin.example.assignment;

import com.yttimes.dolphin.kernel.AssignmentHandler;
import com.yttimes.dolphin.kernel.TaskInstance;


/**
 * @author Ivan Li
 * @version $Id: ApplierAssignment.java,v 1.2.2.1 2011/09/23 02:39:01 neo-cvs Exp $
 */
public class ApplierAssignment implements AssignmentHandler {

  @Override
  public long[] findUserIds(TaskInstance taskInstance) {
    return new long[]{taskInstance.getActivityInstance().getProcessInstance().getCreatorId()};
  }

  @Override
  public boolean isOptional() {
    // TODO Auto-generated method stub
    return false;
  }

}
