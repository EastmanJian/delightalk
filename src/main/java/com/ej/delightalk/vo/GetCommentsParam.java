package com.ej.delightalk.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetCommentsParam {
    public String pageURL;
    public int lastN;
}
