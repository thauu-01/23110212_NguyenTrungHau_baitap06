<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xem Video - ${video.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 800px;
            margin: 30px auto;
            padding: 20px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #007bff;
            margin-bottom: 20px;
        }
        .video-player {
            width: 100%;
            max-height: 400px;
            margin-bottom: 20px;
            border-radius: 8px;
        }
        .info-label {
            font-weight: bold;
            color: #333;
        }
        .info-value {
            color: #555;
            margin-bottom: 15px;
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
        .text-danger {
            text-align: center;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Xem Video: ${video.title}</h2>
    
    <c:if test="${not empty error}">
        <p class="text-danger">${error}</p>
    </c:if>
    
    <c:choose>
        <c:when test="${video != null}">
            <!-- Trình phát video -->
            <video class="video-player" controls>
                <source src="${pageContext.request.contextPath}/uploads/${video.videoFile}" type="video/mp4">
                Trình duyệt của bạn không hỗ trợ thẻ video.
            </video>
            
            <!-- Thông tin video -->
            <div class="info-section">
                <div class="row">
                    <div class="col-md-6">
                        <p><span class="info-label">Tiêu đề:</span></p>
                        <p class="info-value">${video.title}</p>
                    </div>
                    <div class="col-md-6">
                        <p><span class="info-label">Danh mục:</span></p>
                        <p class="info-value">${video.category != null ? video.category.cateName : 'N/A'}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <p><span class="info-label">Người tải lên:</span></p>
                        <p class="info-value">${video.user != null ? video.user.username : 'N/A'}</p>
                    </div>
                    <div class="col-md-6">
                        <p><span class="info-label">Ngày tải lên:</span></p>
                        <p class="info-value">${video.uploadDate}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <p><span class="info-label">Mô tả:</span></p>
                        <p class="info-value">${video.description != null ? video.description : 'Không có mô tả'}</p>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <p class="text-danger">Không tìm thấy video!</p>
        </c:otherwise>
    </c:choose>
    
    <a href="${pageContext.request.contextPath}/admin/home" class="back-link">Quay về trang chủ</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>