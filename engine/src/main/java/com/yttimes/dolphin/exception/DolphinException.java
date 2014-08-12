/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/DolphinException.java,v 1.2 2011/07/25 06:53:34 ivan-cvs Exp $
 * $Revision: 1.2 $
 * $Date: 2011/07/25 06:53:34 $
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

/**
 * @author Neo
 * @version $Id: DolphinException.java,v 1.2 2011/07/25 06:53:34 ivan-cvs Exp $
 */
public class DolphinException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 6464053260083431838L;

  public DolphinException(String msg) {
    super(msg);
  }

  public DolphinException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public Throwable getRootCause() {
    Throwable rootCause = null;
    Throwable cause = getCause();
    while (cause != null && cause != rootCause) {
      rootCause = cause;
      cause = cause.getCause();
    }
    return rootCause;
  }
}
