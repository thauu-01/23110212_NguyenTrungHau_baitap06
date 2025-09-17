<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .container { max-width: 500px; margin: 30px auto; padding: 20px; background: #fff; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #007bff; margin-bottom: 20px; }
        p { font-size: 16px; margin: 8px 0; }
        img { display: block; margin: 10px auto; border: 1px solid #ccc; border-radius: 8px; }
        .back-link { display: inline-block; margin-top: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
    <h2>Profile Details</h2>
    <p><strong>Username:</strong> ${sessionScope.user.username}</p>
    <p><strong>Full Name:</strong> ${sessionScope.user.fullname}</p>
    <p><strong>Phone:</strong> ${sessionScope.user.phone}</p>
    <p><strong>Role:</strong> <c:choose><c:when test="${sessionScope.user.roleid == 1}">User</c:when><c:when test="${sessionScope.user.roleid == 2}">Manager</c:when><c:otherwise>Admin</c:otherwise></c:choose></p>
    <p><strong>Image:</strong></p>
    <img src="${pageContext.request.contextPath}/Uploads/${sessionScope.user.image}" alt="Profile Image" width="200" onerror="this.src='${pageContext.request.contextPath}/Uploads/default-user.png'"/>
    <br/>
    <a href="${pageContext.request.contextPath}/<c:choose><c:when test="${sessionScope.user.roleid == 1}">user</c:when><c:when test="${sessionScope.user.roleid == 2}">manager</c:when><c:otherwise>admin</c:otherwise></c:choose>/profile" class="back-link">Edit Profile</a>
    <a href="${pageContext.request.contextPath}/<c:choose><c:when test="${sessionScope.user.roleid == 1}">user</c:when><c:when test="${sessionScope.user.roleid == 2}">manager</c:when><c:otherwise>admin</c:otherwise></c:choose>/home" class="back-link">Back to Home</a>
</div>
</body>
</html>