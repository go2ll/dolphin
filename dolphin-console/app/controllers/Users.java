/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/controllers/Attic/Users.java,v 1.1.2.1 2011/08/30 05:07:21 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/08/30 05:07:21 $
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
package controllers;

import play.mvc.With;
import models.User;
import controllers.CRUD.For;

/**
 *
 * @author Neo
 * @version $Id: Users.java,v 1.1.2.1 2011/08/30 05:07:21 neo-cvs Exp $
 */
@For(User.class)
@With(Secure.class)
public class Users extends CRUD {

}
