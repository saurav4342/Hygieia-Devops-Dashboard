package com.capitalone.dashboard.datafactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.capitalone.dashboard.model.Environment;
import com.capitalone.dashboard.model.EnvironmentComponent;
import com.capitalone.dashboard.model.SplunkEvent;
import com.capitalone.dashboard.model.UDeployApplication;
import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.ResultsReaderXml;
import com.splunk.SSLSecurityProtocol;
import com.splunk.SavedSearch;
import com.splunk.Service;
import com.splunk.ServiceArgs;

public class DeploymentDataFactory {
	
	private List<Environment> envList = new ArrayList<Environment>();
	private List<UDeployApplication> appList = new ArrayList<UDeployApplication>();
	private List<EnvironmentComponent> envComponent = new ArrayList<EnvironmentComponent>();
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
	
		SavedSearch savedSearch = service.getSavedSearches(namespace).get("TLM CDL daily deployment status");
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
		    	splunkEvent.setHostCount(event.get("HostCount"));
		    	splunkEvent.setPod(event.get("pod"));
		        splunkEvent.setwarncount(event.get("WarnCount"));
		        splunkEvent.setLastDeploymentTime(event.get("LastDeploymentTime"));
		        splunkEvent.setErrorCount(event.get("ErrorCount"));
		        splunkEvent.setdeploymentStatus(event.get("DeploymentStatus"));
		        splunkEvent.setLatestStage(event.get("LatestStage"));
		        splunkEventList.add(splunkEvent);
		    }
		    return splunkEventList;
		}
	public void createApplications(List<SplunkEvent> splunkEventList){
		String pod="",app="";
		
		for(SplunkEvent event:splunkEventList){
			pod = event.getPod();
			
			if(pod.contains("TLM")){
				app="TLM";
			}
			else if(pod.contains("WFN")){
				app="WFN";
			}
			else if(pod.contains("AP")){
				app="AP";
			}
			else if(pod.contains("HDC")){
				app="HDC";
			}
			else if(pod.contains("ETC")){
				app="ETC";
			}
			Environment environment = new Environment(pod,pod);
			UDeployApplication application = new UDeployApplication();
			EnvironmentComponent component = new EnvironmentComponent();
			application.setApplicationId(app);
			application.setApplicationName(app);
			application.setDescription(app);
			envList.add(environment);
			appList.add(application);
		}
		
	}
	public List<UDeployApplication> returnApp(){
		return appList;
	}
	public List<Environment> returnEnv(){
		return envList;
	}
	
}