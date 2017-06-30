package com.capitalone.dashboard.model.deploy;

import java.util.ArrayList;
import java.util.List;

public class NewEnvironment extends Environment{
	private final List<NewDeployableUnit> newUnits = new ArrayList<>();
	public NewEnvironment(String name, String url) {
		super(name, url);
		
		// TODO Auto-generated constructor stub
	}
	
	 public List<NewDeployableUnit> getNewUnits() {
	        return newUnits;
	    }
}
