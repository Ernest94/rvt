package nu.educom.rvt.rest;

import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://0.0.0.0").port(8081).build();
  }
  
  private static URI getBaseURISecured(){
    return UriBuilder.fromUri("https://0.0.0.0/").port(8081).build();
}

  static final URI BASE_URI = getBaseURI();
  
  static final URI BASE_URI_SECURED = getBaseURISecured();
  
  static final boolean SECURED = true;
  

  static HttpServer startServer() {
	ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
    ResourceConfig rc = ResourceConfig.forApplication(new MyApp());
//	ResourceConfig rc = new ResourceConfig().packages("nu.educom.rvt.rest");
    
    if(SECURED) {
      SSLContextConfigurator sslCon=new SSLContextConfigurator();
      sslCon.setKeyStoreFile("/var/www/vhosts/vps-edu-detacom.hostnet.nl/voortgang.educom.nu/ssl/educom_voortgang.keystore"); // contains server keypair
      sslCon.setKeyStorePass("?120qhZl");
      sslCon.setKeyStoreType("PKCS12");
      sslCon.setTrustStoreFile("/var/www/vhosts/vps-edu-detacom.hostnet.nl/voortgang.educom.nu/ssl/educom_voortgang.truststore"); // contains client certificate
      sslCon.setTrustStorePass("?120qhZl");
      sslCon.setTrustStoreType("PKCS12");
      sslCon.setSecurityProtocol("TLSv1.3");
      if (!sslCon.validateConfiguration(true)) {
          System.out.println("Context is not valid");

        }
      return GrizzlyHttpServerFactory.createHttpServer(BASE_URI_SECURED, rc, true, new SSLEngineConfigurator(sslCon).setClientMode(false).setNeedClientAuth(false), locator);
    }
    return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc, locator);
  }

  
  
  public static void main(String[] args) throws IOException {
    System.out.println("Starting grizzly...");
    HttpServer httpServer = startServer();
	if (Filler.isDatabaseEmpty()) {
		Filler.fillDatabase();
	}
    System.in.read();
    httpServer.shutdownNow();
  }
}
