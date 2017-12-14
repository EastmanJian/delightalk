package com.ej.delightalk.resource;

import com.ej.delightalk.vo.GetCommentsParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class CommentResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String siteDesc() {
        return "<html><head><meta charset=\"utf-8\"></head><body>" +
               "<h5>Delightalk commenting system - RESTful interface. By Eastman Jian, com.ej©2017</h5></body></html>";
    }


    @Path("{siteName}/getRecentComments")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getRecentComments(@PathParam("siteName") String siteName, GetCommentsParam param) {

        return "{\"result\":\"" + siteName + ", " + param.pageURL + ", " + param.lastN + "\"}";
    }


    @Path("{siteName}/addComment")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("siteName") String siteName) {

        return Response.ok().build();
    }
}
