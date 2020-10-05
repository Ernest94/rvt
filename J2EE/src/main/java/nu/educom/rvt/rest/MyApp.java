package nu.educom.rvt.rest;

import java.util.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("")
public class MyApp extends Application {
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(UserResource.class);
        set.add(CORSFilter.class);
        return set;
    }
	
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<>();
		set.add(UserResource.class);
		set.add(CORSFilter.class);
		return set;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return Collections.emptySet();
	}
	
}
