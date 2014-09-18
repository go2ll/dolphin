/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/Application.java,v 1.2 2011/07/25 06:53:33 ivan-cvs Exp $
 * $Revision: 1.2 $
 * $Date: 2011/07/25 06:53:33 $
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
 * @version $Id: Application.java,v 1.2 2011/07/25 06:53:33 ivan-cvs Exp $
 */
public class Application {

  private String id;
  private String clazz;
  private String description;
  private Set<FormalParameter> formalParameterDefs;

  public Application(String id, String clazz) {
    this.id = id;
    this.clazz = clazz;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the clazz
   */
  public String getClazz() {
    return clazz;
  }

  /**
   * @return the formalParameterDefs
   */
  public Set<FormalParameter> getFormalParameterDefs() {
    if (formalParameterDefs == null) {
      formalParameterDefs = new HashSet<FormalParameter>();
    }
    return formalParameterDefs;
  }

  public void addFormalParameterDef(FormalParameter formalParameterDef) {
    getFormalParameterDefs().add(formalParameterDef);
  }

  @Override
  public int hashCode() {
    final int prime = 41;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Application other = (Application) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

}
