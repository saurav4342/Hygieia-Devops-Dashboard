package com.capitalone.dashboard.model;

import java.util.List;

public class TestCaseWithDuration{
private List<TestCase> tCase;
private long totalDurationOfTestSuite;
private int failedCasesNum;
private int passedCasesNum;
public void setTCase(List<TestCase> tCase){
	this.tCase=tCase;
}
public void setTotalDurationOfTestSuite(long totalDurationOfTestSuite){
	this.totalDurationOfTestSuite=totalDurationOfTestSuite;
}
public void setFailedCasesNum(int failedCasesNum){
	this.failedCasesNum=failedCasesNum;
}
public void setPassedCasesNum(int passedCasesNum){
	this.passedCasesNum=passedCasesNum;
}
public List<TestCase> getTestCase(){
	return tCase;
}
public long getTotalDurationOfTestSuite(){
	return totalDurationOfTestSuite;
}
public int getFailedCasesNum(){
	return failedCasesNum;
}
public int getPassedCasesNum(){
	return passedCasesNum;
}
}
