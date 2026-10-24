<%@page session="false" language="java"%>
<%!
  private String escapeHtml(String value){
    if(value==null){
      return "";
    }
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
  <script type="text/javascript" src="/MIMS/Scripts/validate.js"></script>
  <script type="text/javascript">
    function validate(){
      var uid = document.getElementById("UserId");
      var pwd = document.getElementById("Password");
      return !isEmpty(uid) && !isEmpty(pwd);
    }
  </script>
  <title>MIMS - Sign in</title>
</head>
<body class="login-page">
  <header class="login-topbar">
    <a class="app-brand" href="/MIMS/Login/Login.jsp">
      <span class="app-brand-mark" aria-hidden="true">M</span>
      <span>MICRO-INVENTIONS<br>MANAGEMENT SYSTEM</span>
    </a>
    <a href="/MIMS/Inventor/AddRequest.jsp">Submit an invention request</a>
  </header>
  <main class="login-shell">
    <span class="eyebrow">Welcome to MIMS</span>
    <h1>Sign in to your account</h1>
    <p class="login-intro">Access your workspace to manage inventions, reviews, and requests.</p>
    <form class="login-form" id="loginfrm" method="POST" action="/MIMS/login" onsubmit="return validate()">
      <label class="login-field" for="UserId">
        User ID
        <input type="text" name="UserId" id="UserId" autocomplete="username" required autofocus>
        <span id="UserIdError" class="error"></span>
      </label>
      <label class="login-field" for="Password">
        Password
        <input type="password" name="Password" id="Password" autocomplete="current-password" required>
        <span id="PasswordError" class="error"></span>
      </label>
      <div class="login-actions">
        <input type="submit" name="ok" id="ok" value="Sign in">
        <input type="reset" name="cancel" id="cancel" value="Clear">
      </div>
    </form>
    <div class="login-message" aria-live="polite">
      <%
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        String auth = request.getParameter("auth");
        if(error!=null){%>
          <h2 class="err"><%=escapeHtml(error)%></h2>
        <%}else if(success!=null){%>
          <h2 class="suc"><%=escapeHtml(success)%></h2>
        <%}else if(auth!=null){%>
          <h2 class="auth"><%=escapeHtml(auth)%></h2>
        <%}
      %>
    </div>
  </main>
</body>
</html>