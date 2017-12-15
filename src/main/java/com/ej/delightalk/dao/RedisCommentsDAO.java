package com.ej.delightalk.dao;

import com.ej.delightalk.vo.Comment;
import com.ej.delightalk.vo.RecentComments;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class RedisCommentsDAO implements CommentsDAO {
    private static JedisPool pool;
    private static String authPass;

    public RedisCommentsDAO() throws IOException {
        //read Redis connection parameters from properties file.
        Properties pps = new Properties();
        InputStream in = getClass().getResourceAsStream("/redis.properties");
        pps.load(in);
        String host = pps.getProperty("host");
        int port = Integer.valueOf(pps.getProperty("port"));
        authPass = pps.getProperty("pass");

        //lazy initiate connection pool
        if (pool == null) {
            pool = new JedisPool(new JedisPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, authPass);
        }
    }

    @Override
    public RecentComments getRecentComments(String siteName, String pageUrl, int lastN) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set("RedisCommentsDAO", "bar");
            String foobar = jedis.get("RedisCommentsDAO");
            jedis.zadd("sose", 0, "car");
            jedis.zadd("sose", 0, "bike");
            Set<String> sose = jedis.zrange("sose", 0, -1);
        }
        return null;
    }

    @Override
    public void addComment(String siteName, String pageUrl, Comment comment, String ip) {

    }

    @Override
    public void housekeep(String siteName, int lastN) {

    }

    public static void closePool() {
        if (pool != null) {
            pool.close();
        }
    }
}
