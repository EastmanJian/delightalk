package com.ej.delightalk.dao;

import com.ej.delightalk.vo.Comment;
import com.ej.delightalk.vo.RecentComments;
import com.google.gson.Gson;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class RedisCommentsDAOTest {
    @Before
    public void setUp() throws Exception {

    }

    @AfterClass
    public static void tearDown() throws Exception {
        RedisCommentsDAO.closePool();
    }

    @Test
    public void getRecentComments() throws Exception {
        CommentsDAO dao = new RedisCommentsDAO();
        RecentComments rc = dao.getRecentComments("ejTest", "ejTest.cn/blog/2017/05/07/using-markdown-for-web-writing", 10);
//        Gson gson = new Gson();
//        String json = gson.toJson(rc);
//        System.out.println("json=" + json);
        for (Comment comment: rc.getRecentComments()) {
            System.out.println("user=" + comment.user);
            System.out.println("ip=" + comment.ip);
            System.out.println("timestamp=" + comment.timestamp);
            System.out.println("comment=" + comment.comment);
        }
    }

    @Test
    public void addComment() throws Exception {
        CommentsDAO dao = new RedisCommentsDAO();
        dao.addComment("ejTest", "ejTest.cn/blog/2017/05/07/using-markdown-for-web-writing", "Eastman", "116.23.248.169", "I'm good! 我很好"); //default is UTF-8 encoding
        dao.addComment("ejTest", "ejTest.cn/blog/2017/05/07/using-markdown-for-web-writing", "Eastman", "116.23.248.169", new String("Thank you! 谢谢！!@#$%^&*()_+=-[];',./{}:\"<>?".getBytes(), "UTF-8"));
        dao.addComment("ejTest", "ejTest.cn/blog/2017/05/07/using-markdown-for-web-writing", "Eastman", "116.23.248.169", "Good bye! 再见! 良いさよなら! до свидания! ลาก่อน!");
    }

    @Test
    public void housekeep() throws Exception {

    }

}