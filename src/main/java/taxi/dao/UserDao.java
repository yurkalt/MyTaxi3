package taxi.dao;

import taxi.model.Order;
import taxi.model.User;

import java.util.Collection;
import java.util.List;

// CRUD, Create, Read, Update, Delete
public interface UserDao {

    User createUser(User user);
    Collection<User> getAllUsers();
    User updateUser(User newUser);
    User deleteUser(int id);

    User find(String phone);
    List<User> getAllPassenger();
    List<User> getAllDrivers();
    List<Order> getOrdersOfUser(User user);
}