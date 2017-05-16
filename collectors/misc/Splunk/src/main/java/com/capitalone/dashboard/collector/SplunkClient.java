package com.capitalone.dashboard.collector;

import java.util.List;

import com.capitalone.dashboard.model.SplunkEvent;

public interface SplunkClient {
	 List<SplunkEvent> getSplunkEventList() throws Exception;
}
