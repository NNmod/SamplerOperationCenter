<%@ page import="entity.Telemetry" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Mission" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Mission View - Sampler Operation Center</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet"
          href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />

    <style>
        #map {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body class="flex flex-col w-full h-screen min-h-screen max-h-screen bg-white invert items-center justify-center">
<div class="z-40 top-0 flex w-full h-16 p-2 bg-[rgba(43,45,48,1)] border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
    <a href="/dashboard/missions" class="cursor-pointer">
        <button class="flex bg-blue-500 rounded-lg w-64 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
            <p class="text-black text-xl text-center">Dashboard - Missions</p>
        </button>
    </a>
    <div class="flex grow items-center justify-center">
        <p class="text-white/90 text-3xl text-center pointer-events-none">Mission View</p>
    </div>
    <div class="w-64"></div>
</div>
<div class="flex h-full w-full items-start justify-center">
    <div class="flex flex-col w-96 h-full items-center justify-center">
        <p class="text-white/90 text-3xl text-center pointer-events-none my-2">Mission Data</p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">id: <c:out value="${mission.id}"/></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">operator: <c:out value="${mission.operator.login}"/></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">drone: <c:out value="${mission.drone.name}"/></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">status:             <c:choose>
            <c:when test="${not empty mission.finishedAt}">finished</c:when>
            <c:when test="${not empty mission.probeAt}">returning</c:when>
            <c:when test="${not empty mission.startedAt}">flying to the position</c:when>
            <c:otherwise>waiting to start</c:otherwise>
        </c:choose></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">created: <c:out value="${mission.createdAt}"/></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">started: <c:out value="${mission.startedAt}"/></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">probe: <c:out value="${mission.probeAt}"/></p>
        <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">finished: <c:out value="${mission.finishedAt}"/></p>
        <c:if test="${not empty mission.result}">
            <a href="/dashboard/missions/view/result?id=<c:out value="${mission.id}"/>" class="cursor-pointer flex w-fit h-14 items-center justify-center">
                <button class="flex bg-yellow-500 rounded-lg w-64 h-14 h-full text-white items-center justify-center hover:bg-yellow-400 hover:drop-shadow-sm duration-300">
                    <p class="text-black text-xl text-center">View result</p>
                </button>
            </a>
        </c:if>
        <c:if test="${empty mission.startedAt}">
            <form class="flex w-fit h-14 items-center justify-center" method="post" action="/dashboard/missions/start">
                <input type="hidden" name="id" value="<c:out value="${mission.id}"/>" />
                <div class="flex bg-blue-500 rounded-lg w-64 h-14 cursor-pointer hover:bg-blue-400 hover:drop-shadow-sm duration-300 cursor-pointer">
                    <input class="flex-grow cursor-pointer" type="submit" value="Start Mission" />
                </div>
            </form>
        </c:if>
        <div class="relative flex w-full mt-2 grow bg-black overflow-y-auto">
            <div class="absolute top-0 flex flex-col w-full" id="telemetry-log">
                <c:forEach var="telemetry" items="${mission.telemetries}">
                    <p class="text-start text-white">[<c:out value="${telemetry.createdAt}"/>] battery: <c:out value="${telemetry.batteryLevel}"/>, speed: <c:out value="${telemetry.speed}"/>, thrust <c:out value="${telemetry.thrust}"/>, lat: <c:out value="${telemetry.latitude}"/>, lng: <c:out value="${telemetry.longitude}"/>, alt: <c:out value="${telemetry.altitude}"/>, roll: <c:out value="${telemetry.roll}"/>, pitch: <c:out value="${telemetry.pitch}"/>, yaw: <c:out value="${telemetry.yaw}"/></p>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="flex w-full h-full invert">
        <div id="map"></div>
    </div>
</div>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<style>
    .drone-dot {
        width: 14px;
        height: 14px;
        background: yellow;
        border-radius: 50%;
        border: 2px solid #333;
        box-shadow: 0 0 8px rgba(255, 255, 0, 0.9);
        animation: drone-blink 1s infinite alternate;
    }

    @keyframes drone-blink {
        0% {
            opacity: 0.4;
            box-shadow: 0 0 4px rgba(255, 255, 0, 0.5);
        }
        100% {
            opacity: 1;
            box-shadow: 0 0 12px rgba(255, 255, 0, 1);
        }
    }
</style>
<script>
    <%Mission mission = (Mission)request.getAttribute("mission");%>
    const path = [
        <%
            for (Telemetry t : mission.getTelemetries()) {
        %>
        [<%= t.getLatitude() %>, <%= t.getLongitude() %>],
        <% } %>
    ];
    const target = [<%= mission.getLatitude() %>, <%= mission.getLongitude() %>]

    const map = L.map('map').setView(target, 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap'
    }).addTo(map);

    const polyline = L.polyline(path, { color: 'red' }).addTo(map);

    L.marker(target).addTo(map).bindPopup("Target");

    let droneMarker = null;
    const droneIcon = L.divIcon({
        className: 'drone-dot',
        iconSize: [14, 14],
        iconAnchor: [7, 7]
    });
    const telemetryLog = document.getElementById('telemetry-log');
    const source = new EventSource('<c:url value="/api/missions/${mission.id}/stream"/>');
    source.addEventListener("telemetry", function(e) {
        const data = JSON.parse(e.data);
        const point = [data.latitude, data.longitude];

        path.push(point);
        polyline.setLatLngs(path);

        if (!droneMarker) {
            droneMarker = L.marker([data.latitude, data.longitude], { icon: droneIcon }).addTo(map);
        } else {
            droneMarker.setLatLng([data.latitude, data.longitude]);
        }

        const p = document.createElement('p');
        p.className = 'text-start text-white';
        p.textContent =
            "[" + data.createdAt + "] " +
            "battery: " + data.batteryLevel + ", " +
            "speed: " + data.speed + ", " +
            "thrust: " + data.thrust + ", " +
            "lat: " + data.latitude + ", lng: " + data.longitude + ", " +
            "alt: " + data.altitude + ", " +
            "roll: " + data.roll + ", pitch: " + data.pitch + ", yaw: " + data.yaw;

        telemetryLog.appendChild(p);
    });
</script>
</body>
</html>