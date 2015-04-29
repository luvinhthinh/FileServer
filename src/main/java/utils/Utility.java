package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class Utility {
	private static String defautMessageBundle = "messageBundle.properties";
	private static String defautAppConfig = "app.properties";
	private static String defautContentTypeMapping = "contentType.properties";
	private static Properties messageBundle, appConfig, contenTypeMapping;
	
	private static Properties load(String fileName){
		try{
			
			System.out.println("Loading... " + fileName);
			Properties props = new Properties();
			props.load(Utility.class.getClassLoader().getResourceAsStream(fileName));
			return props;
		}catch(FileNotFoundException fnfE){
			System.err.println("File not found : " + fileName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static Properties getAppConfig(){
		if(appConfig == null){
			appConfig = load(defautAppConfig);
		}	
		return appConfig;
	}
	
	private static Properties getMessageBundle(){
		if(messageBundle == null){
			messageBundle = load(defautMessageBundle);
		}	
		return messageBundle;
	}
	
	private static Properties getContentTypeMapping(){
		if(contenTypeMapping == null){
			contenTypeMapping = load(defautContentTypeMapping);
		}	
		return contenTypeMapping;
	}
	
	private static String getProperty(String key){
		return messageBundle.getProperty(key);
	}
	
	public static String getMessage(String msg, Object[] params){
		return getMessageBundle().getProperty("error."+msg) == null ? null : new MessageFormat(getProperty("error."+msg)).format(params);
	}
	
	public static String getLabel(String msg){
		return getMessageBundle().getProperty("label."+msg);
	}
	
	public static String getConfig(String name){
		return getAppConfig().getProperty(name);
	}
	
	public static String getContentType(String fileType){
		String contentType = getContentTypeMapping().getProperty(fileType);
		return contentType == null ? Constants.DEFAULT_CONTENT_TYPE : contentType;
	}
	
	public static String readFileToString(String file){
		BufferedReader br = null;
		FileReader fr = null;
		String str = "";
		try{
			br = new BufferedReader(new FileReader(file));
			str = br.readLine();
		}catch(FileNotFoundException e){
			System.err.println("Cannot find file at : " + file);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(fr != null) fr.close();
				if(br != null) br.close();
				return str;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return str;
	}
}
