/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/Attic/PatternAssignmentHandler.java,v 1.1.2.1 2011/12/05 03:01:19 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/12/05 03:01:19 $
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
 * @author Neo
 * @version $Id: PatternAssignmentHandler.java,v 1.1.2.1 2011/12/05 03:01:19 neo-cvs Exp $
 */
public class PatternAssignmentHandler implements AssignmentHandler {

  private String optionPattern;

  public PatternAssignmentHandler(String optionPattern) {
    this.optionPattern = optionPattern;
  }

  @Override
  public long[] findUserIds(TaskInstance taskInstance) {
    return null;
  }

  @Override
  public boolean isOptional() {
    return true;
  }

  public String getOptionPattern() {
    return optionPattern;
  }


}
