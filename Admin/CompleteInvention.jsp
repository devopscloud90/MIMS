<%@page session="false" import="mims.beans.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
  String id = request.getParameter("id");
  String domain = request.getParameter("domain");
  %>
  <body class='BD'>
  <center>
  <%
   Statement stmt = DBUtil.createStatement("");
   String query = "update inventions set status='Completed',patentdate='";
   query += UserBean.getTodayDate();
   query += "' where id='"+id+"'";
   int result = stmt.executeUpdate(query);
   stmt.close();
   if(result==1){%>
	 <jsp:forward page="ViewInventions.jsp">
	   <jsp:param name="domain" value="<%=domain%>"/>
	 </jsp:forward>
   <%}else{%>
     <jsp:forward page="ViewInventions.jsp">
   	   <jsp:param name="domain" value="<%=domain%>"/>
     </jsp:forward>
   <%}%>
   </center>
   </body>
  <%}
%>