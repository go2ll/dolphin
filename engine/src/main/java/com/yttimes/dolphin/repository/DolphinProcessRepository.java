/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/repository/Attic/DolphinProcessRepository.java,v 1.1.2.5 2011/09/09 04:36:17 neo-cvs Exp $
 * $Revision: 1.1.2.5 $
 * $Date: 2011/09/09 04:36:17 $
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

import com.yttimes.dolphin.definition.ProcessDefinition;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * @author Neo
 * @version $Id: DolphinProcessRepository.java,v 1.1.2.5 2011/09/09 04:36:17 neo-cvs Exp $
 */
public interface DolphinProcessRepository {

  void setEntityManager(EntityManager em);

  void save(ProcessDefinition def);

  void delete(long id);

  void removeAll();

  List<ProcessDefinition> getAllProcessDefinitions();

  List<ProcessDefinition> getUsedProcessDefinitions();

  List<ProcessDefinition> getProcessListByProcessId(String processId);

  ProcessDefinition getProcessByVersion(String processId, double version);

  ProcessDefinition getProcessDefinition(long id);

  List<ProcessDefinition> searchProcessDefinitions(String str);

}