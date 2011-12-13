/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/controllers/Attic/Security.java,v 1.1.2.1 2011/08/30 05:07:21 neo-cvs Exp $
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

import models.*;
/**
 *
 * @author Neo
 * @version $Id: Security.java,v 1.1.2.1 2011/08/30 05:07:21 neo-cvs Exp $
 */
public class Security extends Secure.Security{
	
	 static boolean authenticate(String username, String password) {
		    return User.connect(username, password) != null;
	 }
	 
	 static void onDisconnected() {
		    Console.index();
		}	 
}
