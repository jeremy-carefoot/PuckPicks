package com.carefoot.puckpicks.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.carefoot.puckpicks.data.paths.FilePath;
import com.carefoot.puckpicks.main.Log;

/**
 * Abstract class for instatiating different files to store process-independent data in
 * @author jeremycarefoot
 *
 */
public abstract class FileStorage {
	
	private static List<FileStorage> files = new ArrayList<>();
	private static Path dataDir = Paths.get(FilePath.DATA_DIRECTORY.toString()); 		// directory to store all file data
	
	private String fileName;
	
	/**
	 * Default constructor
	 * @param fileName File name/path relative to app data directory
	 */
	public FileStorage(String fileName) {
		this.fileName = fileName;
		files.add(this);
		load(readFile(dataDir.resolve(fileName).toString())); 	// load file data into object on construction
	}
	
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Writes content to file 
	 * @param contents List of String where each string is a new line
	 */
	public void updateFile(List<String> contents) {
		validateDir();
		writeFile(contents, dataDir.resolve(fileName).toString());
	}
	
	/**
	 * Save all instances of FileStorage.
	 * Should be executed on program exit.
	 */
	public static void saveFiles() {
		validateDir();
		/* Save all file data into appropriate files*/
		for (FileStorage file : files) {
			writeFile(file.write(), dataDir.resolve(file.getFileName()).toString());
		}
	}
	
	/**
	 * Writes the content of the string list to a file at the provided path
	 * @param contents List of String where each list entry is a new line
	 * @param path Path of the output file
	 */
	private static void writeFile(List<String> contents, String path) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			for (String line : contents) {
				writer.write(line);
				writer.newLine();
			}

			writer.close();  	// close the BufferedWriter
		} catch (IOException e) {
			Log.log("Could not write to file: " + e.getMessage(), Log.ERROR);
		}
	}
	
	/**
	 * Reads the content of the file at the provided path.
	 * Returns null if the file is not found or IO exception occurs
	 * @param path File path
	 * @return String list where each entry is a new line
	 */
	private static List<String> readFile(String path) {
		List<String> output = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while((line = reader.readLine()) != null) {
				output.add(line);
			}
			
			reader.close(); 	// close the BufferedReader
			return output;
		} catch (IOException e) {
			Log.log("Could not read file: " + e.getMessage(), Log.WARNING);
			return null;
		}
	}
	
	/**
	 * Validate that the app data directory exists; if not, create it 
	 */
	private static void validateDir() {		
		if(!Files.exists(dataDir))
			try {
				Files.createDirectories(dataDir);
			} catch (IOException e) {
				Log.log("Could not create data directory: " + e.getMessage(), Log.ERROR);
			}
	}
	
	/**
	 * Write content of this object into a file that will be automatically stored and loaded on app startup/exit
	 * @return Content to write in a String list form; each list entry is a new line in the file
	 */
	protected abstract List<String> write();	
	
	/**
	 * Loads data from file on app startup.
	 * Contents can be null if the file does not exist yet (first startup)
	 * @param contents Contents of the file in String list format; each list entry represents a new line from file
	 */
	protected abstract void load(List<String> contents); 	

}
