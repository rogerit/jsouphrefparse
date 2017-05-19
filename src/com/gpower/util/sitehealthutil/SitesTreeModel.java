package com.gpower.util.sitehealthutil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SitesTreeModel {
	private static final Map<String,Integer> URL_ID = new HashMap<String, Integer>();
	private Set<Integer> parentsID = new HashSet<Integer>();
	private Set<Integer> childrenID = new HashSet<Integer>();
	private Integer ID = 0;
	private String url = null;

	public Set<Integer> getParentsID() {
		return parentsID;
	}

	public void setParentsID(Set<Integer> parentsID) {
		this.parentsID = parentsID;
	}

	public Set<Integer> getChildrenID() {
		return childrenID;
	}

	public void setChildrenID(Set<Integer> childrenID) {
		this.childrenID = childrenID;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
