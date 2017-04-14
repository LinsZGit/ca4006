package com.vogella.jersey.first;
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList; 
import javax.ws.rs.*; /* GET, PUT, POST, Consumes Stuff*/ 
import javax.ws.rs.PathParam; 
import javax.ws.rs.Produces; 
import javax.ws.rs.WebApplicationException; 
import javax.ws.rs.core.Response; 
import javax.ws.rs.core.StreamingOutput; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import java.io.*; 
import java.net.URI; 
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
 
@Path("/properties")/*cust’r service’s relative root URI*/  

public class PropertyResource {
	
	private Map<Integer, Property> propertyDB = new  ConcurrentHashMap<Integer, Property>();   
	private AtomicInteger idCounter = new AtomicInteger();    
	
	public PropertyResource() {  }   
	@POST   
	@Consumes("application/xml")  
	
	public Response createProperty(InputStream is) {      
		Property property1 = readProperty(is);      
		property1.setId(idCounter.incrementAndGet());    
		propertyDB.put(property1.getId(), property1);      
		System.out.println("Created property " +  property1.getId());      
		return Response.created(URI.create("/properties/" +  property1.getId())).build(); 
	}
	
	@GET   
	@Path("{id}")   
	@Produces("application/xml")   
	public StreamingOutput getProperty(@PathParam("id") int id) {      
		final Property prob = propertyDB.get(id);      
		if (prob == null) {         
			throw new WebApplicationException(Response.Status.NOT_FOUND);      
		}      
		return new StreamingOutput() {         
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {            
				outputProperty(outputStream, prob);         
			}      
		};
	}
	

	
	@PUT 
	@Path("{id}")
	@Consumes("application/xml")   
	public void updateProperty(@PathParam("id") int id,  InputStream is) {      
		Property update = readProperty(is);      
		Property property1 = propertyDB.get(id);      
		if (property1 == null) throw new WebApplicationException(Response.Status.NOT_FOUND); 
	 
		property1.setDistrict(update.getDistrict());      
		property1.setPrice(update.getPrice());      
	}
	 
	 
	protected void outputProperty(OutputStream os, Property prop) throws IOException {      
		PrintStream writer = new PrintStream(os);      
		writer.println("<property id=\"" + prop.getId() + "\">");      
		writer.println("   <district>" + prop.getDistrict() + "</district>");      
		writer.println("   <bedroom>" + prop.getBedroom() + "</bedroom>"); 
		writer.println("   <price>" + prop.getPrice() + "</price>");          
		writer.println("</property>");   
	}
	
	protected Property readProperty(InputStream is) {      
		try {         
			DocumentBuilder builder = /* create DOM Doc from XML*/ 
			DocumentBuilderFactory.newInstance().newDocumentBuilder();         
			Document doc = builder.parse(is); /*parse, rtn DOM */         
			Element root = doc.getDocumentElement();/*doc element*/         
			Property prop = new Property();         
			if (root.getAttribute("id") != null &&  !root.getAttribute("id").trim().equals(""))          
				prop.setId(Integer.valueOf(root.getAttribute("id")));         
			NodeList nodes = root.getChildNodes();         
			for (int i = 0; i < nodes.getLength(); i++) {            
				Element element = (Element) nodes.item(i);            
				if (element.getTagName().equals("district")) {               
					prop.setDistrict(element.getTextContent());            
				}   
				else if (element.getTagName().equals("bedroom")){               
					prop.setBedroom(element.getTextContent());            
				} 
				else if (element.getTagName().equals("price")){               
					prop.setPrice(element.getTextContent());            
				}                   
			}         
				
			return prop;     
		}      
		catch (Exception e) {         
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);      
		} 
	}
	
	
}
