package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.UserDAO;
import AcademicExchangePlatform.dao.UserDAOImpl;
import AcademicExchangePlatform.model.User;

public class UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    public boolean register(User user) {
        return userDAO.addUser(user);
    }

    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean updateProfile(User user) {
        return userDAO.updateUser(user);
    }
}
