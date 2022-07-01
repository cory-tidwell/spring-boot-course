<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

    <title><tiles:insertAttribute name="title" /></title>

    <c:set var="contextRoot" value="${pageContext.request.contextPath}" />

    <!-- Bootstrap: --!>
    <link href="${contextRoot}/css/bootstrap.css" rel="stylesheet"/>
    <link href="${contextRoot}/css/main.css" rel="stylesheet"/>

    <!-- For tagging - js in Profile --!>
    <script src="${contextRoot}/js/jquery.tokeninput.js"></script>
    <link href="${contextRoot}/css/token-input.css" rel="stylesheet" type="text/css" />

    <!-- For Chat implementation: --!>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <tiles:insertAttribute name="chatnotifications" />
    <tiles:insertAttribute name="chatviewscript" ignore="true" />

</head>
<body>

<h2 id="messages">Message here</h2>

<!-- Navbar -->
<nav class="navbar navbar-expand-md mb-4">
  <div class="container-fluid">
    <a class="navbar-brand" href="/">Spring Boot Course</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarCollapse">
      <ul class="navbar-nav me-auto mb-2 mb-md-0">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="${contextRoot}/">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${contextRoot}/about">About</a>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-right">

        <%-- Admin menu : only visible for Users with ROLE_ADMIN--%>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <div class="dropdown">
              <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false"> Status </button>
              <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                <li><a class="dropdown-item" href="${contextRoot}/addstatus">Add Status</a></li>
                <li><a class="dropdown-item" href="${contextRoot}/viewstatus">View Status Updates</a></li>
              </ul>
            </div>
        </sec:authorize>

        <%-- Only visible to authorized Users!--%>
        <sec:authorize access="isAuthenticated">
            <li><a class="nav-link" href="${contextRoot}/profile">Profile</a></li>
            <li><a class="nav-link" href="javascript:document.getElementById('logoutForm').submit()">Logout</a></li>
        </sec:authorize>

        <sec:authorize access="!isAuthenticated">
            <li><a class="nav-link" href="${contextRoot}/login">Login</a></li>
            <li><a class="nav-link" href="${contextRoot}/register">Register</a></li>
        </sec:authorize>
      </ul>
    </div>
  </div>
</nav>

<%-- Required to submit token along with logout!! --%>
<c:url var="logoutLink" value="/logout" />
<form id="logoutForm" method="post" action="${logoutLink}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<div class="container">
    <tiles:insertAttribute name="content" />
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.5/dist/umd/popper.min.js" integrity="sha384-Xe+8cL9oJa6tN/veChSP7q+mnSPaj5Bcu9mPX5F5xIGE0DVittaqT5lorf0EI7Vk" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.min.js" integrity="sha384-kjU+l4N0Yf4ZOJErLsIcvOU2qSb74wXpOhqTvwVx3OElZRweTnQ6d31fXEoRD1Jy" crossorigin="anonymous"></script>
<script src="${contextRoot}/js/bootstrap.js"</script>
</body>
</html>