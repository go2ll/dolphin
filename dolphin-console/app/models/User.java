/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/models/Attic/User.java,v 1.1.2.3 2011/09/23 08:58:41 neo-cvs Exp $
 * $Revision: 1.1.2.3 $
 * $Date: 2011/09/23 08:58:41 $
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
package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author Neo
 * @version $Id: User.java,v 1.1.2.3 2011/09/23 08:58:41 neo-cvs Exp $
 */

@Entity
@Table(name = "DOL_CONSOLE_USER")
public class User extends Model {
	
	public enum ROLE {ADMIN,POWER};
 
	@Email
    @Required
    public String email;
	
	@Required
    public String password;
	
    public String fullname;
    
    @Required
    public ROLE role;
    
    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }

	public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }
	
	public String toString() {
	    return email;
	}
 
}