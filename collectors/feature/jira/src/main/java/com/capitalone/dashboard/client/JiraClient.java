package com.capitalone.dashboard.client;

import java.util.List;
import java.util.Map;

import com.capitalone.dashboard.domain.*;
import com.atlassian.jira.rest.client.api.domain.Issue;

public interface JiraClient {
	List<HierarchyRequirement> getIssues(long startTime, int pageStart);
	
	List<Project> getProjects();
	
	int getPageSize();

	List<Issue> getEpics(List<String> epicKeys);
	
	Map<String, String> getStatusMapping();
}
