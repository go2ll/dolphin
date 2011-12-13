/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/repository/Attic/ProcessInstanceRepository.java,v 1.1.2.5 2011/11/30 05:38:30 neo-cvs Exp $
 * $Revision: 1.1.2.5 $
 * $Date: 2011/11/30 05:38:30 $
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

import com.yttimes.dolphin.kernel.ActivityInstance;
import com.yttimes.dolphin.kernel.ProcessInstance;

/**
 *
 * @author Neo
 * @version $Id: ProcessInstanceRepository.java,v 1.1.2.5 2011/11/30 05:38:30 neo-cvs Exp $
 */
public interface ProcessInstanceRepository {

	void setEntityManager(EntityManager em);

	List<ProcessInstance> queryAll();
	
	List<ProcessInstance> queryByDefinitionId(long definitionId);

	ProcessInstance queryById(long id);	
	
	ProcessInstance refreshById(long id);	
	
	List<ActivityInstance> getAllActivities(long processInsId);

	void store(ProcessInstance instance);
	
	void delete(long id);
}
