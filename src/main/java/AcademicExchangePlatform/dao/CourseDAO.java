package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.Course;
import java.util.List;

public interface CourseDAO {
    void createCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(int courseId);
    Course getCourseById(int courseId);
    List<Course> getAllCourses();
}
