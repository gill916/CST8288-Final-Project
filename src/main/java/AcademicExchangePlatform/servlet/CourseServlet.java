package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.AcademicInstitution;
import AcademicExchangePlatform.service.CourseService;
import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/course/*")
public class CourseServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        AcademicInstitution institution = (AcademicInstitution) request.getSession().getAttribute("user");
        if (institution == null) {
            response.sendRedirect("/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            request.setAttribute("courses", courseService.getCoursesByInstitution(institution.getUserId()));
            request.getRequestDispatcher("/WEB-INF/course/list.jsp").forward(request, response);
        } else if (pathInfo.equals("/create")) {
            request.getRequestDispatcher("/WEB-INF/course/form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            int courseId = Integer.parseInt(pathInfo.substring(6));
            Course course = courseService.getCourseById(courseId);
            if (course != null && course.getInstitutionId() == institution.getUserId()) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("/WEB-INF/course/form.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        AcademicInstitution institution = (AcademicInstitution) request.getSession().getAttribute("user");
        if (institution == null) {
            response.sendRedirect("/login");
            return;
        }

        try {
            Course course = createCourseFromRequest(request);
            course.setInstitutionId(institution.getUserId());

            String courseId = request.getParameter("courseId");
            boolean success;
            if (courseId != null && !courseId.isEmpty()) {
                course.setCourseId(Integer.parseInt(courseId));
                success = courseService.updateCourse(course);
            } else {
                success = courseService.createCourse(course);
            }

            if (success) {
                request.setAttribute("success", "Course saved successfully");
            } else {
                request.setAttribute("error", "Failed to save course");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }

        response.sendRedirect("/course");
    }

    private Course createCourseFromRequest(HttpServletRequest request) throws ParseException {
        Course course = new Course();
        course.setCourseTitle(request.getParameter("courseTitle"));
        course.setCourseCode(request.getParameter("courseCode"));
        course.setTerm(request.getParameter("term"));
        course.setSchedule(Schedule.valueOf(request.getParameter("schedule")));
        course.setDeliveryMethod(DeliveryMethod.valueOf(request.getParameter("deliveryMethod")));
        course.setOutline(request.getParameter("outline"));
        course.setPreferredQualifications(request.getParameter("preferredQualifications"));
        course.setCompensation(new BigDecimal(request.getParameter("compensation")));
        course.setStatus(CourseStatus.valueOf(request.getParameter("status")));
        
        String deadlineStr = request.getParameter("applicationDeadline");
        if (deadlineStr != null && !deadlineStr.isEmpty()) {
            course.setApplicationDeadline(dateFormat.parse(deadlineStr));
        }
        
        return course;
    }
}