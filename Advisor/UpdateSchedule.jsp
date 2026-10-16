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
    <form name="f1" id="f1" action="UpdateSchedule.jsp" onsubmit="return validate()">

	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      <%
    	if(reqId != null ){
	      req = new RequestBean(reqId);
		  if(!"Approved".equals(req.getStatus())){%>
      <tr>
        <th colspan = "2" align="center" class="labeltext">
          Shedule Results
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
         <td>Decision </td>
		 <td>
		   <select name="decision" id="decision">
		    <option value="--">--Select--</option>
		    <option value="Approved">Approve</option>
            <option value="Rejected">Reject</option>
		   </select>
         </td>
       </tr>

       <tr>
         <td>Comments </td>
		 <td>
		   <textarea name="comments" id="comments" rows="3" cols="25"></textarea>
         </td>
       </tr>
       
       <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="SAVE" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
       </tr>
    <%}else{%>
        <jsp:forward page="ViewSchedules.jsp">
	      <jsp:param name="msg" value="Error"/>
		  <jsp:param name="ok" value=""/>
	    </jsp:forward>
      
	   <%}}
    reqId = request.getParameter("reqId"); 
	String name = request.getParameter("reqBy");
    String phno = request.getParameter("phno");
    String email = request.getParameter("email");
    String address = request.getParameter("address");
    String decision = request.getParameter("decision");
    String comments = request.getParameter("comments");
	Integer userId = UserBean.generateId();
    String buttons = request.getParameter("ok");
	String msg = request.getParameter("msg");
	if("SAVE".equals(buttons)){
      Statement stmt = DBUtil.createStatement(""); 
      int n = stmt.executeUpdate("update requests set comments='"+comments+"' ,status='"+decision+"',userid='"+userId+"' where id='"+reqId+"'");
	  if(n==1){
 	    if("Approved".equals(decision)){
			n = stmt.executeUpdate("insert into userdetails values("+userId+",'"+name+"','"+phno+"','"+email+"','"+address+"')");
		}
	  %>
	  <jsp:forward page="ViewSchedules.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="ViewSchedules.jsp">
	    <jsp:param name="msg" value="Failed" />
    	<jsp:param name="ok" value=""/>
	  </jsp:forward>
    <%}
    }}%>