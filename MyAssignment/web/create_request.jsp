<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:main_layout title="Tạo đơn mới">

    <div class="row justify-content-center">
        <div class="col-lg-8">
            
            <div class="card shadow-sm">
                <div class="card-header">
                    <h2 class="mb-0">Tạo đơn xin nghỉ phép mới</h2>
                </div>
                <div class="card-body">
                    
                    <div class="alert alert-secondary">
                        <p class="mb-1"><strong>Người tạo:</strong> ${sessionScope.user.fullName}</p>
                        <p class="mb-0"><strong>Phòng ban:</strong> ${sessionScope.user.division.divisionName}</p>
                    </div>

                    <form action="${pageContext.request.contextPath}/request/create" method="POST">
                        <div class="mb-3">
                            <label for="title" class="form-label">Tiêu đề:</label>
                            <input type="text" class="form-control" id="title" name="title" required>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="fromDate" class="form-label">Từ ngày:</label>
                                <input type="date" class="form-control" id="fromDate" name="fromDate" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="toDate" class="form-label">Tới ngày:</label>
                                <input type="date" class="form-control" id="toDate" name="toDate" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="reason" class="form-label">Lý do:</label>
                            <textarea class="form-control" id="reason" name="reason" rows="5" required></textarea>
                        </div>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">Gửi đơn</button>
                        </div>
                    </form>
                    
                </div> </div> </div> </div> </layout:main_layout>