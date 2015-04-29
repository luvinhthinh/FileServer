package handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DirectoryHandler implements HttpHandler{
	
	
	
	public void handle(HttpExchange x) throws IOException{
		String response = "";
		x.sendResponseHeaders(200, response.length());
		OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
}
