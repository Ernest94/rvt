package nu.educom.rvt.rest;

import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost").port(8081).build();
  }

  static final URI BASE_URI = getBaseURI();
  

  static HttpServer startServer() {
	ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
    ResourceConfig rc = ResourceConfig.forApplication(new MyApp());
//	ResourceConfig rc = new ResourceConfig().packages("nu.educom.rvt.rest");
    return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc, locator);
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Starting grizzly...");
    HttpServer httpServer = startServer();
    System.in.read();
    httpServer.shutdownNow();
  }
}
