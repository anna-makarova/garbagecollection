package com.rest;

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/")
public class RestResources {
    private static final Logger log = Logger.getLogger(RestResources.class.getName());
    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest(@QueryParam("id") Integer id, @QueryParam("string") String s) {
        try {
            JSONObject out = new JSONObject();
            out.put("id", id);
            out.put("string", s);

            log.info("called: /test returned: " + out.toString());
            return Response.status(Response.Status.OK).entity(out.toString()).build();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}