/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/test/java/com/yttimes/dolphin/definition/XMLParserTest.java,v 1.2.2.1 2011/12/05 03:02:08 neo-cvs Exp $
 * $Revision: 1.2.2.1 $
 * $Date: 2011/12/05 03:02:08 $
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ivan Li
 * @version $Id: XMLParserTest.java,v 1.2.2.1 2011/12/05 03:02:08 neo-cvs Exp $
 */
public class XMLParserTest {
	private ProcessDefinition p;

	private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Process Id=\"leave_sequence\" Name=\"请假流程\" Version=\"1.0\">"
			+ "<DataFields/><Participants/><Activities><Activity Id=\"startNode\" Name=\"开始\" Type=\"START\"/>"
			+ "<Activity Id=\"act_1\" Name=\"填写请假申请表\" Type=\"MANUAL\"><Tasks>"
			+ "<FormTask Id=\"applyFormTask\" Type=\"FORM\" Name=\"请假申请表\">"
			+ "<Assignment>com.yttimes.dolphin.kernel.QueryCurrentUserHandler</Assignment><Form>/apply.jsp</Form></FormTask></Tasks>"
			+ "</Activity><Activity Id=\"act_2\" Name=\"部门经理审批\" Type=\"MANUAL\"><Tasks>"
			+ "<FormTask Id=\"approvalFormTask\" Type=\"FORM\" Name=\"部门经理审批\">"
			+ "<Assignment>com.yttimes.dolphin.kernel.QueryLeaderHandler</Assignment><Form></Form></FormTask></Tasks>"
			+ "</Activity><Activity Id=\"act_3\" Name=\"打印请假单\" Type=\"AUTOMATIC\"><Tasks>"
			+ "<AppTask Id=\"sendEmailTask\" Name=\"打印请假单\">"
			+ "<Application>com.yttimes.dolphin.kernel.PrintLeaveInfoAppHandler</Application></AppTask></Tasks></Activity>"
			+ "<Activity Id=\"endNode\" Name=\"结束\" Type=\"END\"/></Activities><Transitions>"
			+ "<Transition From=\"startNode\" To=\"act_1\"><Condition/></Transition>"
			+ "<Transition From=\"act_1\" To=\"act_2\"><Condition/></Transition><Transition From=\"act_2\" To=\"act_3\">"
			+ "<Condition/></Transition><Transition From=\"act_3\" To=\"endNode\"><Condition/></Transition></Transitions></Process>";

	@Before
	public void setUp() {
		DefinitionXMLParser parser = new DefinitionXMLParser();
		p = parser.parseXMLStr(xml);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testProcessIsNotNull() {
		assertNotNull(p);
	}

	@Test
	public void testProcessDefinitions() {
		assertEquals("leave_sequence", p.getProcessId());
		assertEquals("请假流程", p.getName());
	}

	@Test
	public void testStartNode() {
		assertNotNull(p.getStartActivity());
		assertEquals("startNode", p.getStartActivity().getId());
	}

	@Test
	public void testActivity() {
		assertEquals(3, p.getActivities().size());
		assertEquals(1, p.getEndActivities().size());
	}
	
	@Test
	public void testManualActivity() {
		boolean f = false;
		for(Activity activity: p.getActivities()) {
			if("act_1".equals(activity.getId())) {
				Set<Task> tasks = activity.getTasks();
				assertEquals(1,tasks.size());
				Task task = tasks.iterator().next();
				assertEquals("applyFormTask",task.getId());
				assertEquals("/apply.jsp",task.getFormURL());
				f=true;
			}
		}
		assertTrue(f);
	}

	@Test
	public void testGetNodeTransitions() {
		Activity startNode = p.getStartActivity();
		List<Transition> transitions = p.getNextTransitions(startNode.getId());
		assertEquals(1, transitions.size());
		assertEquals("act_1", transitions.get(0).toActivity());
	}
}
