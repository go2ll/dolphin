/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/Attic/UploadXmlService.java,v 1.1.2.1 2011/09/27 03:27:36 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/09/27 03:27:36 $
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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.WorkflowManager;
import com.yttimes.dolphin.definition.DefinitionXMLParser;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.example.servlet.InitServlet;
import com.yttimes.dolphin.exception.DuplicateDefinitionException;
import com.yttimes.dolphin.support.annotation.JTATransactional;

/**
 *
 * @author Ivan Li
 * @version $Id: UploadXmlService.java,v 1.1.2.1 2011/09/27 03:27:36 neo-cvs Exp $
 */
public class UploadXmlService {

	private Logger logger = LoggerFactory.getLogger(UploadXmlService.class);

	@JTATransactional
	public void upload() throws DuplicateDefinitionException {
		String defDirPath = InitServlet.class.getResource("/sample").getPath();
		logger.debug("defDirPath:" + defDirPath);
		DefinitionXMLParser parser = new DefinitionXMLParser();
		Set<ProcessDefinition> processDef = parser.parseDir(defDirPath);
		for (ProcessDefinition process : processDef) {
			logger.debug("persist ProcessDefinition " + process.getProcessId());
			WorkflowManager workflowManager = new WorkflowManager();
			workflowManager.saveProcessDefinition(process);
		}
	}
}
