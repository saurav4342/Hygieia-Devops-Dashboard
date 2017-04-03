package com.deploy.application.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.deploy.application.model.DeploymentTransaction;

import java.util.List;
import java.time.LocalDate;
public interface DeploymentRepository extends MongoRepository<DeploymentTransaction,String>{
public List<DeploymentTransaction> findByStartDate(String StartDate);
public DeploymentTransaction findByPodAndStatus(String pod,String status);
}
