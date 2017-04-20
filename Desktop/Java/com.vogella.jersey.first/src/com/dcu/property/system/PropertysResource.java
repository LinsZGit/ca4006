package com.dcu.property.system;

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

@Path("/propertys")/*customer services relative root URI*/

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
			System.out.println("No matching property found");
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
			System.out.println("No matching property found");
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

	
	@GET
	@Path("/count")
	@Produces("text/plain")
	public int getProperty() {
		if(propertyDB.isEmpty()){
			return 0;
		}
		return propertyDB.size();
	}

	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public void updateProperty(@PathParam("id") int id,  InputStream is) throws Exception {
		Property update = readProperty(is);
		Property property1 = propertyDB.get(id);
		if (property1 == null){
			System.out.println("No matching property found");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		property1.setType(update.getType());
		property1.setDistrict(update.getDistrict());
		property1.setPrice(update.getPrice());
		property1.setStart(update.getStart().toString());
		property1.setEnd(update.getEnd().toString());
		boolean checkBid = property1.makeBid(update.getBid());
		if(checkBid){
			property1.setBid(update.getBid());
			property1.setBidder(update.getBidder());
		}
		else{
			throw new java.lang.Error("There is higher bid");
		}
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
			writer.println("      <startTime>" + prop.getStart().toString() + "</startTime>");
			writer.println("      <endTime>" + prop.getEnd().toString() + "</endTime>");
			writer.println("      <bidder>" + prop.getBidder() + "</bidder>");
			writer.println("      <bid>" + prop.getBid() + "</bid>");
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
		writer.println("   <startTime>" + prop.getStart().toString() + "</startTime>");
		writer.println("   <endTime>" + prop.getEnd().toString() + "</endTime>");
		writer.println("   <bidder>" + prop.getBidder() + "</bidder>");
		writer.println("   <bid>" + prop.getBid() + "</bid>");
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
				else if (element.getTagName().equals("bid")){
					prop.setBid(Integer.parseInt(element.getTextContent()));
				}
				else if (element.getTagName().equals("bidder")){
					prop.setBidder(element.getTextContent());
				}
			}

			return prop;
		}
		catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}


}
