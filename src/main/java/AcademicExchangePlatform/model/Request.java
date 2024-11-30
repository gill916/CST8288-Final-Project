package AcademicExchangePlatform.model;
import java.util.Date;

public class Request {
    private int requestId;
    private int courseId;
    private int professionalId;
    private String status;
    private Date requestDate;
    private Date decisionDate;

    public Request(
        int requestId, 
        int courseId, 
        int professionalId, 
        String status, 
        Date requestDate,
        Date decisionDate
    ) {
        this.requestId = requestId;
        this.courseId = courseId;
        this.professionalId = professionalId;
        this.status = status;
        this.requestDate = requestDate;
        this.decisionDate = decisionDate;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getProfessionalId() {
        return this.professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getDecisionDate() {
        return this.decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public boolean submitRequest(int courseId, int professionalId){
        return false;
    }

    public boolean updateStatus(int requestId, String status){
        return false;
    }
}
