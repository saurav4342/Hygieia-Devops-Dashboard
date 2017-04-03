package com.deploy.application;
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
//import java.util.List;
//import java.time.LocalDate;
@RestController
public class UpdateDeploymentController {
@Autowired
private DeploymentRepository depRep;
	@RequestMapping(path="/update/{pod}/{status}",method=RequestMethod.GET)
	public void updateData(@PathVariable String status,@PathVariable String pod){
		LocalDateTime endTime=LocalDateTime.now();
		DeploymentTransaction transaction = depRep.findByPodAndStatus(pod,"In-Progress");	
		transaction.setEndTime(endTime.toString());
		transaction.setStatus(status);
		//ToDo: try catch for Null Pointer Exception
		depRep.save(transaction);
		
	}
}
