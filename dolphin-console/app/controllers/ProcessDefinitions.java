/**
 * $Header: /home/cvsroot/dolphin/dolphin-console/app/controllers/Attic/ProcessDefinitions.java,v 1.1.2.12 2011/09/29 05:37:48 neo-cvs Exp $
 * $Revision: 1.1.2.12 $
 * $Date: 2011/09/29 05:37:48 $
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.User;
import play.Logger;
import play.data.validation.Required;
import play.i18n.Messages;
import play.modules.router.Get;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import com.yttimes.dolphin.definition.DefinitionXMLParser;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.exception.DuplicateDefinitionException;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.play.DolTransactional;
import com.yttimes.dolphin.play.Dolphin;

/**
 *
 * @author Neo
 * @version $Id: ProcessDefinitions.java,v 1.1.2.12 2011/09/29 05:37:48 neo-cvs Exp $
 */

@CRUD.ForEntity(com.yttimes.dolphin.definition.ProcessDefinition.class)
@With(Secure.class)
public class ProcessDefinitions extends Controller {

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user.fullname);
		}
	}

	@DolTransactional
	public static void upload(@Required File uploadDefinitionFile) throws Exception {
		if (uploadDefinitionFile == null) {
			Logger.warn("no file upload!");
			flash.error(Messages.get("definition.upload.error"));
		} else {
			DefinitionXMLParser parser = new DefinitionXMLParser();
			ProcessDefinition processDef = parser.parse(uploadDefinitionFile);
			try {
				Dolphin.saveProcessDefinition(processDef);
				flash.success(Messages.get("definition.upload.success", processDef.getProcessId()));
			} catch (DuplicateDefinitionException e) {
				flash.error(Messages.get("definition.upload.duplicate"));
			}
		}
		redirect("/processdefinitions/list");
	}

	@DolTransactional
	public static void createProcessInstance(@Required Long processDefId) throws Exception {
		ProcessInstance ins = Dolphin.createProcessInstance(processDefId, 0);
		flash.success(Messages.get("definition.createinstance.success", ins.getId()));
		list(); 
	}

	@Get("/processdefinitions")
	public static void list() throws Exception {
		List<ProcessDefinition> defs = Dolphin.getAllProcessDefinitions();
		render(defs);
	}

	@Get("/xml")
	public static void xml(long defId) {
		ProcessDefinition def = Dolphin.workflowManager.getProcessDefinition(defId);
		renderText(prettyFormat(def.getContent()));
	}

	@DolTransactional
	public static void delete(Long defId) throws Exception {
		Dolphin.deleteProcessDefinition(defId);
		list();
	}

	public static void search(String defName) throws Exception {
		List<ProcessDefinition> defs = new ArrayList<ProcessDefinition>();
		if (defName != null && !"".equals(defName)) {
			defs = Dolphin.searchProcessDefinitions(defName);
		} else {
			defs = Dolphin.getAllProcessDefinitions();
		}
		render(defs);
	}

	private static String prettyFormat(String input) {
		try {
			String output = new String(input);
			output = output.replaceAll("&", "&amp;");
			output = output.replaceAll("<", "&lt;");
			output = output.replaceAll(">", "&gt;");
			return "<pre>"+output+"</pre>";
		} catch (Exception e) {
			throw new RuntimeException(e); // simple exception handling, please review it
		}
	}
}
