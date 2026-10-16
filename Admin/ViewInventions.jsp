<%@page import="mims.beans.*,java.sql.*,java.util.ArrayList,java.util.HashMap"%>
<%
	String domain = request.getParameter("domain");
    %>
    <head>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class='BD'>
    <center>
    <%	
     ArrayList<String> inventions = InventionBean.getObjectsByDomain(domain);
	 if(inventions!=null){
 	   %>
       List of Inventions Registered 
       <table class='notebook'>
		<tr class="row_title">
		  <th>ID</th>
		  <th>Title</th>
		  <th>Description</th>
		  <th>Domain</th>
		  <th>Inventor</th>
          <th>Registered Date</th>
		  <th>Patented Date</th>
		  <th>Status</th>
          <th>Reviews</th>
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
           <td align='center'><%=invention.getRegisteredDate()%></td>
           <td align='center'><%=invention.getPatentDate()%></td>
  		   <td align='center'><%=invention.getStatus()%></td>
		   <td align='center'>
		     <a href="ViewReviews.jsp?id=<%=invention.getId()%>" target="myframe">View</a>
		   </td>
		  </tr>
		  <%
		   rCount++;
		  }%>
	  </table>
	<%}else{%>
	  <font color="#cc0000">No Inventions Found</font>
</center>
</body>
<%}%>