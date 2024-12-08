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


@WebServlet({
    "/application/view/*",
    "/application/manage/*",
    "/application/manage",
    "/application/my-applications",
    "/application/apply",
    "/application/submit"
})
public class CourseApplicationServlet extends HttpServlet {
    private final CourseApplicationService applicationService = CourseApplicationService.getInstance();
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        
        System.out.println("Debug - PathInfo: " + pathInfo);
        System.out.println("Debug - ServletPath: " + servletPath);
        System.out.println("Debug - RequestURI: " + request.getRequestURI());
        
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        
        if ("/application/apply".equals(servletPath)) {
            // Get the course ID and forward to the application form
            String courseId = request.getParameter("courseId");
            Course course = courseService.getCourseById(Integer.parseInt(courseId));
            if (course != null) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("/WEB-INF/views/application/apply.jsp")
                       .forward(request, response);
                return;
            }
        } else if ("/application/submit".equals(servletPath)) {
            handleSubmitApplication(request, response);
            return;
        }
        
        // Handle other POST requests with pathInfo
        if (pathInfo != null) {
            switch (pathInfo) {
                case "/withdraw":
                    handleWithdrawApplication(request, response);
                    break;
                case "/updateStatus":
                    handleUpdateStatus(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void handleViewApplication(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int applicationId = Integer.parseInt(request.getPathInfo().substring(6));
            CourseApplication application = applicationService.getApplicationById(applicationId);
            
            if (application != null) {
                AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
                if (professional != null && application.getProfessionalId() == professional.getUserId()) {
                    request.setAttribute("application", application);
                    request.getRequestDispatcher("/WEB-INF/views/application/details.jsp")
                           .forward(request, response);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

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

    private void handleMyApplications(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
        List<CourseApplication> applications = applicationService.getApplicationsByProfessional(professional.getUserId());
        request.setAttribute("applications", applications);
        request.getRequestDispatcher("/WEB-INF/views/application/myApplications.jsp")
               .forward(request, response);
    }

    private void handleSubmitApplication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
        
        CourseApplication application = new CourseApplication();
        application.setCourseId(Integer.parseInt(request.getParameter("courseId")));
        application.setProfessionalId(professional.getUserId());
        application.setCoverLetter(request.getParameter("coverLetter"));
        application.setAdditionalDocuments(request.getParameter("additionalDocs"));
        application.setApplicationDate(new Date());
        application.setStatus(ApplicationStatus.PENDING);

        boolean success = applicationService.applyForCourse(application, professional);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/application/my-applications?success=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/view/" + application.getCourseId() + "?error=true");
        }
    }

    private void handleWithdrawApplication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        int applicationId = Integer.parseInt(request.getParameter("applicationId"));
        AcademicProfessional professional = (AcademicProfessional) request.getSession().getAttribute("user");
        
        boolean success = applicationService.withdrawApplication(applicationId, professional.getUserId());
        response.sendRedirect("/application/my-applications?withdrawn=" + success);
    }

    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        int applicationId = Integer.parseInt(request.getParameter("applicationId"));
        ApplicationStatus status = ApplicationStatus.valueOf(request.getParameter("status"));
        int institutionId = (int) request.getSession().getAttribute("institutionId");
        
        boolean success = applicationService.updateApplicationStatus(applicationId, status, institutionId);
        response.sendRedirect("/application/manage?updated=" + success);
    }
} 