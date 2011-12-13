/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/example/listener/Attic/BTMServletContextListener.java,v 1.1.2.4 2011/08/31 02:00:37 neo-cvs Exp $
 * $Revision: 1.1.2.4 $
 * $Date: 2011/08/31 02:00:37 $
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
package com.yttimes.dolphin.example.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;

import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Ivan Li
 * @version $Id: BTMServletContextListener.java,v 1.1.2.4 2011/08/31 02:00:37 neo-cvs Exp $
 */
public class BTMServletContextListener implements ServletContextListener {
	private BitronixTransactionManager btm;

	public BTMServletContextListener() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		btm.shutdown();
		JPAService.getInstance().releaseJPAContext();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.setProperty("bitronix-resource-path", Thread.currentThread().getContextClassLoader().getResource("").getPath());
		btm = TransactionManagerServices.getTransactionManager();
		JPAService.getInstance().createJPAContext();
	}
}
