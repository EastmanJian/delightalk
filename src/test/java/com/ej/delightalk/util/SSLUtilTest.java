package com.ej.delightalk.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class SSLUtilTest {
    @Test
    public void testMD5() throws Exception {
        String data = "eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing";
        String md5 = SSLUtil.MD5(data);
        System.out.println(md5);
        //should be the same as the openssl result
        // $ echo -n "eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing" | openssl md5
        //(stdin)= f6a79b682067ac33ed2b45f2ace68e56
        assertEquals(md5, "f6a79b682067ac33ed2b45f2ace68e56");
    }

}