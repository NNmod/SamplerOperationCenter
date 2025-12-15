package dev.nnmod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class DroneSimulator {

    private static final double EARTH_RADIUS_M = 6371000.0;
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final StartCommand cmd;
    private final TelemetrySender sender;

    public DroneSimulator(StartCommand cmd, TelemetrySender sender) {
        this.cmd = cmd;
        this.sender = sender;
    }

    public void run() {
        double[] home = randomPointAround(cmd.lat, cmd.lng, 1000, 3000);
        double homeLat = home[0];
        double homeLng = home[1];

        double lat = homeLat;
        double lng = homeLng;
        double alt = 0.0;
        double speed = 0.0;
        double yaw = 0.0;
        double roll = 0.0;
        double pitch = 0.0;
        double thrust = 0.0;
        double battery = randomRange(90.0, 100.0);

        try {
            int takeoffSeconds = 5;
            for (int i = 0; i < takeoffSeconds; i++) {
                double ratio = (i + 1) / (double) takeoffSeconds;
                alt = 5.0 * ratio;
                thrust = 0.5 + 0.2 * ratio;
                speed = 0.0;
                roll = smallNoise(0.0, 0.02);
                pitch = smallNoise(0.0, 0.02);

                battery -= 0.02;
                sendTelemetry("TAKEOFF", lat, lng, alt, speed,
                        roll, pitch, yaw, thrust, battery, null);
                sleep1s();
            }

            double targetLat = cmd.lat;
            double targetLng = cmd.lng;
            double cruiseSpeed = 10.0;
            double arrivalTolerance = 1.0;

            while (true) {
                double distance = distanceMeters(lat, lng, targetLat, targetLng);
                if (distance <= arrivalTolerance) break;

                double step = Math.min(cruiseSpeed, distance);
                double bearing = bearingRad(lat, lng, targetLat, targetLng);
                bearing += smallNoise(0.0, Math.toRadians(2));

                double[] newPos = move(lat, lng, bearing, step);
                double prevLat = lat;
                double prevLng = lng;

                lat = newPos[0];
                lng = newPos[1];
                alt = 5.0 + smallNoise(0.0, 0.2);
                yaw = bearing;
                roll = smallNoise(0.0, 0.05);
                pitch = smallNoise(0.0, 0.05);
                thrust = 0.6 + smallNoise(0.0, 0.05);
                speed = distanceMeters(prevLat, prevLng, lat, lng);
                battery -= step * 0.0005;

                sendTelemetry("FLYING", lat, lng, alt, speed,
                        roll, pitch, yaw, thrust, battery, null);
                sleep1s();
            }

            int hoverTargetSec = 15;
            for (int i = 0; i < hoverTargetSec; i++) {
                lat = cmd.lat + smallNoise(0.0, 0.00001);
                lng = cmd.lng + smallNoise(0.0, 0.00001);
                alt = 5.0 + smallNoise(0.0, 0.2);
                speed = smallNoise(0.0, 0.1);
                yaw += smallNoise(0.0, 0.02);
                roll = smallNoise(0.0, 0.03);
                pitch = smallNoise(0.0, 0.03);
                thrust = 0.55 + smallNoise(0.0, 0.03);

                battery -= 0.02;
                sendTelemetry("HOVER_TARGET", lat, lng, alt, speed,
                        roll, pitch, yaw, thrust, battery, null);
                sleep1s();
            }

            ProbeData probe = randomProbeData();
            int probeHoverSec = 10;
            for (int i = 0; i < probeHoverSec; i++) {
                lat = cmd.lat + smallNoise(0.0, 0.00001);
                lng = cmd.lng + smallNoise(0.0, 0.00001);
                alt = 5.0 + smallNoise(0.0, 0.2);
                speed = smallNoise(0.0, 0.1);
                yaw += smallNoise(0.0, 0.02);
                roll = smallNoise(0.0, 0.03);
                pitch = smallNoise(0.0, 0.03);
                thrust = 0.55 + smallNoise(0.0, 0.03);

                battery -= 0.02;
                sendTelemetry("PROBE", lat, lng, alt, speed,
                        roll, pitch, yaw, thrust, battery, probe);
                sleep1s();
            }

            while (true) {
                double distance = distanceMeters(lat, lng, homeLat, homeLng);
                if (distance <= arrivalTolerance) break;

                double step = Math.min(cruiseSpeed, distance);
                double bearing = bearingRad(lat, lng, homeLat, homeLng);
                bearing += smallNoise(0.0, Math.toRadians(2));

                double[] newPos = move(lat, lng, bearing, step);
                double prevLat = lat;
                double prevLng = lng;

                lat = newPos[0];
                lng = newPos[1];
                alt = 5.0 + smallNoise(0.0, 0.2);
                yaw = bearing;
                roll = smallNoise(0.0, 0.05);
                pitch = smallNoise(0.0, 0.05);
                thrust = 0.6 + smallNoise(0.0, 0.05);

                speed = distanceMeters(prevLat, prevLng, lat, lng);
                battery -= step * 0.0005;

                sendTelemetry("RETURNING", lat, lng, alt, speed,
                        roll, pitch, yaw, thrust, battery, probe);
                sleep1s();
            }

            int landingSec = 5;
            for (int i = 0; i < landingSec; i++) {
                double ratio = 1.0 - (i + 1) / (double) landingSec;
                alt = 5.0 * ratio;
                thrust = 0.6 * ratio;
                speed = smallNoise(0.0, 0.1);
                roll = smallNoise(0.0, 0.03);
                pitch = smallNoise(0.0, 0.03);

                battery -= 0.01;
                sendTelemetry("LANDING", lat, lng, alt, speed,
                        roll, pitch, yaw, thrust, battery, probe);
                sleep1s();
            }

            alt = 0.0;
            thrust = 0.0;
            speed = 0.0;
            sendTelemetry("DONE", lat, lng, alt, speed,
                    0.0, 0.0, yaw, thrust, battery, probe);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface TelemetrySender {
        void sendJson(String missionId, String json);
    }

    public static class ProbeData {
        public double pH;
        public double DO;
        public double temp;
        public double turbidity;
        public double conductivity;
    }

    private void sendTelemetry(String phase,
                               double lat,
                               double lng,
                               double alt,
                               double speed,
                               double roll,
                               double pitch,
                               double yaw,
                               double thrust,
                               double battery,
                               ProbeData probe) throws Exception {

        TelemetryPayload p = new TelemetryPayload();
        p.missionId = cmd.missionId;
        p.drone = cmd.drone;
        p.latitude = lat;
        p.longitude = lng;
        p.altitude = alt;
        p.speed = speed;
        p.roll = roll;
        p.pitch = pitch;
        p.yaw = yaw;
        p.thrust = thrust;
        p.batteryLevel = battery;
        p.timestamp = LocalDateTime.now();
        p.probe = probe;
        p.isLanded = Objects.equals(phase, "DONE");

        String json = MAPPER.writeValueAsString(p);
        sender.sendJson(cmd.missionId, json);
    }

    public static class TelemetryPayload {
        public String missionId;
        public String drone;
        public double latitude;
        public double longitude;
        public double altitude;
        public double speed;
        public double roll;
        public double pitch;
        public double yaw;
        public double thrust;
        public double batteryLevel;
        public LocalDateTime timestamp;
        public ProbeData probe;
        public Boolean isLanded;
    }

    private static void sleep1s() throws InterruptedException {
        Thread.sleep(1000);
    }

    private static double randomRange(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private static double smallNoise(double mean, double stdDev) {
        return mean + stdDev * ThreadLocalRandom.current().nextGaussian();
    }

    private static ProbeData randomProbeData() {
        ProbeData p = new ProbeData();
        p.pH = randomRange(6.5, 8.5);
        p.DO = randomRange(6.0, 12.0);
        p.temp = randomRange(5.0, 25.0);
        p.turbidity = randomRange(0.0, 10.0);
        p.conductivity = randomRange(100.0, 800.0);
        return p;
    }

    private static double[] randomPointAround(double latDeg, double lngDeg,
                                              double minDistM, double maxDistM) {
        double bearing = randomRange(0, 2 * Math.PI);
        double dist = randomRange(minDistM, maxDistM);
        return move(latDeg, lngDeg, bearing, dist);
    }

    private static double[] move(double latDeg, double lngDeg, double bearingRad, double distanceM) {
        double latRad = Math.toRadians(latDeg);
        double lngRad = Math.toRadians(lngDeg);

        double angDist = distanceM / EARTH_RADIUS_M;

        double newLatRad = Math.asin(
                Math.sin(latRad) * Math.cos(angDist) +
                        Math.cos(latRad) * Math.sin(angDist) * Math.cos(bearingRad)
        );

        double newLngRad = lngRad + Math.atan2(
                Math.sin(bearingRad) * Math.sin(angDist) * Math.cos(latRad),
                Math.cos(angDist) - Math.sin(latRad) * Math.sin(newLatRad)
        );

        return new double[]{Math.toDegrees(newLatRad), Math.toDegrees(newLngRad)};
    }

    private static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_M * c;
    }

    private static double bearingRad(double lat1, double lon1, double lat2, double lon2) {
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);
        double dLon = Math.toRadians(lon2 - lon1);

        double y = Math.sin(dLon) * Math.cos(rLat2);
        double x = Math.cos(rLat1) * Math.sin(rLat2) -
                Math.sin(rLat1) * Math.cos(rLat2) * Math.cos(dLon);

        return Math.atan2(y, x);
    }
}