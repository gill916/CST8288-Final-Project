package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.CourseBuilder;
import AcademicExchangePlatform.service.CourseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    private final CourseService courseService = new CourseService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            // Use CourseBuilder to construct the Course object
            Course course = new CourseBuilder()
                    .setCourseTitle(request.getParameter("courseTitle"))
                    .setCourseCode(request.getParameter("courseCode"))
                    .setTerm(request.getParameter("term"))
                    .setSchedule(Schedule.valueOf(request.getParameter("schedule")))
                    .setDeliveryMethod(DeliveryMethod.valueOf(request.getParameter("deliveryMethod")))
                    .setOutline(request.getParameter("outline"))
                    .setPreferredQualifications(request.getParameter("preferredQualifications"))
                    .setCompensation(new BigDecimal(request.getParameter("compensation")))
                    .setInstitutionId(Integer.parseInt(request.getParameter("institutionId")))
                    .build();

            courseService.createCourse(course);
        }
        response.sendRedirect("course.jsp");
    }


}
