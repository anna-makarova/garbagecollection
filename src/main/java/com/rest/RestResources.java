package com.rest;

import com.db.DatabaseUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;



@Path("/")
public class RestResources {
    private static final Logger log = Logger.getLogger(RestResources.class.getName());
    @Path("echo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doEcho(@QueryParam("id") Integer id, @QueryParam("string") String s) {
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
    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest(@QueryParam("id") int id, @QueryParam("sting") String s) {
        try {

            String SQLStatement = "CALL test(" + id + ",'" + s +"')";
            log.info("SQLStatement: " + SQLStatement);
            JSONArray result = DatabaseUtil.executeStatement(SQLStatement);

            String out = "{\"result\":" + result.toString() + "}";
            log.info("Response: " + out);
            return Response.status(Response.Status.OK).entity(out.toString()).build();

        } catch (Exception e) {
            log.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}