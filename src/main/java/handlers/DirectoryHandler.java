package handlers;

import handlers.helper.TagPrinter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import utils.Constants;
import utils.Utility;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DirectoryHandler implements HttpHandler{
	private String location;
	private String url;
	private TagPrinter tagPrinter;
	
	public DirectoryHandler(String url, String location){
		this.url = url;
		this.location = location;
		this.tagPrinter = new TagPrinter();
	}
	
	public void handle(HttpExchange x) throws IOException{
		StringBuilder content = new StringBuilder();
		String htmlTemplate = Utility.readFileToString(Utility.getConfig(Constants.HTML_TEMPLATE_FILE));
		File[] fileList = new File(location).listFiles();
		for(File file : fileList){
			String filename = file.getName();
			content.append(
					tagPrinter.printTag_row(
							tagPrinter.printTag_link("http://"+this.url+"/"+filename, filename, file.isDirectory() ? Utility.getLabel(Constants.LABEL_DIRECTORY) : "")
						));
		}

		String response = htmlTemplate.replace(Constants.HTML_TEMPLATE_CONTENT, content.toString());
		x.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
		OutputStream os = x.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
}
