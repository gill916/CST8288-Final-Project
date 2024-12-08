package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.service.CourseService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/course/view/*")
public class ViewCourseServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        int courseId = Integer.parseInt(pathInfo.substring(1));
        Course course = courseService.getCourseById(courseId);
        
        if (course == null) {
            response.sendRedirect("/courses");
            return;
        }
        
        request.setAttribute("course", course);
        request.setAttribute("userType", request.getSession().getAttribute("userType"));
        request.getRequestDispatcher("/WEB-INF/views/course/details.jsp")
               .forward(request, response);
    }
} 