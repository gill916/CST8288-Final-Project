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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();
    private final NotificationService notificationService = NotificationService.getInstance();
    private final CourseApplicationService applicationService = CourseApplicationService.getInstance();

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