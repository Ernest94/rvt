/**
 * 
 */
package nu.educom.rvt.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.ThreadContext;

/**
 * 
 */
@Provider
@PreMatching
public class LogFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
    	ThreadContext.put("user.name", "User Name"); // TODO get from HttpSession o.i.d. using ContainerRequestContex ?
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		ThreadContext.clearMap();
	}
}
