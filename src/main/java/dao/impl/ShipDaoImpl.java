package dao.impl;

import dao.ConnectionFactory;
import dao.ShipDao;
import dao.exception.DaoException;
import model.Ship;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipDaoImpl implements ShipDao {
    public static ShipDao instance;

    private static final Logger log = Logger.getLogger(ShipDaoImpl.class);

    private static final String SQL_INSERT_SHIP = "INSERT INTO ships VALUES (DEFAULT, ?, ?)";
    private static final String SQL_FIND_SHIP_BY_ID = "Select * from ships where id=?";
    private static final String SQL_FIND_SHIP_BY_NAME = "Select * from ships where name=?";
    private static final String SQL_FIND_SHIP_BY_LIMIT_AND_OFFSET = "Select * from ships ORDER BY ID LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_SHIPS = "SELECT * FROM ships";
    private static final String SQL_UPDATE_SHIP = "UPDATE ships SET name=?, passenger_amount=? WHERE id=?";
    private static final String SQL_DELETE_SHIP = "DELETE FROM ships WHERE id=?";
    private static final String SQL_COUNT = "Select COUNT(id) from ships";

    private ShipDaoImpl() {}

    public static ShipDao getInstance() {
        if (instance == null) {
           instance = new ShipDaoImpl();
        }
        return instance;
    }

    @Override
    public void create(Ship ship) {
        log.info("Enter create with parameters: " + ship);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_SHIP);
            int k = 1;
            pstmt.setString(k++, ship.getName());
            pstmt.setInt(k, ship.getPassengerAmount());
            if (pstmt.executeUpdate() > 0) {
                Ship newShip = getByName(ship.getName());
                ship.setId(newShip.getId());
            }
        } catch (SQLException e) {
            log.error("Can't create Ship");
            throw new DaoException("Can't create Ship", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit create method");
    }

    @Override
    public Ship getById(int id) {
        log.info("Enter getById method with parameters: id=" + id);
        Ship ship = new Ship();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_SHIP_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ship = extractShip(rs);
            }
        } catch (SQLException e) {
            log.error("Can't get Ship by Id");
            throw new DaoException("Cannot get Ship by Id", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getById method with parameters: " + ship);
        return ship;
    }

    @Override
    public Ship getByName(String name) {
        log.info("Enter getByName method with parameters: name=" + name);
        Ship ship = new Ship();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_SHIP_BY_NAME);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ship = extractShip(rs);
            }
        } catch (SQLException e) {
            log.error("Can't get Ship by Name");
            throw new DaoException("Cannot get Ship by Name", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getById method with parameters: " + ship);
        return ship;
    }

    @Override
    public List<Ship> getAllShipsByLimitAndOffset(int currentPage, int numOfRecords) {
        log.info("Enter getAllShipsByLimitAndOffset method with parameters: currentPage=" + currentPage + " numOfRecords=" +  numOfRecords);
        List<Ship> ships = new ArrayList<>();
        int start = currentPage * numOfRecords - numOfRecords;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_SHIP_BY_LIMIT_AND_OFFSET);
            pstmt.setInt(1, numOfRecords);
            pstmt.setInt(2, start);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                ships.add(extractShip(rs));
            }
        } catch (SQLException e) {
            log.error("Can't get Ships by Limit and Offset");
            throw new DaoException("Can't get Ships by Limit and Offset", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getAllShipsByLimitAndOffset method with parameters: " + ships);
        return ships;

    }

    @Override
    public List<Ship> findAll() {
        log.info("Enter findAll");
        List<Ship> ships = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_SHIPS);
            while(rs.next()) {
                ships.add(extractShip(rs));
            }
        } catch (SQLException e) {
            log.error("Can't find all Ships");
            throw new DaoException("Can't find all Ships", e);
        } finally {
            close(rs, stmt, con);
        }
        log.info("Exit finaAll method with parameters: " + ships);
        return ships;
    }

    @Override
    public void update(Ship ship) {
        log.info("Enter update with parameters: " + ship);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_SHIP);
            int k = 1;
            pstmt.setString(k++, ship.getName());
            pstmt.setInt(k++, ship.getPassengerAmount());
            pstmt.setInt(k, ship.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update Ship");
            throw new DaoException("Can't update Ship", e);
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
            pstmt = con.prepareStatement(SQL_DELETE_SHIP);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't delete Ship");
            throw new DaoException("Cannot delete Ship", e);
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

    private Ship extractShip(ResultSet rs) throws SQLException {
        Ship ship= new Ship();
        ship.setId(rs.getInt("id"));
        ship.setName(rs.getString("name"));
        ship.setPassengerAmount(rs.getInt("passenger_amount"));
        return ship;
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
