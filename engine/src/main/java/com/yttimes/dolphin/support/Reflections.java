/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/Attic/Reflections.java,v 1.1.2.3 2011/10/12 03:49:59 neo-cvs Exp $
 * $Revision: 1.1.2.3 $
 * $Date: 2011/10/12 03:49:59 $
 *
 * ==================================================================== 
 *
 * Copyright (c) 2010 YT Times Co., Ltd All Rights Reserved. 
 * This software is the confidential and proprietary information of 
 * YT Times Co., Ltd. You shall not disclose such Confidential 
 * Information. 
 *
 * ==================================================================== 
 *
 */
package com.yttimes.dolphin.support;

import com.yttimes.dolphin.exception.DolphinException;

import java.lang.reflect.Method;

/**
 * Object factory
 *
 * @author Ivan Li
 * @version $Id: Reflections.java,v 1.1.2.3 2011/10/12 03:49:59 neo-cvs Exp $
 */
public class Reflections {

  public static Object newObject(String className) throws Exception {
    Class<?> cls = loadClass(className);
    return cls.newInstance();
  }

  public static <T> T newObject(Class<T> cls) {
    try {
      return cls.newInstance();
    } catch (Exception e) {
      throw new DolphinException(e.getMessage());
    }
  }

  public static Class<?> loadClass(String className) {
    try {
      return loadClass(className, null);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "Class '" + className + "' could not be found: " + e.getMessage());
    }
  }

  public static Class<?> loadClass(String className, ClassLoader classLoader)
      throws ClassNotFoundException {
    if (classLoader == null) {
      // Look up the class loader to be used
      classLoader = Thread.currentThread().getContextClassLoader();

      if (classLoader == null) {
        classLoader = Reflections.class.getClassLoader();
      }
    }
    // Attempt to load the specified class
    return classLoader.loadClass(className);
  }

  public static Object invokeMethod(Object owner, String methodName, Object[] args,
                                    boolean primitiveArgsTrans) throws Exception {
    Class<?> ownerClass = owner.getClass();
    Class<?>[] argsClass = new Class[args.length];

    for (int i = 0, j = args.length; i < j; i++) {
      if (primitiveArgsTrans && Long.class.equals(args[i].getClass())) {
        argsClass[i] = long.class;
      } else {
        argsClass[i] = args[i].getClass();
      }
    }

    Method method = ownerClass.getDeclaredMethod(methodName, argsClass);
    method.setAccessible(true);
    return method.invoke(owner, args);
  }

  public static Object invokeMethod(Object owner, String methodName, Object[] args)
      throws Exception {
    return invokeMethod(owner, methodName, args, false);
  }


  public static Object invokeMethod(Object owner, String methodName) throws Exception {
    Class<?> ownerClass = owner.getClass();
    Method method = ownerClass.getDeclaredMethod(methodName);
    method.setAccessible(true);
    return method.invoke(owner);
  }

}
