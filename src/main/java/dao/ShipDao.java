package dao;

import model.Order;
import model.Ship;
import model.enums.Status;

import java.sql.SQLException;
import java.util.List;

public interface ShipDao {
    void create (Ship ship) throws SQLException;
    Ship getById (int id);
    Ship getByName(String name);
    List<Ship> getAllShipsByLimitAndOffset(int currentPage, int numOfRecords);
    List<Ship> findAll();
    void update (Ship ship);
    void deleteById(int id);
    int getNumberOfRows();
}
