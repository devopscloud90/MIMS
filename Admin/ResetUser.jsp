<%@page session="false" import="mims.beans.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
  String id = request.getParameter("id");
  String usertype = request.getParameter("usertype");
  String usertypeid = request.getParameter("usertypeid");
  %>
  <body class='BD'>
  <center>
  <%
   Statement stmt = DBUtil.createStatement("");
   ResultSet rs = stmt.executeQuery("select initpassword from logindetails where id="+id);
   String query = "update logindetails set password='";
   if(rs.next()){
	 query += rs.getString(1);
   }
   query +="' where id="+id;
   int result = stmt.executeUpdate(query);
   rs.close();
   stmt.close();
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
   <%}%>
   </center>
   </body>
  <%}
%>