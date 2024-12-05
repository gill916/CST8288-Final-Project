package AcademicExchangePlatform.service;

import AcademicExchangePlatform.model.*;
import AcademicExchangePlatform.dao.ProfileDAO;
import java.util.List;

public class ProfileService {
    private static ProfileService instance;
    private final ProfileDAO profileDAO;

    private ProfileService() {
        this.profileDAO = ProfileDAO.getInstance();
    }

    public static ProfileService getInstance() {
        if (instance == null) {
            instance = new ProfileService();
        }
        return instance;
    }

    public boolean completeProfessionalProfile(int userId, 
                                             String position, 
                                             String currentInstitution,
                                             List<Education> educationList,
                                             List<String> expertise) {
        try {
            return profileDAO.updateProfessionalProfile(userId, position, 
                    currentInstitution, educationList, expertise);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean completeInstitutionProfile(int institutionId,
                                            String address,
                                            List<CourseOffering> courseOfferings) {
        try {
            return profileDAO.updateInstitutionProfile(institutionId, 
                    address, courseOfferings);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Profile getProfile(int userId) {
        return profileDAO.getProfile(userId);
    }
}
