package com.capitalone.dashboard.collector;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.config.SplunkSettings;
import com.capitalone.dashboard.model.SplunkCollector;
import com.capitalone.dashboard.model.SplunkEvent;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.SplunkCollectorRepository;
import com.capitalone.dashboard.repository.SplunkEventRepository;

@Component
public class SplunkEventCollectorTask extends CollectorTask<SplunkCollector>{
	private final static Logger LOG = LoggerFactory.getLogger(SplunkEventCollectorTask.class);
private final SplunkClient splunkClient;
private final SplunkCollectorRepository splunkRepository;
private final SplunkSettings splunkSettings;
private final SplunkEventRepository eventRepository;

@Autowired
public SplunkEventCollectorTask(TaskScheduler taskScheduler,
		                        SplunkClient splunkClient,
		                        SplunkCollectorRepository splunkRepository,     
                                SplunkSettings splunkSettings,
                                SplunkEventRepository eventRepository) {
    super(taskScheduler, "Splunk");
    this.splunkClient = splunkClient;
    this.splunkRepository = splunkRepository;
    this.splunkSettings = splunkSettings;
    this.eventRepository = eventRepository;
   
}

@Override
public SplunkCollector getCollector() {
    return SplunkCollector.prototype();
}

@Override
public BaseCollectorRepository<SplunkCollector> getCollectorRepository() {
    return splunkRepository;
}

@Override
public String getCron() {
    return splunkSettings.getCron();
}

@Override
public void collect(SplunkCollector collector) {
 

        long start = System.currentTimeMillis();
        log(splunkSettings.getUrl());
        try {
			List<SplunkEvent> eventList = splunkClient.getSplunkEventList();
			cleanEvents();
			eventRepository.save(eventList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.info(e.getMessage());
			LOG.info("Error");
		}
        log("Finished", start);
        
    }

public void cleanEvents(){
	
eventRepository.deleteAll();

}

}


