package com.capitalone.dashboard.collector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.capitalone.dashboard.datafactory.rally.RallyDataFactory;
import com.capitalone.dashboard.model.ScopeOwnerCollectorItem;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
//import com.capitalone.dashboard.repository.ScopeOwnerRepository;
import com.capitalone.dashboard.repository.TeamRepository;

public class TeamDataClient {
	private final RallyDataFactory rallyData;
	private final TeamRepository teamRepository;
	private final FeatureCollectorRepository featureCollectorRepository;
	@Autowired
public TeamDataClient(RallyDataFactory rallyData,TeamRepository teamRepository, FeatureCollectorRepository featureCollectorRepository)
{
	this.rallyData=rallyData;
	this.teamRepository=teamRepository;
	this.featureCollectorRepository=featureCollectorRepository;
}
	public void updateMongoInfo() throws URISyntaxException, IOException{
		List<ScopeOwnerCollectorItem> users = rallyData.getUsers();
		for(ScopeOwnerCollectorItem item:users){
			item.setCollectorId(featureCollectorRepository.findByName("Rally").getId());
			try
			{
				cleanExistingItems(item);
			}
			catch(Exception e){
continue;
			}
			
			teamRepository.save(item);
		}
	}
	public void cleanExistingItems(ScopeOwnerCollectorItem item){
		List<ScopeOwnerCollectorItem> teams = teamRepository.getTeamById(item.getTeamId());
		teamRepository.delete(teams);
		
	}
}
