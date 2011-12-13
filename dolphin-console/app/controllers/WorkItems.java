/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/controllers/Attic/WorkItems.java,v 1.1.2.9 2011/11/30 06:27:59 neo-cvs Exp $
 * $Revision: 1.1.2.9 $
 * $Date: 2011/11/30 06:27:59 $
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

import java.util.HashMap;
import java.util.Map;

import play.Logger;
import play.mvc.With;

import com.yttimes.dolphin.exception.AssignmentException;
import com.yttimes.dolphin.exception.DolphinException;
import com.yttimes.dolphin.exception.ExpressionException;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.play.DolTransactional;
import com.yttimes.dolphin.play.Dolphin;
import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: WorkItems.java,v 1.1.2.9 2011/11/30 06:27:59 neo-cvs Exp $
 */

@CRUD.ForEntity(com.yttimes.dolphin.kernel.WorkItem.class)
@With(Secure.class)
public class WorkItems extends CRUD {

	@DolTransactional
	public static void reassignUser(Long itemId, Long userId) throws Throwable {
		Logger.info("reassignUser itemId="+itemId+",userId="+userId);
		if (itemId != null && itemId != null) {
			WorkItem workItem = Dolphin.workflowManager.getWorkItemById(itemId);
			Dolphin.reAssignWorkItem(workItem, userId);
		}
	}

	@DolTransactional
	public static void complete(Long itemId) {
		Map<String, String> back = new HashMap<String, String>();
		try {
			WorkItem workitem = Dolphin.workflowManager.getWorkItemById(itemId);
			workitem.complete();
			back.put("processState", workitem.getProcessInstance().getState().toString());
		} catch (AssignmentException e) {
			JPAService.getInstance().setRollbackOnly();
			back.put("exception", "AssignmentException");
			back.put("fromActId", e.getTaskInstance().getActivityInstance().getActivityId());
			back.put("fromActName", e.getTaskInstance().getActivityInstance().getName());
		} catch (ExpressionException e) {
			JPAService.getInstance().setRollbackOnly();
			back.put("exception", "ExpressionException");
			back.put("keys", e.getKeys());
		} catch(DolphinException e) {
			e.printStackTrace();
			JPAService.getInstance().setRollbackOnly();
		}
		renderJSON(back);
	}

	
}
