<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết Danh Mục</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 30px auto;
            padding: 20px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1, h2 {
            text-align: center;
            color: #007bff;
            margin-bottom: 20px;
        }
        .detail p {
            font-size: 16px;
            margin: 8px 0;
        }
        .detail img {
            display: block;
            margin: 10px auto;
            width: 80px;
            height: 80px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }
        form {
            margin-top: 20px;
        }
        label {
            font-weight: bold;
        }
        input[type=text], input[type=file] {
            width: 100%;
            padding: 8px;
            margin: 5px 0 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type=submit] {
            background: #007bff;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type=submit]:hover {
            background: #0056b3;
        }
        .back-link {
            display: inline-block;
            margin-top: 15px;
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .video-list {
            margin-top: 20px;
        }
        .video-list ul {
            list-style: none;
            padding: 0;
        }
        .video-list li {
            margin: 10px 0;
        }
        .video-list a {
            color: #007bff;
            text-decoration: none;
        }
        .video-list a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Chi tiết Danh Mục</h1>

    <!-- Hiển thị thông tin danh mục -->
    <div class="detail">
        <p><strong>ID:</strong> ${category.cateId}</p>
        <p><strong>Tên danh mục:</strong> ${category.cateName}</p>
        <p><strong>Icon:</strong></p>
        <img src="${pageContext.request.contextPath}/Uploads/${category.icons}" 
             alt="Icon"
             onerror="this.src='${pageContext.request.contextPath}/Uploads/default-icon.png'">
        <p><strong>Người tạo:</strong> ${category.user.username}</p>
    </div>

    <!-- Hiển thị danh sách video thuộc danh mục -->
    <h2>Video trong Danh Mục</h2>
    <div class="video-list">
        <c:choose>
            <c:when test="${empty category.videos}">
                <p>Chưa có video nào trong danh mục này.</p>
            </c:when>
            <c:otherwise>
                <ul>
                    <c:forEach var="video" items="${category.videos}">
                        <li>
                            ${video.title}
                            <a href="${pageContext.request.contextPath}/video/view?video_id=${video.videoId}">Xem</a>
                            <a href="${pageContext.request.contextPath}/video/delete?video_id=${video.videoId}">Xóa</a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Form cập nhật danh mục -->
    <h2>Cập nhật Danh Mục</h2>
    <form method="post" action="${pageContext.request.contextPath}/category/update" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${category.cateId}" />
        
        <label for="cateName">Tên danh mục:</label>
        <input type="text" id="cateName" name="cateName" value="${category.cateName}" required />

        <label for="icons">Icon (chọn file):</label>
        <input type="file" id="icons" name="icons" accept="image/*" />

        <input type="submit" value="Cập nhật" />
    </form>

    <!-- Nút quay về danh sách -->
    <c:choose>
        <c:when test="${sessionScope.user.roleid == 1}">
            <a class="back-link" href="${pageContext.request.contextPath}/user/home">← Quay về danh sách</a>
        </c:when>
        <c:when test="${sessionScope.user.roleid == 2}">
            <a class="back-link" href="${pageContext.request.contextPath}/manager/home">← Quay về danh sách</a>
        </c:when>
        <c:otherwise>
            <a class="back-link" href="${pageContext.request.contextPath}/admin/home">← Quay về danh sách</a>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>