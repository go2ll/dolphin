/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/DefinitionXMLParser.java,v 1.2.2.14 2011/12/02 08:20:06 neo-cvs Exp $
 * $Revision: 1.2.2.14 $
 * $Date: 2011/12/02 08:20:06 $
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
package com.yttimes.dolphin.definition;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.definition.Task.Type;
import com.yttimes.dolphin.exception.DolphinException;

/**
 *
 * @author Ivan Li
 * @version $Id: DefinitionXMLParser.java,v 1.2.2.14 2011/12/02 08:20:06 neo-cvs Exp $
 */
public final class DefinitionXMLParser {
	Logger logger = LoggerFactory.getLogger(DefinitionXMLParser.class);

	public Set<ProcessDefinition> parseDir(String dirPath) {
		Set<ProcessDefinition> processes = new HashSet<ProcessDefinition>();
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("xml");
			}
		});
		for (File file : files) {
			processes.add(parse(file));
		}
		return processes;
	}

	public ProcessDefinition parseFile(String filePath) {
		File file = new File(filePath);
		return parse(file);
	}

	public ProcessDefinition parseXMLStr(String xmlContent) {
		if (xmlContent == null || "".equals(xmlContent)) {
			throw new DolphinException("process content can not be empty");
		}
		SAXReader reader = new SAXReader();
		ProcessDefinition process = null;
		Reader strReader = null;
		try {
			strReader = new StringReader(xmlContent);
			Document document = reader.read(strReader);
			Element root = document.getRootElement();
			process = new ProcessDefinition(root.attributeValue("Id"), Double.parseDouble(root.attributeValue("Version")));
			process.setContent(xmlContent);
			process.setCreateDate(new Date());
			assemble(process, root);
		} catch (DocumentException e) {
			throw new DolphinException("parse xml string error", e);
		} finally {
			try {
				if (strReader != null) {
					strReader.close();
				}
			} catch (IOException e) {
				logger.error("close reader error", e);
			}
		}
		return process;
	}

	public ProcessDefinition parse(File file) {
		SAXReader reader = new SAXReader();
		ProcessDefinition process = null;
		try {
			Document document = reader.read(file);
			Element root = document.getRootElement();
			process = new ProcessDefinition(root.attributeValue("Id"), Double.parseDouble(root.attributeValue("Version")));
			process.setContent(document.asXML());
			process.setCreateDate(new Date());
			assemble(process, root);
		} catch (Exception e) {
			throw new DolphinException("parse xml error", e);
		}
		return process;
	}

	public void rebuild(ProcessDefinition process) {
		logger.debug("rebuild the process definition...");
		if (process == null) {
			throw new DolphinException("process can not be null");
		}
		if (process.getContent() == null || "".equals(process.getContent())) {
			throw new DolphinException("The content of process can not be empty");
		}
		if ("test".equals(process.getContent()))
			return;
		Reader strReader = null;
		try {
			strReader = new StringReader(process.getContent());
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(strReader);
			assemble(process, document.getRootElement());
		} catch (Exception ex) {
			throw new DolphinException("parse xml string error", ex);
		} finally {
			try {
				if (strReader != null) {
					strReader.close();
				}
			} catch (IOException e) {
				logger.error("close reader error", e);
			}
		}
	}

	private void assemble(ProcessDefinition processDef, Element root) {

		processDef.setName(root.attributeValue("Name"));
		Element activityDefsParent = root.element("Activities");
		Element transitionDefsParent = root.element("Transitions");
		processActivity(processDef, activityDefsParent);
		processDef.setTransitions(processTransition(transitionDefsParent));
	}

	private List<Transition> processTransition(Element transitionDefsParent) {
		List<Transition> transitions = new ArrayList<Transition>(transitionDefsParent.elements().size());
		for (Object o : transitionDefsParent.elements()) {
			Element e = (Element) o;
			Transition transitionDef = new Transition(e.attributeValue("From"), e.attributeValue("To"));
			transitionDef.setCondition(e.elementTextTrim("Condition"));
			transitions.add(transitionDef);
		}
		return transitions;
	}

	private void processActivity(ProcessDefinition processDef, Element activityDefsParent) {
		List<Activity> activities = new ArrayList<Activity>();
		List<Activity> endActivities = new ArrayList<Activity>();
		for (Object o : activityDefsParent.elements()) {
			Element e = (Element) o;
			Activity definition = new Activity(e.attributeValue("Id"), e.attributeValue("Name"), e.attributeValue("Type"));
			if(definition.getId()==null) throw new IllegalArgumentException("activity id is null");
			definition.setDescription(e.attributeValue("Desc"));
			definition.setJoinType(e.attributeValue("JoinType"));
			definition.setSplit(Boolean.valueOf(e.attributeValue("Split")));

			if (e.element("Tasks") != null) {
				Element tasksElement = e.element("Tasks");
				for (Object oo : tasksElement.elements("FormTask")) {
					Element ee = (Element) oo;
					Task taskDefinition = new Task(ee.attributeValue("Id"), Type.FORM);
					taskDefinition.setStrategy(ee.attributeValue("Strategy"));
					taskDefinition.setName(ee.attributeValue("Name"));
					taskDefinition.setAssignment(ee.elementTextTrim("Assignment"));
					taskDefinition.setFormURL(ee.elementTextTrim("Form"));
					definition.addTask(taskDefinition);
				}
				for (Object oo : tasksElement.elements("AppTask")) {
					Element ee = (Element) oo;
					Task taskDefinition = new Task(ee.attributeValue("Id"), Type.APPLICATION);
					taskDefinition.setStrategy(ee.attributeValue("Strategy"));
					taskDefinition.setName(ee.attributeValue("Name"));
					taskDefinition.setHandlerClass(ee.elementTextTrim("Application"));
					definition.addTask(taskDefinition);
				}
			}

			if (definition.getType() == Activity.Type.START) {
				processDef.setStartActivity(definition);
			} else if (definition.getType() == Activity.Type.END) {
				endActivities.add(definition);
			} else {
				activities.add(definition);
			}
		}

		processDef.setEndActivities(endActivities);
		processDef.setActivities(activities);
	}
}
