package dev.nnmod.sampleroperationcenter.web.api;

import dev.nnmod.sampleroperationcenter.application.service.interf.IOperatorService;
import entity.Operator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @EJB
    IOperatorService operatorService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Operator created = operatorService.createOperator(login, password);
        request.setAttribute("operator", created);
        request.getRequestDispatcher("/register_success.jsp")
                .forward(request, response);
    }
}
