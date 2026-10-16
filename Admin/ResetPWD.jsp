<%@page session="false" import="mims.beans.*,java.sql.*,java.util.ArrayList,java.util.HashMap"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String uid = (String)session.getAttribute("id");
	ArrayList<String> domains = DomainBean.getAllObjects();
    %>
    <head>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class='BD'>
    <center>
    <%	
	 ArrayList<String> users = UserBean.getAllObjects();
	 if(users!=null){
 	   %>
       List of All Available Users
	   <table class='notebook'>
		<tr class="row_title">
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
	      <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
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
		    <a href="ResetUser.jsp?id=<%=user.getId()%>" target="result">Reset</a>
		   </td>
		  </tr>
		  <%
		   rCount++;
		  }
		 %>
	  </table>
	<%}else{%>
	  <font color="#cc0000">No Users Found</font>
	<%}
	String msg = request.getParameter("msg");
	String userid = request.getParameter("userid");
	if("Success".equals(msg)){%>
      <script>alert("User's Password with id <%=userid%> Updated Successfully");</script>
    <%}else if ("Failed".equals(msg)){%>
      <script>alert("User's Password with id <%=userid%> Updation Failed");</script>
    <%}%>
</center>
</body>
<%}%>