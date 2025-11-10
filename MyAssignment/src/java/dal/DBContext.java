package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp này quản lý kết nối CSDL.
 * Tất cả các lớp DAO khác sẽ kế thừa (extends) từ lớp này
 * để sử dụng chung biến 'connection'.
 */
public class DBContext {
    protected Connection connection; // Biến này sẽ được các DAO con sử dụng
    
    // === DI CHUYỂN THÔNG TIN CSDL RA ĐÂY ===
    // Khai báo là 'static' để hàm main có thể truy cập được
    private static final String host = "LAPTOP-EBK847C3";
    private static final String port = "1433";
    private static final String dbName = "FALL25_Assignment"; 
    private static final String user = "sa";
    private static final String pass = "123";

    public DBContext() {
        try {
            // Sử dụng các biến static đã khai báo ở trên
            String url = "jdbc:sqlserver://" + host + ":" + port + ";"
                       + "databaseName=" + dbName + ";"
                       + "encrypt=false;";
            
            // Nạp driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Tạo kết nối
            connection = DriverManager.getConnection(url, user, pass);
            
        } catch (ClassNotFoundException | SQLException ex) {
            // Ghi log lỗi nếu có sự cố
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Hàm main để kiểm tra kết nối CSDL có thành công không.
     * Bạn có thể bấm chuột phải vào file này và chọn "Run File"
     */
    public static void main(String[] args) {
        try {
            DBContext db = new DBContext();
            if (db.connection != null) {
                // Giờ 'dbName' đã có thể được truy cập vì nó là 'static'
                System.out.println("Chúc mừng! Kết nối CSDL '" + dbName + "' thành công!");
            } else {
                System.out.println("Kết nối CSDL thất bại. Vui lòng kiểm tra lại thông tin.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}