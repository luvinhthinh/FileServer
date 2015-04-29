package functional;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTest {
	private static String localhost = "http://localhost:8080";
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
	
	@Test
	public void test_wrongContextPath() throws Exception{
		HttpURLConnection connection = doGET(localhost + "/test");
		Assert.assertEquals(404, connection.getResponseCode());
	}
	
	@Test
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
	
	@Test
	public void test_NonExistFile() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath + "/test");
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals("No such file or directory.", getResponseContent(connection));
	}
	
	@Test
	public void test_homeDirectory() throws Exception{
		HttpURLConnection connection = doGET(localhost + contextPath);
		String response = getResponseContent(connection);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertTrue(response.contains("htmlFolder"));
		Assert.assertTrue(response.contains("imageFolder"));
		Assert.assertTrue(response.contains("pdfFolder"));
		Assert.assertTrue(response.contains("textFolder"));
		Assert.assertTrue(response.contains("file1"));
		Assert.assertTrue(response.contains("file2"));
		
		Assert.assertTrue(response.contains("<tr><td><a href=\""+localhost+contextPath+"/content/htmlFolder\">htmlFolder</a></td></tr>"));
		
	}
}
