package AcademicExchangePlatform.dao;

import Academ
import java.util.List;

public interface SearchDAO {
    List<Course> searchCourses(String keyword);
}