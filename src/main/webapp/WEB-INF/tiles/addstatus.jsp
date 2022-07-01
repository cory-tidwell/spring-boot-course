<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- jstl Format! Tag -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="row">

    <div class="col-md-8 offset-md-2">

        <div class="card card-default">

            <div class="card-header">
                <div class="card-title">Add Status Update</div>
            </div>

            <!-- form-tag + specify where Form can find the Objects that it can save Data into: -->

            <form:form modelAttribute="statusUpdate">
                <div class="errors">
                <form:errors path="text"/>
                </div>
                <div class="form-group">
                    <!-- path-attribute from set Method in StatusUpdate class (setText Method)-->
                    <form:textarea path="text" name ="text" rows="10" cols="50"></form:textarea>
                </div>
                <input type="submit" name="submit" value="Submit"/>
            </form:form>

        </div>

        <div class="card card-default">

            <div class="card-header">
                <div class="card-title"> Status update added on <fmt:formatDate pattern="EEEE d MMMM y 'at' H:mm:s" value="${latestStatusUpdate.added}"/></div>
            </div>

            <div class="card-body">
                ${latestStatusUpdate.text}
            </div>

        </div>

    </div>

</div>

<script
    type="text/javascript"
    src='https://cdn.tiny.cloud/1/no-api-key/tinymce/6/tinymce.min.js'
    referrerpolicy="origin">
  </script>
  <script type="text/javascript">
  tinymce.init({
    selector: 'textarea',
    plugins: [
      'advlist', 'autolink', 'link', 'image', 'lists', 'charmap', 'preview', 'anchor', 'pagebreak',
      'searchreplace', 'wordcount', 'visualblocks', 'visualchars', 'code', 'fullscreen', 'insertdatetime',
      'media', 'table', 'emoticons', 'template', 'help'
    ],
    toolbar: 'undo redo | styles | bold italic | alignleft aligncenter alignright alignjustify | ' +
      'bullist numlist outdent indent | link image | print preview media fullscreen | ' +
      'forecolor backcolor emoticons | help',
    menu: {
      favs: { title: 'My Favorites', items: 'code visualaid | searchreplace | emoticons' }
    },
    menubar: 'favs file edit view insert format tools table help',
    content_css: 'css/content.css'
  });
  </script>