package dev.nnmod.sampleroperationcenter.web.api;

import dev.nnmod.sampleroperationcenter.application.service.interf.IDroneService;
import dev.nnmod.sampleroperationcenter.application.service.interf.IOperatorService;
import dev.nnmod.sampleroperationcenter.application.service.interf.ITelemetryService;
import entity.Drone;
import entity.Operator;
import entity.Telemetry;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/dashboard/drones/telemetry")
@ServletSecurity(@HttpConstraint(rolesAllowed = "USER"))
public class DroneTelemetryServlet extends HttpServlet {
    @EJB
    IDroneService droneService;
    @EJB
    ITelemetryService telemetryService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long droneId = Long.valueOf(request.getParameter("id"));
        Drone drone = droneService.getOrThrow(droneId);
        Optional<Telemetry> telemetry = telemetryService.findByDroneId(droneId);
        request.setAttribute("drone", drone);
        if (telemetry.isPresent())
            request.setAttribute("telemetry", telemetry.get());
        else
            request.setAttribute("telemetry", null);
        request.getRequestDispatcher("/dashboard_drones_telemetry.jsp")
                .forward(request, response);
    }
}
