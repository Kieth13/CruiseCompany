package dao.impl;

import dao.ConnectionFactory;
import dao.UserDao;
import dao.exception.DaoException;
import model.User;
import model.enums.Role;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public static UserDao instance;

    private static final Logger log = Logger.getLogger(UserDaoImpl.class);

    private static final String SQL_INSERT_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_USER_BY_ID = "Select * from users where id=?";
    private static final String SQL_FIND_USER_BY_EMAIL_AND_PASSWORD = "Select * from users where email = ? AND password = ?";
    private static final String SQL_FIND_USERS_BY_LIMIT_AND_OFFSET = "Select * from users ORDER BY ID LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_UPDATE_USER = "UPDATE users SET email=?, password=?, name=?, last_name=?, balance=? WHERE id=?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE id=?";
    private static final String SQL_COUNT = "Select COUNT(id) from users";

    private UserDaoImpl() {}

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public void create(User user) {
        log.info("Enter create with parameters: " + user);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_USER);
            int k = 1;
            pstmt.setString(k++, user.getEmail());
            pstmt.setString(k++, user.getPassword());
            pstmt.setString(k++, user.getRole().name());
            pstmt.setString(k++, user.getName());
            pstmt.setString(k++, user.getLastName());
            pstmt.setFloat(k, user.getBalance());
            if (pstmt.executeUpdate() > 0) {
                User newUser = this.getByEmailAndPassword(user.getEmail(), user.getPassword());
                user.setId(newUser.getId());
            }
        } catch (SQLException e) {
            log.error("Can't create User");
            throw new DaoException("Can't create User", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit create method");
    }

    @Override
    public User getById(int userId) {
        log.info("Enter getById method with parameters: userId=" + userId);
        User user = new User();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
        } catch (SQLException e) {
            log.error("Can't get User by Id");
            throw new DaoException("Cannot get User by Id", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getById method with parameters: " + user);
        return user;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) {
        log.info("Enter getByEmailAndPassword method with parameters: email=" + email + " password=" + password);
        User user = new User();
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
                user = extractUser(rs);
            }
        } catch (SQLException e) {
            log.error("Can't get User by Email and Password");
            throw new DaoException("Cannot get User by Email and Password", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getByEmailAndPassword method with parameters: " + user);
        return user;
    }

    @Override
    public List<User> getAllUsersByLimitAndOffset(int currentPage, int numOfRecords) {
        log.info("Enter getAllShipsByLimitAndOffset method with parameters: currentPage=" + currentPage + " numOfRecords=" +  numOfRecords);
        List<User> users = new ArrayList<>();
        int start = currentPage * numOfRecords - numOfRecords;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_USERS_BY_LIMIT_AND_OFFSET);
            pstmt.setInt(1, numOfRecords);
            pstmt.setInt(2, start);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException e) {
            log.error("Can't get Users by Limit and Offset");
            throw new DaoException("Can't get Users by Limit and Offset", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getAllShipsByLimitAndOffset method with parameters: " + users);
        return users;
    }

    @Override
    public List<User> findAll() {
        log.info("Enter findAll");
        List<User> users = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_USERS);
            while(rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException e) {
            log.error("Can't find all Users");
            throw new DaoException("Can't find all Users", e);
        } finally {
            close(rs, stmt, con);
        }
        log.info("Exit finaAll method with parameters: " + users);
        return users;
    }

    @Override
    public void update(User user) {
        log.info("Enter update with parameters: " + user);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER);
            int k = 1;
            pstmt.setString(k++, user.getEmail());
            pstmt.setString(k++, user.getPassword());
            pstmt.setString(k++, user.getName());
            pstmt.setString(k++, user.getLastName());
            pstmt.setFloat(k++, user.getBalance());
            pstmt.setInt(k, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update User");
            throw new DaoException("Can't update User", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit update method");
    }

    @Override
    public void deleteById(int id) {
        log.info("Enter delete method parameters: id=" + id);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_USER);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't delete User");
            throw new DaoException("Cannot delete User", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit delete method");
    }

    @Override
    public int getNumberOfRows() {
        log.info("Enter getNumberOfRows");
        int numOfRows = 0;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_COUNT);
            if (rs.next()) {
                numOfRows = rs.getInt("COUNT(id)");
            }
        } catch (SQLException e) {
            log.error("Can't get NumberOfRows");
            throw new DaoException("Can't get NumberOfRows", e);
        } finally {
            close(rs, stmt, con);
        }
        log.info("Exit getNumberOfRows method with parameters: " + numOfRows);
        return numOfRows;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        user.setLastName(rs.getString("last_name"));
        user.setBalance(rs.getFloat("balance"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
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
