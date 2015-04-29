package handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import utils.Constants;
import utils.Utility;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileHandler implements HttpHandler {
	private String location;
	
	public FileHandler(String location){
		this.location = location;
	}
	
	private void handleFile(HttpExchange x, String contentType, String filename) throws IOException{
		Headers h = x.getResponseHeaders();
	    h.add("Content-Type", contentType);

	    File file = new File (filename);
	    byte [] byteArray  = new byte [(int)file.length()];
	    int len = byteArray.length;
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	    bis.read(byteArray, 0, len);

	    x.sendResponseHeaders(HttpURLConnection.HTTP_OK, file.length());
	    OutputStream os = x.getResponseBody();      
	    os.write(byteArray, 0, len);
	      
	    bis.close();
	    os.close();
	}
	
	@Override
	public void handle(HttpExchange x) throws IOException {
		int dotIndex = this.location.lastIndexOf('.');
		if(dotIndex < 0){
			handleFile(x, Constants.DEFAULT_CONTENT_TYPE, this.location);
		}else{
			String extension = this.location.substring(dotIndex+1);
			String contentType = Utility.getContentType(extension);
			handleFile(x, contentType, this.location);
		}
	}

}
