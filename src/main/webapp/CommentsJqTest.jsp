<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script>
        function getDoubanTop10() {
            $.ajax({
                url: 'https://api.douban.com//v2/movie/top250',
                type: 'GET',
                data: '',
                dataType: 'jsonp',
                crossDomain: true,
                success: function (data) {
                    console.log(data);
                    $("#top10Films").text("Check Console log for the data,loaded.");

                }
            });
        }

        function getRecentComments() {
            $.ajax({
                url: 'http://localhost:8080/delightalk/rest/ejBlog/getRecentComments',
                type: 'PUT',
                contentType: 'application/json; charset=UTF-8',
                data: '{"pageURL":"eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing", "lastN":10}',
                dataType: 'json',
                crossDomain: true,
                success: function (data) {
                    console.log(data);
                    $("#recentComments").text(JSON.stringify(data));
                }
            });
        }

        function addComment() {
            let user = $("#userName").val();
            let userName = (user == "")? "Anonymous" : user;
            let commentItem = {
                pageURL: "eastmanjian.cn/blog/2017/05/07/using-markdown-for-web-writing",
                user: userName,
                comment: $("#comment").val()
            };
            $.ajax({
                url: 'http://localhost:8080/delightalk/rest/ejBlog/addComment/',
                type: 'POST',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify(commentItem),
                dataType: 'json',
                crossDomain: true,
                statusCode: {
                    200: function() {
                        console.log("comment added.");
                        $("#addCommentBtn").text("Your comment is published.").attr("disabled", true);
                        setTimeout(function (){
                            $("#addCommentBtn").text("Publish").attr("disabled", false);
                        }, 1500)
                    }
                }
            });
        }

        function testBtnClicked () {
            let url = window.location;
            $("#testResult").text(url);
        }
    </script>
</head>
<body>
<section id="readCommentTest">
    <button onclick="getDoubanTop10()">Get Douban Top10 Films</button><br/>
    <div id="top10Films">Top 10 films data</div>
    <hr/>
    <button onclick="getRecentComments()">Get Recent Comments</button><br/>
    <div id="recentComments">Recent Comments Data</div>
</section>
<hr/>
<section id="addCommentTest">
    Name: <input id="userName" type="text" name="user" placeholder="Anonymous"/><br/>
    Comment:
    <textarea id="comment" style="width: 300px; height: 200px">Beautiful. สวยงาม. 美丽的. 美しい. красивая</textarea>
    <br/>
    <button id="addCommentBtn" onclick="addComment()">Publish</button>
</section>
<hr/>
<section id="anyTest">
    <button onclick="testBtnClicked()">Test</button><br/>
    <div id="testResult">Test result data</div>
</section>
</body>
</html>
