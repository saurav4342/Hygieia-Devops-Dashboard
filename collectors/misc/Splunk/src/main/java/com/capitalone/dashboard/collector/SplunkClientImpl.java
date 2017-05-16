package com.capitalone.dashboard.collector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.datafactory.SplunkDataFactory;
import com.capitalone.dashboard.model.SplunkEvent;
@Component
public class SplunkClientImpl implements SplunkClient {

	private final SplunkDataFactory splunkDataFactory;
	@Autowired
    public SplunkClientImpl(SplunkDataFactory splunkDataFactory){
	this.splunkDataFactory=splunkDataFactory;
}
    @Override
    public List<SplunkEvent> getSplunkEventList() throws Exception {
    	return splunkDataFactory.readJobResultsInXML();
    }

}
