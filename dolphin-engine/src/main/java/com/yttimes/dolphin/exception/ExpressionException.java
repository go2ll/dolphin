/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/Attic/ExpressionException.java,v 1.1.2.2 2011/09/13 08:15:18 yinxiong-cvs Exp $
 * $Revision: 1.1.2.2 $
 * $Date: 2011/09/13 08:15:18 $
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
 * @version $Id: ExpressionException.java,v 1.1.2.2 2011/09/13 08:15:18 yinxiong-cvs Exp $
 */
public class ExpressionException extends DolphinException {

	private static final long serialVersionUID = 7816045343694010731L;

	private String keys;

	public ExpressionException(String msg) {
		super(msg);
	}
	
	public ExpressionException(String msg,Exception ex) {
		super(msg,ex);
	}

	public ExpressionException(String msg, String keys, Exception ex) {
		super(msg, ex);
		this.keys = keys;
	}

	/**
	 * @return the keys
	 */
	public String getKeys() {
		return keys;
	}

}
