package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.model.AcademicProfessional;
import AcademicExchangePlatform.service.CourseApplicationService;
import AcademicExchangePlatform.service.CourseService;
import AcademicExchangePlatform.dbenum.ApplicationStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@WebServlet("/application/*")
public class CourseApplicationServlet extends HttpServlet {
    private final CourseApplicationService applicationService = CourseApplicationService.getInstance();
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (pathInfo.startsWith("/view/")) {
            handleViewApplication(request, response);
        } else if (pathInfo.equals("/manage")) {
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
        
        switch (pathInfo) {
            case "/submit":
                handleSubmitApplication(request, response);
                break;
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
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        int institutionId = (int) request.getSession().getAttribute("institutionId");
        
        List<CourseApplication> applications = applicationService.getApplicationsByCourse(courseId, institutionId);
        request.setAttribute("applications", applications);
        request.setAttribute("course", courseService.getCourseById(courseId));
        
        request.getRequestDispatcher("/WEB-INF/views/application/manageApplications.jsp")
               .forward(request, response);
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
            response.sendRedirect("/application/my-applications?success=true");
        } else {
            response.sendRedirect("/course/view?id=" + application.getCourseId() + "&error=true");
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