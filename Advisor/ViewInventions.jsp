<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
	UserBean user = new UserBean(userid);
    %>
    <head>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class='BD'>
    <center>
    <%	
     ArrayList<String> inventions = InventionBean.getObjectsByDomain(user.getUserTypeId());
	 if(inventions!=null){
 	   %>
       List of Inventions Done 
       <table class='notebook'>
		<tr class="row_title">
		  <th>ID</th>
		  <th>Title</th>
		  <th>Description</th>
		  <th>Domain</th>
		  <th>Inventor</th>
		  <th>Status</th>
		</tr>
	    <%
	     int rCount=0;
	     for (String id:inventions){
           InventionBean invention = new InventionBean(id);
		  %>
	      <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		   <td align='center'><%=invention.getId()%></td>
		   <td align='center'><%=invention.getTitle()%></td>
		   <td align='center'><%=invention.getThesis()%></td>
  		   <td align='center'><%=invention.getDomainName()%></td>
           <td align='center'><%=invention.getUserName()%></td>
  		   <td align='center'><%=invention.getStatus()%></td>
		  </tr>
		  <%
		   rCount++;
		  }%>
	  </table>
	<%}else{%>
	  <font color="#cc0000">No Inventions Found</font>
</center>
</body>
<%}}%>