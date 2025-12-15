<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sampler Operation Center</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="flex flex-col gap-4 w-full min-h-screen bg-[rgba(45,45,51,1)] items-center justify-center">
<div class="flex flex-col w-full mb-1">
    <p class="text-white/90 text-3xl text-center pointer-events-none">Welcome, <c:out value="${operator.login}" /> to the Sampler Operation Center's dashboard<br/></p>
</div>
<div class="flex flex-col gap-2 mt-1 items-center justify-center">
    <a href="/dashboard/operators" class="cursor-pointer h-14">
        <button class="flex bg-yellow-500 rounded-lg w-64 h-14 h-full text-white items-center justify-center hover:bg-yellow-400 hover:drop-shadow-sm duration-300">
            <p class="text-black text-xl text-center">View operators</p>
        </button>
    </a>
    <a href="/dashboard/missions" class="cursor-pointer h-14">
        <button class="flex bg-yellow-500 rounded-lg w-64 h-14 h-full text-white items-center justify-center hover:bg-yellow-400 hover:drop-shadow-sm duration-300">
            <p class="text-black text-xl text-center">View missions</p>
        </button>
    </a>
    <a href="/dashboard/drones" class="cursor-pointer h-14">
        <button class="flex bg-yellow-500 rounded-lg w-64 h-14 h-full text-white items-center justify-center hover:bg-yellow-400 hover:drop-shadow-sm duration-300">
            <p class="text-black text-xl text-center">View drones</p>
        </button>
    </a>
</div>
</body>
</html>