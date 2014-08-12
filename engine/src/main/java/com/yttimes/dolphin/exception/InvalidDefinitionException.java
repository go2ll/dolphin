/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/Attic/InvalidDefinitionException.java,v 1.1.2.1 2011/10/11 10:05:15 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/10/11 10:05:15 $
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
 * @version $Id: InvalidDefinitionException.java,v 1.1.2.1 2011/10/11 10:05:15 neo-cvs Exp $
 */
public class InvalidDefinitionException extends DolphinException {

  private static final long serialVersionUID = 8128940920696820233L;

  public InvalidDefinitionException(String msg) {
    super(msg);
  }

  public InvalidDefinitionException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
