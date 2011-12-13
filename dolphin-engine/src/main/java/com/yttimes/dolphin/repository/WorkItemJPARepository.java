package com.yttimes.dolphin.repository;

import java.util.List;

import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.kernel.WorkItem.State;
import com.yttimes.dolphin.support.JPARepository;

public class WorkItemJPARepository extends JPARepository<WorkItem, Long> implements WorkItemRepository {

	public WorkItemJPARepository() {
		entityClass = WorkItem.class;
	}  

	public WorkItem getWorkItemById(long workItemId) {
		return findById(workItemId);
	}

	public List<WorkItem> getUndoneWorkItems(long userId) {
		return getEntityManager().createQuery(" FROM WorkItem where userId = :userId and state != :state order by urgentLevel desc, id desc", WorkItem.class)
				.setParameter("userId", userId).setParameter("state", State.COMPLETED).getResultList();
	}
	
	public List<WorkItem> getUndoneWorkItemsByDef(long userId,long processDefId) {
		return getEntityManager().createQuery("select a from WorkItem a,FormTaskInstance b,ManualActivity c,ProcessInstance d,ProcessDefinition e "
				+ "where a.userId=:userId and a.state != :state and a.taskInstance.id = b.id and b.activityInstance.id = c.id and c.processInst.id = d.id and d.processDef.id = e.id and e.id=:processDefId", WorkItem.class)
				.setParameter("userId", userId).setParameter("processDefId", processDefId).setParameter("state", State.COMPLETED).getResultList();
	}		

	public List<WorkItem> getUndoneWorkItemsByInst(long userId, long processInsId) {
		return getEntityManager().createQuery("select a from WorkItem a,FormTaskInstance b,ManualActivity c,ProcessInstance d "
				+ "where a.userId=:userId and a.state != :state and a.taskInstance.id = b.id and b.activityInstance.id = c.id and c.processInst.id = d.id and d.id= :processInsId", WorkItem.class)
				.setParameter("userId", userId).setParameter("processInsId", processInsId).setParameter("state", State.COMPLETED).getResultList();
	}

	public List<WorkItem> getAllWorkItems() {
		return getEntityManager().createQuery("from WorkItem order by id desc", WorkItem.class).getResultList();
	}

	public List<WorkItem> getAllWorkItems(long procesInsId) {
		List<WorkItem> workItems = getEntityManager().createQuery(
				"select a from WorkItem a,TaskInstance b,ActivityInstance c,ProcessInstance d "
						+ "where a.taskInstance.id = b.id and b.activityInstance.id = c.id and c.processInst.id = d.id "
						+ "and d.id=:procesInsId order by a.id", WorkItem.class)
						.setParameter("procesInsId", procesInsId)
						.getResultList();
		refresh(workItems);
		return workItems;
	}


}
