/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/Attic/ConfusedTransitionException.java,v 1.1.2.2 2011/10/09 10:13:25 neo-cvs Exp $
 * $Revision: 1.1.2.2 $
 * $Date: 2011/10/09 10:13:25 $
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
package com.yttimes.dolphin.exception;

import com.yttimes.dolphin.kernel.ActivityInstance;

import java.util.List;

/**
 * @author Neo
 * @version $Id: ConfusedTransitionException.java,v 1.1.2.2 2011/10/09 10:13:25 neo-cvs Exp $
 */
public class ConfusedTransitionException extends DolphinException {

  private static final long serialVersionUID = -9183150261825524536L;
  private List<ActivityInstance> nextActivities;

  public ConfusedTransitionException(String msg) {
    super(msg);
  }

  public ConfusedTransitionException(String msg, List<ActivityInstance> nextActivities) {
    super(msg);
    this.nextActivities = nextActivities;
  }

  public ConfusedTransitionException(String msg, Exception ex) {
    super(msg, ex);
  }

  public List<ActivityInstance> getNextActivities() {
    return this.nextActivities;
  }

  public String getNextActivitiesId() {
    String nextActivitiesId = "";
    for (int i = 0; i < nextActivities.size(); i++) {
      nextActivitiesId += nextActivities.get(i).getActivityId();
      if (i < nextActivities.size() - 1) {
        nextActivitiesId += ",";
      }
    }
    return nextActivitiesId;
  }
}
