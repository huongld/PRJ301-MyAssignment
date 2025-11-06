<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Danh sách đơn</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" /> <%-- (Tùy chọn: Tạo 1 file header.jsp chung) --%>
    
    <div class="container">
        <h2>Danh sách đơn nghỉ phép</h2>
        <p>Xin chào, <strong>${sessionScope.user.fullName}</strong>! | <a href="logout">Đăng xuất</a></p>
        <p><a href="request/create">Tạo đơn mới</a></p>
        
        <table>
            <thead>
                <tr>
                    <th>Tiêu đề</th>
                    <th>Từ ngày</th>
                    <th>Tới ngày</th>
                    <th>Người tạo</th>
                    <th>Trạng thái</th>
                    <th>Người duyệt</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="req" items="${requestList}">
                    <tr>
                        <td>${req.title}</td>
                        <td><fmt:formatDate value="${req.fromDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${req.toDate}" pattern="dd/MM/yyyy"/></td>
                        <td>${req.createdBy.fullName}</td>
                        <td>
                            <span class="status-${req.statusText}">${req.statusText}</span>
                        </td>
                        <td>${req.processedBy.fullName}</td>
                        <td>
                            <c:if test="${req.createdBy.managerID == sessionScope.user.userID && req.status == 1}">
                                <a href="request/approve?id=${req.requestID}">Xem/Duyệt</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty requestList}">
                    <tr>
                        <td colspan="7">Không tìm thấy đơn nào.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>