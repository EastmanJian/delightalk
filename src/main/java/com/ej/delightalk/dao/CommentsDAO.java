package com.ej.delightalk.dao;

import com.ej.delightalk.vo.Comment;
import com.ej.delightalk.vo.RecentComments;

public interface CommentsDAO {
    RecentComments getRecentComments(String siteName, String pageUrl, int lastN);

    void addComment(String siteName, String pageUrl, Comment comment, String ip);

    void housekeep(String siteName, int lastN);

}
