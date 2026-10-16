<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
    UserBean user = new UserBean(userid);
	
	%>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" /> 
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
    <form name="f1" id="f1" action="PostJobs.jsp" onsubmit="return validate()">
      <input type="hidden" id="domainId" name="domainId" value="<%=user.getUserTypeId()%>"/>

	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      
	  <tr>
        <th colspan = "2" align="center" class="labeltext">
          Enter Opportuinities in Your Domain
        </th>
      </tr>

      <tr>
        <td>Id</td>
        <td><input name="oppId" id="oppId" type="text" readonly="readonly" value="<%=OppBean.generateId()%>"><td>
      </tr>

      <tr>
        <td>Opportuinity</td>
        <td><input name="offer" id="offer" type="text"><td>
      </tr>
	  
	  <tr>
        <td>Description</td>
        <td><input name="desc" id="desc"  type="text"><td>
      </tr>
	  
	  <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="POST" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
      </tr>
    <%
    String oppId = request.getParameter("oppId"); 
    String domainId = request.getParameter("domainId"); 
	String offer = request.getParameter("offer");
	String desc = request.getParameter("desc");
    String buttons = request.getParameter("ok");
	String msg = request.getParameter("msg");
	if("POST".equals(buttons)){
      Statement stmt = DBUtil.createStatement("");
	  String query = "insert into opportuinities values ('"+oppId+"',"+domainId+",'"+offer+"','"+desc+"','New')";
      int n = stmt.executeUpdate(query);
	  if(n==1){
	  %>
	  <jsp:forward page="PostJobs.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="PostJobs.jsp">
	    <jsp:param name="msg" value="Failed" />
    	<jsp:param name="ok" value=""/>
	  </jsp:forward>
    <%}
   }
   if("Success".equals(msg)){%>
      <script>alert("Job Posted Successfully");</script>
   <%}else if ("Failed".equals(msg)){%>
      <script>alert("Job Posting Failed");</script>
  <%}
}%>