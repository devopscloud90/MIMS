<%@page session="false" import="java.util.Calendar,mims.beans.UserBean"%>
<%
 HttpSession session = request.getSession(false);
 if(session!=null)
 {
 %>
  <html>
  <head>
   <title>Welcome Page</title>
   <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
  </head>
  <body class="BD">
  <%
    String id = (String)session.getAttribute("id");
    if(id!=null){
	  UserBean user = new UserBean(id);
      out.println("<h3>Welcome <font color='#990000'>"+user.getName()+"</font> to "+user.getUserType()+"'s interface.</h3>");
	}
  %>
  </body>
  </html>
  <%
  }else{%>
    <jsp:forward page="Login.jsp">
     <jsp:param name="error" value="Session has expired" />
    </jsp:forward>
<%}%>