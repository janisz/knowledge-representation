package pl.edu.pw.mini.msi.knowledgerepresentation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author maq
 * 
 */
public class FileLoader {
	
	private static final Logger log = LoggerFactory.getLogger(FileLoader.class);
	
	private static List<String> parseFromFile(File file) throws FileNotFoundException, IOException{
		List<String> result = new ArrayList<String>();
		
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line;
//		StringBuffer stringBuffer;
		while((line = bufferedReader.readLine()) != null){
//			stringBuffer = new StringBuffer();
//			stringBuffer.append(line).append("\n");
			result.add(line);
		}
		bufferedReader.close();
		
		return result;
	}
	
	private static List<String> parseFile(String path){
		List<String> result = new ArrayList<String>();
		
		try{
			File file = new File(path);
			if(file.exists()){
				result = parseFromFile(file);
			}else{
				log.info("Specified file (" + path + ") does not exists.");
			}
		}catch(FileNotFoundException fileNotFoundException){
			fileNotFoundException.printStackTrace();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
		
		return result;
	}
	
	public static String parseTestFileAsString(String basePath){
		String result = "";
		
		List<String> lines;
		
		lines = parseFile(basePath);
		
		StringBuffer stringBuffer = new StringBuffer();
		for(String line : lines){
			stringBuffer.append(line).append("\n");
			System.out.println("line: " + line);
		}
		
		result = stringBuffer.toString();
		
		return result;
	}
	
	public static List<String> loadFile(String path){
		List<String> result;
		
		result = parseFile(path);
		
		return result;
	}
	
}
