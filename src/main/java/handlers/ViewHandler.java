package handlers;

import java.io.IOException;

import utils.Constants;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ViewHandler implements HttpHandler {
	
	private boolean isAbsolutePath(String location){
		return location.startsWith("/");
	}
	
	public void handle(HttpExchange x) throws IOException {
    	String requestMethod = x.getRequestMethod();
    	
    	if("GET".equalsIgnoreCase(requestMethod)){
    		String path = x.getRequestURI().getPath();
    		String location = path.substring(1);
    		if(isAbsolutePath(location)){
    			ErrorHandler errorHandler = new ErrorHandler(Constants.ABSOLUTE_PATH);
        		errorHandler.handle(x);
    		}else{
    			FileHandler fileHandler = new FileHandler(location);
        		fileHandler.handle(x);
    		}
    	}else{
    		ErrorHandler errorHandler = new ErrorHandler(Constants.WRONG_METHOD);
    		errorHandler.handle(x);
    	}
    }
}
