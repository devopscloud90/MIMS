<%@page session="false" import="mims.beans.UserBean"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
    String id = (String)session.getAttribute("id");
    String displayName = "";
    if(id!=null){
      UserBean user = new UserBean(id);
      displayName = user.getName();
      if(displayName==null){
        displayName = "";
      }
      displayName = displayName.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
    %>
    <header class="app-header">
      <a class="app-brand" href="/MIMS/Login/Welcome.jsp" target="result">
        <span class="app-brand-mark" aria-hidden="true">M</span>
        <span>MICRO-INVENTIONS<br>MANAGEMENT SYSTEM</span>
      </a>
      <div class="app-header-info">
        <%if(!"".equals(displayName)){%>
          <span>Signed in as <strong><%=displayName%></strong></span>
        <%}%>
        <div class="app-header-actions">
          <a href="/MIMS/Login/Welcome.jsp" target="result">Home</a>
          <a href="/MIMS/Login/Logout.jsp" target="_top">Log out</a>
        </div>
      </div>
    </header>
  <%}%>
