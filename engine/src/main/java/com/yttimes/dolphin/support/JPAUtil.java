/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/JPAUtil.java,v 1.2.2.2 2011/08/02 09:35:03 neo-cvs Exp $
 * $Revision: 1.2.2.2 $
 * $Date: 2011/08/02 09:35:03 $
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
package com.yttimes.dolphin.support;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Neo
 * @version $Id: JPAUtil.java,v 1.2.2.2 2011/08/02 09:35:03 neo-cvs Exp $
 */
public class JPAUtil {

  private static EntityManagerFactory emFactory;

  static {
    emFactory = Persistence.createEntityManagerFactory("dolphin");
  }

  public static EntityManagerFactory getEntityManagerFactory() {
    return emFactory;
  }

  public static void shutdownEntityManagerFactory() {
    emFactory.close();
  }

}
