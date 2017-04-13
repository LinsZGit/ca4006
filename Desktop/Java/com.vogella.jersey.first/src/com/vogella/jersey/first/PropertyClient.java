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
					+ "<district>3</district>"                 
					+ "<bedroom>2</bedroom>"                 
					+ "<price>375,000</price>"                                
					+ "</property>"; 

			xml[1] = "<property>"
					+ "<district>5</district>"                 
					+ "<bedroom>3</bedroom>"                 
					+ "<price>360,000</price>"                                
					+ "</property>"; 
			xml[2] = "<property>"
					+ "<district>3</district>"                 
					+ "<bedroom>3</bedroom>"                 
					+ "<price>500,000</price>"                                
					+ "</property>"; 
			xml[3] = "<property>"
					+ "<district>5</district>"                 
					+ "<bedroom>2</bedroom>"                 
					+ "<price>250,000</price>"                                
					+ "</property>"; 
			xml[4] = "<property>"
					+ "<district>7</district>"                 
					+ "<bedroom>1</bedroom>"                 
					+ "<price>150,000</price>"                                
					+ "</property>"; 
			for(String s: xml){
				Response response = client.target("http://localhost:8080/com.vogella.jersey.first/services/properties").request().post(Entity.xml(s)); 
			
			
			
				if (response.getStatus() != 201) throw new  RuntimeException("Failed to create");         
				String location = response.getLocation().toString();        
				System.out.println("Location: " + location); /*as URI*/         
				response.close();     
			}
	   } 
		catch(Exception e){
			System.out.println(e);
		}finally {         
		
		   client.close();      
		}   
	} 
}
