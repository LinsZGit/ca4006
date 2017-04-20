package com.dcu.property.system;

import java.util.Map;

public class ListingManager implements Runnable{

	private Map<Integer, Property> propertyDB;

	public ListingManager(Map<Integer, Property> props)
	{
		this.propertyDB = props;
	}

	public void run()
	{
		//every 10 minutes, iterate through all listings, changing the 'active' variable where appropriate
		while(true)
		{
			try{
				Thread.sleep(60000);

				for (Map.Entry<Integer, Property> entry : propertyDB.entrySet())
				{
				    System.out.println(entry.getKey() + "/" + entry.getValue());
				}

				Thread.sleep(60000);

			} catch(InterruptedException e)
			{

			}
		}
	}
}
