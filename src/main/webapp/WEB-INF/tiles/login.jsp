<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="loginUrl" value="/login"/>
<c:url var="registerUrl" value="/register"/>

<div class="row">
    <div class="col-md-6 offset-md-3 col-sm-8 offset-sm-2 register-prompt">
        Please log in or <a href="${registerUrl}">click here to create an account.</a>
    </div>
</div>

<div class="row">

    <div class="col-md-6 offset-md-3 col-sm-8 offset-sm-2">

        <c:if test="${param.error != null}">
            <div class="login-error">Incorrect Username or Password.</div>
        </c:if>

        <div class="card card-default">

            <div class="card-header">
                <div class="panel-title">User Login</div>
            </div>

                <div class="card-body">
                    <form method="post" action="${loginUrl}" class="login-form">

                    <%-- Hidden Login form with the CSRF Token to submit: --%>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                        <div class="input-group">
                            <input type="text" name="username" placeholder="Username" class="form-control"/>
                        </div>
                        <div class="input-group">
                            <input type="password" name="password" placeholder="Password" class="form-control"/>
                        </div>
                        <div>
                            <button type="submit" class="btn btn-primary float-end">Sign In</button>
                        </div>
                    </form>
                </div>

        </div>

    </div>

</div>