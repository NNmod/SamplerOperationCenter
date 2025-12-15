package dev.nnmod.sampleroperationcenter.domain.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.nnmod.sampleroperationcenter.application.service.MissionService;
import dev.nnmod.sampleroperationcenter.application.service.TelemetryService;
import dev.nnmod.sampleroperationcenter.application.service.interf.IDroneService;
import dev.nnmod.sampleroperationcenter.application.service.interf.IMissionService;
import dev.nnmod.sampleroperationcenter.application.service.interf.ITelemetryService;
import dev.nnmod.sampleroperationcenter.domain.model.TelemetryDto;
import entity.Mission;
import entity.Telemetry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.inject.Inject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Singleton
@Startup
public class TelemetryConsumerBean {

    @Inject
    TelemetrySseHub sseHub;

    @EJB
    ITelemetryService telemetryService;

    @EJB
    IMissionService missionService;

    @EJB
    IDroneService droneService;

    @Resource
    ManagedExecutorService executor;

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private volatile boolean running = true;

    @PostConstruct
    public void init() {
        executor.execute(this::pollLoop);
        System.out.println("[TelemetryConsumer] Started.");
    }

    private void pollLoop() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "drone-telemetry-consumer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("drone-telemetry"));
            System.out.println("[TelemetryConsumer] Subscribed to drone-telemetry.");

            while (running) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                if (!records.isEmpty()) {
                    System.out.println("[TelemetryConsumer] Got " + records.count() + " record(s).");
                }

                for (ConsumerRecord<String, String> record : records) {
                    try {
                        handleRecord(record);
                    } catch (Exception ex) {
                        System.err.println("[TelemetryConsumer] Error handling record: " + ex);
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("[TelemetryConsumer] FATAL error, exiting poll loop: " + ex);
            ex.printStackTrace();
        }
    }

    private void handleRecord(ConsumerRecord<String, String> record) throws Exception {
        String missionId = record.key();
        String payload = record.value();
        TelemetryDto dto = mapper.readValue(payload, TelemetryDto.class);

        if (dto.probe != null) {
            missionService.setResult(
                    dto.missionId,
                    dto.probe.pH,
                    dto.probe.DO,
                    dto.probe.temp,
                    dto.probe.turbidity,
                    dto.probe.conductivity
            );
        }

        if (dto.isLanded) {
            Mission mission = missionService.setFinishedAt(dto.missionId, dto.timestamp);
            droneService.setStatus(mission.getDrone().getId(), "inactive");
        }

        Telemetry telemetry = telemetryService.createTelemetry(
                dto.missionId,
                dto.drone,
                dto.batteryLevel,
                dto.altitude,
                dto.speed,
                dto.latitude,
                dto.longitude,
                dto.thrust,
                dto.roll,
                dto.pitch,
                dto.yaw,
                dto.timestamp
        );

        sseHub.broadcast(missionId, telemetry);
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("[TelemetryConsumer] Shutting down.");
        running = false;
    }
}