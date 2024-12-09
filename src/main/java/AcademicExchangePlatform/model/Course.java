package AcademicExchangePlatform.model;

import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents a course offered by an institution.
 */
public class Course {
    private int courseId;
    private String courseTitle;
    private String courseCode;
    private String term;
    private Schedule schedule; // Enum for Schedule
    private DeliveryMethod deliveryMethod; // Enum for Delivery Method
    private String outline;
    private String preferredQualifications;
    private BigDecimal compensation;
    private int institutionId;
    private CourseStatus status;
    private Date applicationDeadline;
    private String institutionName;

    // Getters and Setters
    /**
     * Gets the ID of the course.
     * @return The ID of the course
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Sets the ID of the course.
     * @param courseId The ID of the course
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the title of the course.
     * @return The title of the course
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * Sets the title of the course.
     * @param courseTitle The title of the course
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * Gets the code of the course.
     * @return The code of the course
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Sets the code of the course.
     * @param courseCode The code of the course
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Gets the term of the course.
     * @return The term of the course
     */
    public String getTerm() {
        return term;
    }

    /**
     * Sets the term of the course.
     * @param term The term of the course
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Gets the schedule of the course.
     * @return The schedule of the course
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Sets the schedule of the course.
     * @param schedule The schedule of the course
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Gets the delivery method of the course.
     * @return The delivery method of the course
     */
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the delivery method of the course.
     * @param deliveryMethod The delivery method of the course
     */
    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
    
    /**
     * Gets the outline of the course.
     * @return The outline of the course
     */
    public String getOutline() {
        return outline;
    }

    /**
     * Sets the outline of the course.
     * @param outline The outline of the course
     */
    public void setOutline(String outline) {
        this.outline = outline;
    }

    /**
     * Gets the preferred qualifications of the course.
     * @return The preferred qualifications of the course
     */
    public String getPreferredQualifications() {
        return preferredQualifications;
    }

    /**
     * Sets the preferred qualifications of the course.
     * @param preferredQualifications The preferred qualifications of the course
     */
    public void setPreferredQualifications(String preferredQualifications) {
        this.preferredQualifications = preferredQualifications;
    }

    /**
     * Gets the compensation of the course.
     * @return The compensation of the course
     */
    public BigDecimal getCompensation() {
        return compensation;
    }

    /**
     * Sets the compensation of the course.
     * @param compensation The compensation of the course
     */
    public void setCompensation(BigDecimal compensation) {
        this.compensation = compensation;
    }

    /**
     * Gets the institution ID of the course.
     * @return The institution ID of the course
     */
    public int getInstitutionId() {
        return institutionId;
    }

    /**
     * Sets the institution ID of the course.
     * @param institutionId The institution ID of the course
     */
    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    /**
     * Gets the status of the course.
     * @return The status of the course
     */
    public CourseStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the course.
     * @param status The status of the course
     */
    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    /**
     * Gets the application deadline of the course.
     * @return The application deadline of the course
     */
    public Date getApplicationDeadline() {
        return applicationDeadline;
    }

    /**
     * Sets the application deadline of the course.
     * @param applicationDeadline The application deadline of the course
     */
    public void setApplicationDeadline(Date applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    /**
     * Gets the institution name of the course.
     * @return The institution name of the course
     */
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     * Sets the institution name of the course.
     * @param institutionName The institution name of the course
     */
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
