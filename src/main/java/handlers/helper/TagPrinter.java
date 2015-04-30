package handlers.helper;

public class TagPrinter {
	
	public TagPrinter(){
		
	}
	
	public String printTag_row(String text){
		return "<tr><td>"+text+"</td></tr>";
	}
	
	public String printTag_row(String text, String type){
		return "<tr><td>"+type+"</td><td>"+text+"</td></tr>";
	}
	
	public String printTag_link(String location, String text){
		return "<tr><td><a href=\""+location+"\">"+text+"</a></td></tr>";
	}
	
	public String printTag_link(String location, String text, String type){
		return "<tr><td width=\"10%\">"+type+"</td><td><a href=\""+location+"\">"+text+"</a></td></tr>";
	}
}
