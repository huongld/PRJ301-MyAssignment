<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:main_layout title="Tạo tài khoản mới">

    <div class="row justify-content-center">
        <div class="col-lg-6">
            <div class="card shadow-sm">
                <div class="card-header">
                    <h2 class="mb-0">Tạo tài khoản nhân viên mới</h2>
                </div>
                <div class="card-body">
                    <p class="card-text">Phòng ban: <strong>${sessionScope.user.division.divisionName}</strong></p>
                    
                    <form action="${pageContext.request.contextPath}/user/create" method="POST">
                        <div class="mb-3">
                            <label for="username" class="form-label">Username:</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password:</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Họ và Tên:</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" required>
                        </div>
                        <div class="mb-3">
                            <label for="managerID" class="form-label">Quản lý trực tiếp (Manager):</label>
                            <select class="form-select" id="managerID" name="managerID" required>
                                <c:forEach var="manager" items="${managerList}">
                                    <option value="${manager.userID}">${manager.fullName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="roleID" class="form-label">Vai trò (Role):</label>
                            <select class="form-select" id="roleID" name="roleID" required>
                                <c:forEach var="role" items="${roleList}">
                                    <option value="${role.roleID}">${role.roleName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>
                        
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">Tạo tài khoản</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</layout:main_layout>