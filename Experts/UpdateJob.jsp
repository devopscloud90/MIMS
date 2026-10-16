<%@page session="false" import="mims.beans.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
  String id = request.getParameter("id");
  %>
  <body class='BD'>
  <center>
  <%
   Statement stmt = DBUtil.createStatement("");
   ResultSet rs = stmt.executeQuery("select status from opportuinities where id='"+id+"'");
   String query = "update opportuinities set status='";
   if(rs.next()){
	 query += ("Open".equals(rs.getString(1).trim()))?"Closed":"Open";
   }
   query +="' where id='"+id+"'";
   int result = stmt.executeUpdate(query);
   rs.close();
   stmt.close();
   if(result==1){
	  %>
	 <jsp:forward page="ViewJobs.jsp">
	   <jsp:param name="msg" value="Success"/>
	 </jsp:forward>
   <%}else{%>
     <jsp:forward page="ViewJobs.jsp">
       <jsp:param name="msg" value="Failed"/>
     </jsp:forward>
   <%}%>
   </center>
   </body>
  <%}
%>