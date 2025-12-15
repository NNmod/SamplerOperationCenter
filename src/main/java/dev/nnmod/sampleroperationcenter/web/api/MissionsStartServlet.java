package dev.nnmod.sampleroperationcenter.web.api;

import dev.nnmod.sampleroperationcenter.application.service.interf.IDroneService;
import dev.nnmod.sampleroperationcenter.application.service.interf.IMissionService;
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

@WebServlet("/dashboard/missions/start")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class MissionsStartServlet extends HttpServlet {
    @EJB
    IMissionService missionService;
    @EJB
    IDroneService droneService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long missionId = Long.valueOf(request.getParameter("id"));
            Mission mission = missionService.startMission(missionId);
            droneService.setStatus(mission.getDrone().getId(), "active");
            request.setAttribute("mission", mission);
            response.sendRedirect("/dashboard/missions/view?id=" + missionId);
        } catch (Exception exception) {
            request.setAttribute("error", getMessage(exception));
            request.getRequestDispatcher("/dashboard_missions_view.jsp")
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
