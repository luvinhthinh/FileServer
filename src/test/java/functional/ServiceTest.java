package functional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import servlet.FileServlet;
import junit.framework.Assert;

public class ServiceTest extends junit.framework.TestCase{
	private FileServlet fs;
	private static int port = 8080;
	private static String localhost = "http://localhost:"+port;
	private static String contextPath = "/view";
	private final String USER_AGENT = "Mozilla/5.0";
	
	private HttpURLConnection doGET(String url) throws Exception{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		return con;
	}
	
	private String getResponseContent(HttpURLConnection connection){
		StringBuffer response = new StringBuffer();
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return response.toString();
	}
	
	protected void setUp() {
		FileServlet fs = new FileServlet(port, contextPath);
		fs.start();
    }
	
	protected void tearDown(){
		fs.stop();
	}
	
	public void test_wrongContextPath() throws Exception{
		HttpURLConnection connection = doGET(localhost + "/test");
		Assert.assertEquals(404, connection.getResponseCode());
	}
	

	public void test_sendPOST() throws Exception{
		String url = localhost + contextPath;
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setDoOutput(true);
		
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals("Sorry guys ! Only support GET at the moment", getResponseContent(connection));
	}
	

	public void test_NonExistFile() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath + "/test");
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals("No such file or directory.", getResponseContent(connection));
	}
	

	public void test_homeDirectory_default() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath);
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertTrue(response.contains("htmlFolder"));
		Assert.assertTrue(response.contains("imageFolder"));
		Assert.assertTrue(response.contains("pdfFolder"));
		Assert.assertTrue(response.contains("textFolder"));
		Assert.assertTrue(response.contains("file1"));
		Assert.assertTrue(response.contains("file2"));
		
		Assert.assertTrue(response.contains("<td><a href=\""+localhost+contextPath+"/content/htmlFolder\">htmlFolder</a></td>"));
	}
	

	public void test_homeDirectory() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath+ "/content");
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertTrue(response.contains("htmlFolder"));
		Assert.assertTrue(response.contains("imageFolder"));
		Assert.assertTrue(response.contains("pdfFolder"));
		Assert.assertTrue(response.contains("textFolder"));
		Assert.assertTrue(response.contains("file1"));
		Assert.assertTrue(response.contains("file2"));
		
		Assert.assertTrue(response.contains("<td><a href=\""+localhost+contextPath+"/content/htmlFolder\">htmlFolder</a></td>"));
	}
	

	public void test_subDirectory() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath+ "/content/textFolder");
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertTrue(response.contains("folder21"));
		Assert.assertTrue(response.contains("folder22"));
		Assert.assertTrue(response.contains("textFile1.txt"));
	}
	

	public void test_openFile_text() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath+ "/content/textFolder/textFile1.txt");
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals("This is test file 1", response);
	}
	

	public void test_openFile_image() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath+ "/content/imageFolder/imageFile1.png");
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertNotNull(response);
	}
	
	public void test_openFile_pdf() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath+ "/content/pdfFolder/pdfFile1.pdf");
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertNotNull(response);
	}
}
