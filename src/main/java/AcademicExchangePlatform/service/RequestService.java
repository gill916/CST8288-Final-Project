package AcademicExchangePlatform.service;
import AcademicExchangePlatform.dao.*;
import AcademicExchangePlatform.model.*;

import java.sql.SQLException;
import java.util.List;

public class RequestService {
    public boolean submitRequest(Request request) throws ClassNotFoundException, SQLException{
        RequestDAO dao = RequestDAOImpl.getInstance();
        return dao.addRequest(request);
    }

    public List<Request> getRequestByCourse(int courseId) throws ClassNotFoundException, SQLException{
        RequestDAO dao = RequestDAOImpl.getInstance();
        return dao.getRequestByCourse(courseId);
    }

    public boolean handleRequest(int requestId, String status) throws ClassNotFoundException, SQLException{
        RequestDAO dao = RequestDAOImpl.getInstance();
        return dao.updateRequestStatus(requestId, status);
    }
}
