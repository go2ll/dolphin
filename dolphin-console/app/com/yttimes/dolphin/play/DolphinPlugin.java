package com.yttimes.dolphin.play;
import play.Invoker.InvocationContext;
import play.Play.Mode;
import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.db.jpa.JPA;
import play.db.jpa.JPAEnhancer;
import play.db.jpa.NoTransaction;
import play.db.jpa.Transactional;

import com.yttimes.dolphin.support.JPAService;


/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/com/yttimes/dolphin/play/Attic/DolphinPlugin.java,v 1.1.2.2 2011/09/29 05:37:34 neo-cvs Exp $
 * $Revision: 1.1.2.2 $
 * $Date: 2011/09/29 05:37:34 $
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

/**
 *
 * @author Neo
 * @version $Id: DolphinPlugin.java,v 1.1.2.2 2011/09/29 05:37:34 neo-cvs Exp $
 */
public class DolphinPlugin extends PlayPlugin {
	
	@Override
	public void beforePrecompile() { 
		Logger.info("DolphinPlugin beforePrecompile");
		enhanceEntities();
    }
	
	@Override
	public void onApplicationReady() {

		Logger.info("DolphinPlugin onApplicationReady");
		 if (Play.mode == Mode.DEV)  {
			 enhanceEntities();
		}
	}
	
	
	private void enhanceEntities() {
		String[] moreEntities = Play.configuration.getProperty("enhance.entities", "").split(",");
		for (String entity : moreEntities) {
			if (entity.trim().equals("")) {
				continue;
			}
			try {
				entity = entity.trim();
				Logger.info("enhance entity class:" + entity);
				new DolphinEnhancer().enhanceEntityClass(entity);
				//Logger.trace("current classloader:"+clz.getClassLoader());
				//Logger.trace("?????????????????????Is it a Model? "+Model.class.isAssignableFrom(clz));
			} catch (Exception e) {
				e.printStackTrace();
				Logger.warn("JPA -> Entity not found: %s", entity);
			}
		}
	}
	
	
    @Override
    public void onApplicationStart() {
    	 Logger.trace("DolphinPlugin onApplicationStart");
    	 if (JPA.entityManagerFactory == null) {
    		 throw new IllegalStateException("play JPA context is not available!");
    	 } else {
    		 JPAService.getInstance().createJPAContext(JPA.entityManagerFactory);
    	 }
    }
    
    @Override
    public void onApplicationStop() {
    	Logger.trace("DolphinPlugin onApplicationStop");
    	JPAService.getInstance().releaseJPAContext();
    }
    
    @Override
    public void beforeInvocation() {
    	Logger.trace("DolphinPlugin beforeInvocation");
        DolTransactional tx = InvocationContext.current().getAnnotation(DolTransactional.class);
        if (tx != null) {
        	Logger.info("start entity transaction");
           JPAService.getInstance().beginTransaction();
        }        
    }

    @Override
    public void afterInvocation() {
    	Logger.trace("DolphinPlugin afterInvocation");
    	DolTransactional tx = InvocationContext.current().getAnnotation(DolTransactional.class);
        if (tx != null) {
        	Logger.info("end entity transaction");
           JPAService.getInstance().commitTransaction();
           JPAService.getInstance().releaseEntityManager();
        }        
    }
    
    @Override
    public void onInvocationException(Throwable e) {
    	Logger.trace("DolphinPlugin onInvocationException");
    	DolTransactional tx = InvocationContext.current().getAnnotation(DolTransactional.class);
        if (tx != null) {
        	Logger.info("roolback entity transaction");
    	   JPAService.getInstance().rollbackTransaction();
        }
    }

}
