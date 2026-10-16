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
     ArrayList<String> jobs = OppBean.getObjectsByDomain(user.getUserTypeId());
	 if(jobs!=null){
 	   %>
       List of Jobs Posted in Your domain 
       <table class='notebook'>
		<tr class="row_title">
		  <th>ID</th>
		  <th>Job</th>
		  <th>Description</th>
		  <th>Status</th>
          <th>Set Status to</th>
		</tr>
	    <%
	     int rCount=0;
	     for (String id:jobs){
           OppBean job = new OppBean(id);
		  %>
	      <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		   <td align='center'><%=job.getId()%></td>
		   <td align='center'><%=job.getName()%></td>
		   <td align='center'><%=job.getDescription()%></td>
  		   <td align='center'><%=job.getStatus()%></td>
           <td align='center'>
		    <a href="UpdateJob.jsp?id=<%=job.getId()%>" target="result"><%="Open".equals(job.getStatus())?"Closed":"Open"%></a>
		   </td>
		  </tr>
		  <%
		   rCount++;
		  }%>
	  </table>
	<%}else{%>
	  <font color="#cc0000">No Jobs Found</font>
	<%}
	String msg = request.getParameter("msg");
	if("Success".equals(msg)){%>
      <script>alert("Job Updated Successfully");</script>
    <%}else if ("Failed".equals(msg)){%>
      <script>alert("Job Updation Failed");</script>
    <%}%>
</center>
</body>
<%}%>