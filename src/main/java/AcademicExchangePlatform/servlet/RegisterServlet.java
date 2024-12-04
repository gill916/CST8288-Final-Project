package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.dao.UserDAOImpl;
import AcademicExchangePlatform.dbenum.UserType;
import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.UserFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userTypeValue = request.getParameter("userType");

        try {
            UserType userType = UserType.fromValue(userTypeValue);
            User user = UserFactory.createUser(userType.getValue());
            user.setEmail(email);
            user.setPassword(password);
            user.setUserType(userType);

            if (userDAO.addUser(user)) {
                response.sendRedirect("login.jsp");
            } else {
                response.getWriter().write("Registration failed. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            response.getWriter().write("Invalid user type. Registration failed.");
        }
    }
}
