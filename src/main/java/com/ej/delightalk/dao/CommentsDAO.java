package com.ej.delightalk.dao;

import com.ej.delightalk.vo.Comment;
import com.ej.delightalk.vo.RecentComments;

public interface CommentsDAO {
    RecentComments getRecentComments(String siteName, String pageUrl, int lastN);

    void addComment(String siteName, String pageUrl, String user, String ip, String comment);

    void housekeep(String siteName, int lastN);

}
