/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/repository/Attic/ProcessInstanceJPARepository.java,v 1.1.2.8 2011/11/25 03:02:30 neo-cvs Exp $
 * $Revision: 1.1.2.8 $
 * $Date: 2011/11/25 03:02:30 $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.kernel.ActivityInstance;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.support.JPARepository;

/**
 *
 * @author Neo
 * @version $Id: ProcessInstanceJPARepository.java,v 1.1.2.8 2011/11/25 03:02:30 neo-cvs Exp $
 */
public class ProcessInstanceJPARepository extends JPARepository<ProcessInstance, Long> implements ProcessInstanceRepository {

	Logger logger = LoggerFactory.getLogger(ProcessInstanceJPARepository.class);
	
	public ProcessInstanceJPARepository() {
		entityClass = ProcessInstance.class;
	}

	public void store(ProcessInstance instance) {
		this.persist(instance);
	}

	public ProcessInstance refreshById(long id) {
		return refresh(findById(id));
	}
	
	public ProcessInstance queryById(long id) {
		return findById(id);
	}

	public List<ProcessInstance> queryAll() {
		return getEntityManager().createQuery("from ProcessInstance", ProcessInstance.class).getResultList();
	}

	public List<ProcessInstance> queryByDefinitionId(long definitionId) {
		List<ProcessInstance> results = getEntityManager()
				.createQuery("from ProcessInstance where processDef.id=:processDefinitionId order by updatedTime Desc", ProcessInstance.class)
				.setParameter("processDefinitionId", definitionId)
				.getResultList();
		return results;
	}

	public void delete(long id) {
		this.remove(queryById(id));		
	}

	public List<ActivityInstance> getAllActivities(long processInsId) {
		List<ActivityInstance> activities = getEntityManager()				
				.createQuery("from ActivityInstance  where processInst.id=:processInsId order by id", ActivityInstance.class)
				.setHint("org.hibernate.cacheable", false)
				.setParameter("processInsId", processInsId)
				.getResultList();
		return activities;
	}
}
