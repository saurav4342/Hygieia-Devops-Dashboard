package com.capitalone.dashboard.datafactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.capitalone.dashboard.model.TestCapability;
import com.capitalone.dashboard.model.TestCase;
import com.capitalone.dashboard.model.TestCaseStatus;
import com.capitalone.dashboard.model.TestCaseWithDuration;
import com.capitalone.dashboard.model.TestResult;
import com.capitalone.dashboard.model.TestSuite;
import com.capitalone.dashboard.model.TestSuiteType;

public class SeleniumTestDataFactory {
	public TestResult getTestResult() throws IOException{
		List<TestCapability> capabilities = getTestCapabilities();
		TestResult testResult = new TestResult();
        testResult.setDescription("Test Result desciption");
		testResult.getTestCapabilities().addAll(capabilities);  //add all capabilities
        testResult.setTotalCount(capabilities.size());
        int testCapabilitySkippedCount = 0, testCapabilitySuccessCount = 0, testCapabilityFailCount = 0;
        int testCapabilityUnknownCount = 0;
        // Calculate counts based on test suites
        for (TestCapability cap : capabilities) {
            switch (cap.getStatus()) {
                case Success:
                    testCapabilitySuccessCount++;
                    break;
                case Failure:
                    testCapabilityFailCount++;
                    break;
                case Skipped:
                    testCapabilitySkippedCount++;
                    break;
                default:
                    testCapabilityUnknownCount++;
                    break;
            }
        }
        testResult.setSuccessCount(testCapabilitySuccessCount);
        testResult.setFailureCount(testCapabilityFailCount);
        testResult.setSkippedCount(testCapabilitySkippedCount);
        testResult.setUnknownStatusCount(testCapabilityUnknownCount);
        return testResult;
	}
	public List<TestCapability> getTestCapabilities() throws IOException{
		List<TestCapability> capabilities = new ArrayList<TestCapability>();
		TestCapability testCap = new TestCapability();
		testCap.setType(TestSuiteType.Functional);
		List<TestSuite> suiteList = getTestSuitesFromFile();
		testCap.getTestSuites().addAll(suiteList);
	     long duration = 0;
         int testSuiteSkippedCount = 0, testSuiteSuccessCount = 0, testSuiteFailCount = 0, testSuiteUnknownCount = 0;
         for (TestSuite t : suiteList) {
             duration += t.getDuration();
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
         testCap.setTotalTestSuiteCount(suiteList.size());
         testCap.setDuration(duration);
         capabilities.add(testCap);
         return capabilities;
	}
	public List<String> getFileNames() throws IOException{
		List<String> fileNames = new ArrayList<String>();
		URL uri  = new URL("file://cdltlmdash2/Regression/");
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
	public List<TestSuite> getTestSuitesFromFile() throws IOException{
		List<TestSuite> suiteList = new ArrayList<TestSuite>();
		List<String> fileNames = getFileNames();
		for(String name:fileNames){
		String url="file://cdltlmdash2/Regression/"+name;
		URL uri = new URL(url);
		URLConnection urlc = uri.openConnection();
		InputStream files = urlc.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(files));
		TestSuite suite = getTestSuiteWithCases(reader);
	    suiteList.add(suite);
		}
		return suiteList;
	}
	
	@SuppressWarnings("unused")
	public TestSuite getTestSuiteWithCases(BufferedReader reader) throws IOException{
		TestCaseWithDuration casesWithDuration;
		String line = null;
long duration=0;
int failedCasesCount=0,passedCasesCount=0,unknownCount=0;
		List<TestCase> cases = new ArrayList<TestCase>();
		line=reader.readLine();
		boolean flag=true;
		String suiteDescription,suiteId;
		String[] lineArray;
		while((line=reader.readLine())!=null){
			TestCase tCase = new TestCase();
			lineArray = line.split(",");
			if(flag){
			suiteDescription=lineArray[2]+"-"+lineArray[0];
			suiteId=lineArray[1];
			flag=false;
			}
			tCase.setDescription(lineArray[4]);
			tCase.setDuration(getDuration(lineArray[6]));
			duration+=getDuration(lineArray[6]);
			tCase.setId(lineArray[3]);
			tCase.setStatus(getStatus(lineArray[5]));
			switch(getStatus(lineArray[5])){
			case Success:
			passedCasesCount++;	
			break;
			case Failure:
			failedCasesCount++;
			break;
			default:
				unknownCount++;
				break;
			}
			cases.add(tCase);
				}
		TestSuite suite = new TestSuite();
		suite.getTestCases().addAll(cases);
		suite.setFailedTestCaseCount((failedCasesCount));
		suite.setSuccessTestCaseCount(passedCasesCount);
		suite.setUnknownStatusCount(unknownCount);
		if(failedCasesCount>0){
			suite.setStatus(TestCaseStatus.Success);
		}
		else if(unknownCount>0){
			suite.setStatus(TestCaseStatus.Unknown);
		}
		else if(passedCasesCount>0){
			suite.setStatus(TestCaseStatus.Success);
		}
	suite.setDuration(duration);
		return suite;
	}
	public Long getDuration(String duration){
		String[] time = duration.split(",");
		int hours = Integer.parseInt(time[0]);
		int minutes = Integer.parseInt(time[1]);
		int seconds = Integer.parseInt(time[2]);
		long totalTimeInSecs = (hours*3600)+(minutes*60)+seconds;
		return totalTimeInSecs;
	}
	public TestCaseStatus getStatus(String caseStatus){
		switch(caseStatus){
		case "Passed":
			return TestCaseStatus.Success;
		case "Failed":
			return TestCaseStatus.Failure;
		default:
		return TestCaseStatus.Unknown;
		
		}
	}
	
}
