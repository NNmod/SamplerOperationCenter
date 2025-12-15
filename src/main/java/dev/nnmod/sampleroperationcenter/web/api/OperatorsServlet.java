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

@WebServlet("/dashboard/operators")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class OperatorsServlet extends HttpServlet {
    @EJB
    IOperatorService operatorService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("operators", operatorService.getAllOperators());
        request.getRequestDispatcher("/dashboard_operators.jsp")
                .forward(request, response);
    }
}
