<%@ page import="entity.Telemetry" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard Add Drone - Sampler Operation Center</title>
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
<body class="flex flex-col w-full h-screen min-h-screen max-h-screen bg-[rgba(45,45,51,1)] items-center justify-center">
<div class="z-40 top-0 flex w-full h-16 p-2 bg-[rgba(43,45,48,1)] border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
  <a href="/dashboard/drones" class="cursor-pointer">
    <button class="flex bg-blue-500 rounded-lg w-64 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
      <p class="text-black text-xl text-center">Dashboard - Drones</p>
    </button>
  </a>
  <div class="flex grow items-center justify-center">
    <p class="text-white/90 text-3xl text-center pointer-events-none">Latest Drone Telemetry</p>
  </div>
  <div class="w-64"></div>
</div>
<div class="flex h-full w-full items-start justify-center">
  <div class="flex flex-col w-96 h-full items-center justify-center">
    <p class="text-white/90 text-3xl text-center pointer-events-none my-2">Drone Data</p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">id: <c:out value="${drone.id}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">name: <c:out value="${drone.name}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">status: <c:out value="${drone.status}"/></p>
    <p class="text-white/90 text-3xl text-center pointer-events-none my-2">Telemetry Data</p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">timestamp: <c:out value="${telemetry.createdAt}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">battery: <c:out value="${telemetry.batteryLevel}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90">speed: <c:out value="${telemetry.speed}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">thrust: <c:out value="${telemetry.thrust}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">lat: <c:out value="${telemetry.latitude}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">lng: <c:out value="${telemetry.longitude}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">alt: <c:out value="${telemetry.altitude}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">roll: <c:out value="${telemetry.roll}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">pitch: <c:out value="${telemetry.pitch}"/></p>
    <p class="flex text-start self-center justify-self-center text-md w-64 text-white/90 mb-2">yaw: <c:out value="${telemetry.yaw}"/></p>
  </div>
  <div class="flex w-full h-full">
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
  <%
      Telemetry telemetry = (Telemetry) request.getAttribute("telemetry");
      boolean hasTelemetry = telemetry != null;
  %>
  <% if (hasTelemetry) { %>
  const target = [<%= telemetry.getLatitude() %>, <%= telemetry.getLongitude() %>];
  <% } else { %>
  const target = null;
  <% } %>
  const map = L.map('map').setView(
          target ?? [50.45, 30.52],
          13
  );

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap'
  }).addTo(map);

  <% if (hasTelemetry) { %>
  const droneIcon = L.divIcon({
    className: 'drone-dot',
    iconSize: [14, 14],
    iconAnchor: [7, 7]
  });

  L.marker(target, { icon: droneIcon }).addTo(map);
  <% } %>
</script>
</body>
</html>