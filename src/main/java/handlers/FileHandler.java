package handlers;

import handlers.helper.TagPrinter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import utils.Constants;
import utils.Utility;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileHandler implements HttpHandler {
	private String location;
	private TagPrinter tagPrinter;
	
	public FileHandler(String location){
		this.location = location;
		this.tagPrinter = new TagPrinter();
	}

	
	private void handleFile(HttpExchange x, String contentType, String filename) throws IOException{
		Headers h = x.getResponseHeaders();
	    h.add("Content-Type", contentType);

	    File file = new File (filename);
	    byte [] bytearray  = new byte [(int)file.length()];
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	    bis.read(bytearray, 0, bytearray.length);

	    x.sendResponseHeaders(200, file.length());
	    OutputStream os = x.getResponseBody();      
	    os.write(bytearray,0,bytearray.length);
	      
	    bis.close();
	    os.close();
	}
	
	public void readFile(HttpExchange x) throws IOException{
//	    handleFile(x, "application/pdf", "bin/temp.pdf");
		String htmlTemplate = Utility.readFileToString("bin/template.html");
		String content = tagPrinter.printTag_link("/abc", "abc");
		String response = htmlTemplate.replace(Constants.HTML_TEMPLATE_CONTENT, content.toString());
		x.sendResponseHeaders(200, response.length());
		OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
	
	public void readDirectory(HttpExchange x, String location) throws IOException{
		StringBuilder content = new StringBuilder();
		String htmlTemplate = Utility.readFileToString(Utility.getConfig(Constants.HTML_TEMPLATE_FILE));
		File[] fileList = new File(location).listFiles();
		for(File file : fileList){
			String filename = file.getName();
			if(file.isDirectory()){
				content.append(
						tagPrinter.printTag_row(
								tagPrinter.printTag_link("http://localhost:8080/"+location+"/"+filename, filename)));
			}else{
				content.append(
						tagPrinter.printTag_row(
								filename));
			}
		}

		String response = htmlTemplate.replace(Constants.HTML_TEMPLATE_CONTENT, content.toString());
		x.sendResponseHeaders(200, response.length());
		OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
	
	@Override
	public void handle(HttpExchange x) throws IOException {
		try{
			if("".equals(this.location)){
				readDirectory(x, Utility.getConfig(Constants.HOME_DIR));
			}else{
				File file = new File(location);
				if(file.exists()){
					boolean isDir = file.isDirectory();
					if(isDir){
						readDirectory(x, this.location);
					}else{
						readFile(x);
					}
				}else{
					ErrorHandler errorHandler = new ErrorHandler(Constants.NONEXIST);
					errorHandler.handle(x);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
