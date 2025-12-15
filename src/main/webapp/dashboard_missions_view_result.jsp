<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard View Mission Result - Sampler Operation Center</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="flex flex-col gap-4 w-full min-h-screen bg-[rgba(45,45,51,1)] items-center justify-center">
<div class="z-40 fixed top-0 flex w-full h-16 p-2 bg-[rgba(43,45,48,1)] border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
    <a href="/dashboard/missions/view?id=<c:out value="${result.id}"/>" class="cursor-pointer">
        <button class="flex bg-blue-500 rounded-lg w-64 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
            <p class="text-black text-xl text-center">Back to Mission</p>
        </button>
    </a>
    <div class="flex grow items-center justify-center">
        <p class="text-white/90 text-3xl text-center pointer-events-none">Mission Result</p>
    </div>
    <div class="w-64"></div>
</div>
<form class="flex flex-col gap-2 mt-1 items-center justify-center bg-gray-200/20 rounded-xl py-2 pt-18 px-2" method="post" action="/dashboard/report/add">
    <input type="hidden" name="id" value="<c:out value="${result.id}"/>" />
    <div class="flex flex-col flex-grow w-64 items-center justify-center">
        <p class="flex w-full text-start text-md text-white/90">pH</p>
        <p class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400"><c:out value="${result.pH}"/></p>
    </div>
    <div class="flex flex-col flex-grow w-64 items-center justify-center">
        <p class="flex w-full text-start text-md text-white/90">Dissolved Oxygen</p>
        <p class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400"><c:out value="${result.dissolvedOxygen}"/></p>
    </div>
    <div class="flex flex-col flex-grow w-64 items-center justify-center">
        <p class="flex w-full text-start text-md text-white/90">Temperature</p>
        <p class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400"><c:out value="${result.temp}"/></p>
    </div>
    <div class="flex flex-col flex-grow w-64 items-center justify-center">
        <p class="flex w-full text-start text-md text-white/90">Turbidity</p>
        <p class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400"><c:out value="${result.turbidity}"/></p>
    </div>
    <div class="flex flex-col flex-grow w-64 items-center justify-center">
        <p class="flex w-full text-start text-md text-white/90">Conductivity</p>
        <p class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400"><c:out value="${result.conductivity}"/></p>
    </div>
    <div class="flex flex-col flex-grow w-64 items-center justify-center">
        <p class="flex w-full text-start text-md text-white/90">report</p>
        <input class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400" type="text" name="report" required value="<c:out value="${result.probeReport}"/>" />
    </div>
    <c:if test="${not empty error}">
        <p class="error text-red-500"><c:out value="${error}" /></p>
    </c:if>
    <c:if test="${empty result.probeReport}">
        <div class="flex flex-grow h-14 items-center justify-center">
            <div class="flex bg-blue-500 rounded-lg w-64 h-14 cursor-pointer hover:bg-blue-400 hover:drop-shadow-sm duration-300 cursor-pointer">
                <input class="flex-grow cursor-pointer" type="submit" value="Add Report" />
            </div>
        </div>
    </c:if>
</form>
</body>
</html>