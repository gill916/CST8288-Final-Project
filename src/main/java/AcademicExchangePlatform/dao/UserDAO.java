package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.User;

public interface UserDAO {
    boolean addUser(User user);
    User getUserByEmail(String email);
    User getUserById(int userId);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}
