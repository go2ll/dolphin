/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/controllers/Attic/ProcessInstances.java,v 1.1.2.25 2011/12/05 02:58:33 yinxiong-cvs Exp $
 * $Revision: 1.1.2.25 $
 * $Date: 2011/12/05 02:58:33 $
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
import java.util.List;
import java.util.Map;

import play.Logger;
import play.modules.router.Get;
import play.mvc.Controller;
import play.mvc.With;

import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.exception.AssignmentException;
import com.yttimes.dolphin.exception.ConfusedTransitionException;
import com.yttimes.dolphin.exception.ExpressionException;
import com.yttimes.dolphin.kernel.ActivityInstance;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.play.DolTransactional;
import com.yttimes.dolphin.play.Dolphin;
import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: ProcessInstances.java,v 1.1.2.25 2011/12/05 02:58:33 yinxiong-cvs Exp $
 */

@CRUD.ForEntity(com.yttimes.dolphin.kernel.ProcessInstance.class)
@With(Secure.class)
public class ProcessInstances extends Controller {

	private static ProcessInstance currentProcess;

	@Get("/processinstances")
	public static void index(Long defId, Long instId) throws Exception {
		List<ProcessDefinition> defs = Dolphin.getUsedProcessDefinitions();
		if (!defs.isEmpty()) {
			boolean flag = false;
			if (defId != null) {
				for (ProcessDefinition def : defs) {
					if (def.getId() == defId) {
						flag = true;
						break;
					}
				}
			}
			if (!flag && defs.size() > 0) {
				defId = defs.get(0).getId();
			}
		}
		render(defId, instId, defs);
	}

	public static void list(Long defId, Long instId) throws Exception {
		List<ProcessInstance> insts = Dolphin.getProcessInstancesByDef(defId);
		if (!insts.isEmpty()) {
			boolean flag = false;
			if (instId != null) {
				for (ProcessInstance inst : insts) {
					if (inst.getId() == instId) {
						flag = true;
						break;
					}
				}
			}
			if (!flag && insts.size() > 0) {
				instId = insts.get(0).getId();
			}
		}
		render(instId, insts);
	}

	public static void detail(Long instId) {
		Logger.info("detailbyinstid");
		ProcessInstance inst = Dolphin.workflowManager.refreshProcessInstanceById(instId);
		render(inst);
	}

	@DolTransactional
	public static void run(Long instId) {
		Map<String, Object> back = new HashMap<String, Object>();
		try {
			Dolphin.runProcess(instId);
		} catch (AssignmentException e) {
			JPAService.getInstance().setRollbackOnly();
			back.put("exception", "AssignmentException");
			back.put("fromActId", e.getTaskInstance().getActivityInstance().getActivityId());
			back.put("fromActName", e.getTaskInstance().getActivityInstance().getName());
		} catch (ExpressionException e) {
			JPAService.getInstance().setRollbackOnly();
			back.put("exception", "ExpressionException");
			back.put("keys", e.getKeys());
		} catch (ConfusedTransitionException e) {
			JPAService.getInstance().setRollbackOnly();
			back.put("exception", "ConfusedTransitionException");
			back.put("activities", e.getNextActivitiesId());
		} catch (Throwable e) {
			e.printStackTrace();
			JPAService.getInstance().setRollbackOnly();
		}
		renderJSON(back);
	}

	@DolTransactional
	public static void undo(Long instId) throws Throwable {
		Dolphin.undoProcess(instId);
	}

	public static void assign(Long instId, String fromActId, String fromActName) {
		render(instId, fromActId, fromActName);
	}

