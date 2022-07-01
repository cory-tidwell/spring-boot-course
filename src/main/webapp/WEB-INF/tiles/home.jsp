<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url var="search" value="/search"/>

<div class="row status-row">
    <div class="col-md-6 offset-md-3 col-sm-8 offset-sm-2">
        <div class="homepage-status">
            <!-- from StatusUpdate object getText() method invoked - returns String -->
            ${statusUpdate.text}
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-8 offset-md-2">

        <form method="post" action="${search}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="input-group input-group-lg">
                <input type="text" class="form-control" name="profilesearch" placeholder="Search Interests.">
                <span class="input-group-btn">
                    <button id="search-button" class="btn btn-primary" type="submit">Find</button>
                </span>
            </div>
        </form>

    </div>
</div>