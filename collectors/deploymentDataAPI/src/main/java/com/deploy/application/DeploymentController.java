package com.deploy.application;
//import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
//import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deploy.application.model.DeploymentTransaction;
import com.deploy.application.repository.DeploymentRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;

@RestController
public class DeploymentController {
	@Autowired
	private DeploymentRepository depRepo;
	 @RequestMapping(path="/post/{pod}/{version}",method=RequestMethod.GET)
	 public void postData(@PathVariable String pod,@PathVariable String version){
		 //updateMongoInfo(pod,version,status);
		 LocalDateTime startTime=LocalDateTime.now();
		 LocalDate startDate=startTime.toLocalDate();
		 DeploymentTransaction transaction = new DeploymentTransaction();
		 transaction.setPod(pod);
		 transaction.setStartDate(startDate.toString());
		 transaction.setStartTime(startTime.toString());
		 transaction.setStatus("In-Progress");
		 transaction.setVersion(version);
		 if(pod.contains("DIT"))
		 {
			 transaction.setEnvironment("DIT");
		 }
		 else if(pod.contains("FIT"))
		 {
			 transaction.setEnvironment("FIT");
		 }
		 depRepo.save(transaction);
	 } 
}
