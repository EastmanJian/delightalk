package com.ej.delightalk.dao;

import org.junit.After;
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
        dao.getRecentComments("ejBlog", "9e9a5923e83e7db270518fc27e30cdca", 10);
    }

    @Test
    public void addComment() throws Exception {

    }

    @Test
    public void housekeep() throws Exception {

    }

}