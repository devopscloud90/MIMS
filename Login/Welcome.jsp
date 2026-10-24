<%@page session="false" import="mims.beans.UserBean"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
    String id = (String)session.getAttribute("id");
    if(id!=null){
      UserBean user = new UserBean(id);
      String userName = user.getName();
      if(userName==null){
        userName = "";
      }
      userName = userName.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
      String userType = user.getUserType();
      if(userType==null){
        userType = "";
      }
      userType = userType.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
      %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MIMS - Welcome</title>
  <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
</head>
<body class="welcome-page">
  <main class="welcome-card">
    <span class="eyebrow">Your workspace</span>
    <h1>Welcome, <%=userName%></h1>
    <p>You are signed in to the <strong><%=userType%></strong> workspace. Use the navigation to get started.</p>
  </main>
</body>
</html>
      <%}else{%>
        <jsp:forward page="Login.jsp">
          <jsp:param name="error" value="Session has expired" />
        </jsp:forward>
      <%}
    }else{%>
      <jsp:forward page="Login.jsp">
        <jsp:param name="error" value="Session has expired" />
      </jsp:forward>
    <%}%>
