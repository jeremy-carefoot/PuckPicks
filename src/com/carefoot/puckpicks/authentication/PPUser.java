package com.carefoot.puckpicks.authentication;

/**
 * Class used to store basic user information
 * @author jeremycarefoot
 */
public class PPUser {
	
	private String id;
	private String email;
	
	public PPUser(String id, String email)  {
		this.id = id;
		this.email = email;
	}
	
	public String getUniqueID() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String toString() {
		return "(id: " + id + ", email: " + email + ")";
	}

}
