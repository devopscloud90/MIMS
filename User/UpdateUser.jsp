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
   ResultSet rs = stmt.executeQuery("select status from logindetails where id="+id);
   String query = "update logindetails set status='";
   String operation = "";
   if(rs.next()){
	 query += ("Active".equals(rs.getString(1).trim()))?"Inactive":"Active";
     operation = ("Active".equals(rs.getString(1).trim()))?"decrement":"increment";
   }
   query +="' where id="+id;
   int result = stmt.executeUpdate(query);
   rs.close();
   stmt.close();
   if(result==1){
	  DomainBean bean = new DomainBean(usertypeid);
	  if(usertype.equals("Advisor")){
        bean.updateAdvisorsCount(operation);
	  }else if(usertype.equals("Domain Expert")){
        bean.updateExpertsCount(operation);
	  }
	  %>
	 <jsp:forward page="ViewUsers.jsp">
	   <jsp:param name="msg" value="Success"/>
   	   <jsp:param name="usertype" value="<%=usertype%>"/>
       <jsp:param name="userid" value="<%=id%>"/>
	 </jsp:forward>
   <%}else{%>
     <jsp:forward page="ViewUsers.jsp">
       <jsp:param name="msg" value="Failed"/>
   	   <jsp:param name="usertype" value="<%=usertype%>"/>
	   <jsp:param name="userid" value="<%=id%>"/>
     </jsp:forward>
   <%}%>
   </center>
   </body>
  <%}
%>