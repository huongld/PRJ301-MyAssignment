<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:main_layout title="Danh sách đơn">

    <%-- Nội dung dưới đây sẽ được "bơm" vào <jsp:doBody/> của file layout --%>
    
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Danh sách đơn nghỉ phép</h2>
        
        <a href="${pageContext.request.contextPath}/request/create" class="btn btn-success">
            Tạo đơn mới
        </a>
    </div>

    <c:if test="${sessionScope.user.hasRole('Division Leader')}">
        <div class="alert alert-info">
            Bạn là Quản lý: 
            <a href="${pageContext.request.contextPath}/admin/list" class="alert-link">
                Đi đến trang Quản lý Nhân sự (Admin)
            </a>
        </div>
    </c:if>
    
    <table class="table table-striped table-hover table-bordered">
        <thead class="table-dark">
            <tr>
                <th>Tiêu đề</th>
                <th>Từ ngày</th>
                <th>Tới ngày</th>
                <th>Người tạo</th>
                <th>Trạng thái</th>
                <th>Người duyệt</th>
                <th>Lý do duyệt/từ chối</th>
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
                        <c:choose>
                            <c:when test="${req.status == 1}">
                                <span class="badge bg-warning text-dark">Inprogress</span>
                            </c:when>
                            <c:when test="${req.status == 2}">
                                <span class="badge bg-success">Approved</span>
                            </c:when>
                            <c:when test="${req.status == 3}">
                                <span class="badge bg-danger">Rejected</span>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>${req.processedBy.fullName}</td>
                    <td>${req.processedComment}</td>
                    <td>
                        <c:if test="${req.createdBy.managerID == sessionScope.user.userID && req.status == 1}">
                            <a href="${pageContext.request.contextPath}/request/approve?id=${req.requestID}" class="btn btn-primary btn-sm">
                                Xem/Duyệt
                            </a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestList}">
                <tr>
                    <td colspan="8" class="text-center">Không tìm thấy đơn nào.</td>
                </tr>
            </c:if>
        </tbody>
    </table>

</layout:main_layout>