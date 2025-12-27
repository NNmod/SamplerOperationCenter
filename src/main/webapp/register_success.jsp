<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Sampler Operation Center</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="flex flex-col gap-4 w-full min-h-screen bg-white invert items-center justify-center">
<div class="flex flex-col w-full mb-1">
    <p class="text-white/90 text-3xl text-center pointer-events-none">You successfully registered new operator: <c:out value="${operator.login}" /></p>
    <p class="text-white/90 text-3xl text-center pointer-events-none">you can return to Home to process login to a system</p>
</div>
<a href="/" class="cursor-pointer h-14">
    <button class="flex bg-orange-500 rounded-lg w-64 h-14 h-full text-white items-center justify-center hover:bg-orange-400 hover:drop-shadow-sm duration-300">
        <p class="text-black text-xl text-center">Back to home</p>
    </button>
</a>
</body>
</html>