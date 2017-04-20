package com.dcu.property.system;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
@ApplicationPath("/services")
public class PropertyApplication extends Application{

	private Set<Object> singletons = new HashSet<Object>();

	public PropertyApplication() {
		singletons.add(new PropertysResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
