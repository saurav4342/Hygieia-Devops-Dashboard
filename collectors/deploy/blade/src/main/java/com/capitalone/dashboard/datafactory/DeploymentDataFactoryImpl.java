package com.capitalone.dashboard.datafactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.collector.BladeSettings;
import com.capitalone.dashboard.model.Deployment;
import com.capitalone.dashboard.model.DeploymentMap;
import com.capitalone.dashboard.model.DeploymentTask;
import com.capitalone.dashboard.model.Host;
import com.capitalone.dashboard.model.Pod;
import com.capitalone.dashboard.model.PodVersionMap;
import com.capitalone.dashboard.model.UDeployApplication;
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
	private final BladeSettings bladeSettings;
	@Autowired
	public DeploymentDataFactoryImpl(BladeSettings bladeSettings ){
		this.bladeSettings = bladeSettings;
	}
    
	public List<DeploymentTask> connectToSplunk() throws InterruptedException, IOException{
		List<DeploymentTask> taskList = new ArrayList<DeploymentTask>();
	    HttpService.setSslSecurityProtocol( SSLSecurityProtocol.TLSv1_2 );
		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername(bladeSettings.getUsername());
		loginArgs.setPassword(bladeSettings.getPassword());
		loginArgs.setHost(bladeSettings.getUrl());
		//loginArgs.setPort(8089);

		// Create a Service instance and log in with the argument map
		Service service = Service.connect(loginArgs);
		ServiceArgs namespace = new ServiceArgs();
		namespace.setApp(bladeSettings.getSplunkApp());
	
		SavedSearch savedSearch = service.getSavedSearches(namespace).get(bladeSettings.getSavedSearch());
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
	@Override
	public List<UDeployApplication> createPods(List<DeploymentMap> mapList){
		List<UDeployApplication> applications = new ArrayList<>();
		List<String> release = new ArrayList<>();
		for(DeploymentMap task:mapList){
			if(!release.contains(task.getApplication())){
			release.add(task.getApplication());
			UDeployApplication application = new UDeployApplication();
			application.setApplicationId(task.getApplication());
			application.setApplicationName(task.getApplication());
			application.setDescription(task.getApplication());
		    applications.add(application);
			}
		}
		return applications;
	}
	public Map<String,Deployment> createDeploymentMap(List<DeploymentTask> taskList){
		Map<String,Deployment> deploymentMap = new HashMap<String,Deployment>();
		for(DeploymentTask task : taskList){
			Host host = new Host();
			host.setHostName(task.getHost());
			host.setComponents(task.getComponents());
			host.setDeploymentStatus(task.getDeploymentStatus());
			if(host.getDeploymentStatus().equals("Good")){
				host.setOnline(true);
			}
			else{
				host.setOnline(false);
			}
			if(deploymentMap.containsKey(task.getDeploymentId())){
				deploymentMap.get(task.getDeploymentId()).getHosts().add(host);
			}
			else{
				Deployment deployment = new Deployment();
				Pod pod = new Pod();
				pod.setPod(task.getPodName());
				deployment.setPod(pod);
				//deployment.setDeploymentStatus(task.getDeploymentStatus());
				List<Host> hostList = new ArrayList<Host>();
				hostList.add(host);
				deployment.setHosts(hostList);
				deployment.setLastDeploymentTime(task.getLastDeploymentTime());
				deploymentMap.put(task.getDeploymentId(), deployment);
			}
		}
		return deploymentMap;
	}
	@Override
	public List<DeploymentMap> getDeploymentMap(List<DeploymentTask> taskList, List<PodVersionMap> podVersionMap){
		Map<String,Deployment> deploymentMap = createDeploymentMap(taskList);
		List<DeploymentMap> depMapList = new ArrayList<DeploymentMap>();
		for(String key : deploymentMap.keySet()){
			//LOGGER.info("key:"+key);
			DeploymentMap map = new DeploymentMap();
			map.setDeploymentId(key);
			map.setDeployment(deploymentMap.get(key));
			for(PodVersionMap podVersion : podVersionMap){
				if(podVersion.getPod().trim().equals(map.getDeployment().getPod().getPod())){
					map.setVersion(podVersion.getVersion().trim());
					map.setApplication(createApplication(podVersion.getVersion()));
				}
			}
			int i=0;
			for(Host host : map.getDeployment().getHosts()){
			    	
				if(host.getDeploymentStatus().equals("Good"))
				{
					i++;
				}
			}
			if(i==map.getDeployment().getHosts().size()){
				map.getDeployment().setDeploymentStatus("Good");
				map.setDeployed(true);
			}
			
			else{
				map.setDeployed(false);
			map.getDeployment().setDeploymentStatus("Error");
			}
		depMapList.add(map);
		}
		return depMapList;
	}
	
	public String createApplication(String version){
		String [] arr = version.split("\\.");
		String application = "v"+arr[0]+"."+arr[1]+"."+arr[2];
		return application;
	}
	
	public List<PodVersionMap> getVersionData() throws IOException{
		List<PodVersionMap> podMapList = new ArrayList<PodVersionMap>();
		String command = bladeSettings.getCommand();
		  // Executing the command
		  Process powerShellProcess = Runtime.getRuntime().exec(command);
		  // Getting the results
		  powerShellProcess.getOutputStream().close();
		  String line;
		  BufferedReader stdout = new BufferedReader(new InputStreamReader(
		    powerShellProcess.getInputStream()));
		  while ((line = stdout.readLine()) != null) {
		   PodVersionMap podMap = createPodMap(line);
		   podMapList.add(podMap);
		  }
		  stdout.close();
		  //System.out.println("Standard Error:");
//		  BufferedReader stderr = new BufferedReader(new InputStreamReader(
//		    powerShellProcess.getErrorStream()));
//		  while ((line = stderr.readLine()) != null) {
//		   System.out.println(line);
//		  }
//		  stderr.close();
		return podMapList;
		  
	}

	public PodVersionMap createPodMap(String line){
		PodVersionMap podMap = new PodVersionMap();
		String arr[] = line.split("---");
		podMap.setPod(arr[0]);
		podMap.setVersion(arr[1]);
		podMap.setDate(arr[2]);
		if(arr[0].contains("TLM")){
			podMap.setApp("TLM");
		}
		else if(arr[0].contains("WFN")){
			podMap.setApp("WFN");
		}
		else if(arr[0].contains("ETC")){
			podMap.setApp("ETC");
		}
		else if(arr[0].contains("AP")){
			podMap.setApp("AP");
		}
		else if(arr[0].contains("DB")){
			podMap.setApp("DB");
		}
		return podMap;
	}
}