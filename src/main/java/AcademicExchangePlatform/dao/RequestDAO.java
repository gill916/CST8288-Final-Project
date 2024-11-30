package AcademicExchangePlatform.dao;
import java.util.List;
import AcademicExchangePlatform.model.Request;

public interface RequestDAO {
    public boolean addRequest(Request request);

    public Request getRequestById(int requestId);

    public List<Request> getRequestByCourse(int courseId);

    public boolean updateRequestStatus(int requestId, String status);
}
