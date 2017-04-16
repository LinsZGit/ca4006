package com.vogella.jersey.first;

import javax.ws.rs.client.Client; /* interface to build/execute           client Reqs to consume resps returned */ 
import javax.ws.rs.client.ClientBuilder; /*entry pt to Client*/ 
import javax.ws.rs.client.Entity; 
import javax.ws.rs.core.Response;

public class PropertyClient {
	public static void main (String[] args) throws Exception{ 
		Client client = ClientBuilder.newClient();      
		try {         
			System.out.println("*** Create a new Property ***"); 
			String[] xml = new String[5];
			xml[0]= "<property>"
					+ "<type>H1</type>"  
					+ "<district>3</district>"                 
					+ "<bedroom>2</bedroom>"                 
					+ "<price>375000</price>"       
					+ "<startTime>15/04/2017</startTime>" 
					+ "<endTime>30/04/2017</endTime>" 
					+ "</property>"; 

			xml[1] = "<property>"
					+ "<type>H2</type>"  
					+ "<district>5</district>"                 
					+ "<bedroom>3</bedroom>"                 
					+ "<price>360000</price>"  
					+ "<startTime>15/03/2017</startTime>" 
					+ "<endTime>30/03/2017</endTime>" 
					+ "</property>"; 
			xml[2] = "<property>"
					+ "<type>H3</type>"  
					+ "<district>3</district>"                 
					+ "<bedroom>3</bedroom>"                 
					+ "<price>500000</price>"  
					+ "<startTime>15/01/2017</startTime>" 
					+ "<endTime>30/08/2017</endTime>" 
					+ "</property>"; 
			xml[3] = "<property>"
					+ "<type>A1</type>"  
					+ "<district>5</district>"                 
					+ "<bedroom>2</bedroom>"                 
					+ "<price>250000</price>"
					+ "<startTime>30/04/2017</startTime>" 
					+ "<endTime>30/06/2017</endTime>" 
					+ "</property>"; 
			xml[4] = "<property>"
					+ "<type>A2</type>"  
					+ "<district>7</district>"                 
					+ "<bedroom>1</bedroom>"                 
					+ "<price>150000</price>" 
					+ "<startTime>15/02/2017</startTime>" 
					+ "<endTime>30/04/2018</endTime>" 
					+ "</property>"; 
			for(String s: xml){
				Response response = client.target("http://localhost:8080/com.vogella.jersey.first/services/propertys").request().post(Entity.xml(s)); 
			
			
			
				if (response.getStatus() != 201) throw new  RuntimeException("Failed to create");         
				String location = response.getLocation().toString();      
				
				System.out.println("Location: " + location); /*as URI*/         
				response.close();     
			}
			
			/* test GET method */         
			/*System.out.println("*** GET Created Customer **");   
			String location = "http://localhost:8080/com.vogella.jersey.first/services/propertys/100";
			String customer =  client.target(location).request().get(String.class);         
			System.out.println(customer); 
			 */
	        String updateCust = "<property>"
	        		+ "<type>H5</type>"  
					+ "<district>700000</district>"                 
					+ "<bedroom>1</bedroom>"                 
					+ "<price>150000</price>" 
					+ "<startTime>15/08/2017</startTime>" 
					+ "<endTime>30/12/2017</endTime>" 
					+ "</property>"; 
	        String location = "http://localhost:8080/com.vogella.jersey.first/services/propertys/3";
	 
	        /* test PUT method */         
	        Response response = client.target(location).request().put(Entity.xml(updateCust));         
	        if (response.getStatus() != 204) throw new  RuntimeException("Failed to update");         
	        response.close(); 
	 
	        //System.out.println("**** After Update ***");         
	        //String c = client.target(  location).request().get(String.class);         
	        //System.out.println(c);      
	   } 
		catch(Exception e){
			System.out.println(e);
		}finally {         
		
		   client.close();      
		}   
	} 
}
