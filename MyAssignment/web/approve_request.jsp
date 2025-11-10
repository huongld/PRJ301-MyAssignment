<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

<layout:main_layout title="Duyệt đơn">

    <div class="row justify-content-center">
        <div class="col-lg-8">
            <h2 class="mb-3">Duyệt đơn xin nghỉ phép</h2>
            <p>Duyệt bởi: <strong>${sessionScope.user.fullName}</strong></p>
            
            <form action="${pageContext.request.contextPath}/request/approve" method="POST">
                <input type="hidden" name="requestID" value="${requestDetail.requestID}">
                
                <div class="card shadow-sm">
                    <div class="card-header">
                        <h3 class="mb-0">Thông tin đơn</h3>
                    </div>
                    <div class="card-body">
                        <p><strong>Tạo bởi:</strong> ${requestDetail.createdBy.fullName}</p>
                        <p><strong>Từ ngày:</strong> <fmt:formatDate value="${requestDetail.fromDate}" pattern="dd/MM/yyyy"/></p>
                        <p><strong>Tới ngày:</strong> <fmt:formatDate value="${requestDetail.toDate}" pattern="dd/MM/yyyy"/></p>
                        <hr>
                        <p class="mb-1"><strong>Lý do:</strong></p>
                        <div class="alert alert-secondary">
                            ${requestDetail.reason}
                        </div>
                    </div>
                </div>
                
                <div class="card shadow-sm mt-4">
                    <div class="card-header">
                        <h3 class="mb-0">Xử lý đơn</h3>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="comment" class="form-label">Lý do duyệt/từ chối (tùy chọn):</label>
                            <textarea class="form-control" id="comment" name="comment" rows="3"></textarea>
                        </div>
                        
                        <div class="d-flex justify-content-end gap-2">
                            <button type="submit" name="action" value="reject" class="btn btn-danger btn-lg">
                                Reject (Từ chối)
                            </button>
                            <button type="submit" name="action" value="approve" class="btn btn-success btn-lg">
                                Approve (Đồng ý)
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    
</layout:main_layout>