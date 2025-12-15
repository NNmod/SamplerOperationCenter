package dev.nnmod.sampleroperationcenter.web.api;

import dev.nnmod.sampleroperationcenter.application.service.interf.IMissionService;
import entity.Drone;
import entity.Mission;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dashboard/missions/add")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class MissionsAddServlet extends HttpServlet {
    @EJB
    IMissionService missionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/dashboard_missions_add.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String login = request.getUserPrincipal().getName();
            String drone = request.getParameter("drone");
            Float latitude = Float.valueOf(request.getParameter("lat"));
            Float longitude = Float.valueOf(request.getParameter("lng"));
            Mission mission = missionService.createMission(login, drone, latitude, longitude);
            response.sendRedirect("/dashboard/missions");
        } catch (Exception exception) {
            request.setAttribute("error", getMessage(exception));
            request.getRequestDispatcher("/dashboard_missions_add.jsp")
                    .forward(request, response);
        }
    }

    private static String getMessage(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        String message;
        if (cause instanceof IllegalArgumentException && cause.getMessage() != null) {
            message = cause.getMessage();
        } else if (exception.getMessage() != null) {
            message = "Unknown error: " + exception.getMessage();
        } else {
            message = "Unknown error. Try again";
        }
        return message;
    }
}
