package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.service.CourseService;
import AcademicExchangePlatform.model.CourseSearchCriteria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Servlet for searching courses.
 */
@WebServlet("/course/search")
public class CourseSearchServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();

    /**
     * Handles GET requests for course search.
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error writing to the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Build search criteria from request parameters
        CourseSearchCriteria criteria = new CourseSearchCriteria();
        
        // Basic search
        criteria.setKeyword(request.getParameter("keyword"));
        
        // Filters
        if (request.getParameter("schedule") != null && !request.getParameter("schedule").isEmpty()) {
            criteria.setSchedule(Schedule.valueOf(request.getParameter("schedule")));
        }
        
        if (request.getParameter("deliveryMethod") != null && !request.getParameter("deliveryMethod").isEmpty()) {
            criteria.setDeliveryMethod(DeliveryMethod.valueOf(request.getParameter("deliveryMethod")));
        }
        
        if (request.getParameter("term") != null && !request.getParameter("term").isEmpty()) {
            criteria.setTerm(request.getParameter("term"));
        }
        
        // Checkboxes
        criteria.setActiveOnly("true".equals(request.getParameter("activeOnly")));
        criteria.setOpenOnly("true".equals(request.getParameter("openOnly")));

        // Perform search
        List<Course> searchResults = courseService.searchCourses(criteria);
        
        // Get available terms for filter dropdown
        Set<String> availableTerms = courseService.getAvailableTerms();

        // Set attributes for JSP
        request.setAttribute("courses", searchResults);
        request.setAttribute("availableTerms", availableTerms);
        
        // Forward to search page
        request.getRequestDispatcher("/WEB-INF/views/course/search.jsp")
               .forward(request, response);
    }
} 