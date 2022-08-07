package dao.impl;

import dao.ConnectionFactory;
import dao.OrderDao;
import dao.exception.DaoException;
import model.Order;
import model.enums.Status;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    public static OrderDao instance;

    private static final Logger log = Logger.getLogger(OrderDao.class);

    private static final String SQL_INSERT_ORDER = "INSERT INTO orders VALUES (DEFAULT, ?, ?, ?, ?, ?, null)";
    private static final String SQL_FIND_ORDER_BY_ID = "Select * from orders where id=?";
    private static final String SQL_FIND_ORDER_BY_CRUISES_AND_USERS = "Select * from orders where cruise_id=? AND user_id=?";
    private static final String SQL_FIND_ALL_USER_ORDERS = "SELECT * FROM orders JOIN users on orders.user_id=users.id where users.id=?";
    private static final String SQL_FIND_ORDER_BY_STATUS = "Select * from orders where status=? ORDER BY ID LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_ORDERS = "SELECT * FROM orders";
    private static final String SQL_UPDATE_ORDER = "UPDATE orders SET cruise_id=?, user_id=?, num_of_passengers=?, total_price=?, status=?, user_docs=? WHERE id=?";
    private static final String SQL_DELETE_ODER = "DELETE FROM orders WHERE id=?";
    private static final String SQL_COUNT = "Select COUNT(id) from orders where status=? ";

    private OrderDaoImpl() {}

    public static OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDaoImpl();
        }
        return instance;
    }

    @Override
    public void create(Order order) {
        log.info("Enter create with parameters: " + order);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_ORDER);
            int k = 1;
            pstmt.setInt(k++, order.getCruiseId());
            pstmt.setInt(k++, order.getUserId());
            pstmt.setInt(k++, order.getNumOfPassengers());
            pstmt.setFloat(k++, order.getTotalPrice());
            pstmt.setString(k, order.getStatus().name());
            if (pstmt.executeUpdate() > 0) {
                Order newOrder = getByCruiseAndUser(order.getCruiseId(), order.getUserId());
                order.setId(newOrder.getId());
            }
        } catch (SQLException e) {
            log.error("Can't create Order");
            throw new DaoException("Can't create Order", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit create method");
    }

    @Override
    public Order getById(int id) {
        log.info("Enter getById method with parameters: id=" + id );
        Order order = new Order();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ORDER_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                order = extractOrder(rs);
            }
        } catch (SQLException e) {
            log.error("Can't get Order by Id");
            throw new DaoException("Can't get Order by Id", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getById method with parameters: " + order);
        return order;
    }

    @Override
    public Order getByCruiseAndUser(int cruiseId, int userId) {
        log.info("Enter getByCruiseAndUser method parameters: cruiseId=" + cruiseId + " userId="+ userId);
        Order order = new Order();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ORDER_BY_CRUISES_AND_USERS);
            pstmt.setInt(1, cruiseId);
            pstmt.setInt(2, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                order = extractOrder(rs);
            }
        } catch (SQLException e) {
            log.error("Can't get Order by Cruise and User");
            throw new DaoException("Can't get Order by Cruise and User", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getByCruiseAndUser method with parameters: " + order);
        return order;
    }

    @Override
    public List<Order> getAllOrdersByUserId(int id) {
        log.info("Enter getAllOrdersByUserId with parameters: UserId=" + id);
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ALL_USER_ORDERS);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            log.error("Can't find Orders by userId");
            throw new DaoException("Can't find Orders by userId", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getAllOrdersByUserId method with parameters: " + orders);
        return orders;
    }

    @Override
    public List<Order> getAllOrdersByStatusAndLimitAndOffset(Status status, int currentPage, int numOfRecords) {
        log.info("Enter getAllOrdersByStatus method parameters: status=" + status + " currentPage=" + currentPage + " numOfRecords=" +  numOfRecords);
        List<Order> orders = new ArrayList<>();
        int start = currentPage * numOfRecords - numOfRecords;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_ORDER_BY_STATUS);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, numOfRecords);
            pstmt.setInt(3, start);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            log.error("Can't get Orders by Status and Limit and Offset");
            throw new DaoException("Can't get Orders by Status and Limit and Offset", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getAllOrdersByStatus method with parameters: " + orders);
        return orders;
    }

    @Override
    public List<Order> findAll() {
        log.info("Enter findAll");
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_ORDERS);
            while(rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            log.error("Can't find all Orders");
            throw new DaoException("Can't find all Orders", e);
        } finally {
            close(rs, stmt, con);
        }
        log.info("Exit findAll method with parameters: " + orders);
        return orders;
    }

    @Override
    public void update(Order order) {
        log.info("Enter update with parameters: " + order);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_ORDER);
            int k = 1;
            pstmt.setInt(k++, order.getCruiseId());
            pstmt.setInt(k++, order.getUserId());
            pstmt.setInt(k++, order.getNumOfPassengers());
            pstmt.setFloat(k++, order.getTotalPrice());
            pstmt.setString(k++, order.getStatus().name());
            pstmt.setString(k++, order.getUserDocs());
            pstmt.setInt(k, order.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update Order");
            throw new DaoException("Can't update Order", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit update method");
    }

    @Override
    public void deleteById(int id) {
        log.info("Enter delete method with parameters: id=" + id);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_ODER);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't delete Order");
            throw new DaoException("Cannot delete Order", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit delete method");
    }

    @Override
    public int getNumberOfRows(Status status) {
        log.info("Enter getNumberOfRows with parameters: status=" + status);
        int numOfRows = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_COUNT);
            pstmt.setString(1, status.name());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                numOfRows = rs.getInt("COUNT(id)");
            }
        } catch (SQLException e) {
            log.error("Can't get NumberOfRows");
            throw new DaoException("Can't get NumberOfRows", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getNumberOfRows method with parameters: " + numOfRows);
        return numOfRows;
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setCruiseId(rs.getInt("cruise_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setNumOfPassengers(rs.getInt("num_of_passengers"));
        order.setTotalPrice(rs.getFloat("total_price"));
        order.setStatus(Status.valueOf(rs.getString("status")));
        order.setUserDocs(rs.getString("user_docs"));
        return order;
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
