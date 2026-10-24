<%@page session="false" import="mims.beans.*,java.sql.*,java.util.ArrayList,java.util.HashMap"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
    String uid = (String)session.getAttribute("id");
    ArrayList<String> domains = DomainBean.getAllObjects();
    %>
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>MIMS - User Management</title>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class="dashboard-page">
      <div class="dashboard-shell">
        <div class="page-header">
          <div>
            <span class="eyebrow">MIMS</span>
            <h1>Available Users</h1>
          </div>
          <span class="status-chip">Admin access</span>
        </div>

        <div class="panel">
          <div class="panel-header">
            <span>System users</span>
          </div>
          <%
             ArrayList<String> users = UserBean.getAllObjects();
             if(users!=null && !users.isEmpty()){
          %>
          <div class="table-wrap">
            <table class="data-table">
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Phone Number</th>
                <th>Email</th>
                <th>Address</th>
                <th>Domain</th>
                <th>Status</th>
                <th>Operation</th>
              </tr>
              <%
                 int rCount=0;
                 for (String id:users){
                  UserBean user = new UserBean(id);
              %>
              <tr class="<%=(rCount%2!=0)? "row_even" : "row_odd"%>">
                <td align='center'><%=user.getId()%></td>
                <td align='center'><%=user.getName()%></td>
                <td align='center'><%=user.getMobile()%></td>
                <td align='center'><%=user.getEmail()%></td>
                <td align='center'><%=user.getAddress()%></td>
                <td align='center'>
                  <%DomainBean domain = new DomainBean(user.getUserTypeId());%>
                  <%=domain.getName()%>
                </td>
                <td align='center'><%=user.getStatus()%></td>
                <td align='center'>
                  <a class="action-link" href="ResetUser.jsp?id=<%=user.getId()%>" target="result">Reset</a>
                </td>
              </tr>
              <%
                 rCount++;
                 }
              %>
            </table>
          </div>
          <% } else { %>
            <p class="error">No Users Found</p>
          <% } %>
        </div>
      </div>

      <%
        String msg = request.getParameter("msg");
        String userid = request.getParameter("userid");
        if("Success".equals(msg)){%>
          <script>alert("User's Password with id <%=userid%> Updated Successfully");</script>
        <%}else if ("Failed".equals(msg)){%>
          <script>alert("User's Password with id <%=userid%> Updation Failed");</script>
        <%}
      %>
    </body>
    </html>
  <%}
%>