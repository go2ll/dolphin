/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/exception/Attic/DuplicateDefinitionException.java,v 1.1.2.1 2011/08/31 01:47:08 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/08/31 01:47:08 $
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
 * @version $Id: DuplicateDefinitionException.java,v 1.1.2.1 2011/08/31 01:47:08 neo-cvs Exp $
 */
public class DuplicateDefinitionException  extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8459981587198978130L;

	public DuplicateDefinitionException(String msg) {
		super(msg);
	}
}
