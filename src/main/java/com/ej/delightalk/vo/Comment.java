package com.ej.delightalk.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comment {
    public String user;
    public String ip;
    public long timestamp;
    public String comment;

    public Comment() {}

    public Comment(String user, String ip, long timestamp, String comment) {
        this.user = user;
        this.ip = ip;
        this.timestamp = timestamp;
        this.comment = comment;
    }
}
