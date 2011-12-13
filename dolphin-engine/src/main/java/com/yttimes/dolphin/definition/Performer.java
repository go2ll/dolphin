/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/Performer.java,v 1.2 2011/07/25 06:53:33 ivan-cvs Exp $
 * $Revision: 1.2 $
 * $Date: 2011/07/25 06:53:33 $
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
package com.yttimes.dolphin.definition;

/**
 *
 * @author Ivan Li
 * @version $Id: Performer.java,v 1.2 2011/07/25 06:53:33 ivan-cvs Exp $
 */
public class Performer {

	private String refId;
	private String property;

	public Performer(String refId) {
		this.refId = refId;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the refId
	 */
	public String getRefId() {
		return refId;
	}

}
