<%@page import="mims.beans.*,java.util.*"%>
<head>
 <LINK href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
</head>
<body class="BD">
<center>
<%
  String invid = request.getParameter("invid");
  String domain = request.getParameter("domain");
  String userId = request.getParameter("userId");
  String usertype = request.getParameter("utype");
  String button = request.getParameter("ok");
  String query = "";
  if("GET DETAILS".equals(button)){
  if(!"--".equals(invid) && "--".equals(domain) && "--".equals(userId) && "--".equals(usertype)){
	  %>
	   <jsp:include page="ViewInvention.jsp">
	    <jsp:param name="invid" value="<%=invid%>" />
	   </jsp:include>
	  <%
  }
  else
   if("--".equals(invid) && !"--".equals(domain) && "--".equals(userId) && "--".equals(usertype)){
	  %>
	   <jsp:include page="ViewInventions.jsp">
	     <jsp:param name="domain" value="<%=domain%>" />
	   </jsp:include>
	  <%
    }
   else
   if("--".equals(invid) && "--".equals(domain) && !"--".equals(userId) && "--".equals(usertype)){
	  %>
	 <jsp:include page="ViewUser.jsp">
	    <jsp:param name="id" value="<%=userId%>" />
	  </jsp:include>
   <%}
   else
   if("--".equals(invid) && "--".equals(domain) && "--".equals(userId) && !"--".equals(usertype)){
	  %>
      <jsp:include page="ViewUsers.jsp">
	    <jsp:param name="usertype" value="<%=usertype%>" />
	  </jsp:include>
   <%}
   else{
      out.println("<font color='#dd0000'>Invalid Filters</font>");
   }
   }else{
     out.println("<font color='#FFFF00'>Please Use the above filters to view Details</font>");
   }%>
</center>
</body>