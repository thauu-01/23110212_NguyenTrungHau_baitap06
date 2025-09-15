<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập</title>
    <!-- Bootstrap 4.5.2 CDN -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 5.15.4 CDN -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        .login-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .form-group label {
            font-weight: bold;
        }
        .btn-primary {
            width: 100%;
            background-color: #007bff;
            border-color: #007bff;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
        .form-control {
            background-color: #e9ecef;
            border: none;
        }
        .form-check-label {
            margin-left: 5px;
        }
        .login-container h2 {
            font-size: 1.5rem;
            text-align: center;
        }
        .error {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2 class="text-center mb-4">Đăng Nhập Vào Hệ Thống</h2>
        
        <!-- Form login, action luôn trỏ về servlet /login -->
        <form action="${pageContext.request.contextPath}/login" method="post">
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            
            <div class="form-group">
                <label for="username"><i class="fas fa-user"></i> Tài khoản</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="Tài khoản" required>
            </div>
            
            <div class="form-group">
                <label for="password"><i class="fas fa-lock"></i> Mật khẩu</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu" required>
            </div>
            
            <div class="form-check mb-3">
                <input type="checkbox" class="form-check-input" name="remember" id="remember">
                <label class="form-check-label" for="remember">Nhớ mật khẩu</label>
            </div>
            
            <button type="submit" class="btn btn-primary">Đăng nhập</button>
        </form>
    </div>
</body>
</html>
