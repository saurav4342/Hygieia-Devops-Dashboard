package com.capitalone.dashboard.datafactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.capitalone.dashboard.collector.JenkinsSettings;
import com.capitalone.dashboard.model.TestCapability;
import com.capitalone.dashboard.model.TestCase;
import com.capitalone.dashboard.model.TestCaseStatus;
import com.capitalone.dashboard.model.TestCaseStep;
import com.capitalone.dashboard.model.TestResult;
import com.capitalone.dashboard.model.TestSuite;
import com.capitalone.dashboard.model.TestSuiteType;

public class SeleniumTestDataFactory {
	private JenkinsSettings jenkinsSettings;
	@Autowired
	public SeleniumTestDataFactory(JenkinsSettings jenkinsSettings){
		this.jenkinsSettings = jenkinsSettings;
	}
	public Map<String,List<TestSuite>> podMap = new HashMap<String,List<TestSuite>>(); 
	public List<TestResult> getTestResult() throws IOException{
		List<TestResult> results = new ArrayList<TestResult>();
		List<TestCapability> capabilities = getTestCapabilities();
        // Calculate counts based on test suites
        for (TestCapability cap : capabilities) {	
    		TestResult testResult = new TestResult();
            testResult.setDescription(cap.getDescription());
    		testResult.getTestCapabilities().add(cap);  //add all capabilities
            testResult.setTotalCount(cap.getTestSuites().size());
        testResult.setSuccessCount(cap.getSuccessTestSuiteCount());
        testResult.setFailureCount(cap.getFailedTestSuiteCount());
        testResult.setSkippedCount(cap.getSkippedTestSuiteCount());
        testResult.setUnknownStatusCount(cap.getUnknownStatusTestSuiteCount());
        //testResult.setBuildId(testResult.getId());
        testResult.setExecutionId("163");
        testResult.setType(TestSuiteType.Functional);
        //long startTime = 1464572353376l,endTime=1464572355506l;
        testResult.setStartTime(cap.getStartTime());
        testResult.setEndTime(cap.getEndTime());
        testResult.setDuration(cap.getDuration());
        testResult.setTargetEnvName(cap.getDescription());
       results.add(testResult);
	}
        return results; 
	}
	public Map<String,List<TestSuite>> returnMap(){
		return podMap;
	}
	public List<TestCapability> getTestCapabilities() throws IOException{
		List<TestCapability> capabilities = new ArrayList<TestCapability>();
		getTestSuitesFromFile();
		Iterator<Map.Entry<String,List<TestSuite>>> it = podMap.entrySet().iterator();
		String pod=null;
		List<TestSuite> suites = new ArrayList<TestSuite>();
	    while (it.hasNext()) {
	        Map.Entry<String,List<TestSuite>> pair = (Map.Entry<String,List<TestSuite>>)it.next();
	        pod = pair.getKey();
	        suites=pair.getValue();
	        TestCapability testCap = new TestCapability();
	        testCap.setType(TestSuiteType.Functional);
	        testCap.getTestSuites().addAll(suites);
	        long duration = 0l;
	        int testSuiteSkippedCount = 0, testSuiteSuccessCount = 0, testSuiteFailCount = 0, testSuiteUnknownCount = 0;
	        long startTime=Long.MAX_VALUE,endTime=0;
	        for (TestSuite t : suites) {
	        	if(startTime > t.getStartTime()){
	        	startTime=t.getStartTime();
	        	}
	        	if(endTime < t.getEndTime()){
	        		endTime=t.getEndTime();
	        	}
	             duration += t.getDuration();
	             if(t.getStatus()!=null){
	             switch (t.getStatus()) {
	                 case Success:
	                     testSuiteSuccessCount++;
	                     break;
	                 case Failure:
	                     testSuiteFailCount++;
	                     break;
	                 case Skipped:
	                     testSuiteSkippedCount++;
	                     break;
	                 default:
	                     testSuiteUnknownCount++;
	                     break;
	             }
	         }
	             }
	         if (testSuiteFailCount > 0) {
	             testCap.setStatus(TestCaseStatus.Failure);
	         } else if (testSuiteSkippedCount > 0) {
	             testCap.setStatus(TestCaseStatus.Skipped);
	         } else if (testSuiteSuccessCount > 0) {
	             testCap.setStatus(TestCaseStatus.Success);
	         } else {
	             testCap.setStatus(TestCaseStatus.Unknown);
	         }
	         testCap.setFailedTestSuiteCount(testSuiteFailCount);
	         testCap.setSkippedTestSuiteCount(testSuiteSkippedCount);
	         testCap.setSuccessTestSuiteCount(testSuiteSuccessCount);
	         testCap.setUnknownStatusTestSuiteCount(testSuiteUnknownCount);
	         testCap.setTotalTestSuiteCount(suites.size());
	         testCap.setDuration(duration);
	         testCap.setDescription(pod);
	         testCap.setTimestamp(System.currentTimeMillis());
	         testCap.setEndTime(endTime);
	         testCap.setStartTime(startTime);
	         capabilities.add(testCap);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
         return capabilities;
	}
	public List<String> getFileNames() throws IOException{
		List<String> fileNames = new ArrayList<String>();
		URL uri  = new URL(jenkinsSettings.getUrl());
		URLConnection urlc = uri.openConnection();
		InputStream files = urlc.getInputStream();
		String line = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(files));
		 while ((line = reader.readLine()) != null) {
             if(line.contains("BVT") || line.contains("PROD")){
			 continue;
			 }
             else {
             fileNames.add(line);
             }
         }
		 return fileNames;
	}
	public void getTestSuitesFromFile() throws IOException{
		//List<TestSuite> suiteList = new ArrayList<TestSuite>();
		List<String> fileNames = getFileNames();
		for(String name:fileNames){
		String url=jenkinsSettings.getUrl()+name;
		URL uri = new URL(url);
		URLConnection urlc = uri.openConnection();
		long timestamp=urlc.getLastModified();
		InputStream files = urlc.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(files));
		 getTestSuiteWithCases(reader,timestamp);
		}
	}
	
	@SuppressWarnings("unused")
	public void getTestSuiteWithCases(BufferedReader reader,long timestamp) throws IOException{
		String line = null;
        long duration=0l;
        int failedCasesCount=0,passedCasesCount=0,unknownCount=0,skipCount=0;
		List<TestCase> cases = new ArrayList<TestCase>();
		line=reader.readLine();
		boolean flag=true;
		String suiteDescription=null,suiteId=null,podName=null;
		String[] lineArray;
		while((line=reader.readLine())!=null){
			TestCase tCase = new TestCase();
			TestCaseStep step = new TestCaseStep();
			lineArray = line.split(",");
			if(flag){
			suiteDescription=lineArray[2]+"-"+lineArray[1];
			suiteId=lineArray[1];
			podName=lineArray[0].toUpperCase();
			flag=false;
			}
			//tCase.setDescription(lineArray[4]);
			step.setDescription(lineArray[4]);
			for(int i=5;i<lineArray.length;i++){
				if(lineArray[i].equals("Passed")||lineArray[i].equals("Failed")||lineArray[i].equals("Aborted")){
					TestCaseStatus status = getStatus(lineArray[i]);
					tCase.setStatus(status);
					step.setStatus(status);
					switch(status){
					case Success:
					passedCasesCount++;	
					break;
					case Failure:
					failedCasesCount++;
					break;
					case Skipped:
						skipCount++;
						break;
					default:
						unknownCount++;
						break;
					}
				}
				else if(lineArray[i].contains("0:")){
					long caseDuration=getDuration(lineArray[i]);
					tCase.setDuration(caseDuration);
					step.setDuration(caseDuration);
					duration+=caseDuration;
				}
				 
			}
			
			step.setId(lineArray[3]);
			tCase.setId(lineArray[3]);
			tCase.getTestSteps().add(step);
			cases.add(tCase);
				}
		TestSuite suite = new TestSuite();
		suite.getTestCases().addAll(cases);
		suite.setFailedTestCaseCount((failedCasesCount));
		suite.setSuccessTestCaseCount(passedCasesCount);
		suite.setUnknownStatusCount(unknownCount);
		suite.setSkippedTestCaseCount(skipCount);
		suite.setTotalTestCaseCount(failedCasesCount+passedCasesCount+unknownCount+skipCount);
		suite.setDescription(suiteDescription);
		long endDate=timestamp;
		long startDate=timestamp-duration;
		suite.setEndTime(endDate);
		suite.setStartTime(startDate);
		suite.setId(suiteId);
		suite.setDuration(duration);
		if(failedCasesCount>0){
			suite.setStatus(TestCaseStatus.Failure);
		}
		else if(unknownCount>0){
			suite.setStatus(TestCaseStatus.Unknown);
		}
		else if(passedCasesCount>0){
			suite.setStatus(TestCaseStatus.Success);
		}
		
		if(podMap.containsKey(podName)){
			List<TestSuite> suites =podMap.get(podName);
			suites.add(suite);
			podMap.put(podName, suites);
		}
		else{
			List<TestSuite> suites = new ArrayList<TestSuite>();
			suites.add(suite);
			podMap.put(podName, suites);
		}
     	
	
	
	//podMap.put(podName, suites);
	}
	public Long getDuration(String duration){
		String[] time = duration.split(":");
		int hours = Integer.parseInt(time[0]);
		int minutes = Integer.parseInt(time[1]);
		int seconds = Integer.parseInt(time[2]);
		long totalTimeInSecs = (hours*3600)+(minutes*60)+seconds;
		return totalTimeInSecs*1000;
	}
	public TestCaseStatus getStatus(String caseStatus){
		switch(caseStatus){
		case "Passed":
			return TestCaseStatus.Success;
		case "Failed":
			return TestCaseStatus.Failure;
		case "Aborted":
			return TestCaseStatus.Skipped;
		default:
		return TestCaseStatus.Unknown;
		
		}
	}
	
}
