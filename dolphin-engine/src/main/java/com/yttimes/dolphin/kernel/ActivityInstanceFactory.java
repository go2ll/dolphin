/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/kernel/ActivityInstanceFactory.java,v 1.2.2.2 2011/09/26 07:10:16 neo-cvs Exp $
 * $Revision: 1.2.2.2 $
 * $Date: 2011/09/26 07:10:16 $
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
package com.yttimes.dolphin.kernel;

import java.util.Set;

import com.yttimes.dolphin.definition.Activity;

/**
 *
 * @author Neo
 * @version $Id: ActivityInstanceFactory.java,v 1.2.2.2 2011/09/26 07:10:16 neo-cvs Exp $
 */
class ActivityInstanceFactory {

	   static ActivityInstance createInstance(ProcessInstance instance, String activityId) {
		if (activityId == null) {
			throw new IllegalArgumentException("activityId is not null!");
		}
		Set<Activity> activities = instance.getProcessDefinition().getAllActivities();
		Activity activityDef = null;
		for (Activity activity : activities) {
			if (activityId.equals(activity.getId())) {
				activityDef = activity;
				break;
			}
		}
		if (activityDef == null) {
			throw new IllegalArgumentException("activityId doesn't exist.");
		}

		ActivityInstance activityInstance = null;
		switch (activityDef.getType()) {
		case MANUAL:
			activityInstance = new ManualActivity(instance, activityDef);
			break;
		case AUTOMATIC:
			activityInstance = new AutomaticActivity(instance, activityDef);
			break;
		case START:
			activityInstance = new StartActivity(instance, activityDef); 
			break;
		case END:
			activityInstance = new EndActivity(instance, activityDef);
			break;
		case ROUTE:
			activityInstance = new RouteActivity(instance, activityDef);
			break;
		default:
			activityInstance = new RouteActivity(instance, activityDef);
			break;
		}
		return activityInstance;

	}
}
