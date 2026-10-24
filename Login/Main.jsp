<%@page session="false" import="mims.beans.UserBean"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
  %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MIMS - Workspace</title>
  <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
</head>
<body class="app-page">
  <div class="app-main">
    <aside class="app-sidebar">
      <jsp:include page="Menu.jsp" />
    </aside>
    <main class="app-content">
      <iframe id="result" name="result" src="/MIMS/Login/Welcome.jsp" title="MIMS workspace content"></iframe>
    </main>
  </div>
</body>
</html>
  <%}else{%>
    <jsp:forward page="Login.jsp">
      <jsp:param name="error" value="Session has expired" />
    </jsp:forward>
  <%}%>
