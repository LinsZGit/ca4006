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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
 
@Path("/propertys")/*cust’r service’s relative root URI*/  

public class PropertysResource {
	private LocalDate today = LocalDate.now();
	private Map<Integer, Property> propertyDB = new  ConcurrentHashMap<Integer, Property>();   
	private AtomicInteger idCounter = new AtomicInteger();    
	
	public PropertysResource() {  }   
	@POST   
	@Consumes("application/xml")  
	
	public Response createProperty(InputStream is) {      
		Property property1 = readProperty(is);      
		property1.setId(idCounter.incrementAndGet());    
		propertyDB.put(property1.getId(), property1);      
		System.out.println("Created property " +  property1.getId());      
		return Response.created(URI.create("/propertys/" +  property1.getId())).build(); 
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
	@GET
	@Path("/min={min}&&max={max}")
	@Produces("application/xml") 
	public StreamingOutput findProperty(@PathParam("min") int min,@PathParam("max") int max){
		if (propertyDB.isEmpty()) {         
			throw new WebApplicationException(Response.Status.NOT_FOUND);      
		}
		List<Property> result = new ArrayList<>();
		Set<Integer> id = propertyDB.keySet();
		for(Integer i: id){
			Property prob = propertyDB.get(i);
			int price = Integer.parseInt(prob.getPrice());
			LocalDate start = prob.getStart();
			LocalDate end = prob.getEnd();
			if(price >= min && price <= max){
				if((start.isBefore(today)||start.equals(today))&&(end.isAfter(today)|| end.equals(today))){
					result.add(prob);
				}
			}
			
		}
		
		return new StreamingOutput() {         
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {            
				outputPropertys(outputStream, result);         
			}      
		};
	}
	
	/*
	@GET   
	@Path("min = {min_price}")   
	@Produces("application/xml")   
	public StreamingOutput getRange(@PathParam("min_price") int min) {      
		final Property prob = propertyDB.get(id);      
		if (prob == null) {         
			throw new WebApplicationException(Response.Status.NOT_FOUND);      
		}      
		return new StreamingOutput() {         
			public void write(OutputStream outputStream) throws IOException, WebApplicationException {            
				outputProperty(outputStream, prob);         
			}      
		};
	}*/
	
	@PUT 
	@Path("{id}")
	@Consumes("application/xml")   
	public void updateProperty(@PathParam("id") int id,  InputStream is) {      
		Property update = readProperty(is);      
		Property property1 = propertyDB.get(id);      
		if (property1 == null) throw new WebApplicationException(Response.Status.NOT_FOUND); 
		property1.setType(update.getType()); 
		property1.setDistrict(update.getDistrict());      
		property1.setPrice(update.getPrice());    
		property1.setStart(update.getStart().toString());   
		property1.setEnd(update.getEnd().toString());   
	}
	
	
	protected void outputPropertys(OutputStream os, List<Property> propertys) throws IOException {  
		PrintStream writer = new PrintStream(os);
		writer.println("<property_list>");  
		for(Property prop: propertys){
			writer.println("   <property id=\"" + prop.getId() + "\">");  
			writer.println("      <type>" + prop.getType() + "</type>");  
			writer.println("      <district>" + prop.getDistrict() + "</district>");      
			writer.println("      <bedroom>" + prop.getBedroom() + "</bedroom>"); 
			writer.println("      <price>" + prop.getPrice() + "</price>");    
			writer.println("      <startTime>" + prop.getStart() + "</startTime>"); 
			writer.println("      <endTime>" + prop.getEnd() + "</endTime>"); 
			writer.println("   </property>"); 
		}
		writer.println("</property_list>"); 
	}
	 
	protected void outputProperty(OutputStream os, Property prop) throws IOException {      
		PrintStream writer = new PrintStream(os);      
		writer.println("<property id=\"" + prop.getId() + "\">");  
		writer.println("   <type>" + prop.getType() + "</type>");  
		writer.println("   <district>" + prop.getDistrict() + "</district>");      
		writer.println("   <bedroom>" + prop.getBedroom() + "</bedroom>"); 
		writer.println("   <price>" + prop.getPrice() + "</price>");    
		writer.println("   <startTime>" + prop.getStart() + "</startTime>"); 
		writer.println("   <endTime>" + prop.getEnd() + "</endTime>"); 
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
				if (element.getTagName().equals("type")){               
					prop.setType(element.getTextContent());            
				}
				else if (element.getTagName().equals("district")) {               
					prop.setDistrict(element.getTextContent());            
				}
				else if (element.getTagName().equals("bedroom")){               
					prop.setBedroom(element.getTextContent());            
				} 
				else if (element.getTagName().equals("price")){               
					prop.setPrice(element.getTextContent());            
				} 
				else if (element.getTagName().equals("startTime")){               
					prop.setStart(element.getTextContent());            
				}
				else if (element.getTagName().equals("endTime")){               
					prop.setEnd(element.getTextContent());            
				}
			}         
				
			return prop;     
		}      
		catch (Exception e) {         
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);      
		} 
	}
	
	
}
