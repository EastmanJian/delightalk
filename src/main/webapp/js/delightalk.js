/**
 *  MIT License
 *  Copyright (c) 2017 Eastman Jian
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */


/**
 * Initiate Delightalk parameters.
 */
delightParams.pageUrlId = location.hostname + location.pathname;

/**
 * Initiate Delightalk section.
 */
(function () {
    renderDelightalkSkeleton();
    getDelightalkRecentComments();
})();

/**
 * wrapper function for AJAX REST request.
 * @param params - The parameter object contains REST request parameters.
 *                  Including: type (method), url, contentType, accept, data.
 * @returns {Promise} - The promise object produced by an AJAX call.
 */
function ajaxRestReq(params) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.onload = () => {
            if (xhr.status == 200) {
                resolve(xhr.responseText);
            } else {
                reject("Server Error: " + xhr.status);
            }
        }
        xhr.onerror = () => {
            reject("XMLHttpRequest Error:"  + xhr.status);
        }
        xhr.open(params.type, params.url, true);
        xhr.setRequestHeader("Content-type", params.contentType);
        xhr.setRequestHeader("Accept", params.accept);
        xhr.send(params.data);
    });
}

/**
 * Add a comment and store the data to the REST service.
 */
function addDelightalkComment() {
    let user = document.querySelector("#delightalkUserName").value;
    let userName = (user == "") ? "Anonymous" : user;
    let commentContent = document.querySelector("#delightalkCommentInput").value;
    if (commentContent == "") {
        document.querySelector("#delightalkCommentInput").focus();
        return;
    }
    let btnAddComment = document.querySelector("#addDelightalkCommentBtn")
    btnAddComment.innerText = "Publishing";
    btnAddComment.disabled = true;
    let commentItem = {
        pageURL: delightParams.pageUrlId,
        user: userName,
        comment: commentContent
    };
    ajaxRestReq({
        //url: 'http://localhost:8080/delightalk/rest/' + delightParams.siteName + '/addComment/',  //local test
        url: 'https://eastmanjian.cn/delightalk/rest/' + delightParams.siteName + '/addComment/',    //remote server test
        type: 'POST',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(commentItem),
        accept: 'application/json',
    }).then(doneAddDelightalkComment, logErr);
}

/**
 * Callback function after a new comment is added successfully.
 */
function doneAddDelightalkComment() {
    let btnAddComment = document.querySelector("#addDelightalkCommentBtn")
    btnAddComment.innerText = "Published";
    document.querySelector("#delightalkCommentInput").value = "";
    setTimeout(function () {
        btnAddComment.innerText = " Publish ";
        btnAddComment.disabled = false;
    }, 1500);
    getDelightalkRecentComments();
}

/**
 * Callback function in case ajax request error.
 * @param reason - the error reason
 */
function logErr(reason) {
    console.log(reason);
}

/**
 * Get recent comments from REST service
 */
function getDelightalkRecentComments() {
    let requestData = {
        pageURL: delightParams.pageUrlId,
        lastN: delightParams.previousComments
    };
    ajaxRestReq({
        //url: 'http://localhost:8080/delightalk/rest/' + delightParams.siteName + '/getRecentComments',  //local server test
        url: 'https://eastmanjian.cn/delightalk/rest/' + delightParams.siteName + '/getRecentComments',  //remote server test
        type: 'PUT',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(requestData),
        accept: 'application/json',
    }).then(renderRecentComments, logErr);
}

/**
 * Render the REST returned json string into the recent comment section of the page.
 * @param jsonString the json format string contains the recent comment data.
 */
function renderRecentComments(jsonString) {
    let data = JSON.parse(jsonString);
    let htmlTxt = '';
    for (i = data.recentComments.length - 1; i >= 0; i--) {
        htmlTxt += '<div class="a-delightalk-comment">';
        htmlTxt +=   '<header id="recentCmtHeader">';
        htmlTxt +=     '<span id="delightalkCommentUser">';
        htmlTxt +=       '<i class="fa fa-user comment-icon" aria-hidden="true"></i>';
        htmlTxt +=       escapeHtml(data.recentComments[i].user + ' ');
        htmlTxt +=     '</span>';
        htmlTxt +=     '<span id="delightalkCommentDate">';
        htmlTxt +=       '<i class="fa fa-clock-o comment-icon" aria-hidden="true"></i>';
        htmlTxt +=       (new Date(data.recentComments[i].timestamp)).Format("yyyy-MM-dd hh:mm:ss");
        htmlTxt +=     '</span>';
        htmlTxt +=   '</header>';
        htmlTxt +=   '<article  id="delightalkCommentContent">';
        htmlTxt +=     escapeHtml(data.recentComments[i].comment);
        htmlTxt +=   '</article>';
        htmlTxt += '</div>';
    }
    document.querySelector("#recentDelightalkComments").innerHTML = htmlTxt;
}

/**
 * Data format function
 * Extend Date object to provide a format method
 *
 * e.g.
 * (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 * (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
 */
Date.prototype.Format = function (fmt) {
    let o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};


/**
 * convert normal string into HTML escaped string
 * @param html - The normal string
 * @returns {string} - The HTML escaped string
 */
function escapeHtml(html)
{
    var text = document.createTextNode(html);
    var div = document.createElement('div');
    div.appendChild(text);
    return div.innerHTML;
}


/**
 * Initial render of the skeleton of Deligtalk comment sections
 */
function renderDelightalkSkeleton() {
    let htmlTxt = '';
    htmlTxt += '<section id="addDelightalkComment">';
    htmlTxt +=   '<textarea id="delightalkCommentInput" class="shadow-focus" placeholder="I like this page. I delight in talking about this."></textarea>';
    htmlTxt +=   '<footer id="addDelightalkCmtFooter">';
    htmlTxt +=     '<button id="addDelightalkCommentBtn" class="shadow-focus float-right" onclick="addDelightalkComment()"> Publish </button>';
    htmlTxt +=     '<input id="delightalkUserName" class="shadow-focus float-right" type="text" name="user" placeholder="Anonymous"/>';
    htmlTxt +=     '<i class="fa fa-user comment-icon float-right" aria-hidden="true"></i>';
    htmlTxt +=   '</footer>';
    htmlTxt += '</section>';
    htmlTxt += '<hr id="CmtInputDispSplit"/>';
    htmlTxt += '<section id="recentDelightalkComments">';
    htmlTxt += '</section>';
    document.querySelector("#delightalk").innerHTML = htmlTxt;
}
