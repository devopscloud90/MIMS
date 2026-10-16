<%@page import="mims.beans.*,java.sql.*,java.util.ArrayList,java.util.HashMap"%>
<%
    String usertype = request.getParameter("usertype");
    %>
    <head>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class='BD'>
    <center>
    <%	
	 ArrayList<String> users = UserBean.getObjectsByUserType(usertype);
	 if(users!=null){
 	   %>
       List of 
	   <%if("Admin".equals(usertype))
	      out.println("Administrators");
         else
		  if("Advisor".equals(usertype))
  	       out.println("Advisors");
         else
		  if("Domain Expert".equals(usertype)){
  	       out.println("Domain Experts");
		  }else
		    if("Inventor".equals(usertype))
  	          out.println("Inventors");
          %>
       <table class='notebook'>
		<tr class="row_title">
		  <th>ID</th>
		  <th>Name</th>
		  <th>Phone Number</th>
		  <th>Email Id</th>
		  <th>Address</th>
		  <th>Domain</th>
		  <th>Status</th>
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
		    <%DomainBean bean = new DomainBean(user.getUserTypeId());%><%=bean.getName()%></td>
  		   <td align='center'><%=user.getStatus()%></td>
		  </tr>
		  <%
		   rCount++;
		  }%>
	  </table>
	<%}else{%>
	  <font color="#cc0000">No Users Found</font>
</center>
</body>
<%}%>