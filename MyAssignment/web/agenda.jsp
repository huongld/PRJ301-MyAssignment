
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Agenda Phòng Ban</title>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        .on-leave { background-color: #ffcccc; } /* Bôi đỏ */
        .working { background-color: #ccffcc; } /* Bôi xanh */
    </style>
</head>
<body>
    <jsp:include page="header.jsp" /> 

    <div class="container">
        <h2>Agenda Tình hình Lao động</h2>
        <p>Phòng: <strong>${sessionScope.user.division.divisionName}</strong></p>
        <p>
            Từ ngày: <fmt:formatDate value="${fromDate}" pattern="dd/MM/yyyy"/> 
            Tới ngày: <fmt:formatDate value="${toDate}" pattern="dd/MM/yyyy"/>
        </p>

        <table>
            <thead>
                <tr>
                    <th>Nhân sự</th>
                    <th><fmt:formatDate value="${fromDate}" pattern="dd/MM"/></th>
                    <th>... (các ngày khác) ...</th>
                    <th><fmt:formatDate value="${toDate}" pattern="dd/MM"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="staff" items="${staffList}">
                    <tr>
                        <td>${staff.fullName}</td>
                        
                        <td class="working">Đi làm</td>
                        <td>...</td>
                        <td class="on-leave">Nghỉ phép</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>