<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
	%>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />    
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
  <%
   ArrayList<String> requests = RequestBean.getObjectsByStatus("New");
   if(requests!=null){%>
   List of New Requests 
	<table class='notebook'>
	  <tr class="row_title">
		<th>Request Id</th>
	    <th>Requestor Name</th>
	    <th>Phone Number</th>
		<th>Email</th>
		<th>Address</th>
        <th>Domain</th>
    	<th>Status</th>
        <th>Process?</th>
	  </tr>
	  <%
	  int rCount=0;
	  for(String id:requests)
	  { 
	    RequestBean req = new RequestBean(id);%>
	    <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		  <td align='center'><%=req.getId()%></td>
		  <td align='center'><%=req.getName()%></td>
		  <td align='center'><%=req.getMobile()%></td>
  		  <td align='center'><%=req.getEmail()%></td>
  		  <td align='center'><%=req.getAddress()%></td>
          <td align='center'><%=req.getDomainName()%></td>
          <td align='center'><%=req.getStatus()%></td>
  		  <td align='center'><a href="UpdateRequest.jsp?reqid=<%=req.getId()%>" target="result">Yes</a></td>		 
	    </tr>
	  <%rCount++;
	  }%>
	</table>
   <%}else{%>
     <font color="#cc0000">No Requests Found</font>
   <%}%>
  </center></body>
<%}%>