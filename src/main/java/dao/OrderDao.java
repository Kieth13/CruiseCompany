package dao;

import model.Order;
import model.enums.Status;

import java.util.List;


public interface OrderDao {
    void create(Order order);
    Order getById(int id);
    List<Order> getAllOrdersByUserId(int id);
    Order getByCruiseAndUser(int cruiseId, int userId);
    List<Order> getAllOrdersByStatusAndLimitAndOffset(Status status, int currentPage, int numOfRecords);
    List<Order> findAll();
    void update(Order order);
    void deleteById (int id);
    int getNumberOfRows(Status status);

}
