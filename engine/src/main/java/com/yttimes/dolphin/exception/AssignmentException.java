/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/Attic/AssignmentException.java,v 1.1.2.3 2011/09/20 01:49:41 neo-cvs Exp $
 * $Revision: 1.1.2.3 $
 * $Date: 2011/09/20 01:49:41 $
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

import com.yttimes.dolphin.kernel.FormTaskInstance;

/**
 * @author Neo
 * @version $Id: AssignmentException.java,v 1.1.2.3 2011/09/20 01:49:41 neo-cvs Exp $
 */
public class AssignmentException extends DolphinException {


  private FormTaskInstance taskInstance;

  private long[] userOptions;
  /**
   *
   */
  private static final long serialVersionUID = -2253063611634089036L;

  public AssignmentException(String msg) {
    super(msg);
  }

  public AssignmentException(String msg, FormTaskInstance taskInstance) {
    super(msg);
    this.taskInstance = taskInstance;
  }

  public AssignmentException(String msg, long[] users) {
    super(msg);
    this.userOptions = users;
  }

  public AssignmentException(String msg, Exception ex) {
    super(msg, ex);
  }

  public FormTaskInstance getTaskInstance() {
    return this.taskInstance;
  }

  public long[] getUserOptions() {
    return this.userOptions;
  }

}
