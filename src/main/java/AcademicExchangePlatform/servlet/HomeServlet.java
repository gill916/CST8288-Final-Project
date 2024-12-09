package AcademicExchangePlatform.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.service.UserService;
import AcademicExchangePlatform.dbenum.UserType;
import javax.servlet.annotation.WebServlet;
import AcademicExchangePlatform.service.NotificationService;
import AcademicExchangePlatform.service.CourseApplicationService;
import AcademicExchangePlatform.model.AcademicProfessional;
import AcademicExchangePlatform.model.AcademicInstitution;
import java.util.Map;

/**
 * Servlet for handling the main dashboard/home page functionality.
 * Displays user-specific content and notifications based on user type.
 * Manages session validation and user data refresh.
 * Different content is displayed based on user type (Professional/Institution).
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();
    private final NotificationService notificationService = NotificationService.getInstance();
    private final CourseApplicationService applicationService = CourseApplicationService.getInstance();

    /**
     * Handles GET requests for the dashboard page.
     * Validates user session, refreshes user data, and sets up dashboard content.
     * Different content is displayed based on user type (Professional/Institution).
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If the request cannot be handled
     * @throws IOException If an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("DEBUG: HomeServlet doGet started");
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            System.out.println("DEBUG: User is null, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // Refresh user data from database
        if (user instanceof AcademicProfessional) {
            AcademicProfessional professional = userService.getProfessionalById(user.getUserId());
            if (professional != null) {
                System.out.println("DEBUG: Profile Complete Status from DB: " + professional.isProfileComplete());
                session.setAttribute("user", professional);
                user = professional;
            }
        }
        
        System.out.println("DEBUG: User type: " + user.getUserType());
        System.out.println("DEBUG: User class: " + user.getClass().getName());
        System.out.println("DEBUG: Profile Complete: " + 
            (user instanceof AcademicProfessional ? ((AcademicProfessional)user).isProfileComplete() : "N/A"));
        
        // Set common attributes
        request.setAttribute("user", user);
        int userId = user.getUserId();
        request.setAttribute("unreadNotifications", notificationService.getUnreadCount(userId));

        // Set user-specific attributes based on user type
        if (user.getUserType().equals(UserType.PROFESSIONAL)) {
            System.out.println("DEBUG: Setting PROFESSIONAL attributes");
            AcademicProfessional professional = (AcademicProfessional) user;
            
            // Get application statistics
            Map<String, Integer> stats = applicationService.getApplicationStatistics(professional.getUserId());
            request.setAttribute("applicationStats", stats);
        }
        
        System.out.println("DEBUG: Forwarding to dashboard.jsp");
        request.getRequestDispatcher("/WEB-INF/views/home/dashboard.jsp").forward(request, response);
    }
} 