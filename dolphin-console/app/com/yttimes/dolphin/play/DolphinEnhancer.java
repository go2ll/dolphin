/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/com/yttimes/dolphin/play/Attic/DolphinEnhancer.java,v 1.1.2.2 2011/09/29 05:37:35 neo-cvs Exp $
 * $Revision: 1.1.2.2 $
 * $Date: 2011/09/29 05:37:35 $
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
package com.yttimes.dolphin.play;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ClassFile;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

/**
 *
 * @author Neo
 * @version $Id: DolphinEnhancer.java,v 1.1.2.2 2011/09/29 05:37:35 neo-cvs Exp $
 */
public class DolphinEnhancer  extends Enhancer {

	
    public Class<?> enhanceEntityClass(String className) throws Exception {
        CtClass ctClass = makeClass(className);

        // Enhance only JPA entities
        if (!hasAnnotation(ctClass, "javax.persistence.Entity")) {
            return null;
        }

        if (!ctClass.subtypeOf(classPool.get("play.db.jpa.Model"))) {
        	enhanceEntity(ctClass);
        }
        
        CtClass enhancedCtc = enhance(ctClass);
        return enhancedCtc.toClass();
    }
    
    private CtClass enhanceEntity( CtClass ctClass) throws Exception {
    	ctClass.setSuperclass(classPool.get("play.db.jpa.GenericModel"));
    	 /* remove getId() method in entity class to prevent
         * duplicate method.          */  	
  	
        // key
        CtMethod key = CtMethod.make("public Object _key() { return Long.valueOf(getId()); }", ctClass);
        ctClass.addMethod(key);

        return ctClass;
    }
    
    private CtClass enhance( CtClass ctClass) throws Exception {

        String entityName = ctClass.getName();    

        // count
        CtMethod count = CtMethod.make("public static long count() { return play.db.jpa.JPQL.instance.count(\"" + entityName + "\"); }", ctClass);
        ctClass.addMethod(count);

        // count2
        CtMethod count2 = CtMethod.make("public static long count(String query, Object[] params) { return play.db.jpa.JPQL.instance.count(\"" + entityName + "\", query, params); }", ctClass);
        ctClass.addMethod(count2);

        // findAll
        CtMethod findAll = CtMethod.make("public static java.util.List findAll() { return play.db.jpa.JPQL.instance.findAll(\"" + entityName + "\"); }", ctClass);
        ctClass.addMethod(findAll);

        // findById
        CtMethod findById = CtMethod.make("public static play.db.jpa.JPABase findById(Object id) { return play.db.jpa.JPQL.instance.findById(\"" + entityName + "\", id); }", ctClass);
        ctClass.addMethod(findById);

        // find
        CtMethod find = CtMethod.make("public static play.db.jpa.GenericModel.JPAQuery find(String query, Object[] params) { return play.db.jpa.JPQL.instance.find(\"" + entityName + "\", query, params); }", ctClass);
        ctClass.addMethod(find);

        // find
        CtMethod find2 = CtMethod.make("public static play.db.jpa.GenericModel.JPAQuery find() { return play.db.jpa.JPQL.instance.find(\"" + entityName + "\"); }", ctClass);
        ctClass.addMethod(find2);

        // all
        CtMethod all = CtMethod.make("public static play.db.jpa.GenericModel.JPAQuery all() { return play.db.jpa.JPQL.instance.all(\"" + entityName + "\"); }", ctClass);
        ctClass.addMethod(all);

        // delete
        CtMethod delete = CtMethod.make("public static int delete(String query, Object[] params) { return play.db.jpa.JPQL.instance.delete(\"" + entityName + "\", query, params); }", ctClass);
        ctClass.addMethod(delete);

        // deleteAll
        CtMethod deleteAll = CtMethod.make("public static int deleteAll() { return play.db.jpa.JPQL.instance.deleteAll(\"" + entityName + "\"); }", ctClass);
        ctClass.addMethod(deleteAll);

        // findOneBy
        CtMethod findOneBy = CtMethod.make("public static play.db.jpa.JPABase findOneBy(String query, Object[] params) { return play.db.jpa.JPQL.instance.findOneBy(\"" + entityName + "\", query, params); }", ctClass);
        ctClass.addMethod(findOneBy);

        // create
        CtMethod create = CtMethod.make("public static play.db.jpa.JPABase create(String name, play.mvc.Scope.Params params) { return play.db.jpa.JPQL.instance.create(\"" + entityName + "\", name, params); }", ctClass);
        ctClass.addMethod(create);

        return ctClass;
    }

	/* (non-Javadoc)
	 * @see play.classloading.enhancers.Enhancer#enhanceThisClass(play.classloading.ApplicationClasses.ApplicationClass)
	 */
	@Override
	public void enhanceThisClass(ApplicationClass applicationClass) throws Exception {
	   throw new UnsupportedOperationException("applicationClass is not supported here");
	}
}
