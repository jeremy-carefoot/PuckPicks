package com.carefoot.puckpicks.main;

/**
 * Enum responsible for PuckPicks logs
 * @author jeremycarefoot
 *
 */
public enum Log {
	
	INFO, WARNING, ERROR;
	
	/**
	 * Log a message to the appropriate stream
	 * @param message Message to log
	 * @param logType Severity/type of log
	 */
	public static void log(String message, Log logType) {
		String output = "[PuckPicks] - " + logType.name() + " - " + message;
		/* Print to error stream if error log */
		if (logType == ERROR)
			System.err.println(output);
		else
			System.out.println(output);
	}

}
