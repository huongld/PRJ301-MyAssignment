<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:main_layout title="Agenda Phòng Ban">

    <h2>Agenda Tình hình Lao động</h2>
    <p>Phòng: <strong>${sessionScope.user.division.divisionName}</strong></p>
    <p>
        Từ ngày: <fmt:formatDate value="${fromDate}" pattern="dd/MM/yyyy"/> 
        Tới ngày: <fmt:formatDate value="${toDate}" pattern="dd/MM/yyyy"/>
    </p>

    <div class="table-responsive">
        <table class="table table-bordered text-center">
            <thead class="table-dark">
                <tr>
                    <th classC="text-start">Nhân sự</th>
                    
                    <th><fmt:formatDate value="${fromDate}" pattern="dd/MM"/></th>
                    <th>... (các ngày khác) ...</th>
                    <th><fmt:formatDate value="${toDate}" pattern="dd/MM"/></th>
                    
                    <c:if test="${sessionScope.user.hasRole('Division Leader')}">
                        <th>Hành động</th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="staff" items="${staffList}">
                    <tr>
                        <td class="text-start">${staff.fullName}</td>
                        
                        <td style="background-color: #ccffcc;">Đi làm</td>
                        <td>...</td>
                        <td style="background-color: #ffcccc;">Nghỉ phép</td>

                        <c:if test="${sessionScope.user.hasRole('Division Leader')}">
                            <td>
                                <c:if test="${staff.userID != sessionScope.user.userID}">
                                    <a href="${pageContext.request.contextPath}/user/delete?id=${staff.userID}" 
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa user ${staff.fullName}?');"
                                       class="btn btn-danger btn-sm">
                                        Xóa
                                    </a>
                                </c:if>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</layout:main_layout>