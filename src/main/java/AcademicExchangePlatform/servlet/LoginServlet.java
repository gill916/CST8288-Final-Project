package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.service.UserService;
import AcademicExchangePlatform.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user login functionality.
 * Validates user credentials and manages session creation.
 * Redirects to the dashboard or login page based on session state.
 */
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    /**
     * Handles GET requests for the login page.
     * Checks if a user is already logged in and redirects to the dashboard or login page.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If the request cannot be handled
     * @throws IOException If an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if a session already exists
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // If a user is already logged in, redirect to the unified dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Otherwise, show the login page
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests for user login.
     * Validates user credentials and creates a session for the user.
     * Redirects to the dashboard after successful login.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If the request cannot be handled
     * @throws IOException If an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
    
        try {
            User user = userService.login(email, password); // Validate login credentials
            if (user != null) {
                // Create a new session for the user
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userType", user.getUserType().toString());
                session.setAttribute("userId", user.getUserId());  // Add this line
                System.out.println("DEBUG: Setting userId in session: " + user.getUserId());
    
                // Redirect to the unified dashboard after login
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                // Login failed, display an error message
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Handle exceptions and show an error message
            request.setAttribute("error", "An error occurred during login: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
