package com.yttimes.dolphin;

import com.yttimes.dolphin.definition.DefinitionXMLParser;
import com.yttimes.dolphin.definition.ProcessDefinition;
import com.yttimes.dolphin.support.JPAService;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ProcessSample {

  protected static Logger logger = LoggerFactory.getLogger(ProcessSample.class);
  protected WorkflowManager manager;

  @BeforeClass
  public static void beforeClass() {
    JPAService.getInstance().createJPAContext();
    // upload file
    String
        defDirPath =
        Thread.currentThread().getContextClassLoader().getResource("sample").getPath();
    DefinitionXMLParser parser = new DefinitionXMLParser();
    Set<ProcessDefinition> processDef = parser.parseDir(defDirPath);
    logger.debug("ddddddddddd" + defDirPath + ", " + processDef.size());

    WorkflowManager workflowManager = new WorkflowManager();
    JPAService.getInstance().beginTransaction();
    try {
      for (ProcessDefinition process : processDef) {
        logger.debug("id = " + process.getProcessId());
        workflowManager.saveProcessDefinition(process);
      }
    } catch (Exception e) {
      logger.error("error", e);
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
