/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/annotation/Attic/ContextFactory.java,v 1.1.2.1 2011/08/02 09:19:22 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/08/02 09:19:22 $
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
package com.yttimes.dolphin.support.annotation;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author Ivan Li
 * @version $Id: ContextFactory.java,v 1.1.2.1 2011/08/02 09:19:22 neo-cvs Exp $
 */
public class ContextFactory {

	private Context context;

	private ContextFactory() {
		init();
	}

	private void init() {
		try {
			context = new InitialContext();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public static ContextFactory getInstance() {
		return ContextFactoryHolder.CONTEXT_FACTORY;
	}

	public Context getContext() {
		return context;
	}

	private static class ContextFactoryHolder {
		private static final ContextFactory CONTEXT_FACTORY = new ContextFactory();
	}
}
