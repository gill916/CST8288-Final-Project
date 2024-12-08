package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.service.CourseApplicationService;
import AcademicExchangePlatform.service.CourseService;
import AcademicExchangePlatform.model.Course;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ManageApplicationsServlet extends HttpServlet {
    private final CourseApplicationService applicationService = CourseApplicationService.getInstance();
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Check if user is authenticated and has institution role
        Integer institutionId = (Integer) request.getSession().getAttribute("institutionId");
        if (institutionId == null) {
            response.sendRedirect("/login");
            return;
        }
        
        if (pathInfo == null || pathInfo.equals("/")) {
            List<CourseApplication> applications = applicationService.getAllInstitutionApplications(institutionId);
            request.setAttribute("applications", applications);
            request.getRequestDispatcher("/WEB-INF/views/application/manageAll.jsp")
                   .forward(request, response);
            return;
        }
        
        try {
            int applicationId = Integer.parseInt(pathInfo.substring(1));
            CourseApplication application = applicationService.getApplicationById(applicationId);
            
            if (application != null) {
                // Verify institution has access to this application
                Course course = courseService.getCourseById(application.getCourseId());
                if (course != null && course.getInstitutionId() == institutionId) {
                    request.setAttribute("application", application);
                    request.getRequestDispatcher("/WEB-INF/views/application/details.jsp")
                           .forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            // Log error
            e.printStackTrace();
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
} 