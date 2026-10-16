<%@page session="false" import="mims.beans.*,java.util.ArrayList"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
  %>
  <head>
   <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
   <script type="text/javascript">
    history.go(+1);
   </script>
  </head>
  <body class='BD'>
  <center>
  <%
	ArrayList<String> domains = DomainBean.getAllObjects();
    if(domains!=null){
	 %>
      List of All Domains
	  <table class='notebook'>
		<tr class="row_title">
		  <th>ID</th>
		  <th>Name</th>
		  <th>Description</th>
		  <th>Advisors Count</th>
		  <th>Experts Count</th>
		  <th>Edit ?</th>
		</tr>
	 <%
	   int rCount=0;
	   for(String id:domains)
		{
		 DomainBean domain = new DomainBean(id);%>
		 <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		   <td align='center'><%=domain.getId()%></td>
		   <td align='center'><%=domain.getName()%></td>
           <td align='center'><%=domain.getDescription()%></td>
 		   <td align='center'><%=domain.getAdvisorsCount()%></td>
   		   <td align='center'><%=domain.getExpertsCount()%></td>
		   <td align='center'><a href="EditDomain.jsp?id=<%=domain.getId()%>" target="result" />Yes</td>
		 </tr>
		 <%
		   rCount++;
		 }%>
	 </table>
	<%}else{%>
	  <font color="#cc0000">No Domains Found</font>
	<%}
	String msg = request.getParameter("msg");
	String id = request.getParameter("id");
	if("Success".equals(msg)){%>
      <script>alert("Domain with id <%=id%> Updated Successfully");</script>
    <%}else if ("Failed".equals(msg)){%>
      <script>alert("Domain with id <%=id%> Updation Failed");</script>
    <%}%>
</center>
</body>
<%}%>