package com.xyzcorp.jersey;

import com.xyzcorp.hystrix.CommandHelloWorld;
import com.xyzcorp.hystrix.Gate1Command;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        Gate1Command gate1Command = new Gate1Command();
        String result = gate1Command.execute();
        return "Got it! Your name is " + result;
    }
}

