package com.capitalone.dashboard.datafactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.BladeCollectorTask;
import com.capitalone.dashboard.model.EnvironmentComponent;
import com.capitalone.dashboard.model.EnvironmentStatus;
import com.capitalone.dashboard.model.SplunkEvent;
import com.capitalone.dashboard.model.UDeployApplication;
import com.capitalone.dashboard.repository.BladeApplicationRepository;
import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.ResultsReaderXml;
import com.splunk.SSLSecurityProtocol;
import com.splunk.SavedSearch;
import com.splunk.Service;
import com.splunk.ServiceArgs;

@Component
public class DeploymentDataFactoryImpl implements DeploymentDataFactory{ 
	public List<String> components;
	private static final Logger LOGGER = LoggerFactory.getLogger(BladeCollectorTask.class);
	private final BladeApplicationRepository appRepository;
    @Autowired
	public DeploymentDataFactoryImpl( BladeApplicationRepository appRepository){
		this.appRepository=appRepository;
	}
	public List<SplunkEvent> connectToSplunk() throws InterruptedException, IOException{
		List<SplunkEvent> splunkEventList = new ArrayList<SplunkEvent>();
	    HttpService.setSslSecurityProtocol( SSLSecurityProtocol.TLSv1_2 );
		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername("nayaksau");
		loginArgs.setPassword("s4ur4vn4y4k#");
		loginArgs.setHost("splunkcdl.es.ad.adp.com");
		//loginArgs.setPort(8089);

		// Create a Service instance and log in with the argument map
		Service service = Service.connect(loginArgs);
		ServiceArgs namespace = new ServiceArgs();
		namespace.setApp("ezlm_main");
	
		SavedSearch savedSearch = service.getSavedSearches(namespace).get("Bladecollector");
		Job jobSavedSearch = savedSearch.dispatch();
		 
		// Wait for the job to finish
		while (!jobSavedSearch.isDone()) {
		        Thread.sleep(500);
		}
		InputStream stream = jobSavedSearch.getResults();
		ResultsReaderXml resultsReaderNormalSearch = new ResultsReaderXml(stream);
		    HashMap<String, String> event;
		    while ((event = resultsReaderNormalSearch.getNextEvent()) != null) { 
		    	SplunkEvent splunkEvent = new SplunkEvent();
		    	splunkEvent.setHost(event.get("hosts"));
		    	splunkEvent.setPod(event.get("pod"));
		    	if(event.containsKey("components")){
		        splunkEvent.setComponents(createComponentList(event.get("components")));
		    	}
		        splunkEvent.setLastDeploymentTime(event.get("_time"));
		        splunkEvent.setdeploymentStatus(event.get("DeploymentStatus"));
		        splunkEvent.setDeploymentId(event.get("DeploymentID"));
		        splunkEventList.add(splunkEvent);
		    }
		    return splunkEventList;
		}
	public List<String> createComponentList(String components){
		List<String> componentList = new ArrayList<String>(Arrays.asList(components.split(",")));
		return componentList;
	}
	@Override
	public List<EnvironmentComponent> getEnvironmentComponent() throws InterruptedException, IOException{
		List<UDeployApplication> appList = new ArrayList<UDeployApplication>();
		List<EnvironmentComponent> environmentComponentList = new ArrayList<EnvironmentComponent>();
		List<SplunkEvent> splunkEventList = connectToSplunk();
		for(SplunkEvent splunkEvent:splunkEventList){
			UDeployApplication app = new UDeployApplication();
			app.setApplicationId(splunkEvent.getPod());
			app.setApplicationName(splunkEvent.getPod());
			app.setDescription(splunkEvent.getPod());
			app.setPushed(true);
			appList.add(app);
			if(splunkEvent.getComponents()!=null){
				
			for(String component:splunkEvent.getComponents()){
				LOGGER.info(component);
				EnvironmentComponent envComponent = new EnvironmentComponent();
				//envComponent.setId(component);
				envComponent.setComponentName(component);
				envComponent.setComponentID(component);
				//envComponent.setAsOfDate(Long.parseLong(splunkEvent.getLastDeploymentTime()));
				envComponent.setDeployed(isDeployed(splunkEvent.getDeploymentStatus()));
				//envComponent.setDeployTime(Long.parseLong(splunkEvent.getLastDeploymentTime()));
				envComponent.setEnvironmentID(splunkEvent.getHost());
				envComponent.setEnvironmentName(splunkEvent.getHost());
				environmentComponentList.add(envComponent);
			}
			}
		}
		appRepository.save(appList);
		return environmentComponentList;
	}
	@Override
	public List<EnvironmentStatus> getEnvironmentStatus() throws InterruptedException, IOException{
		List<EnvironmentStatus> environmentStatusList = new ArrayList<EnvironmentStatus>();
		List<SplunkEvent> splunkEventList = connectToSplunk();
		for(SplunkEvent splunkEvent:splunkEventList){
			if(splunkEvent.getComponents()!=null){
			for(String component:splunkEvent.getComponents()){
				EnvironmentStatus envStatus = new EnvironmentStatus();
				//envComponent.setId(component);
				envStatus.setComponentID(component);
				envStatus.setComponentName(component);
				envStatus.setEnvironmentName(splunkEvent.getHost());
				envStatus.setOnline(isDeployed(splunkEvent.getDeploymentStatus()));
				envStatus.setParentAgentName(splunkEvent.getPod());
				envStatus.setResourceName(component);
				environmentStatusList.add(envStatus);
			}
			}
		}
		return environmentStatusList;
	}
	public boolean isDeployed(String deploymentStatus){
		switch(deploymentStatus){
		case "Good":
		return true;
		case "Error":
		return false;
        default:
        return false;
		
		}
	}
}