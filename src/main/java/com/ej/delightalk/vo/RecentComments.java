package com.ej.delightalk.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class RecentComments {
    public List<Comment> recentComments;

    public RecentComments() {
        recentComments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        recentComments.add(comment);
    }
}
