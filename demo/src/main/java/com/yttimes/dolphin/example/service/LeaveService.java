/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/example/service/Attic/LeaveService.java,v 1.1.2.4 2011/09/09 02:48:02 neo-cvs Exp $
 * $Revision: 1.1.2.4 $
 * $Date: 2011/09/09 02:48:02 $
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
package com.yttimes.dolphin.example.service;

import com.yttimes.dolphin.WorkflowManager;
import com.yttimes.dolphin.example.Leave;
import com.yttimes.dolphin.kernel.ProcessInstance;
import com.yttimes.dolphin.kernel.WorkItem;
import com.yttimes.dolphin.support.annotation.JTATransactional;
import com.yttimes.dolphin.support.annotation.JndiIntegration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author Neo
 * @version $Id: LeaveService.java,v 1.1.2.4 2011/09/09 02:48:02 neo-cvs Exp $
 */
public class LeaveService {

  private Logger logger = LoggerFactory.getLogger(LeaveService.class);
  private static final String DS_JNDI = "java:comp/env/jdbc/myDB";
  private WorkflowManager manager = new WorkflowManager();

  @JTATransactional
  public void startLeave(final long userId) throws Exception {
    manager.createProcessInstance("leave", userId).start();
  }

  @JTATransactional
  public List<WorkItem> getTodoWorkItems(final long userId) {
    return manager.getUndoneWorkItems(userId);
  }

  @JTATransactional
  public Leave getLeaveByWorkItemId(final long workItemId) throws Exception {
    WorkItem wi = manager.getWorkItemById(workItemId);
    Leave leave = null;

    DataSource ds = JndiIntegration.fromJndi(DataSource.class, DS_JNDI);
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      stmt = conn.createStatement();
      String
          sql =
          "select * from DOLPHIN_EXAMPLE_LEAVE where PROCESS_INS_ID=" + wi.getProcessInstance()
              .getId();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        leave = new Leave();
        long applier = rs.getInt("APPLIER");
        long leaveDay = rs.getInt("LEAVE_DAY");
        String reason = rs.getString("REASON");
        long processInsId = rs.getInt("PROCESS_INS_ID");
        leave.setApplierId(applier);
        leave.setLeaveDayNum(leaveDay);
        leave.setReason(reason);
        leave.setProcessInsId(processInsId);
      }
      logger.info("get leave info:" + leave);
    } catch (Exception ex) {
      throw ex;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    return leave;
  }

  @JTATransactional
  public Leave getLeaveByUserId(final long userId) throws Exception {
    Leave leave = null;

    DataSource ds = JndiIntegration.fromJndi(DataSource.class, DS_JNDI);
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      stmt = conn.createStatement();
      String sql = "select * from DOLPHIN_EXAMPLE_LEAVE where APPLIER=" + userId;
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        leave = new Leave();
        long applier = rs.getInt("APPLIER");
        long approver = rs.getInt("APPROVER");
        long leaveDay = rs.getInt("LEAVE_DAY");
        Date applyDate = rs.getDate("APPLY_DATE");
        String reason = rs.getString("REASON");
        String approveFlag = rs.getString("APPROVE_FLAG");
        long processInsId = rs.getInt("PROCESS_INS_ID");
        leave.setApplierId(applier);
        leave.setLeaveDayNum(leaveDay);
        leave.setApproverId(approver);
        leave.setApproveFlag(approveFlag);
        leave.setReason(reason);
        leave.setApplyDate(applyDate);
        leave.setProcessInsId(processInsId);
      }
      logger.info("get leave info:" + leave);
    } catch (Exception ex) {
      throw ex;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    return leave;
  }

  @JTATransactional
  public ProcessInstance getProcessInstance(final long processInsId) {
    logger.info("getProcessInstance processInsId=" + processInsId);
    return manager.getProcessInstanceById(processInsId);
  }


  @JTATransactional
  public void applyLeave(final long workItemId, final long applierId, final long dayNum,
                         final String reason) throws Exception {
    WorkItem wi = manager.getWorkItemById(workItemId);
    wi.getProcessInstance().setDataVariable("leaveDayNum", String.valueOf(dayNum));
    wi.complete();

    DataSource ds = JndiIntegration.fromJndi(DataSource.class, DS_JNDI);
    Connection conn = null;
    PreparedStatement pst = null;
    try {
      conn = ds.getConnection();
      pst =
          conn.prepareStatement(
              "insert into DOLPHIN_EXAMPLE_LEAVE(APPLIER,LEAVE_DAY,REASON,PROCESS_INS_ID,APPLY_DATE) values(?,?,?,?,?)");
      pst.setLong(1, applierId);
      pst.setLong(2, dayNum);
      pst.setString(3, reason);
      pst.setLong(4, wi.getProcessInstance().getId());
      pst.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
      pst.execute();
    } catch (Exception ex) {
      throw ex;
    } finally {
      if (pst != null) {
        pst.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }


  @JTATransactional
  public void approveLeave(final long workItemId, final long userId, final boolean approveFlag)
      throws Exception {
    WorkItem wi = manager.getWorkItemById(workItemId);
    wi.getProcessInstance().setDataVariable("approveFlag", String.valueOf(approveFlag));
    wi.complete();

    DataSource ds = JndiIntegration.fromJndi(DataSource.class, DS_JNDI);
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ds.getConnection();
      stmt = conn.createStatement();
      String
          sql =
          "update DOLPHIN_EXAMPLE_LEAVE set APPROVER=" + userId + ",APPROVE_FLAG='" + approveFlag
          + "' where PROCESS_INS_ID="
          + wi.getProcessInstance().getId();
      logger.info("approveLeave sql=" + sql);
      stmt.execute(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }
}
