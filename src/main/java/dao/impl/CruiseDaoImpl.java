package dao.impl;

import dao.ConnectionFactory;
import dao.CruiseDao;
import dao.exception.DaoException;
import model.Cruise;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CruiseDaoImpl implements CruiseDao {
    public static CruiseDao instance;
    
    private static final Logger log = Logger.getLogger(CruiseDao.class);

    private static final String SQL_INSERT_CRUISE = "INSERT INTO cruises VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_CRUISE_BY_ID = "SELECT * FROM cruises WHERE id=?";
    private static final String SQL_FIND_CRUISE_BY_NAME = "SELECT * FROM cruises WHERE name=?";
    private static final String SQL_FIND_CRUISE_BY_START_DATE = "SELECT * FROM cruises WHERE day_start=?";
    private static final String SQL_UPDATE_CRUISE = "UPDATE cruises SET name=?, ship_id=?, places_reserved=?, route_from=?, route_to=?, num_of_ports=?, day_start=?, day_end=?, price=? WHERE id=?";
    private static final String SQL_DELETE_CRUISE = "DELETE FROM cruises WHERE id=?";
    private static final String SQL_FIND_ALL_CRUISES = "SELECT * FROM cruises";
    private static final String SQL_FIND_CRUISES_BY_LIMIT_AND_OFFSET = "Select * from cruises ORDER BY ID LIMIT ? OFFSET ?";
    private static final String SQL_COUNT = "Select COUNT(id) from cruises";
    
    private CruiseDaoImpl() {}
    
    public static CruiseDao getInstance() {
        if (instance == null) {
            instance = new CruiseDaoImpl();
        }
        return instance;
    }

    @Override
    public void create(Cruise cruise) {
        log.info("Enter create with parameters: " + cruise);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_CRUISE);
            int k = 1;
            pstmt.setString(k++, cruise.getName());
            pstmt.setInt(k++, cruise.getShipId());
            pstmt.setInt(k++, cruise.getPlacesReserved());
            pstmt.setString(k++, cruise.getRouteFrom());
            pstmt.setString(k++, cruise.getRouteTo());
            pstmt.setInt(k++, cruise.getNumOfPorts());
            pstmt.setDate(k++, cruise.getDayStart());
            pstmt.setDate(k++, cruise.getDayEnd());
            pstmt.setFloat(k, cruise.getPrice());
            if (pstmt.executeUpdate() > 0) {
                Cruise newCruise = getByName(cruise.getName());
                cruise.setId(newCruise.getId());
            }
        } catch (SQLException e) {
            log.error("Can't create Cruise");
            throw new DaoException("Can't create Cruise", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit create method");
    }

    @Override
    public Cruise getById(int id) {
        log.info("Enter getById with parameters: id=" + id);
        Cruise cruise = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_CRUISE_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cruise = extractCruise(rs);
            }
        } catch (SQLException e) {
            log.error("Can't obtain Cruise by id");
            throw new DaoException("Can't obtain Cruise by id", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getById method with parameters: " + cruise);
        return cruise;
    }

    @Override
    public Cruise getByName(String name) {
        log.info("Enter getByName with parameters: name=" + name);
        Cruise cruise = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_CRUISE_BY_NAME);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cruise = extractCruise(rs);
            }
        } catch (SQLException e) {
            log.error("Can't obtain Cruise by name");
            throw new DaoException("Can't obtain Cruise by name", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getByName method with parameters: " + cruise);
        return cruise;
    }

    @Override
    public List<Cruise> getByDays(int days) {
        log.info("Enter getByDays with parameters: days=" + days);
        List<Cruise> cruises = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_CRUISES);
            while(rs.next()) {
                Cruise cruise = extractCruise(rs);
                long timeDiff = cruise.getDayEnd().getTime() - cruise.getDayStart().getTime();
                int daysDiff = (int) (timeDiff / (1000 * 60 * 60 * 24));
                if (daysDiff == days) {
                    cruises.add(cruise);
                }
            }
        } catch (SQLException e) {
            log.error("Can't obtain Cruise by days");
            throw new DaoException("Can't obtain Cruise by days", e);
        } finally {
            close(rs, stmt, con);
        }
        log.info("Exit getByDays method with parameters: " + cruises);
        return cruises;
    }

    @Override
    public List<Cruise> getByStartDate(Date startDay) {
        log.info("Enter getByStartDate with parameters: startDate=" + startDay);
        List<Cruise> cruises = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_CRUISE_BY_START_DATE);
            pstmt.setDate(1, startDay);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                Cruise cruise = extractCruise(rs);
                cruises.add(cruise);
            }
        } catch (SQLException e) {
            log.error("Can't obtain Cruise by startDay");
            throw new DaoException("Can't obtain Cruise by startDay", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getByStartDate method with parameters: " + cruises);
        return cruises;
    }

    @Override
    public List<Cruise> getAllCruisesByLimitAndOffset(int currentPage, int numOfRecords) {
        log.info("Enter getAllCruisesByLimitAndOffset method parameters: currentPage=" + currentPage + " numOfRecords=" +  numOfRecords);
        List<Cruise> cruises = new ArrayList<>();
        int start = currentPage * numOfRecords - numOfRecords;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_FIND_CRUISES_BY_LIMIT_AND_OFFSET);
            pstmt.setInt(1, numOfRecords);
            pstmt.setInt(2, start);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                cruises.add(extractCruise(rs));
            }
        } catch (SQLException e) {
            log.error("Can't obtain Cruise by Limit and Offset");
            throw new DaoException("Can't obtain Cruise by Limit and Offset", e);
        } finally {
            close(rs, pstmt, con);
        }
        log.info("Exit getAllShipsByLimitAndOffset method with parameters: " + cruises);
        return cruises;

    }

    @Override
    public List<Cruise> findAll() {
        log.info("Enter findAll");
        List<Cruise> cruises = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con =  ConnectionFactory.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_FIND_ALL_CRUISES);
            while(rs.next()) {
                cruises.add(extractCruise(rs));
            }
        } catch (SQLException e) {
            log.error("Can't find all Cruises");
            throw new DaoException("Can't find all Cruises", e);
        } finally {
            close(rs, stmt, con);
        }
        log.info("Exit finaAll method with parameters: " + cruises);
        return cruises;
    }

    @Override
    public void update(Cruise cruise) {
        log.info("Enter update with parameters: " + cruise);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        try {
            con = ConnectionFactory.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_CRUISE);
            int k = 1;
            pstmt.setString(k++, cruise.getName());
            pstmt.setInt(k++, cruise.getShipId());
            pstmt.setInt(k++, cruise.getPlacesReserved());
            pstmt.setString(k++, cruise.getRouteFrom());
            pstmt.setString(k++, cruise.getRouteTo());
            pstmt.setInt(k++, cruise.getNumOfPorts());
            pstmt.setDate(k++, cruise.getDayStart());
            pstmt.setDate(k++, cruise.getDayEnd());
            pstmt.setFloat(k++, cruise.getPrice());
            pstmt.setInt(k, cruise.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update Cruise");
            throw new DaoException("Can't update Cruise", e);
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
            pstmt = con.prepareStatement(SQL_DELETE_CRUISE);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't delete Cruise");
            throw new DaoException("Cannot delete Cruise", e);
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

    private Cruise extractCruise(ResultSet rs) throws SQLException {
        Cruise cruise = new Cruise();
        cruise.setId(rs.getInt("id"));
        cruise.setName(rs.getString("name"));
        cruise.setShipId(rs.getInt("ship_id"));
        cruise.setPlacesReserved(rs.getInt("places_reserved"));
        cruise.setRouteFrom(rs.getString("route_from"));
        cruise.setRouteTo(rs.getString("route_to"));
        cruise.setNumOfPorts(rs.getInt("num_of_ports"));
        cruise.setDayStart(rs.getDate("day_start"));
        cruise.setDayEnd(rs.getDate("day_end"));
        cruise.setPrice(rs.getFloat("price"));
        return cruise;
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
