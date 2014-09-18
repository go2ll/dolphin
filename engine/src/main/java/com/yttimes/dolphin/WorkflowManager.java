/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/WorkflowManager.java,v 1.2.2.22 2011/12/06 07:01:08 neo-cvs Exp $
 * $Revision: 1.2.2.22 $
 * $Date: 2011/12/06 07:01:08 $
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
package com.yttimes.dolphin;

import com.yttimes.dolphin.definition.Activity;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.exception.DolphinException;
import com.yttimes.dolphin.exception.DuplicateDefinitionException;
import com.yttimes.dolphin.kernel.ActivityInstance;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.repository.DolphinProcessJPARepository;
import com.yttimes.dolphin.repository.DolphinProcessRepository;
import com.yttimes.dolphin.repository.ProcessInstanceJPARepository;
import com.yttimes.dolphin.repository.ProcessInstanceRepository;
import com.yttimes.dolphin.repository.WorkItemJPARepository;
import com.yttimes.dolphin.repository.WorkItemRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Neo
 * @version $Id: WorkflowManager.java,v 1.2.2.22 2011/12/06 07:01:08 neo-cvs Exp $
 */
public class WorkflowManager {

  Logger logger = LoggerFactory.getLogger(WorkflowManager.class);

  private ProcessInstanceRepository processInstanceRepository = new ProcessInstanceJPARepository();
  private DolphinProcessRepository dolphinProcessRepository = new DolphinProcessJPARepository();
  private WorkItemRepository workItemRepository = new WorkItemJPARepository();

  public ProcessInstance createProcessInstance(String processId, long userId) {
    ProcessDefinition processDef = getLatestVersionOfProcess(processId);
    if (processDef == null) {
      throw new IllegalArgumentException("Process id " + processId + " not defined!");
    }
    return createProcessInstance(processDef, userId);
  }

  ProcessInstance createProcessInstance(long processDefId, long userId) {
    ProcessDefinition processDef = getProcessDefinition(processDefId);
    if (processDef == null) {
      throw new IllegalArgumentException("Process definition id not defined!");
    }
    return createProcessInstance(processDef, userId);
  }

  private ProcessInstance createProcessInstance(ProcessDefinition def, long userId) {
    ProcessInstance processInstance = new ProcessInstance(def, userId);
    processInstanceRepository.store(processInstance);
    return processInstance;
  }

  public String getProcessEntryURL(String processDefId, long userId) {
    ProcessDefinition processDef = getLatestVersionOfProcess(processDefId);
    List<Activity> firstManualActivities = processDef.getFirstManualActivities();
    if (firstManualActivities.size() == 0) {
      throw new IllegalStateException("process definition has no manual activity");
    }
    if (firstManualActivities.size() == 1) {
      return firstManualActivities.get(0).getTask().getFormURL();
    }
    for (Activity activity : firstManualActivities) {
      if (activity.getTask().isAssignedTo(userId)) {
        return activity.getTask().getFormURL();
      }
    }
    return null;
  }

  public long[] getProcessEntryPerformer(String processDefId) {
    ProcessDefinition processDef = getLatestVersionOfProcess(processDefId);
    List<Activity> firstManualActivities = processDef.getFirstManualActivities();
    if (firstManualActivities.size() == 0) {
      throw new IllegalStateException("no manual activity follows start activity");
    }
    if (firstManualActivities.size() == 1) {
      return firstManualActivities.get(0).getTask().getAssignedPerformers();
    }
    return null;
  }

  void deleteProcessInstance(long insId) {
    processInstanceRepository.delete(insId);
  }

  void deleteProcessDefinition(long defId) {
    dolphinProcessRepository.delete(defId);
  }

  List<ActivityInstance> getActivities(long processInsId) {
    return processInstanceRepository.getAllActivities(processInsId);
  }

