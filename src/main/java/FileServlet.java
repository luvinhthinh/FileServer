
import handlers.ViewHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;


public class FileServlet{
	private int port;
	private String contextPath;
	private HttpServer server;
	
	public FileServlet(int port, String contextPath){
		this.port = port;
		this.contextPath = contextPath;
	}
	
	public void start(){
		try{
			server = HttpServer.create(new InetSocketAddress(this.port), 0);
			server.createContext(this.contextPath, new ViewHandler());
			server.setExecutor(null);
			server.start();
			System.out.println("Server started. Try : http://localhost:"+this.port+this.contextPath);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try{
			server.stop(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		int port = 8080;
		String contextPath = "/";
		FileServlet fs = new FileServlet(port, contextPath);
		fs.start();
	}
}
