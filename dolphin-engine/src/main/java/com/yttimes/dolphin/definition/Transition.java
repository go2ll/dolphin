/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/Transition.java,v 1.2.2.2 2011/10/09 10:13:18 neo-cvs Exp $
 * $Revision: 1.2.2.2 $
 * $Date: 2011/10/09 10:13:18 $
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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.kernel.ExpressionParser;
import com.yttimes.dolphin.kernel.ProcessInstance;

/**
 *
 * @author Ivan Li
 * @version $Id: Transition.java,v 1.2.2.2 2011/10/09 10:13:18 neo-cvs Exp $
 */
public class Transition {
	
	Logger logger = LoggerFactory.getLogger(Transition.class);

	private String from;
	private String to;
	private String condition;

	public Transition(String from, String to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return the from
	 */
	public String fromActivity() {
		return from;
	}

	/**
	 * @return the to
	 */
	public String toActivity() {
		return to;
	}

	public boolean validate(Map<String, String> parameters) {
		String nextActivitySetByUser = parameters.get(ProcessInstance.NEXT_ACTIVITY);
		if (nextActivitySetByUser != null) {
			if(nextActivitySetByUser.equals(to)) {
				logger.debug("got user assigned activity "+to);
				return true;
			} else {
				return false;
			}
		} else {
			if (this.condition.isEmpty()) {
				return true;
			} else {
				ExpressionParser parser = new ExpressionParser();
				return parser.evaluate(this.condition, parameters);
			}
		}

	}

}
