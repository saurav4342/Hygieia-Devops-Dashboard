package com.capitalone.dashboard.collector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.datafactory.rally.*;
public class ProjectDataClient {
private final RallyDataFactory rallyData;
private final ScopeRepository projectData;
private final FeatureCollectorRepository featureCollectorRepository;

	@Autowired
	public ProjectDataClient(RallyDataFactory rallyData,ScopeRepository projectData,FeatureCollectorRepository featureCollectorRepository){
		this.rallyData=rallyData;
		this.projectData=projectData;
		this.featureCollectorRepository=featureCollectorRepository;
	}
	public void updateMongoInfo() throws URISyntaxException, IOException
{
	List<Scope> projects= rallyData.getProjects();
	for(Scope scope:projects){
		scope.setCollectorId(featureCollectorRepository.findByName("Rally").getId());
		deleteExistingProject(scope);
		projectData.save(scope);
	}
	}
	public void deleteExistingProject(Scope scope){
		projectData.delete(projectData.getScopeById(scope.getpId()));
	}
}
