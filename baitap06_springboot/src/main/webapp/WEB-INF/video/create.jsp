<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Video Mới</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .container { max-width: 600px; margin: 30px auto; padding: 20px; background: #fff; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #007bff; margin-bottom: 20px; }
        .form-label { font-weight: bold; }
        .form-control { margin-bottom: 15px; }
        .btn-primary { background-color: #007bff; border-color: #007bff; }
        .btn-primary:hover { background-color: #0056b3; }
        .back-link { display: inline-block; margin-top: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .back-link:hover { text-decoration: underline; }
        .text-danger { text-align: center; margin-bottom: 15px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Thêm Video Mới</h2>
    <c:if test="${not empty error}">
        <p class="text-danger">${error}</p>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/video/create" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="title" class="form-label">Tiêu đề</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>
        <div class="mb-3">
            <label for="description" class="form-label">Mô tả</label>
            <textarea class="form-control" id="description" name="description"></textarea>
        </div>
        <div class="mb-3">
            <label for="videoFile" class="form-label">Video (chọn file)</label>
            <input type="file" class="form-control" id="videoFile" name="videoFile" accept="video/*" required>
        </div>
        <div class="mb-3">
            <label for="cateId" class="form-label">Danh mục</label>
            <select class="form-control" id="cateId" name="cateId" required>
                <option value="">Chọn danh mục</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.cateId}">${category.cateName}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary w-100">Thêm video</button>
    </form>
    <a href="${pageContext.request.contextPath}/admin/home" class="back-link">Quay về trang chủ</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>