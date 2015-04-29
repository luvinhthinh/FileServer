package handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Utility;

public class ErrorHandler implements HttpHandler{
	private String errorMsg;
	
	public ErrorHandler(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	public void handle(HttpExchange x) throws IOException{
		String response = Utility.getMessage(this.errorMsg, null);
		x.sendResponseHeaders(200, response.length());
		OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
}
