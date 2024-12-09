package AcademicExchangePlatform.model;

import AcademicExchangePlatform.dbenum.ApplicationStatus;
import java.util.Date;

/**
 * Represents an application for a course.
 * Contains details about the application, including course, professional, status, and related documents.
 */
public class CourseApplication {
    private int applicationId;
    private int courseId;
    private int professionalId;
    private Date decisionDate;
    private Date applicationDate;
    private ApplicationStatus status;
    private String coverLetter;
    private String additionalDocuments;
    private Course course;
    private String institutionName;
    private AcademicProfessional professional;

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getAdditionalDocuments() {
        return additionalDocuments;
    }

    public void setAdditionalDocuments(String additionalDocuments) {
        this.additionalDocuments = additionalDocuments;
    }
    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public AcademicProfessional getProfessional() {
        return professional;
    }

    public void setProfessional(AcademicProfessional professional) {
        this.professional = professional;
    }

} 