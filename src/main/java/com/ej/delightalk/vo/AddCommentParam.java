package com.ej.delightalk.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddCommentParam {
    public String pageURL;
    public String user;
    public String comment;
}
