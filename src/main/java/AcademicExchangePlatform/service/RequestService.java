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

    public List<Request> getRequestByUserId(int userId) throws ClassNotFoundException, SQLException{
        RequestDAO dao = RequestDAOImpl.getInstance();
        return dao.getRequestByUserId(userId);
    }

    public boolean handleRequest(int requestId, String status) throws ClassNotFoundException, SQLException{
        RequestDAO dao = RequestDAOImpl.getInstance();
        return dao.updateRequestStatus(requestId, status);
    }

    public void cancelRequestById(int requestId) throws ClassNotFoundException, SQLException{
        RequestDAO dao = RequestDAOImpl.getInstance();
        dao.cancelRequestById(requestId);
    }
}
