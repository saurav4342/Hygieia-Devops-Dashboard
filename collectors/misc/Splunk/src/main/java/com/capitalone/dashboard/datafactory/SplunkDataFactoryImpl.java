package com.capitalone.dashboard.datafactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.config.SplunkSettings;
import com.capitalone.dashboard.model.SplunkEvent;
import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.ResultsReaderXml;
import com.splunk.SSLSecurityProtocol;
import com.splunk.SavedSearch;
import com.splunk.Service;
import com.splunk.ServiceArgs;
@Component
public class SplunkDataFactoryImpl implements SplunkDataFactory{
	
	private final SplunkSettings splunkSettings;
	
	@Autowired
	public SplunkDataFactoryImpl(SplunkSettings splunkSettings){
		this.splunkSettings = splunkSettings;
	}
	@Override
public ServiceArgs getCredentials(){
	
	 HttpService.setSslSecurityProtocol( SSLSecurityProtocol.TLSv1_2 );
		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername(splunkSettings.getUsername());
		loginArgs.setPassword(splunkSettings.getPassword());
		loginArgs.setHost("splunkcdl.es.ad.adp.com");
		//loginArgs.setPort(8089);
		return loginArgs;
		
}
	@Override
public ServiceArgs getApplication(){
	
	ServiceArgs namespace = new ServiceArgs();
	namespace.setApp("ezlm_main");
	return namespace;
	
}
	@Override
		// Create a Service instance and log in with the argument map
public InputStream runSavedSearch() throws InterruptedException{
	
		Service service = Service.connect(getCredentials());
		SavedSearch savedSearch = service.getSavedSearches(getApplication()).get("Hygieia");
		Job jobSavedSearch = null;
	    jobSavedSearch = savedSearch.dispatch();
		// Wait for the job to finish
		while (!jobSavedSearch.isDone()) {
		        Thread.sleep(500);
		}
		InputStream stream = jobSavedSearch.getResults();
		return stream;
		}
	@Override
public List<SplunkEvent> readJobResultsInXML() throws Exception{
	List<SplunkEvent> splunkEventList = new ArrayList<SplunkEvent>();
		ResultsReaderXml resultsReaderNormalSearch;
		List<String> blankList = new ArrayList<String>();
		    resultsReaderNormalSearch = new ResultsReaderXml(runSavedSearch());
		    HashMap<String, String> event;
		    while ((event = resultsReaderNormalSearch.getNextEvent()) != null) {
	           SplunkEvent splunkEvent = new SplunkEvent();
		       splunkEvent.setPod(event.get("pod"));
		       splunkEvent.setApp(event.get("Version"));
		          splunkEvent.setErrorCount(event.get("Errors")) ; 
		          splunkEvent.setPercentage(event.get("%"));
		          if(event.containsKey("uCIDs")){
		          splunkEvent.setCustIDs(createCustIdList(event.get("uCIDs")));
		          }
		          else{
		        	  splunkEvent.setCustIDs(blankList);
		          }
		          if(event.containsKey("SysCode")){
		          splunkEvent.setSyscodes(createSyscodeList(event.get("SysCode")));
		          }
		          else{
		        	  splunkEvent.setSyscodes(blankList);
		          }
		          if(event.containsKey("ErrorMessage")){
		          splunkEvent.setErrorMessages(createErrorMessageList(event.get("ErrorMessage")));
		          }
		          else{
		        	  splunkEvent.setErrorMessages(blankList);
		          }
		          splunkEventList.add(splunkEvent);
		    }	
		    return splunkEventList;
}
	public List<String> createSyscodeList(String syscodes){
		List<String> syscodeList = new ArrayList<String>(Arrays.asList(syscodes.split(",")));
		return syscodeList;
	}
	public List<String> createCustIdList(String custIds){
		List<String> custIdList = new ArrayList<String>(Arrays.asList(custIds.split(",")));
		return custIdList;
	}
	public List<String> createErrorMessageList(String errorMessages){
		List<String> errorMessageList = new ArrayList<String>(Arrays.asList(errorMessages.split(",")));
		return errorMessageList;
	}
}
