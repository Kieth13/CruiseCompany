package service;

import dao.ConnectionFactory;
import java.sql.*;


public class Validate {
    private static final String SQL_FIND_USER_BY_EMAIL_AND_PASSWORD = "Select * from users where email = ? AND password = ?";
    private static final String SQL_FIND_USER_BY_EMAIL = "Select * from users where email=?";

    public boolean checkUser(String email, String password) {
        boolean isExist = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL_AND_PASSWORD);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                isExist = true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, pstmt, con);
        }
        return isExist;
    }

    public boolean checkEmail(String email) {
        boolean status = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                status = true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, pstmt, con);
        }
        return status;
    }

    private void close(ResultSet rs, Statement stmt, Connection con) {
        close(rs);
        close(stmt);
        close(con);
    }

    private void close(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Cannot close a connection");
        }
    }

    private void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            System.out.println("Cannot close a statement");
        }
    }

    private void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            System.out.println("Cannot close a result set");
        }
    }
}
