package com.capitalone.dashboard.datafactory.rally;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.capitalone.dashboard.collector.FeatureSettings;
import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.model.Iteration;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.model.ScopeOwnerCollectorItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
public class RallyDataFactoryImpl implements RallyDataFactory {
	private FeatureSettings featureSettings;
	@Autowired
	public RallyDataFactoryImpl(FeatureSettings featureSettings){
		this.featureSettings=featureSettings;
	}
public RallyRestApi getRallyClient() throws URISyntaxException
{
	URI url = new URI(featureSettings.getRallyBaseUri());
	String key = featureSettings.getRallyApiKey();
	return new RallyRestApi(url,key);
	}
public List<Scope> getProjects() throws URISyntaxException, IOException
{  
	List<Scope> projects = new ArrayList<>();
	RallyRestApi restApi=getRallyClient();
	QueryRequest request = new QueryRequest("Projects");
	   request.setFetch(new Fetch("Name","ObjectID","CreationDate","State"));
	   request.setScopedDown(true);
	//  request.setQueryFilter(new QueryFilter("State","=","Planning"));
	   QueryResponse response = restApi.query(request);
	   if(response.getResults()!=null){
	   for(JsonElement result : response.getResults()){
		   JsonObject project = result.getAsJsonObject();
		   if(project.get("Name").getAsString().contains(featureSettings.getApplicationName())){
		  Scope scope = new Scope();
		  scope.setpId(project.get("ObjectID").getAsString());
		  scope.setName(project.get("Name").getAsString());
		  scope.setBeginDate(project.get("CreationDate").getAsString());
		  scope.setAssetState("Active");
          projects.add(scope);
	   }
	   }
	   }
	   restApi.close();
	   return projects;
}
public List<ScopeOwnerCollectorItem> getUsers() throws URISyntaxException, IOException
{
List<ScopeOwnerCollectorItem> users = new ArrayList<>();
RallyRestApi restApi=getRallyClient();
QueryRequest request = new QueryRequest("Projects");
   request.setFetch(new Fetch("Name","ObjectID","CreationDate","State"));
   request.setScopedDown(true);
//  request.setQueryFilter(new QueryFilter("State","=","Planning"));
   QueryResponse response = restApi.query(request);
   if(response.getResults()!=null){
   for(JsonElement result : response.getResults()){
	   JsonObject project = result.getAsJsonObject();
	   if(project.get("Name").getAsString().contains(featureSettings.getApplicationName())){
	   ScopeOwnerCollectorItem user = new ScopeOwnerCollectorItem();   
	  user.setTeamId((project.get("ObjectID").getAsString()));
	  user.setName(project.get("Name").getAsString());
	  user.setAssetState("Active");
      users.add(user);
   }
   }
   }
   return users;
}
@SuppressWarnings("unchecked")
public List<Feature> getStories(String agileType,String storyType) throws URISyntaxException, IOException{
	List<Feature> stories = new ArrayList<>();
	RallyRestApi restApi=getRallyClient();	
	String projectRef = "/project/";
	Iteration it = getCurrentIteration();
	List<ScopeOwnerCollectorItem> users= getUsers();
	for(ScopeOwnerCollectorItem user:users){
		String objectID=user.getTeamId();
		QueryRequest request=null;
		if(storyType.equals("story")){
	 request = new QueryRequest("HierarchicalRequirement");
		}
		else if(storyType.equals("defect")){
	 request = new QueryRequest("Defects");
	
		}
	   request.setFetch(new Fetch("Name","ObjectID","FormattedID","PlanEstimate","TaskStatus","LastUpdateDate","ScheduleState","Owner","Project","Iteration","Parent"));
	   request.setScopedDown(true);
	   request.setProject(projectRef+objectID);
	   if(agileType.equals("scrum")){
	  request.setQueryFilter(new QueryFilter("Iteration.Name","=",it.getName()));
	   }
	   else if(agileType.equals("kanban")){
			  request.setQueryFilter(new QueryFilter("Iteration.Name","=",null).and(new QueryFilter("ScheduleState","!=","Backlog")));
	   }
	   QueryResponse response = restApi.query(request);
	   if(response.getResults()!=null){
	   for(JsonElement result : response.getResults()){
		   Feature feature = new Feature();
		   JsonObject project = result.getAsJsonObject();
		 feature.setsId(project.get("ObjectID").getAsString());
		 feature.setsName(project.get("Name").getAsString());
		 feature.setsNumber(project.get("FormattedID").getAsString());
		 feature.setsStatus(project.get("ScheduleState").getAsString());
		 feature.setIsDeleted("False");
		 //feature.setsState(project.get("TaskStatus").getAsString());
		 if(!project.get("PlanEstimate").isJsonNull()){
		 feature.setsEstimate(project.get("PlanEstimate").getAsString());
	   }
		 feature.setChangeDate(project.get("LastUpdateDate").getAsString());
		 feature.setsProjectName(project.getAsJsonObject("Project").get("_refObjectName").getAsString());
		 feature.setsProjectID(project.getAsJsonObject("Project").get("ObjectID").getAsString());
    	 try{
		 if(!project.get("Parent").isJsonNull()){
			 feature.setsEpicID(project.getAsJsonObject("Parent").get("ObjectID").getAsString());
			feature.setsEpicName(project.getAsJsonObject("Parent").get("_refObjectName").getAsString());	
			
          }
    	 }
    	 catch(Exception e){
    		 feature.setsEpicID(""); 
    	 }
         if(!project.get("Iteration").isJsonNull()){
	     feature.setsSprintID(it.getID());
	      feature.setsSprintName(it.getName());
	      feature.setsSprintBeginDate(it.getBeginDate());
			feature.setsSprintEndDate(it.getEndDate());
			feature.setsSprintAssetState("Active");
        }
         else {
				// Issue #678 - leave sprint blank. Not having a sprint does not imply kanban
				// as a story on a scrum board without a sprint is really on the backlog
				// Instead the feature service is responsible for deducing if a sprint is part of
				// kanban - see service for more details
				feature.setsSprintID("");
				feature.setsSprintName("");
				feature.setsSprintBeginDate("");
				feature.setsSprintEndDate("");
				//feature.setsSprintAssetState(getJSONString(dataMainObj, "Timebox.AssetState"));
			}
         feature.setsTeamID(project.getAsJsonObject("Project").get("ObjectID").getAsString());
         
			// sTeamName
         //List<String> ownerID= new ArrayList<>();
			feature.setsTeamName(project.getAsJsonObject("Project").get("_refObjectName").getAsString());
			//ownerID.add(project.getAsJsonObject("Owner").get("ObjectID").getAsString());
		//	feature.setsOwnersID(ownerID);
	   stories.add(feature);
	   }
	   }
	}
	   return stories;

}


public Iteration getCurrentIteration() throws IOException, URISyntaxException{
	RallyRestApi restApi=getRallyClient();
	Iteration it = new Iteration();
	String projectRef="/project/"+featureSettings.getProjectObjectId();
	String now = LocalDateTime.now().toString();
	QueryRequest request = new QueryRequest("Iteration");
	 request.setFetch(new Fetch("Name","ObjectID","StartDate","EndDate"));
	 request.setProject(projectRef); 
	 request.setQueryFilter(new QueryFilter("StartDate","<=",now).and(new QueryFilter("EndDate",">=",now)));
	 request.setScopedDown(true);  
	   QueryResponse response = restApi.query(request);
	   if(response.getResults()!=null){
		   for(JsonElement result : response.getResults()){
			   JsonObject iteration = result.getAsJsonObject();
		   it.setID(iteration.get("ObjectID").getAsString());
		   it.setName(iteration.get("Name").getAsString());
		   it.setStartDate(iteration.get("StartDate").getAsString());
		   it.setEndDate(iteration.get("EndDate").getAsString());
		   }
}
return it;
}
}
