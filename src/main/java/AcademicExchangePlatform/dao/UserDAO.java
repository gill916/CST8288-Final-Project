package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.AcademicInstitution;
import AcademicExchangePlatform.model.AcademicProfessional;
import java.util.List;

public interface UserDAO {
    boolean addUser(User user);
    User getUserByEmail(String email);
    boolean updateUser(User user);
    List<AcademicInstitution> getAllInstitutions();
    User getUserById(int userId);
    List<AcademicProfessional> getAllProfessionals();
}
