DELIGHTALK Commenting System Architecture
=========================================


Data Elements
-------------
- Site Name (user defined)
- Web page URL  
    - domain
    - path
- A list of comments for a URL
- Comment Item
    - user
    - IP
    - timestamp
    - content

Access Pattern
--------------
### Front-end
- read recent n comments for a page
- insert a new comment
- modify a comment (Todo)
- delete a comment (Todo)

### Management Console
(Todo)

Data Store
----------
Whatever data store system is used (NoSQL or RDBMS), it exposes a common data access interface.      
### 1) Redis
#### Data Model
##### [List] Lists of comments per site page
- Key:  comments:{siteName}:{PageUrlMD5}
- values: {commentItemID} (md5 encoded PageUrl+user+IP+timestamp)
##### [Hash] Comment Item
- Key: comment:{commentItemID}
- fields:
    - user
    - IP
    - timestamp
    - comment
##### [Set] URL set of a Site
- Key: siteURLs:{siteName}
- values: {md5EncodedPageURL}|{pageURL}

#### Data Operations (derived by access pattern)
- Insert new comments  
e.g.
```
RPUSH comments:ejBlog:f6a79b682067ac33ed2b45f2ace68e56 0d5447138aeb12911af2a144884402c3
HMSET comment:0d5447138aeb12911af2a144884402c3 user "Eastman" IP "116.23.248.169" timestamp 1513246350761 comment "I'm happy today."
SADD siteURLs:ejBlog "f6a79b682067ac33ed2b45f2ace68e56:eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing"
    
RPUSH comments:ejBlog:f6a79b682067ac33ed2b45f2ace68e56 c7c895986c1503732e221e1d9158b8c3
HMSET comment:c7c895986c1503732e221e1d9158b8c3 user "Anonymous" IP "116.23.248.169" timestamp 1513246350772 comment "I'm sad today."
SADD siteURLs:ejBlog "f6a79b682067ac33ed2b45f2ace68e56:eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing"

RPUSH comments:ejBlog:f6a79b682067ac33ed2b45f2ace68e56 aa2cf207c93e830c6cee26781b609370
HMSET comment:aa2cf207c93e830c6cee26781b609370 user "Endora" IP "116.23.248.170" timestamp 1513246350783 comment "I love dad."
SADD siteURLs:ejBlog "f6a79b682067ac33ed2b45f2ace68e56:eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing"

RPUSH comments:ejBlog:c6f234a514aaa95ed9c876000b256db2 c9dd9c811465d2f40a29c12852d4a136
HMSET comment:c9dd9c811465d2f40a29c12852d4a136 user "Violet" IP "116.23.248.171" timestamp 1513246350794 comment "I miss you."
SADD siteURLs:ejBlog "c6f234a514aaa95ed9c876000b256db2:eastmanjian.cn/blog/2017/05/25/java-using-lambda-to-replace-anonymous-inner-class"

RPUSH comments:ejBlog:c6f234a514aaa95ed9c876000b256db2 dc111ad47363656efb1ec86b38986a4d
HMSET comment:dc111ad47363656efb1ec86b38986a4d user "Tom" IP "116.23.248.172" timestamp 1513246350805 comment "The quick brown fox."
SADD siteURLs:ejBlog "c6f234a514aaa95ed9c876000b256db2:eastmanjian.cn/blog/2017/05/25/java-using-lambda-to-replace-anonymous-inner-class"

RPUSH comments:anotherSite:e3de46d2edc0a573e37d954936989348 c0856fdea57e655474a411f2e388beea
HMSET comment:c0856fdea57e655474a411f2e388beea user "Alice" IP "116.23.248.173" timestamp 1513246350816 comment "Jumps over the lazy dog."
SADD siteURLs:anotherSite "e3de46d2edc0a573e37d954936989348:agilemanifesto.org/principles.html"

RPUSH comments:anotherSite:e3de46d2edc0a573e37d954936989348 1f136f9a9c7a25002afa355cf2f0be4f
HMSET comment:1f136f9a9c7a25002afa355cf2f0be4f user "Anonymous" IP "116.23.248.174" timestamp 1513246350816 comment "I'm sorry."
SADD siteURLs:anotherSite "e3de46d2edc0a573e37d954936989348:agilemanifesto.org/principles.html"
```

- Read recent n comments for a page
e.g. list recent 2 comments for a page
```
LRANGE "comments:ejBlog:f6a79b682067ac33ed2b45f2ace68e56" -10 -1
HGETALL comment:{the last -2 commentItemID}
HGETALL comment:{the last -1 commentItemID}
```

- List the pages who have comments in a site
e.g.
```
SMEMBERS siteURLs:ejBlog
```

#### Housekeeping, data limits, memory limit
e.g. keep last 100 comments for each page of a site
```
SMEMBERS siteURLs:ejBlog
LRANGE "comments:ejBlog:{the md5EncodedPageURLs listed above}" 0 -101
DEL comment:{the commentItemIDs listed above}
...
LTRIM "comments:ejBlog:9e9a5923e83e7db270518fc27e30cdca" -100 -1
...
```

### 2) MySQL (Todo)



Common Data Access Interface
----------------------------
The goal is that if switch data store to another platform (e.g. from Redis to MySQL), it just requires to configure the injection of the relevant data access implementation without changing the RESTful service layer who uses this common data access interface.
### Interface functions
  - getRecentComments(String siteName, String pageUrlMD5, int lastN)
  - addComment(String siteName, String pageUrlMD5, Comment comment, String ip)
  - housekeep(String siteName, int lastN)


RESTful Interface
-----------------
- get recent comments  
```
curl -X PUT -H 'Accept: application/json' -H 'Content-Type: application/json' -d '{"pageURL":"...", "lastN":10}' 'https://eastmanjian.cn/delightalk/{siteName}/getRecentComments/'
e.g.
curl -i -X PUT -H 'Accept: application/json' -H 'Content-Type: application/json' -d '{"pageURL":"eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing", "lastN":10}' 'http://localhost:8080/delightalk/rest/ejBlog/getRecentComments'
curl -i -X PUT -H 'Accept: application/json' -H 'Content-Type: application/json' -d '{"pageURL":"eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing", "lastN":10}' 'https://eastmanjian.cn/delightalk/rest/ejBlog/getRecentComments'
```
- add a comment
```
curl -X POST -H 'Content-Type: application/json' -d '{"pageURL":"...", "user":"userName", "comment":"the content of the comment"}' 'https://eastmanjian.cn/delightalk/{siteName}/addComment/'
e.g.
curl -i -X POST -H 'Content-Type: application/json' -d '{"pageURL":"eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing", "user":"userName", "comment":"Thank you. ありがとうございました. спасибо. 谢谢. ขอบคุณครับ. "}' 'http://localhost:8080/delightalk/rest/ejBlog/addComment/'
curl -i -X POST -H 'Content-Type: application/json' -d '{"pageURL":"eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing", "user":"userName", "comment":"Hello. こんにちは. привет. 你好. สวัสดี. "}' 'https://eastmanjian.cn/delightalk/rest/ejBlog/addComment/'
```


Front-end Javascript Embedding
------------------------------
### Configuration
- site name
- how many historical comments for a page should be displayed (optional)


Management Console
------------------
(Todo)

Concurrency
-----------