<%@page session="false" import="cctbm.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
    String usertype = (String)session.getAttribute("usertype");
	if(usertype.equals("BO"))
		userid="0";
	%>
  <head>
    <link href="/CCTBM/CSS/styles.css" type="text/css" rel="stylesheet" />    
    <script LANGUAGE="Javascript" src="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
  <%
   ArrayList<String> requests = Requests.getObjectsByRequestTo(userid);
   if(requests!=null){
    ArrayList<String> filteredrequests = Requests.filterRequests(requests,"New");
    requests = filteredrequests;
	if(requests.size()!=0){
	%>
   List of requests send to you
	<table class='notebook'>
	  <tr class="row_title">
		<th>Requested By</th>
	    <th>Requested Date</th>
	    <th>Requested Amount</th>
		<th>Description</th>
		<th>Processed Date</th>
		<th>Processed Comments</th>
    	<th>Status</th>
    	<th>Process</th>
	  </tr>
	  <%
	  int rCount=0;
	  for(String id:requests)
	  { 
	    Requests req = new Requests(id);%>
	    <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		  <td align='center'><%=req.getRequestByName()%></td>
		  <td align='center'><%=req.getRequestDate()%></td>
		  <td align='center'><%=req.getRequestAmount()%></td>
  		  <td align='center'><%=req.getDescription()%></td>
  		  <td align='center'><%=req.getActionDate()%></td>
          <td align='center'><%=req.getActionReason()%></td>
  		  <td align='center'><%=req.getStatus()%></td>	
		  <td align='center'><a href="UpdateRequest.jsp?reqId=<%=req.getId()%>">YES</a></td>
	    </tr>
	  <%rCount++;
	  }%>
	</table>
   <%}else{%>
     <font color="#cc0000">No Pending Requests Found</font>
   <%}}else{%>
     <font color="#cc0000">No Requests Found</font>
   <%}
   String msg = request.getParameter("msg");
   if("Success".equals(msg)){%>
      <script>alert("Requested Processed");</script>
   <%}else if ("Failed".equals(msg)){%>
      <script>alert("Request Processing Failed");</script>
  <%}%>
  </center></body>
<%}%>