package com.ej.delightalk.resource;

import com.ej.delightalk.dao.CommentsDAO;
import com.ej.delightalk.dao.RedisCommentsDAO;
import com.ej.delightalk.vo.AddCommentParam;
import com.ej.delightalk.vo.GetCommentsParam;
import com.ej.delightalk.vo.RecentComments;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

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
    public RecentComments getRecentComments(@PathParam("siteName") String siteName, GetCommentsParam param) throws IOException {
        CommentsDAO dao = new RedisCommentsDAO();
        return dao.getRecentComments(siteName, param.pageURL, param.lastN);
    }


    @Path("{siteName}/addComment")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("siteName") String siteName, AddCommentParam param, @Context HttpServletRequest requestContext) throws IOException {
        CommentsDAO dao = new RedisCommentsDAO();
        dao.addComment(siteName, param.pageURL, param.user, requestContext.getRemoteAddr(), param.comment);
        return Response.ok().build();
    }
}
