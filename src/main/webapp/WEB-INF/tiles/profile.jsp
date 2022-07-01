<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="editProfile" value="/editprofile"/>
<%-- Access "userID" param from the ModelAndView Controller along with the profileimage GETrequest. --%>
<c:url var="profileImage" value="/profileimage/${userID}"/>

<c:url var="saveInterest" value="/save-interest"/>
<c:url var="deleteInterest" value="/delete-interest"/>

<div class="row">

    <div class="col-md-10 offset-md-1">

        <div id="profile-img-status"></div>

        <div id="interestDiv">
            <ul>
                <c:forEach var="interest" items="${profile.interests}">
                    <li>${interest.name}</li>
                </c:forEach>
            </ul>
            <ul id="interestList">
                <c:choose>
                    <c:when test="${empty profile.interests}">
                        <li>Random Tags go here!</li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="interest" items="${profile.interests}">
                            <li>${interest.name}</li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>

        <div class="profile-about">
            <div class="profile-img">
                <div> <img id="profileImg" src="${profileImage}"/> </div>
                <c:if test="${ownProfile == true}">
                    <div class="text-center"> <a href="#" id="uploadLink">Upload Image</a> </div>
                </c:if>
            </div>

            <div class="profile-txt">
                <c:choose>
                    <c:when test="${profile.about == null}">
                        Click 'Edit' to add information about yourself.
                    </c:when>
                    <c:otherwise>
                        ${profile.about}
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="profile-about-edit">
            <c:if test="${ownProfile == true}">
                <a href="${editProfile}">Edit</a>
            </c:if>
        </div>

        <div>

            <c:url value="/upload-profile-photo" var="uploadPhotoLink"/>
            <%-- Specify encoding type so Data is not changed when uploading! --%>
            <form method="post" enctype="multipart/form-data" action="${uploadPhotoLink}" id="imageUploadForm">
                <input type="file" accept="image/*" name="file" id="imageFileInput"/>
                <input type="submit" value="Upload"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>

        </div>
    </div>

</div>

<script language="javascript">

function setUploadStatusText(text) {
    $('#profile-img-status').text(text);
    <%-- Invoke after timeout: --%>
    window.setTimeout(function() {
        $('#profile-img-status').text("");
    }, 2000);
}

function reloadImage(data) {
    $('#profileImg').attr("src", "${profileImage}?t=" + new Date().getMilliseconds()); <%-- change url to reload content, avoid caching --%>
    <%-- blank out name of imageFile to allow reloading image with the same name: --%>
    $('#imageFileInput').val("");

    setUploadStatusText(data.message);
}

function uploadImage(event) {
    $.ajax({
        url: $(this).attr('action'), <%-- this: gets reference to the JQuery Form Object .attr gets attribute--%>
        type: 'POST',
        data: new FormData(this),
        processData: false, <%-- dont encode --%>
        contentType: false,
        success: reloadImage, <%-- invoke method when successful --%>
        error: function() {
            alert('ERROR');
            setUploadStatusText("Image upload failed.");
        }
    });
    event.preventDefault();
}

<%-- For Adding/Removing Tags: --%>
function saveInterest(text) {
    editInterest(text, "${saveInterest}");
}
function deleteInterest(text) {
    editInterest(text, "${deleteInterest}");
}

<%-- Get Meta tags of CSRF-Token, get content Attribute & set content into Request-Header when doing a Post, save to DB--%>
function editInterest(text, actionUrl) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    alert(token + ": " + header);
    $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
        jqXHR.setRequestHeader(header, token);
    });

    $.ajax({
        'url': actionUrl,
        data: { 'name': text },
        type: 'POST',
        success: function() {alert("Saved" + text);},
        error: function() {alert("Failed To Save" + text);}
    });
}

function prePop() {
    console.log("${profile.interests}");
    console.log($("#profile.interests"));
    $('#profile.interests').each( console.log() );
    return {name: "Java", 'readonly': '${ownProfile}' == 'false'};
}

<%-- .ready only runs when the page is fully loaded: --%>
    $(document).ready(function() {
        <%-- Invoke Tagging JS --%>
    $("#interestList").tokenInput([
          {id: 7, name: "Ruby", 'readonly': '${ownProfile}' == 'false', },
          {id: 11, name: "Python", 'readonly': '${ownProfile}' == 'false',},
          {id: 13, name: "JavaScript"},
          {id: 17, name: "ActionScript"},
          {id: 19, name: "Scheme"},
          {id: 23, name: "Lisp"},
          {id: 29, name: "C#"},
          {id: 31, name: "Fortran"},
          {id: 37, name: "Visual Basic"},
          {id: 41, name: "C"},
          {id: 43, name: "C++"},
          {id: 47, name: "Java"}
        ], {
        prePopulate:[prePop()],
        tokenLimit: 10, preventDuplicates: true,
        hintText: "Insert interests here...", searchingText: "Searching interests...",
        onDelete: function(item) {deleteInterest(item.name)},
        onAdd: function(item) {saveInterest(item.name)}
    });

    <%-- syntax to refer to an Element on page: invoke click function--%>
    $("#uploadLink").click(function(event) {
        event.preventDefault(); <%-- prevent Default action of the link when clicking it.--%>
        $("#imageFileInput").trigger('click');
    });

    $("#imageFileInput").change(function() {
        $("#imageUploadForm").submit();
    });

    $("#imageUploadForm").on("submit", uploadImage);
});
</script>