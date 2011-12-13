package com.yttimes.dolphin;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.yttimes.dolphin.WorkflowManager;
import com.yttimes.dolphin.definition.DefinitionXMLParser;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.support.JPAService;

public class ProcessSample {
	protected WorkflowManager manager;

	@BeforeClass
	public static void beforeClass() {
		JPAService.getInstance().createJPAContext();
		// upload file
		String defDirPath = Thread.currentThread().getContextClassLoader().getResource("sample").getPath();
		DefinitionXMLParser parser = new DefinitionXMLParser();
		Set<ProcessDefinition> processDef = parser.parseDir(defDirPath);

		WorkflowManager workflowManager =  new WorkflowManager();
		JPAService.getInstance().beginTransaction();
		try {
			for (ProcessDefinition process : processDef) {
				workflowManager.saveProcessDefinition(process);
			}
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			JPAService.getInstance().commitTransaction();
		}
	}

	@Before
	public void setUp() {
		manager = new WorkflowManager();
	}

	@After
	public void tearDown() {
		JPAService.getInstance().end();
	}

	@AfterClass
	public static void afterClass() {
		JPAService.getInstance().releaseJPAContext();
	}
}
