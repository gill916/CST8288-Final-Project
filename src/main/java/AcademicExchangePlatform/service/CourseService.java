package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.CourseDAO;
import AcademicExchangePlatform.dao.CourseDAOImpl;
import AcademicExchangePlatform.model.Course;

import java.util.List;
import java.util.stream.Collectors;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAOImpl();

    public void createCourse(Course course) {
        courseDAO.createCourse(course);
    }

    public void updateCourse(Course course) {
        courseDAO.updateCourse(course);
    }

    public void deleteCourse(int courseId) {
        courseDAO.deleteCourse(courseId);
    }

    public Course getCourseById(int courseId) {
        return courseDAO.getCourseById(courseId);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }
    public List<Course> getCoursesByInstitutionId(int institutionId) {
        return courseDAO.getAllCourses()
                .stream()
                .filter(course -> course.getInstitutionId() == institutionId)
                .collect(Collectors.toList());
    }


}
