<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tdir"%>

Showing results for: <strong>${profilesearch}</strong>

<c:url var="searchUrl" value="/search?profilesearch=${profilesearch}"/>
<div class="row">
    <div class="col-md-12">
        <tdir:pagination url="${searchUrl}" page="${page}" size="3"/>
    </div>
</div>

<div class="row">
    <div class="col-md-12 results-noresult">
        <c:if test="${empty page.content}">No results found.</c:if>
    </div>
</div>

<c:forEach var="result" items="${page.content}">
    <c:url var="profileImage" value="/profileimage/${result.userId}"/>
    <c:url var="profileLink" value="/profile/${result.userId}"/>

    <div class="row person-row">
        <div class="col-md-12">
                <div class="results-image">
                    <a href="${profileLink}"><img id="profileImg" src="${profileImage}"/></a>
                </div>

                <div class="results-details">
                    <div class="results-name">
                        <a href="${profileLink}"><c:out value="${result.firstname}"/>
                        <c:out value="${result.surname}"/></a>
                    <div>
                    <div class="results-interests">
                        <c:forEach var="interest" items="${result.interests}" varStatus="status">
                            <c:url var="interestLink" value="/search?profilesearch=${interest.name}"/>
                            <a href="${interestLink}"><c:out value="${interest.name}"/></a>
                            <c:if test="${!status.last}"> | </c:if>
                        </c:forEach>
                    </div>
                </div>
        </div>
    </div>
</c:forEach>