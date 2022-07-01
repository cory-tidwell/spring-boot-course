<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="row">
    <div class="col-md-6 offset-md-3 col-sm-8 offset-sm-2 text-center">
        <div class="message">
            <c:out value="${message}"/>
        </div>

        <!--
        Exception: <c:out value="${exception}"/>
        Failed URL: <c:out value="${url}"/>
        <%-- Accessing Exception-class.getMessage() --%>
        Exception Message: <c:out value="${exception.message}"/>

        <c:forEach var="line" items="${exception.stackTrace}">
            <c:out value="${line}"/>
        </c:forEach>
        -->
    </div>
</div>