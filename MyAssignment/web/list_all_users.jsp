<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:main_layout title="Quản lý Nhân sự">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Quản lý Nhân sự Toàn Công ty</h2>
        
        <a href="${pageContext.request.contextPath}/user/create" class="btn btn-success">
            <i class="bi bi-plus-circle"></i> Tạo tài khoản nhân viên mới
        </a>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-hover table-bordered align-middle">
            <thead class="table-dark">
                <tr>
                    <th>Họ và Tên</th>
                    <th>Username</th>
                    <th>Phòng ban</th>
                    <th>Vai trò (Chức vụ)</th>
                    <th>Quản lý trực tiếp (ManagerID)</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${userList}">
                    <tr>
                        <td>${u.fullName}</td>
                        <td>${u.username}</td>
                        <td>${u.division.divisionName}</td>
                        <td>
                            <c:forEach var="role" items="${u.roles}">
                                <c:set var="roleClass">
                                    <c:choose>
                                        <c:when test="${fn:contains(role.roleName, 'Leader')}">bg-danger</c:when>
                                        <c:when test="${fn:contains(role.roleName, 'Trưởng nhóm')}">bg-warning text-dark</c:when>
                                        <c:otherwise>bg-info text-dark</c:otherwise>
                                    </c:choose>
                                </c:set>
                                <span class="badge ${roleClass}">${role.roleName}</span><br>
                            </c:forEach>
                        </td>
                        <td>${u.managerID}</td>
                        <td>
                            <c:if test="${sessionScope.user.hasRole('Division Leader') && u.userID != sessionScope.user.userID}">
                                <a href="${pageContext.request.contextPath}/user/delete?id=${u.userID}" 
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa user ${u.fullName}?');"
                                   class="btn btn-danger btn-sm">
                                    Xóa User
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</layout:main_layout>