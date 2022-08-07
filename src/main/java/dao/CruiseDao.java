package dao;

import model.Cruise;

import java.sql.Date;
import java.util.List;

public interface CruiseDao {
    void create(Cruise ship);
    Cruise getById(int id);
    Cruise getByName(String name);
    List<Cruise> getByStartDate(Date startDay);
    List<Cruise> getByDays(int days);
    List<Cruise> getAllCruisesByLimitAndOffset(int currentPage, int numOfRecords);
    List<Cruise> findAll();
    void update(Cruise ship);
    void deleteById(int id);
    int getNumberOfRows();
}
