<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String reqTo = (String)session.getAttribute("id");
    String reqId = request.getParameter("reqid");
	RequestBean req = null;
	%>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" /> 
	<link href="/MIMS/CSS/DatePicker.css" type="text/css" rel="stylesheet" /> 
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/DatePicker.js"></script>
  </head>
  <body class="BD">
  <center>
    <form name="f1" id="f1" action="UpdateRequest.jsp" onsubmit="return validate()">

	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      <%
    	if(reqId != null ){
	      req = new RequestBean(reqId);
	  %>  
      <tr>
        <th colspan = "2" align="center" class="labeltext">
          Process the Request
        </th>
      </tr>

      <tr>
        <td>Request Id</td>
	    <td><input type="text" id="reqId" name="reqId" readonly="readonly" value="<%=req.getId()%>"/></td>
       </tr>

       <tr>
         <td>Requestor Name</td>
	     <td><input type="text" id="reqBy" name="reqBy" readonly="readonly" value="<%=req.getName()%>"/></td>
       </tr>
  
       <tr>
         <td>Mobile No</td>
		 <td><input type="text" name="phno" id="phno" readonly="readonly" value="<%=req.getMobile()%>"></td>
       </tr>

       <tr>
         <td>Email </td>
		 <td><input type="text" name="email" id="email" readonly="readonly" value="<%=req.getEmail()%>"></td>
       </tr>

       <tr>
         <td>Address </td>
		 <td><input type="text" name="address" id="address" readonly="readonly" value="<%=req.getAddress()%>"></td>
       </tr>

	   <tr>
         <td>Domain</td>
		 <td><input type="text" name="domain" id="domain" readonly="readonly" value="<%=req.getDomainName()%>"></td>
       </tr>

	   <tr>
         <td>Review Date </td>
		 <td><input type="text" name="reviewdate" id="reviewdate" readonly="readonly" onclick="displayDatePicker('reviewdate',false,'dmy','-');"></td>
       </tr>

       <tr>
         <td>Reviewer </td>
		 <td>
		   <select name="reviewer" id="reviewer">
		    <option value="--">--Select--</option>
		   <%
		     ArrayList<String> list = UserBean.getObjectsByUserType("Advisor");
	         if(list != null){
			  for(String id : list){
				 UserBean user = new UserBean(id);
				  %>
				  <option value="<%=user.getId()%>"><%=user.getName()%></option>
			  <%}
			 }
		   %>
         </td>
       </tr>
       
       <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="SCHEDULE" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
       </tr>
    <%}
    reqId = request.getParameter("reqId"); 
    String reviewdate = request.getParameter("reviewdate");
    String reviewer = request.getParameter("reviewer");
    String buttons = request.getParameter("ok");
	String msg = request.getParameter("msg");
	if("SCHEDULE".equals(buttons)){
      Statement stmt = DBUtil.createStatement(""); 
      int n = stmt.executeUpdate("update requests set reviewdate='"+reviewdate+"' ,reviewerid='"+reviewer+"' ,status='Scheduled' where id='"+reqId+"'");
	  if(n==1){
	  %>
	  <jsp:forward page="UpdateRequest.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="UpdateRequest.jsp">
	    <jsp:param name="msg" value="Failed" />
    	<jsp:param name="ok" value=""/>
	  </jsp:forward>
    <%}
    }
   if("Success".equals(msg)){%>
      <script>alert("Request Scheduled Successfully");</script>
   <%}else if ("Failed".equals(msg)){%>
      <script>alert("Request Schedule Failed");</script>
  <%}}%>