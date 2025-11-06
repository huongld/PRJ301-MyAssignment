<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Tạo đơn xin nghỉ phép</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <jsp:include page="header.jsp" /> <%-- (Tùy chọn: Tạo 1 file header.jsp chung) --%>

    <div class="container">
        <h2>Tạo đơn xin nghỉ phép</h2>
        <form action="request/create" method="POST">
            <p>
                Người tạo: <strong>${sessionScope.user.fullName}</strong> <br/>
                Phòng ban: <strong>${sessionScope.user.division.divisionName}</strong>
            </p>
            <div>
                <label>Tiêu đề:</label>
                <input type="text" name="title" required>
            </div>
            <div>
                <label>Từ ngày:</label>
                <input type="date" name="fromDate" required>
            </div>
            <div>
                <label>Tới ngày:</label>
                <input type="date" name="toDate" required>
            </div>
            <div>
                <label>Lý do:</label>
                <textarea name="reason" rows="5" required></textarea>
            </div>
            
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
            
            <button type="submit">Gửi đơn</button>
        </form>
    </div>
</body>
</html>