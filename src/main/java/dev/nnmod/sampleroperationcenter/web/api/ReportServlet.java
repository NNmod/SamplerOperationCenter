package dev.nnmod.sampleroperationcenter.web.api;

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

@WebServlet("/dashboard/report/add")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class ReportServlet extends HttpServlet {
    @EJB
    IMissionService missionService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long missionId = Long.valueOf(request.getParameter("id"));
            String text = request.getParameter("report");
            Mission mission = missionService.setProbeReport(missionId, text);
            request.setAttribute("result", mission.getResult());
            request.getRequestDispatcher("/dashboard_missions_view_result.jsp")
                    .forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", getMessage(exception));
            request.getRequestDispatcher("/dashboard_missions_view_result.jsp")
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
