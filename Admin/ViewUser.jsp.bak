<%@page session="false" import="mims.beans.*,java.sql.*,java.util.*"%>
<%
 String id = request.getParameter("id");
 UserBean user = new UserBean(id);
 if(user!=null){
 %>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    <script language="JavaScript1.2" src="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
   <table align = "center" border="1" cellpadding="10pt" cellspacing="0pt">
    <tr>
      <th colspan = "2" align="center" class="labeltext">
         The following are the <%=user.getName()%> details
      </th>
    </tr>

    <tr>
      <td>User Id </td><td><%=user.getId()%></td>
    </tr>

    <tr>
      <td>Name</td><td><%=user.getName()%></td>
    </tr>

    <tr>
      <td>Phone Number</td><td><%=user.getMobile()%></td>
    </tr>

    <tr>
      <td>Email</td><td><%=user.getEmail()%></td>
    </tr>

    <tr>
      <td>Address</td><td><%=user.getAddress()%></td>
    </tr>
    
    </table>
   </form>
   </center>
  </body>
  <%}%>