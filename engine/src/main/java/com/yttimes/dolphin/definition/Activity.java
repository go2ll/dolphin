/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/Activity.java,v 1.2.2.7 2011/12/01 06:33:05 neo-cvs Exp $
 * $Revision: 1.2.2.7 $
 * $Date: 2011/12/01 06:33:05 $
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

import java.util.HashSet;
import java.util.Set;

/**
 * @author Neo
 * @version $Id: Activity.java,v 1.2.2.7 2011/12/01 06:33:05 neo-cvs Exp $
 */
public class Activity {

  private String id;
  private String name;
  private String description;

  private Type type;
  private JoinType joinType;
  private Set<Task> tasks;
  private boolean isSplit;


  public Activity(String id, String name, Type type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public Activity(String id, String name, String type) {
    this(id, name, Type.valueOf(type));
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String desc) {
    this.description = desc;
  }

  public Type getType() {
    return type;
  }

  public JoinType getJoinType() {
    return joinType;
  }

  public void setJoinType(JoinType joinType) {
    this.joinType = joinType;
  }

  public void setJoinType(String joinType) {
    if (joinType == null || joinType.isEmpty()) {
      this.joinType = JoinType.OR;
    } else {
      this.joinType = JoinType.valueOf(joinType);
    }
  }

  public boolean isSplit() {
    return isSplit;
  }

  public void setSplit(boolean isSplit) {
    this.isSplit = isSplit;
  }


  public Set<Task> getTasks() {
    if (tasks == null) {
      tasks = new HashSet<Task>();
    }
    return tasks;
  }

  public Task getTask(String taskId) {
    for (Task task : getTasks()) {
      if (task.getId().equals(taskId)) {
        return task;
      }
    }
    return null;
  }

  public Task getTask() {
    return tasks == null ? null : tasks.iterator().next();
  }

  public void addTask(Task task) {
    getTasks().add(task);
  }

  public enum Type {
    MANUAL, AUTOMATIC, ROUTE, START, END
  }

  public enum JoinType {
    AND, OR, XOR
  }
}
