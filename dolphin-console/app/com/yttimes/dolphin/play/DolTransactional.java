/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/com/yttimes/dolphin/play/Attic/DolTransactional.java,v 1.1.2.1 2011/09/23 08:57:45 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/09/23 08:57:45 $
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Neo
 * @version $Id: DolTransactional.java,v 1.1.2.1 2011/09/23 08:57:45 neo-cvs Exp $
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DolTransactional {

}
