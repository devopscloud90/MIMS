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
   ArrayList<String> requests = RequestBean.getObjectsByReviewer(userid);
   if(requests!=null){%>
   List of Approved Requests 
	<table class='notebook'>
	  <tr class="row_title">
		<th>Request Id</th>
	    <th>Requestor Name</th>
	    <th>Phone Number</th>
		<th>Email</th>
		<th>Address</th>
        <th>Review Date</th>
        <th>Review Comments</th>
    	<th>Status</th>
        <th>Add Invention?</th>
	  </tr>
	  <%
	  int rCount=0;
	  for(String id:requests)
	  { 
	    RequestBean req = new RequestBean(id);
		if("Approved".equals(req.getStatus())){%>
	    <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		  <td align='center'><%=req.getId()%></td>
		  <td align='center'><%=req.getName()%></td>
		  <td align='center'><%=req.getMobile()%></td>
  		  <td align='center'><%=req.getEmail()%></td>
  		  <td align='center'><%=req.getAddress()%></td>
          <td align='center'><%=req.getReviewDate()%></td>
          <td align='center'><%=req.getComments()%></td>
          <td align='center'><%=req.getStatus()%></td>
  		  <td align='center'><a href="AddInvention.jsp?reqid=<%=req.getId()%>" target="result">Yes</a></td>		 
	    </tr>
	    <%
	    rCount++;
	    }		
	  }if(rCount==0){%> 
        <tr><td colspan="9" align="center"><font color="#cc0000">No Approved Requests Found</font></td></tr>
	  <%}%>
	</table>
   <%}else{%>
     <font color="#cc0000">No Requests Found</font>
   <%}
	String msg = request.getParameter("msg");
   if("Success".equals(msg)){%>
      <script>alert("Schedule Results Saved Successfully");</script>
   <%}else if ("Failed".equals(msg)){%>
      <script>alert("Schedule Results Saving Failed");</script>
  <%}else if ("Error".equals(msg)){%>
      <script>alert("Cannot Edit Approved/Rejected Details");</script>
  <%}%>
  </center></body>
<%}%>