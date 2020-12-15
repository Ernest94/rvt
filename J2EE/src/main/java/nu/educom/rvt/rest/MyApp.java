package nu.educom.rvt.rest;

import java.util.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationPath("")
public class MyApp extends Application {
	private static final Logger LOG = LogManager.getLogger();
	
	
	public MyApp() {
		super();
		LOG.info("----------------------------------------------------------");
		LOG.info("|  Registratie Voortgang Trainees versie 1.0.0 Started   |");
		LOG.info("----------------------------------------------------------");
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<>();
		set.add(UserResource.class);
		set.add(ThemeConceptResource.class);
		set.add(ReviewResource.class);
		set.add(LocationResource.class);
		set.add(BundleResource.class);
		set.add(CORSFilter.class);
		set.add(LogFilter.class);
//		LOG.info("classes: {}", set);
		return set;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return Collections.emptySet();
	}
	
}