  //TODO warning:测试用例未覆盖
  ProcessDefinition getLatestVersionOfProcess(String processId) {
    List<ProcessDefinition>
        dolphinProcessList =
        dolphinProcessRepository.getProcessListByProcessId(processId);
    ProcessDefinition process = null;
    if (!dolphinProcessList.isEmpty()) {
      process = dolphinProcessList.get(0);
      process.rebuild();
    } else {
      throw new IllegalArgumentException("illegal dolphin process definition id: " + processId);
    }
    return process;
  }

  public ProcessDefinition getProcessDefinition(long id) {
    return dolphinProcessRepository.getProcessDefinition(id);
  }

  public String getProcessName(String processId) {
    return getLatestVersionOfProcess(processId).getName();
  }

  //TODO warning:测试用例未覆盖
  ProcessDefinition getProcessDefByVersion(String processId, double version) {
    ProcessDefinition
        dolphinProcess =
        dolphinProcessRepository.getProcessByVersion(processId, version);
    if (dolphinProcess != null) {
      dolphinProcess.rebuild();
    }
    return dolphinProcess;
  }

  void saveProcessDefinition(ProcessDefinition def) throws DuplicateDefinitionException {
    if (getProcessDefByVersion(def.getProcessId(), def.getVersion()) != null) {
      throw new DuplicateDefinitionException(
          "duplicate definition id=" + def.getProcessId() + " version=" + def.getVersion());
    }
    dolphinProcessRepository.save(def);
  }

  //for console
  List<ProcessDefinition> getAllProcessDefinitions() {
    return dolphinProcessRepository.getAllProcessDefinitions();
  }

  //for console
  List<ProcessDefinition> getUsedProcessDefinitions() {
    return dolphinProcessRepository.getUsedProcessDefinitions();
  }

  //for console
  void removeAllProcessDefinitions() {
    dolphinProcessRepository.removeAll();
  }

  public List<WorkItem> getUndoneWorkItems(long userId) {
    return workItemRepository.getUndoneWorkItems(userId);
  }

  public List<WorkItem> getUndoneWorkItemsByDef(long userId, long processDefId) {
    return workItemRepository.getUndoneWorkItemsByDef(userId, processDefId);
  }

  public List<WorkItem> getUndoneWorkItemsByInst(long userId, long processInsId) {
    return workItemRepository.getUndoneWorkItemsByInst(userId, processInsId);
  }

  public WorkItem getUndoneWorkItem(long userId, long processInsId) {
    List<WorkItem> results = getUndoneWorkItemsByInst(userId, processInsId);
    if (results.isEmpty()) {
      return null;
    }
    if (results.size() > 1) {
      logger.info("results:" + results);
      throw new DolphinException(
          "the size of workitems for userid " + userId + " on processId " + processInsId
          + " exceeds one! size=" + results.size());
    }
    return results.get(0);
  }

  public boolean hasUndoneWorkItem(long userId, long processInsId) {
    return getUndoneWorkItemsByInst(userId, processInsId) != null;
  }

  List<WorkItem> getAllWorkItems(long procesInsId) {
    return workItemRepository.getAllWorkItems(procesInsId);
  }

  List<WorkItem> getAllWorkItems() {
    return workItemRepository.getAllWorkItems();
  }

  public WorkItem getWorkItemById(long workItemId) {
    return workItemRepository.getWorkItemById(workItemId);
  }

  public WorkItem getWorkItem(long workItemId, long userId) {
    WorkItem workItem = getWorkItemById(workItemId);
    if (userId != workItem.getUserId()) {
      throw new UnsupportedOperationException(
          "Current user " + userId + " has no privilege to workitem@" + workItemId + "!");
    }
    return workItem;
  }

  public ProcessInstance getProcessInstanceById(long processInsId) {
    return processInstanceRepository.queryById(processInsId);
  }

  public ProcessInstance refreshProcessInstanceById(long processInsId) {
    return processInstanceRepository.refreshById(processInsId);
  }

  List<ProcessInstance> getProcessInstanceByDef(long processDefinitionId) {
    return processInstanceRepository.queryByDefinitionId(processDefinitionId);
  }

  List<ProcessDefinition> searchProcessDefinitions(String str) {
    return dolphinProcessRepository.searchProcessDefinitions(str);
  }
}
