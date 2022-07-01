<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tdir"%>

<!-- add context root automatically to the url -->
<c:url var="url" value="/viewstatus"/>

<div class="row">

    <div class="col-md-8 offset-md-2">

        <!-- displaying numbers of pages; use getTotalPages() from API -->
        <!-- using own taglib(pagination) -->
        <tdir:pagination url="${url}" page="${page}" size="3"/>

        <!-- accessing getContent() (-content attribute) method from SpringAPI, returns List, iterate over results -->
        <c:forEach var="statusUpdate" items="${page.content}">

                <%-- Pass the id of the element via getID() method to the url --%>

                <c:url var="editLink" value="/editstatus?id=${statusUpdate.id}"/>
                <c:url var="deleteLink" value="/deletestatus?id=${statusUpdate.id}"/>

                <div class="card card-default">

                    <div class="card-header">
                        <div class="card-title"> Status update added on <fmt:formatDate pattern="EEEE d MMMM y 'at' H:mm:s" value="${statusUpdate.added}"/></div>
                    </div>

                    <div class="card-body">
                        <div> ${statusUpdate.text} </div>
                        <div class="edit-links float-end">
                            <a href="${editLink}">Edit</a> | <a onclick="return confirm('Do you want to delete the ${statusUpdate.id} Status Update?')" href="${deleteLink}">Delete</a>
                        </div>
                    </div>

                </div>

        </c:forEach>
    </div>
</div>