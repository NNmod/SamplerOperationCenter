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

@WebServlet("/dashboard/missions/view")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class MissionsViewServlet extends HttpServlet {
    @EJB
    IMissionService missionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            Long id = Long.valueOf(request.getParameter("id"));
            Mission mission = missionService.getOrThrow(id);
            request.setAttribute("mission", mission);
            request.getRequestDispatcher("/dashboard_missions_view.jsp")
                    .forward(request, response);
        }
        catch (Exception e) {
            response.sendRedirect("/dashboard/missions");
        }
    }
}
