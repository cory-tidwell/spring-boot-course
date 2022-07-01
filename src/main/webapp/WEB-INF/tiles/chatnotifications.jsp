<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<sec:authorize access="isAuthenticated">

    <c:url var="webSocketEndpoint" value="/chat" />
    <c:url var="inboundDestination" value="/user/queue/${uid}" />

    <script>

        var csrfTokenName = $("meta[name='_csrf_header']").attr("content");
        var csrfTokenValue = $("meta[name='_csrf']").attr("content");

        var headers = [];
        headers[csrfTokenName] = csrfTokenValue;

        var wsocket = new SockJS('${webSocketEndpoint}');
        var client = Stomp.over(wsocket);

        client.connect(headers, function() {
            console.log("Connection has been established.");
            client.subscribe("${inboundDestination}", function(messageJson) {
                var message = Json.parse(messageJson.body);
                console.log(message.text);
            });
        });
    </script>
</sec:authorize>