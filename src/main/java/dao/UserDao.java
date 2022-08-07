package dao;

import model.Ship;
import model.User;

import java.util.List;

public interface UserDao {
    void create (User user);
    User getById(int userId);
    User getByEmailAndPassword(String email, String password);
    List<User> getAllUsersByLimitAndOffset(int currentPage, int numOfRecords);
    List<User> findAll();
    void update(User user);
    void deleteById(int id);
    int getNumberOfRows();
}
