package com.ej.delightalk.resource;

import com.ej.delightalk.vo.AddCommentParam;
import com.ej.delightalk.vo.Comment;
import com.ej.delightalk.vo.GetCommentsParam;
import com.ej.delightalk.vo.RecentComments;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
    public RecentComments getRecentComments(@PathParam("siteName") String siteName, GetCommentsParam param) {
        RecentComments recentComments = new RecentComments();
        Comment comment;
        comment = new Comment("Eastman", "116.23.248.169", 1513246350761L, "I'm happy today.");
        recentComments.addComment(comment);
        comment = new Comment("Eastman", "116.23.248.169", 1513246350772L, "I'm sad today.");
        recentComments.addComment(comment);
        comment = new Comment("Endora", "116.23.248.170", 1513246350783L, "I love dad.");
        recentComments.addComment(comment);
        return recentComments;
    }


    @Path("{siteName}/addComment")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("siteName") String siteName, AddCommentParam param, @Context HttpServletRequest requestContext) {
        System.out.println("siteName=" + siteName);
        System.out.println("param.pageURL=" + param.pageURL);
        System.out.println("param.user=" + param.user);
        System.out.println("param.comment=" + param.comment);
        System.out.println("requestContext.ip=" + requestContext.getRemoteAddr());
        return Response.ok().build();
    }
}
