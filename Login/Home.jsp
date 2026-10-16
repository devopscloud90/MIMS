<%@page session="false"%>
<%
 HttpSession session = request.getSession(false);
 if(session!=null)
 {
 %>
 <head>
 <title>..::Micro-Inventions Management System::..</title>
  <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
 </head>
 <body class="SC">
 <table width="100%" height="100%">
 <tr>
   <td height="18%"><jsp:include page="Head.jsp" /></td>
 </tr>
 <tr>
   <td height="82%">
     <iframe name="main" id="main" src="/MIMS/Login/Main.jsp" width="100%" height="100%" frameborder="0" />
   </td>
 </tr>
 </table>
 </body>
 <%
 }else{%>
  <jsp:forward page="Login.jsp">
   <jsp:param name="error" value="First Login to the System" />
  </jsp:forward>
<%}%>