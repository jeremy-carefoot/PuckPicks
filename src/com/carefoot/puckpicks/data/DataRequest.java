package com.carefoot.puckpicks.data;
/*
 * Basic interface for different types of data request (NHL API)
 */
public interface DataRequest {
	
	public static final int DEFAULT_LIMIT = 5;
	
	public String requestSubUrl(); // subUrl for data request

}
