package dev.nnmod.sampleroperationcenter.web.api;

import dev.nnmod.sampleroperationcenter.application.service.interf.IDroneService;
import dev.nnmod.sampleroperationcenter.application.service.interf.IMissionService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dashboard/missions")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class MissionsServlet extends HttpServlet {
    @EJB
    IMissionService missionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pageParameter = request.getParameter("page");
        if (pageParameter == null)
            pageParameter = "0";
        int page = Integer.parseInt(pageParameter);
        Long entries = missionService.countAll();
        double pages = Math.ceil((double) entries / 9);
        request.setAttribute("page", page);
        request.setAttribute("pages", pages);
        request.setAttribute("missions", missionService.getPage(page,9));
        request.getRequestDispatcher("/dashboard_missions.jsp")
                .forward(request, response);
    }
}
