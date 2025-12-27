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
  <p class="text-white/90 text-3xl text-center pointer-events-none">Register to Sampler Operation Center</p>
</div>
<form class="flex flex-col gap-2 mt-1 items-center justify-center bg-gray-200/20 rounded-xl py-2 pt-4 px-2" method="post" action="${pageContext.request.contextPath}/register">
  <div class="flex flex-col flex-grow w-64 items-center justify-center">
    <p class="flex w-full text-start text-md text-white/90">login</p>
    <input class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400" type="text" name="login" required />
  </div>
  <div class="flex flex-col flex-grow w-64 items-center justify-center">
    <p class="flex w-full text-start text-md text-white/90">password</p>
    <input class="text-xl text-start bg-transparent w-full h-10 text-white/90 outline-none focus:outline-none focus:ring-0 border-b-[2px] border-blue-400" type="text" name="password" required />
  </div>
  <div class="flex flex-grow h-14 items-center justify-center">
    <div class="flex bg-blue-500 rounded-lg w-64 h-14 cursor-pointer hover:bg-blue-400 hover:drop-shadow-sm duration-300 cursor-pointer">
      <input class="flex-grow cursor-pointer" type="submit" value="Register" />
    </div>
  </div>
</form>
</body>
</html>