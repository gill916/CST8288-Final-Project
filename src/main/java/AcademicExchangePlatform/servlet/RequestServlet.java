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


public class RequestServlet extends HttpServlet {

    private final RequestService requestService = RequestService.getInstance();

    private int getIntFromAttribute(HttpServletResponse response, Object attribute, String attributeName) throws IOException {
        if (attribute == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "The attribute '" + attributeName + "' is null.");
            return -1;
        }
        if (!(attribute instanceof String)) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "The attribute '" + attributeName + "' is not a type of String.");
            return -1;
        }
        try {
            return Integer.parseInt((String) attribute);
        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "The attribute '" + attributeName + "' cannot be parsed to an int.");
            return -1;
        }
    }

    private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.sendError(statusCode, message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String userType = getStringAttribute(request, "userType");
            int userId = getIntFromAttribute(response, request.getAttribute("userId"), "userId");
            int courseId = getIntFromAttribute(response, request.getAttribute("courseId"), "courseId");

            if (userId == -1 || courseId == -1) return;

            String targetPage = determineTargetPage(userType);
            if (targetPage == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Target page cannot be determined.");
                return;
            }

            if (targetPage.contains("myRequests.jsp")) {
                handleRequestPage(request, response, userId);
            } else if (targetPage.contains("manage.jsp")) {
                handleDecisionPage(request, response, courseId);
            }
        } catch (IllegalArgumentException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private String getStringAttribute(HttpServletRequest request, String attributeName) {
        Object attribute = request.getAttribute(attributeName);
        if (attribute == null || !(attribute instanceof String)) {
            throw new IllegalArgumentException("Invalid or missing attribute: " + attributeName);
        }
        return (String) attribute;
    }

    private String determineTargetPage(String userType) {
        if ("AcademicProfessional".equals(userType)) {
            return "/WEB-INF/views/request/myRequests.jsp";
        } else if ("AcademicInstitution".equals(userType)) {
            return "/WEB-INF/views/decision/manage.jsp";
        }
        return null;
    }

    private void handleRequestPage(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        try {
            List<Request> requestsByUserId = this.requestService.getRequestByUserId(userId);
            List<Request> paginatedRequests = paginateRequests(request, requestsByUserId);
            request.setAttribute("requestsByUserId", paginatedRequests);
            request.getRequestDispatcher("/WEB-INF/views/request/myRequests.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve requests.");
        }
    }

    private void handleDecisionPage(HttpServletRequest request, HttpServletResponse response, int courseId)
            throws ServletException, IOException {
        try {
            List<Request> requestsByCourseId = this.requestService.getRequestByCourse(courseId);
            List<Request> paginatedRequests = paginateRequests(request, requestsByCourseId);
            request.setAttribute("decisions", paginatedRequests);
            request.getRequestDispatcher("/WEB-INF/views/decision/manage.jsp").forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve requests.");
        }
    }

    private List<Request> paginateRequests(HttpServletRequest request, List<Request> requests) {
        int pageLimit = (requests.size() + 9) / 10;
        int page = 0;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ignored) {
        }

        if (page >= pageLimit) {
            page = pageLimit - 1;
        }

        int start = page * 10;
        int end = Math.min(start + 10, requests.size());
        return new ArrayList<>(requests.subList(start, end));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        try {
            if ("cancel".equals(action)) {
                handleCancelAction(request, response);
            } else if ("accept".equals(action)) {
                handleRequestStatusChange(request, response, "Accepted");
            } else if ("reject".equals(action)) {
                handleRequestStatusChange(request, response, "Rejected");
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    private void handleCancelAction(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException, ClassNotFoundException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        this.requestService.cancelRequestById(requestId);
        response.sendRedirect("/Requests");
    }

    private void handleRequestStatusChange(HttpServletRequest request, HttpServletResponse response, String status)
            throws IOException, SQLException, ClassNotFoundException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        this.requestService.handleRequest(requestId, status);
        response.sendRedirect("/Requests");
    }
}
