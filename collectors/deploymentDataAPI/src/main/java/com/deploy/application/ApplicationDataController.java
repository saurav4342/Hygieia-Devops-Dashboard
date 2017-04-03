package com.deploy.application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
//import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deploy.application.model.BladeApplication;
import com.deploy.application.model.DeploymentTransaction;
import com.deploy.application.repository.DeploymentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
@RestController
public class ApplicationDataController {
	@Autowired
	private DeploymentRepository dataRepo;
@RequestMapping(path="/get/applications/{}")
public List<BladeApplication> getApplications()
{
	List<BladeApplication> app = new ArrayList<>();
	List<DeploymentTransaction> transList = dataRepo.find()
}
}
