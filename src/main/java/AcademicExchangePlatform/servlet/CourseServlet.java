/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.service.CourseService;
import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import AcademicExchangePlatform.dbenum.UserType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servlet for managing courses.
 */
@WebServlet({"/course/create", "/course/edit/*", "/course/manage"})
public class CourseServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Handles GET requests for course management.
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error writing to the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        
        if (pathInfo.equals("/course/edit")) {
            int courseId = Integer.parseInt(request.getParameter("id"));
            Course course = courseService.getCourseById(courseId);
            
            if (course != null) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("/WEB-INF/views/course/form.jsp")
                       .forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (pathInfo.equals("/course/create")) {
            Course newCourse = new Course();
            System.out.println("DEBUG: New Course Object: " + newCourse);
            request.setAttribute("course", newCourse);
            
            // Convert enums to List for better JSP handling
            List<Schedule> scheduleList = Arrays.asList(Schedule.values());
            List<DeliveryMethod> deliveryMethodList = Arrays.asList(DeliveryMethod.values());
            
            request.setAttribute("schedules", scheduleList);
            request.setAttribute("deliveryMethods", deliveryMethodList);
            
            System.out.println("DEBUG: Request Attributes Set");
            System.out.println("DEBUG: Course: " + request.getAttribute("course"));
            System.out.println("DEBUG: Schedules: " + scheduleList);
            System.out.println("DEBUG: DeliveryMethods: " + deliveryMethodList);
            
            request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
        } else if (pathInfo.equals("/course/manage")) {
            handleManageCourses(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Handles the management of courses for institutions.
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error writing to the response
     */
    private void handleManageCourses(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String statusFilter = request.getParameter("status");
        String termFilter = request.getParameter("term");
        
        System.out.println("DEBUG: Handling manage courses");
        System.out.println("DEBUG: User Type: " + user.getUserType());
        System.out.println("DEBUG: User ID: " + user.getUserId());
        System.out.println("DEBUG: Status Filter: " + statusFilter);
        System.out.println("DEBUG: Term Filter: " + termFilter);
        
        // Get courses for the institution
        List<Course> courses = courseService.getCoursesByInstitution(user.getUserId());
        System.out.println("DEBUG: SQL Query executed for institution ID: " + user.getUserId());
        System.out.println("DEBUG: Retrieved courses: " + courses);
        
        // Filter courses if status parameter is present
        if (statusFilter != null && !statusFilter.isEmpty()) {
            CourseStatus status = CourseStatus.valueOf(statusFilter);
            courses = courses.stream()
                           .filter(c -> c.getStatus() == status)
                           .collect(Collectors.toList());
        }
        
        // Filter courses if term parameter is present
        if (termFilter != null && !termFilter.isEmpty()) {
            courses = courses.stream()
                           .filter(c -> termFilter.equals(c.getTerm()))
                           .collect(Collectors.toList());
        }
        
        // Set attributes needed by manage.jsp
        request.setAttribute("courses", courses);
        request.setAttribute("terms", courseService.getAvailableTerms());
        
        // Forward to manage.jsp
        request.getRequestDispatcher("/WEB-INF/views/course/manage.jsp").forward(request, response);
    }

    /**
     * Handles the view of a course.
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error writing to the response
     */
    private void handleViewCourse(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int courseId = Integer.parseInt(request.getPathInfo().substring(6));
            Course course = courseService.getCourseById(courseId);
            if (course != null) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("/WEB-INF/views/course/viewCourse.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Handles POST requests for course creation.
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error writing to the response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.getUserType().equals(UserType.INSTITUTION)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        try {
            Course course = createCourseFromRequest(request);
            course.setInstitutionId(user.getUserId());
            courseService.createCourse(course);
            response.sendRedirect(request.getContextPath() + "/course/manage");
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            // Add back the form options
            request.setAttribute("schedules", Schedule.values());
            request.setAttribute("deliveryMethods", DeliveryMethod.values());
            request.getRequestDispatcher("/WEB-INF/views/course/form.jsp").forward(request, response);
        }
    }

    /**
     * Creates a course object from the request parameters.
     * @param request The HTTP request
     * @return The created course object
     * @throws ParseException If there is an error parsing the date
     */
    private Course createCourseFromRequest(HttpServletRequest request) throws ParseException {
        Course course = new Course();
        course.setCourseTitle(request.getParameter("courseTitle"));
        course.setCourseCode(request.getParameter("courseCode"));
        course.setTerm(request.getParameter("term"));
        
        // Set default status to ACTIVE for new courses
        course.setStatus(CourseStatus.ACTIVE);
        
        // Normalize enum values
        String scheduleStr = request.getParameter("schedule").toUpperCase().replace(" ", "_");
        String deliveryStr = request.getParameter("deliveryMethod").toUpperCase().replace(" ", "_");
        
        try {
            course.setSchedule(Schedule.valueOf(scheduleStr));
            course.setDeliveryMethod(DeliveryMethod.valueOf(deliveryStr));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value provided: " + e.getMessage());
        }
        
        course.setOutline(request.getParameter("outline"));
        course.setPreferredQualifications(request.getParameter("preferredQualifications"));
        course.setCompensation(new BigDecimal(request.getParameter("compensation")));
        
        String deadlineStr = request.getParameter("applicationDeadline");
        if (deadlineStr != null && !deadlineStr.isEmpty()) {
            course.setApplicationDeadline(dateFormat.parse(deadlineStr));
        }
        
        return course;
    }
}