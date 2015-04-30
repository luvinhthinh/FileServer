package servlet;

import handlers.ViewHandler;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

import utils.Constants;
import utils.Utility;

import com.sun.net.httpserver.HttpServer;


public class FileServlet{
	private int port;
	private String host = "localhost";
	private String contextPath;
	private HttpServer server;
	
	public FileServlet(int port, String contextPath){
		this.port = port;
		this.contextPath = contextPath;
	}
	
	public void start(){
		try{
			String url = host+":"+this.port;
			server = HttpServer.create(new InetSocketAddress(this.port), 0);
			server.createContext(this.contextPath, new ViewHandler(url, contextPath));
			server.setExecutor(null);
			server.start();
			System.out.println("Server started. Try : localhost:"+this.port+this.contextPath);
		}catch(BindException be){
			System.err.println("Port already in use. Please choose different port");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try{
			this.server.stop(0);
			System.out.println("Server stopped !");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Usage : java -jar <fileServer.jar>  <port>");
		}else{
			try{
				int port = Integer.parseInt(args[0]);
				String contextPath = Utility.getConfig(Constants.CONTEXT_PATH);
				FileServlet fs = new FileServlet(port, contextPath);
				fs.start();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
