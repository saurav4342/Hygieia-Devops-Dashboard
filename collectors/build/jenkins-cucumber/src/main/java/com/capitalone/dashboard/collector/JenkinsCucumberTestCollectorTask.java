package com.capitalone.dashboard.collector;


import com.capitalone.dashboard.datafactory.SeleniumTestDataFactory;
import com.capitalone.dashboard.model.JenkinsCucumberTestCollector;
import com.capitalone.dashboard.model.JenkinsJob;
import com.capitalone.dashboard.model.TestResult;

import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.JenkinsCucumberTestCollectorRepository;
import com.capitalone.dashboard.repository.JenkinsCucumberTestJobRepository;
import com.capitalone.dashboard.repository.NewTestResultRepository;

import java.util.List;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;



@SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
// Will need to rename com.capitalone.dashboard.Component as it conflicts with Spring.
@Component
public class JenkinsCucumberTestCollectorTask extends
        CollectorTask<JenkinsCucumberTestCollector> {

    private final JenkinsCucumberTestCollectorRepository jenkinsCucumberTestCollectorRepository;
    private final JenkinsSettings jenkinsCucumberTestSettings;
   private final NewTestResultRepository testResultRepository;
private final JenkinsCucumberTestJobRepository jenkinsCucumberTestJobRepository;
    @Autowired
    public JenkinsCucumberTestCollectorTask(
            TaskScheduler taskScheduler,
            JenkinsCucumberTestCollectorRepository jenkinsCucumberTestCollectorRepository,
            NewTestResultRepository testResultRepository,
            JenkinsCucumberTestJobRepository jenkinsCucumberTestJobRepository,
            JenkinsSettings jenkinsCucumberTestSettings
            ) {
        super(taskScheduler, "JenkinsCucumberTest");
        this.jenkinsCucumberTestCollectorRepository = jenkinsCucumberTestCollectorRepository;
        this.jenkinsCucumberTestSettings = jenkinsCucumberTestSettings;
       this.testResultRepository = testResultRepository;
       this.jenkinsCucumberTestJobRepository = jenkinsCucumberTestJobRepository;
    }

    @Override
    public JenkinsCucumberTestCollector getCollector() {
        return JenkinsCucumberTestCollector
                .prototype(jenkinsCucumberTestSettings.getServers());
    }

    @Override
    public BaseCollectorRepository<JenkinsCucumberTestCollector> getCollectorRepository() {
        return jenkinsCucumberTestCollectorRepository;
    }

    @Override
    public String getCron() {
        return jenkinsCucumberTestSettings.getCron();
    }

    @Override
    public void collect(JenkinsCucumberTestCollector collector) {
    	long start = System.currentTimeMillis();
    	 
    	SeleniumTestDataFactory seleniumTestDataFactory = new SeleniumTestDataFactory(jenkinsCucumberTestSettings);
        log("fetching test results",start);
        try {
        
			List<TestResult> results = seleniumTestDataFactory.getTestResult();
			//log("fetching jobs");
			for(TestResult result:results){
				JenkinsJob jenkinsJob = new JenkinsJob();
		        jenkinsJob.setCollectorId(collector.getId());
		        jenkinsJob.setEnabled(false);
		        jenkinsJob.setJobUrl(result.getTargetEnvName());
		        jenkinsJob.setJobName(result.getTargetEnvName());
		        jenkinsJob.setDescription(result.getTargetEnvName());
		        if(jenkinsCucumberTestJobRepository.findJenkinsJobByDescription(jenkinsJob.getDescription())==null){ 	
		        	jenkinsCucumberTestJobRepository.save(jenkinsJob);  
		        }
			result.setCollectorItemId(jenkinsJob.getId());
            result.setTimestamp(System.currentTimeMillis());
			result.setUrl("http");
			TestResult checkResult = testResultRepository.findByDescription(result.getDescription());
			if(checkResult!=null)
				{
				if(checkResult.getEndTime()<result.getEndTime()){
					testResultRepository.delete(testResultRepository.findByDescription(checkResult.getDescription()).getId());
				testResultRepository.save(result);
				}
			}
			else{
			testResultRepository.save(result);
			}
			}
			
        	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log(e.getMessage());
		}
        
        log("Finished", start);
    }

    /**
     * Clean up unused hudson/jenkins collector items
     *
     * @param collector the collector
     */



   
}
