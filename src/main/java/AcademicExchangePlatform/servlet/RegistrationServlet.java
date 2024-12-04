package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.UserFactory;
import AcademicExchangePlatform.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter("userType");
        User user = UserFactory.createUser(userType);
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));

        if (userService.register(user)) {
            response.sendRedirect("success.jsp");
        } else {
            response.sendRedirect("error.jsp");
        }
    }
}
