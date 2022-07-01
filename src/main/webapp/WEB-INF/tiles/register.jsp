<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="loginUrl" value="/login"/>

<div class="row">

    <div class="col-md-6 offset-md-3 col-sm-8 offset-sm-2">

        <div class="errors">
            <form:errors path="user.*"/>
        </div>

        <div class="card card-default">

            <div class="card-header">
                <div class="panel-title">Create an Account</div>
            </div>

                <div class="card-body">
                    <form:form method="post" modelAttribute="user" class="login-form">

                        <div class="input-group">
                            <form:input type="text" path="firstname" placeholder="First Name" class="form-control"/>

                            <form:input type="text" path="surname" placeholder="Surname" class="form-control"/>
                        </div>

                        <div class="input-group">
                            <form:input type="text" path="email" placeholder="Email Address" class="form-control"/>
                        </div>

                        <div class="input-group">
                            <%-- Path submit to setPlainPassword() method--%>
                            <form:input type="password" path="plainPassword" placeholder="Password" class="form-control"/>
                        </div>

                        <div class="input-group">
                            <form:input type="password" path="repeatPassword" placeholder="Repeat Password" class="form-control"/>
                        </div>
                        <div>
                            <button type="submit" class="btn btn-primary float-end">Sign Up</button>
                        </div>
                    </form:form>
                </div>

        </div>

    </div>

</div>