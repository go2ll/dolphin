/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/Task.java,v 1.2.2.9 2011/12/06 07:00:50 neo-cvs Exp $
 * $Revision: 1.2.2.9 $
 * $Date: 2011/12/06 07:00:50 $
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

import java.util.Arrays;

import com.yttimes.dolphin.kernel.AssignmentHandler;
import com.yttimes.dolphin.support.Reflections;

/**
 *
 * @author Neo
 * @version $Id: Task.java,v 1.2.2.9 2011/12/06 07:00:50 neo-cvs Exp $
 */
public class Task {

	private String id;
	private String name;
	private Type type;
	private String application;
	private String formURL;
	private String assignment;
	private String strategy;

	public Task(String id, Type type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getHandlerClass() {
		return application;
	}

	public void setHandlerClass(String handlerClass) {
		this.application = handlerClass;
	}

	public String getFormURL() {
		return formURL;
	}

	public void setFormURL(String url) {
		this.formURL = url;
	}

	public String getAssignment() {
		return assignment;
	}

	public void setAssignment(String assignment) {
		this.assignment = assignment;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public boolean isAssignedTo(long userId) {
		if (assignment == null || assignment.isEmpty())
			return false;
		if (isUserAssignment(assignment)) {
			return assignment.indexOf(String.valueOf(userId)) >= 0;
		} else {
			AssignmentHandler assignmentObj;
			try {
				assignmentObj = (AssignmentHandler) Reflections.newObject(getAssignment());
				long[] userIds = assignmentObj.findUserIds(null);
				if (Arrays.asList(userIds).contains(userId)) {
					return true;
				}
			} catch (Exception e) {
				//when exception happens, do nothing
			}
		}
		return false;
	}

	public long[] getAssignedPerformers() {
		if (assignment == null || assignment.isEmpty())
			return null;
		if (isUserAssignment(assignment)) {
			String[] users = assignment.split(",");
			long[] userIds = new long[users.length];
			int i = 0;
			for (String user : users) {
				userIds[i++] = Long.parseLong(user);
			}
			return userIds;
		} else {
			AssignmentHandler assignmentObj;
			try {
				assignmentObj = (AssignmentHandler) Reflections.newObject(getAssignment());
				return assignmentObj.findUserIds(null);
			} catch (Exception e) {
				//when exception happens, do nothing
			}
		}
		return null;
	}

	public AssignmentPattern getAssignmentPattern() {
		System.out.println("getAssignmentPattern:" + assignment);
		System.out.println("isMember:" + AssignmentPattern.isMember(assignment));
		if (AssignmentPattern.isMember(assignment)) {
			return AssignmentPattern.valueOf(assignment);
		}
		return null;
	}

	public enum Type {
		FORM, APPLICATION
	}

	public enum AssignmentPattern {
		ALL, SELF_DEF_LEADER, SELF_DEPT_LEADER, ORG_LEADER, DEFAULT, SUBORDINATES_RECUR, SUBORDINATES_NO_RECUR, STREET, BUREAU_AND_SELF_MGR;
		static public boolean isMember(String patterName) {
			AssignmentPattern[] patterns = AssignmentPattern.values();
			for (AssignmentPattern pattern : patterns)
				if (pattern.name().equals(patterName))
					return true;
			return false;
		}
	}

	public static boolean isUserAssignment(String str) {
		if (str == null || str.isEmpty())
			return false;
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c) && c != ',')
				return false;
		}
		return true;
	}

}
