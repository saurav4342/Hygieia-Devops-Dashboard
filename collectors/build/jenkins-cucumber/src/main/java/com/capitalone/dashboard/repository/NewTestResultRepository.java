package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.TestResult;

@Component
public interface NewTestResultRepository extends CrudRepository<TestResult, ObjectId>, QueryDslPredicateExecutor<TestResult>{
	 TestResult findByCollectorItemIdAndExecutionId(ObjectId collectorItemId, String executionId);
	 TestResult findByDescription(String description);
}
