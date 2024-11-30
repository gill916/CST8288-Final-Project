package AcademicExchangePlatform.servlet;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Requests")
public class RequestServlet extends HttpServlet{

    @Override
    public void doGet(
        HttpServletRequest request, 
        HttpServletResponse response)
        throws
            IOException,
            ServletException
    {
        
    }

    @Override
    public void doPost(
        HttpServletRequest request, 
        HttpServletResponse response)
        throws
            IOException,
            ServletException
    {
        doGet(request, response);
    }
}
