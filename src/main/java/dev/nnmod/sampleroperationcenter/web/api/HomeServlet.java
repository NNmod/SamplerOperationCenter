package dev.nnmod.sampleroperationcenter.web.api;

import dev.nnmod.sampleroperationcenter.application.service.interf.IOperatorService;
import entity.Operator;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("")
@ServletSecurity(@HttpConstraint(value = ServletSecurity.EmptyRoleSemantic.PERMIT))
public class HomeServlet extends HttpServlet {
    @EJB
    IOperatorService operatorService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getUserPrincipal() != null) {
            String login = request.getUserPrincipal().getName();
            Operator operator = operatorService.getByLoginOrThrow(login);
            request.setAttribute("operator", operator);
            request.getRequestDispatcher("/home.jsp")
                    .forward(request, response);
        } else {
            request.setAttribute("operator", null);
            request.getRequestDispatcher("/home.jsp")
                    .forward(request, response);
        }
    }
}
