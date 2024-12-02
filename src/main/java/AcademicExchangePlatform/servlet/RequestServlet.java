package AcademicExchangePlatform.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import AcademicExchangePlatform.model.Request;
import AcademicExchangePlatform.service.RequestService;

@WebServlet("/Requests")
public class RequestServlet extends HttpServlet{

    private int getIntFromAttribute(HttpServletResponse response, Object attributeObject, String attributeName) {
        try{
            if(attributeObject == null){
                throw new IllegalArgumentException("The attribute '" + attributeName + "' is null.");
            }

            if(!(attributeObject  instanceof String)){
                throw new ClassCastException("The attribute '" + attributeName + "' is not a type of String.");
            }
        } catch (ClassCastException e){
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "The attribute '" + attributeName + "' is not a type of String.");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (NumberFormatException e){
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "The attribute '" + attributeName + "' cannot pe parsed to int.");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return Integer.parseInt((String)attributeObject);
    }

    @Override
    public void doGet(
        HttpServletRequest request, 
        HttpServletResponse response)
        throws
            IOException,
            ServletException
    {
        RequestService requestService = new RequestService();

        String userTypeAttributeName = "userType";
        String userIdAttributeName = "userId";
        String courseTypeAttribteName = "courseId";

        Object userTypeAttributeObject = request.getAttribute(userTypeAttributeName);
        Object userIdAttributeObject = request.getAttribute(userIdAttributeName);
        Object courseIdAttributObject = request.getAttribute(courseTypeAttribteName);

        String userType = null;
        int courseId = -1;
        int userId = -1;

        String targetPage;

        try{
            if(userTypeAttributeObject == null){
                throw new IllegalArgumentException("The attribute '" + userTypeAttributeName + "' is null.");
            }

            if(!(userTypeAttributeObject instanceof String)){
                throw new ClassCastException("The attribute '" + userTypeAttributeName + "' is not a type of String.");
            }
            userType = (String) userTypeAttributeObject;
        } catch (ClassCastException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The attribute '" + userTypeAttributeName + "' is not a type of String.");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

        courseId = getIntFromAttribute(response, courseIdAttributObject, courseTypeAttribteName);
        userId = getIntFromAttribute(response, userIdAttributeObject, userIdAttributeName);

        targetPage = userType.equals("AcademicProfessional") ? "request.jsp" : userType.equals("AcademicInstitution") ? "decision.jsp" : null;



        if(targetPage.equals("request.jsp")){
            try {
                List<Request> requestsByUserId = requestService.getRequestByUserId(userId);
                int pageLimit = (requestsByUserId.size()+9)/10;
                int page;
                if(request.getParameter("page") == null){
                    page = 0;
                } else {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                
                if(page >= pageLimit){
                    response.sendRedirect("/Requests?page="+(pageLimit-1));
                    return;
                }

                List<Request> paginatedRequests = new ArrayList<Request>();
                
                int i = page*10;
                int end = (page+1)*10;
                while(i < requestsByUserId.size() && i < end){
                    paginatedRequests.add(requestsByUserId.get(i));
                    i++;
                }
                
                request.setAttribute("requestsByUserId", paginatedRequests);
            } catch (ClassNotFoundException | SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher(targetPage).forward(request, response);



        } else if(targetPage.equals("decision.jsp")){
            try {
                List<Request> requestsByCourseId = requestService.getRequestByCourse(courseId);
                int pageLimit = (requestsByCourseId.size()+9)/10;
                int page;
                if(request.getParameter("page") == null){
                    page = 0;
                } else {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                if(page >= pageLimit){
                    response.sendRedirect("/Requests?page="+(pageLimit-1));
                    return;
                }

                List<Request> paginatedRequests = new ArrayList<Request>();

                int i = page*10;
                int end = (page+1)*10;
                while(i < requestsByCourseId.size() && i < end){
                    paginatedRequests.add(requestsByCourseId.get(i));
                    i++;
                }
                request.setAttribute("requestsByCourseId", paginatedRequests);
            } catch (ClassNotFoundException | SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher(targetPage).forward(request, response);;



        } else {   
            throw new IllegalArgumentException("Target page cannot be determined.");
        }
    }

    @Override
    public void doPost(
        HttpServletRequest request, 
        HttpServletResponse response)
        throws
            IOException,
            ServletException
    {
        String action = request.getParameter("action");
        RequestService requestService = new RequestService();

        if("cancel".equals(action)){
            String requestIdParam = request.getParameter("requestId");
            try{
                int requestId = Integer.parseInt(requestIdParam);
                requestService.cancelRequestById(requestId);
                response.sendRedirect("/Requests");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID.");
            } catch (ClassNotFoundException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database driver not found.");
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to cancel the request.");
            }
        } else if ("accept".equals(action)){
            String requestIdParam = request.getParameter("requestId");
            try{
                int requestId = Integer.parseInt(requestIdParam);
                requestService.handleRequest(requestId, "Accepted");
                response.sendRedirect("/Requests");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID.");
            } catch (ClassNotFoundException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database driver not found.");
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to change the request status.");
            }
        } else if ("reject".equals(action)){
            String requestIdParam = request.getParameter("requestId");
            try{
                int requestId = Integer.parseInt(requestIdParam);
                requestService.handleRequest(requestId, "Rejected");
                response.sendRedirect("/Requests");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID.");
            } catch (ClassNotFoundException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database driver not found.");
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to change the request status.");
            }
        } else {
            doGet(request, response);
        }
    }
}
