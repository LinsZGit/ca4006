package com.dcu.property.system;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.ws.rs.client.Client; /* interface to build/execute           client Reqs to consume resps returned */
import javax.ws.rs.client.ClientBuilder; /*entry pt to Client*/
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class PropertyClient {

	public static void newProperty(Client client, Scanner scan)
	{
	  //enter main info to create property

	  System.out.println("Enter property type: ");
	  String type = scan.nextLine();
	  System.out.println("Enter district: ");
	  String district = scan.nextLine();
	  System.out.println("Enter bedroom count: ");
	  String bedroom = scan.nextLine();
	  System.out.println("Enter price: ");
	  String price = scan.nextLine();
	  System.out.println("Enter sale opening time in the format: YYYY-MM-DD");
	  String opening = scan.nextLine();
	  System.out.println("Enter sale closing time in the format: YYYY-MM-DD");
	  String closing = scan.nextLine();


	  String xml = "<property><type>" + type + "</type>";
	  xml = xml + "<district>" + district + "</district>";
	  xml = xml + "<bedroom>" + bedroom + "</bedroom>";
	  xml = xml + "<price>" + price + "</price>";
	  xml = xml + "<startTime>" + opening + "</startTime>";
	  xml = xml + "<endTime>" + closing + "</endTime></property>";

	  //System.out.println(xml); //debug only
	  //post new property 
	  Response response = client.target("http://localhost:8080/com.dcu.property.system/services/propertys").request().post(Entity.xml(xml));

	  if (response.getStatus() != 201){//run time error 
		  System.out.println("new property did not creat successfully");
		  throw new  RuntimeException("Failed to create");
	  }
	  response.close();
	}

	public static void listProperties(Client client, Scanner scan){
		try{
			System.out.println("Please select an option from the following list: ");
	        System.out.println("[1]Find property by property ID");
	        System.out.println("[2]List Available properties which are open currently");
	        System.out.println("");
	        String location ="";
	        BufferedReader in;
	        URL readURL;
	        String inputLine = "";
	        System.out.println("'exit' to close");
	        String input = scan.nextLine();
	        switch (input) {
		        case "1" :
		          System.out.println("Selected: [1]Please enter the ID"); //find property by ID
		          input = scan.nextLine();
		          location = "http://localhost:8080/com.dcu.property.system/services/propertys/" + input;
		          readURL = new URL(location);
		          in = new BufferedReader(new InputStreamReader(readURL.openStream()));
		          while ((inputLine = in.readLine()) != null)
		              System.out.println(inputLine);
		          in.close();
		          System.out.println();
		          break;
		        case "2" :
		          System.out.println("Selected: [2]");//find property by price range
		          System.out.println("Please enter min price of property");
		          String min = scan.nextLine();
		          System.out.println("Please enter max price of property");
		          String max = scan.nextLine();
		          location = "http://localhost:8080/com.dcu.property.system/services/propertys/min=" + min + "&&max=" + max;
		          readURL = new URL(location);
		          in = new BufferedReader(new InputStreamReader(readURL.openStream()));
		          while ((inputLine = in.readLine()) != null)
		              System.out.println(inputLine);
		          in.close();
		          System.out.println();
		          break;
		        case "exit" :
		          break;
		        default :
		          System.out.println("Input not Recognised");
	        }
		}
		catch(FileNotFoundException e){
			System.out.println("Can not find matching property");
			
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		} catch (IOException e) {
			System.out.println("Can not find matching property");//if there is no property which can be found 
		}
	}

	public static void bidProperty(Client client, Scanner scan, String username) throws IOException{

		System.out.println("Please enter the ID of the property you want to bid on: ");
		int bidId = Integer.parseInt(scan.nextLine());
		System.out.println("Please enter the price you want to bid: ");
		int price = Integer.parseInt(scan.nextLine());
		
		
		String location = "http://localhost:8080/com.dcu.property.system/services/propertys/" + bidId;
		URL readURL = new URL(location);
        BufferedReader in = new BufferedReader(new InputStreamReader(readURL.openStream()));
        String inputLine = "";
        String previous = "";

        while ((inputLine = in.readLine()) != null){
            previous = previous + inputLine.trim();
        }
        	
        in.close();
        int index = previous.indexOf("<bidder>");
        //generate new property string
        String updateBid = previous.substring(0,index)
        				 + "<bidder>" + username + "</bidder>"
        				 + "<bid>" + price + "</bid>"
        				 +"</property>";
        Response response = client.target(location).request().put(Entity.xml(updateBid));        
         
        //error code
        if (response.getStatus() == 406){
  		  	System.out.println("Sorry, you need to provide higher bid price\n");
        }
        else if(response.getStatus() == 201){//run time error 
        	System.out.print("There is no matching property\n");
        }
  	    else if(response.getStatus() == 204){//succeed 
  	    	System.out.print("Congratulation! You have the highest bid\n");
  	    }
  	    else{
  	    	System.out.print("Exception error\n");
  	    }
        response.close();
	}
	
	public static void main (String[] args) throws Exception{

	    Scanner scan = new Scanner(System.in);
	    String input = "";
	    System.out.print("Please input a username: ");
	    String username = scan.nextLine();
	  	Client client = ClientBuilder.newClient();

	  	//main menu 
	    while((input.compareTo("exit")) != 0)
	    {
	    	System.out.println("\nPlease select an option from the following list: ");
	        System.out.println("[1]Create New Property");
	        System.out.println("[2]List Available Properties");
	        System.out.println("[3]Bid On A Property");
	        System.out.println("");
	        System.out.println("'exit' to close");
	        input = scan.nextLine();

	        switch (input) {
		        case "1" :
		          System.out.println("Selected: [1]");
		          newProperty(client, scan);
		          break;
		        case "2" :
		          System.out.println("Selected: [2]");
		          listProperties(client, scan);
		          break;
		        case "3" :
		          System.out.println("Selected: [3]");
		          bidProperty(client, scan, username);
		          break;
		        case "exit" :
		          break;
		        default :
		          System.out.println("Input not Recognised");
	        }
	    }
	}
}
