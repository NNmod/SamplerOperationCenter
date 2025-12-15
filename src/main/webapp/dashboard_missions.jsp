<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard Missions - Sampler Operation Center</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="flex flex-col gap-4 w-full min-h-screen bg-[rgba(45,45,51,1)] items-center justify-start">
<div class="z-40 sticky top-0 flex w-full h-16 p-2 bg-[rgba(43,45,48,1)] border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
  <a href="/dashboard" class="cursor-pointer">
    <button class="flex bg-blue-500 rounded-lg w-64 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
      <p class="text-black text-xl text-center">Dashboard</p>
    </button>
  </a>
  <div class="flex grow items-center justify-center">
    <p class="text-white/90 text-3xl text-center pointer-events-none">List of missions</p>
  </div>
  <a href="/dashboard/missions/add" class="cursor-pointer">
    <button class="flex bg-yellow-500 rounded-lg w-64 h-full text-white items-center justify-center hover:bg-yellow-400 hover:drop-shadow-sm duration-300">
      <p class="text-black text-xl text-center">Add Mission</p>
    </button>
  </a>
</div>
<div class="flex flex-col w-full h-fit gap-2 px-2 pb-2">
  <div class="flex flex-col w-full text-white/90 border-solid border-1">
    <div class="grid grid-flow-col grid-cols-6 w-full h-14 sticky top-16 items-center justify-center z-30 bg-[rgba(45,45,51,0.75)] backdrop-blur-md border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
      <p class="text-xl text-center pointer-events-none">Mission Id</p>
      <p class="text-xl text-center pointer-events-none">Operator Login</p>
      <p class="text-xl text-center pointer-events-none">Drone Name</p>
      <p class="text-xl text-center pointer-events-none">Mission Status</p>
      <p class="text-xl text-center pointer-events-none">Mission Created</p>
      <p class="text-xl text-center pointer-events-none">Actions</p>
    </div>
    <c:forEach var="mission" items="${missions}">
      <div class="grid grid-flow-col grid-cols-6 flex w-full h-14 items-center border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
        <div class="flex flex-grow h-14 items-center justify-center">
          <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${mission.id}" /></div>
        </div>
        <div class="flex flex-grow h-14 items-center justify-center">
          <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${mission.operator.login}" /></div>
        </div>
        <div class="flex flex-grow h-14 items-center justify-center">
          <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${mission.drone.name}" /></div>
        </div>
        <div class="flex flex-grow h-14 items-center justify-center">
          <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600">
            <c:choose>
              <c:when test="${not empty mission.finishedAt}">finished</c:when>
              <c:when test="${not empty mission.probeAt}">returning</c:when>
              <c:when test="${not empty mission.startedAt}">flying to the position</c:when>
              <c:otherwise>waiting to start</c:otherwise>
            </c:choose>
          </div>
        </div>
        <div class="flex flex-grow h-14 items-center justify-center">
          <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${mission.createdAt}" /></div>
        </div>
        <div class="flex flex-grow h-14 self-start items-center justify-center">
          <a class="flex bg-orange-500 rounded-lg w-48 h-10 cursor-pointer hover:bg-orange-400 hover:drop-shadow-sm duration-300 cursor-pointer items-center justify-center"  href="/dashboard/missions/view?id=<c:out value="${mission.id}" />">
            <p class="flex-grow cursor-pointer text-center">View</p>
          </a>
        </div>
      </div>
    </c:forEach>
  </div>
</div>
<div class="z-40 flex w-full h-16 p-2 justify-center">
  <a href="/dashboard/missions?page=<c:out value="${page - 1}" />" class="cursor-pointer <c:if test="${page <= 0}"> opacity-50 grayscale pointer-events-none selection-none</c:if>">
    <button class="flex bg-blue-500 rounded-lg w-16 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
      <p class="text-black text-xl text-center"><</p>
    </button>
  </a>
  <div class="flex px-8 items-center justify-center">
    <p class="text-white/90 text-3xl text-center pointer-events-none"><c:out value="${page + 1}" /></p>
  </div>
  <a href="/dashboard/missions?page=<c:out value="${page + 1}" />" class="cursor-pointer <c:if test="${page >= pages - 1}"> opacity-50 grayscale pointer-events-none selection-none</c:if>">
    <button class="flex bg-blue-500 rounded-lg w-16 h-full text-white items-center justify-center hover:bg-blue-400 hover:drop-shadow-sm duration-300">
      <p class="text-black text-xl text-center">></p>
    </button>
  </a>
</div>
</body>
</html>