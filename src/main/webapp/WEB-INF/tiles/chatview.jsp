<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-md-12">
        <div class="card card-default">

            <div class="card-header">
                <div class="panel-title">Chatting with "${chatwithUsername}"</div>
            </div>

            <div class="card-body">
                <div id="chat-message-view">

                    <div id="chat-message-previous">
                        <a href="#">View older messages</a>
                    </div>

                    <div id="chat-message-record">
                        Logged in as '${uid}', chatting with '${chatwithUID}'
                    </div>

                    <div id="chat-message-send" class="input-group input-group-lg">
                        <textarea class="form-control" id="chat-message-text" placeholder="Enter a message"></textarea>
                        <span class="input-group-btn">
                            <button id="chat-send-button" type="button" class="btn btn-primary float-end">Send</button>
                        </span>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>
