<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Operators - Sampler Operation Center</title>
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
        <p class="text-white/90 text-3xl text-center pointer-events-none">List of operators</p>
    </div>
    <div class="w-64"></div>
</div>
<div class="flex flex-col w-full h-fit gap-2 px-2 pb-2">
    <div class="flex flex-col w-full text-white/90 border-solid border-1">
        <div class="grid grid-flow-col grid-cols-3 w-full h-14 sticky top-16 items-center justify-center z-30 bg-[rgba(45,45,51,0.75)] backdrop-blur-md border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
            <p class="text-xl text-center pointer-events-none">Operator Id</p>
            <p class="text-xl text-center pointer-events-none">Operator Login</p>
            <p class="text-xl text-center pointer-events-none">Operator Register DateTime</p>
        </div>
        <c:forEach var="operator" items="${operators}">
        <div class="grid grid-flow-col grid-cols-3 flex w-full h-14 items-center border-solid border-b-[1px] border-[rgba(71,71,74,255)]">
            <div class="flex flex-grow h-14 items-center justify-center">
                <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${operator.id}" /></div>
            </div>
            <div class="flex flex-grow h-14 items-center justify-center">
                <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${operator.login}" /></div>
            </div>
            <div class="flex flex-grow h-14 items-center justify-center">
                <div class="text-xl text-center bg-transparent border-b-[2px] border-gray-600"><c:out value="${operator.createdAt}" /></div>
            </div>
        </div>
        </c:forEach>
    </div>
</div>
</body>
</html>