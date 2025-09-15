<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .container { max-width: 500px; margin: 30px auto; padding: 20px; background: #fff; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #007bff; margin-bottom: 20px; }
        .form-label { font-weight: bold; }
        .form-control { margin-bottom: 15px; }
        .btn-primary { background-color: #007bff; border-color: #007bff; }
        .btn-primary:hover { background-color: #0056b3; }
        .back-link { display: inline-block; margin-top: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .back-link:hover { text-decoration: underline; }
        img { display: block; margin: 10px auto; border: 1px solid #ccc; border-radius: 8px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Update Profile</h2>
    <c:if test="${not empty error}">
        <p class="text-danger text-center">${error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/<c:choose><c:when test="${sessionScope.user.roleid == 1}">user</c:when><c:when test="${sessionScope.user.roleid == 2}">manager</c:when><c:otherwise>admin</c:otherwise></c:choose>/profile" method="post" enctype="multipart/form-data">
        <div class="mb-3">
            <label class="form-label">Full Name:</label>
            <input type="text" class="form-control" name="fullname" value="${sessionScope.user.fullname}" required />
        </div>
        <div class="mb-3">
            <label class="form-label">Phone:</label>
            <input type="text" class="form-control" name="phone" value="${sessionScope.user.phone}" />
        </div>
        <div class="mb-3">
            <label class="form-label">Current Image:</label>
            <img src="${pageContext.request.contextPath}/Uploads/${sessionScope.user.image}" alt="Profile Image" width="100" onerror="this.src='${pageContext.request.contextPath}/Uploads/default-user.png'"/>
        </div>
        <div class="mb-3">
            <label class="form-label">Upload New Image:</label>
            <input type="file" class="form-control" name="image" accept="image/*" />
        </div>
        <button type="submit" class="btn btn-primary w-100">Update Profile</button>
    </form>
    <a href="${pageContext.request.contextPath}/<c:choose><c:when test="${sessionScope.user.roleid == 1}">user</c:when><c:when test="${sessionScope.user.roleid == 2}">manager</c:when><c:otherwise>admin</c:otherwise></c:choose>/home" class="back-link">Back to Home</a>
</div>
</body>
</html>