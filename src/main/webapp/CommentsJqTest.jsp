<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script>
        function getRecentComments() {
            $("#recentComments").text("data of comments in JSON");
        }
    </script>
</head>
<body>
<section id="readCommentTest">
    <button onclick="getRecentComments()">Get Recent Comments</button>
    <div id="recentComments">Recent Comments Data</div>
</section>

<section id="addCommentTest">

</section>
</body>
</html>