	@DolTransactional
	public static void assignAndRun(Long instId, String actId, String userId) {
		Logger.info("assignAndRun instId=" + instId + ",actId=" + actId + ",userId=" + userId);
		Map<String, String> back = new HashMap<String, String>();
		String[] users = userId.split(",");
		long[] userIds = new long[users.length];
		int i = 0;
		ProcessInstance inst = null;
		for (String user : users) {
			userIds[i++] = Long.parseLong(user);
		}
		try {
			inst = Dolphin.workflowManager.getProcessInstanceById(instId);
			Dolphin.assignActivityToUsers(inst, actId, userIds);
			if (currentProcess != null && currentProcess.getId() == inst.getId()) {
				Logger.info("we got unassigned process!");
				Logger.info("currentProcess.getDynamicAssignmentMap():" + currentProcess.getDynamicAssignmentMap());
				inst.getDynamicAssignmentMap().putAll(currentProcess.getDynamicAssignmentMap());
			}
			Dolphin.runProcess(inst);
		} catch (AssignmentException e) {
			Logger.error("AssignmentException", e);
			JPAService.getInstance().setRollbackOnly();
			back.put("exception", "AssignmentException");
			back.put("fromActId", e.getTaskInstance().getActivityInstance().getActivityId());
			back.put("fromActName", e.getTaskInstance().getActivityInstance().getName());
			currentProcess = inst;
		} catch (Throwable e) {
			JPAService.getInstance().setRollbackOnly();
			e.printStackTrace();
		}
		renderJSON(back);
	}

	@DolTransactional
	public static void delete(Long instId) throws Exception {
		Dolphin.deleteProcessInstance(instId);
	}

	@DolTransactional
	public static void cancel(Long instId) throws Throwable {
		Dolphin.cancelProcess(instId);
	}

	public static void variables(Long instId, String mode) {
		Map<String, String> dataVariables = new HashMap<String, String>();
		ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
		if (inst != null) {
			dataVariables = inst.getDataVariableMap();
		}
		render(inst, dataVariables, mode);
	}

	@DolTransactional
	public static void addvariable(Long instId, String key, String value) {
		if (key != null && value != null && !"".equals(key) && !"".equals(value)) {
			if (instId != null) {
				ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
				inst.setDataVariable(key, value);
				variables(instId, null);
			}
		} else {
			Logger.error("the key or value is null");
		}
	}

	@DolTransactional
	public static void updatevariables(Long instId, String key, String value) {
		String keys[] = key.split(",");
		String values[] = value.split(",");
		try {
			if (keys.length > 0 && values.length > 0) {
				if (instId != null) {
					ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
					for (int i = 0; i < keys.length; i++) {
						if (keys[i] != null && values[i] != null && !"".equals(keys[i]) && !"".equals(values[i])) {
							inst.setDataVariable(keys[i], values[i]);
						} else {
							Logger.error(keys[i] + " is null");
						}
					}
					index(null, null);
				}
			} else {
				Logger.error("the key or value is null");
			}
		} catch (Exception e) {
			Logger.error("error when update variables");
		}
	}

	@DolTransactional
	public static void clearvariables(Long instId) {
		if (instId != null) {
			ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
			inst.clearDataVariables();
		} else {
			Logger.error("the instance id is null");
		}
	}

	public static void activities(Long instId) throws Exception {
		ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
		List<ActivityInstance> activityInsts = Dolphin.getActivities(instId);
		render(inst, activityInsts);
	}

	public static void workitems(Long instId) throws Exception {
		List<WorkItem> workitems = Dolphin.getAllWorkItems(instId);
		render(workitems);
	}

	public static void assignVariables(Long instId, String keys) {
		render(instId, keys);
	}

	public static void assignNextActivity(Long instId, String activities) {
		render(instId, activities);
	}

	@DolTransactional
	public static void addAssignVariables(Long instId, String keys, String values) {
		String ks[] = keys.split(",");
		String vs[] = values.split(",");
		try {
			if (ks.length > 0 && vs.length > 0) {
				if (instId != null) {
					ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
					for (int i = 0; i < ks.length; i++) {
						if (ks[i] != null && vs[i] != null && !"".equals(ks[i]) && !"".equals(vs[i])) {
							inst.setDataVariable(ks[i], vs[i]);
						} else {
							Logger.error(ks[i] + " is null");
						}
					}
				}
			} else {
				Logger.error("the key or value is null");
			}
		} catch (Exception e) {
			Logger.error("error when assign variables");
		}
	}

	@DolTransactional
	public static void runToNextActivity(Long instId, String activityId) throws Throwable {
		Logger.info("runToNextActivity activityId=" + activityId);
		ProcessInstance inst = Dolphin.workflowManager.getProcessInstanceById(instId);
		inst.setNextActivity(activityId);
		Dolphin.runProcess(inst);
	}
}
