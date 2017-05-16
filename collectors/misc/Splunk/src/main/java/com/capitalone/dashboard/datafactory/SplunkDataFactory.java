package com.capitalone.dashboard.datafactory;

import java.io.InputStream;
import java.util.List;

import com.capitalone.dashboard.model.SplunkEvent;
import com.splunk.ServiceArgs;

public interface SplunkDataFactory {
	 ServiceArgs getCredentials();
	 ServiceArgs getApplication();
	 InputStream runSavedSearch() throws InterruptedException;
	 List<SplunkEvent> readJobResultsInXML() throws Exception;
}
