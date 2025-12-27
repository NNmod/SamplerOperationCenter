<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Add Mission - Sampler Operation Center</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet"
          href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />

    <!-- Give height to the map -->
    <style>
        #map {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body class="flex flex-col w-full h-screen min-h-screen bg-white invert items-center justify-center">
<div class="z-40 top-0 flex w-full h-16 p-2 bg-[rgba(43,45,48,1)] border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
    <a href="/dashboard/missions" class="cursor-pointer">
        <button class="flex bg-blue-500 rounded-lg w-64 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
            <p class="text-black text-xl text-center">Dashboard - Missions</p>
        </button>
    </a>
    <div class="flex grow items-center justify-center">
        <p class="text-white/90 text-3xl text-center pointer-events-none">New Mission</p>
    </div>
    <div class="w-64"></div>
</div>
<div class="flex h-full w-full items-center justify-center">
    <div class="'flex flex-col w-96 items-center justify-center">
        <form class="flex flex-col gap-2 mt-1 mx-4 items-center justify-center bg-gray-200/20 rounded-xl py-2 px-2" method="post" action="/dashboard/missions/add">
            <div class="flex flex-col flex-grow w-64 items-center justify-center">
                <p class="flex w-full text-start text-md text-white/90">drone name</p>
                <input class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400" type="text" name="drone" required />
            </div>
            <div class="flex flex-col flex-grow w-64 items-center justify-center">
                <p class="flex w-full text-start text-md text-white/90">latitude</p>
                <input class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400" type="text" id="lat" name="lat" required />
            </div>
            <div class="flex flex-col flex-grow w-64 items-center justify-center">
                <p class="flex w-full text-start text-md text-white/90">longitude</p>
                <input class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400" type="text" id="lng" name="lng" required />
            </div>
            <c:if test="${not empty error}">
                <p class="error text-red-500"><c:out value="${error}" /></p>
            </c:if>
            <div class="flex flex-grow h-14 items-center justify-center">
                <div class="flex bg-blue-500 rounded-lg w-64 h-14 cursor-pointer hover:bg-blue-400 hover:drop-shadow-sm duration-300 cursor-pointer">
                    <input class="flex-grow cursor-pointer" type="submit" value="Add Mission" />
                </div>
            </div>
        </form>
    </div>
    <div class="flex w-full h-full invert">
        <div id="map"></div>
    </div>
</div>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<script>
    // 1. Create a map centered at a location:
    const map = L.map('map').setView([50.4501, 30.5234], 13);

    // 2. Add OpenStreetMap tiles:
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap'
    }).addTo(map);

    let marker;
    map.on('click', function (e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        if (marker) {
            map.removeLayer(marker);
        }

        marker = L.marker([lat, lng]).addTo(map);
        document.getElementById('lat').value = lat;
        document.getElementById('lng').value = lng;
    });
</script>
</body>
</html>