package com.dcu.property.system;

import java.util.Scanner;

import javax.ws.rs.client.Client; /* interface to build/execute           client Reqs to consume resps returned */
import javax.ws.rs.client.ClientBuilder; /*entry pt to Client*/
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class UpClient {

  /*public static void initialProperties(Client client)
  {
    //if property count returns 0, make some properties
    response = client.target("http://localhost:8080/com.dcu.property.system/services/propertys").request()


    String[] xml = new String[5];
    xml[0]= "<property>"
        + "<type>H1</type>"
        + "<district>3</district>"
        + "<bedroom>2</bedroom>"
        + "<price>375000</price>"
        + "<startTime>2017-01-15</startTime>"
        + "<endTime>2017-01-30</endTime>"
        + "</property>";

    xml[1] = "<property>"
        + "<type>H2</type>"
        + "<district>5</district>"
        + "<bedroom>3</bedroom>"
        + "<price>360000</price>"
        + "<startTime>2017-03-15</startTime>"
        + "<endTime>2017-03-30</endTime>"
        + "</property>";
    xml[2] = "<property>"
        + "<type>H3</type>"
        + "<district>3</district>"
        + "<bedroom>3</bedroom>"
        + "<price>500000</price>"
        + "<startTime>2017-01-15</startTime>"
        + "<endTime>2017-08-30</endTime>"
        + "</property>";
    xml[3] = "<property>"
        + "<type>A1</type>"
        + "<district>5</district>"
        + "<bedroom>2</bedroom>"
        + "<price>250000</price>"
        + "<startTime>2017-04-30</startTime>"
        + "<endTime>2017-06-30</endTime>"
        + "</property>";
    xml[4] = "<property>"
        + "<type>A2</type>"
        + "<district>7</district>"
        + "<bedroom>1</bedroom>"
        + "<price>150000</price>"
        + "<startTime>2017-02-15</startTime>"
        + "<endTime>2018-04-30</endTime>"
        + "</property>";
    for(String s: xml){
      Response response = client.target("http://localhost:8080/com.dcu.property.system/services/propertys").request().post(Entity.xml(s));



      if (response.getStatus() != 201) throw new  RuntimeException("Failed to create");
      String location = response.getLocation().toString();

      System.out.println("Location: " + location); /*as URI*/
      //response.close();
  //}

	public static void newProperty(Client client, Scanner scan)
	{
	  //no type checking as of yet

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

	  System.out.println(xml); //debug only

	  Response response = client.target("http://localhost:8080/com.dcu.property.system/services/propertys").request().post(Entity.xml(xml));

	  if (response.getStatus() != 201) throw new  RuntimeException("Failed to create");
	  String location = response.getLocation().toString();

	  System.out.println("Location: " + location); /*as URI*/
	  response.close();
	}

	public static void main (String[] args) throws Exception{

    Scanner scan = new Scanner(System.in);
    String input = "";
    System.out.print("Please input a username: ");
    String username = scan.nextLine();
  	Client client = ClientBuilder.newClient();


    while((input.compareTo("exit")) != 0)
    {
      System.out.println("Please select an option from the following list: ");
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
          //listProperties(client, scan);
          break;
        case "3" :
          System.out.println("Selected: [3]");
          //bidProperty(client, scan, username);
          break;
        case "exit" :
          break;
        default :
          System.out.println("Input not Recognised");
      }
    }
	}
}
