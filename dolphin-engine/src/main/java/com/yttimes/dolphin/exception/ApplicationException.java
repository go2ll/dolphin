/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/Attic/ApplicationException.java,v 1.1.2.1 2011/09/07 05:46:38 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/09/07 05:46:38 $
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
 *
 * @author Neo
 * @version $Id: ApplicationException.java,v 1.1.2.1 2011/09/07 05:46:38 neo-cvs Exp $
 */
public class ApplicationException extends DolphinException {

	private static final long serialVersionUID = 9023621022714049468L;

	public ApplicationException(String msg) {
		super(msg);
	}
	
	public ApplicationException(String msg,Exception ex) {
		super(msg,ex);
	}
}
