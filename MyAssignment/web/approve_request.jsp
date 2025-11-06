<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Duyệt đơn</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <jsp:include page="header.jsp" /> 

    <div class="container">
        <h2>Duyệt đơn xin nghỉ phép</h2>
        
        <form action="request/approve" method="POST">
            <input type="hidden" name="requestID" value="${requestDetail.requestID}">
            
            <p>Duyệt bởi: <strong>${sessionScope.user.fullName}</strong></p>
            <hr>
            <h3>Thông tin đơn</h3>
            <p>Tạo bởi: ${requestDetail.createdBy.fullName}</p>
            <p>Từ ngày: <fmt:formatDate value="${requestDetail.fromDate}" pattern="dd/MM/yyyy"/></p>
            <p>Tới ngày: <fmt:formatDate value="${requestDetail.toDate}" pattern="dd/MM/yyyy"/></p>
            <p>Lý do:</p>
            <div style="border: 1px solid #ccc; padding: 10px; background: #f9f9f9;">
                ${requestDetail.reason}
            </div>
            
            <hr>
            <h3>Xử lý đơn</h3>
            <div>
                <label>Lý do duyệt/từ chối (tùy chọn):</label>
                <textarea name="comment" rows="3"></textarea>
            </div>
            
            <button type_a="submit" name="action" value="approve" style="background-color: green;">Approve</button>
            <button type_a="submit" name="action" value="reject" style="background-color: red;">Reject</button>
        </form>
    </div>
</body>
</html>