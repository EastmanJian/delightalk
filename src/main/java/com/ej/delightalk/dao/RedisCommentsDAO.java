package com.ej.delightalk.dao;

import com.ej.delightalk.util.SSLUtil;
import com.ej.delightalk.vo.Comment;
import com.ej.delightalk.vo.RecentComments;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        String pageUrlMD5 = SSLUtil.MD5(pageUrl);
        String pageKey = "comments:" + siteName + ":" + pageUrlMD5;
        RecentComments recentComments = new RecentComments();
        try (Jedis jedis = pool.getResource()) {
            List<String> pageComments = jedis.lrange(pageKey, -lastN, -1);
            for (String commentItemID : pageComments) {
                Map<String, String> commentMap = jedis.hgetAll("comment:" + commentItemID);
                Comment comment = new Comment(
                        commentMap.get("user"),
                        commentMap.get("IP"),
                        Long.valueOf(commentMap.get("timestamp")),
                        commentMap.get("comment")
                );
                recentComments.addComment(comment);
            }
        }
        return recentComments;
    }

    @Override
    public void addComment(String siteName, String pageUrl, String user, String ip, String comment) {
        long timestamp = new Date().getTime();
        String pageUrlMD5 = SSLUtil.MD5(pageUrl);
        String pageKey = "comments:" + siteName + ":" + pageUrlMD5;
        String commentItemID = SSLUtil.MD5(pageUrl + user + ip + timestamp);
        try (Jedis jedis = pool.getResource()) {
            jedis.rpush(pageKey, commentItemID);
            Map<String, String> commentMap = new ConcurrentHashMap();
            commentMap.put("user", user);
            commentMap.put("IP", ip);
            commentMap.put("timestamp", "" + timestamp);
            commentMap.put("comment", comment);
            jedis.hmset("comment:" + commentItemID, commentMap);
            jedis.sadd("siteURLs:" + siteName, pageUrlMD5 + ":" + pageUrl);
        }
    }

    @Override
    public void housekeep(String siteName, int lastN) {
        //Todo
    }

    public static void closePool() {
        if (pool != null) {
            pool.close();
        }
    }
}
