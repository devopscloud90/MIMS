<%@page session="false" import="mims.beans.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
    String sessionUserType = (String) session.getAttribute("usertype");
    if(!"Admin".equals(sessionUserType)){
      response.sendRedirect("/MIMS/Login/Login.jsp?error=Unauthorized Access");
      return;
    }
    String id = request.getParameter("id");
    String usertype = request.getParameter("usertype");
    String usertypeid = request.getParameter("usertypeid");
    if(id==null || id.trim().isEmpty()){
      response.sendRedirect("/MIMS/User/ViewUsers.jsp?usertype=" + ((usertype!=null)?usertype:"Advisor") + "&msg=Failed");
      return;
    }
    %>
    <!DOCTYPE html>
    <html lang="en">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>MIMS - Reset Password</title>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class="reset-page">
      <main class="reset-shell">
        <section class="reset-card" aria-labelledby="reset-title">
          <header class="reset-header">
            <span class="brand-badge">MIMS</span>
            <span class="status-chip">Admin</span>
          </header>
          <h1 id="reset-title">Password Reset</h1>
          <p>We are resetting the login credentials for user ID <strong><%=id%></strong>. This process only takes a moment.</p>
          <div class="reset-loader" role="status" aria-label="Processing password reset"></div>
        </section>
      </main>
      <%
       PreparedStatement selectStmt = null;
       PreparedStatement updateStmt = null;
       ResultSet rs = null;
       try{
         selectStmt = DBUtil.prepareStatement("select initpassword from logindetails where id=?");
         selectStmt.setString(1, id);
         rs = selectStmt.executeQuery();
         String initialPassword = "";
         if(rs.next()){
           initialPassword = rs.getString(1);
         }
         updateStmt = DBUtil.prepareStatement("update logindetails set password=? where id=?");
         updateStmt.setString(1, initialPassword);
         updateStmt.setString(2, id);
         int result = updateStmt.executeUpdate();
         if(result==1){
      %>
      <jsp:forward page="ResetPWD.jsp">
        <jsp:param name="msg" value="Success"/>
        <jsp:param name="userid" value="<%=id%>"/>
      </jsp:forward>
      <%}else{%>
        <jsp:forward page="ResetPWD.jsp">
          <jsp:param name="msg" value="Failed"/>
          <jsp:param name="userid" value="<%=id%>"/>
        </jsp:forward>
      <%}
       } finally {
          try{ if(rs != null) rs.close(); } catch(Exception e){ }
          try{ if(selectStmt != null) selectStmt.close(); } catch(Exception e){ }
          try{ if(updateStmt != null) updateStmt.close(); } catch(Exception e){ }
       }
      %>
    </body>
    </html>
  <%}
%>