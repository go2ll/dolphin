/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/repository/Attic/WorkItemRepository.java,v 1.1.2.7 2011/09/29 05:45:44 neo-cvs Exp $
 * $Revision: 1.1.2.7 $
 * $Date: 2011/09/29 05:45:44 $
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
package com.yttimes.dolphin.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.yttimes.dolphin.kernel.WorkItem;


/**
 *
 * @author Neo
 * @version $Id: WorkItemRepository.java,v 1.1.2.7 2011/09/29 05:45:44 neo-cvs Exp $
 */
public interface WorkItemRepository {	
	
	public WorkItem getWorkItemById(long workItemId);
	
	public void setEntityManager(EntityManager em);

	public List<WorkItem> getUndoneWorkItems(long userId);
	
	public List<WorkItem> getUndoneWorkItemsByDef(long userId,long processDefId);
	
	public List<WorkItem> getUndoneWorkItemsByInst(long userId,long processInsId);
	
	public List<WorkItem> getAllWorkItems();
	
	public List<WorkItem> getAllWorkItems(long processDefId);
}