/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/annotation/Attic/DolphinJTAProxy.java,v 1.1.2.3 2011/11/07 06:24:56 neo-cvs Exp $
 * $Revision: 1.1.2.3 $
 * $Date: 2011/11/07 06:24:56 $
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

import com.yttimes.dolphin.support.JPAService;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import javax.transaction.UserTransaction;

/**
 * @author Ivan Li
 * @version $Id: DolphinJTAProxy.java,v 1.1.2.3 2011/11/07 06:24:56 neo-cvs Exp $
 */
public final class DolphinJTAProxy {

  private static final JtaTxnInterceptor JTA_TXN_INTERCEPTOR = new JtaTxnInterceptor();
  private static final JtaTxnFilter JTA_TXN_FILTER = new JtaTxnFilter();
  private static final Callback[]
      CALLBACK_FILTERS =
      new Callback[]{JTA_TXN_INTERCEPTOR, NoOp.INSTANCE};

  private DolphinJTAProxy() {

  }

  public static <T> T getProxy(Class<T> type) {
    T
        obj =
        type.cast(Enhancer.create(type, type.getInterfaces(), JTA_TXN_FILTER, CALLBACK_FILTERS));
    return obj;
  }

  private static final class JtaTxnFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
      return method.isAnnotationPresent(JTATransactional.class) ? 0 : 1;
    }
  }

  private static final class JtaTxnInterceptor implements MethodInterceptor {

    Logger logger = LoggerFactory.getLogger(DolphinJTAProxy.class);

    @Override
    public Object intercept(Object obj, Method m, Object[] args, MethodProxy proxy)
        throws Throwable {
      UserTransaction
          transaction =
          JndiIntegration.fromJndi(UserTransaction.class, "java:comp/UserTransaction");
      Object ret = null;
      logger.debug("JtaTxnInterceptor intercept begin...");
      try {
        transaction.begin();
        JPAService.getInstance().begin();
        ret = proxy.invokeSuper(obj, args);
        //JPAService.getInstance().flush();
        transaction.commit();
      } catch (Throwable t) {
        logger.error("error occured in jta transaction!", t);
        if (transaction != null) {
          transaction.rollback();
        }
        throw t.fillInStackTrace();
      } finally {
        JPAService.getInstance().end();
      }
      logger.debug("JtaTxnInterceptor intercept end...");
      return ret;
    }

  }

}
