package dev.nnmod.sampleroperationcenter.domain.beans;

import entity.Telemetry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/missions")
@ApplicationScoped
public class TelemetrySseHub {
    @Context
    Sse sse;

    private final Map<String, SseBroadcaster> broadcasters = new ConcurrentHashMap<>();

    @GET
    @Path("{missionId}/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void stream(@PathParam("missionId") String missionId,
                       @Context SseEventSink sink) {
        SseBroadcaster broadcaster =
                broadcasters.computeIfAbsent(missionId, id -> sse.newBroadcaster());
        broadcaster.register(sink);
    }

    public void broadcast(String missionId, Telemetry telemetry) {
        SseBroadcaster broadcaster = broadcasters.get(missionId);
        if (broadcaster == null) return;

        OutboundSseEvent event = sse.newEventBuilder()
                .name("telemetry")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(Telemetry.class, telemetry)
                .build();

        broadcaster.broadcast(event);
    }
}