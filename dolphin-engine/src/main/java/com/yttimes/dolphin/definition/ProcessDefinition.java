/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/definition/ProcessDefinition.java,v 1.2.2.8 2011/10/12 06:26:06 neo-cvs Exp $
 * $Revision: 1.2.2.8 $
 * $Date: 2011/10/12 06:26:06 $
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.yttimes.dolphin.kernel.ProcessInstance;

/**
 *
 * @author Neo
 * @version $Id: ProcessDefinition.java,v 1.2.2.8 2011/10/12 06:26:06 neo-cvs Exp $
 */
@Entity
@Table(name = "DOL_PROCESS_DEFINITION")
public class ProcessDefinition {

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private long id;

	@Column(name = "PROCESS_ID", nullable = false)
	private String processId;

	@Column(name = "NAME", nullable = true)
	private String name;

	@Column(name = "VERSION", nullable = false)
	private double version;

	@Lob
	@Column(name = "CONTENT", nullable = false)
	private String content;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", nullable = false)
	private Date createDate;
	
	@OneToMany(mappedBy="processDef",cascade={CascadeType.REMOVE})
	private List<ProcessInstance> processInstances;

	@Transient
	private Activity startActivity;
	@Transient
	private List<Activity> endActivities;
	@Transient
	private List<Activity> activities;
	@Transient
	private List<Transition> transitions;

	public ProcessDefinition() {
	}

	public ProcessDefinition(String processId, double version) {
		this.processId = processId;
		this.version = version;
		this.createDate = new Date();
	}

	public long getId() {
		return id;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getProcessId() {
		return processId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Activity getStartActivity() {
		return startActivity;
	}

	public void setStartActivity(Activity startActivity) {
		this.startActivity = startActivity;
	}

	public List<Activity> getEndActivities() {
		return endActivities;
	}

	public void setEndActivities(List<Activity> endActivities) {
		this.endActivities = endActivities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public Set<Activity> getAllActivities() {
		Set<Activity> allActivities = new HashSet<Activity>();
		allActivities.add(startActivity);
		allActivities.addAll(getActivities());
		allActivities.addAll(getEndActivities());
		return allActivities;
	}
	
	public Activity getActivity(String activityId) {
		Set<Activity> activities = getAllActivities();
		for(Activity activity:activities) {
			if(activity.getId().equals(activityId)) {
				return activity;
			}
		}
		return null;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public List<String> getPreviousActivities(String activityId) {
		List<String> preActivities = new ArrayList<String>();
		for (Transition transition : this.transitions) {
			if (transition.toActivity().equals(activityId)) {
				preActivities.add(transition.fromActivity());
			}
		}
		return preActivities;
	}
	
	public List<String> getNextActivitiesId(String activityId) {
		List<String> nextActivities = new ArrayList<String>();
		for (Transition transition : this.transitions) {
			if (transition.fromActivity().equals(activityId)) {
				nextActivities.add(transition.toActivity());
			}
		}
		return nextActivities;
	}
	
	public List<Activity> getNextActivities(String activityId) {
		List<Activity> nextActivities = new ArrayList<Activity>();
		for (Transition transition : this.transitions) {
			if (transition.fromActivity().equals(activityId)) {
				nextActivities.add(getActivity(transition.toActivity()));
			}
		}
		return nextActivities;
	}

	public List<Transition> getPreviousTransitions(String activityId) {
		List<Transition> preTransitions = new ArrayList<Transition>();
		for (Transition transition : this.transitions) {
			if (transition.toActivity().equals(activityId)) {
				preTransitions.add(transition);
			}
		}
		return preTransitions;
	}
	
	
	public List<Transition> getNextTransitions(String activityId) {
		List<Transition> nextTransitions = new ArrayList<Transition>();
		for (Transition transition : this.transitions) {
			if (transition.fromActivity().equals(activityId)) {
				nextTransitions.add(transition);
			}
		}
		return nextTransitions;
	}

	public void rebuild() {
		DefinitionXMLParser parser = new DefinitionXMLParser();
		parser.rebuild(this);
	}	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ProcessInstance> getProcessInstances() {
		 Collections.sort(processInstances, new Comparator() {
			public int compare(Object o1, Object o2) {
				ProcessInstance ins1 = (ProcessInstance)o1;
				ProcessInstance ins2 = (ProcessInstance)o2;
				return ins2.getUpdatedTime().compareTo(ins1.getUpdatedTime());
			}			 
		 });
		 return processInstances;
	}
	
	
	public List<Activity> getFirstManualActivities() {
		Activity startActivity = getStartActivity();
		return getNextActivities(startActivity.getId());		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processId == null) ? 0 : processId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(version);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessDefinition other = (ProcessDefinition) obj;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		if (Double.doubleToLongBits(version) != Double.doubleToLongBits(other.version))
			return false;
		return true;
	}

}
