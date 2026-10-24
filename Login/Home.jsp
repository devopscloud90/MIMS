<%@page session="false"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
  %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MIMS - Micro-Inventions Management System</title>
  <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
</head>
<body class="app-page">
  <div class="app-shell">
    <jsp:include page="Head.jsp" />
    <main class="home-main">
      <iframe class="home-frame" name="main" id="main" src="/MIMS/Login/Main.jsp" title="MIMS application"></iframe>
    </main>
  </div>
</body>
</html>
  <%}else{%>
    <jsp:forward page="Login.jsp">
      <jsp:param name="error" value="First Login to the System" />
    </jsp:forward>
  <%}%>
