package com.capitalone.dashboard.datafactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.Deployment;
import com.capitalone.dashboard.model.DeploymentMap;
import com.capitalone.dashboard.model.DeploymentTask;
import com.capitalone.dashboard.model.Host;
import com.capitalone.dashboard.model.Pod;
import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.ResultsReaderXml;
import com.splunk.SSLSecurityProtocol;
import com.splunk.SavedSearch;
import com.splunk.Service;
import com.splunk.ServiceArgs;

@Component
public class DeploymentDataFactoryImpl implements DeploymentDataFactory{ 
	//private final Logger LOGGER = LoggerFactory.getLogger(DeploymentDataFactoryImpl.class);
	public DeploymentDataFactoryImpl( ){
		
	}
    
	public List<DeploymentTask> connectToSplunk() throws InterruptedException, IOException{
		List<DeploymentTask> taskList = new ArrayList<DeploymentTask>();
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
		    	DeploymentTask task = new DeploymentTask();
		    	if(event.containsKey("hosts")){
		    	task.setHost(event.get("hosts"));
		    	}
		    	if(event.containsKey("pod")){
		    	task.setPodName(event.get("pod"));
		    	
		    	}
	    	   if(event.containsKey("components")){
		        task.setComponents(createComponentList(event.get("components")));
		    	}
		    	if(event.containsKey("_time")){
		        task.setLastDeploymentTime(event.get("_time"));
		    	}
		    	if(event.containsKey("DeploymentStatus")){
		        task.setDeploymentStatus(event.get("DeploymentStatus"));
		    	}
		    	if(event.containsKey("DeploymentID")){
		        task.setDeploymentId(event.get("DeploymentID"));
		    	}
		        taskList.add(task);
		    }
		    return taskList;
		}
	public List<String> createComponentList(String components){
		List<String> componentList = new ArrayList<String>(Arrays.asList(components.split(",")));
		return componentList;
	}
	
	public List<Pod> createPods(List<DeploymentTask> taskList){
		List<Pod> podList = new ArrayList<Pod>();
		for(DeploymentTask task:taskList){
			Pod pod = new Pod();
			pod.setPod(task.getPodName());
			podList.add(pod);
		}
		return podList;
	}
	public Map<String,Deployment> createDeploymentMap(List<DeploymentTask> taskList){
		Map<String,Deployment> deploymentMap = new HashMap<String,Deployment>();
		for(DeploymentTask task : taskList){
			Host host = new Host();
			host.setHostName(task.getHost());
			host.setComponents(task.getComponents());
			if(deploymentMap.containsKey(task.getDeploymentId())){
				deploymentMap.get(task.getDeploymentId()).getHosts().add(host);
			}
			else{
				Deployment deployment = new Deployment();
				Pod pod = new Pod();
				pod.setPod(task.getPodName());
				deployment.setPod(pod);
				deployment.setDeploymentStatus(task.getDeploymentStatus());
				List<Host> hostList = new ArrayList<Host>();
				hostList.add(host);
				deployment.setHosts(hostList);
				deployment.setLastDeploymentTime(task.getLastDeploymentTime());
				deploymentMap.put(task.getDeploymentId(), deployment);
			}
		}
		return deploymentMap;
	}
	public List<DeploymentMap> getDeploymentMap(List<DeploymentTask> taskList){
		Map<String,Deployment> deploymentMap = createDeploymentMap(taskList);
		List<DeploymentMap> depMapList = new ArrayList<DeploymentMap>();
		for(String key : deploymentMap.keySet()){
			//LOGGER.info("key:"+key);
			DeploymentMap map = new DeploymentMap();
			map.setDeploymentId(key);
			map.setDeployment(deploymentMap.get(key));
			depMapList.add(map);
		}
		return depMapList;
	}
}