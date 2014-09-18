/**
 * $Header: /home/cvsroot/dolphin/LeaveProcessDemo/src/main/java/com/yttimes/dolphin/example/Leave.java,v 1.2.2.1 2011/07/27 02:58:59 ivan-cvs Exp $
 * $Revision: 1.2.2.1 $
 * $Date: 2011/07/27 02:58:59 $
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
package com.yttimes.dolphin.example;

import com.yttimes.dolphin.example.service.LeaveService;
import com.yttimes.dolphin.kernel.ProcessInstance;

import java.util.Date;

/**
 * @author Neo
 * @version $Id: Leave.java,v 1.2.2.1 2011/07/27 02:58:59 ivan-cvs Exp $
 */
public class Leave {

  private long applierId;

  private long leaveDayNum;

  private long approverId;

  private String reason;

  private long processInsId;

  private String approveFlag;

  private Date applyDate;

  public long getApplierId() {
    return applierId;
  }

  public void setApplierId(long applierId) {
    this.applierId = applierId;
  }

  public long getLeaveDayNum() {
    return leaveDayNum;
  }

  public void setLeaveDayNum(long leaveDayNum) {
    this.leaveDayNum = leaveDayNum;
  }

  public long getApproverId() {
    return approverId;
  }

  public void setApproverId(long approverId) {
    this.approverId = approverId;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public long getProcessInsId() {
    return processInsId;
  }

  public void setProcessInsId(long processInsId) {
    this.processInsId = processInsId;
  }

  public ProcessInstance getProcessInstance() {
    return new LeaveService().getProcessInstance(processInsId);
  }

  public String getApproveFlag() {
    return approveFlag;
  }

  public void setApproveFlag(String approveFlag) {
    this.approveFlag = approveFlag;
  }

  public Date getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(Date applyDate) {
    this.applyDate = applyDate;
  }

  @Override
  public String toString() {
    return "Leave [applierId=" + applierId + ", leaveDayNum=" + leaveDayNum + ", approverId="
           + approverId + ", reason=" + reason
           + ", processInsId=" + processInsId + ", approveFlag=" + approveFlag + ", applyDate="
           + applyDate + "]";
  }


}
