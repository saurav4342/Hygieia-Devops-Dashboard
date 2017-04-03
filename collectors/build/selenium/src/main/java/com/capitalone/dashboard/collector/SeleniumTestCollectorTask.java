package com.capitalone.dashboard.collector;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import com.capitalone.dashboard.datafactory.SeleniumTestDataFactory;
import com.capitalone.dashboard.model.SeleniumTestCollector;
import com.capitalone.dashboard.model.TestResult;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.SeleniumTestCollectorRepository;
import com.capitalone.dashboard.repository.TestResultRepository;


public class SeleniumTestCollectorTask extends CollectorTask<SeleniumTestCollector> {
	  private final SeleniumTestCollectorRepository seleniumTestCollectorRepository;
	    private final TestResultRepository testResultRepository; 
private final SeleniumTestSettings seleniumTestSettings;
	    @Autowired
	    public SeleniumTestCollectorTask(
	            TaskScheduler taskScheduler,
	            SeleniumTestCollectorRepository seleniumTestCollectorRepository,
	            TestResultRepository testResultRepository,        
	            SeleniumTestSettings seleniumTestSettings
	            ) {
	        super(taskScheduler, "SeleniumTest");
	        this.seleniumTestCollectorRepository = seleniumTestCollectorRepository;
	        this.testResultRepository = testResultRepository;
	        this.seleniumTestSettings = seleniumTestSettings;
	        
	    }

	    @Override
	    public SeleniumTestCollector getCollector() {
	        return SeleniumTestCollector
	                .prototype();
	    }

	    @Override
	    public BaseCollectorRepository<SeleniumTestCollector> getCollectorRepository() {
	        return seleniumTestCollectorRepository;
	    }

	    @Override
	    public String getCron() {
	        return seleniumTestSettings.getCron();
	    }

	    @Override
	    public void collect(SeleniumTestCollector collector) {

	        long start = System.currentTimeMillis();
              
	        SeleniumTestDataFactory dataFactory = new SeleniumTestDataFactory();
	        
	        try {
				TestResult result = dataFactory.getTestResult();
				testResultRepository.save(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log(e.getMessage());
			}

	        
	            log("Finished", start);
	        }
	    }

