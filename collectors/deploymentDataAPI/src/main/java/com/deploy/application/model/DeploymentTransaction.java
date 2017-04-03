package com.deploy.application.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
public class DeploymentTransaction {
@Id
public String id;
public String pod;
public String startTime;
public String startDate;
public String status;
public String endTime;
public String version;
public String application;
public String environment;
public DeploymentTransaction()
{}
public String getId()
{
return id;	
}
public String getPod()
{
	return pod;
	}
public String getStatus()
{
	return status;
	}
public String getStartTime()
{
	return startTime;
	}
public String getStartDate()
{
	return startDate;
	}
public String endTime()
{
	return endTime;
	}
public String getVersion()
{
	return version;
	}
public void setStartTime(String startTime)
{
	this.startTime=startTime;
	}
public void setPod(String pod)
{
	this.pod=pod;
	}
public void setEndTime(String endTime)
{
	this.endTime=endTime;
	}
public void setStatus(String status)
{
	this.status=status;
	}
public void setVersion(String version)
{
	this.version=version;
	}
public void setStartDate(String startDate)
{
	this.startDate=startDate;
}
public void setApplication(String application)
{
	this.application=application;
}
public void setEnvironment(String environment)
{
	this.environment=environment;
}
}
