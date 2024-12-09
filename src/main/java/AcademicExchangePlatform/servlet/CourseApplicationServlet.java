/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.model.AcademicProfessional;
import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.service.CourseApplicationService;
import AcademicExchangePlatform.service.CourseService;
import AcademicExchangePlatform.dbenum.ApplicationStatus;
import AcademicExchangePlatform.dbenum.UserType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Servlet for handling course application operations.
 * Manages application viewing, submission, withdrawal, and status updates.
 * URL patterns:
 * - /application/view/* : View specific application
 * - /application/manage/* : Manage applications (institution)
 * - /application/my-applications : View user's applications
 * - /application/apply : Apply for a course
 * - /application/submit : Submit application
 * - /application/withdraw : Withdraw application
 * - /application/updateStatus : Update application status
 */
@WebServlet({
    "/application/view/*",
    "/application/manage/*",
    "/application/manage",
    "/application/my-applications",
    "/application/apply",
    "/application/submit",
    "/application/withdraw",
    "/application/updateStatus"
})
public class CourseApplicationServlet extends HttpServlet {
    private final CourseApplicationService applicationService = CourseApplicationService.getInstance();
    private final CourseService courseService = CourseService.getInstance();

    /**
     * Handles GET requests for viewing and managing applications.
     * Routes to appropriate handler based on URL pattern.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        
        System.out.println("Debug - Full Request URI: " + request.getRequestURI());
        System.out.println("Debug - Context Path: " + request.getContextPath());
        System.out.println("Debug - PathInfo: " + pathInfo);
        System.out.println("Debug - ServletPath: " + servletPath);
        System.out.println("Debug - User Type: " + ((User)request.getSession().getAttribute("user")).getUserType());
        
        if (servletPath.equals("/application/view") && pathInfo != null) {
            handleViewApplication(request, response);
            return;
        }
        
        if ("/application/my-applications".equals(request.getRequestURI().substring(request.getContextPath().length()))) {
            System.out.println("Debug - Handling My Applications");
            handleMyApplications(request, response);
            return;
        }
        
        if (pathInfo == null && servletPath.equals("/application/manage")) {
            handleManageApplications(request, response);
            return;
        }
        
        if (pathInfo == null || pathInfo.equals("/")) {
            if (servletPath.equals("/application/manage")) {
                handleManageApplications(request, response);
                return;
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (pathInfo.startsWith("/view/")) {
            handleViewApplication(request, response);
        } else if (pathInfo.startsWith("/manage")) {
            handleManageApplications(request, response);
        } else if (pathInfo.equals("/my-applications")) {
            handleMyApplications(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Handles POST requests for application operations.
     * Includes submission, withdrawal, and status updates.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        
        System.out.println("Debug - POST ServletPath: " + servletPath);
        System.out.println("Debug - POST PathInfo: " + pathInfo);
        System.out.println("Debug - POST Parameters: " + request.getParameterMap().keySet());
        
        if ("/application/updateStatus".equals(servletPath)) {
            handleUpdateStatus(request, response);
            return;
        }
        
        if ("/application/submit".equals(servletPath)) {
            handleSubmitApplication(request, response);
            return;
        } else if ("/application/apply".equals(servletPath)) {
            String courseId = request.getParameter("courseId");
            Course course = courseService.getCourseById(Integer.parseInt(courseId));
            if (course != null) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("/WEB-INF/views/application/apply.jsp")
                       .forward(request, response);
                return;
            }
        } else if ("/application/withdraw".equals(servletPath)) {
            handleWithdrawApplication(request, response);
            return;
        }
        
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Handles viewing of a specific application.
     * Validates user access rights before displaying application details.
     */
    private void handleViewApplication(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            int applicationId = Integer.parseInt(pathInfo.substring(1));
            
            CourseApplication application = applicationService.getApplicationById(applicationId);
            
            if (application != null) {
                User user = (User) request.getSession().getAttribute("user");
                boolean hasAccess = false;
                
                if (user.getUserType() == UserType.PROFESSIONAL) {
                    AcademicProfessional professional = (AcademicProfessional) user;
                    hasAccess = application.getProfessionalId() == professional.getUserId();
                } else if (user.getUserType() == UserType.INSTITUTION) {
                    // Check if the application is for a course belonging to this institution
                    Course course = courseService.getCourseById(application.getCourseId());
                    hasAccess = course != null && course.getInstitutionId() == user.getUserId();
                }
                
                if (hasAccess) {
                    request.setAttribute("application", application);
                    request.getRequestDispatcher("/WEB-INF/views/application/details.jsp")
                           .forward(request, response);
                    return;
                }
            }
            
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Handles management of applications for institutions.
     * Shows all applications or applications for a specific course.
     */
    private void handleManageApplications(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            System.out.println("Debug - User: " + user);
            System.out.println("Debug - UserType: " + (user != null ? user.getUserType() : "null"));

            String courseIdParam = request.getParameter("courseId");
            System.out.println("Debug - CourseId Parameter: " + courseIdParam);

            if (user == null || !UserType.INSTITUTION.equals(user.getUserType())) {
                System.out.println("Debug - User validation failed");
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }

            if (courseIdParam == null || courseIdParam.isEmpty()) {
                // Show all applications if no courseId
                List<CourseApplication> applications = applicationService.getAllInstitutionApplications(user.getUserId());
                request.setAttribute("applications", applications);
                request.getRequestDispatcher("/WEB-INF/views/application/manageAll.jsp")
                       .forward(request, response);
                return;
            }
            
            // Show applications for specific course
            int courseId = Integer.parseInt(courseIdParam);
            Course course = courseService.getCourseById(courseId);
            if (course == null || course.getInstitutionId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            List<CourseApplication> applications = applicationService.getApplicationsByCourse(courseId, user.getUserId());
            request.setAttribute("applications", applications);
            request.setAttribute("course", course);
            
            request.getRequestDispatcher("/WEB-INF/views/application/manageApplications.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            System.out.println("Debug - Exception in handleManageApplications: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

    /**
     * Handles viewing of a professional's own applications.
     * Shows list of all applications submitted by the user.
     */
    private void handleMyApplications(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("Debug - Entering handleMyApplications");
        
        AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
        System.out.println("Debug - Professional from session: " + professional);
        
        if (professional == null) {
            System.out.println("Debug - No professional found in session");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        List<CourseApplication> applications = applicationService.getApplicationsByProfessional(professional.getUserId());
        System.out.println("Debug - Retrieved applications count: " + (applications != null ? applications.size() : "null"));
        
        request.setAttribute("applications", applications);
        System.out.println("Debug - Forward to myApplications.jsp");
        request.getRequestDispatcher("/WEB-INF/views/application/myApplications.jsp")
               .forward(request, response);
    }

    /**
     * Handles submission of new course applications.
     * Validates professional's profile completion and course availability.
     */
    private void handleSubmitApplication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
            if (professional == null) {
                System.out.println("Debug - No professional found in session");
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }
            
            System.out.println("Debug - Professional ID: " + professional.getUserId());
            System.out.println("Debug - Is Profile Complete: " + professional.isProfileComplete());
            
            String courseIdParam = request.getParameter("courseId");
            String coverLetter = request.getParameter("coverLetter");
            String additionalDocs = request.getParameter("additionalDocs");
            
            System.out.println("Debug - CourseId: " + courseIdParam);
            System.out.println("Debug - CoverLetter length: " + (coverLetter != null ? coverLetter.length() : "null"));
            
            // Get course to check if it exists and is active
            Course course = courseService.getCourseById(Integer.parseInt(courseIdParam));
            if (course == null) {
                System.out.println("Debug - Course not found");
                response.sendRedirect(request.getContextPath() + "/error?message=course_not_found");
                return;
            }
            System.out.println("Debug - Course Status: " + course.getStatus());
            
            CourseApplication application = new CourseApplication();
            application.setCourseId(Integer.parseInt(courseIdParam));
            application.setProfessionalId(professional.getUserId());
            application.setCoverLetter(coverLetter);
            application.setAdditionalDocuments(additionalDocs);
            application.setApplicationDate(new Date());
            application.setStatus(ApplicationStatus.PENDING);

            boolean success = applicationService.applyForCourse(application, professional);
            System.out.println("Debug - Application submission success: " + success);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/application/my-applications");
            } else {
                if (!professional.isProfileComplete()) {
                    response.sendRedirect(request.getContextPath() + "/profile/edit?error=incomplete_profile");
                } else {
                    response.sendRedirect(request.getContextPath() + "/course/view/" + application.getCourseId() + "?error=submission_failed");
                }
            }
        } catch (Exception e) {
            System.out.println("Debug - Exception in handleSubmitApplication: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    /**
     * Handles withdrawal of an existing application.
     * Validates that the user owns the application being withdrawn.
     */
    private void handleWithdrawApplication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            String applicationIdParam = request.getParameter("applicationId");
            if (applicationIdParam == null || applicationIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int applicationId = Integer.parseInt(applicationIdParam);
            AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
            
            if (professional == null) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }

            boolean success = applicationService.withdrawApplication(applicationId, professional.getUserId());
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/application/my-applications?success=withdrawn");
            } else {
                response.sendRedirect(request.getContextPath() + "/application/my-applications?error=withdraw_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    /**
     * Handles updating of application status by institutions.
     * Validates institution's authority to update the application.
     */
    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            String applicationIdParam = request.getParameter("applicationId");
            String statusParam = request.getParameter("status");
            
            System.out.println("Debug - Received applicationId: " + applicationIdParam);
            System.out.println("Debug - Received status: " + statusParam);
            
            if (applicationIdParam == null || statusParam == null) {
                System.out.println("Debug - Missing required parameters");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            int applicationId = Integer.parseInt(applicationIdParam);
            ApplicationStatus status = ApplicationStatus.valueOf(statusParam);
            User user = (User) request.getSession().getAttribute("user");
            
            if (user == null || user.getUserType() != UserType.INSTITUTION) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            boolean success = applicationService.updateApplicationStatus(applicationId, status, user.getUserId());
            
            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/application/view/" + applicationId + "?updated=true");
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/application/view/" + applicationId + "?error=update_failed");
            }
            
        } catch (Exception e) {
            System.out.println("Debug - Exception in handleUpdateStatus: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
} 