package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Request {
    private int requestID;
    private String title;
    private String reason;
    private Date fromDate;
    private Date toDate;
    private int status; // 1: Inprogress, 2: Approved, 3: Rejected
    private Timestamp createdDate;
    private Timestamp processedDate;
    private String processedComment;

    // Các đối tượng liên quan
    private User createdBy;   // Người tạo đơn
    private User processedBy; // Người duyệt đơn

    public Request() {
    }

    // Getters and Setters
    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Timestamp processedDate) {
        this.processedDate = processedDate;
    }

    public String getProcessedComment() {
        return processedComment;
    }

    public void setProcessedComment(String processedComment) {
        this.processedComment = processedComment;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }
    
    // (Optional) Hàm tiện ích lấy tên trạng thái
    public String getStatusText() {
        switch (status) {
            case 1: return "Inprogress";
            case 2: return "Approved";
            case 3: return "Rejected";
            default: return "Unknown";
        }
    }
}