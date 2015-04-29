package handlers;

import java.io.File;
import java.io.IOException;

import utils.Constants;
import utils.Utility;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ViewHandler implements HttpHandler {
	private String url, contextPath;
	
	public ViewHandler(String url, String contextPath){
		this.url = url;
		this.contextPath = contextPath;
	}
	
	private boolean isAbsolutePath(String location){
		return location.startsWith("/");
	}
	
	private HttpHandler getHandler(String fileName){
		String followUp = this.url + this.contextPath + "/" + fileName;
		File file = new File(fileName);
		
		return 	isAbsolutePath(fileName) ? new ErrorHandler(Constants.ABSOLUTE_PATH)	 		:
					!file.exists()   	 ? new ErrorHandler(Constants.NONEXIST) 		 		:
					!file.canRead() 	 ? new ErrorHandler(Constants.PERMISSION_DENIED) 		:
					file.isDirectory()   ? new DirectoryHandler(followUp, fileName) 			: 
										   new FileHandler(fileName);
	}
	
	public void handle(HttpExchange x) throws IOException {
    	String requestMethod = x.getRequestMethod();
    	HttpHandler handler = null;
    	
    	if("GET".equalsIgnoreCase(requestMethod)){
    		String context = x.getRequestURI().getPath();
    		if(context.length() == this.contextPath.length()){
    			handler = getHandler(Utility.getConfig(Constants.HOME_DIR));
    		}else{
    			handler = getHandler(context.substring(this.contextPath.length() + 1));
    		}
    	}else{
    		handler = new ErrorHandler(Constants.WRONG_METHOD);
    	}
    	
    	handler.handle(x);
    }
}
