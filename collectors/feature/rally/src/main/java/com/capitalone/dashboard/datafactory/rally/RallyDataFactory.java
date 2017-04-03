package com.capitalone.dashboard.datafactory.rally;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.ScopeOwnerCollectorItem;
import com.rallydev.rest.RallyRestApi;

public interface RallyDataFactory {
	RallyRestApi getRallyClient() throws URISyntaxException;
	List<Scope> getProjects() throws URISyntaxException, IOException;
	List<ScopeOwnerCollectorItem> getUsers() throws URISyntaxException, IOException;
	List<Feature> getStories(String agileType,String storyType) throws URISyntaxException, IOException;
}
